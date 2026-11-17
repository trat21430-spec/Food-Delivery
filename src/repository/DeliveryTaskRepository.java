package repository;

import model.DeliveryState;
import model.DeliveryTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for physical Delivery Tasks (Trang 13 Master Plan).
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class DeliveryTaskRepository extends CsvRepository<DeliveryTask> {

    public DeliveryTaskRepository() {
        super("data/delivery_tasks.csv", "id;orderId;restaurantId;customerId;pickupLat;pickupLng;dropoffLat;dropoffLng;state;assignedDriverId;dispatchAttempt;createdAt;pickedUpAt;deliveredAt;version");
    }

    public DeliveryTaskRepository(String customPath) {
        super(customPath, "id;orderId;restaurantId;customerId;pickupLat;pickupLng;dropoffLat;dropoffLng;state;assignedDriverId;dispatchAttempt;createdAt;pickedUpAt;deliveredAt;version");
    }

    @Override
    public DeliveryTask createEntity() {
        return new DeliveryTask();
    }

    public synchronized DeliveryTask findByOrderId(String orderId) {
        if (orderId == null) {
            return null;
        }
        List<DeliveryTask> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            DeliveryTask t = all.get(i);
            if (orderId.equalsIgnoreCase(t.getOrderId())) {
                return t;
            }
        }
        return null;
    }

    public synchronized List<DeliveryTask> findActiveTasksByDriver(String driverId) {
        List<DeliveryTask> res = new ArrayList<DeliveryTask>();
        if (driverId == null) {
            return res;
        }
        List<DeliveryTask> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            DeliveryTask t = all.get(i);
            if (driverId.equalsIgnoreCase(t.getAssignedDriverId())) {
                DeliveryState st = t.getState();
                if (st == DeliveryState.ASSIGNED || st == DeliveryState.ARRIVED_AT_MERCHANT || st == DeliveryState.PICKED_UP) {
                    res.add(t);
                }
            }
        }
        return res;
    }
}
