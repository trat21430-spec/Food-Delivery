package view;

import java.util.List;
import model.Customer;
import model.Driver;
import model.Restaurant;

public class MapView {

    public void renderMap(List<Restaurant> restaurants, List<Driver> drivers, List<Customer> customers) {
        System.out.println("\n============= Simulation Map =============");
        System.out.println("===========================================\n");
    }

    private int[] mapToGrid(double lat, double lng, double minLat, double maxLat, double minLng, double maxLng) {
        return new int[]{0, 0};
    }
}