package view;

import model.Restaurant;
import model.Customer;
import model.Driver;
import model.DriverStatus;
<<<<<<< HEAD

import java.util.List;

public class MapView {
=======
import model.GeoUtils;
import util.TextUtil;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MapView implements IView {
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    private int rows;
    private int cols;
    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;
<<<<<<< HEAD
=======
    private TextUtil textUtil;
    private GeoUtils geoUtils;
    private Scanner scanner;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

    public MapView() {
        this.rows = 15;
        this.cols = 45;
        this.minLat = 21.000;
        this.maxLat = 21.060;
        this.minLon = 105.800;
        this.maxLon = 105.900;
<<<<<<< HEAD
=======
        this.textUtil = new TextUtil();
        this.geoUtils = new GeoUtils();
        this.scanner = new Scanner(System.in);
    }

    public MapView(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.minLat = 21.000;
        this.maxLat = 21.060;
        this.minLon = 105.800;
        this.maxLon = 105.900;
        this.textUtil = new TextUtil();
        this.geoUtils = new GeoUtils();
        this.scanner = new Scanner(System.in);
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return this.cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public double getMinLat() {
        return this.minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return this.maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLon() {
        return this.minLon;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }

    public double getMaxLon() {
        return this.maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    @Override
    public void display() {
        System.out.println("\n=======================================================");
        System.out.println("          NETWORK ASCII MAP VIEW SUBSYSTEM             ");
        System.out.println("=======================================================");
        System.out.println("  Grid Resolution : " + this.rows + " rows x " + this.cols + " columns");
        System.out.printf(Locale.US, "  Latitude Range  : [%.4f, %.4f] N (Hanoi Region)%n", this.minLat, this.maxLat);
        System.out.printf(Locale.US, "  Longitude Range : [%.4f, %.4f] E (Hanoi Region)%n", this.minLon, this.maxLon);
        System.out.println("  Symbols Legend  : [R] Restaurant, [C] Customer, [D] Available Driver, [B] Busy Driver");
        System.out.println("=======================================================\n");
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    }

    public void renderAsciiMap(List<Restaurant> restaurants, List<Customer> customers, List<Driver> drivers) {
        char[][] grid = new char[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = '.';
            }
        }

<<<<<<< HEAD
=======
        int custCount = 0;
        int restCount = 0;
        int availDriverCount = 0;
        int busyDriverCount = 0;

>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        // Plot Customers 'C'
        if (customers != null) {
            for (int i = 0; i < customers.size(); i++) {
                Customer cust = customers.get(i);
                int r = this.latToRow(cust.getLatitude());
                int c = this.lonToCol(cust.getLongitude());
                if (this.isInBounds(r, c)) {
                    grid[r][c] = 'C';
<<<<<<< HEAD
=======
                    custCount++;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
                }
            }
        }

        // Plot Restaurants 'R'
        if (restaurants != null) {
            for (int i = 0; i < restaurants.size(); i++) {
                Restaurant rest = restaurants.get(i);
                int r = this.latToRow(rest.getLatitude());
                int c = this.lonToCol(rest.getLongitude());
                if (this.isInBounds(r, c)) {
                    grid[r][c] = 'R';
<<<<<<< HEAD
=======
                    restCount++;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
                }
            }
        }

        // Plot Drivers 'D' or 'B' (busy)
        if (drivers != null) {
            for (int i = 0; i < drivers.size(); i++) {
                Driver d = drivers.get(i);
                int r = this.latToRow(d.getLatitude());
                int c = this.lonToCol(d.getLongitude());
                if (this.isInBounds(r, c)) {
<<<<<<< HEAD
                    grid[r][c] = (d.getStatus() == DriverStatus.AVAILABLE) ? 'D' : 'B';
=======
                    if (d.getStatus() == DriverStatus.AVAILABLE) {
                        grid[r][c] = 'D';
                        availDriverCount++;
                    } else {
                        grid[r][c] = 'B';
                        busyDriverCount++;
                    }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
                }
            }
        }

<<<<<<< HEAD
        System.out.println("\n+-----------------------------------------------+");
        System.out.println("|      FOOD DELIVERY NETWORK ASCII MAP GRID     |");
        System.out.println("|  [R] Restaurant  [C] Customer  [D] Avail Driver |");
        System.out.println("+-----------------------------------------------+");
=======
        int headerWidth = this.cols + 14;
        String lineBorder = "+" + this.textUtil.line('-', headerWidth - 2) + "+";

        System.out.println();
        System.out.println(lineBorder);
        System.out.println("|           FOOD DELIVERY NETWORK ASCII GRID MAP              |");
        System.out.println("|  [R] Restaurant  [C] Customer  [D] Avail Driver  [B] Busy   |");
        System.out.printf(Locale.US, "|  Coverage: Hanoi Region (%.3fN - %.3fN, %.3fE - %.3fE)  |%n",
                this.minLat, this.maxLat, this.minLon, this.maxLon);
        System.out.println(lineBorder);
        System.out.println("  [N] North");

        for (int r = 0; r < this.rows; r++) {
            if (r == 0) {
                System.out.printf(Locale.US, "%7.3fN | ", this.maxLat);
            } else if (r == this.rows / 2) {
                System.out.printf(Locale.US, "%7.3fN | ", (this.minLat + this.maxLat) / 2.0);
            } else if (r == this.rows - 1) {
                System.out.printf(Locale.US, "%7.3fN | ", this.minLat);
            } else {
                System.out.print("         | ");
            }

            for (int c = 0; c < this.cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println(" |");
        }

        System.out.println("         " + lineBorder);
        System.out.printf(Locale.US, "  [W]    %.3fE%s%.3fE    [E]%n",
                this.minLon,
                this.textUtil.line(' ', Math.max(1, this.cols - 14)),
                this.maxLon);
        System.out.println("  [S] South");
        System.out.println();
        System.out.printf(Locale.US, "Grid Plot Statistics: Plotted [Restaurants: %d] [Customers: %d] [Available Drivers: %d] [Busy Drivers: %d]%n",
                restCount, custCount, availDriverCount, busyDriverCount);
        System.out.println();
    }

    public void renderDeliveryRoute(Restaurant restaurant, Customer customer, Driver driver) {
        if (restaurant == null || customer == null || driver == null) {
            System.out.println("Cannot render delivery route: missing restaurant, customer, or driver information.");
            return;
        }

        char[][] grid = new char[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = '.';
            }
        }

        int rDriver = this.latToRow(driver.getLatitude());
        int cDriver = this.lonToCol(driver.getLongitude());
        int rRest = this.latToRow(restaurant.getLatitude());
        int cRest = this.lonToCol(restaurant.getLongitude());
        int rCust = this.latToRow(customer.getLatitude());
        int cCust = this.lonToCol(customer.getLongitude());

        // Trace pickup segment from Driver to Restaurant with '*'
        this.interpolateLine(grid, rDriver, cDriver, rRest, cRest, '*');

        // Trace dropoff segment from Restaurant to Customer with '>'
        this.interpolateLine(grid, rRest, cRest, rCust, cCust, '>');

        // Pin the endpoints
        if (this.isInBounds(rDriver, cDriver)) {
            grid[rDriver][cDriver] = 'D';
        }
        if (this.isInBounds(rRest, cRest)) {
            grid[rRest][cRest] = 'R';
        }
        if (this.isInBounds(rCust, cCust)) {
            grid[rCust][cCust] = 'C';
        }

        double pickupKm = this.geoUtils.haversineDistance(
                driver.getLatitude(), driver.getLongitude(),
                restaurant.getLatitude(), restaurant.getLongitude());
        double dropoffKm = this.geoUtils.haversineDistance(
                restaurant.getLatitude(), restaurant.getLongitude(),
                customer.getLatitude(), customer.getLongitude());
        double totalKm = pickupKm + dropoffKm;
        int estimatedMinutes = (int) Math.round(totalKm * 3.5);

        String doubleBorder = this.textUtil.line('=', this.cols + 12);
        String singleBorder = this.textUtil.line('-', this.cols + 12);

        System.out.println();
        System.out.println(doubleBorder);
        System.out.println("             ACTIVE DELIVERY ROUTE VISUALIZATION MAP             ");
        System.out.println("  [D] Driver  [R] Restaurant  [C] Customer Destination           ");
        System.out.println("  [*] Pickup Segment (Driver -> Restaurant)                      ");
        System.out.println("  [>] Dropoff Segment (Restaurant -> Customer)                   ");
        System.out.println(doubleBorder);
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

        for (int r = 0; r < this.rows; r++) {
            System.out.print("| ");
            for (int c = 0; c < this.cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println(" |");
        }
<<<<<<< HEAD
        System.out.println("+-----------------------------------------------+\n");
=======

        System.out.println(singleBorder);
        System.out.printf(Locale.US, "Pickup  : Driver [%s] -> Restaurant [%s] (%.2f km)%n",
                driver.getName(), restaurant.getName(), pickupKm);
        System.out.printf(Locale.US, "Dropoff : Restaurant [%s] -> Customer [%s] (%.2f km)%n",
                restaurant.getName(), customer.getName(), dropoffKm);
        System.out.printf(Locale.US, "Total Distance : %.2f km | Estimated Travel Time : %d minutes%n",
                totalKm, estimatedMinutes);
        System.out.println(doubleBorder);
        System.out.println();
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    }

    private int latToRow(double lat) {
        double ratio = (lat - this.minLat) / (this.maxLat - this.minLat);
<<<<<<< HEAD
        int r = (int)((1.0 - ratio) * (this.rows - 1));
=======
        int r = (int) ((1.0 - ratio) * (this.rows - 1));
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        return Math.max(0, Math.min(this.rows - 1, r));
    }

    private int lonToCol(double lon) {
        double ratio = (lon - this.minLon) / (this.maxLon - this.minLon);
<<<<<<< HEAD
        int c = (int)(ratio * (this.cols - 1));
=======
        int c = (int) (ratio * (this.cols - 1));
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        return Math.max(0, Math.min(this.cols - 1, c));
    }

    private boolean isInBounds(int r, int c) {
        return r >= 0 && r < this.rows && c >= 0 && c < this.cols;
    }
<<<<<<< HEAD
=======

    private void interpolateLine(char[][] grid, int r0, int c0, int r1, int c1, char symbol) {
        int dr = Math.abs(r1 - r0);
        int dc = Math.abs(c1 - c0);
        int steps = Math.max(dr, dc);
        if (steps == 0) {
            return;
        }

        for (int step = 0; step <= steps; step++) {
            double t = (double) step / (double) steps;
            int r = (int) Math.round(r0 + t * (r1 - r0));
            int c = (int) Math.round(c0 + t * (c1 - c0));
            if (this.isInBounds(r, c) && grid[r][c] == '.') {
                grid[r][c] = symbol;
            }
        }
    }
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
}
