package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UseCaseDiagramGenerator {

    static class Actor {
        String id;
        String name;
        String subtitle;
        String type; // "human" or "system"
        int x, y;

        Actor(String id, String name, String subtitle, String type, int x, int y) {
            this.id = id;
            this.name = name;
            this.subtitle = subtitle;
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }

    static class Subsystem {
        String id;
        String title;
        String enTitle;
        int x, y, w, h;

        Subsystem(String id, String title, String enTitle, int x, int y, int w, int h) {
            this.id = id;
            this.title = title;
            this.enTitle = enTitle;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    static class UseCase {
        String id;
        String line1;
        String line2;
        String fullTitle;
        String subsystemId;
        int x, y, w, h; // center x, y, width, height

        UseCase(String id, String line1, String line2, String fullTitle, String subsystemId, int x, int y, int w, int h) {
            this.id = id;
            this.line1 = line1;
            this.line2 = line2;
            this.fullTitle = fullTitle;
            this.subsystemId = subsystemId;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        int left() { return x - w / 2; }
        int right() { return x + w / 2; }
        int top() { return y - h / 2; }
        int bottom() { return y + h / 2; }
    }

    static class UCRelation {
        String fromId;
        String toId;
        String type; // "assoc", "include", "extend"
        String label;

        UCRelation(String fromId, String toId, String type, String label) {
            this.fromId = fromId;
            this.toId = toId;
            this.type = type;
            this.label = label;
        }
    }

    public static void main(String[] args) {
        System.out.println("Generating 100% COMPLETE Monochrome UML Use Case Diagram (Zero Line Crossing)...");
        try {
            List<Actor> actors = buildActors();
            List<Subsystem> subsystems = buildSubsystems();
            List<UseCase> useCases = buildUseCases();
            List<UCRelation> relations = buildRelations();

            generateSvg(actors, subsystems, useCases, relations);
            generateHtml(actors, subsystems, useCases, relations);
            generateDrawio(actors, subsystems, useCases, relations);
            generateMarkdown(actors, subsystems, useCases, relations);

            System.out.println("USE CASE DIAGRAM GENERATION FINISHED SUCCESSFULLY!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Actor> buildActors() {
        List<Actor> list = new ArrayList<>();
        // Left side actors (Human users)
        list.add(new Actor("ACT_CUSTOMER", "Khách hàng", "(Customer)", "human", 160, 680));
        list.add(new Actor("ACT_RESTAURANT", "Quản lý Nhà hàng", "(Restaurant Manager)", "human", 160, 1420));
        list.add(new Actor("ACT_DRIVER", "Tài xế Giao hàng", "(Delivery Driver)", "human", 160, 2010));

        // Right side actors (Admin & Automated System Actors)
        list.add(new Actor("ACT_ADMIN", "Quản trị viên", "(System Administrator)", "human", 3140, 430));
        list.add(new Actor("ACT_DISPATCHER", "Hệ thống Điều phối", "<<system>> Dispatcher Engine", "system", 3140, 1050));
        list.add(new Actor("ACT_SIMULATOR", "Bộ Mô phỏng Đa luồng", "<<system>> Concurrency Simulator", "system", 3140, 1780));
        return list;
    }

    private static List<Subsystem> buildSubsystems() {
        List<Subsystem> list = new ArrayList<>();
        // Left column subsystems (Width = 1180, x = 380)
        list.add(new Subsystem("SUB1", "Phân hệ 1: Xác thực & Hồ sơ Cá nhân", "Authentication & User Profile", 380, 140, 1180, 340));
        list.add(new Subsystem("SUB2", "Phân hệ 2: Khách hàng & Đặt hàng Trực tuyến", "Customer Ordering & Cart Management", 380, 510, 1180, 590));
        list.add(new Subsystem("SUB3", "Phân hệ 3: Quản lý Nhà hàng & Thực đơn Quy Nhơn", "Restaurant & Menu Catalog Management", 380, 1130, 1180, 580));
        list.add(new Subsystem("SUB4", "Phân hệ 4: Tài xế & Vận hành Giao nhận Đơn hàng", "Driver Delivery & Trip Operations", 380, 1740, 1180, 520));

        // Right column subsystems (Width = 1330, x = 1630)
        list.add(new Subsystem("SUB6", "Phân hệ 6: Quản trị Hệ thống & Phê duyệt Đối tác", "System Administration & Partner Approval", 1630, 140, 1330, 610));
        list.add(new Subsystem("SUB5", "Phân hệ 5: Điều phối Tự động & Tối ưu Lộ trình", "Automated Dispatch & Route Matching Engine", 1630, 780, 1330, 560));
        list.add(new Subsystem("SUB7", "Phân hệ 7: Mô phỏng Đa luồng & Kiểm định Tương tranh", "Concurrency Stress Testing & Race Detection", 1630, 1370, 1330, 890));
        return list;
    }

    private static List<UseCase> buildUseCases() {
        List<UseCase> list = new ArrayList<>();

        // ==========================================
        // SUB1: Xác thực & Hồ sơ (x=380, y=140, w=1180, h=340)
        // Col 1 (x=550) - Base cases facing Customer / User
        list.add(new UseCase("UC01", "UC01: Đăng ký tài khoản", "Khách hàng mới", "Đăng ký tài khoản Khách hàng mới", "SUB1", 550, 220, 240, 68));
        list.add(new UseCase("UC02", "UC02: Đăng nhập", "vào Hệ thống", "Đăng nhập hệ thống (Đa vai trò)", "SUB1", 550, 310, 240, 68));
        list.add(new UseCase("UC03", "UC03: Đăng xuất", "khỏi Phiên làm việc", "Đăng xuất khỏi hệ thống", "SUB1", 550, 400, 240, 68));
        // Col 2 (x=880) - Secondary / Included
        list.add(new UseCase("UC04", "UC04: Xem & Cập nhật", "Hồ sơ / Địa chỉ GPS", "Xem & cập nhật hồ sơ cá nhân", "SUB1", 880, 260, 250, 68));
        list.add(new UseCase("UC05", "UC05: Nộp đơn đăng ký", "Ứng tuyển Tài xế", "Nộp đơn ứng tuyển tài xế giao hàng", "SUB1", 880, 360, 250, 68));

        // ==========================================
        // SUB2: Khách hàng & Đặt hàng (x=380, y=510, w=1180, h=590)
        // Col 1 (x=550) - Base cases facing Customer Actor
        list.add(new UseCase("UC06", "UC06: Xem danh mục", "Nhà hàng & Thực đơn", "Xem danh sách nhà hàng & menu Quy Nhơn", "SUB2", 550, 580, 250, 68));
        list.add(new UseCase("UC08", "UC08: Đặt món &", "Khởi tạo Đơn hàng", "Đặt món và tạo đơn hàng mới", "SUB2", 550, 700, 250, 72));
        list.add(new UseCase("UC12", "UC12: Theo dõi trạng thái", "Đơn hàng thời gian thực", "Theo dõi trạng thái đơn hàng thời gian thực", "SUB2", 550, 820, 250, 68));
        list.add(new UseCase("UC13", "UC13: Xem lịch sử", "Các đơn hàng đã đặt", "Xem lịch sử đơn hàng", "SUB2", 550, 920, 250, 68));
        list.add(new UseCase("UC14", "UC14: Hủy đơn hàng", "(khi đang PENDING)", "Hủy đơn hàng chưa điều phối", "SUB2", 550, 1020, 250, 68));
        // Col 2 (x=880) - Included / Extended
        list.add(new UseCase("UC07", "UC07: Tìm kiếm & Lọc", "Món ăn / Giá / Khoảng cách", "Tìm kiếm và lọc món ăn", "SUB2", 880, 580, 250, 68));
        list.add(new UseCase("UC09", "UC09: Trừ tồn kho", "Món ăn tức thời", "Trừ số lượng tồn kho món ăn tức thời", "SUB2", 880, 670, 240, 68));
        list.add(new UseCase("UC10", "UC10: Tính phí ship", "theo Haversine", "Tính phí giao hàng khoảng cách Haversine", "SUB2", 880, 760, 240, 68));
        list.add(new UseCase("UC15", "UC15: Hoàn trả số lượng", "Tồn kho món ăn", "Hoàn trả tồn kho khi đơn bị hủy/từ chối", "SUB2", 880, 1020, 240, 68));
        // Col 3 (x=1180) - Deeply included
        list.add(new UseCase("UC11", "UC11: Xác thực tọa độ", "GPS giao hàng Quy Nhơn", "Xác thực địa chỉ và tọa độ GPS", "SUB2", 1180, 760, 240, 68));

        // ==========================================
        // SUB3: Quản lý Nhà hàng & Thực đơn (x=380, y=1130, w=1180, h=580)
        // Col 1 (x=550) - Base cases facing Restaurant Manager Actor
        list.add(new UseCase("UC16", "UC16: Thêm món ăn mới", "vào Thực đơn", "Thêm món ăn mới vào thực đơn", "SUB3", 550, 1200, 250, 68));
        list.add(new UseCase("UC17", "UC17: Cập nhật thông tin", "Món ăn & Giá bán", "Cập nhật thông tin món và giá bán", "SUB3", 550, 1290, 250, 68));
        list.add(new UseCase("UC18", "UC18: Xóa món ăn", "khỏi Thực đơn", "Xóa món ăn khỏi thực đơn", "SUB3", 550, 1380, 250, 68));
        list.add(new UseCase("UC19", "UC19: Cập nhật & Nhập thêm", "Số lượng Tồn kho (Stock)", "Nhập bổ sung số lượng tồn kho", "SUB3", 550, 1470, 250, 68));
        list.add(new UseCase("UC20", "UC20: Xem danh sách", "Đơn hàng mới tiếp nhận", "Xem danh sách đơn hàng đến", "SUB3", 550, 1560, 250, 68));
        list.add(new UseCase("UC21", "UC21: Xác nhận", "Đơn hàng (Accept)", "Xác nhận chấp nhận đơn hàng", "SUB3", 550, 1650, 250, 68));
        // Col 2 (x=880) - Secondary & Included
        list.add(new UseCase("UC24", "UC24: Xem báo cáo", "Doanh thu & Đơn hàng", "Xem thống kê doanh thu và báo cáo nhà hàng", "SUB3", 880, 1470, 240, 68));
        list.add(new UseCase("UC23", "UC23: Từ chối", "Đơn hàng (Reject)", "Từ chối tiếp nhận đơn hàng", "SUB3", 880, 1560, 240, 68));
        list.add(new UseCase("UC22", "UC22: Kích hoạt", "Điều phối Tự động", "Kích hoạt điều phối tự động tài xế", "SUB3", 880, 1650, 240, 68));

        // ==========================================
        // SUB4: Vận hành Tài xế & Giao nhận (x=380, y=1740, w=1180, h=520)
        // Col 1 (x=550) - Base cases facing Driver Actor
        list.add(new UseCase("UC25", "UC25: Bật/Tắt trạng thái", "Hoạt động (Available/Off)", "Bật tắt trạng thái hoạt động tài xế", "SUB4", 550, 1810, 250, 68));
        list.add(new UseCase("UC26", "UC26: Cập nhật vị trí", "Tọa độ GPS thực tế", "Cập nhật tọa độ vị trí thực tế của tài xế", "SUB4", 550, 1900, 250, 68));
        list.add(new UseCase("UC27", "UC27: Tiếp nhận đơn hàng", "được Điều phối ghép nối", "Nhận đơn hàng được hệ thống điều phối", "SUB4", 550, 1990, 250, 68));
        list.add(new UseCase("UC28", "UC28: Xác nhận lấy món", "(Chuyển DISPATCHED)", "Xác nhận đã lấy hàng tại nhà hàng", "SUB4", 550, 2080, 250, 68));
        list.add(new UseCase("UC29", "UC29: Xác nhận hoàn tất", "Giao hàng (DELIVERED)", "Xác nhận giao hàng thành công tới khách", "SUB4", 550, 2170, 250, 68));
        // Col 2 (x=880) - Included
        list.add(new UseCase("UC30", "UC30: Tự động chuyển", "về Sẵn sàng (AVAILABLE)", "Tự động chuyển tài xế về trạng thái sẵn sàng", "SUB4", 880, 2170, 250, 68));

        // ==========================================
        // SUB6: Quản trị Hệ thống (x=1630, y=140, w=1330, h=610)
        // Col 3 (x=2770) - Rightmost, directly facing Admin Actor
        list.add(new UseCase("UC35", "UC35: Quản lý Tài khoản", "Người dùng & Phân quyền", "Quản lý tài khoản người dùng và phân quyền", "SUB6", 2770, 210, 260, 68));
        list.add(new UseCase("UC37", "UC37: Thẩm định hồ sơ", "Đơn ứng tuyển Tài xế", "Xem và thẩm định hồ sơ ứng tuyển tài xế", "SUB6", 2770, 320, 260, 68));
        list.add(new UseCase("UC40", "UC40: Quản lý danh mục", "Nhà hàng & Phân bổ QL", "Quản lý danh sách nhà hàng và chỉ định quản lý", "SUB6", 2770, 430, 260, 68));
        list.add(new UseCase("UC41", "UC41: Giám sát toàn bộ", "Đơn hàng trong Hệ thống", "Giám sát toàn bộ lưu lượng đơn hàng hệ thống", "SUB6", 2770, 540, 260, 68));
        list.add(new UseCase("UC42", "UC42: Nhập hàng bổ sung", "Tồn kho toàn Hệ thống", "Nhập hàng bổ sung kho toàn hệ thống", "SUB6", 2770, 650, 260, 68));
        // Col 2 (x=2460) - Internal / Extended / Included
        list.add(new UseCase("UC36", "UC36: Khóa / Mở khóa", "Tài khoản người dùng", "Khóa hoặc mở khóa tài khoản vi phạm", "SUB6", 2460, 210, 250, 68));
        list.add(new UseCase("UC38", "UC38: Phê duyệt đơn &", "Tự động tạo hồ sơ Driver", "Phê duyệt đơn và cấp tài khoản tài xế", "SUB6", 2460, 310, 250, 68));
        list.add(new UseCase("UC39", "UC39: Từ chối hồ sơ", "Ứng tuyển tài xế", "Từ chối hồ sơ ứng tuyển tài xế", "SUB6", 2460, 400, 250, 68));

        // ==========================================
        // SUB5: Điều phối Tự động (x=1630, y=780, w=1330, h=560)
        // Col 3 (x=2770) - Facing Dispatcher Engine Actor
        list.add(new UseCase("UC31", "UC31: Quét hàng đợi", "Đơn hàng PENDING", "Quét hàng đợi đơn hàng chờ xử lý", "SUB5", 2770, 870, 260, 68));
        list.add(new UseCase("UC34", "UC34: Tự động ghép nối", "& Khóa tài xế (BUSY)", "Tự động giao đơn và khóa trạng thái tài xế", "SUB5", 2770, 1040, 260, 72));
        // Col 2 (x=2460) - Included from UC34
        list.add(new UseCase("UC32", "UC32: Tìm kiếm tài xế", "Gần nhất (Haversine)", "Tìm kiếm tài xế khả dụng gần nhất", "SUB5", 2460, 980, 250, 68));
        list.add(new UseCase("UC33", "UC33: Tính toán thời gian", "Dự kiến giao hàng (ETA)", "Tính toán thời gian dự kiến giao hàng Quy Nhơn", "SUB5", 2460, 1100, 250, 68));

        // ==========================================
        // SUB7: Mô phỏng Đa luồng & Kiểm định (x=1630, y=1370, w=1330, h=890)
        // Col 3 (x=2770) - Facing Simulator Engine & Admin Actors
        list.add(new UseCase("UC43", "UC43: Cấu hình tham số", "Kiểm thử (Threads/Lock)", "Cấu hình số luồng, số đơn và cơ chế khóa", "SUB7", 2770, 1460, 260, 68));
        list.add(new UseCase("UC44", "UC44: Kích hoạt mô phỏng", "Đa luồng đồng thời", "Khởi chạy kịch bản mô phỏng đa luồng đồng thời", "SUB7", 2770, 1580, 260, 72));
        list.add(new UseCase("UC47", "UC47: So chuẩn 4 cơ chế khóa", "(NO/SYNC/OPT/FILE)", "Đo kiểm so sánh 4 cơ chế khóa tương tranh", "SUB7", 2770, 1720, 260, 68));
        list.add(new UseCase("UC48", "UC48: Tự động phát hiện", "Lỗi Race Conditions", "Phát hiện tranh chấp: Double Assign, Oversell", "SUB7", 2770, 1860, 260, 72));
        // Col 2 (x=2460) - Included / Extended
        list.add(new UseCase("UC45", "UC45: Mô phỏng Đặt hàng", "(CustomerThread)", "Mô phỏng khách hàng đặt hàng đồng thời", "SUB7", 2460, 1530, 250, 68));
        list.add(new UseCase("UC46", "UC46: Mô phỏng Điều phối", "(DispatchThread)", "Mô phỏng bộ điều phối gán đơn đồng thời", "SUB7", 2460, 1630, 250, 68));
        list.add(new UseCase("UC49", "UC49: Xuất báo cáo", "Số liệu & Thông lượng", "Xuất bảng so sánh hiệu năng và thời gian thực thi", "SUB7", 2460, 1860, 250, 68));

        return list;
    }

    private static List<UCRelation> buildRelations() {
        List<UCRelation> list = new ArrayList<>();

        // 1. Customer Actor Associations (Direct solid connections to Col 1)
        list.add(new UCRelation("ACT_CUSTOMER", "UC01", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC02", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC03", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC06", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC08", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC12", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC13", "assoc", ""));
        list.add(new UCRelation("ACT_CUSTOMER", "UC14", "assoc", ""));

        // 2. Restaurant Actor Associations (Direct solid connections to Col 1)
        list.add(new UCRelation("ACT_RESTAURANT", "UC16", "assoc", ""));
        list.add(new UCRelation("ACT_RESTAURANT", "UC17", "assoc", ""));
        list.add(new UCRelation("ACT_RESTAURANT", "UC18", "assoc", ""));
        list.add(new UCRelation("ACT_RESTAURANT", "UC19", "assoc", ""));
        list.add(new UCRelation("ACT_RESTAURANT", "UC20", "assoc", ""));
        list.add(new UCRelation("ACT_RESTAURANT", "UC21", "assoc", ""));

        // 3. Driver Actor Associations (Direct solid connections to Col 1)
        list.add(new UCRelation("ACT_DRIVER", "UC25", "assoc", ""));
        list.add(new UCRelation("ACT_DRIVER", "UC26", "assoc", ""));
        list.add(new UCRelation("ACT_DRIVER", "UC27", "assoc", ""));
        list.add(new UCRelation("ACT_DRIVER", "UC28", "assoc", ""));
        list.add(new UCRelation("ACT_DRIVER", "UC29", "assoc", ""));

        // 4. Admin Actor Associations (Direct solid connections to Col 3)
        list.add(new UCRelation("ACT_ADMIN", "UC35", "assoc", ""));
        list.add(new UCRelation("ACT_ADMIN", "UC37", "assoc", ""));
        list.add(new UCRelation("ACT_ADMIN", "UC40", "assoc", ""));
        list.add(new UCRelation("ACT_ADMIN", "UC41", "assoc", ""));
        list.add(new UCRelation("ACT_ADMIN", "UC42", "assoc", ""));
        list.add(new UCRelation("ACT_ADMIN", "UC43", "assoc", ""));
        list.add(new UCRelation("ACT_ADMIN", "UC44", "assoc", ""));

        // 5. Automated Dispatcher Engine Associations (Direct solid connections to Col 3)
        list.add(new UCRelation("ACT_DISPATCHER", "UC31", "assoc", ""));
        list.add(new UCRelation("ACT_DISPATCHER", "UC34", "assoc", ""));

        // 6. Concurrency Simulator Engine Associations (Direct solid connections to Col 3)
        list.add(new UCRelation("ACT_SIMULATOR", "UC44", "assoc", ""));
        list.add(new UCRelation("ACT_SIMULATOR", "UC47", "assoc", ""));
        list.add(new UCRelation("ACT_SIMULATOR", "UC48", "assoc", ""));

        // 7. Internal System Relationships (<<include>> & <<extend>>)
        // SUB1
        list.add(new UCRelation("UC02", "UC04", "include", "<<include>>"));
        list.add(new UCRelation("UC05", "UC04", "extend", "<<extend>>"));

        // SUB2
        list.add(new UCRelation("UC07", "UC06", "extend", "<<extend>>"));
        list.add(new UCRelation("UC08", "UC09", "include", "<<include>>"));
        list.add(new UCRelation("UC08", "UC10", "include", "<<include>>"));
        list.add(new UCRelation("UC10", "UC11", "include", "<<include>>"));
        list.add(new UCRelation("UC14", "UC15", "include", "<<include>>"));

        // SUB3
        list.add(new UCRelation("UC21", "UC22", "include", "<<include>>"));
        list.add(new UCRelation("UC23", "UC20", "extend", "<<extend>>"));
        list.add(new UCRelation("UC24", "UC20", "extend", "<<extend>>"));

        // SUB4
        list.add(new UCRelation("UC29", "UC30", "include", "<<include>>"));

        // SUB6
        list.add(new UCRelation("UC36", "UC35", "extend", "<<extend>>"));
        list.add(new UCRelation("UC38", "UC37", "extend", "<<extend>>"));
        list.add(new UCRelation("UC39", "UC37", "extend", "<<extend>>"));

        // SUB5
        list.add(new UCRelation("UC34", "UC32", "include", "<<include>>"));
        list.add(new UCRelation("UC34", "UC33", "include", "<<include>>"));

        // SUB7
        list.add(new UCRelation("UC44", "UC45", "include", "<<include>>"));
        list.add(new UCRelation("UC44", "UC46", "include", "<<include>>"));
        list.add(new UCRelation("UC49", "UC48", "extend", "<<extend>>"));

        return list;
    }

    private static UseCase findUseCase(String id, List<UseCase> list) {
        for (UseCase uc : list) {
            if (uc.id.equals(id)) return uc;
        }
        return null;
    }

    private static Actor findActor(String id, List<Actor> list) {
        for (Actor a : list) {
            if (a.id.equals(id)) return a;
        }
        return null;
    }

    // ==========================================
    // 1. GENERATE MONOCHROME STANDALONE SVG
    // ==========================================
    private static void generateSvg(List<Actor> actors, List<Subsystem> subsystems, List<UseCase> useCases, List<UCRelation> relations) throws Exception {
        StringBuilder sb = new StringBuilder();
        int width = 3380;
        int height = 2320;

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 ").append(width).append(" ").append(height).append("\" width=\"").append(width).append("\" height=\"").append(height).append("\">\n");

        sb.append("  <defs>\n");
        sb.append("    <marker id=\"openarrow\" markerWidth=\"12\" markerHeight=\"10\" refX=\"11\" refY=\"5\" orient=\"auto\">\n");
        sb.append("      <polyline points=\"1 1, 11 5, 1 9\" fill=\"none\" stroke=\"#000000\" stroke-width=\"1.8\" />\n");
        sb.append("    </marker>\n");
        sb.append("    <style>\n");
        sb.append("      .system-title { font-family: 'Segoe UI', Arial, sans-serif; font-size: 24px; font-weight: bold; fill: #000000; letter-spacing: 1px; }\n");
        sb.append("      .system-subtitle { font-family: 'Segoe UI', Arial, sans-serif; font-size: 14px; fill: #444444; }\n");
        sb.append("      .sub-title { font-family: 'Segoe UI', Arial, sans-serif; font-size: 16px; font-weight: bold; fill: #000000; }\n");
        sb.append("      .sub-en { font-family: 'Segoe UI', Arial, sans-serif; font-size: 12px; font-style: italic; fill: #555555; }\n");
        sb.append("      .actor-title { font-family: 'Segoe UI', Arial, sans-serif; font-size: 17px; font-weight: bold; fill: #000000; text-anchor: middle; }\n");
        sb.append("      .actor-sub { font-family: 'Segoe UI', Arial, sans-serif; font-size: 13px; fill: #444444; text-anchor: middle; }\n");
        sb.append("      .uc-code { font-family: 'Segoe UI', Arial, sans-serif; font-size: 13px; font-weight: bold; fill: #000000; text-anchor: middle; }\n");
        sb.append("      .uc-line2 { font-family: 'Segoe UI', Arial, sans-serif; font-size: 12px; fill: #111111; text-anchor: middle; }\n");
        sb.append("      .rel-label { font-family: 'Segoe UI', Arial, sans-serif; font-size: 11px; font-style: italic; font-weight: bold; fill: #000000; text-anchor: middle; }\n");
        sb.append("    </style>\n");
        sb.append("  </defs>\n\n");

        // Background: Pure White
        sb.append("  <rect width=\"100%\" height=\"100%\" fill=\"#ffffff\" />\n\n");

        // Main Header Banner
        sb.append("  <rect x=\"30\" y=\"20\" width=\"").append(width - 60).append("\" height=\"60\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"2\" />\n");
        sb.append("  <text x=\"").append(width / 2).append("\" y=\"45\" text-anchor=\"middle\" class=\"system-title\">TỔNG THỂ USE CASE DIAGRAM — HỆ THỐNG ĐẶT VÀ GIAO ĐỒ ĂN TRỰC TUYẾN (FOOD DELIVERY SIMULATION SYSTEM - FDS)</text>\n");
        sb.append("  <text x=\"").append(width / 2).append("\" y=\"68\" text-anchor=\"middle\" class=\"system-subtitle\">LAB211 — OOP with Java | Full 49 Use Cases, 6 Actors, 7 Subsystems (Academic Monochrome Specification)</text>\n\n");

        // System Boundary Box
        int sysX = 350;
        int sysY = 100;
        int sysW = 2640;
        int sysH = 2170;
        sb.append("  <!-- System Boundary -->\n");
        sb.append("  <rect x=\"").append(sysX).append("\" y=\"").append(sysY).append("\" width=\"").append(sysW).append("\" height=\"").append(sysH).append("\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"2.5\" rx=\"6\" />\n");
        sb.append("  <rect x=\"").append(sysX).append("\" y=\"").append(sysY).append("\" width=\"460\" height=\"32\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"1.5\" />\n");
        sb.append("  <text x=\"").append(sysX + 15).append("\" y=\"").append(sysY + 22).append("\" font-family=\"'Segoe UI', Arial, sans-serif\" font-size=\"14px\" font-weight=\"bold\" fill=\"#000000\">&lt;&lt;system boundary&gt;&gt; Food Delivery System</text>\n\n");

        // Subsystems
        for (Subsystem sub : subsystems) {
            sb.append("  <!-- Subsystem ").append(sub.id).append(" -->\n");
            sb.append("  <rect x=\"").append(sub.x).append("\" y=\"").append(sub.y).append("\" width=\"").append(sub.w).append("\" height=\"").append(sub.h).append("\" fill=\"#ffffff\" stroke=\"#444444\" stroke-width=\"1.2\" stroke-dasharray=\"6,4\" rx=\"4\" />\n");
            sb.append("  <text x=\"").append(sub.x + 16).append("\" y=\"").append(sub.y + 24).append("\" class=\"sub-title\">").append(sub.title).append("</text>\n");
            sb.append("  <text x=\"").append(sub.x + 16).append("\" y=\"").append(sub.y + 42).append("\" class=\"sub-en\">[").append(sub.enTitle).append("]</text>\n");
        }

        // Draw Relationships
        sb.append("\n  <!-- Relationships -->\n");
        for (UCRelation rel : relations) {
            Actor a = findActor(rel.fromId, actors);
            UseCase ucFrom = findUseCase(rel.fromId, useCases);
            UseCase ucTo = findUseCase(rel.toId, useCases);

            if (a != null && ucTo != null) {
                // Actor to UseCase Association (Solid Line)
                int startX = a.x > ucTo.x ? a.x - 30 : a.x + 30;
                int startY = a.y;
                int endX = a.x > ucTo.x ? ucTo.right() : ucTo.left();
                int endY = ucTo.y;
                sb.append("  <line x1=\"").append(startX).append("\" y1=\"").append(startY).append("\" x2=\"").append(endX).append("\" y2=\"").append(endY).append("\" stroke=\"#000000\" stroke-width=\"1.4\" />\n");
            } else if (ucFrom != null && ucTo != null) {
                // UseCase to UseCase Dependency (Dashed with Arrow)
                int x1, y1, x2, y2;
                if (Math.abs(ucFrom.x - ucTo.x) >= Math.abs(ucFrom.y - ucTo.y)) {
                    // Horizontal flow
                    if (ucFrom.x < ucTo.x) {
                        x1 = ucFrom.right();
                        y1 = ucFrom.y;
                        x2 = ucTo.left();
                        y2 = ucTo.y;
                    } else {
                        x1 = ucFrom.left();
                        y1 = ucFrom.y;
                        x2 = ucTo.right();
                        y2 = ucTo.y;
                    }
                } else {
                    // Vertical flow
                    if (ucFrom.y < ucTo.y) {
                        x1 = ucFrom.x;
                        y1 = ucFrom.bottom();
                        x2 = ucTo.x;
                        y2 = ucTo.top();
                    } else {
                        x1 = ucFrom.x;
                        y1 = ucFrom.top();
                        x2 = ucTo.x;
                        y2 = ucTo.bottom();
                    }
                }

                sb.append("  <line x1=\"").append(x1).append("\" y1=\"").append(y1).append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\" stroke=\"#000000\" stroke-width=\"1.5\" stroke-dasharray=\"6,4\" marker-end=\"url(#openarrow)\" />\n");

                // Label in center with white background pill
                int midX = (x1 + x2) / 2;
                int midY = (y1 + y2) / 2;
                int lw = 76;
                int lh = 17;
                sb.append("  <rect x=\"").append(midX - lw / 2).append("\" y=\"").append(midY - lh / 2).append("\" width=\"").append(lw).append("\" height=\"").append(lh).append("\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"0.8\" rx=\"3\" />\n");
                sb.append("  <text x=\"").append(midX).append("\" y=\"").append(midY + 4).append("\" class=\"rel-label\">").append(rel.label).append("</text>\n");
            }
        }

        // Draw Use Cases (Ellipses)
        sb.append("\n  <!-- Use Cases -->\n");
        for (UseCase uc : useCases) {
            sb.append("  <g id=\"").append(uc.id).append("\">\n");
            sb.append("    <ellipse cx=\"").append(uc.x).append("\" cy=\"").append(uc.y).append("\" rx=\"").append(uc.w / 2).append("\" ry=\"").append(uc.h / 2).append("\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"2\" />\n");
            sb.append("    <text x=\"").append(uc.x).append("\" y=\"").append(uc.y - 4).append("\" class=\"uc-code\">").append(uc.line1).append("</text>\n");
            sb.append("    <text x=\"").append(uc.x).append("\" y=\"").append(uc.y + 16).append("\" class=\"uc-line2\">").append(uc.line2).append("</text>\n");
            sb.append("  </g>\n");
        }

        // Draw Actors
        sb.append("\n  <!-- Actors -->\n");
        for (Actor a : actors) {
            sb.append("  <g id=\"").append(a.id).append("\">\n");
            if ("human".equals(a.type)) {
                // Classic UML Stick Figure
                int hx = a.x;
                int hy = a.y - 45;
                // Head
                sb.append("    <circle cx=\"").append(hx).append("\" cy=\"").append(hy).append("\" r=\"16\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"2.2\" />\n");
                // Body
                sb.append("    <line x1=\"").append(hx).append("\" y1=\"").append(hy + 16).append("\" x2=\"").append(hx).append("\" y2=\"").append(hy + 52).append("\" stroke=\"#000000\" stroke-width=\"2.2\" />\n");
                // Arms
                sb.append("    <line x1=\"").append(hx - 26).append("\" y1=\"").append(hy + 28).append("\" x2=\"").append(hx + 26).append("\" y2=\"").append(hy + 28).append("\" stroke=\"#000000\" stroke-width=\"2.2\" />\n");
                // Legs
                sb.append("    <line x1=\"").append(hx).append("\" y1=\"").append(hy + 52).append("\" x2=\"").append(hx - 22).append("\" y2=\"").append(hy + 88).append("\" stroke=\"#000000\" stroke-width=\"2.2\" />\n");
                sb.append("    <line x1=\"").append(hx).append("\" y1=\"").append(hy + 52).append("\" x2=\"").append(hx + 22).append("\" y2=\"").append(hy + 88).append("\" stroke=\"#000000\" stroke-width=\"2.2\" />\n");
                // Label
                sb.append("    <text x=\"").append(hx).append("\" y=\"").append(hy + 112).append("\" class=\"actor-title\">").append(a.name).append("</text>\n");
                sb.append("    <text x=\"").append(hx).append("\" y=\"").append(hy + 130).append("\" class=\"actor-sub\">").append(a.subtitle).append("</text>\n");
            } else {
                // System Actor box
                int bx = a.x - 75;
                int by = a.y - 45;
                sb.append("    <rect x=\"").append(bx).append("\" y=\"").append(by).append("\" width=\"150\" height=\"80\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"2.2\" rx=\"4\" />\n");
                sb.append("    <text x=\"").append(a.x).append("\" y=\"").append(by + 24).append("\" font-family=\"'Segoe UI', Arial, sans-serif\" font-size=\"12px\" font-weight=\"bold\" fill=\"#444444\" text-anchor=\"middle\">&lt;&lt;system actor&gt;&gt;</text>\n");
                sb.append("    <text x=\"").append(a.x).append("\" y=\"").append(by + 48).append("\" font-family=\"'Segoe UI', Arial, sans-serif\" font-size=\"14px\" font-weight=\"bold\" fill=\"#000000\" text-anchor=\"middle\">").append(a.name).append("</text>\n");
                sb.append("    <text x=\"").append(a.x).append("\" y=\"").append(by + 68).append("\" font-family=\"'Segoe UI', Arial, sans-serif\" font-size=\"11px\" fill=\"#555555\" text-anchor=\"middle\">").append(a.subtitle).append("</text>\n");
            }
            sb.append("  </g>\n");
        }

        // Legend Box
        sb.append("\n  <!-- Legend / Notation -->\n");
        int legX = 400;
        int legY = 2225;
        sb.append("  <rect x=\"").append(legX).append("\" y=\"").append(legY).append("\" width=\"1020\" height=\"35\" fill=\"#ffffff\" stroke=\"#000000\" stroke-width=\"1\" rx=\"3\" />\n");
        sb.append("  <text x=\"").append(legX + 15).append("\" y=\"").append(legY + 22).append("\" font-family=\"'Segoe UI', Arial, sans-serif\" font-size=\"12px\" fill=\"#000000\">");
        sb.append("<b>Ký hiệu chuẩn UML:</b> &nbsp; ── Association (Liên kết tác nhân) &nbsp;&nbsp;|&nbsp;&nbsp; - - &gt; &lt;&lt;include&gt;&gt; (Bắt buộc bao gồm) &nbsp;&nbsp;|&nbsp;&nbsp; - - &gt; &lt;&lt;extend&gt;&gt; (Mở rộng tùy chọn) &nbsp;&nbsp;|&nbsp;&nbsp; Academic B&amp;W");
        sb.append("</text>\n");

        sb.append("</svg>\n");

        writeFile("docs/Full_UseCase_Diagram.svg", sb.toString());
        System.out.println("Generated: docs/Full_UseCase_Diagram.svg");
    }

    // ==========================================
    // 2. GENERATE STANDALONE OFFLINE HTML VIEWER
    // ==========================================
    private static void generateHtml(List<Actor> actors, List<Subsystem> subsystems, List<UseCase> useCases, List<UCRelation> relations) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"vi\">\n<head>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<title>Sơ Đồ Use Case Toàn Bộ Hệ Thống Food Delivery (FDS) - LAB211</title>\n");
        sb.append("<style>\n");
        sb.append("  * { box-sizing: border-box; margin: 0; padding: 0; }\n");
        sb.append("  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #18191a; color: #e4e6eb; height: 100vh; display: flex; flex-direction: column; overflow: hidden; }\n");
        sb.append("  header { background: #242526; padding: 10px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #3a3b3c; flex-shrink: 0; }\n");
        sb.append("  .title-group h1 { font-size: 18px; color: #ffffff; }\n");
        sb.append("  .title-group p { font-size: 12px; color: #b0b3b8; }\n");
        sb.append("  .controls { display: flex; gap: 8px; align-items: center; }\n");
        sb.append("  button { background: #3a3b3c; color: #ffffff; border: 1px solid #4e4f50; padding: 7px 14px; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 500; transition: all 0.2s; }\n");
        sb.append("  button:hover { background: #4e4f50; }\n");
        sb.append("  .btn-primary { background: #000000; border: 1px solid #ffffff; color: #ffffff; font-weight: bold; }\n");
        sb.append("  .btn-primary:hover { background: #333333; }\n");
        sb.append("  #zoomLevel { font-size: 13px; color: #ffffff; min-width: 50px; text-align: center; }\n");
        sb.append("  .content { display: flex; flex: 1; overflow: hidden; }\n");
        sb.append("  #viewport { flex: 1; background: #e5e5e5; position: relative; overflow: hidden; cursor: grab; display: flex; justify-content: center; align-items: center; }\n");
        sb.append("  #viewport:active { cursor: grabbing; }\n");
        sb.append("  #svgWrapper { transform-origin: 0 0; position: absolute; top: 0; left: 0; }\n");
        sb.append("  #sidebar { width: 380px; background: #242526; border-left: 1px solid #3a3b3c; display: flex; flex-direction: column; flex-shrink: 0; }\n");
        sb.append("  .sidebar-header { padding: 12px 16px; border-bottom: 1px solid #3a3b3c; }\n");
        sb.append("  .search-box { width: 100%; padding: 8px 12px; background: #3a3b3c; border: 1px solid #4e4f50; border-radius: 6px; color: #ffffff; font-size: 13px; outline: none; }\n");
        sb.append("  .sidebar-content { overflow-y: auto; flex: 1; padding: 12px 16px; }\n");
        sb.append("  .subsystem-group { margin-bottom: 16px; }\n");
        sb.append("  .subsystem-name { font-size: 12px; font-weight: bold; color: #4facfe; margin-bottom: 6px; text-transform: uppercase; letter-spacing: 0.5px; }\n");
        sb.append("  .uc-card { background: #18191a; border: 1px solid #3a3b3c; border-radius: 6px; padding: 8px 10px; margin-bottom: 6px; cursor: pointer; transition: 0.15s; }\n");
        sb.append("  .uc-card:hover { border-color: #ffffff; background: #282a2d; }\n");
        sb.append("  .uc-id { font-weight: bold; color: #ffffff; font-size: 12px; }\n");
        sb.append("  .uc-desc { font-size: 12px; color: #b0b3b8; margin-top: 2px; }\n");
        sb.append("</style>\n</head>\n<body>\n");

        sb.append("<header>\n");
        sb.append("  <div class=\"title-group\">\n");
        sb.append("    <h1>SƠ ĐỒ USE CASE TOÀN DIỆN — FOOD DELIVERY SIMULATION (FDS)</h1>\n");
        sb.append("    <p>LAB211 OOP with Java • Quy Nhơn City • Đầy đủ 49 Use Cases • 6 Tác nhân • Academic B&amp;W Mode</p>\n");
        sb.append("  </div>\n");
        sb.append("  <div class=\"controls\">\n");
        sb.append("    <button onclick=\"zoomIn()\">+ Phóng to</button>\n");
        sb.append("    <button onclick=\"zoomOut()\">- Thu nhỏ</button>\n");
        sb.append("    <button onclick=\"resetZoom()\">Vừa màn hình</button>\n");
        sb.append("    <span id=\"zoomLevel\">100%</span>\n");
        sb.append("    <button class=\"btn-primary\" onclick=\"downloadSvg()\">Tải SVG</button>\n");
        sb.append("    <button onclick=\"window.print()\">In Diagram</button>\n");
        sb.append("  </div>\n");
        sb.append("</header>\n");

        sb.append("<div class=\"content\">\n");
        sb.append("  <div id=\"viewport\">\n");
        sb.append("    <div id=\"svgWrapper\">\n");

        File svgFile = new File("docs/Full_UseCase_Diagram.svg");
        byte[] encoded = java.nio.file.Files.readAllBytes(svgFile.toPath());
        String svgContent = new String(encoded, StandardCharsets.UTF_8);
        svgContent = svgContent.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        sb.append(svgContent);

        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div id=\"sidebar\">\n");
        sb.append("    <div class=\"sidebar-header\">\n");
        sb.append("      <input type=\"text\" id=\"searchInput\" class=\"search-box\" placeholder=\"Tìm kiếm Use Case theo mã hoặc tên...\" oninput=\"filterUseCases()\">\n");
        sb.append("    </div>\n");
        sb.append("    <div class=\"sidebar-content\" id=\"sidebarContent\">\n");

        for (Subsystem sub : subsystems) {
            sb.append("      <div class=\"subsystem-group\" data-sub=\"").append(sub.id).append("\">\n");
            sb.append("        <div class=\"subsystem-name\">").append(sub.title).append("</div>\n");
            for (UseCase uc : useCases) {
                if (uc.subsystemId.equals(sub.id)) {
                    sb.append("        <div class=\"uc-card\" onclick=\"focusUseCase('").append(uc.id).append("')\" data-title=\"").append(uc.fullTitle.toLowerCase()).append(" ").append(uc.id.toLowerCase()).append("\">\n");
                    sb.append("          <span class=\"uc-id\">").append(uc.id).append("</span>\n");
                    sb.append("          <div class=\"uc-desc\">").append(uc.fullTitle).append("</div>\n");
                    sb.append("        </div>\n");
                }
            }
            sb.append("      </div>\n");
        }

        sb.append("    </div>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        sb.append("<script>\n");
        sb.append("  let scale = 0.55;\n");
        sb.append("  let panX = 20, panY = 20;\n");
        sb.append("  let isDragging = false;\n");
        sb.append("  let startX, startY;\n");
        sb.append("  const wrapper = document.getElementById('svgWrapper');\n");
        sb.append("  const viewport = document.getElementById('viewport');\n");
        sb.append("  const zoomText = document.getElementById('zoomLevel');\n\n");
        sb.append("  function updateTransform() {\n");
        sb.append("    wrapper.style.transform = `translate(${panX}px, ${panY}px) scale(${scale})`;\n");
        sb.append("    zoomText.innerText = Math.round(scale * 100) + '%';\n");
        sb.append("  }\n\n");
        sb.append("  function zoomIn() { scale = Math.min(scale * 1.2, 3.0); updateTransform(); }\n");
        sb.append("  function zoomOut() { scale = Math.max(scale / 1.2, 0.15); updateTransform(); }\n");
        sb.append("  function resetZoom() {\n");
        sb.append("    const vw = viewport.clientWidth;\n");
        sb.append("    const vh = viewport.clientHeight;\n");
        sb.append("    scale = Math.min(vw / 3400, vh / 2350) * 0.95;\n");
        sb.append("    panX = (vw - 3380 * scale) / 2;\n");
        sb.append("    panY = 10;\n");
        sb.append("    updateTransform();\n");
        sb.append("  }\n\n");
        sb.append("  viewport.addEventListener('mousedown', (e) => {\n");
        sb.append("    if (e.target.closest('#sidebar')) return;\n");
        sb.append("    isDragging = true;\n");
        sb.append("    startX = e.clientX - panX;\n");
        sb.append("    startY = e.clientY - panY;\n");
        sb.append("  });\n\n");
        sb.append("  window.addEventListener('mousemove', (e) => {\n");
        sb.append("    if (!isDragging) return;\n");
        sb.append("    panX = e.clientX - startX;\n");
        sb.append("    panY = e.clientY - startY;\n");
        sb.append("    updateTransform();\n");
        sb.append("  });\n\n");
        sb.append("  window.addEventListener('mouseup', () => { isDragging = false; });\n\n");
        sb.append("  viewport.addEventListener('wheel', (e) => {\n");
        sb.append("    e.preventDefault();\n");
        sb.append("    const zoomFactor = e.deltaY < 0 ? 1.1 : 0.9;\n");
        sb.append("    const rect = viewport.getBoundingClientRect();\n");
        sb.append("    const mouseX = e.clientX - rect.left;\n");
        sb.append("    const mouseY = e.clientY - rect.top;\n");
        sb.append("    const newScale = Math.max(0.15, Math.min(3.0, scale * zoomFactor));\n");
        sb.append("    panX = mouseX - (mouseX - panX) * (newScale / scale);\n");
        sb.append("    panY = mouseY - (mouseY - panY) * (newScale / scale);\n");
        sb.append("    scale = newScale;\n");
        sb.append("    updateTransform();\n");
        sb.append("  });\n\n");
        sb.append("  function focusUseCase(id) {\n");
        sb.append("    const el = document.getElementById(id);\n");
        sb.append("    if (!el) return;\n");
        sb.append("    const ellipse = el.querySelector('ellipse');\n");
        sb.append("    const cx = parseFloat(ellipse.getAttribute('cx'));\n");
        sb.append("    const cy = parseFloat(ellipse.getAttribute('cy'));\n");
        sb.append("    scale = 1.0;\n");
        sb.append("    panX = viewport.clientWidth / 2 - cx * scale;\n");
        sb.append("    panY = viewport.clientHeight / 2 - cy * scale;\n");
        sb.append("    updateTransform();\n");
        sb.append("    ellipse.setAttribute('stroke-width', '5');\n");
        sb.append("    setTimeout(() => ellipse.setAttribute('stroke-width', '2'), 1500);\n");
        sb.append("  }\n\n");
        sb.append("  function filterUseCases() {\n");
        sb.append("    const q = document.getElementById('searchInput').value.toLowerCase().trim();\n");
        sb.append("    const cards = document.querySelectorAll('.uc-card');\n");
        sb.append("    cards.forEach(c => {\n");
        sb.append("      const t = c.getAttribute('data-title');\n");
        sb.append("      c.style.display = t.includes(q) ? 'block' : 'none';\n");
        sb.append("    });\n");
        sb.append("  }\n\n");
        sb.append("  function downloadSvg() {\n");
        sb.append("    const svg = document.querySelector('#svgWrapper svg');\n");
        sb.append("    const blob = new Blob([svg.outerHTML], { type: 'image/svg+xml;charset=utf-8' });\n");
        sb.append("    const url = URL.createObjectURL(blob);\n");
        sb.append("    const a = document.createElement('a');\n");
        sb.append("    a.href = url;\n");
        sb.append("    a.download = 'Full_UseCase_Diagram.svg';\n");
        sb.append("    a.click();\n");
        sb.append("  }\n\n");
        sb.append("  window.onload = resetZoom;\n");
        sb.append("</script>\n</body>\n</html>\n");

        writeFile("docs/use_case_view.html", sb.toString());
        System.out.println("Generated: docs/use_case_view.html");
    }

    // ==========================================
    // 3. GENERATE NATIVE DRAW.IO XML (.drawio)
    // ==========================================
    private static void generateDrawio(List<Actor> actors, List<Subsystem> subsystems, List<UseCase> useCases, List<UCRelation> relations) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<mxfile host=\"app.diagrams.net\" modified=\"2026-11-09T10:45:00.000Z\" agent=\"Antigravity AI\" version=\"21.1.0\" type=\"device\">\n");
        sb.append("  <diagram id=\"FoodDeliveryUseCase\" name=\"Food Delivery Full Use Case\">\n");
        sb.append("    <mxGraphModel dx=\"2400\" dy=\"1800\" grid=\"1\" gridSize=\"10\" guides=\"1\" tooltips=\"1\" connect=\"1\" arrows=\"1\" fold=\"1\" page=\"1\" pageScale=\"1\" pageWidth=\"3400\" pageHeight=\"2400\" background=\"#ffffff\">\n");
        sb.append("      <root>\n");
        sb.append("        <mxCell id=\"0\" />\n");
        sb.append("        <mxCell id=\"1\" parent=\"0\" />\n");

        int cellId = 2;

        // System Boundary
        sb.append("        <mxCell id=\"").append(cellId++).append("\" value=\"&lt;b&gt;HỆ THỐNG ĐẶT VÀ GIAO ĐỒ ĂN TRỰC TUYẾN (FOOD DELIVERY SIMULATION SYSTEM - FDS)&lt;/b&gt;\" style=\"shape=rect;html=1;whiteSpace=wrap;align=center;verticalAlign=top;fillColor=#ffffff;strokeColor=#000000;strokeWidth=2;fontSize=16;fontColor=#000000;\" vertex=\"1\" parent=\"1\">\n");
        sb.append("          <mxGeometry x=\"350\" y=\"100\" width=\"2640\" height=\"2170\" as=\"geometry\" />\n");
        sb.append("        </mxCell>\n");

        // Subsystems
        for (Subsystem sub : subsystems) {
            sb.append("        <mxCell id=\"").append(cellId++).append("\" value=\"&lt;b&gt;").append(escapeXml(sub.title)).append("&lt;/b&gt;&lt;br&gt;&lt;i&gt;[").append(escapeXml(sub.enTitle)).append("]&lt;/i&gt;\" style=\"shape=rect;html=1;whiteSpace=wrap;align=left;verticalAlign=top;fillColor=none;strokeColor=#444444;strokeWidth=1;dashed=1;fontSize=13;fontColor=#000000;spacingLeft=10;spacingTop=6;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"").append(sub.x).append("\" y=\"").append(sub.y).append("\" width=\"").append(sub.w).append("\" height=\"").append(sub.h).append("\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");
        }

        // Actors
        java.util.Map<String, Integer> actorCellMap = new java.util.HashMap<>();
        for (Actor a : actors) {
            int aId = cellId++;
            actorCellMap.put(a.id, aId);
            if ("human".equals(a.type)) {
                sb.append("        <mxCell id=\"").append(aId).append("\" value=\"&lt;b&gt;").append(escapeXml(a.name)).append("&lt;/b&gt;&lt;br&gt;").append(escapeXml(a.subtitle)).append("\" style=\"shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;fillColor=#ffffff;strokeColor=#000000;strokeWidth=2;fontColor=#000000;fontSize=13;\" vertex=\"1\" parent=\"1\">\n");
                sb.append("          <mxGeometry x=\"").append(a.x - 25).append("\" y=\"").append(a.y - 45).append("\" width=\"50\" height=\"90\" as=\"geometry\" />\n");
                sb.append("        </mxCell>\n");
            } else {
                sb.append("        <mxCell id=\"").append(aId).append("\" value=\"&amp;lt;&amp;lt;system&amp;gt;&amp;gt;&lt;br&gt;&lt;b&gt;").append(escapeXml(a.name)).append("&lt;/b&gt;&lt;br&gt;").append(escapeXml(a.subtitle)).append("\" style=\"shape=rect;html=1;whiteSpace=wrap;align=center;verticalAlign=middle;fillColor=#ffffff;strokeColor=#000000;strokeWidth=2;fontColor=#000000;fontSize=12;rounded=1;\" vertex=\"1\" parent=\"1\">\n");
                sb.append("          <mxGeometry x=\"").append(a.x - 75).append("\" y=\"").append(a.y - 40).append("\" width=\"150\" height=\"80\" as=\"geometry\" />\n");
                sb.append("        </mxCell>\n");
            }
        }

        // Use Cases
        java.util.Map<String, Integer> ucCellMap = new java.util.HashMap<>();
        for (UseCase uc : useCases) {
            int uId = cellId++;
            ucCellMap.put(uc.id, uId);
            sb.append("        <mxCell id=\"").append(uId).append("\" value=\"&lt;b&gt;").append(escapeXml(uc.line1)).append("&lt;/b&gt;&lt;br&gt;").append(escapeXml(uc.line2)).append("\" style=\"ellipse;whiteSpace=wrap;html=1;fillColor=#ffffff;strokeColor=#000000;strokeWidth=2;fontColor=#000000;fontSize=12;align=center;verticalAlign=middle;\" vertex=\"1\" parent=\"1\">\n");
            sb.append("          <mxGeometry x=\"").append(uc.left()).append("\" y=\"").append(uc.top()).append("\" width=\"").append(uc.w).append("\" height=\"").append(uc.h).append("\" as=\"geometry\" />\n");
            sb.append("        </mxCell>\n");
        }

        // Relations
        for (UCRelation rel : relations) {
            Integer srcId = actorCellMap.containsKey(rel.fromId) ? actorCellMap.get(rel.fromId) : ucCellMap.get(rel.fromId);
            Integer dstId = actorCellMap.containsKey(rel.toId) ? actorCellMap.get(rel.toId) : ucCellMap.get(rel.toId);

            if (srcId != null && dstId != null) {
                int rId = cellId++;
                if ("assoc".equals(rel.type)) {
                    sb.append("        <mxCell id=\"").append(rId).append("\" style=\"endArrow=none;html=1;rounded=0;strokeColor=#000000;strokeWidth=1.5;\" edge=\"1\" parent=\"1\" source=\"").append(srcId).append("\" target=\"").append(dstId).append("\">\n");
                    sb.append("          <mxGeometry relative=\"1\" as=\"geometry\" />\n");
                    sb.append("        </mxCell>\n");
                } else {
                    sb.append("        <mxCell id=\"").append(rId).append("\" value=\"").append(escapeXml(rel.label)).append("\" style=\"endArrow=open;dashed=1;html=1;rounded=0;strokeColor=#000000;strokeWidth=1.5;endFill=0;fontSize=11;fontStyle=2;fontColor=#000000;labelBackgroundColor=#ffffff;\" edge=\"1\" parent=\"1\" source=\"").append(srcId).append("\" target=\"").append(dstId).append("\">\n");
                    sb.append("          <mxGeometry relative=\"1\" as=\"geometry\" />\n");
                    sb.append("        </mxCell>\n");
                }
            }
        }

        sb.append("      </root>\n");
        sb.append("    </mxGraphModel>\n");
        sb.append("  </diagram>\n");
        sb.append("</mxfile>\n");

        writeFile("docs/Full_UseCase_Diagram.drawio", sb.toString());
        System.out.println("Generated: docs/Full_UseCase_Diagram.drawio");
    }

    // ==========================================
    // 4. GENERATE COMPREHENSIVE MARKDOWN SPEC
    // ==========================================
    private static void generateMarkdown(List<Actor> actors, List<Subsystem> subsystems, List<UseCase> useCases, List<UCRelation> relations) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("# TÀI LIỆU ĐẶC TẢ VÀ SƠ ĐỒ USE CASE TOÀN DIỆN (FOOD DELIVERY SIMULATION SYSTEM - FDS)\n");
        sb.append("**Môn học:** LAB211 — Lập trình Hướng đối tượng với Java | FPT University  \n");
        sb.append("**Phạm vi triển khai:** Hệ thống Đặt & Giao đồ ăn trực tuyến (Thành phố Quy Nhơn)  \n");
        sb.append("**Quy chuẩn thiết kế:** UML 2.5 Academic Monochrome Specification (Đen - Trắng chuẩn đồ án)  \n\n");
        sb.append("---\n\n");

        sb.append("## 1. TỔNG QUAN HỆ THỐNG VÀ DANH SÁCH TÁC NHÂN (ACTORS)\n\n");
        sb.append("| STT | Mã Actor | Tên Tác Nhân | Vai Trò (Role) | Phân Loại | Nhiệm Vụ Trọng Tâm |\n");
        sb.append("| :---: | :--- | :--- | :--- | :---: | :--- |\n");
        sb.append("| 1 | `ACT_CUSTOMER` | **Khách hàng** (Customer) | Người dùng cuối | Human | Tìm kiếm quán ăn Quy Nhơn, đặt món, tính phí ship Haversine, theo dõi đơn và nộp đơn làm tài xế. |\n");
        sb.append("| 2 | `ACT_RESTAURANT` | **Quản lý Nhà hàng** (Restaurant) | Đối tác ẩm thực | Human | Quản lý menu món ăn, cập nhật số lượng tồn kho (stock), duyệt hoặc từ chối đơn hàng, xem doanh thu. |\n");
        sb.append("| 3 | `ACT_DRIVER` | **Tài xế Giao hàng** (Driver) | Đối tác vận chuyển | Human | Bật/tắt trạng thái nhận cuốc, cập nhật GPS, nhận đơn điều phối, xác nhận lấy hàng và giao thành công. |\n");
        sb.append("| 4 | `ACT_ADMIN` | **Quản trị viên** (Administrator) | Điều hành hệ thống | Human | Quản lý tài khoản, duyệt đơn ứng tuyển tài xế, quản lý mạng lưới nhà hàng, kiểm thử mô phỏng đa luồng. |\n");
        sb.append("| 5 | `ACT_DISPATCHER` | **Hệ thống Điều phối** (Dispatcher Engine) | Hệ thống tự động | System | Tự động quét hàng đợi `PENDING`, tính toán khoảng cách Haversine & ETA, ghép nối và khóa tài xế `BUSY`. |\n");
        sb.append("| 6 | `ACT_SIMULATOR` | **Bộ Mô phỏng Đa luồng** (Simulator Engine) | Hệ thống kiểm thử | System | Khởi tạo nhiều luồng đồng thời (Customer/Dispatch Thread), so chuẩn 4 cơ chế khóa và phát hiện Race Conditions. |\n\n");

        sb.append("---\n\n");
        sb.append("## 2. BẢNG ĐẶC TẢ CHI TIẾT 49 USE CASES THEO PHÂN HỆ\n\n");

        for (Subsystem sub : subsystems) {
            sb.append("### ").append(sub.title).append(" (`").append(sub.id).append("`)\n");
            sb.append("*Tên tiếng Anh:* ").append(sub.enTitle).append("  \n\n");
            sb.append("| Mã UC | Tên Use Case | Tác Nhân Chính | Quan Hệ Phụ Thuộc | Mô Tả Nghiệp Vụ Code Triển Khai |\n");
            sb.append("| :---: | :--- | :--- | :---: | :--- |\n");

            for (UseCase uc : useCases) {
                if (uc.subsystemId.equals(sub.id)) {
                    StringBuilder actStr = new StringBuilder();
                    for (UCRelation r : relations) {
                        if ("assoc".equals(r.type) && r.toId.equals(uc.id)) {
                            Actor a = findActor(r.fromId, actors);
                            if (a != null) {
                                if (actStr.length() > 0) actStr.append(", ");
                                actStr.append(a.name);
                            }
                        }
                    }
                    StringBuilder depStr = new StringBuilder();
                    for (UCRelation r : relations) {
                        if (!"assoc".equals(r.type)) {
                            if (r.fromId.equals(uc.id)) {
                                if (depStr.length() > 0) depStr.append("<br>");
                                depStr.append(r.label).append(" ").append(r.toId);
                            } else if (r.toId.equals(uc.id)) {
                                if (depStr.length() > 0) depStr.append("<br>");
                                depStr.append(r.fromId).append(" ").append(r.label);
                            }
                        }
                    }
                    if (depStr.length() == 0) depStr.append("─");
                    if (actStr.length() == 0) actStr.append("Hệ thống tự động");

                    sb.append("| **").append(uc.id).append("** | ").append(uc.fullTitle).append(" | ").append(actStr).append(" | ").append(depStr).append(" | Mã nguồn: `").append(uc.id).append("` trong package tương ứng. |\n");
                }
            }
            sb.append("\n");
        }

        sb.append("---\n\n");
        sb.append("## 3. SƠ ĐỒ USE CASE DẠNG MERMAID CODE\n\n");
        sb.append("```mermaid\n");
        sb.append("graph LR\n");
        sb.append("    subgraph FDS [\"HỆ THỐNG ĐẶT VÀ GIAO ĐỒ ĂN TRỰC TUYẾN (FOOD DELIVERY SIMULATION SYSTEM)\"]\n");

        for (Subsystem sub : subsystems) {
            sb.append("        subgraph ").append(sub.id).append(" [\"").append(sub.title).append("\"]\n");
            for (UseCase uc : useCases) {
                if (uc.subsystemId.equals(sub.id)) {
                    sb.append("            ").append(uc.id).append("((\"").append(uc.id).append(": ").append(uc.line1.replace(uc.id + ": ", "")).append(" ").append(uc.line2).append("\"))\n");
                }
            }
            sb.append("        end\n");
        }
        sb.append("    end\n\n");

        for (Actor a : actors) {
            sb.append("    ").append(a.id).append("[\"👤 ").append(a.name).append("\"]\n");
        }
        sb.append("\n");

        for (UCRelation r : relations) {
            if ("assoc".equals(r.type)) {
                sb.append("    ").append(r.fromId).append(" --- ").append(r.toId).append("\n");
            } else if ("include".equals(r.type)) {
                sb.append("    ").append(r.fromId).append(" -. \"&lt;&lt;include&gt;&gt;\" .-> ").append(r.toId).append("\n");
            } else if ("extend".equals(r.type)) {
                sb.append("    ").append(r.fromId).append(" -. \"&lt;&lt;extend&gt;&gt;\" .-> ").append(r.toId).append("\n");
            }
        }

        sb.append("```\n\n");
        sb.append("---\n\n");
        sb.append("## 4. DANH MỤC FILE XUẤT BẢN ĐÍNH KÈM\n");
        sb.append("- **Vector SVG Standalone:** [`docs/Full_UseCase_Diagram.svg`](file:///D:/NHOM_01_LAB211_FoodDelivery/docs/Full_UseCase_Diagram.svg) (Đen trắng chuẩn in ấn)\n");
        sb.append("- **Trang xem tương tác Offline:** [`docs/use_case_view.html`](file:///D:/NHOM_01_LAB211_FoodDelivery/docs/use_case_view.html) (Có Zoom, Pan, Tìm kiếm, In ấn)\n");
        sb.append("- **File thiết kế gốc Draw.io:** [`docs/Full_UseCase_Diagram.drawio`](file:///D:/NHOM_01_LAB211_FoodDelivery/docs/Full_UseCase_Diagram.drawio) (Kéo thả, chỉnh sửa trực tiếp)\n");

        writeFile("docs/use_case_diagram.md", sb.toString());
        System.out.println("Generated: docs/use_case_diagram.md");
    }

    private static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private static void writeFile(String path, String content) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            bw.write(content);
        }
    }
}
