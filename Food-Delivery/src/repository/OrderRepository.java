package repository;

import exception.DoubleAssignmentException;
import exception.OptimisticLockException;
import exception.OrderCancellationException;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import repository.lock.FileLockManager;
import repository.lock.LockMechanism;
import repository.lock.ResourceLockManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * OrderRepository - HOT SPOT #1 (orders.csv).
 * Schema: orderId,customerId,restaurantId,driverId,status,totalAmount,createdAt,version
 *
 * Toàn bộ logic gán đơn / hủy đơn / hết hạn đơn nằm ở đây (không đặt ở Controller/View).
 * Hỗ trợ đủ 4 cơ chế: NO_LOCK, FILE_LOCK, SYNCHRONIZED (per-resource), OPTIMISTIC (version + retry).
 */
public class OrderRepository extends CsvRepository<Order> {

    private static final String FILE_PATH = "data/orders.csv";
    private static final String HEADER =
            "orderId,customerId,restaurantId,driverId,status,totalAmount,createdAt,version";
    private static final int MAX_RETRIES = 5;

    /**
     * Mutex NGẮN, chỉ bảo vệ bước "đọc cả file -> thay 1 dòng -> ghi lại cả file".
     * Vì CSV phải ghi đè cả file nên nếu 2 thread sửa 2 đơn khác nhau cùng lúc thì
     * một bản ghi sẽ bị mất (lost update) dù đã khóa theo từng ORDER_id.
     * Chỉ dùng cho SYNCHRONIZED / OPTIMISTIC; NO_LOCK và FILE_LOCK không dùng nó.
     */
    private static final Object FILE_IO_GUARD = new Object();

    private final DriverRepository driverRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ResourceLockManager lockManager;
    private final FileLockManager fileLockManager;

    public OrderRepository(DriverRepository driverRepository,
                           MenuItemRepository menuItemRepository,
                           OrderItemRepository orderItemRepository,
                           ResourceLockManager lockManager,
                           FileLockManager fileLockManager) {
        super(FILE_PATH, HEADER, Order::new);
        this.driverRepository = driverRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.lockManager = lockManager;
        this.fileLockManager = fileLockManager;
    }

    // ------------------------------------------------------------------
    // Dispatcher theo cơ chế khóa (SimulationEngine / DispatchThread gọi hàm này)
    // ------------------------------------------------------------------
    public void assignDriver(String orderId, String driverId, LockMechanism mechanism) {
        switch (mechanism) {
            case NO_LOCK:
                assignDriverNoLock(orderId, driverId);
                break;
            case FILE_LOCK:
                assignDriverWithFileLock(orderId, driverId);
                break;
            case SYNCHRONIZED:
                assignDriverWithSync(orderId, driverId);
                break;
            case OPTIMISTIC:
                assignDriverWithRetry(orderId, driverId);
                break;
            default:
                throw new IllegalArgumentException("Unknown mechanism: " + mechanism);
        }
    }

    // ------------------------------------------------------------------
    // 1. NO_LOCK (baseline) - cố tình không bảo vệ gì để lộ race condition
    // ------------------------------------------------------------------
    public void assignDriverNoLock(String orderId, String driverId) {
        assignCore(orderId, driverId, driverRepository::markBusyNoLock, false);
    }

    // ------------------------------------------------------------------
    // 2. FILE_LOCK - khóa cả file CSV bằng FileChannel.lock()
    // ------------------------------------------------------------------
    public void assignDriverWithFileLock(String orderId, String driverId) {
        try (AutoCloseable ignored = fileLockManager.acquireLock(FILE_PATH)) {
            assignCore(orderId, driverId, driverRepository::markBusyWithFileLock, false);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("FILE_LOCK failed for order " + orderId, e);
        }
    }

    // ------------------------------------------------------------------
    // 3. SYNCHRONIZED - khóa mịn theo từng tài nguyên + Global Lock Ordering
    //    (KHÔNG dùng synchronized(this))
    // ------------------------------------------------------------------
    public void assignDriverWithSync(String orderId, String driverId) {
        String key1 = "ORDER_" + orderId;
        String key2 = "DRIVER_" + driverId;
        String first = key1.compareTo(key2) < 0 ? key1 : key2;
        String second = key1.compareTo(key2) < 0 ? key2 : key1;

        synchronized (lockManager.getLock(first)) {
            synchronized (lockManager.getLock(second)) {
                assignCore(orderId, driverId, driverRepository::markBusyWithSync, true);
            }
        }
    }

    // ------------------------------------------------------------------
    // 4. OPTIMISTIC - 1 lần thử (so version khi ghi), ném OptimisticLockException nếu lệch
    // ------------------------------------------------------------------
    public void assignDriverWithOptimistic(String orderId, String driverId) {
        assignCore(orderId, driverId, driverRepository::markBusyWithOptimistic, true);
    }

    /** OPTIMISTIC + retry với exponential jitter backoff (tối đa 5 lần). */
    public void assignDriverWithRetry(String orderId, String driverId) {
        for (int attempt = 1; ; attempt++) {
            try {
                assignDriverWithOptimistic(orderId, driverId);
                return;
            } catch (OptimisticLockException e) {
                if (attempt >= MAX_RETRIES) {
                    throw new OptimisticLockException(
                            "Vượt quá " + MAX_RETRIES + " lần thử lại cho đơn: " + orderId);
                }
                sleepBackoff(attempt);
            }
        }
    }

    // ------------------------------------------------------------------
    // Lõi gán đơn dùng chung cho cả 4 cơ chế
    // ------------------------------------------------------------------
    @FunctionalInterface
    private interface DriverMarker {
        void mark(String driverId, String orderId);
    }

    /**
     * @param guardedIo true  -> ghi qua FILE_IO_GUARD và so version (compare-and-set)
     *                  false -> ghi thẳng, không kiểm tra (NO_LOCK / FILE_LOCK)
     */
    private void assignCore(String orderId, String driverId, DriverMarker marker, boolean guardedIo) {
        Order order = requireOrder(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new DoubleAssignmentException(
                    "Order " + orderId + " đã ở trạng thái " + order.getStatus() + ", không thể gán thêm tài xế");
        }

        // Giữ chỗ tài xế trước (ném DriverOverloadException nếu tài xế đang BUSY)
        marker.mark(driverId, orderId);

        try {
            int expectedVersion = order.getVersion();
            order.setDriverId(driverId);
            order.setStatus(OrderStatus.ASSIGNED);
            order.incrementVersion();
            writeOrder(order, expectedVersion, guardedIo);
        } catch (RuntimeException e) {
            // Ghi đơn thất bại -> trả tài xế về AVAILABLE để không bị "kẹt" BUSY
            driverRepository.markAvailable(driverId);
            throw e;
        }
    }

    // ------------------------------------------------------------------
    // Nghiệp vụ: hủy đơn & hết hạn 5 phút
    // ------------------------------------------------------------------

    /** Chỉ hủy được khi PENDING; ngược lại ném OrderCancellationException. Hoàn trả stock khi hủy. */
    public boolean cancelOrder(String orderId) {
        if (!transitionIfPending(orderId, OrderStatus.CANCELLED)) {
            throw new OrderCancellationException(
                    "Không thể hủy đơn " + orderId + " - trạng thái hiện tại: " + requireOrder(orderId).getStatus());
        }
        restoreStockOf(orderId);
        return true;
    }

    /** Quét các đơn PENDING quá timeoutMinutes -> EXPIRED và hoàn trả stock. Trả về danh sách đơn đã hết hạn. */
    public List<Order> checkAndExpireOrders(int timeoutMinutes) {
        List<Order> expired = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        long timeoutMillis = timeoutMinutes * 60_000L;

        for (Order pending : findPendingOrders()) {
            long ageMillis = Duration.between(parseTime(pending.getCreatedAt()), now).toMillis();
            if (ageMillis <= timeoutMillis) {
                continue;
            }
            if (transitionIfPending(pending.getId(), OrderStatus.EXPIRED)) {
                restoreStockOf(pending.getId());
                expired.add(requireOrder(pending.getId()));
            }
        }
        return expired;
    }

    public List<Order> findPendingOrders() {
        return findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    /** Đọc - kiểm tra PENDING - đổi trạng thái - ghi, tất cả trong 1 critical section ngắn. */
    private boolean transitionIfPending(String orderId, OrderStatus target) {
        synchronized (FILE_IO_GUARD) {
            Order row = requireOrder(orderId);
            if (row.getStatus() != OrderStatus.PENDING) {
                return false;
            }
            int expectedVersion = row.getVersion();
            row.setStatus(target);
            row.incrementVersion();
            replaceRow(row, expectedVersion, true);
            return true;
        }
    }

    private void restoreStockOf(String orderId) {
        for (OrderItem item : orderItemRepository.findByOrderId(orderId)) {
            menuItemRepository.restoreStock(item.getItemId(), item.getQuantity());
        }
    }

    private void writeOrder(Order updated, int expectedVersion, boolean guardedIo) {
        if (guardedIo) {
            synchronized (FILE_IO_GUARD) {
                replaceRow(updated, expectedVersion, true);
            }
        } else {
            replaceRow(updated, expectedVersion, false);
        }
    }

    private void replaceRow(Order updated, int expectedVersion, boolean checkVersion) {
        List<Order> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            Order row = all.get(i);
            if (row.getId().equals(updated.getId())) {
                if (checkVersion && row.getVersion() != expectedVersion) {
                    throw new OptimisticLockException("Order " + updated.getId()
                            + " đã bị thread khác sửa (version " + row.getVersion()
                            + " != " + expectedVersion + ")");
                }
                all.set(i, updated);
                writeAll(all);
                return;
            }
        }
        throw new IllegalArgumentException("Order not found: " + updated.getId());
    }

    private Order requireOrder(String orderId) {
        return findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    /** Hỗ trợ cả "2026-10-18T10:15:30" và "2026-10-18 10:15:30". */
    private static LocalDateTime parseTime(String createdAt) {
        return LocalDateTime.parse(createdAt.trim().replace(' ', 'T'));
    }

    private static void sleepBackoff(int attempt) {
        long delay = (10L << (attempt - 1)) + ThreadLocalRandom.current().nextInt(20);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new OptimisticLockException("Bị ngắt khi đang chờ retry");
        }
    }
}
