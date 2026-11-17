package model;

import java.time.LocalDateTime;

/**
 * Immutable Order Snapshot (Trang 5 & Trang 10 Master Plan).
 * Captures point-in-time prices, restaurant details, customer address,
 * delivery fee, and voucher discount so changes to restaurant menus never corrupt historical records.
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class OrderSnapshot extends BaseEntity {
    private String orderId;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private double deliveryLat;
    private double deliveryLon;

    private String restaurantId;
    private String restaurantName;
    private String restaurantAddress;

    private String itemsJson;
    private long foodAmountVnd;
    private long deliveryFeeVnd;
    private long discountAmountVnd;
    private long finalTotalVnd;
    private String voucherCode;
    private String paymentMethod;
    private String snapshotTimestamp;

    public OrderSnapshot() {
        super();
        this.orderId = "";
        this.customerId = "";
        this.customerName = "";
        this.customerPhone = "";
        this.deliveryAddress = "";
        this.deliveryLat = 0.0;
        this.deliveryLon = 0.0;
        this.restaurantId = "";
        this.restaurantName = "";
        this.restaurantAddress = "";
        this.itemsJson = "[]";
        this.foodAmountVnd = 0L;
        this.deliveryFeeVnd = 0L;
        this.discountAmountVnd = 0L;
        this.finalTotalVnd = 0L;
        this.voucherCode = "";
        this.paymentMethod = "COD";
        this.snapshotTimestamp = LocalDateTime.now().toString();
    }

    public OrderSnapshot(String id, String orderId, String customerId, String customerName,
                         String customerPhone, String deliveryAddress, double deliveryLat, double deliveryLon,
                         String restaurantId, String restaurantName, String restaurantAddress,
                         String itemsJson, long foodAmountVnd, long deliveryFeeVnd,
                         long discountAmountVnd, long finalTotalVnd, String voucherCode,
                         String paymentMethod) {
        super(id);
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.deliveryLat = deliveryLat;
        this.deliveryLon = deliveryLon;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.itemsJson = itemsJson != null ? itemsJson : "[]";
        this.foodAmountVnd = foodAmountVnd;
        this.deliveryFeeVnd = deliveryFeeVnd;
        this.discountAmountVnd = discountAmountVnd;
        this.finalTotalVnd = finalTotalVnd;
        this.voucherCode = voucherCode != null ? voucherCode : "";
        this.paymentMethod = paymentMethod != null ? paymentMethod : "COD";
        this.snapshotTimestamp = LocalDateTime.now().toString();
    }

    public String getOrderId() { return this.orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return this.customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return this.customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return this.customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getDeliveryAddress() { return this.deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public double getDeliveryLat() { return this.deliveryLat; }
    public void setDeliveryLat(double deliveryLat) { this.deliveryLat = deliveryLat; }

    public double getDeliveryLon() { return this.deliveryLon; }
    public void setDeliveryLon(double deliveryLon) { this.deliveryLon = deliveryLon; }

    public String getRestaurantId() { return this.restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return this.restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getRestaurantAddress() { return this.restaurantAddress; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }

    public String getItemsJson() { return this.itemsJson; }
    public void setItemsJson(String itemsJson) { this.itemsJson = itemsJson; }

    public long getFoodAmountVnd() { return this.foodAmountVnd; }
    public void setFoodAmountVnd(long foodAmountVnd) { this.foodAmountVnd = foodAmountVnd; }

    public long getDeliveryFeeVnd() { return this.deliveryFeeVnd; }
    public void setDeliveryFeeVnd(long deliveryFeeVnd) { this.deliveryFeeVnd = deliveryFeeVnd; }

    public long getDiscountAmountVnd() { return this.discountAmountVnd; }
    public void setDiscountAmountVnd(long discountAmountVnd) { this.discountAmountVnd = discountAmountVnd; }

    public long getFinalTotalVnd() { return this.finalTotalVnd; }
    public void setFinalTotalVnd(long finalTotalVnd) { this.finalTotalVnd = finalTotalVnd; }

    public String getVoucherCode() { return this.voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    public String getPaymentMethod() { return this.paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getSnapshotTimestamp() { return this.snapshotTimestamp; }
    public void setSnapshotTimestamp(String snapshotTimestamp) { this.snapshotTimestamp = snapshotTimestamp; }

    private String encodeField(String text) {
        if (text == null) return "";
        return text.replace(";", "%3B").replace(",", "%2C").replace("\n", "%0A");
    }

    private String decodeField(String text) {
        if (text == null) return "";
        return text.replace("%3B", ";").replace("%2C", ",").replace("%0A", "\n");
    }

    @Override
    public String toCsvLine() {
        return String.join(";",
                this.encodeField(this.getId()),
                this.encodeField(this.orderId),
                this.encodeField(this.customerId),
                this.encodeField(this.customerName),
                this.encodeField(this.customerPhone),
                this.encodeField(this.deliveryAddress),
                String.format(java.util.Locale.US, "%.6f", this.deliveryLat),
                String.format(java.util.Locale.US, "%.6f", this.deliveryLon),
                this.encodeField(this.restaurantId),
                this.encodeField(this.restaurantName),
                this.encodeField(this.restaurantAddress),
                this.encodeField(this.itemsJson),
                String.valueOf(this.foodAmountVnd),
                String.valueOf(this.deliveryFeeVnd),
                String.valueOf(this.discountAmountVnd),
                String.valueOf(this.finalTotalVnd),
                this.encodeField(this.voucherCode),
                this.encodeField(this.paymentMethod),
                this.encodeField(this.snapshotTimestamp),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] p = line.split(";", -1);
        if (p.length >= 19) {
            this.setId(this.decodeField(p[0].trim()));
            this.orderId = this.decodeField(p[1].trim());
            this.customerId = this.decodeField(p[2].trim());
            this.customerName = this.decodeField(p[3].trim());
            this.customerPhone = this.decodeField(p[4].trim());
            this.deliveryAddress = this.decodeField(p[5].trim());
            try { this.deliveryLat = Double.parseDouble(p[6].trim()); } catch (Exception e) { this.deliveryLat = 0.0; }
            try { this.deliveryLon = Double.parseDouble(p[7].trim()); } catch (Exception e) { this.deliveryLon = 0.0; }
            this.restaurantId = this.decodeField(p[8].trim());
            this.restaurantName = this.decodeField(p[9].trim());
            this.restaurantAddress = this.decodeField(p[10].trim());
            this.itemsJson = this.decodeField(p[11].trim());
            try { this.foodAmountVnd = Long.parseLong(p[12].trim()); } catch (Exception e) { this.foodAmountVnd = 0L; }
            try { this.deliveryFeeVnd = Long.parseLong(p[13].trim()); } catch (Exception e) { this.deliveryFeeVnd = 0L; }
            try { this.discountAmountVnd = Long.parseLong(p[14].trim()); } catch (Exception e) { this.discountAmountVnd = 0L; }
            try { this.finalTotalVnd = Long.parseLong(p[15].trim()); } catch (Exception e) { this.finalTotalVnd = 0L; }
            this.voucherCode = this.decodeField(p[16].trim());
            this.paymentMethod = this.decodeField(p[17].trim());
            this.snapshotTimestamp = this.decodeField(p[18].trim());
            if (p.length > 19) {
                try { this.setVersion(Long.parseLong(p[19].trim())); } catch (Exception ignored) {}
            }
        }
    }
}
