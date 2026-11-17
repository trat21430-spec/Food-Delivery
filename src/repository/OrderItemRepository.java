package repository;

import model.OrderItem;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository extends CsvRepository<OrderItem> {

    public OrderItemRepository() {
        super("data/order_items.csv", "id,orderId,menuItemId,quantity,unitPriceVnd");
    }

    public OrderItemRepository(String filePath) {
        super(filePath, "id,orderId,menuItemId,quantity,unitPriceVnd");
    }

    @Override
    public OrderItem createEntity() {
        return new OrderItem();
    }

    public List<OrderItem> findByOrderId(String orderId) {
        List<OrderItem> list = new ArrayList<>();
        if (orderId == null) {
            return list;
        }
        List<OrderItem> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            OrderItem item = all.get(i);
            if (orderId.equals(item.getOrderId())) {
                list.add(item);
            }
        }
        return list;
    }
}
