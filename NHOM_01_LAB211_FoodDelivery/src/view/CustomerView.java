package view;

import model.Customer;
import util.TextUtil;

import java.util.List;
import java.util.Locale;

public class CustomerView {
    private TextUtil textUtil;
    private int tableWidth;
    private int maxRows;

    public CustomerView() {
        this.textUtil = new TextUtil();
        this.tableWidth = 91;
        this.maxRows = 20;
    }

    public void renderCustomerList(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        String border = this.textUtil.line('-', this.tableWidth);
        System.out.println();
        System.out.println(border);
        System.out.printf(Locale.US, "| %-8s | %-24s | %-14s | %-32s |%n", "ID", "Customer Name", "Phone", "Delivery Address");
        System.out.println(border);
        int limit = Math.min(customers.size(), this.maxRows);

        for (int i = 0; i < limit; i++) {
            Customer c = customers.get(i);
            System.out.printf(Locale.US, "| %-8s | %-24s | %-14s | %-32s |%n",
                    c.getId(),
                    this.textUtil.truncate(c.getName(), 24),
                    this.textUtil.truncate(c.getPhone(), 14),
                    this.textUtil.truncate(c.getAddress(), 32));
        }
        System.out.println(border);
        System.out.println("Total customers displayed: " + limit + " / " + customers.size());
        System.out.println();
    }
}

