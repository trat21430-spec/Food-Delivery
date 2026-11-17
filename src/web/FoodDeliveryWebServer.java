package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.AdminController;
import controller.AuthController;
import controller.CustomerController;
import controller.DriverController;
import controller.MainController;
import controller.OrderController;
import controller.RestaurantController;
import controller.SimulatorController;
import model.DriverApplication;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Customer;
import model.Driver;
import model.DriverOffer;
import model.DriverStatus;
import model.FinancialLedgerEntry;
import model.LockMechanism;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderSnapshot;
import model.OrderStatus;
import model.Quote;
import model.Restaurant;
import model.SimulationRun;
import model.User;
import model.UserRole;
import repository.MenuItemRepository;
import repository.OrderItemRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Modern Embedded Web Server for Food Delivery Network Simulation (FDS).
 * Exposes full RESTful JSON APIs and serves the interactive Progressive Web App (PWA).
 * Adheres strictly to LAB211 rules: pure OOP, 0% static outside main, 100% this.
 */
public class FoodDeliveryWebServer {
    private int port;
    private HttpServer server;
    private MainController mainController;
    private File webRootDir;
    private List<OutputStream> sseClients;
    private ScheduledExecutorService sseHeartbeat;

    public FoodDeliveryWebServer() {
        this(8080);
    }

    public FoodDeliveryWebServer(int port) {
        this.port = port;
        this.mainController = new MainController();
        this.webRootDir = new File("web");
        this.sseClients = new CopyOnWriteArrayList<OutputStream>();
        this.ensureInitialStock();
    }

    /**
     * Ensures menu items have positive stock if all were depleted in prior benchmark tests.
     */
    public void ensureInitialStock() {
        try {
            RestaurantController rc = this.mainController.getRestaurantController();
            List<Restaurant> rests = rc.getAllRestaurants();
            if (rests != null && !rests.isEmpty()) {
                int availableCount = 0;
                for (Restaurant r : rests) {
                    List<MenuItem> items = rc.getMenuByRestaurant(r.getId());
                    if (items != null) {
                        for (MenuItem item : items) {
                            if (item.getStockQty() > 0) {
                                availableCount++;
                            }
                        }
                    }
                }
                if (availableCount < 5) {
                    this.restockAllItems(50);
                }
            }
        } catch (Exception ignored) {}
    }

    public void restockAllItems(int amount) {
        try {
            RestaurantController rc = this.mainController.getRestaurantController();
            List<Restaurant> rests = rc.getAllRestaurants();
            if (rests != null) {
                int count = 0;
                for (Restaurant r : rests) {
                    List<MenuItem> items = rc.getMenuByRestaurant(r.getId());
                    if (items != null) {
                        for (MenuItem item : items) {
                            if (item.getStockQty() < amount) {
                                rc.updateStock(item.getId(), amount);
                            }
                        }
                    }
                    count++;
                    if (count >= 20) {
                        break;
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    public void start() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
        this.server.setExecutor(Executors.newCachedThreadPool());

        // SSE Heartbeat scheduler (keeps persistent connections alive)
        this.sseHeartbeat = Executors.newSingleThreadScheduledExecutor();
        this.sseHeartbeat.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                broadcastEvent("ping", "{\"time\":" + System.currentTimeMillis() + "}");
            }
        }, 15, 15, TimeUnit.SECONDS);

        // API Contexts
        this.server.createContext("/api/status", new StatusHandler(this));
        this.server.createContext("/api/events", new EventsHandler(this));
        this.server.createContext("/api/admin/overview", new AdminOverviewHandler(this));
        this.server.createContext("/api/admin/driver-applications", new AdminDriverAppsHandler(this));
        this.server.createContext("/api/admin/driver-applications/approve", new AdminDriverAppApproveHandler(this));
        this.server.createContext("/api/admin/driver-applications/reject", new AdminDriverAppRejectHandler(this));
        this.server.createContext("/api/vouchers", new VoucherListHandler(this));
        this.server.createContext("/api/vouchers/apply", new VoucherApplyHandler(this));
        this.server.createContext("/api/stats", new StatsHandler(this));
        this.server.createContext("/api/auth/demo-accounts", new DemoAccountsHandler(this));
        this.server.createContext("/api/auth/login", new LoginHandler(this));
        this.server.createContext("/api/auth/register", new RegisterHandler(this));
        this.server.createContext("/api/restaurants", new RestaurantsHandler(this));
        this.server.createContext("/api/menu", new MenuHandler(this));
        this.server.createContext("/api/menu/stock", new StockUpdateHandler(this));
        this.server.createContext("/api/drivers", new DriversHandler(this));
        this.server.createContext("/api/drivers/status", new DriverStatusHandler(this));
        this.server.createContext("/api/customers", new CustomersHandler(this));
        this.server.createContext("/api/orders", new OrdersHandler(this));
        this.server.createContext("/api/orders/confirm", new OrderConfirmHandler(this));
        this.server.createContext("/api/orders/reject", new OrderRejectHandler(this));
        this.server.createContext("/api/orders/pickup", new OrderPickupHandler(this));
        this.server.createContext("/api/orders/deliver", new OrderDeliverHandler(this));
        this.server.createContext("/api/orders/cancel", new OrderCancelHandler(this));
        this.server.createContext("/api/dispatch/auto", new AutoDispatchHandler(this));
        this.server.createContext("/api/simulate", new SimulateHandler(this));
        this.server.createContext("/api/admin/restock", new RestockHandler(this));
        this.server.createContext("/api/docs", new ApiDocsHandler(this));

        // Master Plan Decoupled Architecture Endpoints
        this.server.createContext("/api/v1/quotes", new QuotesHandler(this));
        this.server.createContext("/api/quotes", new QuotesHandler(this));
        this.server.createContext("/api/v1/driver/offers", new DriverOffersHandler(this));
        this.server.createContext("/api/v1/driver/offers/accept", new DriverOfferAcceptHandler(this));
        this.server.createContext("/api/v1/driver/offers/reject", new DriverOfferRejectHandler(this));
        this.server.createContext("/api/v1/finance/ledger", new FinancialLedgerHandler(this));
        this.server.createContext("/api/finance/ledger", new FinancialLedgerHandler(this));
        this.server.createContext("/api/v1/webhooks/payment-provider", new PaymentWebhookHandler(this));
        this.server.createContext("/api/v1/orders/snapshot", new OrderSnapshotHandler(this));
        this.server.createContext("/api/orders/snapshot", new OrderSnapshotHandler(this));
        this.server.createContext("/api/v1/orders", new OrdersHandler(this));

        // Static Files Context (Frontend SPA & PWA)
        this.server.createContext("/", new StaticFileHandler(this));

        this.server.start();
        System.out.println("=================================================================");
        System.out.println("  FOOD DELIVERY NETWORK SIMULATION (FDS) - WEB SERVER STARTED   ");
        System.out.println("=================================================================");
        System.out.println("  URL: http://localhost:" + this.port + "/");
        System.out.println("  Portals available:");
        System.out.println("   • Khach Hang (Customer Web & PWA App)");
        System.out.println("   • Nha Hang (Restaurant Kitchen Tablet / POS)");
        System.out.println("   • Tai Xe (Driver Mobile Smartphone Portal)");
        System.out.println("   • Dieu Phoi Trung Tam (Central Dispatch & Leaflet GPS Radar)");
        System.out.println("   • Mo Phong Concurrency (Live Benchmark Simulator)");
        System.out.println("=================================================================");
    }

    public void stop() {
        if (this.sseHeartbeat != null) {
            this.sseHeartbeat.shutdown();
        }
        for (OutputStream os : this.sseClients) {
            try { os.close(); } catch (Exception ignored) {}
        }
        this.sseClients.clear();
        if (this.server != null) {
            this.server.stop(0);
        }
    }

    public void registerSseClient(OutputStream os) {
        this.sseClients.add(os);
    }

    public void removeSseClient(OutputStream os) {
        this.sseClients.remove(os);
    }

    public int getSseClientCount() {
        return this.sseClients.size();
    }

    public synchronized void broadcastEvent(String eventType, String jsonPayload) {
        byte[] data = ("event: " + eventType + "\ndata: " + jsonPayload + "\n\n").getBytes(StandardCharsets.UTF_8);
        List<OutputStream> dead = new ArrayList<>();
        for (OutputStream os : this.sseClients) {
            try {
                os.write(data);
                os.flush();
            } catch (Exception ex) {
                dead.add(os);
            }
        }
        if (!dead.isEmpty()) {
            this.sseClients.removeAll(dead);
        }
    }

    public MainController getMainController() {
        return this.mainController;
    }

    public int getPort() {
        return this.port;
    }

    public File getWebRootDir() {
        return this.webRootDir;
    }

    // =========================================================================
    // HTTP HANDLERS
    // =========================================================================

    private class StatusHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public StatusHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            String json = "{\"status\":\"OK\",\"message\":\"Food Delivery Web Server is running\",\"port\":" + this.serverRef.getPort() + ",\"time\":" + System.currentTimeMillis() + "}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class StatsHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public StatsHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                MainController mc = this.serverRef.getMainController();
                List<Restaurant> rests = mc.getRestaurantController().getAllRestaurants();
                List<Driver> drivers = mc.getDriverController().getAllDrivers();
                List<Customer> custs = mc.getCustomerController().getAllCustomers();
                List<Order> orders = mc.getOrderController().getAllOrders();

                int availableDrivers = 0;
                int busyDrivers = 0;
                for (Driver d : drivers) {
                    if (d.getStatus() == DriverStatus.AVAILABLE) availableDrivers++;
                    else if (d.getStatus() == DriverStatus.BUSY) busyDrivers++;
                }

                int pendingOrders = 0;
                int confirmedOrders = 0;
                int dispatchedOrders = 0;
                int deliveredOrders = 0;
                int cancelledOrders = 0;
                double totalRevenue = 0.0;

                for (Order o : orders) {
                    if (o.getStatus() == OrderStatus.PENDING) pendingOrders++;
                    else if (o.getStatus() == OrderStatus.CONFIRMED) confirmedOrders++;
                    else if (o.getStatus() == OrderStatus.DISPATCHED) dispatchedOrders++;
                    else if (o.getStatus() == OrderStatus.DELIVERED) {
                        deliveredOrders++;
                        totalRevenue += o.getTotalAmount();
                    } else if (o.getStatus() == OrderStatus.CANCELLED) cancelledOrders++;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"totalRestaurants\":").append(rests != null ? rests.size() : 0).append(",");
                sb.append("\"totalDrivers\":").append(drivers != null ? drivers.size() : 0).append(",");
                sb.append("\"availableDrivers\":").append(availableDrivers).append(",");
                sb.append("\"busyDrivers\":").append(busyDrivers).append(",");
                sb.append("\"totalCustomers\":").append(custs != null ? custs.size() : 0).append(",");
                sb.append("\"totalOrders\":").append(orders != null ? orders.size() : 0).append(",");
                sb.append("\"pendingOrders\":").append(pendingOrders).append(",");
                sb.append("\"confirmedOrders\":").append(confirmedOrders).append(",");
                sb.append("\"dispatchedOrders\":").append(dispatchedOrders).append(",");
                sb.append("\"deliveredOrders\":").append(deliveredOrders).append(",");
                sb.append("\"cancelledOrders\":").append(cancelledOrders).append(",");
                sb.append("\"totalRevenue\":").append(totalRevenue);
                sb.append("}");

                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class DemoAccountsHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public DemoAccountsHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            String json = "["
                    + "{\"role\":\"CUSTOMER\",\"username\":\"customer1\",\"password\":\"123\",\"displayName\":\"Nguyen Van An (Khach Hang)\",\"linkedId\":\"C0001\"},"
                    + "{\"role\":\"CUSTOMER\",\"username\":\"customer2\",\"password\":\"123\",\"displayName\":\"Tran Thi Binh (Khach Hang)\",\"linkedId\":\"C0002\"},"
                    + "{\"role\":\"RESTAURANT\",\"username\":\"restaurant1\",\"password\":\"123\",\"displayName\":\"Banh Xeo Tom Nhay Gia Vy 101\",\"linkedId\":\"R001\"},"
                    + "{\"role\":\"RESTAURANT\",\"username\":\"restaurant2\",\"password\":\"123\",\"displayName\":\"Bun Cha Ca Ngoc Lien 102\",\"linkedId\":\"R002\"},"
                    + "{\"role\":\"DRIVER\",\"username\":\"driver1\",\"password\":\"123\",\"displayName\":\"Tai Xe Nguyen Van A\",\"linkedId\":\"D0001\"},"
                    + "{\"role\":\"DRIVER\",\"username\":\"driver2\",\"password\":\"123\",\"displayName\":\"Tai Xe Tran Minh B\",\"linkedId\":\"D0002\"},"
                    + "{\"role\":\"DISPATCHER\",\"username\":\"dispatcher\",\"password\":\"disp123\",\"displayName\":\"Dieu Phoi Vien Trung Tam Quy Nhon\",\"linkedId\":\"DISP01\"},"
                    + "{\"role\":\"ADMIN\",\"username\":\"admin\",\"password\":\"admin123\",\"displayName\":\"Quan Tri Vien He Thong (Admin)\",\"linkedId\":\"ADMIN\"}"
                    + "]";
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class LoginHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public LoginHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String username = params.get("username");
                String password = params.get("password");

                AuthController ac = this.serverRef.getMainController().getAuthController();
                boolean success = ac.login(username, password);
                if (success) {
                    User user = ac.getCurrentUser();
                    String role = user != null && user.getRole() != null ? user.getRole().name() : "CUSTOMER";
                    String linkedId = "";
                    if (user != null) {
                        if (user.getLinkedCustomerId() != null && !user.getLinkedCustomerId().isEmpty()) {
                            linkedId = user.getLinkedCustomerId();
                        } else if (user.getLinkedDriverId() != null && !user.getLinkedDriverId().isEmpty()) {
                            linkedId = user.getLinkedDriverId();
                        }
                    }
                    String displayName = (user != null && user.getFullName() != null) ? user.getFullName() : username;
                    String json = "{\"success\":true,\"username\":\"" + escapeJson(username) + "\",\"role\":\"" + role + "\",\"displayName\":\"" + escapeJson(displayName) + "\",\"linkedId\":\"" + escapeJson(linkedId) + "\"}";
                    sendJsonResponse(exchange, 200, json);
                } else {
                    sendJsonResponse(exchange, 401, "{\"success\":false,\"message\":\"Sai ten dang nhap hoac mat khau!\"}");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class RegisterHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public RegisterHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String username = params.get("username");
                String password = params.get("password");
                String fullName = params.get("fullName");
                String phone = params.get("phone");
                String address = params.get("address");
                double lat = parseDouble(params.get("lat"), 13.775);
                double lon = parseDouble(params.get("lon"), 109.225);

                AuthController ac = this.serverRef.getMainController().getAuthController();
                User user = ac.registerCustomer(username, password, fullName, phone, address, lat, lon);
                if (user != null) {
                    String json = "{\"success\":true,\"message\":\"Dang ky tai khoan thanh cong!\",\"customerId\":\"" + user.getLinkedCustomerId() + "\"}";
                    sendJsonResponse(exchange, 200, json);
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Dang ky that bai!\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class RestaurantsHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public RestaurantsHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                List<Restaurant> list = this.serverRef.getMainController().getRestaurantController().getAllRestaurants();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < list.size(); i++) {
                    Restaurant r = list.get(i);
                    if (i > 0) sb.append(",");
                    sb.append("{");
                    sb.append("\"id\":\"").append(escapeJson(r.getId())).append("\",");
                    sb.append("\"name\":\"").append(escapeJson(r.getName())).append("\",");
                    sb.append("\"category\":\"").append(escapeJson(r.getCategory())).append("\",");
                    sb.append("\"address\":\"").append(escapeJson(r.getAddress())).append("\",");
                    sb.append("\"latitude\":").append(r.getLatitude()).append(",");
                    sb.append("\"longitude\":").append(r.getLongitude()).append(",");
                    sb.append("\"rating\":").append(r.getRating()).append(",");
                    sb.append("\"open\":").append(r.isOpen());
                    sb.append("}");
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class MenuHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public MenuHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                Map<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                String restId = query.get("restaurantId");

                RestaurantController rc = this.serverRef.getMainController().getRestaurantController();
                List<MenuItem> items;
                if (restId != null && !restId.isEmpty()) {
                    items = rc.getMenuByRestaurant(restId);
                } else {
                    MenuItemRepository mir = new MenuItemRepository();
                    items = mir.findAll();
                }

                StringBuilder sb = new StringBuilder();
                sb.append("[");
                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        MenuItem m = items.get(i);
                        if (i > 0) sb.append(",");
                        sb.append("{");
                        sb.append("\"id\":\"").append(escapeJson(m.getId())).append("\",");
                        sb.append("\"restaurantId\":\"").append(escapeJson(m.getRestaurantId())).append("\",");
                        sb.append("\"name\":\"").append(escapeJson(m.getName())).append("\",");
                        sb.append("\"price\":").append(m.getPriceVnd()).append(",");
                        sb.append("\"stockQty\":").append(m.getStockQty()).append(",");
                        sb.append("\"version\":").append(m.getVersion());
                        sb.append("}");
                    }
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class StockUpdateHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public StockUpdateHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String itemId = params.get("menuItemId");
                int newStock = Integer.parseInt(params.get("stockQty"));

                RestaurantController rc = this.serverRef.getMainController().getRestaurantController();
                boolean ok = rc.updateStock(itemId, newStock);
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"menuItemId\":\"" + escapeJson(itemId) + "\",\"stockQty\":" + newStock + "}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class DriversHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public DriversHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                List<Driver> drivers = this.serverRef.getMainController().getDriverController().getAllDrivers();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                if (drivers != null) {
                    for (int i = 0; i < drivers.size(); i++) {
                        Driver d = drivers.get(i);
                        if (i > 0) sb.append(",");
                        sb.append("{");
                        sb.append("\"id\":\"").append(escapeJson(d.getId())).append("\",");
                        sb.append("\"fullName\":\"").append(escapeJson(d.getFullName())).append("\",");
                        sb.append("\"phone\":\"").append(escapeJson(d.getPhone())).append("\",");
                        sb.append("\"licensePlate\":\"").append(escapeJson(d.getLicensePlate())).append("\",");
                        sb.append("\"latitude\":").append(d.getLatitude()).append(",");
                        sb.append("\"longitude\":").append(d.getLongitude()).append(",");
                        sb.append("\"status\":\"").append(d.getStatus() != null ? d.getStatus().name() : "OFFLINE").append("\",");
                        sb.append("\"activeOrderCount\":").append(d.getActiveOrderCount()).append(",");
                        sb.append("\"currentOrderId\":\"").append(escapeJson(d.getCurrentOrderId() != null ? d.getCurrentOrderId() : "")).append("\"");
                        sb.append("}");
                    }
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class DriverStatusHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public DriverStatusHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String driverId = params.get("driverId");
                String statusStr = params.get("status");
                DriverStatus status = DriverStatus.valueOf(statusStr.toUpperCase());

                boolean ok = this.serverRef.getMainController().getDriverController().updateDriverStatus(driverId, status);
                if (ok) {
                    this.serverRef.broadcastEvent("driver_status_changed", "{\"driverId\":\"" + escapeJson(driverId) + "\",\"status\":\"" + status.name() + "\"}");
                }
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"driverId\":\"" + escapeJson(driverId) + "\",\"status\":\"" + status.name() + "\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class CustomersHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public CustomersHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                List<Customer> list = this.serverRef.getMainController().getCustomerController().getAllCustomers();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Customer c = list.get(i);
                        if (i > 0) sb.append(",");
                        sb.append("{");
                        sb.append("\"id\":\"").append(escapeJson(c.getId())).append("\",");
                        sb.append("\"fullName\":\"").append(escapeJson(c.getFullName())).append("\",");
                        sb.append("\"phone\":\"").append(escapeJson(c.getPhone())).append("\",");
                        sb.append("\"ward\":\"").append(escapeJson(c.getWard() != null ? c.getWard() : "")).append("\",");
                        sb.append("\"address\":\"").append(escapeJson(c.getAddress())).append("\",");
                        sb.append("\"latitude\":").append(c.getLatitude()).append(",");
                        sb.append("\"longitude\":").append(c.getLongitude());
                        sb.append("}");
                    }
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class OrdersHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrdersHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    Map<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                    String custId = query.get("customerId");
                    String restId = query.get("restaurantId");
                    String driverId = query.get("driverId");

                    OrderController oc = this.serverRef.getMainController().getOrderController();
                    List<Order> orders;
                    if (custId != null && !custId.isEmpty()) {
                        orders = oc.getOrdersByCustomer(custId);
                    } else if (restId != null && !restId.isEmpty()) {
                        orders = oc.getOrdersByRestaurant(restId);
                    } else if (driverId != null && !driverId.isEmpty()) {
                        orders = oc.getOrdersByDriver(driverId);
                    } else {
                        orders = oc.getAllOrders();
                    }

                    OrderItemRepository oir = new OrderItemRepository();
                    List<OrderItem> allItems = oir.findAll();
                    Map<String, List<OrderItem>> orderItemsMap = new HashMap<>();
                    if (allItems != null) {
                        for (OrderItem oi : allItems) {
                            List<OrderItem> list = orderItemsMap.computeIfAbsent(oi.getOrderId(), k -> new ArrayList<>());
                            list.add(oi);
                        }
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    if (orders != null) {
                        for (int i = 0; i < orders.size(); i++) {
                            Order o = orders.get(i);
                            if (i > 0) sb.append(",");
                            sb.append("{");
                            sb.append("\"id\":\"").append(escapeJson(o.getId())).append("\",");
                            sb.append("\"customerId\":\"").append(escapeJson(o.getCustomerId())).append("\",");
                            sb.append("\"restaurantId\":\"").append(escapeJson(o.getRestaurantId())).append("\",");
                            sb.append("\"driverId\":\"").append(escapeJson(o.getDriverId() != null ? o.getDriverId() : "")).append("\",");
                            sb.append("\"status\":\"").append(o.getStatus() != null ? o.getStatus().name() : "PENDING").append("\",");
                            sb.append("\"orderState\":\"").append(o.getOrderState() != null ? o.getOrderState().name() : "PENDING").append("\",");
                            sb.append("\"paymentState\":\"").append(o.getPaymentState() != null ? o.getPaymentState().name() : "INITIATED").append("\",");
                            sb.append("\"deliveryState\":\"").append(o.getDeliveryState() != null ? o.getDeliveryState().name() : "CREATED").append("\",");
                            sb.append("\"settlementState\":\"").append(o.getSettlementState() != null ? o.getSettlementState().name() : "OPEN").append("\",");
                            sb.append("\"finalAmountVnd\":").append(o.getFinalAmountVnd()).append(",");
                            sb.append("\"quoteId\":\"").append(escapeJson(o.getQuoteId() != null ? o.getQuoteId() : "")).append("\",");
                            sb.append("\"totalFoodAmount\":").append(o.getTotalFoodAmount()).append(",");
                            sb.append("\"deliveryFee\":").append(o.getDeliveryFee()).append(",");
                            sb.append("\"totalAmount\":").append(o.getTotalAmount()).append(",");
                            sb.append("\"createdAtEpochMs\":").append(o.getCreatedAtEpochMs()).append(",");
                            sb.append("\"assignCount\":").append(o.getAssignCount()).append(",");

                            // Items array
                            List<OrderItem> oiList = orderItemsMap.get(o.getId());
                            sb.append("\"items\":[");
                            if (oiList != null) {
                                for (int j = 0; j < oiList.size(); j++) {
                                    OrderItem oi = oiList.get(j);
                                    if (j > 0) sb.append(",");
                                    sb.append("{\"menuItemId\":\"").append(escapeJson(oi.getMenuItemId())).append("\",");
                                    sb.append("\"quantity\":").append(oi.getQuantity()).append(",");
                                    sb.append("\"unitPrice\":").append(oi.getUnitPriceVnd()).append("}");
                                }
                            }
                            sb.append("]");

                            sb.append("}");
                        }
                    }
                    sb.append("]");
                    sendJsonResponse(exchange, 200, sb.toString());
                } catch (Exception e) {
                    sendErrorResponse(exchange, 500, e.getMessage());
                }
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    String body = readBody(exchange);
                    Map<String, String> params = parseJsonSimple(body);
                    String custId = params.get("customerId");
                    String restId = params.get("restaurantId");
                    String menuItemId = params.get("menuItemId");
                    int qty = 1;
                    if (params.containsKey("quantity")) {
                        try {
                            qty = Integer.parseInt(params.get("quantity").replaceAll("[^0-9]", ""));
                        } catch (Exception ignored) {}
                    }

                    // Regex extractors for nested structures
                    if (menuItemId == null || menuItemId.isEmpty()) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"menuItemId\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
                        if (m.find()) {
                            menuItemId = m.group(1);
                        }
                    }
                    if (qty <= 0 || !params.containsKey("quantity")) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"quantity\"\\s*:\\s*(\\d+)").matcher(body);
                        if (m.find()) {
                            qty = Integer.parseInt(m.group(1));
                        }
                    }
                    if (custId == null || custId.isEmpty()) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"customerId\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
                        if (m.find()) {
                            custId = m.group(1);
                        }
                    }
                    if (restId == null || restId.isEmpty()) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"restaurantId\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
                        if (m.find()) {
                            restId = m.group(1);
                        }
                    }

                    if (custId == null || custId.isEmpty()) custId = "C0001";
                    if (qty <= 0) qty = 1;

                    String mechStr = params.get("mechanism");
                    LockMechanism mech = LockMechanism.OPTIMISTIC;
                    if (mechStr != null && !mechStr.isEmpty()) {
                        try {
                            mech = LockMechanism.valueOf(mechStr.toUpperCase());
                        } catch (Exception ignored) {}
                    }

                    String quoteId = params.get("quoteId");
                    if (quoteId == null || quoteId.isEmpty()) {
                        java.util.regex.Matcher mq = java.util.regex.Pattern.compile("\"quoteId\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
                        if (mq.find()) quoteId = mq.group(1);
                    }
                    String paymentMethod = params.get("paymentMethod");
                    if (paymentMethod == null || paymentMethod.isEmpty()) paymentMethod = "COD";

                    String idempotencyKey = exchange.getRequestHeaders().getFirst("Idempotency-Key");
                    if (idempotencyKey == null || idempotencyKey.isEmpty()) {
                        idempotencyKey = params.get("idempotencyKey");
                    }

                    OrderController oc = this.serverRef.getMainController().getOrderController();

                    if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                        util.IdempotencyManager.CachedResponse cached = oc.getIdempotencyManager().getCachedResponse(idempotencyKey.trim());
                        if (cached != null) {
                            sendJsonResponse(exchange, cached.getStatusCode(), cached.getResponseBody());
                            return;
                        }
                    }

                    Order order;
                    if (quoteId != null && !quoteId.trim().isEmpty()) {
                        order = oc.createOrderFromQuote(quoteId.trim(), custId, paymentMethod, mech);
                    } else {
                        order = oc.createCustomerOrder(custId, restId, menuItemId, qty, mech);
                    }

                    this.serverRef.broadcastEvent("order_created", "{\"orderId\":\"" + escapeJson(order.getId()) + "\",\"customerId\":\"" + escapeJson(order.getCustomerId()) + "\",\"restaurantId\":\"" + escapeJson(order.getRestaurantId()) + "\",\"totalAmount\":" + order.getTotalAmount() + ",\"status\":\"" + order.getStatus().name() + "\"}");

                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    sb.append("\"success\":true,");
                    sb.append("\"orderId\":\"").append(escapeJson(order.getId())).append("\",");
                    sb.append("\"status\":\"").append(order.getStatus().name()).append("\",");
                    sb.append("\"orderState\":\"").append(order.getOrderState().name()).append("\",");
                    sb.append("\"paymentState\":\"").append(order.getPaymentState().name()).append("\",");
                    sb.append("\"deliveryState\":\"").append(order.getDeliveryState().name()).append("\",");
                    sb.append("\"settlementState\":\"").append(order.getSettlementState().name()).append("\",");
                    sb.append("\"totalAmount\":").append(order.getTotalAmount()).append(",");
                    sb.append("\"finalAmountVnd\":").append(order.getFinalAmountVnd()).append(",");
                    sb.append("\"deliveryFee\":").append(order.getDeliveryFee()).append(",");
                    sb.append("\"quoteId\":\"").append(escapeJson(order.getQuoteId() != null ? order.getQuoteId() : "")).append("\",");
                    sb.append("\"message\":\"Dat hang thanh cong! Don hang dang cho nha hang xac nhan va dieu phoi.\"");
                    sb.append("}");

                    if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                        oc.getIdempotencyManager().recordResponse(idempotencyKey.trim(), 200, sb.toString());
                    }

                    sendJsonResponse(exchange, 200, sb.toString());
                } catch (Exception e) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    private class OrderConfirmHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrderConfirmHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String orderId = params.get("orderId");
                String restId = params.get("restaurantId");

                OrderController oc = this.serverRef.getMainController().getOrderController();
                if (restId == null || restId.isEmpty()) {
                    Order existing = oc.getOrderById(orderId);
                    if (existing != null) {
                        restId = existing.getRestaurantId();
                    }
                }
                Order updated = oc.confirmOrderByRestaurant(orderId, restId);
                String driverId = (updated != null && updated.getDriverId() != null) ? updated.getDriverId() : "";
                this.serverRef.broadcastEvent("order_confirmed", "{\"orderId\":\"" + escapeJson(orderId) + "\",\"restaurantId\":\"" + escapeJson(restId) + "\",\"driverId\":\"" + escapeJson(driverId) + "\",\"status\":\"" + updated.getStatus().name() + "\"}");

                sendJsonResponse(exchange, 200, "{\"success\":true,\"orderId\":\"" + escapeJson(orderId) + "\",\"status\":\"" + updated.getStatus().name() + "\",\"driverId\":\"" + escapeJson(driverId) + "\",\"message\":\"Nha hang da xac nhan don hang!\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class OrderRejectHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrderRejectHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String orderId = params.get("orderId");
                String restId = params.get("restaurantId");
                String reason = params.get("reason");
                if (reason == null) reason = "Nha hang tu choi.";

                OrderController oc = this.serverRef.getMainController().getOrderController();
                if (restId == null || restId.isEmpty()) {
                    Order existing = oc.getOrderById(orderId);
                    if (existing != null) {
                        restId = existing.getRestaurantId();
                    }
                }
                boolean ok = oc.rejectOrderByRestaurant(orderId, restId, reason);
                if (ok) {
                    this.serverRef.broadcastEvent("order_rejected", "{\"orderId\":\"" + escapeJson(orderId) + "\",\"status\":\"CANCELLED\"}");
                }
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"orderId\":\"" + escapeJson(orderId) + "\",\"message\":\"Don hang da bi tu choi va ton kho da duoc hoan tra!\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class OrderPickupHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrderPickupHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String orderId = params.get("orderId");
                String driverId = params.get("driverId");

                OrderController oc = this.serverRef.getMainController().getOrderController();
                boolean ok = oc.markOrderPickedUp(orderId, driverId);
                if (ok) {
                    this.serverRef.broadcastEvent("order_dispatched", "{\"orderId\":\"" + escapeJson(orderId) + "\",\"driverId\":\"" + escapeJson(driverId) + "\",\"status\":\"DISPATCHED\"}");
                }
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"orderId\":\"" + escapeJson(orderId) + "\",\"status\":\"DISPATCHED\",\"message\":\"Tai xe da den quan va nhan mon!\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class OrderDeliverHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrderDeliverHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String orderId = params.get("orderId");
                String driverId = params.get("driverId");

                OrderController oc = this.serverRef.getMainController().getOrderController();
                if (driverId == null || driverId.isEmpty()) {
                    Order existing = oc.getOrderById(orderId);
                    if (existing != null) {
                        driverId = existing.getDriverId();
                    }
                }
                boolean ok = oc.markOrderDelivered(orderId, driverId);
                if (ok) {
                    this.serverRef.broadcastEvent("order_delivered", "{\"orderId\":\"" + escapeJson(orderId) + "\",\"driverId\":\"" + escapeJson(driverId) + "\",\"status\":\"DELIVERED\"}");
                }
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"orderId\":\"" + escapeJson(orderId) + "\",\"status\":\"DELIVERED\",\"message\":\"Giao hang thanh cong! Tai xe da duoc giai phong ve AVAILABLE.\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class OrderCancelHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrderCancelHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String orderId = params.get("orderId");
                String customerId = params.get("customerId");

                OrderController oc = this.serverRef.getMainController().getOrderController();
                if (customerId == null || customerId.isEmpty()) {
                    Order existing = oc.getOrderById(orderId);
                    if (existing != null) {
                        customerId = existing.getCustomerId();
                    }
                }
                boolean ok = oc.cancelOrderByCustomer(orderId, customerId);
                if (ok) {
                    this.serverRef.broadcastEvent("order_cancelled", "{\"orderId\":\"" + escapeJson(orderId) + "\",\"status\":\"CANCELLED\"}");
                }
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"orderId\":\"" + escapeJson(orderId) + "\",\"status\":\"CANCELLED\",\"message\":\"Don hang da duoc huy thanh cong va so luong mon da duoc hoan tra vao kho!\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class AutoDispatchHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public AutoDispatchHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                OrderController oc = this.serverRef.getMainController().getOrderController();
                int dispatchedCount = oc.autoDispatchPendingOrders(LockMechanism.OPTIMISTIC);
                this.serverRef.broadcastEvent("dispatch_completed", "{\"dispatchedCount\":" + dispatchedCount + "}");
                sendJsonResponse(exchange, 200, "{\"success\":true,\"dispatchedCount\":" + dispatchedCount + ",\"message\":\"Da kich hoat thuat toan dieu phoi! Da ghep thanh cong " + dispatchedCount + " don hang.\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class SimulateHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public SimulateHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String ordersStr = params.get("orders");
                if (ordersStr == null) ordersStr = params.get("orderCount");
                int numOrders = parseInt(ordersStr, 100);

                String mechStr = params.get("mechanism");
                if (mechStr == null) mechStr = params.get("strategy");
                LockMechanism mech = LockMechanism.OPTIMISTIC;
                if (mechStr != null) {
                    try {
                        mech = LockMechanism.valueOf(mechStr.toUpperCase());
                    } catch (Exception ignored) {}
                }

                SimulatorController sc = this.serverRef.getMainController().getSimulatorController();
                SimulationRun run = sc.runSimulation(numOrders, mech);

                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"success\":true,");
                sb.append("\"mechanism\":\"").append(run.getMechanism()).append("\",");
                sb.append("\"totalOrders\":").append(run.getTotalOrders()).append(",");
                sb.append("\"durationMs\":").append(run.getDurationMs()).append(",");
                sb.append("\"throughput\":").append(run.getThroughputOpsPerSec()).append(",");
                sb.append("\"dropPercent\":").append(run.getThroughputDropPercent()).append(",");
                sb.append("\"doubleAssignments\":").append(run.getDoubleAssignments()).append(",");
                sb.append("\"oversells\":").append(run.getOversells()).append(",");
                sb.append("\"driverOverloads\":").append(run.getDriverOverloads()).append(",");
                sb.append("\"anomalyCount\":").append(run.getTotalErrors()).append(",");
                sb.append("\"totalAnomalies\":").append(run.getTotalErrors()).append(",");
                sb.append("\"timeMs\":").append(run.getDurationMs()).append(",");
                sb.append("\"message\":\"Chay mo phong tuong tranh thanh cong!\"");
                sb.append("}");

                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class RestockHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public RestockHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                this.serverRef.restockAllItems(50);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Tat ca cac mon an da duoc cap bo sung ton kho = 50 suat!\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class ApiDocsHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public ApiDocsHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"openapi\":\"3.0.1\",");
            sb.append("\"info\":{");
            sb.append("\"title\":\"Food Delivery Quy Nhon API\",");
            sb.append("\"version\":\"2.0.0\",");
            sb.append("\"description\":\"He thong API RESTful thuc te cho Food Delivery Quy Nhon (LAB211)\"");
            sb.append("},");
            sb.append("\"servers\":[{\"url\":\"http://localhost:").append(this.serverRef.getPort()).append("\"}],");
            sb.append("\"endpoints\":[");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/status\",\"summary\":\"Kiem tra trang thai may chu backend\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/stats\",\"summary\":\"Bao cao KPI tong the he thong\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/auth/demo-accounts\",\"summary\":\"Lay danh sach tai khoan demo cac vai tro\"},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/auth/login\",\"summary\":\"Dang nhap tai khoan\",\"body\":[\"username\",\"password\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/auth/register\",\"summary\":\"Dang ky khach hang moi\",\"body\":[\"username\",\"password\",\"fullName\",\"phone\",\"address\",\"lat\",\"lon\"]},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/restaurants\",\"summary\":\"Lay danh sach nha hang doi tac Quy Nhon\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/menu\",\"summary\":\"Lay thuc don mon an\",\"params\":[\"restaurantId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/menu/stock\",\"summary\":\"Cap nhat ton kho mon an\",\"body\":[\"menuItemId\",\"delta\"]},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/drivers\",\"summary\":\"Lay danh sach tai xe va toa do GPS\"},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/drivers/status\",\"summary\":\"Bat/tat trang thai truc tuyen tai xe\",\"body\":[\"driverId\",\"status\"]},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/customers\",\"summary\":\"Lay danh sach khach hang va dia chi phuong xa Quy Nhon\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/orders\",\"summary\":\"Truy van danh sach don hang\",\"params\":[\"customerId\",\"restaurantId\",\"driverId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/orders\",\"summary\":\"Khach hang tao don dat mon\",\"body\":[\"customerId\",\"restaurantId\",\"menuItemId\",\"quantity\",\"mechanism\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/orders/confirm\",\"summary\":\"Bep nha hang tiep nhan va bat dau nau\",\"body\":[\"orderId\",\"restaurantId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/orders/reject\",\"summary\":\"Bep nha hang tu choi don va hoan kho\",\"body\":[\"orderId\",\"restaurantId\",\"reason\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/orders/pickup\",\"summary\":\"Tai xe xac nhan da lay mon tu quan (DISPATCHED)\",\"body\":[\"orderId\",\"driverId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/orders/deliver\",\"summary\":\"Tai xe xac nhan da giao hang thanh cong (DELIVERED)\",\"body\":[\"orderId\",\"driverId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/orders/cancel\",\"summary\":\"Khach hang huy don hang va hoan kho (CANCELLED)\",\"body\":[\"orderId\",\"customerId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/dispatch/auto\",\"summary\":\"Kich hoat thuat toan tu dong ghep tai xe gan nhat\"},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/simulate\",\"summary\":\"Chay kiem thu stress test dong thoi 4 co che khoa\",\"body\":[\"orders\",\"mechanism\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/admin/restock\",\"summary\":\"Khoi phuc ton kho 50 suat cho cac quan an\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/events\",\"summary\":\"Server-Sent Events (SSE) stream realtime thong bao trang thai don hang\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/admin/overview\",\"summary\":\"Bao cao tai chinh tong the GMV, doanh thu san 20%, tien quan 80%, thu nhap tai xe\"},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/admin/driver-applications\",\"summary\":\"Lay danh sach ho so tai xe dang cho duyet\"},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/admin/driver-applications/approve\",\"summary\":\"Phe duyet ho so tai xe moi\",\"body\":[\"applicationId\"]},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/admin/driver-applications/reject\",\"summary\":\"Tu choi ho so tai xe\",\"body\":[\"applicationId\",\"reason\"]},");
            sb.append("{\"method\":\"GET\",\"path\":\"/api/vouchers\",\"summary\":\"Lay danh sach ma giam gia khuyen mai Quy Nhon dang ap dung\"},");
            sb.append("{\"method\":\"POST\",\"path\":\"/api/vouchers/apply\",\"summary\":\"Kiem tra va ap dung voucher giam gia vao gio hang\",\"body\":[\"code\",\"totalFoodAmount\",\"deliveryFee\"]}");
            sb.append("]");
            sb.append("}");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    // =========================================================================
    // FULLSTACK ENTERPRISE EXTENSIONS: SSE, ADMIN OVERVIEW, VOUCHERS
    // =========================================================================

    private class EventsHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public EventsHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            exchange.getResponseHeaders().set("Content-Type", "text/event-stream; charset=UTF-8");
            exchange.getResponseHeaders().set("Cache-Control", "no-cache, no-transform");
            exchange.getResponseHeaders().set("Connection", "keep-alive");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, 0);

            OutputStream os = exchange.getResponseBody();
            this.serverRef.registerSseClient(os);

            try {
                byte[] init = ("event: connected\ndata: {\"status\":\"CONNECTED\",\"message\":\"Real-time Server-Sent Events (SSE) Bus Active\",\"clients\":" + this.serverRef.getSseClientCount() + ",\"time\":" + System.currentTimeMillis() + "}\n\n").getBytes(StandardCharsets.UTF_8);
                os.write(init);
                os.flush();
            } catch (Exception e) {
                this.serverRef.removeSseClient(os);
            }
        }
    }

    private class AdminOverviewHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public AdminOverviewHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                MainController mc = this.serverRef.getMainController();
                List<Order> orders = mc.getOrderController().getAllOrders();
                List<Restaurant> rests = mc.getRestaurantController().getAllRestaurants();
                List<Driver> drivers = mc.getDriverController().getAllDrivers();
                List<Customer> custs = mc.getCustomerController().getAllCustomers();
                List<DriverApplication> pendingApps = mc.getAdminController().getPendingDriverApplications();

                double totalGMV = 0.0;
                double deliveredGMV = 0.0;
                double totalFoodDelivered = 0.0;
                double totalDeliveryFeeDelivered = 0.0;

                int pendingOrders = 0;
                int confirmedOrders = 0;
                int dispatchedOrders = 0;
                int deliveredOrders = 0;
                int cancelledOrders = 0;

                Map<String, Integer> restOrderCount = new HashMap<>();
                Map<String, Double> restRevenue = new HashMap<>();

                if (orders != null) {
                    for (Order o : orders) {
                        totalGMV += o.getTotalAmount();
                        if (o.getStatus() == OrderStatus.PENDING) pendingOrders++;
                        else if (o.getStatus() == OrderStatus.CONFIRMED) confirmedOrders++;
                        else if (o.getStatus() == OrderStatus.DISPATCHED) dispatchedOrders++;
                        else if (o.getStatus() == OrderStatus.DELIVERED) {
                            deliveredOrders++;
                            deliveredGMV += o.getTotalAmount();
                            totalFoodDelivered += o.getTotalFoodAmount();
                            totalDeliveryFeeDelivered += o.getDeliveryFee();

                            String rid = o.getRestaurantId();
                            restOrderCount.put(rid, restOrderCount.getOrDefault(rid, 0) + 1);
                            restRevenue.put(rid, restRevenue.getOrDefault(rid, 0.0) + o.getTotalFoodAmount());
                        } else if (o.getStatus() == OrderStatus.CANCELLED) {
                            cancelledOrders++;
                        }
                    }
                }

                double platformFee = totalFoodDelivered * 0.20; // 20% platform commission
                double restaurantPayout = totalFoodDelivered * 0.80; // 80% restaurant payout
                double driverEarnings = totalDeliveryFeeDelivered; // 100% delivery fee to drivers

                int availDrivers = 0;
                int busyDrivers = 0;
                int offlineDrivers = 0;
                if (drivers != null) {
                    for (Driver d : drivers) {
                        if (d.getStatus() == DriverStatus.AVAILABLE) availDrivers++;
                        else if (d.getStatus() == DriverStatus.BUSY) busyDrivers++;
                        else offlineDrivers++;
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"totalGMV\":").append(totalGMV).append(",");
                sb.append("\"deliveredGMV\":").append(deliveredGMV).append(",");
                sb.append("\"totalFoodDelivered\":").append(totalFoodDelivered).append(",");
                sb.append("\"totalDeliveryFeeDelivered\":").append(totalDeliveryFeeDelivered).append(",");
                sb.append("\"platformFee\":").append(platformFee).append(",");
                sb.append("\"restaurantPayout\":").append(restaurantPayout).append(",");
                sb.append("\"driverEarnings\":").append(driverEarnings).append(",");
                sb.append("\"totalOrders\":").append(orders != null ? orders.size() : 0).append(",");
                sb.append("\"pendingOrders\":").append(pendingOrders).append(",");
                sb.append("\"confirmedOrders\":").append(confirmedOrders).append(",");
                sb.append("\"dispatchedOrders\":").append(dispatchedOrders).append(",");
                sb.append("\"deliveredOrders\":").append(deliveredOrders).append(",");
                sb.append("\"cancelledOrders\":").append(cancelledOrders).append(",");
                sb.append("\"totalRestaurants\":").append(rests != null ? rests.size() : 0).append(",");
                sb.append("\"totalDrivers\":").append(drivers != null ? drivers.size() : 0).append(",");
                sb.append("\"availableDrivers\":").append(availDrivers).append(",");
                sb.append("\"busyDrivers\":").append(busyDrivers).append(",");
                sb.append("\"offlineDrivers\":").append(offlineDrivers).append(",");
                sb.append("\"totalCustomers\":").append(custs != null ? custs.size() : 0).append(",");
                sb.append("\"pendingDriverAppsCount\":").append(pendingApps != null ? pendingApps.size() : 0);
                sb.append("}");

                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class AdminDriverAppsHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public AdminDriverAppsHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            try {
                AdminController ac = this.serverRef.getMainController().getAdminController();
                List<DriverApplication> apps = ac.getAllDriverApplications();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                if (apps != null) {
                    for (int i = 0; i < apps.size(); i++) {
                        DriverApplication a = apps.get(i);
                        if (i > 0) sb.append(",");
                        sb.append("{");
                        sb.append("\"id\":\"").append(escapeJson(a.getId())).append("\",");
                        sb.append("\"username\":\"").append(escapeJson(a.getUsername())).append("\",");
                        sb.append("\"fullName\":\"").append(escapeJson(a.getFullName())).append("\",");
                        sb.append("\"phone\":\"").append(escapeJson(a.getPhone())).append("\",");
                        sb.append("\"licensePlate\":\"").append(escapeJson(a.getLicensePlate())).append("\",");
                        sb.append("\"vehicleType\":\"").append(escapeJson(a.getVehicleType())).append("\",");
                        sb.append("\"status\":\"").append(escapeJson(a.getStatus())).append("\",");
                        sb.append("\"note\":\"").append(escapeJson(a.getNote())).append("\"");
                        sb.append("}");
                    }
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class AdminDriverAppApproveHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public AdminDriverAppApproveHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String appId = params.get("applicationId");
                if (appId == null || appId.isEmpty()) appId = params.get("id");

                AdminController ac = this.serverRef.getMainController().getAdminController();
                User user = ac.approveDriverApplication(appId);
                if (user != null) {
                    this.serverRef.broadcastEvent("driver_approved", "{\"applicationId\":\"" + escapeJson(appId) + "\",\"driverId\":\"" + escapeJson(user.getLinkedDriverId()) + "\"}");
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"applicationId\":\"" + escapeJson(appId) + "\",\"driverId\":\"" + escapeJson(user.getLinkedDriverId()) + "\",\"message\":\"Da phe duyet ho so tai xe thanh cong! Tai khoan: " + escapeJson(user.getUsername()) + "\"}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Khong the phe duyet ho so hoac ho so khong o trang thai PENDING!\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class AdminDriverAppRejectHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public AdminDriverAppRejectHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String appId = params.get("applicationId");
                if (appId == null || appId.isEmpty()) appId = params.get("id");
                String reason = params.get("reason");
                if (reason == null) reason = "Khong dap ung tieu chuan ho so tai xe.";

                AdminController ac = this.serverRef.getMainController().getAdminController();
                boolean ok = ac.rejectDriverApplication(appId, reason);
                sendJsonResponse(exchange, 200, "{\"success\":" + ok + ",\"applicationId\":\"" + escapeJson(appId) + "\",\"message\":\"Da tu choi ho so tai xe!\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class VoucherListHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public VoucherListHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            String json = "["
                    + "{\"code\":\"QUYNHON20\",\"discountPercent\":20,\"maxDiscount\":50000,\"minOrder\":50000,\"title\":\"Khao 20% Am Thuc Quy Nhon\",\"description\":\"Giam 20% toi da 50.000d cho don tu 50.000d\"},"
                    + "{\"code\":\"FREESHIP\",\"discountPercent\":100,\"maxDiscount\":20000,\"minOrder\":30000,\"title\":\"Freeship Quy Nhon\",\"description\":\"Mien phi ship 100% (toi da 20.000d) cho don tu 30.000d\"},"
                    + "{\"code\":\"BANHXEO\",\"flatDiscount\":15000,\"minOrder\":40000,\"title\":\"Uu Dai Banh Xeo Tom Nhay\",\"description\":\"Giam ngay 15.000d cho moi don tu 40.000d\"},"
                    + "{\"code\":\"CHAOMUNG\",\"flatDiscount\":25000,\"minOrder\":60000,\"title\":\"Chao Ban Moi Quy Nhon\",\"description\":\"Giam ngay 25.000d cho don hang tu 60.000d\"}"
                    + "]";
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class VoucherApplyHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public VoucherApplyHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String code = params.get("code");
                if (code != null) code = code.trim().toUpperCase();
                double foodAmount = parseDouble(params.get("totalFoodAmount"), 0.0);
                if (foodAmount <= 0) foodAmount = parseDouble(params.get("foodAmount"), 0.0);
                double deliveryFee = parseDouble(params.get("deliveryFee"), 15000.0);

                if (code == null || code.isEmpty()) {
                    sendJsonResponse(exchange, 400, "{\"valid\":false,\"message\":\"Vui long nhap ma giam gia!\"}");
                    return;
                }

                double discount = 0.0;
                String desc = "";
                if ("QUYNHON20".equals(code)) {
                    if (foodAmount < 50000) {
                        sendJsonResponse(exchange, 200, "{\"valid\":false,\"message\":\"Ma QUYNHON20 chi ap dung cho don hang co tien mon tu 50.000d tro len!\"}");
                        return;
                    }
                    discount = Math.min(foodAmount * 0.20, 50000.0);
                    desc = "Giam 20% toi da 50.000d";
                } else if ("FREESHIP".equals(code)) {
                    if (foodAmount < 30000) {
                        sendJsonResponse(exchange, 200, "{\"valid\":false,\"message\":\"Ma FREESHIP chi ap dung cho don tu 30.000d tro len!\"}");
                        return;
                    }
                    discount = Math.min(deliveryFee, 20000.0);
                    desc = "Mien phi van chuyen (toi da 20.000d)";
                } else if ("BANHXEO".equals(code)) {
                    if (foodAmount < 40000) {
                        sendJsonResponse(exchange, 200, "{\"valid\":false,\"message\":\"Ma BANHXEO chi ap dung cho don tu 40.000d tro len!\"}");
                        return;
                    }
                    discount = 15000.0;
                    desc = "Giam ngay 15.000d";
                } else if ("CHAOMUNG".equals(code)) {
                    if (foodAmount < 60000) {
                        sendJsonResponse(exchange, 200, "{\"valid\":false,\"message\":\"Ma CHAOMUNG chi ap dung cho don tu 60.000d tro len!\"}");
                        return;
                    }
                    discount = 25000.0;
                    desc = "Giam ngay 25.000d cho khach hang moi";
                } else {
                    sendJsonResponse(exchange, 200, "{\"valid\":false,\"message\":\"Ma giam gia '" + escapeJson(code) + "' khong ton tai hoac da het han!\"}");
                    return;
                }

                double finalTotal = Math.max(0.0, (foodAmount + deliveryFee) - discount);
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"valid\":true,");
                sb.append("\"code\":\"").append(escapeJson(code)).append("\",");
                sb.append("\"discountAmount\":").append(discount).append(",");
                sb.append("\"finalFoodAmount\":").append(foodAmount).append(",");
                sb.append("\"finalDeliveryFee\":").append(deliveryFee).append(",");
                sb.append("\"finalTotal\":").append(finalTotal).append(",");
                sb.append("\"description\":\"").append(escapeJson(desc)).append("\",");
                sb.append("\"message\":\"Ap dung ma ").append(escapeJson(code)).append(" thanh cong! Tiet kiem ").append((long)discount).append("d.\"");
                sb.append("}");

                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"valid\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class QuotesHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public QuotesHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            OrderController oc = this.serverRef.getMainController().getOrderController();
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    String body = readBody(exchange);
                    Map<String, String> params = parseJsonSimple(body);
                    String custId = params.get("customerId");
                    String restId = params.get("restaurantId");
                    String itemId = params.get("menuItemId");
                    int qty = 1;
                    if (params.containsKey("quantity")) {
                        try { qty = Integer.parseInt(params.get("quantity").replaceAll("[^0-9]", "")); } catch (Exception ignored) {}
                    }
                    String voucher = params.get("voucherCode");

                    Quote q = oc.getQuoteService().createQuote(custId, restId, itemId, qty, voucher);
                    if (q == null) {
                        sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Khong the tao bao gia (Quote) cho don hang\"}");
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    sb.append("\"success\":true,");
                    sb.append("\"quoteId\":\"").append(escapeJson(q.getId())).append("\",");
                    sb.append("\"customerId\":\"").append(escapeJson(q.getCustomerId())).append("\",");
                    sb.append("\"restaurantId\":\"").append(escapeJson(q.getRestaurantId())).append("\",");
                    sb.append("\"foodAmountVnd\":").append(q.getFoodAmountVnd()).append(",");
                    sb.append("\"deliveryFeeVnd\":").append(q.getDeliveryFeeVnd()).append(",");
                    sb.append("\"discountAmountVnd\":").append(q.getDiscountAmountVnd()).append(",");
                    sb.append("\"finalTotalVnd\":").append(q.getFinalTotalVnd()).append(",");
                    sb.append("\"voucherCode\":\"").append(escapeJson(q.getVoucherCode())).append("\",");
                    sb.append("\"expiresAtEpochMs\":").append(q.getExpiresAtEpochMs()).append(",");
                    sb.append("\"remainingSeconds\":").append(q.getRemainingSeconds()).append(",");
                    sb.append("\"valid\":").append(q.isValid());
                    sb.append("}");
                    sendJsonResponse(exchange, 200, sb.toString());
                } catch (Exception e) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
                }
            } else if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    Map<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                    String qId = query.get("quoteId");
                    if (qId == null || qId.isEmpty()) {
                        sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Missing quoteId parameter\"}");
                        return;
                    }
                    Quote q = oc.getQuoteService().getQuote(qId);
                    if (q == null) {
                        sendJsonResponse(exchange, 404, "{\"success\":false,\"message\":\"Bao gia khong ton tai hoac da het han\"}");
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    sb.append("\"success\":true,");
                    sb.append("\"quoteId\":\"").append(escapeJson(q.getId())).append("\",");
                    sb.append("\"customerId\":\"").append(escapeJson(q.getCustomerId())).append("\",");
                    sb.append("\"restaurantId\":\"").append(escapeJson(q.getRestaurantId())).append("\",");
                    sb.append("\"foodAmountVnd\":").append(q.getFoodAmountVnd()).append(",");
                    sb.append("\"deliveryFeeVnd\":").append(q.getDeliveryFeeVnd()).append(",");
                    sb.append("\"discountAmountVnd\":").append(q.getDiscountAmountVnd()).append(",");
                    sb.append("\"finalTotalVnd\":").append(q.getFinalTotalVnd()).append(",");
                    sb.append("\"voucherCode\":\"").append(escapeJson(q.getVoucherCode())).append("\",");
                    sb.append("\"expiresAtEpochMs\":").append(q.getExpiresAtEpochMs()).append(",");
                    sb.append("\"remainingSeconds\":").append(q.getRemainingSeconds()).append(",");
                    sb.append("\"valid\":").append(q.isValid());
                    sb.append("}");
                    sendJsonResponse(exchange, 200, sb.toString());
                } catch (Exception e) {
                    sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    private class DriverOffersHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public DriverOffersHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                Map<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                String drvId = query.get("driverId");
                OrderController oc = this.serverRef.getMainController().getOrderController();
                List<DriverOffer> offers = oc.getDispatchEngine().getPendingOffersForDriver(drvId);
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < offers.size(); i++) {
                    DriverOffer o = offers.get(i);
                    if (i > 0) sb.append(",");
                    sb.append("{");
                    sb.append("\"offerId\":\"").append(escapeJson(o.getId())).append("\",");
                    sb.append("\"deliveryTaskId\":\"").append(escapeJson(o.getDeliveryTaskId())).append("\",");
                    sb.append("\"orderId\":\"").append(escapeJson(o.getOrderId())).append("\",");
                    sb.append("\"driverId\":\"").append(escapeJson(o.getDriverId())).append("\",");
                    sb.append("\"restaurantName\":\"").append(escapeJson(o.getRestaurantName())).append("\",");
                    sb.append("\"restaurantAddress\":\"").append(escapeJson(o.getRestaurantAddress())).append("\",");
                    sb.append("\"deliveryAddress\":\"").append(escapeJson(o.getDeliveryAddress())).append("\",");
                    sb.append("\"deliveryFeeVnd\":").append(o.getDeliveryFeeVnd()).append(",");
                    sb.append("\"estimatedDistanceKm\":").append(String.format(java.util.Locale.US, "%.1f", o.getEstimatedDistanceKm())).append(",");
                    sb.append("\"offeredAtEpochMs\":").append(o.getOfferedAtEpochMs()).append(",");
                    sb.append("\"expiresAtEpochMs\":").append(o.getExpiresAtEpochMs()).append(",");
                    sb.append("\"remainingSeconds\":").append(o.getRemainingSeconds()).append(",");
                    sb.append("\"status\":\"").append(escapeJson(o.getStatus())).append("\"");
                    sb.append("}");
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class DriverOfferAcceptHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public DriverOfferAcceptHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String offerId = params.get("offerId");
                String driverId = params.get("driverId");
                OrderController oc = this.serverRef.getMainController().getOrderController();
                boolean accepted = oc.getDispatchEngine().acceptOffer(driverId, offerId);
                if (accepted) {
                    this.serverRef.broadcastEvent("driver_offer_accepted", "{\"offerId\":\"" + escapeJson(offerId) + "\",\"driverId\":\"" + escapeJson(driverId) + "\"}");
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Nhan don thanh cong! Hay di chuyen den nha hang lay mon.\"}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Don hang da het han 20s hoac da duoc tai xe khac nhan.\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class DriverOfferRejectHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public DriverOfferRejectHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String offerId = params.get("offerId");
                String driverId = params.get("driverId");
                OrderController oc = this.serverRef.getMainController().getOrderController();
                oc.getDispatchEngine().rejectOffer(offerId, driverId);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Da tu choi chuyen xe. Don hang se chuyen sang tai xe khac.\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class FinancialLedgerHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public FinancialLedgerHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                OrderController oc = this.serverRef.getMainController().getOrderController();
                List<FinancialLedgerEntry> entries = oc.getPaymentService().getLedgerRepo().findAll();
                boolean balanced = oc.getPaymentService().getLedgerRepo().verifyLedgerBalance();
                long totalDebit = 0;
                long totalCredit = 0;
                for (FinancialLedgerEntry e : entries) {
                    totalDebit += e.getDebitVnd();
                    totalCredit += e.getCreditVnd();
                }

                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"success\":true,");
                sb.append("\"isBalanced\":").append(balanced).append(",");
                sb.append("\"totalDebitVnd\":").append(totalDebit).append(",");
                sb.append("\"totalCreditVnd\":").append(totalCredit).append(",");
                sb.append("\"entryCount\":").append(entries.size()).append(",");
                sb.append("\"entries\":[");
                for (int i = 0; i < entries.size(); i++) {
                    FinancialLedgerEntry e = entries.get(i);
                    if (i > 0) sb.append(",");
                    sb.append("{");
                    sb.append("\"id\":\"").append(escapeJson(e.getId())).append("\",");
                    sb.append("\"transactionRef\":\"").append(escapeJson(e.getTransactionRef())).append("\",");
                    sb.append("\"orderId\":\"").append(escapeJson(e.getOrderId())).append("\",");
                    sb.append("\"accountCode\":\"").append(escapeJson(e.getAccountCode())).append("\",");
                    sb.append("\"debitVnd\":").append(e.getDebitVnd()).append(",");
                    sb.append("\"creditVnd\":").append(e.getCreditVnd()).append(",");
                    sb.append("\"currency\":\"").append(escapeJson(e.getCurrency())).append("\",");
                    sb.append("\"occurredAt\":\"").append(escapeJson(e.getOccurredAt())).append("\",");
                    sb.append("\"sourceType\":\"").append(escapeJson(e.getSourceType())).append("\",");
                    sb.append("\"note\":\"").append(escapeJson(e.getNote())).append("\"");
                    sb.append("}");
                }
                sb.append("]");
                sb.append("}");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class PaymentWebhookHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public PaymentWebhookHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                String body = readBody(exchange);
                Map<String, String> params = parseJsonSimple(body);
                String orderId = params.get("orderId");
                String txRef = params.get("transactionRef");
                if (txRef == null || txRef.isEmpty()) {
                    txRef = "VQR_TX_" + System.currentTimeMillis();
                }
                long amount = 0L;
                if (params.containsKey("amountVnd")) {
                    try { amount = Long.parseLong(params.get("amountVnd").replaceAll("[^0-9]", "")); } catch (Exception ignored) {}
                }
                String status = params.get("status");
                if (status == null || status.isEmpty()) status = "SUCCESS";

                OrderController oc = this.serverRef.getMainController().getOrderController();
                Order o = oc.getOrderById(orderId);
                if (o == null) {
                    sendJsonResponse(exchange, 404, "{\"success\":false,\"message\":\"Order not found\"}");
                    return;
                }
                if (amount <= 0) {
                    amount = o.getFinalAmountVnd() > 0 ? o.getFinalAmountVnd() : (long)o.getTotalAmount();
                }

                boolean ok = oc.getPaymentService().processPaymentWebhook(orderId, txRef, amount, status);
                if (ok) {
                    this.serverRef.broadcastEvent("payment_captured", "{\"orderId\":\"" + escapeJson(orderId) + "\",\"transactionRef\":\"" + escapeJson(txRef) + "\",\"amountVnd\":" + amount + "}");
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Thanh toan VietQR duoc xac nhan va ghi so cai thanh cong!\",\"transactionRef\":\"" + escapeJson(txRef) + "\"}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Xu ly webhook thanh toan that bai\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class OrderSnapshotHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public OrderSnapshotHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            try {
                Map<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                String orderId = query.get("orderId");
                OrderController oc = this.serverRef.getMainController().getOrderController();
                OrderSnapshot s = oc.getSnapshotRepository().findByOrderId(orderId);
                if (s == null) {
                    sendJsonResponse(exchange, 404, "{\"success\":false,\"message\":\"Snapshot khong ton tai cho don hang nay\"}");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"success\":true,");
                sb.append("\"snapshotId\":\"").append(escapeJson(s.getId())).append("\",");
                sb.append("\"orderId\":\"").append(escapeJson(s.getOrderId())).append("\",");
                sb.append("\"customerId\":\"").append(escapeJson(s.getCustomerId())).append("\",");
                sb.append("\"customerName\":\"").append(escapeJson(s.getCustomerName())).append("\",");
                sb.append("\"customerPhone\":\"").append(escapeJson(s.getCustomerPhone())).append("\",");
                sb.append("\"deliveryAddress\":\"").append(escapeJson(s.getDeliveryAddress())).append("\",");
                sb.append("\"restaurantId\":\"").append(escapeJson(s.getRestaurantId())).append("\",");
                sb.append("\"restaurantName\":\"").append(escapeJson(s.getRestaurantName())).append("\",");
                sb.append("\"restaurantAddress\":\"").append(escapeJson(s.getRestaurantAddress())).append("\",");
                sb.append("\"foodAmountVnd\":").append(s.getFoodAmountVnd()).append(",");
                sb.append("\"deliveryFeeVnd\":").append(s.getDeliveryFeeVnd()).append(",");
                sb.append("\"discountAmountVnd\":").append(s.getDiscountAmountVnd()).append(",");
                sb.append("\"finalTotalVnd\":").append(s.getFinalTotalVnd()).append(",");
                sb.append("\"voucherCode\":\"").append(escapeJson(s.getVoucherCode())).append("\",");
                sb.append("\"paymentMethod\":\"").append(escapeJson(s.getPaymentMethod())).append("\",");
                sb.append("\"snapshotTimestamp\":\"").append(escapeJson(s.getSnapshotTimestamp())).append("\"");
                sb.append("}");
                sendJsonResponse(exchange, 200, sb.toString());
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class StaticFileHandler implements HttpHandler {
        private FoodDeliveryWebServer serverRef;
        public StaticFileHandler(FoodDeliveryWebServer serverRef) {
            this.serverRef = serverRef;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }

            String path = exchange.getRequestURI().getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            File targetFile = new File(this.serverRef.getWebRootDir(), path);
            if (!targetFile.exists() || targetFile.isDirectory()) {
                targetFile = new File(this.serverRef.getWebRootDir(), "index.html");
            }

            if (!targetFile.exists()) {
                sendErrorResponse(exchange, 404, "Not Found");
                return;
            }

            String mimeType = getMimeType(targetFile.getName());
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, targetFile.length());

            try (InputStream is = new FileInputStream(targetFile);
                 OutputStream os = exchange.getResponseBody()) {
                byte[] buf = new byte[8192];
                int len;
                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
            }
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization, Idempotency-Key");
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length > 0 ? bytes.length : -1);
        if (bytes.length > 0) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String json = "{\"error\":true,\"message\":\"" + escapeJson(message) + "\"}";
        sendJsonResponse(exchange, statusCode, json);
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                if (idx > 0) {
                    map.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                } else if (idx == -1) {
                    map.put(URLDecoder.decode(pair, "UTF-8"), "");
                }
            } catch (Exception ignored) {}
        }
        return map;
    }

    private Map<String, String> parseJsonSimple(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) return map;
        String trimmed = json.trim();
        if (trimmed.startsWith("{")) trimmed = trimmed.substring(1);
        if (trimmed.endsWith("}")) trimmed = trimmed.substring(0, trimmed.length() - 1);

        String[] tokens = trimmed.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String token : tokens) {
            String[] kv = token.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (kv.length == 2) {
                String k = kv[0].trim().replaceAll("^\"|\"$", "");
                String v = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(k, v);
            }
        }
        return map;
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private double parseDouble(String s, double fallback) {
        if (s == null) return fallback;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private int parseInt(String s, int fallback) {
        if (s == null) return fallback;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private String getMimeType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=UTF-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (lower.endsWith(".json")) return "application/json; charset=UTF-8";
        if (lower.endsWith(".css")) return "text/css; charset=UTF-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".xml")) return "application/xml; charset=UTF-8";
        if (lower.endsWith(".txt")) return "text/plain; charset=UTF-8";
        return "application/octet-stream";
    }

    /**
     * Entry point to launch the Food Delivery Web Server & Real-World Application.
     */
    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ignored) {}
        }
        try {
            FoodDeliveryWebServer webServer = new FoodDeliveryWebServer(port);
            webServer.start();
        } catch (Exception e) {
            System.err.println("Failed to start web server on port " + port + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
