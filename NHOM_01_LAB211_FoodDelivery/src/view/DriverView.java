package view;

import model.Driver;
import util.TextUtil;

import java.util.List;
import java.util.Locale;

public class DriverView {
    private TextUtil textUtil;
    private int tableWidth;
    private int maxRows;

    public DriverView() {
        this.textUtil = new TextUtil();
        this.tableWidth = 92;
        this.maxRows = 25;
    }

    public void renderDriverList(List<Driver> drivers) {
        if (drivers == null || drivers.isEmpty()) {
            System.out.println("No drivers registered in the fleet.");
            return;
        }

        String border = this.textUtil.line('-', this.tableWidth);
        System.out.println();
        System.out.println(border);
        System.out.printf(Locale.US, "| %-8s | %-22s | %-12s | %-12s | %-22s |%n",
                "ID", "Driver Name", "Phone", "Status", "Current Order");
        System.out.println(border);
        int limit = Math.min(drivers.size(), this.maxRows);
        for (int i = 0; i < limit; i++) {
            Driver d = drivers.get(i);
            String currOrder = (d.getCurrentOrderId() == null || d.getCurrentOrderId().isEmpty()) ? "NONE" : d.getCurrentOrderId();
            String status = (d.getStatus() == null) ? "UNKNOWN" : d.getStatus().name();
            System.out.printf(Locale.US, "| %-8s | %-22s | %-12s | %-12s | %-22s |%n",
                    d.getId(),
                    this.textUtil.truncate(d.getName(), 22),
                    this.textUtil.truncate(d.getPhone(), 12),
                    status,
                    this.textUtil.truncate(currOrder, 22));
        }
        System.out.println(border);
        System.out.println("Total drivers displayed: " + limit + " / " + drivers.size());
        System.out.println();
    }

    public void renderDriverStats(int total, int available, int busy, int offline) {
        System.out.println("\n============== DRIVER FLEET STATUS ==============");
        System.out.println("  Total Drivers Registered : " + total);
        System.out.println("  [AVAILABLE] Free to pick : " + available);
        System.out.println("  [BUSY] Delivering order  : " + busy);
        System.out.println("  [OFFLINE] Inactive       : " + offline);
        System.out.println("===================================================");
    }
}

