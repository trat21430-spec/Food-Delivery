/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author HP
 */

import model.OrderItem;

public class OrderItemRepository extends CsvRepository<OrderItem> {

    public OrderItemRepository() {
        super("data/order_items.csv");
    }

    public OrderItemRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected OrderItem fromCsvLine(String line) {
        String[] parts = line.split(",(?=(?:[^"]*\"[^"]*\")*[^"]*$)");
        OrderItem item = new OrderItem();
        if (parts.length < 8) {
            return item;
        }
        item.setId(parts[0].trim());
        item.setOrderId(parts[1].trim());
        item.setMenuItemId(parts[2].trim());
        item.setQuantity(Integer.parseInt(parts[3].trim()));
        item.setPrice(Double.parseDouble(parts[4].trim()));
        item.setCreatedAt(java.time.LocalDateTime.parse(parts[5].trim()));
        item.setUpdatedAt(java.time.LocalDateTime.parse(parts[6].trim()));
        item.setVersion(Long.parseLong(parts[7].trim()));
        return item;
    }

    @Override
    protected String toCsvLine(OrderItem item) {
        return String.join(",",
                item.getId(),
                item.getOrderId(),
                item.getMenuItemId(),
                String.valueOf(item.getQuantity()),
                String.valueOf(item.getPrice()),
                item.getCreatedAt().toString(),
                item.getUpdatedAt().toString(),
                String.valueOf(item.getVersion()));
    }

    @Override
    protected String getHeader() {
        return "id,orderId,menuItemId,quantity,price,createdAt,updatedAt,version";
    }
}
