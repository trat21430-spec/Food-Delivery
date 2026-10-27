import controller.OrderController;
import repository.DriverRepository;
import repository.MenuItemRepository;
import repository.OrderRepository;
import view.MainView;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Repositories
        OrderRepository orderRepo = new OrderRepository();
        MenuItemRepository menuRepo = new MenuItemRepository();
        DriverRepository driverRepo = new DriverRepository();

        // 2. Initialize Controllers
        OrderController orderController = new OrderController(orderRepo, menuRepo, driverRepo);

        // 3. Initialize & Start View Layer
        MainView mainView = new MainView(orderController, menuRepo, driverRepo);
        mainView.startMenu();
    }
}
