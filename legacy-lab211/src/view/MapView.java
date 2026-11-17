package view;

import model.Customer;
import model.Driver;
import model.DriverStatus;
import model.Order;
import model.OrderStatus;
import model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders an ASCII grid map of Quy Nhon City showing Restaurants [R], Drivers [D/B], and Customers [C].
 * Strictly adheres to LAB211 rules: 100% instance-based, no static, no final, no protected, 100% this.
 */
public class MapView {
    private int rows;
    private int cols;
    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;

    public MapView() {
        this.rows = 16;
        this.cols = 50;
        this.minLat = 13.745;
        this.maxLat = 13.795;
        this.minLon = 109.200;
        this.maxLon = 109.245;
    }

    public MapView(int rows, int cols, double minLat, double maxLat, double minLon, double maxLon) {
        this.rows = rows;
        this.cols = cols;
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
    }

    public void renderAsciiMap(List<Restaurant> restaurants, List<Customer> customers, List<Driver> drivers) {
        this.renderAsciiMap(restaurants, customers, drivers, null);
    }

    public void renderAsciiMap(List<Restaurant> restaurants, List<Customer> customers,
                               List<Driver> drivers, List<Order> orders) {
        char[][] grid = new char[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = '.';
            }
        }

        // 1. Plot representative sample of Customers [C]
        if (customers != null) {
            int stepC = Math.max(1, customers.size() / 50);
            for (int i = 0; i < customers.size(); i += stepC) {
                Customer c = customers.get(i);
                this.plot(grid, c.getLatitude(), c.getLongitude(), 'C');
            }
        }

        // 2. Plot representative sample of Restaurants [R]
        if (restaurants != null) {
            int stepR = Math.max(1, restaurants.size() / 30);
            for (int i = 0; i < restaurants.size(); i += stepR) {
                Restaurant r = restaurants.get(i);
                this.plot(grid, r.getLatitude(), r.getLongitude(), 'R');
            }
        }

        // 3. Separate available drivers [D] and busy drivers [B]
        List<Driver> availableDrivers = new ArrayList<>();
        List<Driver> busyDrivers = new ArrayList<>();
        if (drivers != null) {
            for (int i = 0; i < drivers.size(); i++) {
                Driver d = drivers.get(i);
                if (d.getStatus() == DriverStatus.AVAILABLE) {
                    availableDrivers.add(d);
                } else if (d.getStatus() == DriverStatus.BUSY) {
                    busyDrivers.add(d);
                }
            }
        }

        // 4. Plot Available Drivers [D]
        int stepD = Math.max(1, availableDrivers.size() / 35);
        for (int i = 0; i < availableDrivers.size(); i += stepD) {
            Driver d = availableDrivers.get(i);
            this.plot(grid, d.getLatitude(), d.getLongitude(), 'D');
        }

        // 5. Plot Busy Drivers [B] (Highest visual priority)
        int stepB = Math.max(1, busyDrivers.size() / 30);
        for (int i = 0; i < busyDrivers.size(); i += stepB) {
            Driver d = busyDrivers.get(i);
            this.plot(grid, d.getLatitude(), d.getLongitude(), 'B');
        }

        System.out.println("+--------------------------------------------------+");
        System.out.println("|     BAN DO DIEU PHOI GIAO DO AN TP. QUY NHON     |");
        System.out.println("|   [R] Quan an   [D] Tai xe ranh  [B] Dang giao   |");
        System.out.println("|   [C] Khach hang                 [.] Mat duong   |");
        System.out.println("+--------------------------------------------------+");
        for (int r = 0; r < this.rows; r++) {
            System.out.print("|");
            for (int c = 0; c < this.cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println("|");
        }
        System.out.println("+--------------------------------------------------+");

        // Real-time audit summary below the map
        int totalRestaurants = restaurants != null ? restaurants.size() : 0;
        int totalCustomers = customers != null ? customers.size() : 0;
        int totalDrivers = drivers != null ? drivers.size() : 0;
        int busyCount = busyDrivers.size();
        int availCount = availableDrivers.size();
        int offlineCount = totalDrivers - busyCount - availCount;

        System.out.println("==================================================================================");
        System.out.println("   XAC THUC TOA DO & DU LIEU BAN DO DIEU PHOI TP. QUY NHON (LIVE CSV AUDIT)");
        System.out.println("==================================================================================");
        System.out.printf(" * Toa do khung luoi Quy Nhon: Vi do [%.4f - %.4f], Kinh do [%.4f - %.4f]%n",
                this.minLat, this.maxLat, this.minLon, this.maxLon);
        System.out.printf(" * Thong ke thuc te CSVDATA: [R]=%d quan an | [C]=%d khach hang | [D]=%d tai xe ranh | [B]=%d tai xe ban giao%n",
                totalRestaurants, totalCustomers, availCount, busyCount);
        if (offlineCount > 0) {
            System.out.printf("   (Ghi chu: Co %d tai xe OFFLINE khong hien thi tren ban do vi ngoai ca truc)%n", offlineCount);
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println(" * TOP 5 TAI XE DANG GIAO HANG TRUC TIEP [B] (CHI TIET MINH BACH TU HE THONG):");

        int showLimit = Math.min(5, busyDrivers.size());
        if (showLimit == 0) {
            System.out.println("   (Hien tai khong co tai xe nao o trang thai BUSY)");
        } else {
            for (int i = 0; i < showLimit; i++) {
                Driver d = busyDrivers.get(i);
                String orderDetail = "Dang van chuyen don hang";
                if (orders != null) {
                    for (int j = 0; j < orders.size(); j++) {
                        Order o = orders.get(j);
                        if (d.getId().equalsIgnoreCase(o.getDriverId()) &&
                            (o.getStatus() == OrderStatus.DISPATCHED || o.getStatus() == OrderStatus.CONFIRMED)) {
                            orderDetail = "Don [" + o.getId() + "] -> Khach: " + o.getCustomerId() +
                                          " (Mon an: " + String.format("%,.0f", o.getTotalFoodAmount()) + " VND)";
                            break;
                        }
                    }
                }
                System.out.printf("   %d. [%s] %-20s | Bien so: %-13s | GPS: (%.4f, %.4f) | %s%n",
                        (i + 1), d.getId(), d.getFullName(), d.getLicensePlate(), d.getLatitude(), d.getLongitude(), orderDetail);
            }
        }
        System.out.println("==================================================================================\n");
    }

    private void plot(char[][] grid, double lat, double lon, char symbol) {
        if (lat < this.minLat || lat > this.maxLat || lon < this.minLon || lon > this.maxLon) {
            return;
        }
        int row = (int) ((this.maxLat - lat) / (this.maxLat - this.minLat) * (this.rows - 1));
        int col = (int) ((lon - this.minLon) / (this.maxLon - this.minLon) * (this.cols - 1));
        row = Math.max(0, Math.min(this.rows - 1, row));
        col = Math.max(0, Math.min(this.cols - 1, col));
        grid[row][col] = symbol;
    }
}
