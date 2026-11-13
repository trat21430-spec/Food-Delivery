"""
Script sinh trọn bộ Diagram theo 7 Phân hệ chức năng (Subsystems Architecture)
Dành cho Đồ án LAB211: Food Delivery Network Simulation (FDS).
Bao gồm:
1. File Markdown (docs/subsystems/subsystems_diagram.md) với 7 biểu đồ Mermaid chi tiết.
2. File Draw.io Multi-Tab (docs/subsystems/Subsystems_Architecture.drawio) chứa 7 tab phân hệ + 1 tab tổng thể.
3. Trình duyệt Web HTML5 (docs/subsystems/subsystems_view.html) để nhóm mở trình chiếu hoặc tra cứu theo từng phân hệ.
4. Đồng bộ vào các thư mục D:/Lab211-Group1/docs/subsystems và D:/Lab211-Group1/diagram/subsystems.
"""

import os
import shutil

SUB_INFO = [
    {
        "id": "SUB1",
        "num": 1,
        "title": "Phân hệ 1: Xác thực & Quản lý Truy cập",
        "en": "Authentication & Access Management",
        "owner": "Đỗ Tấn Vũ (QE170159)",
        "role": "Admin & Security Dev",
        "color": "#1E3A8A", # Deep Blue
        "accent": "#38BDF8",
        "ucs": "UC01 – UC05",
        "uc_list": [
            ("UC01", "Đăng ký tài khoản Khách hàng mới"),
            ("UC02", "Đăng nhập Hệ thống (PBKDF2 with Salt)"),
            ("UC03", "Đăng xuất khỏi Phiên làm việc"),
            ("UC04", "Xem danh sách Tài khoản mẫu Demo"),
            ("UC05", "Nộp hồ sơ đăng ký Làm Tài xế (Chờ duyệt)")
        ],
        "classes": [
            ("AuthController", "controller", "Điều phối xác thực, login/logout, check session"),
            ("UserRepository", "repository", "Lưu trữ và đọc/ghi users.csv"),
            ("AccountRepository", "repository", "Bảo mật PBKDF2WithHmacSHA256, 65.536 iterations"),
            ("User", "model", "Thực thể người dùng cơ bản"),
            ("UserRole", "model", "Enum: CUSTOMER, RESTAURANT, DRIVER, ADMIN"),
            ("UserAccount", "model", "Tài khoản kèm salt và hash"),
            ("AuthSession", "model", "Phiên đăng nhập kèm token và thời hạn"),
            ("AccountRole", "model", "Enum phân quyền truy cập hệ thống"),
            ("InvalidInputException", "exception", "Ngoại lệ khi thông tin đăng nhập/đăng ký sai")
        ],
        "csv": ["data/users.csv"],
        "desc": "Quản lý toàn bộ vòng đời tài khoản người dùng, cơ chế bảo mật tiêu chuẩn ngân hàng PBKDF2 chống tấn công Rainbow table và phân quyền RBAC."
    },
    {
        "id": "SUB2",
        "num": 2,
        "title": "Phân hệ 2: Khách hàng & Đặt hàng Quy Nhơn",
        "en": "Customer Portal & Food Ordering",
        "owner": "Võ Thảo Nguyên (QE200064)",
        "role": "Customer Portal Dev",
        "color": "#0369A1", # Sky Blue
        "accent": "#0284C7",
        "ucs": "UC06 – UC15",
        "uc_list": [
            ("UC06", "Xem danh sách Nhà hàng & Thực đơn"),
            ("UC07", "Đặt món & Khởi tạo Đơn hàng"),
            ("UC08", "Trừ tồn kho Món ăn tức thời"),
            ("UC09", "Tính phí ship theo Haversine GPS Quy Nhơn"),
            ("UC10", "Xác thực GPS Cửa hàng & Khách hàng"),
            ("UC11", "Xem lịch sử & Chi tiết đơn hàng"),
            ("UC12", "Hủy đơn hàng (Khi đang PENDING)"),
            ("UC13", "Hoàn trả số lượng Tồn kho (Rollback Stock)"),
            ("UC14", "Xem thông tin Tài khoản cá nhân"),
            ("UC15", "Kiểm tra Hộp thư Thông báo tài xế & ETA")
        ],
        "classes": [
            ("CustomerController", "controller", "Xử lý nghiệp vụ khách hàng xem menu, đặt và hủy đơn"),
            ("CustomerRepository", "repository", "Quản lý dữ liệu customers.csv"),
            ("OrderRepository", "repository", "Quản lý dữ liệu orders.csv"),
            ("OrderItemRepository", "repository", "Quản lý dữ liệu chi tiết món order_items.csv"),
            ("Customer", "model", "Thực thể khách hàng kèm tọa độ GPS"),
            ("Order", "model", "Thực thể đơn hàng kèm trạng thái, cước phí, version"),
            ("OrderItem", "model", "Chi tiết từng món trong đơn"),
            ("OrderStatus", "model", "Enum trạng thái: PENDING, ACCEPTED, ASSIGNED..."),
            ("OrderView", "view", "Giao diện Console hiển thị giỏ hàng và đơn hàng"),
            ("OversellException", "exception", "Ngoại lệ chặn bán quá tồn kho"),
            ("EntityNotFoundException", "exception", "Ngoại lệ khi không tìm thấy món/nhà hàng")
        ],
        "csv": ["data/customers.csv", "data/orders.csv", "data/order_items.csv"],
        "desc": "Cổng trải nghiệm khách hàng tại Quy Nhơn: duyệt menu, tính cước khoảng cách Haversine chuẩn xác, đặt đơn với kiểm tra tồn kho và cơ chế hoàn stock khi hủy đơn."
    },
    {
        "id": "SUB3",
        "num": 3,
        "title": "Phân hệ 3: Quản lý Nhà hàng & Thực đơn",
        "en": "Restaurant Portal & Menu Management",
        "owner": "Huỳnh Nguyễn Quốc Huy (QE200031)",
        "role": "Restaurant Portal Dev",
        "color": "#B45309", # Amber/Orange
        "accent": "#F59E0B",
        "ucs": "UC16 – UC22",
        "uc_list": [
            ("UC16", "Xem thực đơn & Tình trạng tồn kho"),
            ("UC17", "Cập nhật đơn giá & Bổ sung tồn kho"),
            ("UC18", "Xem danh sách Đơn hàng chờ quán"),
            ("UC19", "Xác nhận đơn (Accept Order) & Bắt đầu nấu"),
            ("UC20", "Kích hoạt tự động Điều phối Tài xế"),
            ("UC21", "Từ chối đơn (Reject Order) & Hủy"),
            ("UC22", "Thống kê doanh thu Bán của cửa hàng")
        ],
        "classes": [
            ("RestaurantController", "controller", "Duyệt đơn, cập nhật menu và số lượng tồn kho"),
            ("RestaurantRepository", "repository", "Quản lý file restaurants.csv"),
            ("MenuItemRepository", "repository", "Quản lý món ăn menu_items.csv, deduct/restoreStock"),
            ("Restaurant", "model", "Thực thể nhà hàng kèm GPS Quy Nhơn"),
            ("MenuItem", "model", "Thực thể món ăn kèm đơn giá và số lượng tồn kho"),
            ("RestaurantView", "view", "Giao diện Console cho chủ quán ăn"),
            ("OrderController", "controller", "Phương thức confirmOrderByRestaurant và rejectOrderByRestaurant")
        ],
        "csv": ["data/restaurants.csv", "data/menu_items.csv"],
        "desc": "Cổng quản lý nhà hàng: quản lý thực đơn, điều chỉnh giá bán, kiểm soát kho hàng chống Oversell và duyệt đơn kích hoạt trung tâm điều phối."
    },
    {
        "id": "SUB4",
        "num": 4,
        "title": "Phân hệ 4: Vận hành Tài xế & Giao nhận Đơn",
        "en": "Driver Delivery & Lifecycle",
        "owner": "Đào Cao Duy (QE190089)",
        "role": "Driver Portal Dev",
        "color": "#047857", # Emerald Green
        "accent": "#10B981",
        "ucs": "UC23 – UC28",
        "uc_list": [
            ("UC23", "Xem hồ sơ & Thông tin cá nhân tài xế"),
            ("UC24", "Chuyển trạng thái AVAILABLE / OFFLINE"),
            ("UC25", "Xem các đơn hàng được phân công"),
            ("UC26", "Xác nhận lấy hàng (Khoản PICKED_UP)"),
            ("UC27", "Xác nhận hoàn tất Giao hàng (DELIVERED)"),
            ("UC28", "Tự động chuyển về Sẵn sàng (AVAILABLE)")
        ],
        "classes": [
            ("DriverController", "controller", "Quản lý trạng thái tài xế, nhận đơn, giao hàng"),
            ("DriverRepository", "repository", "Quản lý drivers.csv, markBusy, markAvailable"),
            ("Driver", "model", "Thực thể tài xế kèm trạng thái, tọa độ GPS di chuyển"),
            ("DriverStatus", "model", "Enum: AVAILABLE, BUSY, OFFLINE"),
            ("DriverView", "view", "Giao diện Console dành riêng cho tài xế"),
            ("OrderController", "controller", "Phương thức markOrderPickedUp và markOrderDelivered")
        ],
        "csv": ["data/drivers.csv"],
        "desc": "Cổng tác nghiệp tài xế: quản lý ca trực, nhận đơn được điều phối, xác nhận nhận món tại quán, giao hàng tận tay khách và tự động chuyển về AVAILABLE."
    },
    {
        "id": "SUB5",
        "num": 5,
        "title": "Phân hệ 5: Điều phối Trung tâm & Grid Tọa độ Quy Nhơn",
        "en": "Central Dispatching & Network Grid",
        "owner": "Hồ Anh Nhật (QE200301)",
        "role": "Leader / Central Dispatching",
        "color": "#1D4ED8", # Royal Blue
        "accent": "#60A5FA",
        "ucs": "UC29 – UC35",
        "uc_list": [
            ("UC29", "Xem danh sách đơn Chờ điều phối (ACCEPTED)"),
            ("UC30", "Tự động điều phối Tất cả đơn hàng mới"),
            ("UC31", "Tìm kiếm tài xế Gần nhất (Haversine Distance)"),
            ("UC32", "Tính toán ETA Lấy món & Giao hàng"),
            ("UC33", "Gán một đơn & Khóa tài xế (ASSIGNED)"),
            ("UC34", "Giám sát trạng thái Toàn đội ngũ tài xế"),
            ("UC35", "Hiển thị Bản đồ ASCII Grid Map Quy Nhơn (16x50)")
        ],
        "classes": [
            ("MainController", "controller", "Facade MVC Orchestrator điều phối toàn bộ các phân hệ"),
            ("AppController", "controller", "Bộ điều khiển khởi động ứng dụng"),
            ("DeliveryRouteRepository", "repository", "Lưu trữ lịch sử hành trình delivery_routes.csv"),
            ("DeliveryRoute", "model", "Thực thể lộ trình giao hàng kèm cự ly và thời gian"),
            ("GeoUtils", "model", "Tính toán khoảng cách Haversine và cước phí chuẩn xác"),
            ("MapView", "view", "Vẽ bản đồ ASCII Grid 16x50 hiển thị vị trí quán, khách, xế"),
            ("DoubleAssignmentException", "exception", "Ngoại lệ ngăn chặn 2 đơn gán trùng 1 tài xế"),
            ("DriverOverloadException", "exception", "Ngoại lệ ngăn chặn tài xế bận bị ép nhận thêm đơn")
        ],
        "csv": ["data/delivery_routes.csv"],
        "desc": "Bộ não trung tâm của hệ thống: thuật toán gán đơn tự động cho tài xế gần nhất, tính toán thời gian ETA, ngăn chặn Double Assignment và vẽ bản đồ ASCII Grid 16x50."
    },
    {
        "id": "SUB6",
        "num": 6,
        "title": "Phân hệ 6: Quản trị Hệ thống Toàn diện & Big Data",
        "en": "System Administration & Big Data",
        "owner": "Đỗ Tấn Vũ (QE170159)",
        "role": "System Administrator",
        "color": "#4338CA", # Indigo
        "accent": "#818CF8",
        "ucs": "UC36 – UC44",
        "uc_list": [
            ("UC36", "Phê duyệt hồ sơ & Tự động cấp mã tài xế"),
            ("UC37", "Từ chối hồ sơ Đăng ký tài xế"),
            ("UC38", "Quản trị Danh bạ Toàn bộ Khách hàng"),
            ("UC39", "Quản lý Nhà hàng, Thực đơn & Khớp kho"),
            ("UC40", "Quản trị Đội xe & Khóa/Mở tài xế"),
            ("UC41", "Quản trị Mạng lưới & Phân quyền Hệ thống"),
            ("UC42", "Giám sát toàn bộ Đơn hàng & Lộ trình"),
            ("UC43", "Sinh dữ liệu mẫu 18.000 dòng CSV (Test Bench)"),
            ("UC44", "Báo cáo Tổng quan & Nghiên cứu Big Question")
        ],
        "classes": [
            ("AdminController", "controller", "Quản trị danh bạ, phê duyệt hồ sơ lái xe, sinh dữ liệu"),
            ("DriverApplicationRepository", "repository", "Quản lý driver_applications.csv"),
            ("DriverApplication", "model", "Hồ sơ đăng ký tài xế mới"),
            ("DriverApplicationStatus", "model", "Enum: PENDING, APPROVED, REJECTED"),
            ("DataGenerator", "util", "Sinh bộ dữ liệu lớn hơn 18.000 dòng CSV chuẩn"),
            ("AdminView", "view", "Giao diện quản trị tối cao của Admin")
        ],
        "csv": ["data/driver_applications.csv"],
        "desc": "Cổng quản trị tối cao: duyệt đơn đăng ký đối tác tài xế, quản trị danh mục toàn hệ thống và sinh bộ dữ liệu lớn 18.000 dòng CSV để kiểm thử tải cực hạn."
    },
    {
        "id": "SUB7",
        "num": 7,
        "title": "Phân hệ 7: Mô phỏng Concurrency & Kiểm định Tương tranh",
        "en": "Concurrency Stress Simulator & Core Engine",
        "owner": "Trương Thanh Trà (QE200100)",
        "role": "Technical Lead / Core Engine (20% ★)",
        "color": "#BE123C", # Rose / Dark Red
        "accent": "#FB7185",
        "ucs": "UC45 – UC51",
        "uc_list": [
            ("UC45", "Cấu hình tham số Kiểm thử (threads, orders, mechanism)"),
            ("UC46", "Khởi chạy mô phỏng Đa luồng đồng thời"),
            ("UC47", "Mô phỏng Đặt hàng (Customer Thread)"),
            ("UC48", "Mô phỏng Điều phối (Dispatch Thread)"),
            ("UC49", "Tự động phát hiện Lỗi Race Conditions (Oversell, Double Assign)"),
            ("UC50", "Đo đạc và So chuẩn 4 Cơ chế Khóa (Benchmark)"),
            ("UC51", "Xuất bảng so sánh Hiệu năng & Kết luận Big Question")
        ],
        "classes": [
            ("SimulatorController", "controller", "Điều phối bài kiểm tra tương tranh, quản lý threads"),
            ("CustomerThread", "controller", "Luồng giả lập khách hàng đặt món và hủy đơn liên tục"),
            ("DispatchThread", "controller", "Luồng giả lập điều phối quét đơn tự động"),
            ("CustomerSimTask", "controller", "Tác vụ runnable cho khách hàng"),
            ("DispatchSimTask", "controller", "Tác vụ runnable cho điều phối"),
            ("SimulationRunRepository", "repository", "Lưu trữ kết quả simulation_runs.csv"),
            ("SimulationRun", "model", "Kết quả thực nghiệm: throughput, drop rate, conflicts"),
            ("LockMechanism", "model", "Enum: NO_LOCK, REENTRANT_LOCK, READ_WRITE_LOCK, OPTIMISTIC_LOCK"),
            ("OrderController", "controller", "Quản trị xung đột tương tranh và cơ chế version checking"),
            ("OptimisticLockException", "exception", "Ngoại lệ khi phiên bản dữ liệu bị xung đột"),
            ("SimulatorView", "view", "Giao diện cấu hình và theo dõi tiến độ benchmark"),
            ("ReportView", "view", "Xuất bảng báo cáo so sánh kết quả 4 cơ chế khóa")
        ],
        "csv": ["data/simulation_runs.csv"],
        "desc": "Trọng tâm kỹ thuật quan trọng nhất (20%): Mô phỏng tải cực hạn 700 đơn hàng đồng thời, kiểm định 3 Race Conditions và giải quyết câu hỏi khoa học Big Question với giải pháp Optimistic Locking."
    }
]

def generate_markdown_docs():
    md = []
    md.append("# 🧩 KIẾN TRÚC DIAGRAM THEO 7 PHÂN HỆ CHỨC NĂNG (SUBSYSTEM ARCHITECTURE)")
    md.append("\n> 🏫 **LAB211 – OOP with Java – FPT University**")
    md.append("> 🍱 **Chủ đề:** Food Delivery Network Simulation (FDS)")
    md.append("> 👥 **Nhóm thực hiện:** Nhóm 01 (Group 1)")
    md.append("> 🎯 **Phương pháp phân chia:** Subsystem-Oriented Architecture (Chia theo Phân hệ chức năng & Ma trận Trách nhiệm WBS)\n")
    md.append("---\n")
    md.append("## 📌 1. Tại sao nên chia Diagram theo Phân hệ chức năng?")
    md.append("Thay vì chỉ chia sơ đồ theo các **tầng kỹ thuật ngang** (Model, Repository, Controller, View), việc chia theo **Phân hệ chức năng (Vertical Slices / Subsystems)** mang lại 4 lợi ích vượt trội khi bảo vệ đồ án:\n")
    md.append("1. **Độc lập và Rõ ràng trách nhiệm:** Mỗi phân hệ gắn liền với 1 thành viên phụ trách cụ thể theo ma trận RACI. Giảng viên chấm đến ai, chỉ cần mở Diagram phân hệ đó ra hỏi.")
    md.append("2. **Trọn vẹn một luồng nghiệp vụ (End-to-End):** Trong mỗi phân hệ, người xem thấy được từ Actor tương tác → Use Cases → Controller điều phối → Repository đọc/ghi → Model dữ liệu → File CSV vật lý.")
    md.append("3. **Bớt rối và Dễ đọc (Decoupled Visual):** Không bị tình trạng hàng chục đường dây đan chéo nhau giữa các phân hệ không liên quan.")
    md.append("4. **Đồng bộ tuyệt đối:** Khớp 100% với danh sách 51 Use Cases và 60 Classes trong mã nguồn Java.\n")
    md.append("---\n")
    md.append("## 📋 2. Bảng Tổng hợp 7 Phân hệ Chức năng\n")
    md.append("| STT | Tên Phân hệ | Thành viên phụ trách | Use Cases | Số Lớp Java | File CSV vật lý |")
    md.append("|:---:|:---|:---|:---:|:---:|:---|")
    for s in SUB_INFO:
        md.append(f"| **{s['num']}** | **{s['title']}**<br>*{s['en']}* | **{s['owner']}**<br>({s['role']}) | `{s['ucs']}` | {len(s['classes'])} classes | `{', '.join(s['csv'])}` |")
    md.append("\n---\n")

    for s in SUB_INFO:
        md.append(f"## 🔷 {s['title']}")
        md.append(f"**Tên tiếng Anh:** `{s['en']}`  ")
        md.append(f"**Thành viên phụ trách:** `{s['owner']}` – *{s['role']}*  ")
        md.append(f"**Phạm vi Use Case:** `{s['ucs']}` | **File CSV liên quan:** `{', '.join(s['csv'])}`\n")
        md.append(f"> 💬 **Mô tả nghiệp vụ:** {s['desc']}\n")

        md.append("### 📑 Danh sách Use Cases của Phân hệ:")
        for uc_id, uc_name in s["uc_list"]:
            md.append(f"- **`{uc_id}`:** {uc_name}")

        md.append("\n### 🏗️ Các Class Java đảm nhiệm trong Phân hệ:")
        md.append("| Tên Lớp Java | Thuộc Tầng | Vai trò trong Phân hệ |")
        md.append("|---|:---:|---|")
        for cname, cpkg, crole in s["classes"]:
            md.append(f"| `{cname}` | `{cpkg}` | {crole} |")

        md.append("\n### 📊 Sơ đồ Kiến trúc Phân hệ (Mermaid Class Diagram):")
        md.append("```mermaid")
        md.append(f"classDiagram")
        md.append(f"    direction TB")
        md.append(f"    %% Subsystem {s['num']}: {s['en']}")
        for cname, cpkg, crole in s["classes"]:
            md.append(f"    class {cname} {{")
            md.append(f"        <<{cpkg}>>")
            md.append(f"    }}")

        # Specific relations for each subsystem
        if s["num"] == 1:
            md.append("    AuthController --> UserRepository : uses")
            md.append("    AuthController --> AccountRepository : uses (PBKDF2)")
            md.append("    UserRepository --> User : persists")
            md.append("    AccountRepository --> UserAccount : persists")
            md.append("    UserAccount --> UserRole : has")
            md.append("    AuthSession --> User : refers")
        elif s["num"] == 2:
            md.append("    CustomerController --> CustomerRepository : uses")
            md.append("    CustomerController --> OrderRepository : uses")
            md.append("    CustomerController --> OrderItemRepository : uses")
            md.append("    OrderRepository --> Order : persists")
            md.append("    OrderItemRepository --> OrderItem : persists")
            md.append("    Order --> OrderStatus : has")
            md.append("    OrderView ..> Order : displays")
        elif s["num"] == 3:
            md.append("    RestaurantController --> RestaurantRepository : uses")
            md.append("    RestaurantController --> MenuItemRepository : uses")
            md.append("    RestaurantRepository --> Restaurant : persists")
            md.append("    MenuItemRepository --> MenuItem : persists")
            md.append("    RestaurantView ..> MenuItem : displays")
        elif s["num"] == 4:
            md.append("    DriverController --> DriverRepository : uses")
            md.append("    DriverRepository --> Driver : persists")
            md.append("    Driver --> DriverStatus : has")
            md.append("    DriverView ..> Driver : displays")
        elif s["num"] == 5:
            md.append("    MainController --> AppController : orchestrates")
            md.append("    MainController --> DeliveryRouteRepository : uses")
            md.append("    MainController --> MapView : renders")
            md.append("    MapView ..> GeoUtils : uses (Haversine)")
            md.append("    DeliveryRouteRepository --> DeliveryRoute : persists")
        elif s["num"] == 6:
            md.append("    AdminController --> DriverApplicationRepository : uses")
            md.append("    AdminController --> DataGenerator : invokes (18k CSV)")
            md.append("    DriverApplicationRepository --> DriverApplication : persists")
            md.append("    DriverApplication --> DriverApplicationStatus : has")
            md.append("    AdminView ..> AdminController : displays")
        elif s["num"] == 7:
            md.append("    SimulatorController --> CustomerThread : spawns")
            md.append("    SimulatorController --> DispatchThread : spawns")
            md.append("    SimulatorController --> SimulationRunRepository : records")
            md.append("    SimulationRunRepository --> SimulationRun : persists")
            md.append("    SimulatorController ..> LockMechanism : tests 4 locks")
            md.append("    ReportView ..> SimulationRun : displays benchmark")
        md.append("```\n")
        md.append("---\n")

    return "\n".join(md)

def generate_subsystems_html():
    # Build HTML view
    tabs_btn = []
    tabs_content = []

    for s in SUB_INFO:
        tabs_btn.append(f"""
        <button class="nav-tab {'active' if s['num'] == 1 else ''}" onclick="switchSubsystem('{s['id']}', this)">
            <span class="tab-badge">{s['num']}</span>
            <div class="tab-info">
                <div class="tab-title">{s['title'].split(':')[1].strip()}</div>
                <div class="tab-owner">{s['owner'].split('(')[0].strip()}</div>
            </div>
        </button>
        """)

        # Build content for this subsystem
        uc_rows = "".join([f"<tr><td><code>{uc[0]}</code></td><td>{uc[1]}</td></tr>" for uc in s["uc_list"]])
        class_rows = "".join([f"<tr><td><code>{c[0]}</code></td><td><span class='badge badge-{c[1]}'>{c[1]}</span></td><td>{c[2]}</td></tr>" for c in s["classes"]])

        tabs_content.append(f"""
        <div id="panel-{s['id']}" class="sub-panel {'active' if s['num'] == 1 else ''}">
            <div class="panel-header" style="border-left: 5px solid {s['accent']};">
                <div class="header-badge" style="background: {s['color']}; color: #fff;">PHÂN HỆ {s['num']} • {s['ucs']}</div>
                <h1 class="panel-title">{s['title']}</h1>
                <p class="panel-subtitle">{s['en']}</p>
                <div class="owner-chip">
                    👤 <strong>Phụ trách chính:</strong> {s['owner']} ({s['role']})
                </div>
                <p class="panel-desc">{s['desc']}</p>
            </div>

            <div class="panel-body-grid">
                <div class="card">
                    <h3 class="card-title">📑 Danh sách Use Cases ({s['ucs']})</h3>
                    <table class="table-compact">
                        <thead><tr><th>Mã UC</th><th>Tên Chức năng Nghiệp vụ</th></tr></thead>
                        <tbody>{uc_rows}</tbody>
                    </table>
                </div>

                <div class="card">
                    <h3 class="card-title">🏗️ Kiến trúc Lớp Java ({len(s['classes'])} Classes)</h3>
                    <table class="table-compact">
                        <thead><tr><th>Tên Class</th><th>Package</th><th>Nhiệm vụ</th></tr></thead>
                        <tbody>{class_rows}</tbody>
                    </table>
                    <div style="margin-top: 14px; font-size: 12px; color: #94a3b8;">
                        📁 <strong>File CSV lưu trữ:</strong> <code>{', '.join(s['csv'])}</code>
                    </div>
                </div>
            </div>
        </div>
        """)

    html = f"""<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kiến trúc Diagram theo 7 Phân hệ - Food Delivery Simulation</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">
  <style>
    :root {{
      --bg: #0f172a;
      --card: #1e293b;
      --card-border: #334155;
      --navy: #1b365d;
      --cyan: #0284c7;
      --cyan-light: #38bdf8;
      --text: #f8fafc;
      --text-muted: #94a3b8;
    }}
    * {{ box-sizing: border-box; margin: 0; padding: 0; }}
    body {{
      font-family: 'Inter', sans-serif;
      background: var(--bg);
      color: var(--text);
      display: flex;
      height: 100vh;
      overflow: hidden;
    }}
    /* SIDEBAR */
    aside {{
      width: 320px;
      background: #090d16;
      border-right: 1px solid var(--card-border);
      display: flex;
      flex-direction: column;
      flex-shrink: 0;
    }}
    .brand-box {{
      padding: 20px;
      border-bottom: 1px solid var(--card-border);
    }}
    .brand-title {{
      font-size: 15px;
      font-weight: 800;
      color: #fff;
      display: flex;
      align-items: center;
      gap: 8px;
    }}
    .brand-sub {{
      font-size: 11px;
      color: var(--cyan-light);
      margin-top: 4px;
      text-transform: uppercase;
      font-weight: 600;
    }}
    .nav-list {{
      flex: 1;
      overflow-y: auto;
      padding: 12px;
      display: flex;
      flex-direction: column;
      gap: 6px;
    }}
    .nav-tab {{
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 10px 14px;
      border-radius: 8px;
      background: transparent;
      border: 1px solid transparent;
      color: #cbd5e1;
      cursor: pointer;
      text-align: left;
      transition: all 0.2s;
      width: 100%;
    }}
    .nav-tab:hover {{
      background: rgba(51, 65, 85, 0.4);
      border-color: var(--card-border);
    }}
    .nav-tab.active {{
      background: var(--card);
      border-color: var(--cyan);
      color: #fff;
      box-shadow: 0 4px 12px rgba(0,0,0,0.3);
    }}
    .tab-badge {{
      width: 26px;
      height: 26px;
      border-radius: 6px;
      background: rgba(2, 132, 199, 0.2);
      color: var(--cyan-light);
      font-size: 12px;
      font-weight: 800;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }}
    .nav-tab.active .tab-badge {{
      background: var(--cyan);
      color: #fff;
    }}
    .tab-title {{
      font-size: 13px;
      font-weight: 700;
      line-height: 1.3;
    }}
    .tab-owner {{
      font-size: 11px;
      color: var(--text-muted);
      margin-top: 2px;
    }}

    /* MAIN VIEWPORT */
    main {{
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow-y: auto;
      background: var(--bg);
      padding: 28px 36px;
    }}
    .sub-panel {{
      display: none;
      animation: fadeIn 0.25s ease-in-out;
    }}
    .sub-panel.active {{
      display: block;
    }}
    @keyframes fadeIn {{
      from {{ opacity: 0; transform: translateY(6px); }}
      to {{ opacity: 1; transform: translateY(0); }}
    }}

    .panel-header {{
      background: var(--card);
      border: 1px solid var(--card-border);
      border-radius: 12px;
      padding: 24px 28px;
      margin-bottom: 24px;
      box-shadow: 0 10px 25px rgba(0,0,0,0.2);
    }}
    .header-badge {{
      display: inline-block;
      padding: 4px 12px;
      border-radius: 999px;
      font-size: 11px;
      font-weight: 800;
      letter-spacing: 0.5px;
      margin-bottom: 12px;
    }}
    .panel-title {{
      font-size: 24px;
      font-weight: 800;
      color: #fff;
      margin-bottom: 4px;
    }}
    .panel-subtitle {{
      font-size: 14px;
      color: var(--cyan-light);
      font-weight: 600;
      margin-bottom: 14px;
    }}
    .owner-chip {{
      display: inline-flex;
      align-items: center;
      background: rgba(15, 23, 42, 0.6);
      border: 1px solid var(--card-border);
      padding: 6px 14px;
      border-radius: 6px;
      font-size: 12.5px;
      color: #e2e8f0;
      margin-bottom: 14px;
    }}
    .panel-desc {{
      font-size: 13.5px;
      line-height: 1.6;
      color: #cbd5e1;
    }}

    .panel-body-grid {{
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 24px;
    }}
    .card {{
      background: var(--card);
      border: 1px solid var(--card-border);
      border-radius: 12px;
      padding: 22px 24px;
    }}
    .card-title {{
      font-size: 15px;
      font-weight: 700;
      color: #fff;
      margin-bottom: 16px;
      border-bottom: 1px solid var(--card-border);
      padding-bottom: 10px;
    }}

    .table-compact {{
      width: 100%;
      border-collapse: collapse;
      font-size: 12.5px;
    }}
    .table-compact th {{
      text-align: left;
      padding: 8px 10px;
      background: rgba(15, 23, 42, 0.6);
      color: var(--text-muted);
      border-bottom: 1px solid var(--card-border);
      font-weight: 600;
    }}
    .table-compact td {{
      padding: 8px 10px;
      border-bottom: 1px solid rgba(51, 65, 85, 0.3);
      color: #e2e8f0;
    }}
    .badge {{
      display: inline-block;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 10.5px;
      font-weight: 700;
      font-family: 'JetBrains Mono', monospace;
      text-transform: uppercase;
    }}
    .badge-controller {{ background: rgba(56, 189, 248, 0.15); color: #38bdf8; border: 1px solid rgba(56, 189, 248, 0.4); }}
    .badge-model {{ background: rgba(16, 185, 129, 0.15); color: #34d399; border: 1px solid rgba(16, 185, 129, 0.4); }}
    .badge-repository {{ background: rgba(245, 158, 11, 0.15); color: #fbbf24; border: 1px solid rgba(245, 158, 11, 0.4); }}
    .badge-exception {{ background: rgba(239, 68, 68, 0.15); color: #f87171; border: 1px solid rgba(239, 68, 68, 0.4); }}
    .badge-view {{ background: rgba(168, 85, 247, 0.15); color: #c084fc; border: 1px solid rgba(168, 85, 247, 0.4); }}
    .badge-util {{ background: rgba(148, 163, 184, 0.15); color: #cbd5e1; border: 1px solid rgba(148, 163, 184, 0.4); }}
  </style>
</head>
<body>
  <!-- SIDEBAR -->
  <aside>
    <div class="brand-box">
      <div class="brand-title">🍔 7 PHÂN HỆ HỆ THỐNG</div>
      <div class="brand-sub">LAB211 Food Delivery</div>
    </div>
    <div class="nav-list">
      {''.join(tabs_btn)}
    </div>
  </aside>

  <!-- MAIN -->
  <main>
    {''.join(tabs_content)}
  </main>

  <script>
    function switchSubsystem(subId, btn) {{
      document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));
      document.querySelectorAll('.sub-panel').forEach(p => p.classList.remove('active'));

      btn.classList.add('active');
      const target = document.getElementById('panel-' + subId);
      if (target) {{
        target.classList.add('active');
      }}
    }}
  </script>
</body>
</html>
"""
    return html

def create_drawio_subsystems():
    # Build a clean multi-tab drawio file with 7 tabs
    xml_header = '<mxfile host="app.diagrams.net" modified="2026-11-14T00:00:00.000Z" agent="Antigravity" version="21.0.0" type="device">\n'
    xml_tabs = []

    for s in SUB_INFO:
        tab_name = f"Phan_He_{s['num']}_{s['id']}"
        tab_xml = f'  <diagram id="{s["id"]}" name="{tab_name}">\n'
        tab_xml += '    <mxGraphModel dx="1422" dy="794" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="0" pageScale="1" pageWidth="1600" pageHeight="1200">\n'
        tab_xml += '      <root>\n'
        tab_xml += '        <mxCell id="0"/>\n'
        tab_xml += '        <mxCell id="1" parent="0"/>\n'

        # Subsystem Header Card
        tab_xml += f'        <mxCell id="hdr_{s["id"]}" value="&lt;b&gt;{s["title"]}&lt;/b&gt;&lt;br/&gt;{s["en"]}&lt;br/&gt;&lt;i&gt;Phụ trách: {s["owner"]}&lt;/i&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor={s["color"]};fontColor=#ffffff;strokeColor=#334155;fontSize=14;align=center;" vertex="1" parent="1">\n'
        tab_xml += '          <mxGeometry x="80" y="40" width="800" height="70" as="geometry"/>\n'
        tab_xml += '        </mxCell>\n'

        # Class boxes inside subsystem
        for idx, (cname, cpkg, crole) in enumerate(s["classes"]):
            col = idx % 3
            row = idx // 3
            cx = 80 + col * 280
            cy = 140 + row * 130
            c_style = "rounded=1;whiteSpace=wrap;html=1;fillColor=#1e293b;fontColor=#ffffff;strokeColor=#0284c7;fontSize=11;align=left;spacingLeft=10;"
            val = f"&lt;b&gt;{cname}&lt;/b&gt;&lt;br/&gt;&lt;font color=&quot;#38bdf8&quot;&gt;&amp;lt;&amp;lt;{cpkg}&amp;gt;&amp;gt;&lt;/font&gt;&lt;br/&gt;&lt;font color=&quot;#94a3b8&quot; style=&quot;font-size:10px;&quot;&gt;{crole}&lt;/font&gt;"
            tab_xml += f'        <mxCell id="c_{s["id"]}_{idx}" value="{val}" style="{c_style}" vertex="1" parent="1">\n'
            tab_xml += f'          <mxGeometry x="{cx}" y="{cy}" width="260" height="100" as="geometry"/>\n'
            tab_xml += '        </mxCell>\n'

        tab_xml += '      </root>\n'
        tab_xml += '    </mxGraphModel>\n'
        tab_xml += '  </diagram>\n'
        xml_tabs.append(tab_xml)

    xml_footer = '</mxfile>'
    return xml_header + "".join(xml_tabs) + xml_footer

def main():
    md_content = generate_markdown_docs()
    html_content = generate_subsystems_html()
    drawio_content = create_drawio_subsystems()

    dirs = [
        "docs/subsystems",
        "D:/Lab211-Group1/docs/subsystems",
        "D:/Lab211-Group1/diagram/subsystems",
        "D:/Lab211-Group1/NHOM_01_LAB211_FoodDelivery/docs/subsystems"
    ]

    for d in dirs:
        os.makedirs(d, exist_ok=True)
        with open(os.path.join(d, "subsystems_diagram.md"), "w", encoding="utf-8") as f:
            f.write(md_content)
        with open(os.path.join(d, "subsystems_view.html"), "w", encoding="utf-8") as f:
            f.write(html_content)
        with open(os.path.join(d, "Subsystems_Architecture.drawio"), "w", encoding="utf-8") as f:
            f.write(drawio_content)
        print(f"Saved subsystem files to: {d}")

    print("ALL SUBSYSTEM ARTIFACTS GENERATED SUCCESSFULLY!")

if __name__ == "__main__":
    main()
