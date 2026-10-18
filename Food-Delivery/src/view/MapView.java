package view;

import model.Customer;
import model.Driver;
import model.Restaurant;
import java.util.Arrays;
import java.util.List;

public class MapView {
    private static final int ROWS = 15;
    private static final int COLS = 45;
    // Boundary coords for map grid (e.g., Hanoi Area)
    private static final double MIN_LAT = 21.000;
    private static final double MAX_LAT = 21.060;
    private static final double MIN_LON = 105.800;
    private static final double MAX_LON = 105.900;

    public void renderAsciiMap(List<Customer> customers, List<Restaurant> restaurants, List<Driver> drivers) {
        char[][] grid = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(grid[r], '.');
        }
        // Render Customers 'C'
        for (Customer c : customers) {
            int r = latToRow(c.getLatitude());
            int col = lonToCol(c.getLongitude());
            if (isValid(r, col)) {
                grid[r][col] = 'C';
            }
        }
        // Render Restaurants 'R'
        for (Restaurant rest : restaurants) {
            int r = latToRow(rest.getLatitude());
            int col = lonToCol(rest.getLongitude());
            if (isValid(r, col)) {
                grid[r][col] = 'R';
            }
        }
        // Render Drivers 'D'
        for (Driver d : drivers) {
            int r = latToRow(d.getLatitude());
            int col = lonToCol(d.getLongitude());
            if (isValid(r, col)) {
                grid[r][col] = 'D';
            }
        }
        System.out.println("\n=== ASCII LIVE MAP VISUALIZATION (C: Customer, R: Restaurant, D: Driver) ===");
        System.out.println("+" + "-".repeat(COLS) + "+");
        for (int r = 0; r < ROWS; r++) {
            System.out.print("|");
            for (int c = 0; c < COLS; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println("|");
        }
        System.out.println("+" + "-".repeat(COLS) + "+");
        System.out.printf("Map Bounds: Lat [%.3f - %.3f], Lon [%.3f - %.3f]\n\n", MIN_LAT, MAX_LAT, MIN_LON, MAX_LON);
    }

    private int latToRow(double lat) {
        double norm = (lat - MIN_LAT) / (MAX_LAT - MIN_LAT);
        int r = (int) ((1.0 - norm) * (ROWS - 1));
        return Math.max(0, Math.min(ROWS - 1, r));
    }

    private int lonToCol(double lon) {
        double norm = (lon - MIN_LON) / (MAX_LON - MIN_LON);
        int c = (int) (norm * (COLS - 1));
        return Math.max(0, Math.min(COLS - 1, c));
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }
}