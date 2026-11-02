package view;

import model.Customer;
import model.Driver;
import model.DriverStatus;
import model.Restaurant;

import java.util.List;

/**
 * Renders an ASCII grid map of Quy Nhon City showing Restaurants [R], Drivers [D/B], and Customers [C].
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
        this.cols = 48;
        this.minLat = 13.745;
        this.maxLat = 13.795;
        this.minLon = 109.200;
        this.maxLon = 109.245;
    }

    public void renderAsciiMap(List<Restaurant> restaurants, List<Customer> customers, List<Driver> drivers) {
        char[][] grid = new char[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = '.';
            }
        }

        if (customers != null) {
            customers.stream().limit(30).forEach(c -> this.plot(grid, c.getLatitude(), c.getLongitude(), 'C'));
        }
        if (drivers != null) {
            drivers.stream().limit(25).forEach(d -> this.plot(grid, d.getLatitude(), d.getLongitude(),
                    d.getStatus() == DriverStatus.AVAILABLE ? 'D' : 'B'));
        }
        if (restaurants != null) {
            restaurants.stream().limit(15).forEach(r -> this.plot(grid, r.getLatitude(), r.getLongitude(), 'R'));
        }

        System.out.println("+------------------------------------------------+");
        System.out.println("|   BAN DO DIEU PHOI GIAO DO AN TP. QUY NHON     |");
        System.out.println("|   [R] Quan an   [D] Tai xe ranh  [B] Dang giao |");
        System.out.println("|   [C] Khach hang               [.] Mat duong   |");
        System.out.println("+------------------------------------------------+");
        for (int r = 0; r < this.rows; r++) {
            System.out.print("|");
            for (int c = 0; c < this.cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println("|");
        }
        System.out.println("+------------------------------------------------+");
    }

    private void plot(char[][] grid, double lat, double lon, char symbol) {
        int row = (int) ((this.maxLat - lat) / (this.maxLat - this.minLat) * (this.rows - 1));
        int col = (int) ((lon - this.minLon) / (this.maxLon - this.minLon) * (this.cols - 1));
        row = Math.max(0, Math.min(this.rows - 1, row));
        col = Math.max(0, Math.min(this.cols - 1, col));
        grid[row][col] = symbol;
    }
}
