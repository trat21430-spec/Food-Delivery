import view.MainView;
import web.WebServer;
import java.io.File;
public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   FOOD DELIVERY NETWORK CONCURRENCY SIMULATOR (LAB211)");
        System.out.println("==================================================================");
        // Check if data directory exists and contains customers.csv
        File customerFile = new File("data/customers.csv");
        if (!customerFile.exists() || customerFile.length() < 100) {
            System.out.println("Dataset missing or incomplete! Generating synthetic CSV dataset...");
            DataGenerator.generateAllData();
        } else {
            System.out.println("Synthetic CSV dataset detected in data/ directory.");
        }
        // Start Web Dashboard Backend Server on Port 8080 in background thread
        try {
            WebServer webServer = new WebServer(8080);
            webServer.start();
        } catch (Exception e) {
            System.err.println("Could not start WebServer on port 8080: " + e.getMessage());
        }
        // Start Interactive CLI Main Menu
        MainView mainView = new MainView();
        mainView.startMenu();
    }
}
