package service;

import model.Customer;
import model.GeoUtils;
import model.MenuItem;
import model.Quote;
import model.Restaurant;
import repository.CustomerRepository;
import repository.MenuItemRepository;
import repository.RestaurantRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enterprise Pricing Quote Service (Trang 11 Master Plan).
 * Calculates point-in-time quotes, locks pricing for 10 minutes,
 * and ensures clients cannot manipulate prices, shipping fees, or voucher discounts.
 */
public class QuoteService {
    private RestaurantRepository restRepo;
    private MenuItemRepository menuItemRepo;
    private CustomerRepository custRepo;
    private Map<String, Quote> quoteCache;

    public QuoteService() {
        this.restRepo = new RestaurantRepository();
        this.menuItemRepo = new MenuItemRepository();
        this.custRepo = new CustomerRepository();
        this.quoteCache = new ConcurrentHashMap<String, Quote>();
    }

    public Quote createQuote(String customerId, String restaurantId, String menuItemId, int quantity, String voucherCode) {
        Map<String, Integer> items = new java.util.HashMap<String, Integer>();
        items.put(menuItemId, quantity);
        return this.createQuote(customerId, restaurantId, items, voucherCode);
    }

    public Quote createQuote(String customerId, String restaurantId,
                             Map<String, Integer> itemQuantities, String voucherCode) {
        String quoteId = "Q_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 8999 + 1000);
        Restaurant rest = this.restRepo.findById(restaurantId);
        String restName = rest != null ? rest.getName() : "Nhà hàng Quy Nhơn";

        Quote quote = new Quote(quoteId, customerId, restaurantId, restName);

        if (rest == null) {
            quote.setValid(false);
            quote.setMessage("Không tìm thấy nhà hàng đối tác!");
            return quote;
        }

        long foodAmount = 0L;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String itemId = entry.getKey();
            int qty = entry.getValue() != null ? entry.getValue() : 1;
            if (qty <= 0) qty = 1;

            MenuItem item = this.menuItemRepo.findById(itemId);
            if (item != null) {
                long unitPrice = (long)item.getPriceVnd();
                Quote.QuoteItem qItem = new Quote.QuoteItem(item.getId(), item.getName(), qty, unitPrice);
                quote.addItem(qItem);
                foodAmount += (unitPrice * qty);
            }
        }

        if (foodAmount <= 0) {
            quote.setValid(false);
            quote.setMessage("Giỏ hàng không có món ăn hợp lệ!");
            return quote;
        }

        quote.setFoodAmountVnd(foodAmount);

        // Distance & Shipping fee
        Customer cust = this.custRepo.findById(customerId);
        double cLat = cust != null ? cust.getLatitude() : 13.775;
        double cLon = cust != null ? cust.getLongitude() : 109.225;
        double rLat = rest.getLatitude();
        double rLon = rest.getLongitude();

        GeoUtils geo = new GeoUtils();
        double dist = geo.haversine(cLat, cLon, rLat, rLon);
        quote.setDistanceKm(dist);

        long shippingFee = 15000L + (long)(dist * 5000L);
        quote.setDeliveryFeeVnd(shippingFee);

        // Voucher Calculation
        long discount = 0L;
        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            String code = voucherCode.trim().toUpperCase();
            quote.setVoucherCode(code);
            if ("QUYNHON20".equals(code)) {
                if (foodAmount >= 50000L) {
                    discount = Math.min((long)(foodAmount * 0.20), 50000L);
                }
            } else if ("FREESHIP".equals(code)) {
                if (foodAmount >= 30000L) {
                    discount = Math.min(shippingFee, 20000L);
                }
            } else if ("BANHXEO".equals(code)) {
                if (foodAmount >= 40000L) {
                    discount = 15000L;
                }
            } else if ("CHAOMUNG".equals(code)) {
                if (foodAmount >= 60000L) {
                    discount = 25000L;
                }
            }
        }

        quote.setDiscountAmountVnd(discount);
        long finalTotal = Math.max(0L, foodAmount + shippingFee - discount);
        quote.setFinalTotalVnd(finalTotal);

        this.quoteCache.put(quoteId, quote);
        return quote;
    }

    public Quote getQuote(String quoteId) {
        if (quoteId == null) return null;
        Quote q = this.quoteCache.get(quoteId);
        if (q != null && q.isExpired()) {
            this.quoteCache.remove(quoteId);
            return null;
        }
        return q;
    }
}
