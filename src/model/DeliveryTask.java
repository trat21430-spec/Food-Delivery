package model;

import java.time.LocalDateTime;

/**
 * Delivery Task Entity (Trang 13 Master Plan).
 * Separate state machine for physical transport from Restaurant to Customer.
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class DeliveryTask extends BaseEntity {
    private String orderId;
    private String restaurantId;
    private String customerId;
    private double pickupLat;
    private double pickupLng;
    private double dropoffLat;
    private double dropoffLng;
    private DeliveryState state;
    private String assignedDriverId;
    private int dispatchAttempt;
    private String taskCreatedAt;
    private String pickedUpAt;
    private String deliveredAt;

    public DeliveryTask() {
        super();
        this.orderId = "";
        this.restaurantId = "";
        this.customerId = "";
        this.pickupLat = 0.0;
        this.pickupLng = 0.0;
        this.dropoffLat = 0.0;
        this.dropoffLng = 0.0;
        this.state = DeliveryState.CREATED;
        this.assignedDriverId = "";
        this.dispatchAttempt = 0;
        this.taskCreatedAt = LocalDateTime.now().toString();
        this.pickedUpAt = "";
        this.deliveredAt = "";
    }

    public DeliveryTask(String id, String orderId, String restaurantId, String customerId,
                        double pickupLat, double pickupLng, double dropoffLat, double dropoffLng) {
        super(id);
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
        this.state = DeliveryState.CREATED;
        this.assignedDriverId = "";
        this.dispatchAttempt = 0;
        this.taskCreatedAt = LocalDateTime.now().toString();
        this.pickedUpAt = "";
        this.deliveredAt = "";
    }

    public String getOrderId() { return this.orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getRestaurantId() { return this.restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getCustomerId() { return this.customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public double getPickupLat() { return this.pickupLat; }
    public void setPickupLat(double pickupLat) { this.pickupLat = pickupLat; }

    public double getPickupLng() { return this.pickupLng; }
    public void setPickupLng(double pickupLng) { this.pickupLng = pickupLng; }

    public double getDropoffLat() { return this.dropoffLat; }
    public void setDropoffLat(double dropoffLat) { this.dropoffLat = dropoffLat; }

    public double getDropoffLng() { return this.dropoffLng; }
    public void setDropoffLng(double dropoffLng) { this.dropoffLng = dropoffLng; }

    public DeliveryState getState() { return this.state; }
    public void setState(DeliveryState state) { this.state = state; }

    public String getAssignedDriverId() { return this.assignedDriverId; }
    public void setAssignedDriverId(String assignedDriverId) { this.assignedDriverId = assignedDriverId; }

    public int getDispatchAttempt() { return this.dispatchAttempt; }
    public void setDispatchAttempt(int dispatchAttempt) { this.dispatchAttempt = dispatchAttempt; }

    public String getTaskCreatedAt() { return this.taskCreatedAt; }
    public void setTaskCreatedAt(String taskCreatedAt) { this.taskCreatedAt = taskCreatedAt; }

    public String getPickedUpAt() { return this.pickedUpAt; }
    public void setPickedUpAt(String pickedUpAt) { this.pickedUpAt = pickedUpAt; }

    public String getDeliveredAt() { return this.deliveredAt; }
    public void setDeliveredAt(String deliveredAt) { this.deliveredAt = deliveredAt; }

    private String encodeField(String text) {
        if (text == null) return "";
        return text.replace(";", "%3B").replace(",", "%2C");
    }

    private String decodeField(String text) {
        if (text == null) return "";
        return text.replace("%3B", ";").replace("%2C", ",");
    }

    @Override
    public String toCsvLine() {
        return String.join(";",
                this.encodeField(this.getId()),
                this.encodeField(this.orderId),
                this.encodeField(this.restaurantId),
                this.encodeField(this.customerId),
                String.format(java.util.Locale.US, "%.6f", this.pickupLat),
                String.format(java.util.Locale.US, "%.6f", this.pickupLng),
                String.format(java.util.Locale.US, "%.6f", this.dropoffLat),
                String.format(java.util.Locale.US, "%.6f", this.dropoffLng),
                this.state != null ? this.state.name() : DeliveryState.CREATED.name(),
                this.encodeField(this.assignedDriverId),
                String.valueOf(this.dispatchAttempt),
                this.encodeField(this.taskCreatedAt),
                this.encodeField(this.pickedUpAt),
                this.encodeField(this.deliveredAt),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] p = line.split(";", -1);
        if (p.length >= 14) {
            this.setId(this.decodeField(p[0].trim()));
            this.orderId = this.decodeField(p[1].trim());
            this.restaurantId = this.decodeField(p[2].trim());
            this.customerId = this.decodeField(p[3].trim());
            try { this.pickupLat = Double.parseDouble(p[4].trim()); } catch (Exception e) { this.pickupLat = 0.0; }
            try { this.pickupLng = Double.parseDouble(p[5].trim()); } catch (Exception e) { this.pickupLng = 0.0; }
            try { this.dropoffLat = Double.parseDouble(p[6].trim()); } catch (Exception e) { this.dropoffLat = 0.0; }
            try { this.dropoffLng = Double.parseDouble(p[7].trim()); } catch (Exception e) { this.dropoffLng = 0.0; }
            try { this.state = DeliveryState.valueOf(p[8].trim()); } catch (Exception e) { this.state = DeliveryState.CREATED; }
            this.assignedDriverId = this.decodeField(p[9].trim());
            try { this.dispatchAttempt = Integer.parseInt(p[10].trim()); } catch (Exception e) { this.dispatchAttempt = 0; }
            this.taskCreatedAt = this.decodeField(p[11].trim());
            this.pickedUpAt = this.decodeField(p[12].trim());
            this.deliveredAt = this.decodeField(p[13].trim());
            if (p.length > 14) {
                try { this.setVersion(Long.parseLong(p[14].trim())); } catch (Exception ignored) {}
            }
        }
    }
}
