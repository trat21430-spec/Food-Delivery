package repository;

import model.OrderSnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Immutable Order Snapshots (Trang 5 & 10 Master Plan).
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class OrderSnapshotRepository extends CsvRepository<OrderSnapshot> {

    public OrderSnapshotRepository() {
        super("data/order_snapshots.csv", "id;orderId;customerId;customerName;customerPhone;deliveryAddress;deliveryLat;deliveryLon;restaurantId;restaurantName;restaurantAddress;itemsJson;foodAmountVnd;deliveryFeeVnd;discountAmountVnd;finalTotalVnd;voucherCode;paymentMethod;snapshotTimestamp;version");
    }

    public OrderSnapshotRepository(String customPath) {
        super(customPath, "id;orderId;customerId;customerName;customerPhone;deliveryAddress;deliveryLat;deliveryLon;restaurantId;restaurantName;restaurantAddress;itemsJson;foodAmountVnd;deliveryFeeVnd;discountAmountVnd;finalTotalVnd;voucherCode;paymentMethod;snapshotTimestamp;version");
    }

    @Override
    public OrderSnapshot createEntity() {
        return new OrderSnapshot();
    }

    public OrderSnapshot findByOrderId(String orderId) {
        if (orderId == null) {
            return null;
        }
        List<OrderSnapshot> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            OrderSnapshot s = all.get(i);
            if (orderId.equalsIgnoreCase(s.getOrderId())) {
                return s;
            }
        }
        return null;
    }
}
