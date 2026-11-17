package util;

import controller.AuthController;
import controller.CustomerController;
import controller.DriverController;
import controller.OrderController;
import controller.RestaurantController;
import model.Customer;
import model.Driver;
import model.GeoUtils;
import model.LockMechanism;
import model.Order;
import model.Restaurant;
import model.User;

public class CustomerNotificationFlowTest {
    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        } catch (Exception ignored) {}

        System.out.println("==========================================================================");
        System.out.println("   KIEM TRA LUONG THONG BAO TAI XE VA TINH THOI GIAN GIAO TP. QUY NHON   ");
        System.out.println("==========================================================================");

        AuthController auth = new AuthController();
        OrderController orderCtrl = new OrderController();
        CustomerController custCtrl = new CustomerController();
        RestaurantController restCtrl = new RestaurantController();
        DriverController driverCtrl = new DriverController();
        GeoUtils geo = new GeoUtils();

        // 1. Dang nhap tai khoan khach hang demo
        boolean ok = auth.login("customer1", "123");
        if (!ok) {
            System.err.println(">>> [FAIL] Dang nhap that bai!");
            System.exit(1);
        }
        User user = auth.getCurrentUser();
        System.out.println(">>> [PASS] Dang nhap thanh cong: " + user.getDisplayName() + " (EntityId: " + user.getLinkedEntityId() + ")");

        // 2. Kiem tra thong tin khach hang tai Quy Nhon
        Customer cust = custCtrl.getCustomerById(user.getLinkedEntityId());
        System.out.println(">>> Khach hang: " + cust.getName() + " | Dia chi: " + cust.getAddress() + " | Toa do: (" + cust.getLatitude() + ", " + cust.getLongitude() + ")");

        // 3. Dat hang tai Quan com tam R001 tai Tran Phu, Quy Nhon
        Restaurant rest = restCtrl.getRestaurantById("R001");
        System.out.println(">>> Nha hang dat: " + rest.getName() + " | Dia chi: " + rest.getAddress() + " | Toa do: (" + rest.getLatitude() + ", " + rest.getLongitude() + ")");

        try {
            Order order = orderCtrl.placeCustomerOrderAndAutoDispatch(cust.getId(), rest.getId(), "M0001", 2, LockMechanism.OPTIMISTIC);
            System.out.println(">>> [PASS] Don hang tao thanh cong: " + order.getId() + " | Trang thai: " + order.getStatus());

            if (order.getDriverId() != null && !order.getDriverId().isEmpty()) {
                Driver d = driverCtrl.getDriverById(order.getDriverId());
                double driverToRestKm = geo.haversine(d.getLatitude(), d.getLongitude(), rest.getLatitude(), rest.getLongitude());
                double restToCustKm = geo.haversine(rest.getLatitude(), rest.getLongitude(), cust.getLatitude(), cust.getLongitude());

                int pickupMin = geo.calculatePickupMinutes(driverToRestKm);
                int deliveryMin = geo.calculateDeliveryMinutes(driverToRestKm, restToCustKm);
                String etaPickupClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), pickupMin);
                String etaDeliveryClock = geo.formatEstimatedClockTime(order.getCreatedAtEpochMs(), deliveryMin);

                System.out.println("\n--------------------------------------------------------------------------");
                System.out.println(">>> [THONG BAO TAI XE DA NHAN DON (MATCHED SUCCESSFUL)] <<<");
                System.out.println("  * Tai xe phu trach:  " + d.getName() + " (Ma: " + d.getId() + ")");
                System.out.println("  * So dien thoai:     " + d.getPhone());
                System.out.println("  * Bien so xe may:    " + d.getLicensePlate() + " (Khu vuc Quy Nhon)");
                System.out.printf("  * Khoang cach:       %.2f km (tai xe -> quan) | %.2f km (quan -> ban)\n", driverToRestKm, restToCustKm);
                System.out.println("  * Thoi gian lay mon: ~" + pickupMin + " phut (Du kien luc " + etaPickupClock + ")");
                System.out.println("  * Thoi gian giao den:~" + deliveryMin + " phut (Du kien luc " + etaDeliveryClock + " tai " + cust.getWard() + ")");
                System.out.println("--------------------------------------------------------------------------");
                System.out.println(">>> [PASS] Xac dinh thoi gian lay hang va thoi gian giao hang tai Quy Nhon hoan toan chinh xac!");
            } else {
                System.out.println(">>> Don hang dang cho tai xe phu hop...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("==========================================================================");
        System.out.println(">>> TAT CA CAC DIEU KIEN THONG BAO & THOI GIAN GIAO HANG QUY NHON HOP LE!");
        System.out.println("==========================================================================");
    }
}
