package View;

import model.Customer;
import model.Driver;
import model.Restaurant;
import java.util.List;

public class MapView {

    public void renderMap(List<Restaurant> restaurants, List<Driver> drivers, List<Customer> customers) {
        System.out.println("\n============= Simulation Map =============");
        // TODO: Logic prints 2d map
        System.out.println("===========================================\n");
    }

    private int[] mapToGrid(double lat, double lng, double minLat, double maxLat, double minLng, double maxLng) {
        // TODO: Logic of change locations
        return new int[]{0, 0}; 
    }
}