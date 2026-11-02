import controller.MainController;

public class Main {
    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        } catch (Exception ignored) {}

        System.out.println("Starting Food Delivery Network Simulation (MVC Controller-Driven)...");
        MainController mainController = new MainController();
        mainController.run();
    }
}
