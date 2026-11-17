package model;

import java.time.LocalDateTime;

/**
 * Enhanced Order Entity supporting Master Plan 4 Decoupled State Machines:
 * - orderState (OrderState: PENDING -> CONFIRMED -> PREPARING -> READY_FOR_PICKUP -> COMPLETED / CANCELLED)
 * - paymentState (PaymentState: INITIATED -> AUTHORIZED -> CAPTURED -> REFUNDED / FAILED)
 * - deliveryState (DeliveryState: CREATED -> SEARCHING_DRIVER -> ASSIGNED -> ARRIVED_AT_MERCHANT -> PICKED_UP -> DELIVERED / FAILED)
 * - settlementState (SettlementState: OPEN -> CALCULATED -> APPROVED -> PAID)
 */
public class Order extends BaseEntity {
    private String customerId;
    private String restaurantId;
    private String driverId;
    private OrderStatus status; // Legacy compatibility
    private OrderState orderState;
    private PaymentState paymentState;
    private DeliveryState deliveryState;
    private SettlementState settlementState;
    private double totalFoodAmount;
    private double deliveryFee;
    private long createdAtEpochMs;
    private int assignCount;
    private String quoteId;
    private long finalAmountVnd;

    public Order() {
        super();
        this.customerId = "";
        this.restaurantId = "";
        this.driverId = "";
        this.status = OrderStatus.PENDING;
        this.orderState = OrderState.PENDING;
        this.paymentState = PaymentState.INITIATED;
        this.deliveryState = DeliveryState.CREATED;
        this.settlementState = SettlementState.OPEN;
        this.totalFoodAmount = 0.0;
        this.deliveryFee = 15000.0;
        this.createdAtEpochMs = System.currentTimeMillis();
        this.assignCount = 0;
        this.quoteId = "";
        this.finalAmountVnd = 0L;
    }

    public Order(String id, String customerId, String restaurantId, String driverId,
                 OrderStatus status, double totalFoodAmount, double deliveryFee,
                 long createdAtEpochMs, int assignCount, long version) {
        super(id, LocalDateTime.now(), LocalDateTime.now(), version);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId == null ? "" : driverId;
        this.status = status;
        this.totalFoodAmount = totalFoodAmount;
        this.deliveryFee = deliveryFee;
        this.createdAtEpochMs = createdAtEpochMs;
        this.assignCount = assignCount;
        this.quoteId = "";
        this.finalAmountVnd = (long)(totalFoodAmount + deliveryFee);
        this.syncStateMachinesFromStatus(status);
    }

    public Order(String id, String customerId, String restaurantId, String driverId,
                 OrderStatus status, double totalFoodAmount, double deliveryFee,
                 LocalDateTime createdAt, LocalDateTime updatedAt, long version) {
        super(id, createdAt, updatedAt, version);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId == null ? "" : driverId;
        this.status = status;
        this.totalFoodAmount = totalFoodAmount;
        this.deliveryFee = deliveryFee;
        this.createdAtEpochMs = System.currentTimeMillis();
        this.assignCount = (driverId != null && !driverId.isEmpty()) ? 1 : 0;
        this.quoteId = "";
        this.finalAmountVnd = (long)(totalFoodAmount + deliveryFee);
        this.syncStateMachinesFromStatus(status);
    }

    public void syncStateMachinesFromStatus(OrderStatus st) {
        if (st == null) st = OrderStatus.PENDING;
        switch (st) {
            case PENDING:
                this.orderState = OrderState.PENDING;
                this.paymentState = PaymentState.INITIATED;
                this.deliveryState = DeliveryState.CREATED;
                this.settlementState = SettlementState.OPEN;
                break;
            case CONFIRMED:
                this.orderState = OrderState.CONFIRMED;
                this.deliveryState = DeliveryState.SEARCHING_DRIVER;
                this.settlementState = SettlementState.OPEN;
                break;
            case DISPATCHED:
                this.orderState = OrderState.READY_FOR_PICKUP;
                this.deliveryState = DeliveryState.PICKED_UP;
                break;
            case DELIVERED:
                this.orderState = OrderState.COMPLETED;
                this.deliveryState = DeliveryState.DELIVERED;
                this.paymentState = PaymentState.CAPTURED;
                this.settlementState = SettlementState.CALCULATED;
                break;
            case CANCELLED:
                this.orderState = OrderState.CANCELLED;
                this.deliveryState = DeliveryState.FAILED;
                this.paymentState = PaymentState.REFUNDED;
                this.settlementState = SettlementState.ON_HOLD;
                break;
        }
    }

    public String getCustomerId() { return this.customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return this.restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getDriverId() { return this.driverId != null ? this.driverId : ""; }
    public void setDriverId(String driverId) {
        this.driverId = driverId == null ? "" : driverId;
        if (!this.driverId.isEmpty()) {
            this.assignCount++;
            this.deliveryState = DeliveryState.ASSIGNED;
        }
    }

    public OrderStatus getStatus() { return this.status; }
    public void setStatus(OrderStatus status) {
        this.status = status;
        this.syncStateMachinesFromStatus(status);
    }

    public OrderState getOrderState() {
        if (this.orderState == null) this.syncStateMachinesFromStatus(this.status);
        return this.orderState;
    }
    public void setOrderState(OrderState orderState) { this.orderState = orderState; }

    public PaymentState getPaymentState() {
        if (this.paymentState == null) this.syncStateMachinesFromStatus(this.status);
        return this.paymentState;
    }
    public void setPaymentState(PaymentState paymentState) { this.paymentState = paymentState; }

    public DeliveryState getDeliveryState() {
        if (this.deliveryState == null) this.syncStateMachinesFromStatus(this.status);
        return this.deliveryState;
    }
    public void setDeliveryState(DeliveryState deliveryState) { this.deliveryState = deliveryState; }

    public SettlementState getSettlementState() {
        if (this.settlementState == null) this.syncStateMachinesFromStatus(this.status);
        return this.settlementState;
    }
    public void setSettlementState(SettlementState settlementState) { this.settlementState = settlementState; }

    public double getTotalFoodAmount() { return this.totalFoodAmount; }
    public void setTotalFoodAmount(double totalFoodAmount) { this.totalFoodAmount = totalFoodAmount; }

    public double getDeliveryFee() { return this.deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }

    public double getTotalAmount() { return this.totalFoodAmount + this.deliveryFee; }
    public void setTotalAmount(double totalAmount) { this.totalFoodAmount = totalAmount; }

    public long getFinalAmountVnd() { return this.finalAmountVnd > 0 ? this.finalAmountVnd : (long)this.getTotalAmount(); }
    public void setFinalAmountVnd(long finalAmountVnd) { this.finalAmountVnd = finalAmountVnd; }

    public String getQuoteId() { return this.quoteId != null ? this.quoteId : ""; }
    public void setQuoteId(String quoteId) { this.quoteId = quoteId; }

    public long getCreatedAtEpochMs() { return this.createdAtEpochMs; }
    public void setCreatedAtEpochMs(long createdAtEpochMs) { this.createdAtEpochMs = createdAtEpochMs; }

    public int getAssignCount() { return this.assignCount; }
    public void setAssignCount(int assignCount) { this.assignCount = assignCount; }

    @Override
    public String toCsvLine() {
        return String.join(",",
                this.getId(),
                this.customerId,
                this.restaurantId,
                (this.driverId == null ? "" : this.driverId),
                this.status.name(),
                String.format(java.util.Locale.US, "%.0f", this.totalFoodAmount),
                String.format(java.util.Locale.US, "%.0f", this.deliveryFee),
                String.valueOf(this.createdAtEpochMs),
                String.valueOf(this.assignCount),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length >= 10) {
            this.setId(p[0].trim());
            this.customerId = p[1].trim();
            this.restaurantId = p[2].trim();
            this.driverId = p[3].trim();
            this.status = OrderStatus.valueOf(p[4].trim());
            this.totalFoodAmount = Double.parseDouble(p[5].trim());
            this.deliveryFee = Double.parseDouble(p[6].trim());
            this.createdAtEpochMs = Long.parseLong(p[7].trim());
            this.assignCount = Integer.parseInt(p[8].trim());
            this.setVersion(Long.parseLong(p[9].trim()));
            this.finalAmountVnd = (long)(this.totalFoodAmount + this.deliveryFee);
            this.syncStateMachinesFromStatus(this.status);
        }
    }
}
