package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pricing Quote Object (Trang 11 Master Plan - API /api/v1/quotes).
 * Backend computes prices, delivery fees, and voucher discounts, returning a Quote with an expiration window (10 mins).
 * Client MUST provide quoteId when creating an order; client CANNOT send raw grand_total.
 */
public class Quote {
    private String quoteId;
    private String customerId;
    private String restaurantId;
    private String restaurantName;
    private List<QuoteItem> items;
    private long foodAmountVnd;
    private long deliveryFeeVnd;
    private long discountAmountVnd;
    private long finalTotalVnd;
    private String voucherCode;
    private double distanceKm;
    private long createdAtMs;
    private long expiresAtMs; // Default 10 minutes validity
    private boolean valid;
    private String message;

    public static class QuoteItem {
        private String menuItemId;
        private String name;
        private int quantity;
        private long unitPriceVnd;
        private long subtotalVnd;

        public QuoteItem() {}

        public QuoteItem(String menuItemId, String name, int quantity, long unitPriceVnd) {
            this.menuItemId = menuItemId;
            this.name = name;
            this.quantity = quantity;
            this.unitPriceVnd = unitPriceVnd;
            this.subtotalVnd = unitPriceVnd * quantity;
        }

        public String getMenuItemId() { return this.menuItemId; }
        public String getName() { return this.name; }
        public int getQuantity() { return this.quantity; }
        public long getUnitPriceVnd() { return this.unitPriceVnd; }
        public long getSubtotalVnd() { return this.subtotalVnd; }
    }

    public Quote() {
        this.items = new ArrayList<QuoteItem>();
        this.createdAtMs = System.currentTimeMillis();
        this.expiresAtMs = this.createdAtMs + (10 * 60 * 1000); // 10 minutes
        this.valid = true;
    }

    public Quote(String quoteId, String customerId, String restaurantId, String restaurantName) {
        this();
        this.quoteId = quoteId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.expiresAtMs;
    }

    public void addItem(QuoteItem item) {
        this.items.add(item);
    }

    public String getId() { return this.quoteId; }
    public long getExpiresAtEpochMs() { return this.expiresAtMs; }
    public long getRemainingSeconds() {
        long rem = (this.expiresAtMs - System.currentTimeMillis()) / 1000L;
        return rem > 0 ? rem : 0L;
    }

    public String getQuoteId() { return this.quoteId; }
    public void setQuoteId(String quoteId) { this.quoteId = quoteId; }

    public String getCustomerId() { return this.customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return this.restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return this.restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public List<QuoteItem> getItems() { return this.items; }
    public void setItems(List<QuoteItem> items) { this.items = items; }

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

    public double getDistanceKm() { return this.distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public long getCreatedAtMs() { return this.createdAtMs; }
    public void setCreatedAtMs(long createdAtMs) { this.createdAtMs = createdAtMs; }

    public long getExpiresAtMs() { return this.expiresAtMs; }
    public void setExpiresAtMs(long expiresAtMs) { this.expiresAtMs = expiresAtMs; }

    public boolean isValid() { return this.valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }
}
