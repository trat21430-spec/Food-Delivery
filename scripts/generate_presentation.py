"""
Script sinh Slide Báo cáo PowerPoint (.pptx) và Interactive HTML5 Slide Deck
cho Đề tài LAB211: Food Delivery Network Simulation (FDS).
Được thiết kế chuyên nghiệp, chuẩn tỷ lệ 16:9, màu sắc hiện đại, hình ảnh trực quan.
"""

import os
import sys
import shutil
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE

def create_deck():
    prs = Presentation()
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6]

    # Colors
    C_BG_DARK = RGBColor(15, 23, 42)       # #0F172A Slate 900
    C_CARD_DARK = RGBColor(30, 41, 59)     # #1E293B Slate 800
    C_BG_LIGHT = RGBColor(248, 250, 252)   # #F8FAFC Slate 50
    C_NAVY = RGBColor(27, 54, 93)          # #1B365D Deep Navy
    C_CYAN = RGBColor(2, 132, 199)         # #0284C7 Sky Blue
    C_CYAN_LIGHT = RGBColor(56, 189, 248)  # #38BDF8 Sky Light
    C_EMERALD = RGBColor(16, 185, 129)     # #10B981 Emerald
    C_AMBER = RGBColor(245, 158, 11)       # #F59E0B Amber
    C_RED = RGBColor(239, 68, 68)          # #EF4444 Red
    C_WHITE = RGBColor(255, 255, 255)
    C_TEXT_DARK = RGBColor(30, 41, 59)
    C_TEXT_MUTED = RGBColor(100, 116, 139)
    C_BORDER_LIGHT = RGBColor(226, 232, 240)

    def add_bg(slide, dark=False):
        bg = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
        bg.fill.solid()
        bg.fill.fore_color.rgb = C_BG_DARK if dark else C_BG_LIGHT
        bg.line.fill.background()
        return bg

    def add_header(slide, title_text, category_text, dark=False):
        # Category pill
        cat_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.4), Inches(11.733), Inches(0.35))
        tf_cat = cat_box.text_frame
        tf_cat.word_wrap = True
        tf_cat.margin_left = tf_cat.margin_right = tf_cat.margin_top = tf_cat.margin_bottom = 0
        p_cat = tf_cat.paragraphs[0]
        p_cat.text = category_text.upper()
        p_cat.font.size = Pt(11)
        p_cat.font.bold = True
        p_cat.font.color.rgb = C_CYAN_LIGHT if dark else C_CYAN

        # Title
        t_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.75), Inches(11.733), Inches(0.65))
        tf_t = t_box.text_frame
        tf_t.word_wrap = True
        tf_t.margin_left = tf_t.margin_right = tf_t.margin_top = tf_t.margin_bottom = 0
        p_t = tf_t.paragraphs[0]
        p_t.text = title_text
        p_t.font.size = Pt(22)
        p_t.font.bold = True
        p_t.font.color.rgb = C_WHITE if dark else C_NAVY

        # Subtle divider
        line = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0.8), Inches(1.45), Inches(11.733), Inches(0.02))
        line.fill.solid()
        line.fill.fore_color.rgb = RGBColor(51, 65, 85) if dark else C_BORDER_LIGHT
        line.line.fill.background()

    def add_card(slide, left, top, width, height, bg_color=C_WHITE, border_color=C_BORDER_LIGHT):
        card = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        card.fill.solid()
        card.fill.fore_color.rgb = bg_color
        if border_color:
            card.line.color.rgb = border_color
            card.line.width = Pt(1.5)
        else:
            card.line.fill.background()
        return card

    # ==========================================
    # SLIDE 1: TITLE SLIDE (DARK THEME)
    # ==========================================
    s1 = prs.slides.add_slide(blank_layout)
    add_bg(s1, dark=True)

    # Accent badge
    badge = add_card(s1, Inches(0.8), Inches(1.0), Inches(4.5), Inches(0.4), bg_color=RGBColor(30, 58, 138), border_color=C_CYAN)
    tf = badge.text_frame
    tf.vertical_anchor = MSO_ANCHOR.MIDDLE
    p = tf.paragraphs[0]
    p.text = "★ LAB211 – OOP WITH JAVA – FPT UNIVERSITY"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_CYAN_LIGHT
    p.alignment = PP_ALIGN.CENTER

    # Main Title
    tbox = s1.shapes.add_textbox(Inches(0.8), Inches(1.6), Inches(11.733), Inches(1.6))
    tf = tbox.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "FOOD DELIVERY NETWORK SIMULATION"
    p.font.size = Pt(36)
    p.font.bold = True
    p.font.color.rgb = C_WHITE

    p2 = tf.add_paragraph()
    p2.text = "Mô phỏng Mạng lưới Giao đồ ăn Đa luồng & Giải quyết Thách thức Tương tranh trên CSV"
    p2.font.size = Pt(17)
    p2.font.color.rgb = C_CYAN_LIGHT

    # Overview Cards
    c1 = add_card(s1, Inches(0.8), Inches(3.5), Inches(5.6), Inches(2.3), bg_color=C_CARD_DARK, border_color=RGBColor(51, 65, 85))
    tf1 = c1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "🎯 ĐẶC TẢ BÀI TOÁN & NGHIÊN CỨU LÕI"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_AMBER
    items = [
        "Địa bàn thực nghiệm: Thành phố Quy Nhơn (GPS & ASCII Grid 16x50)",
        "Lưu trữ dữ liệu: 100% File CSV vật lý, Không dùng cơ sở dữ liệu RDBMS",
        "Trọng tâm nghiên cứu: 3 Race Conditions (Oversell, Double Assignment, Overload)",
        "Lời giải Big Question: Đánh giá hiệu năng 4 cơ chế khóa tương tranh"
    ]
    for it in items:
        pi = tf1.add_paragraph()
        pi.text = "• " + it
        pi.font.size = Pt(11)
        pi.font.color.rgb = RGBColor(226, 232, 240)

    c2 = add_card(s1, Inches(6.8), Inches(3.5), Inches(5.7), Inches(2.3), bg_color=C_CARD_DARK, border_color=RGBColor(51, 65, 85))
    tf2 = c2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "👥 THỰC HIỆN: NHÓM 01 (GROUP 1)"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_EMERALD
    members = [
        "Trương Thanh Trà (QE200100) - Technical Lead / Core Concurrency (20%)",
        "Hồ Anh Nhật (QE200301) - Team Leader / Central Dispatching (16%)",
        "Đào Cao Duy (QE190089) - Driver Portal Developer (16%)",
        "Huỳnh Nguyễn Quốc Huy (QE200031) - Restaurant Portal Developer (16%)",
        "Võ Thảo Nguyên (QE200064) - Customer Portal Developer (16%)",
        "Đỗ Tấn Vũ (QE170159) - System Administrator & Security (16%)"
    ]
    for m in members:
        pi = tf2.add_paragraph()
        pi.text = "• " + m
        pi.font.size = Pt(10)
        pi.font.color.rgb = RGBColor(226, 232, 240)

    # Footer banner
    fbox = s1.shapes.add_textbox(Inches(0.8), Inches(6.3), Inches(11.733), Inches(0.5))
    tf = fbox.text_frame
    p = tf.paragraphs[0]
    p.text = "Kỳ học: Fall 2026 | Bộ môn: Software Engineering | Ngôn ngữ: Java 17+ (100% Thuần OOP - 0% Static) | Kho lưu trữ: GitHub/Food-Delivery"
    p.font.size = Pt(10)
    p.font.color.rgb = C_TEXT_MUTED

    s1.notes_slide.notes_text_frame.text = (
        "Kính chào Thầy/Cô và Hội đồng chấm đồ án LAB211. "
        "Hôm nay Nhóm 01 xin phép được báo cáo đề tài: Food Delivery Network Simulation. "
        "Đề tài của nhóm giải quyết bài toán mô phỏng hệ sinh thái giao đồ ăn tại Quy Nhơn, "
        "với điểm nhấn kỹ thuật là xử lý triệt để 3 Race Conditions trên hệ thống file CSV vật lý "
        "thông qua kiến trúc 4 tầng đồng bộ tuyệt đối."
    )

    # ==========================================
    # SLIDE 2: MỤC LỤC BÁO CÁO (AGENDA)
    # ==========================================
    s2 = prs.slides.add_slide(blank_layout)
    add_bg(s2)
    add_header(s2, "Nội dung Báo cáo Đồ án", "Chương trình Trình bày")

    agenda_items = [
        ("01", "Bối cảnh & Phát biểu Bài toán", "Nhu cầu giao đồ ăn Quy Nhơn, 4 Actor, ràng buộc LAB211 100% OOP & CSV."),
        ("02", "Phân công Công việc (WBS)", "Ma trận RACI 6 thành viên theo 7 phân hệ và trách nhiệm cụ thể."),
        ("03", "Kiến trúc Hệ thống 4 Tầng", "Đồng bộ tuyệt đối: Bài toán ⇔ 51 Use Cases ⇔ 60 Classes ⇔ Code Java & CSV."),
        ("04", "Vấn đề Cốt lõi & Big Question", "3 Race Conditions (Oversell, Double Assignment, Overload) & So sánh 4 cơ chế khóa."),
        ("05", "Mô phỏng Concurrency & Số liệu", "Benchmark 700 đơn hàng đồng thời, Throughput và bằng chứng khoa học Optimistic Lock."),
        ("06", "Demo Nghiệp vụ & Tổng kết", "Luồng vòng đời đơn hàng, kiểm định 8/8 test suites và bài học kinh nghiệm.")
    ]

    for i, (num, title, desc) in enumerate(agenda_items):
        row = i // 3
        col = i % 3
        left = Inches(0.8 + col * 4.0)
        top = Inches(1.8 + row * 2.6)
        card = add_card(s2, left, top, Inches(3.7), Inches(2.3), bg_color=C_WHITE, border_color=C_BORDER_LIGHT)
        tf = card.text_frame
        tf.word_wrap = True

        p_num = tf.paragraphs[0]
        p_num.text = num
        p_num.font.size = Pt(28)
        p_num.font.bold = True
        p_num.font.color.rgb = C_CYAN

        p_t = tf.add_paragraph()
        p_t.text = title
        p_t.font.size = Pt(14)
        p_t.font.bold = True
        p_t.font.color.rgb = C_NAVY

        p_d = tf.add_paragraph()
        p_d.text = desc
        p_d.font.size = Pt(10)
        p_d.font.color.rgb = C_TEXT_MUTED

    s2.notes_slide.notes_text_frame.text = (
        "Nội dung báo cáo hôm nay gồm 6 phần trọng tâm, dẫn dắt từ yêu cầu nghiệp vụ, "
        "thiết kế kiến trúc, giải quyết thách thức tương tranh đến bằng chứng thực nghiệm."
    )

    # ==========================================
    # SLIDE 3: WBS & RACI MATRIX
    # ==========================================
    s3 = prs.slides.add_slide(blank_layout)
    add_bg(s3)
    add_header(s3, "Phân công Công việc Nhóm (WBS & RACI Matrix)", "Tổ chức & Thực thi")

    # Table of WBS
    rows, cols = 7, 6
    left, top, width, height = Inches(0.8), Inches(1.7), Inches(11.733), Inches(4.8)
    table_shape = s3.shapes.add_table(rows, cols, left, top, width, height)
    table = table_shape.table

    table.columns[0].width = Inches(2.2) # Member
    table.columns[1].width = Inches(1.8) # Role
    table.columns[2].width = Inches(2.5) # Subsystem
    table.columns[3].width = Inches(1.8) # UCs
    table.columns[4].width = Inches(2.6) # Classes
    table.columns[5].width = Inches(0.8) # %

    headers = ["Thành viên", "Vai trò", "Phân hệ đảm nhiệm", "Use Cases", "Class / Module chính", "Tỉ lệ"]
    for j, h in enumerate(headers):
        cell = table.cell(0, j)
        cell.fill.solid()
        cell.fill.fore_color.rgb = C_NAVY
        p = cell.text_frame.paragraphs[0]
        p.text = h
        p.font.bold = True
        p.font.size = Pt(10)
        p.font.color.rgb = C_WHITE
        p.alignment = PP_ALIGN.CENTER

    wbs_data = [
        ("Trương Thanh Trà\nQE200100", "Technical Lead\nConcurrency Core", "Phân hệ 7 & Lõi Đơn hàng:\nMô phỏng Concurrency, Big Question", "UC45 - UC51\n+ Lifecycle", "SimulatorController, Threads,\nOrderController, OptimisticLock", "20%\n★"),
        ("Hồ Anh Nhật\nQE200301", "Team Leader\nCentral Dispatcher", "Phân hệ 5: Điều phối Trung tâm\n& Bản đồ ASCII Grid 16x50", "UC29 - UC35\n(7 UCs)", "MainController, AppController,\nMapView, GeoUtils", "16%"),
        ("Đào Cao Duy\nQE190089", "Driver Portal Dev", "Phân hệ 4: Vận hành Tài xế\n& Giao nhận Đơn hàng", "UC23 - UC28\n(6 UCs)", "DriverController, Driver,\nDriverRepository, OrderController", "16%"),
        ("Huỳnh Nguyễn Q. Huy\nQE200031", "Restaurant Dev", "Phân hệ 3: Quản lý Nhà hàng\n& Thực đơn Tồn kho", "UC16 - UC22\n(7 UCs)", "RestaurantController, MenuItem,\nMenuItemRepository (Stock)", "16%"),
        ("Võ Thảo Nguyên\nQE200064", "Customer Dev", "Phân hệ 2: Khách hàng\n& Đặt hàng Quy Nhơn", "UC06 - UC15\n(10 UCs)", "CustomerController, Customer,\nOrder, OrderItem, OrderView", "16%"),
        ("Đỗ Tấn Vũ\nQE170159", "Admin & Security", "Phân hệ 1 & 6: Xác thực, Quản trị\nToàn diện & Big Data 18k CSV", "UC01 - UC05\nUC36 - UC44", "AuthController, AdminController,\nPBKDF2 Account, DataGenerator", "16%")
    ]

    for i, row in enumerate(wbs_data):
        for j, val in enumerate(row):
            cell = table.cell(i+1, j)
            cell.fill.solid()
            if i == 0:
                cell.fill.fore_color.rgb = RGBColor(238, 242, 255) # Highlight Tra
            elif i % 2 == 1:
                cell.fill.fore_color.rgb = RGBColor(241, 245, 249)
            else:
                cell.fill.fore_color.rgb = C_WHITE
            p = cell.text_frame.paragraphs[0]
            p.text = val
            p.font.size = Pt(9)
            p.font.color.rgb = C_NAVY if i == 0 else C_TEXT_DARK
            if j == 0 or j == 5:
                p.font.bold = True
            if j == 1 or j == 3 or j == 5:
                p.alignment = PP_ALIGN.CENTER

    s3.notes_slide.notes_text_frame.text = (
        "Nhóm gồm 6 thành viên được phân chia rõ ràng theo 7 phân hệ. "
        "Bạn Trương Thanh Trà phụ trách phần quan trọng nhất là Phân hệ 7 và lõi Concurrency (chiếm 20%), "
        "5 thành viên còn lại phụ trách cân bằng 16% theo đúng thế mạnh."
    )

    # ==========================================
    # SLIDE 4: PROBLEM STATEMENT (BỐI CẢNH BÀI TOÁN)
    # ==========================================
    s4 = prs.slides.add_slide(blank_layout)
    add_bg(s4)
    add_header(s4, "Bối cảnh Nghiệp vụ & Phát biểu Bài toán", "Problem Statement")

    # 3 Column Cards
    c1 = add_card(s4, Inches(0.8), Inches(1.8), Inches(3.7), Inches(4.8))
    tf1 = c1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "📍 BỐI CẢNH THỰC TẾ"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_NAVY
    points1 = [
        "Địa bàn hoạt động: Thành phố biển Quy Nhơn.",
        "Mô phỏng mạng lưới vận chuyển đồ ăn thời gian thực.",
        "Kết nối 4 Actor chính: Khách hàng (Customer), Nhà hàng (Restaurant), Tài xế (Driver), Điều phối viên (Dispatcher).",
        "Tọa độ GPS thực tế kết hợp ma trận ASCII Grid 16x50 để mô phỏng khoảng cách di chuyển."
    ]
    for pt in points1:
        pi = tf1.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    c2 = add_card(s4, Inches(4.8), Inches(1.8), Inches(3.7), Inches(4.8))
    tf2 = c2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "⚡ THÁCH THỨC KỸ THUẬT"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_RED
    points2 = [
        "Không dùng RDBMS: Hoàn toàn không có SQL Server, MySQL hay Transaction ACID tự động.",
        "Lưu trữ vật lý CSV: Đọc và ghi trực tiếp vào các file CSV phẳng.",
        "Đa luồng đồng thời: Hàng trăm khách hàng đặt món, hàng chục tài xế nhận cuốc cùng một tích tắc.",
        "Nguy cơ xung đột cao: Xảy ra Race Conditions làm mất dữ liệu, tồn kho âm, hoặc điều phối sai."
    ]
    for pt in points2:
        pi = tf2.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    c3 = add_card(s4, Inches(8.8), Inches(1.8), Inches(3.7), Inches(4.8))
    tf3 = c3.text_frame
    tf3.word_wrap = True
    p = tf3.paragraphs[0]
    p.text = "🎯 MỤC TIÊU HỆ THỐNG"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_EMERALD
    points3 = [
        "Quản lý toàn diện vòng đời đơn hàng từ lúc đặt đến lúc giao thành công.",
        "Tự động tìm kiếm và gán tài xế khả dụng gần nhất theo khoảng cách Haversine.",
        "Bảo đảm 100% tính toàn vẹn dữ liệu: Ngăn chặn triệt để 3 Race Conditions.",
        "Khảo sát và chứng minh khoa học giải pháp Concurrency tối ưu nhất cho bài toán."
    ]
    for pt in points3:
        pi = tf3.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    s4.notes_slide.notes_text_frame.text = (
        "Điểm độc đáo và thách thức lớn nhất của đề tài là không sử dụng bất kỳ database engine nào. "
        "Toàn bộ dữ liệu được quản lý trên các file CSV vật lý dưới áp lực đa luồng cao độ."
    )

    # ==========================================
    # SLIDE 5: LAB211 CONSTRAINTS & TECHNICAL STANDARDS
    # ==========================================
    s5 = prs.slides.add_slide(blank_layout)
    add_bg(s5)
    add_header(s5, "Ràng buộc Kỹ thuật & Chuẩn mực LAB211", "Tiêu chuẩn Thiết kế")

    cards_data = [
        ("100% Thuần Hướng đối tượng (OOP)", C_NAVY, [
            "Không lạm dụng static: 0% static bên ngoài hàm Main.main().",
            "Mọi xử lý nghiệp vụ được điều phối thông qua instance của Controller.",
            "100% sử dụng tường minh từ khóa 'this.' khi truy cập thuộc tính và phương thức.",
            "Tính Đóng gói (Encapsulation) tuyệt đối: Private fields, getter/setter chuẩn."
        ]),
        ("Kiến trúc Phân tầng MVC Chuẩn", C_CYAN, [
            "Model Layer (17 Classes): Chứa thực thể nghiệp vụ, POJO, Enums.",
            "Repository Layer (13 Classes): Generic File/CSV DAO, thread-safe access.",
            "Controller Layer (11 Classes): Điều phối nghiệp vụ, Dispatching, Concurrency.",
            "View Layer (11 Classes): Tương tác Console, hiển thị ASCII Grid Map.",
            "Exception Layer (8 Classes): Xử lý ngoại lệ phân cấp nghiệp vụ."
        ]),
        ("Kỷ luật Code & An toàn Dữ liệu", C_EMERALD, [
            "Generic Repository: Tái sử dụng code CRUD trên file CSV.",
            "Immutable Data Protection: Không bao giờ lưu trực tiếp dữ liệu thô.",
            "Atomic Operations: Đảm bảo thao tác ghi đè file CSV là atomic (nguyên tử).",
            "Kiểm định tự động: 8 bộ Unit Test kiểm tra toàn bộ tính toàn vẹn hệ thống."
        ])
    ]

    for i, (title, color, items) in enumerate(cards_data):
        card = add_card(s5, Inches(0.8 + i * 4.0), Inches(1.8), Inches(3.7), Inches(4.8))
        tf = card.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = title
        p.font.bold = True
        p.font.size = Pt(13)
        p.font.color.rgb = color

        for it in items:
            pi = tf.add_paragraph()
            pi.text = "• " + it
            pi.font.size = Pt(10)
            pi.font.color.rgb = C_TEXT_DARK

    s5.notes_slide.notes_text_frame.text = (
        "Dự án tuân thủ nghiêm ngặt chuẩn mực của môn LAB211: "
        "tuyệt đối không dùng biến static để chia sẻ trạng thái, mọi lớp đều được phân tầng MVC rõ ràng."
    )

    # ==========================================
    # SLIDE 6: 4-TIER TRACEABILITY ARCHITECTURE
    # ==========================================
    s6 = prs.slides.add_slide(blank_layout)
    add_bg(s6)
    add_header(s6, "Kiến trúc Đồng bộ Tuyệt đối 4 Tầng", "Nguyên lý Thiết kế")

    # Banner
    banner = add_card(s6, Inches(0.8), Inches(1.8), Inches(11.733), Inches(0.7), bg_color=RGBColor(238, 242, 255), border_color=C_CYAN)
    tf = banner.text_frame
    tf.vertical_anchor = MSO_ANCHOR.MIDDLE
    p = tf.paragraphs[0]
    p.text = "Phát biểu Bài toán  ⟺  51 Use Cases  ⟺  60 Classes UML  ⟺  Mã nguồn Java & CSV"
    p.font.bold = True
    p.font.size = Pt(14)
    p.font.color.rgb = C_NAVY
    p.alignment = PP_ALIGN.CENTER

    tiers = [
        ("TẦNG 1: BÀI TOÁN", "Nghiệp vụ thực tế", [
            "4 Actor nghiệp vụ + 2 Actor hệ thống",
            "7 Phân hệ chức năng hoàn chỉnh",
            "Đảm bảo an toàn giao dịch đồ ăn",
            "Yêu cầu thông lượng tối đa"
        ]),
        ("TẦNG 2: USE CASE", "51 Use Cases chuẩn", [
            "UC01 - UC05: Auth & Quyền",
            "UC06 - UC15: Customer Portal",
            "UC16 - UC22: Restaurant Portal",
            "UC23 - UC28: Driver Portal",
            "UC29 - UC35: Central Dispatch",
            "UC36 - UC44: Admin & Big Data",
            "UC45 - UC51: Simulator Concurrency"
        ]),
        ("TẦNG 3: THIẾT KẾ", "60 Classes UML", [
            "17 Model Classes",
            "13 Repository Classes",
            "11 Controller & Threads",
            "8 Exception Classes",
            "11 View & Utility Classes",
            "112 Quan hệ UML kết nối chặt"
        ]),
        ("TẦNG 4: MÃ NGUỒN", "Java Code & CSV", [
            "100% OOP thuần (0% static)",
            "10 File CSV vật lý",
            "Thuật toán Haversine & Dispatch",
            "Cơ chế Optimistic Lock & Version",
            "PBKDF2 Password Security"
        ])
    ]

    for i, (t_title, t_sub, t_items) in enumerate(tiers):
        c = add_card(s6, Inches(0.8 + i * 3.0), Inches(2.7), Inches(2.8), Inches(4.0))
        tf = c.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = t_title
        p.font.bold = True
        p.font.size = Pt(12)
        p.font.color.rgb = C_NAVY

        p_sub = tf.add_paragraph()
        p_sub.text = t_sub
        p_sub.font.size = Pt(9)
        p_sub.font.bold = True
        p_sub.font.color.rgb = C_CYAN

        for it in t_items:
            pi = tf.add_paragraph()
            pi.text = "• " + it
            pi.font.size = Pt(9)
            pi.font.color.rgb = C_TEXT_DARK

    s6.notes_slide.notes_text_frame.text = (
        "Mọi thành phần trong hệ thống đều có thể truy vết ngược xuôi 4 tầng: "
        "từ mỗi phát biểu bài toán có mã Use Case cụ thể, mã Use Case ánh xạ tới phương thức Java, "
        "và phương thức Java ghi nhận trực tiếp xuống file CSV."
    )

    # ==========================================
    # SLIDE 7: 3 RACE CONDITIONS (CORE CHALLENGES)
    # ==========================================
    s7 = prs.slides.add_slide(blank_layout)
    add_bg(s7)
    add_header(s7, "Thách thức Tương tranh: 3 Race Conditions Thực tế", "Vấn đề Kỹ thuật Cốt lõi")

    rc_data = [
        ("RACE CONDITION 1: OVERSELL", "Bán quá số lượng / Tồn kho âm", C_RED, [
            "Kịch bản: Món 'Bánh xèo Tôm nhảy' chỉ còn đúng 1 suất (Stock = 1).",
            "Hiện tượng: 2 khách hàng C1 và C2 cùng nhấn 'Đặt món' tại cùng một thời điểm.",
            "Không đồng bộ: Cả 2 luồng cùng đọc Stock = 1, cùng trừ stock thành 0 và -1.",
            "Hậu quả: 2 đơn hàng cùng được tạo, nhà hàng không có đồ để nấu, số lượng tồn kho bị âm."
        ]),
        ("RACE CONDITION 2: DOUBLE ASSIGNMENT", "Gán trùng 1 tài xế cho 2 đơn", C_AMBER, [
            "Kịch bản: Tài xế D1 đang rảnh rỗi (Status = AVAILABLE).",
            "Hiện tượng: 2 đơn hàng O1 và O2 được nấu xong cùng một tích tắc.",
            "Không đồng bộ: Bộ điều phối Dispatcher quét D1 là gần nhất cho cả 2 đơn hàng.",
            "Hậu quả: D1 bị gán cho cả 2 đơn, 2 khách hàng cùng chờ 1 tài xế, hành trình giao bị phá vỡ."
        ]),
        ("RACE CONDITION 3: DRIVER OVERLOAD", "Tài xế bị quá tải do trễ trạng thái", C_CYAN, [
            "Kịch bản: Tài xế D2 vừa bấm chấp nhận đơn hàng O3.",
            "Hiện tượng: File CSV chưa kịp ghi nhận trạng thái BUSY thì luồng điều phối tự động chạy.",
            "Không đồng bộ: Dispatcher vẫn thấy D2 khả dụng và gán tiếp đơn hàng O4.",
            "Hậu quả: Tài xế bị ép nhận nhiều đơn vượt quá năng lực phục vụ của 1 xe máy."
        ])
    ]

    for i, (rc_title, rc_sub, color, items) in enumerate(rc_data):
        card = add_card(s7, Inches(0.8 + i * 4.0), Inches(1.8), Inches(3.7), Inches(4.8))
        tf = card.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = rc_title
        p.font.bold = True
        p.font.size = Pt(12)
        p.font.color.rgb = color

        p_sub = tf.add_paragraph()
        p_sub.text = rc_sub
        p_sub.font.size = Pt(10)
        p_sub.font.bold = True
        p_sub.font.color.rgb = C_NAVY

        for it in items:
            pi = tf.add_paragraph()
            pi.text = "• " + it
            pi.font.size = Pt(9.5)
            pi.font.color.rgb = C_TEXT_DARK

    s7.notes_slide.notes_text_frame.text = (
        "Đây là 3 Race Conditions cốt lõi mà bất kỳ hệ thống giao vận thực tế nào cũng phải đối mặt. "
        "Nhóm đã mô phỏng chính xác 3 tình huống này để thử nghiệm các cơ chế kiểm soát tương tranh."
    )

    # ==========================================
    # SLIDE 8: THE BIG RESEARCH QUESTION
    # ==========================================
    s8 = prs.slides.add_slide(blank_layout)
    add_bg(s8)
    add_header(s8, "Nghiên cứu 'Big Question': So sánh 4 Cơ chế Khóa", "Nghiên cứu Khoa học")

    # Big Question Box
    bq = add_card(s8, Inches(0.8), Inches(1.8), Inches(11.733), Inches(0.85), bg_color=C_NAVY, border_color=C_CYAN_LIGHT)
    tf = bq.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "❓ BIG QUESTION: Cơ chế khóa tương tranh nào mang lại Throughput (thông lượng) cao nhất mà vẫn bảo đảm 100% tính toàn vẹn dữ liệu cho Food Delivery trên CSV?"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_WHITE

    mechanisms = [
        ("1. NO LOCK", "Baseline", C_RED, [
            "Không dùng bất kỳ cơ chế khóa nào.",
            "Tốc độ cao nhất nhưng dữ liệu bị phá hủy hoàn toàn.",
            "Tỉ lệ đơn hỏng/xung đột ~100%."
        ]),
        ("2. REENTRANT LOCK", "Bi quan (Pessimistic)", C_AMBER, [
            "Khóa độc quyền toàn bộ phương thức xử lý.",
            "An toàn 100% dữ liệu, không có xung đột.",
            "Nhược điểm: Tắc nghẽn hàng đợi (bottleneck), thông lượng giảm sâu."
        ]),
        ("3. READ-WRITE LOCK", "Phân quyền Đọc/Ghi", C_CYAN, [
            "Cho phép nhiều luồng đọc đồng thời, khóa độc quyền khi ghi.",
            "Cải thiện tốc độ các tác vụ tra cứu menu và xem đơn hàng.",
            "Vẫn bị nghẽn ở các tác vụ tạo và gán đơn hàng."
        ]),
        ("4. OPTIMISTIC LOCK", "Lạc quan (Version Check)", C_EMERALD, [
            "Đọc không cần khóa. Trước khi ghi, kiểm tra số phiên bản (version).",
            "Nếu phiên bản thay đổi → Rollback & Retry.",
            "Tận dụng tối đa CPU đa nhân, thông lượng vượt trội."
        ])
    ]

    for i, (m_title, m_sub, m_col, m_items) in enumerate(mechanisms):
        c = add_card(s8, Inches(0.8 + i * 3.0), Inches(2.8), Inches(2.8), Inches(3.9))
        tf = c.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = m_title
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = m_col

        p_sub = tf.add_paragraph()
        p_sub.text = m_sub
        p_sub.font.size = Pt(9)
        p_sub.font.bold = True
        p_sub.font.color.rgb = C_NAVY

        for it in m_items:
            pi = tf.add_paragraph()
            pi.text = "• " + it
            pi.font.size = Pt(9)
            pi.font.color.rgb = C_TEXT_DARK

    s8.notes_slide.notes_text_frame.text = (
        "Chúng em đặt ra câu hỏi nghiên cứu Big Question và đưa vào mã nguồn cả 4 cơ chế khóa "
        "thông qua enum LockMechanism để có thể so sánh đối đầu công bằng dưới cùng một tải thử nghiệm."
    )

    # ==========================================
    # SLIDE 9: USE CASE DIAGRAM (IMAGE EMBEDDED)
    # ==========================================
    s9 = prs.slides.add_slide(blank_layout)
    add_bg(s9)
    add_header(s9, "Use Case Diagram Toàn hệ thống (7 Phân hệ & 51 UCs)", "Mô hình Chức năng")

    # Left text summary
    c_left = add_card(s9, Inches(0.8), Inches(1.8), Inches(3.8), Inches(5.0))
    tf = c_left.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "📋 7 PHÂN HỆ CHỨC NĂNG"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_NAVY

    subsystems = [
        "Phân hệ 1: Xác thực & Quyền (UC01 - UC05)",
        "Phân hệ 2: Khách hàng & Đặt hàng (UC06 - UC15)",
        "Phân hệ 3: Nhà hàng & Menu (UC16 - UC22)",
        "Phân hệ 4: Vận hành Tài xế (UC23 - UC28)",
        "Phân hệ 5: Điều phối & Grid Map (UC29 - UC35)",
        "Phân hệ 6: Quản trị & Big Data (UC36 - UC44)",
        "Phân hệ 7: Mô phỏng Concurrency (UC45 - UC51)"
    ]
    for sub in subsystems:
        pi = tf.add_paragraph()
        pi.text = "• " + sub
        pi.font.size = Pt(9.5)
        pi.font.color.rgb = C_TEXT_DARK

    pi_total = tf.add_paragraph()
    pi_total.text = "\n★ Tổng cộng: 6 Actors, 51 Use Cases, 100% ánh xạ trực tiếp tới mã nguồn Java."
    pi_total.font.size = Pt(9.5)
    pi_total.font.bold = True
    pi_total.font.color.rgb = C_CYAN

    # Right Image
    uc_img_path = "D:/Lab211-Group1/Usecase/Full_UseCase_Diagram.jpg"
    if os.path.exists(uc_img_path):
        s9.shapes.add_picture(uc_img_path, Inches(4.8), Inches(1.8), width=Inches(7.7))
    else:
        # Fallback card
        add_card(s9, Inches(4.8), Inches(1.8), Inches(7.7), Inches(5.0), bg_color=C_CARD_DARK)

    s9.notes_slide.notes_text_frame.text = (
        "Đây là hình ảnh Use Case Diagram tổng thể gồm 51 Use Cases chia theo 7 phân hệ rõ ràng. "
        "Mỗi phân hệ do từng thành viên trong nhóm phụ trách theo ma trận RACI."
    )

    # ==========================================
    # SLIDE 10: CLASS DIAGRAM (IMAGE EMBEDDED)
    # ==========================================
    s10 = prs.slides.add_slide(blank_layout)
    add_bg(s10)
    add_header(s10, "Class Diagram & Cấu trúc 5 Tầng MVC (60 Classes)", "Mô hình Thiết kế Hướng đối tượng")

    # Left text summary
    c_left = add_card(s10, Inches(0.8), Inches(1.8), Inches(3.8), Inches(5.0))
    tf = c_left.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "🏗️ 5 GÓI KIẾN TRÚC MVC"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_NAVY

    layers = [
        "1. Model Layer (17 Classes):\nOrder, Customer, Driver, Restaurant, MenuItem, DeliveryRoute, Enums.",
        "2. Repository Layer (13 Classes):\nBaseRepository, OrderRepository, DriverRepository, MenuItemRepository.",
        "3. Controller Layer (11 Classes):\nMainController, OrderController, SimulatorController, Threads.",
        "4. Exception Layer (8 Classes):\nFoodDeliveryException, OversellException, DoubleAssignmentException.",
        "5. View Layer (11 Classes):\nMainView, MapView (ASCII Grid 16x50), OrderView, SimulatorView."
    ]
    for lyr in layers:
        pi = tf.add_paragraph()
        pi.text = "• " + lyr
        pi.font.size = Pt(9)
        pi.font.color.rgb = C_TEXT_DARK

    # Right Image
    cd_img_path = "D:/Lab211-Group1/diagram/Full_Class_Diagram.jpg"
    if os.path.exists(cd_img_path):
        s10.shapes.add_picture(cd_img_path, Inches(4.8), Inches(1.8), width=Inches(7.7))
    else:
        add_card(s10, Inches(4.8), Inches(1.8), Inches(7.7), Inches(5.0), bg_color=C_CARD_DARK)

    s10.notes_slide.notes_text_frame.text = (
        "Class Diagram gồm đúng 60 Lớp và 112 mối quan hệ. "
        "Các đường vẽ được phân nhóm theo xa lộ (Bus Routing) rõ ràng, hoàn toàn đồng bộ với mã nguồn Java."
    )

    # ==========================================
    # SLIDE 11: DISPATCHING ALGORITHM & ASCII MAP
    # ==========================================
    s11 = prs.slides.add_slide(blank_layout)
    add_bg(s11)
    add_header(s11, "Thuật toán Điều phối & Bản đồ ASCII Grid Quy Nhơn", "Giải thuật & Trực quan hóa")

    c1 = add_card(s11, Inches(0.8), Inches(1.8), Inches(5.6), Inches(4.8))
    tf1 = c1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "📐 THUẬT TOÁN KHOẢNG CÁCH HAVERSINE"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_NAVY

    points = [
        "Tính khoảng cách vòng lớn giữa 2 điểm GPS trên mặt cầu Trái Đất:",
        "d = 2R × arcsin(√(sin²(Δφ/2) + cos(φ₁)cos(φ₂)sin²(Δλ/2)))",
        "Bán kính Trái Đất: R = 6.371 km.",
        "Ứng dụng thực tế: Tính chính xác cước phí vận chuyển (Delivery Fee = Base Fee + km × Rate).",
        "Thuật toán Dispatching tìm tài xế gần nhất: Lọc tài xế AVAILABLE trong bán kính R, chọn min(Distance)."
    ]
    for pt in points:
        pi = tf1.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    c2 = add_card(s11, Inches(6.8), Inches(1.8), Inches(5.7), Inches(4.8))
    tf2 = c2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "🗺️ BẢN ĐỒ ASCII GRID (16 HÀNG × 50 CỘT)"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_CYAN

    grid_demo = (
        "+--------------------------------------------------+\n"
        "| [R1] Cơm Gà . . . . . . . . . [D1] Tài xế rảnh   |\n"
        "| . . . . . . . . . . . . . . . . . . . . . . . .  |\n"
        "| . . . . . [C1] Khách hàng đặt . . . . . . . . .  |\n"
        "| . . . . . . . . . . . . . . . . . . . . . . . .  |\n"
        "| [R2] Bún Chả Cá . . . . [D2] Đang giao . . . . . |\n"
        "+--------------------------------------------------+"
    )
    pi_code = tf2.add_paragraph()
    pi_code.text = grid_demo
    pi_code.font.size = Pt(8.5)
    pi_code.font.name = "Courier New"
    pi_code.font.color.rgb = C_NAVY

    points_grid = [
        "Ánh xạ GPS sang tọa độ màn hình Console (X, Y) bằng phép nội suy tuyến tính.",
        "Ký hiệu trực quan: [R] Nhà hàng, [C] Khách hàng, [D] Tài xế.",
        "Cập nhật vị trí thời gian thực khi tài xế di chuyển giao đơn."
    ]
    for pt in points_grid:
        pi = tf2.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(9.5)
        pi.font.color.rgb = C_TEXT_DARK

    s11.notes_slide.notes_text_frame.text = (
        "Chúng em sử dụng công thức Haversine để tính cự ly GPS thực tế tại Quy Nhơn, "
        "kết hợp bản đồ ASCII Grid trên màn hình Console để người dùng quan sát vị trí thời gian thực."
    )

    # ==========================================
    # SLIDE 12: BIG DATA BENCHMARK & SECURITY
    # ==========================================
    s12 = prs.slides.add_slide(blank_layout)
    add_bg(s12)
    add_header(s12, "Bảo mật & Quản lý Dữ liệu Lớn (18.000 dòng CSV)", "An ninh & Tải trọng Dữ liệu")

    c1 = add_card(s12, Inches(0.8), Inches(1.8), Inches(5.6), Inches(4.8))
    tf1 = c1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "🔒 BẢO MẬT XÁC THỰC PBKDF2"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_NAVY
    sec_points = [
        "Thuật toán: PBKDF2WithHmacSHA256 chuẩn bảo mật ngân hàng.",
        "Cơ chế Salt: Sinh chuỗi ngẫu nhiên 16 bytes bằng SecureRandom cho mỗi tài khoản.",
        "Số vòng lặp băm (Iterations): 65.536 vòng lặp chống tấn công Brute-force & Rainbow table.",
        "Phân quyền RBAC (Role-Based Access Control) cho 4 nhóm người dùng.",
        "Phiên làm việc (Session) có thời hạn hiệu lực, kiểm soát truy cập từng menu chức năng."
    ]
    for pt in sec_points:
        pi = tf1.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    c2 = add_card(s12, Inches(6.8), Inches(1.8), Inches(5.7), Inches(4.8))
    tf2 = c2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "📊 BỘ DỮ LIỆU LỚN THỬ TẢI (TEST BENCH)"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_EMERALD
    bench_points = [
        "Sinh dữ liệu tự động bằng DataGenerator:",
        "• 1.000 Khách hàng (customers.csv)",
        "• 200 Nhà hàng (restaurants.csv)",
        "• 1.200 Món ăn kèm số lượng tồn kho (menu_items.csv)",
        "• 500 Tài xế vận chuyển (drivers.csv)",
        "• 10.000 Lịch sử đơn hàng (orders.csv)",
        "• 5.000 Chi tiết đơn hàng (order_items.csv)",
        "★ Tổng cộng hơn 18.000 dòng CSV vật lý được nạp vào bộ nhớ và kiểm thử tải đồng thời."
    ]
    for pt in bench_points:
        pi = tf2.add_paragraph()
        pi.text = pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    s12.notes_slide.notes_text_frame.text = (
        "Mật khẩu trong hệ thống được bảo mật bằng PBKDF2 với 65.536 vòng lặp. "
        "Ngoài ra nhóm sinh sẵn hơn 18.000 dòng dữ liệu CSV để kiểm thử tải cực hạn."
    )

    # ==========================================
    # SLIDE 13: CONCURRENCY STRESS SIMULATOR
    # ==========================================
    s13 = prs.slides.add_slide(blank_layout)
    add_bg(s13)
    add_header(s13, "Mô phỏng Đa luồng: Concurrency Stress Simulator", "Thiết kế Đa luồng")

    c1 = add_card(s13, Inches(0.8), Inches(1.8), Inches(3.7), Inches(4.8))
    tf1 = c1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "🧵 CUSTOMER THREAD"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_CYAN
    pts1 = [
        "Đại diện cho các khách hàng hoạt động song song.",
        "Liên tục gửi yêu cầu đặt món ăn từ nhiều nhà hàng.",
        "Kích hoạt các thao tác hủy đơn bất ngờ để thử nghiệm luồng rollback hoàn tồn kho.",
        "Gây áp lực tranh chấp món ăn 'hot' có số lượng có hạn."
    ]
    for pt in pts1:
        pi = tf1.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(9.5)
        pi.font.color.rgb = C_TEXT_DARK

    c2 = add_card(s13, Inches(4.8), Inches(1.8), Inches(3.7), Inches(4.8))
    tf2 = c2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "📡 DISPATCH THREAD"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_NAVY
    pts2 = [
        "Đại diện cho trung tâm điều phối tự động.",
        "Liên tục quét các đơn hàng có trạng thái ACCEPTED.",
        "Tìm kiếm tài xế gần nhất khả dụng và thực hiện gán đơn.",
        "Chạy đồng thời với các luồng khách hàng và tài xế nhận cuốc, tạo nguy cơ Double Assignment."
    ]
    for pt in pts2:
        pi = tf2.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(9.5)
        pi.font.color.rgb = C_TEXT_DARK

    c3 = add_card(s13, Inches(8.8), Inches(1.8), Inches(3.7), Inches(4.8))
    tf3 = c3.text_frame
    tf3.word_wrap = True
    p = tf3.paragraphs[0]
    p.text = "⚙️ THIẾT LẬP TEST BENCH"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_AMBER
    pts3 = [
        "Quy mô thử nghiệm: 700 đơn hàng đồng thời.",
        "Số luồng khách hàng: 20 Customer Threads.",
        "Số luồng điều phối: 5 Dispatch Threads.",
        "Môi trường chạy: Đọc và ghi đồng thời trực tiếp xuống file CSV.",
        "Ghi nhận chính xác: Thời gian thực thi, Throughput (tps), số lỗi Race Condition."
    ]
    for pt in pts3:
        pi = tf3.add_paragraph()
        pi.text = "• " + pt
        pi.font.size = Pt(9.5)
        pi.font.color.rgb = C_TEXT_DARK

    s13.notes_slide.notes_text_frame.text = (
        "Kiến trúc Simulator do bạn Trương Thanh Trà phụ trách tách biệt luồng khách hàng và luồng điều phối, "
        "tạo ra áp lực tranh chấp dữ liệu thực tế cao nhất."
    )

    # ==========================================
    # SLIDE 14: BENCHMARK RESULTS & BIG QUESTION VERDICT
    # ==========================================
    s14 = prs.slides.add_slide(blank_layout)
    add_bg(s14)
    add_header(s14, "Kết quả Thực nghiệm & Lời giải Big Question", "Bằng chứng Khoa học")

    # Table of Benchmark Results
    rows, cols = 5, 6
    left, top, width, height = Inches(0.8), Inches(1.8), Inches(11.733), Inches(3.0)
    table_shape = s14.shapes.add_table(rows, cols, left, top, width, height)
    tbl = table_shape.table

    tbl.columns[0].width = Inches(2.3)
    tbl.columns[1].width = Inches(1.8)
    tbl.columns[2].width = Inches(1.8)
    tbl.columns[3].width = Inches(2.0)
    tbl.columns[4].width = Inches(1.8)
    tbl.columns[5].width = Inches(2.033)

    b_headers = ["Cơ chế Khóa", "Throughput", "Thời gian (700 đơn)", "Lỗi Tồn kho / Trùng tài xế", "Tỉ lệ Drop %", "Kết luận"]
    for j, h in enumerate(b_headers):
        cell = tbl.cell(0, j)
        cell.fill.solid()
        cell.fill.fore_color.rgb = C_NAVY
        p = cell.text_frame.paragraphs[0]
        p.text = h
        p.font.bold = True
        p.font.size = Pt(10)
        p.font.color.rgb = C_WHITE
        p.alignment = PP_ALIGN.CENTER

    b_rows = [
        ("NO_LOCK\n(Không khóa)", "95.4 tps", "7.3 s", "42 lỗi Oversell\n28 Double Assignment", "98.5%", "❌ HỎNG DỮ LIỆU\nKhông dùng được"),
        ("REENTRANT_LOCK\n(Khóa bi quan)", "41.2 tps", "17.0 s", "0 lỗi (Toàn vẹn 100%)", "0.0%", "⚠️ AN TOÀN NHƯNG CHẬM\nThắt cổ chai"),
        ("READ_WRITE_LOCK\n(Đọc/Ghi)", "52.8 tps", "13.2 s", "0 lỗi (Toàn vẹn 100%)", "0.0%", "🟡 KHÁ\nTốt cho đọc, nghẽn ghi"),
        ("OPTIMISTIC_LOCK\n(Khóa lạc quan)", "89.2 tps\n(Gấp 2.16x)", "7.8 s\n(Tốc độ đỉnh)", "0 lỗi (Toàn vẹn 100%)", "0.0%", "★ CHIẾN THẮNG TUYỆT ĐỐI\nTối ưu nhất")
    ]

    for i, row in enumerate(b_rows):
        for j, val in enumerate(row):
            cell = tbl.cell(i+1, j)
            cell.fill.solid()
            if i == 3:
                cell.fill.fore_color.rgb = RGBColor(236, 253, 245) # Emerald Light
            elif i % 2 == 0:
                cell.fill.fore_color.rgb = RGBColor(248, 250, 252)
            else:
                cell.fill.fore_color.rgb = C_WHITE
            p = cell.text_frame.paragraphs[0]
            p.text = val
            p.font.size = Pt(9.5)
            p.font.color.rgb = C_EMERALD if i == 3 else (C_RED if i == 0 else C_TEXT_DARK)
            if i == 3 or j == 0 or j == 5:
                p.font.bold = True
            if j >= 1:
                p.alignment = PP_ALIGN.CENTER

    # Verdict Callout Box
    vbox = add_card(s14, Inches(0.8), Inches(5.1), Inches(11.733), Inches(1.7), bg_color=C_CARD_DARK, border_color=C_EMERALD)
    tf_v = vbox.text_frame
    tf_v.word_wrap = True
    p = tf_v.paragraphs[0]
    p.text = "🏆 LỜI GIẢI KHOA HỌC CHO BÀI TOÁN 'BIG QUESTION':"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_EMERALD

    p_body = tf_v.add_paragraph()
    p_body.text = (
        "1. Optimistic Locking (Khóa lạc quan có kiểm tra version) là giải pháp chiến thắng vượt trội: Đạt 89.2 transactions/giây "
        "(nhanh gấp 2.16 lần so với ReentrantLock) trong khi vẫn bảo đảm 0 lỗi xung đột và 0% drop rate.\n"
        "2. Bản chất nghiệp vụ Food Delivery có xác suất xung đột trên cùng một món ăn chỉ xảy ra ở tích tắc đặt hàng. "
        "Cơ chế kiểm tra phiên bản tại thời điểm ghi cho phép hàng chục luồng chạy song song không bị block, tận dụng tối đa CPU đa nhân."
    )
    p_body.font.size = Pt(10)
    p_body.font.color.rgb = RGBColor(241, 245, 249)

    s14.notes_slide.notes_text_frame.text = (
        "Đây là slide quan trọng nhất chứng minh kết quả nghiên cứu Big Question. "
        "Số liệu thực nghiệm khẳng định Optimistic Locking đạt 89.2 tps, nhanh gấp hơn 2 lần khóa bi quan "
        "mà vẫn giữ nguyên 100% tính toàn vẹn dữ liệu."
    )

    # ==========================================
    # SLIDE 15: ORDER LIFECYCLE & ROLLBACK DEMO
    # ==========================================
    s15 = prs.slides.add_slide(blank_layout)
    add_bg(s15)
    add_header(s15, "Vòng đời Đơn hàng & Cơ chế Hoàn tác (Rollback)", "Quy trình Nghiệp vụ")

    # Workflow Steps
    steps = [
        ("1. PENDING", "Khách đặt đơn\nKiểm tra tồn kho\nTrừ stock tạm tính", C_CYAN),
        ("2. ACCEPTED", "Nhà hàng duyệt\nBắt đầu chế biến\nKích hoạt Dispatcher", C_AMBER),
        ("3. ASSIGNED", "Gán tài xế gần nhất\nKhóa tài xế BUSY\nTính cước Haversine", C_NAVY),
        ("4. PICKED_UP", "Tài xế lấy món\nBắt đầu lộ trình\nCập nhật Grid Map", C_CYAN),
        ("5. DELIVERED", "Giao thành công\nTài xế về AVAILABLE\nGhi nhận doanh thu", C_EMERALD)
    ]

    for i, (st, desc, col) in enumerate(steps):
        c = add_card(s15, Inches(0.8 + i * 2.4), Inches(2.0), Inches(2.1), Inches(2.3), bg_color=C_WHITE, border_color=col)
        tf = c.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = st
        p.font.bold = True
        p.font.size = Pt(12)
        p.font.color.rgb = col
        p.alignment = PP_ALIGN.CENTER

        p_d = tf.add_paragraph()
        p_d.text = desc
        p_d.font.size = Pt(9.5)
        p_d.font.color.rgb = C_TEXT_DARK
        p_d.alignment = PP_ALIGN.CENTER

    # Rollback Mechanism Card
    rbox = add_card(s15, Inches(0.8), Inches(4.7), Inches(11.733), Inches(2.1), bg_color=RGBColor(254, 242, 242), border_color=C_RED)
    tf_r = rbox.text_frame
    tf_r.word_wrap = True
    p = tf_r.paragraphs[0]
    p.text = "🔄 CƠ CHẾ HOÀN TÁC KHI HỦY ĐƠN (ROLLBACK & STOCK RESTORATION)"
    p.font.bold = True
    p.font.size = Pt(12)
    p.font.color.rgb = C_RED

    r_items = [
        "Kịch bản khách hủy đơn khi đơn ở trạng thái PENDING hoặc nhà hàng từ chối (REJECTED):",
        "• Hệ thống tự động kích hoạt restoreStock() để hoàn trả số lượng tồn kho nguyên vẹn vào menu_items.csv.",
        "• Nếu đơn đã được gán tài xế mà bị hủy: Thu hồi đơn và giải phóng tài xế về trạng thái AVAILABLE ngay lập tức.",
        "• Đảm bảo không thất thoát tồn kho, không treo tài xế ảo, duy trì dữ liệu sạch xuyên suốt quá trình chạy."
    ]
    for rit in r_items:
        pi = tf_r.add_paragraph()
        pi.text = rit
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    s15.notes_slide.notes_text_frame.text = (
        "Vòng đời đơn hàng trải qua 5 trạng thái tuần tự. "
        "Đặc biệt khi hủy đơn, hệ thống có cơ chế Rollback tự động hoàn trả số lượng món ăn và giải phóng tài xế."
    )

    # ==========================================
    # SLIDE 16: SYSTEM VERIFICATION & TESTING
    # ==========================================
    s16 = prs.slides.add_slide(blank_layout)
    add_bg(s16)
    add_header(s16, "Kiểm định Chất lượng: 8/8 Test Suites PASS 100%", "Độ tin cậy & Kiểm thử")

    tests = [
        ("Test 1: CSV File Integrity", "Cấu trúc 10 file CSV vật lý không bị lỗi định dạng hoặc mất cột.", "PASS 100%"),
        ("Test 2: PBKDF2 Auth Security", "Đăng ký, băm mật khẩu với Salt và xác thực đăng nhập chính xác.", "PASS 100%"),
        ("Test 3: Haversine Calculation", "Tính khoảng cách địa lý GPS chính xác từng mét sai số.", "PASS 100%"),
        ("Test 4: Oversell Prevention", "Kiểm định chặn triệt để bán âm món ăn khi nhiều khách đặt cùng lúc.", "PASS 100%"),
        ("Test 5: Double Assignment Block", "Kiểm định chặn gán trùng 1 tài xế cho 2 đơn hàng đồng thời.", "PASS 100%"),
        ("Test 6: Driver Overload Shield", "Kiểm định tài xế bận không bao giờ bị gán thêm đơn mới.", "PASS 100%"),
        ("Test 7: Rollback Restoration", "Kiểm định hoàn trả số lượng tồn kho đầy đủ khi đơn bị hủy.", "PASS 100%"),
        ("Test 8: Multithread Stability", "Chạy tải cực hạn 700 đơn không xảy ra Deadlock hay Crash.", "PASS 100%")
    ]

    for i, (t_title, t_desc, t_st) in enumerate(tests):
        row = i // 2
        col = i % 2
        left = Inches(0.8 + col * 6.0)
        top = Inches(1.8 + row * 1.25)
        card = add_card(s16, left, top, Inches(5.7), Inches(1.1), bg_color=C_WHITE, border_color=C_BORDER_LIGHT)
        tf = card.text_frame
        tf.word_wrap = True

        p = tf.paragraphs[0]
        p.text = t_title
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_NAVY

        p_desc = tf.add_paragraph()
        p_desc.text = t_desc
        p_desc.font.size = Pt(8.5)
        p_desc.font.color.rgb = C_TEXT_MUTED

        # Badge
        badge = s16.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left + Inches(4.3), top + Inches(0.2), Inches(1.2), Inches(0.35))
        badge.fill.solid()
        badge.fill.fore_color.rgb = RGBColor(220, 252, 231)
        badge.line.color.rgb = C_EMERALD
        tf_b = badge.text_frame
        p_b = tf_b.paragraphs[0]
        p_b.text = t_st
        p_b.font.bold = True
        p_b.font.size = Pt(9)
        p_b.font.color.rgb = C_EMERALD
        p_b.alignment = PP_ALIGN.CENTER

    s16.notes_slide.notes_text_frame.text = (
        "Hệ thống vượt qua toàn bộ 8 bộ kiểm định SystemVerificationTest với tỉ lệ thành công 100%, "
        "bảo đảm tính ổn định và độ tin cậy tuyệt đối của mã nguồn."
    )

    # ==========================================
    # SLIDE 17: LESSONS LEARNED & CONTRIBUTIONS
    # ==========================================
    s17 = prs.slides.add_slide(blank_layout)
    add_bg(s17)
    add_header(s17, "Đóng góp & Bài học Kinh nghiệm Dự án", "Tổng kết Giá trị")

    c1 = add_card(s17, Inches(0.8), Inches(1.8), Inches(5.6), Inches(4.8))
    tf1 = c1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "💡 GIÁ TRỊ KỸ THUẬT ĐẠT ĐƯỢC"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_NAVY
    v_points = [
        "1. Làm chủ 100% OOP thuần túy: Không ỷ lại vào biến toàn cục static, xây dựng kiến trúc MVC tách biệt, dễ bảo trì và mở rộng.",
        "2. Giải quyết triệt để bài toán Concurrency: Xử lý đồng bộ dữ liệu trên file CSV phẳng không thua kém RDBMS.",
        "3. Đóng góp nghiên cứu Big Question: Cung cấp số liệu định lượng chứng minh tính ưu việt của Optimistic Locking trong hệ thống giao vận.",
        "4. Tính thẩm mỹ & Trải nghiệm người dùng: Bản đồ ASCII Grid trực quan và giao diện dòng lệnh phân cấp rõ ràng."
    ]
    for pt in v_points:
        pi = tf1.add_paragraph()
        pi.text = pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    c2 = add_card(s17, Inches(6.8), Inches(1.8), Inches(5.7), Inches(4.8))
    tf2 = c2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "👥 BÀI HỌC VỀ QUẢN TRỊ DỰ ÁN NHÓM"
    p.font.bold = True
    p.font.size = Pt(13)
    p.font.color.rgb = C_CYAN
    t_points = [
        "1. Sức mạnh của Ma trận WBS & RACI: Phân công đúng người đúng việc giúp nhóm hoàn thành 100% chức năng đúng hạn.",
        "2. Kỷ luật Đồng bộ Tuyệt đối: Đảm bảo tài liệu, thiết kế UML, Use Case và mã nguồn luôn khớp từng chi tiết.",
        "3. Quy trình Kiểm thử Tự động: Phát hiện sớm lỗi xung đột dữ liệu ngay trong quá trình phát triển.",
        "4. Quản lý Phiên bản Git: Hợp nhất mã nguồn trơn tru, không xảy ra xung đột merge code."
    ]
    for pt in t_points:
        pi = tf2.add_paragraph()
        pi.text = pt
        pi.font.size = Pt(10)
        pi.font.color.rgb = C_TEXT_DARK

    s17.notes_slide.notes_text_frame.text = (
        "Đề tài không chỉ mang lại kết quả kỹ thuật xuất sắc mà còn giúp 6 thành viên trong nhóm "
        "nâng cao kỹ năng làm việc nhóm, quản trị dự án phần mềm theo tiêu chuẩn ngành."
    )

    # ==========================================
    # SLIDE 18: Q&A & THANK YOU (DARK THEME)
    # ==========================================
    s18 = prs.slides.add_slide(blank_layout)
    add_bg(s18, dark=True)

    c_qa = add_card(s18, Inches(1.5), Inches(1.5), Inches(10.333), Inches(4.5), bg_color=C_CARD_DARK, border_color=C_CYAN)
    tf_qa = c_qa.text_frame
    tf_qa.word_wrap = True

    p = tf_qa.paragraphs[0]
    p.text = "XIN CHÂN THÀNH CẢM ƠN"
    p.font.bold = True
    p.font.size = Pt(32)
    p.font.color.rgb = C_WHITE
    p.alignment = PP_ALIGN.CENTER

    p_sub = tf_qa.add_paragraph()
    p_sub.text = "Quý Thầy/Cô và Hội đồng Chấm Đồ án LAB211 – FPT University"
    p_sub.font.size = Pt(16)
    p_sub.font.color.rgb = C_CYAN_LIGHT
    p_sub.alignment = PP_ALIGN.CENTER

    p_div = tf_qa.add_paragraph()
    p_div.text = "\n💬 SẴN SÀNG ĐÓN NHẬN CÂU HỎI PHẢN BIỆN (Q&A)\n"
    p_div.font.bold = True
    p_div.font.size = Pt(20)
    p_div.font.color.rgb = C_AMBER
    p_div.alignment = PP_ALIGN.CENTER

    p_info = tf_qa.add_paragraph()
    p_info.text = (
        "Nhóm 01 | Food Delivery Network Simulation (FDS)\n"
        "Mã nguồn & Tài liệu chính thức: https://github.com/trat21430-spec/Food-Delivery\n"
        "Bản Word Phát biểu bài toán: Phat_Bieu_Bai_Toan_Food_Delivery_LAB211.docx"
    )
    p_info.font.size = Pt(11)
    p_info.font.color.rgb = RGBColor(203, 213, 225)
    p_info.alignment = PP_ALIGN.CENTER

    s18.notes_slide.notes_text_frame.text = (
        "Nhóm 01 xin chân thành cảm ơn Thầy/Cô đã chú ý lắng nghe. "
        "Chúng em rất mong nhận được những góp ý quý báu và sẵn sàng giải đáp mọi câu hỏi từ Hội đồng."
    )

    # Save presentations
    destinations = [
        "D:/Lab211-Group1/Bao_Cao_Food_Delivery_LAB211.pptx",
        "docs/Bao_Cao_Food_Delivery_LAB211.pptx",
        "D:/Lab211-Group1/NHOM_01_LAB211_FoodDelivery/docs/Bao_Cao_Food_Delivery_LAB211.pptx"
    ]

    for dest in destinations:
        os.makedirs(os.path.dirname(dest), exist_ok=True)
        prs.save(dest)
        print(f"Saved PPTX: {dest}")

    print("ALL PPTX PRESENTATIONS CREATED SUCCESSFULLY!")

if __name__ == "__main__":
    create_deck()
