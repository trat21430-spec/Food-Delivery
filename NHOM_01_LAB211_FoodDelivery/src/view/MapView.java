package view;

import model.Restaurant;
import model.Customer;
import model.Driver;
import model.DriverStatus;

import java.util.List;

public class MapView {
    private int rows;
    private int cols;
    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;

    public MapView() {
        this.rows = 15;
        this.cols = 45;
        this.minLat = 21.000;
        this.maxLat = 21.060;
        this.minLon = 105.800;
        this.maxLon = 105.900;
    }

    public void renderAsciiMap(List<Restaurant> restaurants, List<Customer> customers, List<Driver> drivers) {
        char[][] grid = new char[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = '.';
            }
        }

        // Plot Customers 'C'
        if (customers != null) {
            for (int i = 0; i < customers.size(); i++) {
                Customer cust = customers.get(i);
                int r = this.latToRow(cust.getLatitude());
                int c = this.lonToCol(cust.getLongitude());
                if (this.isInBounds(r, c)) {
                    grid[r][c] = 'C';
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
                    grid[r][c] = (d.getStatus() == DriverStatus.AVAILABLE) ? 'D' : 'B';
                }
            }
        }

        System.out.println("\n+-----------------------------------------------+");
        System.out.println("|      FOOD DELIVERY NETWORK ASCII MAP GRID     |");
        System.out.println("|  [R] Restaurant  [C] Customer  [D] Avail Driver |");
        System.out.println("+-----------------------------------------------+");

        for (int r = 0; r < this.rows; r++) {
            System.out.print("| ");
            for (int c = 0; c < this.cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println(" |");
        }
        System.out.println("+-----------------------------------------------+\n");
    }

    private int latToRow(double lat) {
        double ratio = (lat - this.minLat) / (this.maxLat - this.minLat);
        int r = (int)((1.0 - ratio) * (this.rows - 1));
        return Math.max(0, Math.min(this.rows - 1, r));
    }

    private int lonToCol(double lon) {
        double ratio = (lon - this.minLon) / (this.maxLon - this.minLon);
        int c = (int)(ratio * (this.cols - 1));
        return Math.max(0, Math.min(this.cols - 1, c));
    }

    private boolean isInBounds(int r, int c) {
        return r >= 0 && r < this.rows && c >= 0 && c < this.cols;
    }
}
