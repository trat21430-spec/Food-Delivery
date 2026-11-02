import controller.MainController;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Food Delivery Network Simulation (MVC Controller-Driven)...");
        MainController mainController = new MainController();
        mainController.run();
    }
}
