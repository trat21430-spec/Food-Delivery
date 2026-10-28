package view;

import model.Restaurant;
import util.TextUtil;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class RestaurantView {
    private Scanner scanner;
    private TextUtil textUtil;
    private int tableWidth;
    private int maxRows;

    public RestaurantView() {
        this.scanner = new Scanner(System.in);
        this.textUtil = new TextUtil();
        this.tableWidth = 85;
        this.maxRows = 20;
    }

    public RestaurantView(Scanner scanner) {
        this.scanner = scanner;
        this.textUtil = new TextUtil();
        this.tableWidth = 85;
        this.maxRows = 20;
    }

    public void renderRestaurantList(List<Restaurant> restaurants) {
        if (restaurants == null || restaurants.isEmpty()) {
            System.out.println("No restaurants available.");
            return;
        }

        String border = this.textUtil.line('-', this.tableWidth);
        System.out.println();
        System.out.println(border);
        System.out.printf(Locale.US, "| %-8s | %-28s | %-30s | %-6s |%n", "ID", "Restaurant Name", "Address", "Rating");
        System.out.println(border);
        int limit = Math.min(restaurants.size(), this.maxRows);

        for (int i = 0; i < limit; i++) {
            Restaurant r = restaurants.get(i);
            System.out.printf(Locale.US, "| %-8s | %-28s | %-30s | %-6.1f |%n",
                    r.getId(),
                    this.textUtil.truncate(r.getName(), 28),
                    this.textUtil.truncate(r.getAddress(), 30),
                    r.getRating());
        }
        System.out.println(border);
        System.out.println("Total restaurants displayed: " + limit + " / " + restaurants.size());
        System.out.println();
    }

    public void renderRestaurantDetail(Restaurant r) {
        if (r == null) {
            System.out.println("Restaurant not found.");
            return;
        }
        System.out.println("\n============== RESTAURANT DETAILS ==============");
        System.out.println("ID        : " + r.getId());
        System.out.println("Name      : " + r.getName());
        System.out.println("Address   : " + r.getAddress());
        System.out.printf(Locale.US, "GPS Coord : (%.5f, %.5f)%n", r.getLatitude(), r.getLongitude());
        System.out.printf(Locale.US, "Rating    : %.1f / 5.0%n", r.getRating());
        System.out.println("==================================================");
    }

    public String promptInput(String fieldName) {
        System.out.print("Enter " + fieldName + ": ");
        return this.scanner.nextLine().trim();
    }

    public String promtInput(String fieldName) {
        return this.promptInput(fieldName);
    }
}

