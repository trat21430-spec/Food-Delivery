package repository;

import model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository extends CsvRepository<OrderItem> {

    public OrderItemRepository() {
        super("data/order_items.csv", "id,orderId,menuItemId,quantity,price,createdAt,updatedAt,version");
    }

    public OrderItemRepository(String customFilePath) {
        super(customFilePath, "id,orderId,menuItemId,quantity,price,createdAt,updatedAt,version");
    }

    @Override
    public OrderItem createEntity() {
        return new OrderItem();
    }

    public List<OrderItem> findByOrderId(String orderId) {
        List<OrderItem> all = this.findAll();
        List<OrderItem> filtered = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            OrderItem item = all.get(i);
            if (item.getOrderId().equals(orderId)) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}
