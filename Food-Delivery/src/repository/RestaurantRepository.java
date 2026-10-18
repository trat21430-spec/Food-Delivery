package repository;

import model.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RestaurantRepository (restaurants.csv).
 * Schema: restaurantId,name,address,latitude,longitude,rating,isActive
 *
 * Không phải hot-spot nên không cần 4 cơ chế khóa; chỉ cần 1 mutex ngắn
 * cho các thao tác "đọc cả file -> sửa -> ghi lại" (UC5, UC6).
 */
public class RestaurantRepository extends CsvRepository<Restaurant> {

    private static final String FILE_PATH = "data/restaurants.csv";
    private static final String HEADER = "restaurantId,name,address,latitude,longitude,rating,isActive";

    private static final Object WRITE_GUARD = new Object();

    public RestaurantRepository() {
        super(FILE_PATH, HEADER, Restaurant::new);
    }

    /** Các nhà hàng đang mở cửa (isActive = true). */
    public List<Restaurant> findActive() {
        return findAll().stream()
                .filter(Restaurant::isActive)
                .collect(Collectors.toList());
    }

    /** Tìm theo tên, không phân biệt hoa thường. */
    public List<Restaurant> findByName(String keyword) {
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        return findAll().stream()
                .filter(r -> r.getName().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    /** Top N nhà hàng đang hoạt động có rating cao nhất. */
    public List<Restaurant> findTopRated(int limit) {
        return findActive().stream()
                .sorted(Comparator.comparingDouble(Restaurant::getRating).reversed())
                .limit(Math.max(0, limit))
                .collect(Collectors.toList());
    }

    /** Lấy nhà hàng và bảo đảm đang hoạt động; dùng khi đặt món (placeOrder). */
    public Restaurant requireActive(String restaurantId) {
        Restaurant r = findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));
        if (!r.isActive()) {
            throw new IllegalStateException("Restaurant " + restaurantId + " đang đóng cửa");
        }
        return r;
    }

    /** UC6: bật / tắt nhà hàng. */
    public void setActive(String restaurantId, boolean active) {
        synchronized (WRITE_GUARD) {
            Restaurant r = requireRestaurant(restaurantId);
            r.setActive(active);
            r.incrementVersion();
            update(r);
        }
    }

    /** Cập nhật rating trong khoảng 0.0 - 5.0. */
    public void updateRating(String restaurantId, double rating) {
        if (rating < 0.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating phải nằm trong [0, 5]: " + rating);
        }
        synchronized (WRITE_GUARD) {
            Restaurant r = requireRestaurant(restaurantId);
            r.setRating(rating);
            r.incrementVersion();
            update(r);
        }
    }

    public Optional<Restaurant> findByIdSafe(String restaurantId) {
        return findById(restaurantId);
    }

    private Restaurant requireRestaurant(String restaurantId) {
        return findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));
    }
}
