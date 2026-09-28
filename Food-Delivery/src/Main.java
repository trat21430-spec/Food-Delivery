import java.io.File;
import view.MainView;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   FOOD DELIVERY NETWORK CONCURRENCY SIMULATOR (LAB211)");
        System.out.println("==================================================================");
        File customerFile = new File("data/customers.csv");
        if (!customerFile.exists() || customerFile.length() < 100) {
            System.out.println("Dataset missing or incomplete! Generating synthetic CSV dataset...");
            DataGenerator.generateAllData();
        } else {
            System.out.println("Synthetic CSV dataset detected in data/ directory.");
        }
        MainView mainView = new MainView();
        mainView.startMenu();
    }
}
