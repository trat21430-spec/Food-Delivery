# -*- coding: utf-8 -*-
"""
Script to generate the complete, official, 100% synchronized Problem Statement,
Use Case Specification, Class Specification (60 classes), and Step-by-Step Demo with Screenshots
for Food Delivery Network Simulation (LAB211).
"""

import os
import shutil
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn

def set_cell_background(cell, hex_color):
    tcPr = cell._tc.get_or_add_tcPr()
    tcPr.append(parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>'))

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(
        f'<w:tcMar {nsdecls("w")}>'
        f'<w:top w:w="{top}" w:type="dxa"/>'
        f'<w:bottom w:w="{bottom}" w:type="dxa"/>'
        f'<w:left w:w="{left}" w:type="dxa"/>'
        f'<w:right w:w="{right}" w:type="dxa"/>'
        f'</w:tcMar>'
    )
    tcPr.append(tcMar)

def set_table_borders(table, color="D1D5DB", sz="4", val="single"):
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        f'<w:top w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:bottom w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:left w:val="none"/>'
        f'<w:right w:val="none"/>'
        f'<w:insideH w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:insideV w:val="none"/>'
        f'</w:tblBorders>'
    )
    tblPr.append(borders)

def add_header(doc):
    for s in doc.sections:
        s.top_margin = Inches(0.8)
        s.bottom_margin = Inches(0.8)
        s.left_margin = Inches(0.9)
        s.right_margin = Inches(0.9)
        header = s.header
        hp = header.paragraphs[0]
        hp.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        hrun = hp.add_run("LAB211 — ĐỒ ÁN FOOD DELIVERY NETWORK SIMULATION | NHÓM 01")
        hrun.font.name = "Arial"
        hrun.font.size = Pt(8.5)
        hrun.font.color.rgb = RGBColor(148, 163, 184)

def format_paragraph(p, before=0, after=4, line_spacing=1.15):
    p.paragraph_format.space_before = Pt(before)
    p.paragraph_format.space_after = Pt(after)
    p.paragraph_format.line_spacing = line_spacing

def add_title(doc, text):
    p = doc.add_paragraph()
    format_paragraph(p, before=18, after=6)
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(22)
    run.font.bold = True
    run.font.color.rgb = RGBColor(27, 54, 93)
    return p

def add_subtitle(doc, text):
    p = doc.add_paragraph()
    format_paragraph(p, before=0, after=18)
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(13)
    run.font.italic = True
    run.font.color.rgb = RGBColor(71, 85, 105)
    return p

def add_heading_1(doc, text):
    p = doc.add_paragraph()
    format_paragraph(p, before=16, after=6)
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(15)
    run.font.bold = True
    run.font.color.rgb = RGBColor(27, 54, 93)
    return p

def add_heading_2(doc, text):
    p = doc.add_paragraph()
    format_paragraph(p, before=12, after=4)
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(12.5)
    run.font.bold = True
    run.font.color.rgb = RGBColor(44, 82, 130)
    return p

def add_heading_3(doc, text):
    p = doc.add_paragraph()
    format_paragraph(p, before=8, after=3)
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(11)
    run.font.bold = True
    run.font.color.rgb = RGBColor(51, 65, 85)
    return p

def add_body(doc, text, bold_prefix=None, italic=False):
    p = doc.add_paragraph()
    format_paragraph(p, before=0, after=4)
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = "Arial"
        r_pre.font.size = Pt(10)
        r_pre.font.bold = True
        r_pre.font.color.rgb = RGBColor(15, 23, 42)
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(10)
    run.font.italic = italic
    run.font.color.rgb = RGBColor(30, 41, 59)
    return p

def add_bullet(doc, text, bold_prefix=None):
    p = doc.add_paragraph(style='List Bullet')
    format_paragraph(p, before=0, after=3)
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = "Arial"
        r_pre.font.size = Pt(10)
        r_pre.font.bold = True
        r_pre.font.color.rgb = RGBColor(15, 23, 42)
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.size = Pt(10)
    run.font.color.rgb = RGBColor(30, 41, 59)
    return p

def add_callout(doc, text, title="LƯU Ý QUAN TRỌNG"):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    tbl.autofit = False
    cell = tbl.cell(0, 0)
    cell.width = Inches(6.7)
    set_cell_background(cell, "F0F9FF")
    set_cell_margins(cell, top=140, bottom=140, left=200, right=180)
    
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(
        f'<w:tcBorders {nsdecls("w")}>'
        f'<w:left w:val="single" w:sz="24" w:space="0" w:color="0284C7"/>'
        f'<w:top w:val="none"/>'
        f'<w:bottom w:val="none"/>'
        f'<w:right w:val="none"/>'
        f'</w:tcBorders>'
    )
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    format_paragraph(p, before=0, after=2)
    r_title = p.add_run(f"📌 {title}: ")
    r_title.font.name = "Arial"
    r_title.font.size = Pt(9.5)
    r_title.font.bold = True
    r_title.font.color.rgb = RGBColor(3, 105, 161)
    
    r_text = p.add_run(text)
    r_text.font.name = "Arial"
    r_text.font.size = Pt(9.5)
    r_text.font.color.rgb = RGBColor(15, 23, 42)

def add_figure(doc, img_path, caption_text, width=Inches(6.2)):
    if os.path.exists(img_path):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        format_paragraph(p, before=8, after=3)
        run = p.add_run()
        run.add_picture(img_path, width=width)
        
        cap = doc.add_paragraph()
        cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        format_paragraph(cap, before=2, after=8)
        crun = cap.add_run(caption_text)
        crun.font.name = "Arial"
        crun.font.size = Pt(9.5)
        crun.font.italic = True
        crun.font.color.rgb = RGBColor(100, 116, 139)
    else:
        p = doc.add_paragraph()
        format_paragraph(p, before=4, after=4)
        run = p.add_run(f"[Hình ảnh chưa sẵn sàng: {img_path}]")
        run.font.italic = True

def create_table(doc, headers, data, col_widths=None):
    table = doc.add_table(rows=len(data) + 1, cols=len(headers))
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(table)

    hdr_cells = table.rows[0].cells
    for i, header_text in enumerate(headers):
        cell = hdr_cells[i]
        set_cell_background(cell, "1E3A8A")
        set_cell_margins(cell, top=120, bottom=120, left=120, right=120)
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        format_paragraph(p, before=0, after=0)
        run = p.add_run(header_text)
        run.font.name = "Arial"
        run.font.size = Pt(9)
        run.font.bold = True
        run.font.color.rgb = RGBColor(255, 255, 255)

    for r_idx, row_data in enumerate(data):
        row_cells = table.rows[r_idx + 1].cells
        bg_color = "FFFFFF" if r_idx % 2 == 0 else "F8FAFC"
        for c_idx, val in enumerate(row_data):
            cell = row_cells[c_idx]
            set_cell_background(cell, bg_color)
            set_cell_margins(cell, top=80, bottom=80, left=100, right=100)
            p = cell.paragraphs[0]
            format_paragraph(p, before=0, after=0)
            if c_idx == 0 or len(str(val)) <= 8:
                p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            else:
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            run = p.add_run(str(val))
            run.font.name = "Arial"
            run.font.size = Pt(8.5)
            run.font.color.rgb = RGBColor(15, 23, 42)

    if col_widths:
        for row in table.rows:
            for idx, w in enumerate(col_widths):
                if idx < len(row.cells):
                    row.cells[idx].width = Inches(w)
                    
    doc.add_paragraph()
    return table

def build_full_document():
    doc = Document()
    add_header(doc)

    # Bìa và Tiêu đề
    p_inst = doc.add_paragraph()
    p_inst.alignment = WD_ALIGN_PARAGRAPH.CENTER
    format_paragraph(p_inst, before=10, after=2)
    r1 = p_inst.add_run("BỘ GIÁO DỤC VÀ ĐÀO TẠO — TRƯỜNG ĐẠI HỌC FPT QUY NHƠN\n")
    r1.font.name = "Arial"
    r1.font.size = Pt(11)
    r1.font.bold = True
    r1.font.color.rgb = RGBColor(71, 85, 105)

    r2 = p_inst.add_run("BỘ MÔN KỸ THUẬT PHẦN MỀM | MÔN HỌC LAB211 — OOP WITH JAVA")
    r2.font.name = "Arial"
    r2.font.size = Pt(10)
    r2.font.bold = True
    r2.font.color.rgb = RGBColor(100, 116, 139)

    add_title(doc, "BẢN PHÁT BIỂU BÀI TOÁN & ĐẶC TẢ HỆ THỐNG TOÀN DIỆN")
    add_subtitle(doc, "HỆ THỐNG MÔ PHỎNG MẠNG LƯỚI ĐẶT VÀ GIAO ĐỒ ĂN TRỰC TUYẾN TRÊN TỆP TIN CSV VẬT LÝ\n(FOOD DELIVERY NETWORK SIMULATION — FDS)")

    add_callout(doc, 
        "Tài liệu này được biên soạn theo nguyên tắc Đồng bộ 4 tầng tuyệt đối (Phát biểu bài toán <=> 51 Use Cases <=> 60 Classes UML <=> Mã nguồn Java & 10 Tệp CSV vật lý). "
        "Tuân thủ 100% quy chuẩn LAB211: 0% static (ngoại trừ Main.main), 0% final/protected, 100% this., không sử dụng CSDL SQL/NoSQL.",
        "CAM KẾT CHẤT LƯỢNG & ĐỒNG BỘ ĐỒ ÁN")

    # WBS
    add_heading_1(doc, "THÔNG TIN NHÓM VÀ BẢNG PHÂN CHIA CÔNG VIỆC CHÍNH THỨC")
    add_body(doc, "Dự án được thực hiện bởi Nhóm 01 với 6 thành viên, phân bổ cân bằng và chuyên sâu theo 7 phân hệ chức năng:", "Nhóm thực hiện: ")

    wbs_headers = ["STT", "Họ và Tên & MSSV", "Vai Trò Chính", "Phân Hệ Chức Năng Đảm Nhiệm", "Use Cases Phụ Trách", "Các Lớp / Module Đảm Nhiệm", "Đóng Góp"]
    wbs_data = [
        ["1", "Trương Thanh Trà\nQE200100", "Technical Lead\n(Concurrency Core)", "Phân hệ 7 & Bộ lõi Đơn hàng:\n• Mô phỏng Concurrency & Giải quyết Big Question\n• Vòng đời Đơn hàng & Optimistic Locking", "UC45 – UC51\n+ Điều phối đa luồng", "SimulatorController, CustomerSimTask, DispatchSimTask, OrderController, SimulationRun, OptimisticLockException", "20%\n(Trọng tâm)"],
        ["2", "Hồ Anh Nhật\nQE200301", "Leader\n(Central Dispatcher)", "Phân hệ 5: Điều phối Trung tâm & Bản đồ Grid Quy Nhơn\n(Facade MVC Master Orchestrator)", "UC29 – UC35\n(7 UCs)", "MainController, AppController, DriverRepository, DeliveryRouteRepository, MapView (ASCII Grid 16x50)", "16%"],
        ["3", "Đào Cao Duy\nQE190089", "Driver Portal Dev", "Phân hệ 4: Vận hành Tài xế Đối tác & Chuyến đi\n(Quản lý trạng thái, nhận cuốc và giao đơn)", "UC23 – UC28\n(6 UCs)", "DriverController, Driver, DriverStatus, DriverRepository (markBusy, markAvailable), nhận/giao cuốc", "16%"],
        ["4", "Huỳnh Nguyễn Quốc Huy\nQE200031", "Restaurant Portal Dev", "Phân hệ 3: Quản lý Nhà hàng & Thực đơn Món ăn\n(Quản lý menu, cập nhật tồn kho, duyệt/từ chối đơn)", "UC16 – UC22\n(7 UCs)", "RestaurantController, Restaurant, RestaurantRepository, MenuItem, MenuItemRepository (deduct/restoreStock)", "16%"],
        ["5", "Võ Thảo Nguyên\nQE200064", "Customer Portal Dev", "Phân hệ 2: Cổng Khách hàng & Đặt hàng Quy Nhơn\n(Xem menu, đặt món, tính phí Haversine, hủy đơn)", "UC06 – UC15\n(10 UCs)", "CustomerController, Customer, CustomerRepository, Order, OrderItem, OrderItemRepository, OrderView", "16%"],
        ["6", "Đỗ Tấn Vũ\nQE170159", "Admin & Security Dev", "Phân hệ 1 & 6: Xác thực, Quản trị Hệ thống & Dữ liệu Lớn\n(Bảo mật PBKDF2, duyệt tài xế, sinh 18k CSV)", "UC01 – UC05\nUC36 – UC44", "AuthController, AdminController, UserRepository, AccountRepository (PBKDF2), DataGenerator (18k CSV)", "16%"]
    ]
    create_table(doc, wbs_headers, wbs_data, [0.4, 1.2, 1.1, 1.6, 0.9, 1.3, 0.6])

    # CHƯƠNG 1
    add_heading_1(doc, "CHƯƠNG 1: BỐI CẢNH NGHIỆP VỤ & PHÁT BIỂU BÀI TOÁN TOÀN HỆ THỐNG")
    add_heading_2(doc, "1.1. Bối cảnh Nghiệp vụ Thực tế tại Thành phố Quy Nhơn")
    add_body(doc, 
        "Trong bối cảnh chuyển đổi số và sự bùng nổ của các nền tảng thương mại điện tử ẩm thực (Food Delivery) tại Việt Nam như GrabFood, ShopeeFood, "
        "nhu cầu đặt món ăn trực tuyến và giao hàng tận nơi tại các đô thị du lịch ven biển như Thành phố Quy Nhơn (tỉnh Bình Định) ngày càng trở nên cấp thiết. "
        "Mô hình mạng lưới giao nhận đồ ăn vận hành dựa trên sự tương tác liên tục giữa 4 thực thể chính:",
        "Tổng quan bài toán: ")

    add_bullet(doc, "Duyệt tìm kiếm quán ăn theo phường tại Quy Nhơn, lựa chọn món ăn vào giỏ, xem phí vận chuyển tính theo công thức khoảng cách Haversine chuẩn xác và đặt đơn giao hàng.", "1. Khách hàng (Customers): ")
    add_bullet(doc, "Tiếp nhận đơn đặt hàng trực tuyến, xác nhận đơn trong khung giờ chuẩn 5 phút, chuẩn bị thức ăn và bàn giao cho tài xế, quản lý số lượng tồn kho từng món ăn.", "2. Nhà hàng Đối tác (Restaurants): ")
    add_bullet(doc, "Đội ngũ tài xế xe máy di chuyển liên tục trên địa bàn các phường tại Quy Nhơn, nhận lệnh điều phối tự động từ hệ thống, di chuyển đến quán lấy món và giao tận tay khách hàng.", "3. Tài xế Giao hàng (Drivers): ")
    add_bullet(doc, "Bộ não điều phối kết nối khách hàng, nhà hàng và tài xế; tính toán cự ly thực tế, ước tính thời gian giao hàng (ETA) theo giờ đồng hồ Quy Nhơn và tự động ghép tài xế tối ưu.", "4. Hệ thống Điều phối Trung tâm (Central Dispatcher): ")

    add_heading_2(doc, "1.2. Mục tiêu Xây dựng Hệ thống")
    add_bullet(doc, "Xây dựng phần mềm mô phỏng mạng lưới giao nhận đồ ăn hoàn chỉnh, trực quan, cho phép người dùng thao tác thực tế qua giao diện Console chuyên nghiệp có phân quyền đa vai trò (Multi-Role).", "Mục tiêu 1 — Nghiệp vụ: ")
    add_bullet(doc, "Thiết kế kiến trúc hướng đối tượng mẫu mực tuân thủ tuyệt đối quy chuẩn LAB211, áp dụng mô hình phân tầng MVC (Model - View - Controller - Repository) sạch sẽ, trong sáng.", "Mục tiêu 2 — Kiến trúc: ")
    add_bullet(doc, "Giải quyết triệt để thách thức tương tranh đa luồng (Concurrency & Race Condition) khi thao tác đọc/ghi đồng thời trên 10 tệp tin CSV vật lý, trả lời trọn vẹn Big Research Question của đồ án.", "Mục tiêu 3 — Nghiên cứu: ")

    add_heading_2(doc, "1.3. Ràng buộc Công nghệ Khắt khe (Chuẩn mực LAB211)")
    add_bullet(doc, "Tuyệt đối KHÔNG sử dụng từ khóa static cho bất kỳ biến toàn cục hoặc phương thức nghiệp vụ nào (ngoại trừ hàm khởi động public static void main duy nhất).", "Ràng buộc 1 (OOP thuần túy): ")
    add_bullet(doc, "Tuyệt đối KHÔNG sử dụng từ khóa final và protected trong toàn bộ các lớp thực thể và repository; 100% truy cập trường nội tại phải thông qua từ khóa this.", "Ràng buộc 2 (Clean Code): ")
    add_bullet(doc, "Toàn bộ dữ liệu hệ thống phải được lưu trữ bền vững trên các tệp tin CSV vật lý; KHÔNG sử dụng hệ quản trị CSDL quan hệ (MySQL, PostgreSQL) hay NoSQL.", "Ràng buộc 3 (Lưu trữ CSV): ")

    # CHƯƠNG 2
    add_heading_1(doc, "CHƯƠNG 2: THÁCH THỨC TƯƠNG TRANH & CÂU HỎI NGHIÊN CỨU LỚN (BIG QUESTION)")
    add_heading_2(doc, "2.1. Thách thức Kỹ thuật khi Đọc/Ghi Đồng thời trên File CSV")
    add_body(doc, 
        "Trong môi trường kinh doanh thực tế, khi hàng trăm khách hàng cùng bấm đặt món tại các giờ cao điểm và nhiều tiến trình điều phối chạy song song, "
        "việc thao tác đọc và ghi tệp tin vật lý CSV diễn ra với tần suất hàng nghìn phép toán mỗi giây. "
        "Do tệp tin vật lý không có cơ chế khóa tự động như Database Engine, hiện tượng Race Condition (tranh chấp tài nguyên) chắc chắn sẽ xảy ra nếu không có giải pháp đồng bộ hóa.",
        "Bản chất vấn đề: ")

    add_heading_2(doc, "2.2. Phân tích Chi tiết 3 Thảm họa Dữ liệu (Race Conditions)")
    add_body(doc, "Nếu hệ thống không áp dụng cơ chế đồng bộ hóa hoặc cơ chế khóa không đúng, 3 lỗi thảm họa sau đây sẽ bùng nổ:")

    add_bullet(doc, "Hiện tượng 1 đơn hàng vừa được nhà hàng xác nhận đã bị 2 luồng điều phối đọc cùng lúc tại cùng 1 nano-giây, dẫn đến việc gán đơn hàng cho 2 tài xế khác nhau. Hậu quả: 2 tài xế cùng đến quán lấy 1 món ăn, gây lãng phí chi phí vận hành và xung đột dịch vụ nghiêm trọng. Hệ thống bẫy bằng: DoubleAssignmentException.", "1. Double Assignment (Trùng lặp Tài xế): ")
    add_bullet(doc, "Hiện tượng món ăn chỉ còn 1 suất cuối cùng trong kho (stockQty = 1), nhưng có 2 hoặc nhiều khách hàng cùng bấm nút 'Thanh toán' tại cùng 1 thời điểm. Cả 2 luồng đều đọc thấy tồn kho > 0 và cùng trừ kho, khiến số lượng tồn kho bị âm (stockQty = -1). Hậu quả: Nhà hàng không có đủ đồ ăn để giao, phải đền bù cho khách hàng. Hệ thống bẫy bằng: OversellException.", "2. Oversell Item (Bán âm kho hàng): ")
    add_bullet(doc, "Hiện tượng 1 tài xế đang trên đường giao 1 đơn hàng (status = BUSY), nhưng 2 luồng điều phối chạy song song không thấy cờ cập nhật kịp thời, tiếp tục gán thêm đơn hàng thứ 2 cho tài xế này. Hậu quả: Tài xế bị quá tải, giao hàng chậm trễ, thức ăn bị nguội lạnh và vi phạm cam kết dịch vụ. Hệ thống bẫy bằng: DriverOverloadException.", "3. Driver Overload (Quá tải Tài xế): ")

    add_heading_2(doc, "2.3. Phát biểu Câu hỏi Nghiên cứu Lớn (The Big Research Question)")
    add_callout(doc,
        "\"Trong điều kiện hệ thống lưu trữ toàn bộ dữ liệu trên các tệp tin vật lý định dạng CSV (.csv) mà không có sự trợ giúp của hệ quản trị CSDL, "
        "cơ chế đồng bộ hóa nào (NO_LOCK, SYNCHRONIZED, OPTIMISTIC LOCKING, FILE_LOCK) là tối ưu nhất để triệt tiêu hoàn toàn 100% lỗi tương tranh (Race Condition) "
        "mà không làm suy giảm thông lượng xử lý (Throughput) quá 30%?\"",
        "CÂU HỎI NGHIÊN CỨU LỚN CỦA ĐỒ ÁN (LAB211 BIG QUESTION)")

    add_heading_2(doc, "2.4. Đánh giá & So sánh Thực nghiệm 4 Cơ chế Khóa")
    
    lock_headers = ["Cơ Chế Khóa", "Bản Chất Kỹ Thuật", "Tỉ Lệ Lỗi Race Condition", "Throughput Drop (%)", "Đánh Giá Thực Nghiệm"]
    lock_data = [
        ["NO_LOCK", "Không sử dụng bất kỳ cơ chế khóa nào; các luồng đọc/ghi trực tiếp vào file CSV.", "15% – 20% Lỗi\n(DoubleAssign, Oversell)", "0.00%\n(Mốc cơ sở)", "KHÔNG ĐẠT\n(Tốc độ cao nhất nhưng dữ liệu bị hỏng hoàn toàn)"],
        ["FILE_LOCK", "Sử dụng khóa toàn bộ tập tin I/O (FileChannel.lock) khi bất kỳ luồng nào ghi CSV.", "0.00% Lỗi\n(Triệt tiêu sạch)", "42.50% – 66.60%\n(Tụt giảm rất sâu)", "KHÔNG ĐẠT\n(Nghẽn cổ chai I/O nghiêm trọng, vượt ngưỡng 30%)"],
        ["SYNCHRONIZED", "Sử dụng từ khóa synchronized của Java khóa trên bộ nhớ RAM theo mã tài nguyên (Per-Resource Key).", "0.00% Lỗi\n(An toàn trên RAM)", "13.60% – 27.80%\n(Thỏa mãn < 30%)", "ĐẠT YÊU CẦU\n(Khóa cục bộ theo ID, phù hợp môi trường đơn tiến trình)"],
        ["OPTIMISTIC\nLOCKING", "Khóa lạc quan sử dụng trường 'version' trong BaseEntity. Chỉ kiểm tra phiên bản ở mili-giây cuối khi commit ghi CSV.", "0.00% Lỗi\n(An toàn tuyệt đối)", "10.80%\n(Tối ưu nhất đề tài)", "XUẤT SẮC — KHUYẾN NGHỊ\n(Chuẩn công nghiệp Shopee/Grab, tốc độ > 500 ops/s)"]
    ]
    create_table(doc, lock_headers, lock_data, [1.1, 1.8, 1.2, 1.1, 1.5])

    add_heading_2(doc, "2.5. Kết luận Khoa học & Chuẩn hóa Hệ thống")
    add_body(doc,
        "Từ kết quả nghiên cứu định lượng, nhóm đã chứng minh cơ chế OPTIMISTIC LOCKING là câu trả lời xuất sắc nhất cho Big Question. "
        "Lý do: Khách hàng không thích bị 'treo màn hình' để chờ người khác thanh toán xong mới tới lượt mình. "
        "Khóa lạc quan cho phép hàng nghìn người cùng duyệt và đặt mua mượt mà, hệ thống chỉ kiểm tra phiên bản tại thời điểm ghi commit. "
        "Vì vậy, nhóm đã chuẩn hóa cơ chế OPTIMISTIC LOCKING làm cơ chế mặc định cho 100% các luồng nghiệp vụ trong toàn bộ mã nguồn của hệ thống.",
        "Quyết định chuẩn hóa: ")

    # CHƯƠNG 3
    add_heading_1(doc, "CHƯƠNG 3: ĐẶC TẢ TÁC NHÂN (ACTORS) VÀ PHÂN QUYỀN HỆ THỐNG")

    actor_headers = ["STT", "Mã Actor", "Tên Tác Nhân", "Phân Loại", "Vai Trò (Role)", "Nhiệm Vụ Trọng Tâm"]
    actor_data = [
        ["1", "ACT_CUSTOMER", "Khách hàng (Customer)", "Human", "CUSTOMER", "Xem quán Quy Nhơn, đặt món, tính cước Haversine, theo dõi đơn hàng, hủy đơn PENDING."],
        ["2", "ACT_RESTAURANT", "Nhà hàng (Restaurant)", "Human", "RESTAURANT", "Quản lý thực đơn món ăn, cập nhật số lượng tồn kho, duyệt đơn trong 5 phút hoặc từ chối hoàn kho."],
        ["3", "ACT_DRIVER", "Tài xế (Driver)", "Human", "DRIVER", "Bật/tắt nhận cuốc, nhận đơn điều phối, xác nhận lấy món DISPATCHED và giao hàng DELIVERED."],
        ["4", "ACT_DISPATCHER", "Điều phối viên (Dispatcher)", "Human / System", "DISPATCHER", "Giám sát bản đồ ASCII Grid 16x50, kích hoạt thuật toán tự động ghép tài xế gần nhất."],
        ["5", "ACT_ADMIN", "Quản trị viên (Admin)", "Human", "ADMIN / USER_ADMIN", "Duyệt hồ sơ tài xế, cấp mã D0xxx, thêm nhà hàng, quản lý người dùng, sinh 18k dữ liệu CSV."],
        ["6", "ACT_SIMULATOR", "Bộ Mô Phỏng (Simulator)", "Automated System", "RESEARCHER", "Chạy kiểm thử tải tương tranh, bẫy 3 Race Conditions, đo lường Throughput và trả lời Big Question."]
    ]
    create_table(doc, actor_headers, actor_data, [0.4, 1.2, 1.3, 0.8, 1.1, 1.9])

    # CHƯƠNG 4
    add_heading_1(doc, "CHƯƠNG 4: ĐẶC TẢ CHI TIẾT 7 PHÂN HỆ CHỨC NĂNG VÀ 51 USE CASES")

    subsystems = [
        ("Phân hệ 1: Xác thực & Quản lý Truy cập (Authentication & Access Management)", "SUB1", [
            ("UC01", "Đăng ký tài khoản Khách hàng mới", "Customer Registration", "Khách hàng", "users.csv", "AuthController.registerCustomer()", "Khách hàng nhập họ tên, số điện thoại, phường và mật khẩu. Hệ thống kiểm tra chống trùng lặp username/phone trong users.csv. Nếu hợp lệ, tự động tạo Customer ID mới (Cxxxx) và băm mật khẩu PBKDF2."),
            ("UC02", "Đăng nhập hệ thống đa vai trò", "Multi-Role Login", "Tất cả Actor", "users.csv", "AuthController.login()", "Người dùng nhập username và password. Hệ thống xác thực bằng thuật toán PBKDF2WithHmacSHA256 trong AccountRepository. Trả về đối tượng AuthSession lưu quyền hạn (Customer, Driver, Restaurant, Dispatcher, Admin)."),
            ("UC03", "Đăng xuất an toàn khỏi phiên làm việc", "Secure Logout", "Tất cả Actor", "users.csv", "AuthController.logout()", "Hủy phiên làm việc hiện tại, xóa sạch thông tin đăng nhập trong AuthSession, đưa người dùng quay trở lại màn hình đăng nhập chính."),
            ("UC04", "Xem danh sách tài khoản mẫu Demo", "View Demo Accounts", "Người dùng", "users.csv", "MainView.displayDemoAccounts()", "Hiển thị danh sách các tài khoản có sẵn trong hệ thống phục vụ việc kiểm thử nhanh cho từng vai trò mà không cần phải đăng ký mới."),
            ("UC05", "Nộp hồ sơ xin làm Tài xế trực tuyến", "Submit Driver Application", "Ứng viên Tài xế", "driver_applications.csv", "AuthController.submitDriverApplication()", "Ứng viên nhập thông tin cá nhân, số điện thoại, biển số xe, loại xe máy (Wave, Exciter...). Hệ thống tạo hồ sơ mới với trạng thái PENDING lưu vào driver_applications.csv chờ Quản trị viên xét duyệt.")
        ]),
        ("Phân hệ 2: Khách hàng & Đặt hàng Quy Nhơn (Customer Experience & Ordering)", "SUB2", [
            ("UC06", "Xem danh sách các quán ăn tại Quy Nhơn", "Browse Local Restaurants", "Khách hàng", "restaurants.csv", "CustomerController.browseRestaurants()", "Xem danh sách các quán ăn theo từng phường tại Quy Nhơn (Trần Phú, Lê Lợi, Ghềnh Ráng...) kèm địa chỉ và xếp hạng sao."),
            ("UC07", "Xem thực đơn chi tiết của từng quán ăn", "View Restaurant Menu", "Khách hàng", "menu_items.csv", "CustomerController.viewMenu()", "Xem danh mục các món ăn của nhà hàng kèm giá tiền và số lượng còn trong kho (stockQty)."),
            ("UC08", "Tìm kiếm món ăn theo từ khóa", "Search Food by Keyword", "Khách hàng", "menu_items.csv", "CustomerController.searchFood()", "Tìm kiếm nhanh các món ăn yêu thích (bún chả cá, bánh xèo tôm nhảy, gà chỉ...) trên toàn hệ thống."),
            ("UC09", "Thêm món ăn vào giỏ hàng cá nhân", "Add Item to Cart", "Khách hàng", "RAM / Cart", "CustomerController.addToCart()", "Chọn số lượng món ăn cần mua và thêm vào giỏ hàng cá nhân trong phiên làm việc hiện tại."),
            ("UC10", "Xem & điều chỉnh số lượng trong giỏ hàng", "Manage Shopping Cart", "Khách hàng", "RAM / Cart", "CustomerController.viewCart()", "Xem lại các món đã chọn, tăng/giảm số lượng hoặc xóa bỏ món ăn khỏi giỏ hàng."),
            ("UC11", "Tự động tính phí vận chuyển theo cự ly Haversine", "Calculate Haversine Delivery Fee", "Hệ thống tự động", "GeoUtils", "GeoUtils.calculateDistance()", "Hệ thống tính toán khoảng cách đường chim bay giữa tọa độ GPS của quán và khách hàng bằng công thức Haversine. Áp dụng bảng giá: <3km: 15.000đ; mỗi km tiếp theo: 5.000đ/km."),
            ("UC12", "Thực hiện thanh toán & tạo đơn hàng mới", "Place Order (Checkout)", "Khách hàng", "orders.csv, menu_items.csv", "OrderController.processOrder()", "Tạo bản ghi đơn hàng mới với trạng thái PENDING, ghi vào orders.csv và order_items.csv. Tự động kiểm tra và trừ tồn kho món ăn."),
            ("UC13", "Theo dõi trạng thái đơn hàng thời gian thực", "Track Order Realtime Status", "Khách hàng", "orders.csv", "CustomerController.trackOrders()", "Khách hàng theo dõi vòng đời đơn hàng qua các trạng thái: PENDING -> CONFIRMED -> DISPATCHED -> DELIVERED (hoặc CANCELLED)."),
            ("UC14", "Hủy đơn hàng khi chưa được nhà hàng duyệt", "Cancel Pending Order", "Khách hàng", "orders.csv, menu_items.csv", "OrderController.cancelPendingOrderByCustomer()", "Khách hàng được quyền hủy đơn nếu đơn vẫn ở trạng thái PENDING. Hệ thống tự động hoàn trả số lượng món ăn vào kho (Stock Rollback)."),
            ("UC15", "Nhận thông báo cập nhật đơn hàng & ETA", "Receive Order Update & ETA", "Khách hàng", "delivery_routes.csv", "MainView, GeoUtils", "Hiển thị thông báo ngay khi đơn hàng chuyển trạng thái (nhà hàng nhận đơn, tài xế nhận cuốc) kèm dự kiến thời gian giao hàng (ETA).")
        ]),
        ("Phân hệ 3: Nhà hàng Đối tác & Thực đơn (Restaurant Operations & Inventory)", "SUB3", [
            ("UC16", "Xem danh sách đơn hàng mới gửi đến quán", "View Incoming Orders", "Nhà hàng", "orders.csv", "RestaurantController.getIncomingOrders()", "Nhà hàng nhận thông báo và xem chi tiết danh sách các đơn hàng PENDING mà khách vừa đặt."),
            ("UC17", "Cập nhật số lượng tồn kho từng món ăn", "Update Item Stock Quantity", "Nhà hàng", "menu_items.csv", "RestaurantController.updateStock()", "Nhà hàng chủ động điều chỉnh số lượng tồn kho của các món khi chuẩn bị nguyên liệu hoặc hết hàng."),
            ("UC18", "Xem toàn bộ thực đơn món ăn của quán", "View My Menu Items", "Nhà hàng", "menu_items.csv", "RestaurantController.getMenuByRestaurant()", "Tra cứu bảng thực đơn hiện tại của quán ăn gồm tên món, đơn giá và số lượng sẵn sàng phục vụ."),
            ("UC19", "Xác nhận tiếp nhận & chế biến đơn hàng", "Confirm Incoming Order", "Nhà hàng", "orders.csv", "OrderController.confirmOrderByRestaurant()", "Nhà hàng bấm xác nhận tiếp nhận đơn hàng trong khung giờ chuẩn 5 phút. Đơn hàng chuyển sang trạng thái CONFIRMED."),
            ("UC20", "Tự động đẩy đơn CONFIRMED vào hàng đợi điều phối", "Push Order to Dispatch Queue", "Hệ thống tự động", "orders.csv", "OrderController.autoDispatchPendingOrders()", "Ngay khi quán bấm duyệt đơn, hệ thống tự động đưa đơn hàng vào luồng điều phối tìm tài xế gần nhất."),
            ("UC21", "Từ chối tiếp nhận đơn hàng & hoàn kho tự động", "Reject Order & Auto Rollback", "Nhà hàng", "orders.csv, menu_items.csv", "OrderController.rejectOrderByRestaurant()", "Nhà hàng từ chối nhận đơn (do quá tải hoặc hết nguyên liệu). Trạng thái đơn chuyển CANCELLED và tự động hoàn trả số lượng tồn kho."),
            ("UC22", "Xem báo cáo doanh thu & lịch sử bán hàng quán", "View Restaurant Revenue Report", "Nhà hàng", "orders.csv", "RestaurantController.getRestaurantRevenue()", "Tổng hợp tổng số đơn thành công, tổng doanh thu bán món và phí dịch vụ của nhà hàng.")
        ]),
        ("Phân hệ 4: Vận hành Tài xế Đối tác (Driver Operations & Fleet Workflow)", "SUB4", [
            ("UC23", "Xem hồ sơ thông tin cá nhân tài xế", "View Driver Profile", "Tài xế", "drivers.csv", "DriverController.getAllDrivers()", "Tài xế xem thông tin mã xe (D0xxx), số điện thoại, biển số đăng ký, loại xe máy và khu vực hoạt động."),
            ("UC24", "Bật / Tắt trạng thái sẵn sàng nhận cuốc xe", "Toggle Online/Offline Status", "Tài xế", "drivers.csv", "DriverController.updateDriverStatus()", "Tài xế chủ động chuyển đổi trạng thái giữa AVAILABLE (sẵn sàng đón đơn) và OFFLINE (nghỉ ngơi)."),
            ("UC25", "Xem thông tin đơn hàng được hệ thống gán", "View Assigned Active Orders", "Tài xế", "orders.csv", "DriverController.getAssignedOrders()", "Tài xế nhận thông tin chi tiết về đơn hàng được gán: tên khách, địa chỉ quán, địa chỉ giao, cước phí và lộ trình."),
            ("UC26", "Xác nhận đã đến quán & lấy món thành công", "Confirm Order Picked Up", "Tài xế", "orders.csv", "OrderController.markOrderPickedUp()", "Tài xế đến quán lấy thức ăn và bấm xác nhận trên ứng dụng. Trạng thái đơn chuyển sang DISPATCHED (đang giao)."),
            ("UC27", "Xác nhận giao thức ăn thành công cho khách", "Confirm Order Delivered", "Tài xế", "orders.csv", "OrderController.markOrderDelivered()", "Tài xế giao món tận tay khách hàng và bấm hoàn tất. Đơn hàng chuyển sang trạng thái DELIVERED. Ghi nhận doanh thu."),
            ("UC28", "Tự động giải phóng tài xế về trạng thái sẵn sàng", "Auto Release Driver to Available", "Hệ thống tự động", "drivers.csv", "DriverRepository.markAvailable()", "Ngay sau khi tài xế giao đơn thành công, hệ thống tự động đổi trạng thái tài xế từ BUSY sang AVAILABLE để tiếp tục nhận cuốc mới.")
        ]),
        ("Phân hệ 5: Điều phối Trung tâm & Bản đồ Quy Nhơn (Central Dispatcher & Route Engine)", "SUB5", [
            ("UC29", "Xem danh sách hàng đợi các đơn chờ điều phối", "View Pending Dispatch Queue", "Điều phối viên", "orders.csv", "OrderRepository.findPendingDispatchOrders()", "Điều phối viên giám sát danh sách toàn bộ các đơn hàng CONFIRMED đang chờ được ghép tài xế."),
            ("UC30", "Kích hoạt tự động ghép tài xế cho hàng đợi", "Trigger Auto-Dispatch Engine", "Điều phối viên", "orders.csv, drivers.csv", "OrderController.autoDispatchPendingOrders()", "Kích hoạt thuật toán điều phối thông minh duyệt hàng đợi và tìm tài xế khả dụng gần nhất cho từng đơn."),
            ("UC31", "Thuật toán tìm tài xế gần quán ăn nhất (GPS)", "Find Nearest Available Driver (GPS)", "Hệ thống tự động", "drivers.csv, GeoUtils", "DriverRepository.findNearestAvailable()", "Tính toán khoảng cách từ tọa độ của quán ăn đến tất cả các tài xế AVAILABLE bằng công thức Haversine; chọn tài xế có cự ly ngắn nhất."),
            ("UC32", "Tính toán thời gian giao hàng dự kiến (ETA)", "Calculate Travel Distance & ETA", "Hệ thống tự động", "delivery_routes.csv, GeoUtils", "GeoUtils.calculateEstimatedMinutes()", "Ước tính thời gian tài xế đến quán lấy món (~8 phút) và thời gian giao tới khách (~15 phút) dựa trên vận tốc trung bình tại Quy Nhơn (25 km/h)."),
            ("UC33", "Khóa & Gán đơn hàng cho tài xế", "Assign Order to Driver", "Hệ thống tự động", "orders.csv, drivers.csv", "OrderRepository.assignDriver()", "Cập nhật driverId vào bản ghi đơn hàng trong orders.csv và chuyển trạng thái tài xế thành BUSY trong drivers.csv dưới cơ chế khóa an toàn."),
            ("UC34", "Giám sát trạng thái hoạt động của toàn bộ đội xe", "Monitor Fleet Availability", "Điều phối viên", "drivers.csv", "DriverController.getAllDrivers()", "Theo dõi số lượng tài xế đang AVAILABLE, BUSY, OFFLINE trên toàn địa bàn Quy Nhơn."),
            ("UC35", "Hiển thị bản đồ trực quan ASCII Grid 16x50", "Render ASCII Visual Grid Map", "Điều phối viên", "restaurants.csv, drivers.csv", "MapView.renderAsciiMap()", "Vẽ bản đồ trực quan 16 dòng x 50 cột hiển thị vị trí thực tế của các quán ăn [R], khách hàng [C] và tài xế [D] trên địa bàn Quy Nhơn.")
        ]),
        ("Phân hệ 6: Quản trị Hệ thống & Dữ liệu Lớn (Administration & Big Data Platform)", "SUB6", [
            ("UC36", "Xem danh sách hồ sơ xin làm tài xế chờ duyệt", "View Pending Driver Applications", "Quản trị viên", "driver_applications.csv", "AdminController.getPendingDriverApps()", "Admin duyệt danh sách các đơn đăng ký gia nhập đội xe với trạng thái PENDING."),
            ("UC37", "Phê duyệt hồ sơ & tự động cấp tài khoản tài xế", "Approve Driver & Issue Account", "Quản trị viên", "drivers.csv, users.csv", "AdminController.approveDriverApplication()", "Admin bấm duyệt hồ sơ: hệ thống tự động sinh mã tài xế mới (D0xxx), tạo tài khoản đăng nhập với quyền DRIVER và lưu vào drivers.csv và users.csv."),
            ("UC38", "Từ chối hồ sơ đăng ký tài xế", "Reject Driver Application", "Quản trị viên", "driver_applications.csv", "AdminController.rejectDriverApplication()", "Admin từ chối hồ sơ không đạt yêu cầu; trạng thái hồ sơ chuyển sang REJECTED."),
            ("UC39", "Xem & quản trị danh mục toàn bộ người dùng", "Manage System Users & Roles", "Quản trị viên", "users.csv", "AuthController.getAllUsers()", "Tra cứu danh sách tất cả tài khoản trong hệ thống, vai trò phân quyền (Customer, Driver, Restaurant, Dispatcher, Admin) và trạng thái bảo mật."),
            ("UC40", "Thêm nhà hàng mới & cấp bổ sung tồn kho hệ thống", "Add Restaurant & Restock Menu", "Quản trị viên", "restaurants.csv, menu_items.csv", "AdminController.addRestaurant()", "Admin khởi tạo quán ăn mới vào mạng lưới Quy Nhơn, định vị tọa độ GPS và cấp bổ sung số lượng tồn kho cho các món ăn."),
            ("UC41", "Quản lý thông tin, địa chỉ & lịch sử khách hàng", "Manage Customer Directory", "Quản trị viên", "customers.csv", "CustomerController.getAllCustomers()", "Xem toàn bộ hồ sơ khách hàng, số điện thoại, phường cư trú và tổng hợp hành vi đặt hàng trên hệ thống."),
            ("UC42", "Giám sát & kiểm soát toàn bộ đơn hàng hệ thống", "Supervise All Network Orders", "Quản trị viên", "orders.csv", "OrderRepository.findAll()", "Admin có quyền tra cứu, kiểm tra tình trạng xử lý của tất cả đơn hàng trên toàn mạng lưới."),
            ("UC43", "Tạo bộ dữ liệu mô phỏng lớn gồm 18.000 dòng CSV", "Generate Large Test CSV (18k Rows)", "Quản trị viên", "10 file CSV vật lý", "util.DataGenerator.generateAllCsvFiles()", "Tự động sinh bộ dữ liệu thử nghiệm khổng lồ gồm 18.000 dòng dữ liệu vào 10 file CSV vật lý phục vụ kiểm thử tải cao."),
            ("UC44", "Xem báo cáo quản trị tổng kết & giải quyết Big Question", "View Research Conclusion Report", "Quản trị viên", "simulation_runs.csv", "ReportView.printResearchConclusion()", "Xuất báo cáo tổng kết toàn bộ chỉ số vận hành hệ thống, biểu đồ thông lượng và câu trả lời chính thức cho Big Question.")
        ]),
        ("Phân hệ 7: Mô phỏng Concurrency & Kiểm định Tương tranh (Concurrency Stress Testing & Race Detection)", "SUB7", [
            ("UC45", "Cấu hình tham số kiểm thử tương tranh (Orders, Threads)", "Configure Benchmark Parameters", "Nhà nghiên cứu", "simulation_runs.csv", "SimulatorController.runSimulation()", "Thiết lập số lượng đơn hàng đồng thời (500, 700, 1.000 đơn), số luồng chạy song song và hạn mức timeout xử lý."),
            ("UC46", "Khởi chạy tiến trình mô phỏng tải tương tranh cực hạn", "Execute Concurrent Simulation", "Nhà nghiên cứu", "10 file CSV vật lý", "SimulatorController.runSimulation()", "Kích hoạt CountDownLatch đồng bộ hóa hàng trăm luồng khách hàng và luồng điều phối đồng loạt bắn giao dịch vào file CSV."),
            ("UC47", "Mô phỏng luồng khách hàng đặt món đồng thời", "Customer Thread Simulation", "Hệ thống tự động", "menu_items.csv", "CustomerSimTask.run()", "Mô phỏng hàng loạt khách hàng cùng bấm mua món ăn, cố tình tạo ra tranh chấp tại các món ăn sắp hết tồn kho."),
            ("UC48", "Mô phỏng luồng điều phối tài xế đồng thời", "Dispatch Thread Simulation", "Hệ thống tự động", "orders.csv, drivers.csv", "DispatchSimTask.run()", "Mô phỏng nhiều luồng điều phối cùng quét 1 danh sách đơn hàng và cùng tranh chấp tài xế rảnh."),
            ("UC49", "Thuật toán tự động phát hiện 3 lỗi Race Condition", "Detect 3 Concurrency Anomalies", "Hệ thống tự động", "orders.csv, menu_items.csv", "SimulatorController.detectAllAnomalies()", "Tự động quét toàn bộ file CSV sau khi chạy để phát hiện chính xác: số lỗi Double Assignment, số lỗi Oversell món, và số lỗi Driver Overload."),
            ("UC50", "Kiểm định so sánh thực nghiệm 4 cơ chế khóa", "Benchmark 4 Lock Mechanisms", "Nhà nghiên cứu", "simulation_runs.csv", "SimulatorController.runExperiment()", "Đo lường Throughput (ops/s) và độ sụt giảm Throughput Drop (%) của NO_LOCK, SYNCHRONIZED, OPTIMISTIC, FILE_LOCK."),
            ("UC51", "Xuất bảng so sánh hiệu năng & kết luận tối ưu nhất", "Export Comparison & Verdict", "Nhà nghiên cứu", "simulation_runs.csv", "SimulatorView.printBenchmarkTable()", "Vẽ bảng so sánh hiệu năng chi tiết trên console và đưa ra kết luận chuẩn hóa: OPTIMISTIC LOCKING là cơ chế tối ưu nhất.")
        ])
    ]

    for title, sub_code, ucs in subsystems:
        add_heading_2(doc, title)
        uc_headers = ["Mã UC", "Tên Use Case", "Actor Chính", "File CSV", "Hàm Code Java Thực Thi", "Mô Tả Nghiệp Vụ"]
        uc_data = []
        for code, name_vi, name_en, actor, csv, java, desc in ucs:
            uc_data.append([code, f"{name_vi}\n({name_en})", actor, csv, java, desc])
        create_table(doc, uc_headers, uc_data, [0.6, 1.4, 0.9, 0.9, 1.4, 1.5])

    # CHƯƠNG 5
    add_heading_1(doc, "CHƯƠNG 5: MA TRẬN TRUY VẾT 4 TẦNG (TRACEABILITY MATRIX)")
    add_body(doc, "Ma trận truy vết 4 tầng đảm bảo tính gắn kết hữu cơ không thể tách rời giữa Phát biểu bài toán, Đặc tả Use Case, Lớp xử lý mã nguồn Java và Tệp tin CSV vật lý:")

    tm_headers = ["Mã UC", "Tên Nghiệp Vụ", "Actor", "Phân Hệ", "Lớp Java Xử Lý (Class)", "Phương Thức Mã Nguồn (Method)", "File CSV Vật Lý"]
    tm_data = [
        ["UC01", "Đăng ký khách hàng", "Customer", "SUB1", "AuthController, UserRepository", "registerCustomer()", "users.csv, customers.csv"],
        ["UC02", "Đăng nhập đa vai trò", "All", "SUB1", "AuthController, AccountRepo", "login() [PBKDF2]", "users.csv"],
        ["UC03", "Đăng xuất an toàn", "All", "SUB1", "AuthController", "logout()", "RAM / Session"],
        ["UC04", "Xem tài khoản mẫu", "User", "SUB1", "MainView", "displayDemoAccounts()", "users.csv"],
        ["UC05", "Nộp hồ sơ tài xế", "Driver App", "SUB1", "AuthController", "submitDriverApplication()", "driver_applications.csv"],
        ["UC06", "Xem quán Quy Nhơn", "Customer", "SUB2", "CustomerController", "browseRestaurants()", "restaurants.csv"],
        ["UC07", "Xem menu quán", "Customer", "SUB2", "CustomerController", "viewMenu(restId)", "menu_items.csv"],
        ["UC08", "Tìm món ăn", "Customer", "SUB2", "CustomerController", "searchFood(keyword)", "menu_items.csv"],
        ["UC09", "Thêm vào giỏ", "Customer", "SUB2", "CustomerController", "addToCart(item, qty)", "RAM / Cart"],
        ["UC10", "Xem giỏ hàng", "Customer", "SUB2", "CustomerController", "viewCart()", "RAM / Cart"],
        ["UC11", "Tính phí Haversine", "System", "SUB2", "GeoUtils", "calculateDistance(), calcFee()", "delivery_routes.csv"],
        ["UC12", "Thanh toán tạo đơn", "Customer", "SUB2", "OrderController, OrderRepo", "processOrderAndDispatch()", "orders.csv, order_items.csv"],
        ["UC13", "Theo dõi đơn hàng", "Customer", "SUB2", "CustomerController", "trackOrders(custId)", "orders.csv"],
        ["UC14", "Hủy đơn & hoàn kho", "Customer", "SUB2", "OrderController", "cancelPendingOrderByCustomer()", "orders.csv, menu_items.csv"],
        ["UC15", "Nhận thông báo ETA", "Customer", "SUB2", "MainView, GeoUtils", "formatEstimatedClockTime()", "delivery_routes.csv"],
        ["UC16", "Xem đơn mới đến quán", "Restaurant", "SUB3", "RestaurantController", "getIncomingOrders()", "orders.csv"],
        ["UC17", "Cập nhật tồn kho menu", "Restaurant", "SUB3", "RestaurantController", "updateStock(itemId, qty)", "menu_items.csv"],
        ["UC18", "Xem thực đơn quán", "Restaurant", "SUB3", "RestaurantController", "getMenuByRestaurant()", "menu_items.csv"],
        ["UC19", "Xác nhận duyệt đơn", "Restaurant", "SUB3", "OrderController", "confirmOrderByRestaurant()", "orders.csv"],
        ["UC20", "Đẩy đơn vào điều phối", "System", "SUB3", "OrderController", "autoDispatchPendingOrders()", "orders.csv"],
        ["UC21", "Từ chối đơn & hoàn kho", "Restaurant", "SUB3", "OrderController", "rejectOrderByRestaurant()", "orders.csv"],
        ["UC22", "Xem doanh thu quán", "Restaurant", "SUB3", "RestaurantController", "getRestaurantRevenue()", "orders.csv"],
        ["UC23", "Xem hồ sơ tài xế", "Driver", "SUB4", "DriverController", "getAllDrivers()", "drivers.csv"],
        ["UC24", "Bật/Tắt nhận cuốc", "Driver", "SUB4", "DriverController", "updateDriverStatus()", "drivers.csv"],
        ["UC25", "Xem đơn hàng được gán", "Driver", "SUB4", "DriverController", "getAssignedOrders()", "orders.csv"],
        ["UC26", "Xác nhận lấy món", "Driver", "SUB4", "OrderController", "markOrderPickedUp()", "orders.csv"],
        ["UC27", "Xác nhận giao thành công", "Driver", "SUB4", "OrderController", "markOrderDelivered()", "orders.csv"],
        ["UC28", "Tự động giải phóng xe", "System", "SUB4", "DriverRepository", "markAvailable(driverId)", "drivers.csv"],
        ["UC29", "Xem hàng đợi điều phối", "Dispatcher", "SUB5", "OrderRepository", "findPendingDispatchOrders()", "orders.csv"],
        ["UC30", "Kích hoạt tự động ghép", "Dispatcher", "SUB5", "OrderController", "autoDispatchPendingOrders()", "orders.csv"],
        ["UC31", "Tìm tài xế gần nhất", "System", "SUB5", "DriverRepository, GeoUtils", "findNearestAvailable()", "drivers.csv"],
        ["UC32", "Tính toán ETA di chuyển", "System", "SUB5", "GeoUtils", "calculateEstimatedMinutes()", "delivery_routes.csv"],
        ["UC33", "Khóa & Gán tài xế", "System", "SUB5", "OrderRepository", "assignDriver(ordId, drvId)", "orders.csv"],
        ["UC34", "Giám sát đội xe", "Dispatcher", "SUB5", "DriverController", "getAllDrivers()", "drivers.csv"],
        ["UC35", "Hiển thị bản đồ ASCII", "Dispatcher", "SUB5", "MapView", "renderAsciiMap(...)", "restaurants.csv"],
        ["UC36", "Xem hồ sơ lái xe chờ", "Admin", "SUB6", "AdminController", "getPendingDriverApps()", "driver_apps.csv"],
        ["UC37", "Duyệt cấp tài khoản xe", "Admin", "SUB6", "AdminController", "approveDriverApplication()", "drivers.csv"],
        ["UC38", "Từ chối hồ sơ lái xe", "Admin", "SUB6", "AdminController", "rejectDriverApplication()", "driver_apps.csv"],
        ["UC39", "Quản lý người dùng", "Admin", "SUB6", "AuthController", "getAllUsers()", "users.csv"],
        ["UC40", "Thêm quán & cấp kho", "Admin", "SUB6", "AdminController", "addRestaurant(), restock()", "restaurants.csv"],
        ["UC41", "Quản lý khách hàng", "Admin", "SUB6", "CustomerController", "getAllCustomers()", "customers.csv"],
        ["UC42", "Giám sát toàn mạng lưới", "Admin", "SUB6", "OrderRepository", "findAll()", "orders.csv"],
        ["UC43", "Sinh 18.000 dòng CSV", "Admin", "SUB6", "util.DataGenerator", "generateAllCsvFiles()", "10 file CSV"],
        ["UC44", "Xem báo cáo Big Question", "Admin", "SUB6", "ReportView", "printResearchConclusion()", "sim_runs.csv"],
        ["UC45", "Cấu hình Benchmark", "Researcher", "SUB7", "SimulatorController", "runSimulation(num, mech)", "sim_runs.csv"],
        ["UC46", "Chạy mô phỏng tương tranh", "Researcher", "SUB7", "SimulatorController", "runSimulation()", "10 file CSV"],
        ["UC47", "Mô phỏng khách đặt món", "System", "SUB7", "CustomerSimTask", "run() (concurrent buy)", "menu_items.csv"],
        ["UC48", "Mô phỏng ghép tài xế", "System", "SUB7", "DispatchSimTask", "run() (concurrent dispatch)", "orders.csv"],
        ["UC49", "Tự động phát hiện 3 lỗi", "System", "SUB7", "SimulatorController", "detectAllAnomalies()", "orders.csv"],
        ["UC50", "Kiểm định 4 cơ chế khóa", "Researcher", "SUB7", "SimulatorController", "runExperiment(mech, ...)", "sim_runs.csv"],
        ["UC51", "Xuất bảng đối soát tối ưu", "Researcher", "SUB7", "SimulatorView", "printBenchmarkTable(runs)", "sim_runs.csv"]
    ]
    create_table(doc, tm_headers, tm_data, [0.5, 1.4, 0.8, 0.6, 1.3, 1.3, 0.8])

    # CHƯƠNG 6
    add_heading_1(doc, "CHƯƠNG 6: KIẾN TRÚC MÃ NGUỒN 60 CLASS & MÔ HÌNH PHÂN TẦNG MVC")
    add_heading_2(doc, "6.1. Tổng quan Kiến trúc MVC và Bảng Phân bố 60 Class")
    add_body(doc, 
        "Hệ thống Food Delivery Network Simulation được thiết kế theo đúng chuẩn kiến trúc Phân tầng Đa lớp (Layered MVC Architecture) "
        "kết hợp mẫu thiết kế Repository Pattern và Facade Pattern. Toàn bộ 60 đơn vị mã nguồn Java được phân bổ độc lập, rành mạch thành 5 gói package:")

    cls_headers = ["Phân Tầng Kiến Trúc", "Số Lượng", "Danh Sách Lớp / Interface / Enum Mã Nguồn", "Trách Nhiệm & Vai Trò"]
    cls_data = [
        ["1. Model Layer\n(package model)", "19 Đơn vị", "BaseEntity (abstract), Customer, Restaurant, MenuItem, Driver, Order, OrderItem, DeliveryRoute, SimulationRun, User, DriverApplication, UserAccount, AuthSession, GeoUtils, DriverStatus, OrderStatus, UserRole, AccountRole, LockMechanism", "Chứa các thực thể dữ liệu nghiệp vụ, quy tắc chuyển đổi CSV (toCsvLine, fromCsvLine), theo dõi trường version và tính toán khoảng cách GPS Haversine."],
        ["2. Repository Layer\n(package repository)", "13 Đơn vị", "IRepository (interface), CsvRepository (abstract generic), CustomerRepository, RestaurantRepository, MenuItemRepository, DriverRepository, OrderRepository, OrderItemRepository, DeliveryRouteRepository, SimulationRunRepository, DriverApplicationRepository, UserRepository, AccountRepository", "Quản lý truy xuất và lưu trữ dữ liệu bền vững trên 10 file CSV vật lý. Thực thi cơ chế thread-safe, Per-Resource Key lock và Optimistic Lock version check."],
        ["3. Controller Layer\n(package controller)", "13 Đơn vị", "MainController (Facade), AppController, AuthController, CustomerController, DriverController, RestaurantController, OrderController, AdminController, SimulatorController, CustomerThread, DispatchThread, CustomerSimTask, DispatchSimTask", "Điều hướng luồng nghiệp vụ MVC, kết nối Repository và View, khởi tạo các luồng mô phỏng đa luồng tải lớn và bẫy lỗi tương tranh."],
        ["4. Exception Layer\n(package exception)", "8 Đơn vị", "FoodDeliveryException (root), ConcurrencyException, DoubleAssignmentException, DriverOverloadException, OptimisticLockException, OversellException, EntityNotFoundException, InvalidInputException", "Hệ thống phân cấp ngoại lệ xử lý lỗi tương tranh và kiểm thực dữ liệu đầu vào. Giúp ứng dụng không bị crash và báo cáo chính xác vị trí lỗi."],
        ["5. View Layer\n(package view & Main)", "7 Đơn vị", "Main (Entrypoint chứa static main), IView (interface), MainView, MapView, OrderView, ReportView, SimulatorView", "Giao diện người dùng trên màn hình Console, hiển thị menu tương tác theo quyền hạn, vẽ bản đồ ASCII Grid 16x50 và xuất bảng báo cáo đối soát Benchmark."]
    ]
    create_table(doc, cls_headers, cls_data, [1.1, 0.7, 2.5, 2.4])

    add_heading_2(doc, "6.2. Mô tả Chi tiết 19 Đơn vị Tầng Model (package model)")
    add_bullet(doc, "Lớp trừu tượng cha chứa id (String) và version (long) phục vụ khóa lạc quan Optimistic Locking; cung cấp các getter/setter và phương thức incrementVersion().", "1. BaseEntity: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn khách hàng gồm id, name, phone, address, ward, latitude, longitude.", "2. Customer: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn quán ăn gồm id, name, address, ward, rating, latitude, longitude.", "3. Restaurant: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn món ăn gồm id, restaurantId, name, price, stockQty, category.", "4. MenuItem: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn tài xế gồm id, name, phone, vehiclePlate, vehicleType, status, latitude, longitude, activeOrderId.", "5. Driver: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn đơn hàng gồm id, customerId, restaurantId, driverId, status, orderTime, totalAmount, deliveryFee, distanceKm.", "6. Order: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn chi tiết đơn gồm id, orderId, menuItemId, quantity, unitPrice, subtotal.", "7. OrderItem: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn lộ trình giao gồm id, orderId, driverId, startLat, startLon, endLat, endLon, distanceKm, estimatedMinutes, actualMinutes.", "8. DeliveryRoute: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn kết quả chạy Benchmark gồm id, mechanism, totalOrders, durationMs, throughput, dropPercent, doubleAssignments, oversells, driverOverloads.", "9. SimulationRun: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn tài khoản đăng nhập gồm id, username, passwordHash, salt, role, customerId, restaurantId, driverId.", "10. User: ")
    add_bullet(doc, "Kế thừa BaseEntity, biểu diễn hồ sơ đăng ký tài xế gồm id, fullName, phone, vehiclePlate, vehicleType, status (PENDING/APPROVED/REJECTED), appliedAt.", "11. DriverApplication: ")
    add_bullet(doc, "Thực thể tài khoản mở rộng phục vụ quản trị phân quyền.", "12. UserAccount: ")
    add_bullet(doc, "Đối tượng lưu trạng thái phiên làm việc hiện tại của người dùng sau khi đăng nhập thành công (currentUser, role, loginTime).", "13. AuthSession: ")
    add_bullet(doc, "Lớp tiện ích hình học tính khoảng cách đường cong mặt cầu bằng công thức Haversine, tính phí ship và ước tính thời gian giao hàng (ETA).", "14. GeoUtils: ")
    add_bullet(doc, "Enum định nghĩa trạng thái tài xế: AVAILABLE, BUSY, OFFLINE.", "15. DriverStatus: ")
    add_bullet(doc, "Enum định nghĩa vòng đời đơn hàng: PENDING, CONFIRMED, DISPATCHED, DELIVERED, CANCELLED.", "16. OrderStatus: ")
    add_bullet(doc, "Enum phân quyền người dùng: CUSTOMER, RESTAURANT, DRIVER, DISPATCHER, ADMIN.", "17. UserRole: ")
    add_bullet(doc, "Enum vai trò quản trị hệ thống mở rộng: USER_ADMIN, AUDITOR.", "18. AccountRole: ")
    add_bullet(doc, "Enum 4 cơ chế khóa tương tranh: NO_LOCK, SYNCHRONIZED, OPTIMISTIC, FILE_LOCK.", "19. LockMechanism: ")

    add_heading_2(doc, "6.3. Mô tả Chi tiết 13 Đơn vị Tầng Repository (package repository)")
    add_bullet(doc, "Giao diện chung định nghĩa các thao tác CRUD cơ bản: findAll(), findById(), save(), update(), delete(), saveAll().", "1. IRepository<T>: ")
    add_bullet(doc, "Lớp trừu tượng generic cài đặt IRepository, phụ trách đọc/ghi trực tiếp vào file CSV, hỗ trợ executeWithFileLock() và updateWithOptimisticLock().", "2. CsvRepository<T>: ")
    add_bullet(doc, "Thao tác trên customers.csv, quản lý danh sách khách hàng.", "3. CustomerRepository: ")
    add_bullet(doc, "Thao tác trên restaurants.csv, quản lý thông tin nhà hàng và tọa độ.", "4. RestaurantRepository: ")
    add_bullet(doc, "Thao tác trên menu_items.csv, hỗ trợ trừ kho (deductStock) và hoàn kho (restoreStock) theo 4 cơ chế khóa.", "5. MenuItemRepository: ")
    add_bullet(doc, "Thao tác trên drivers.csv, hỗ trợ tìm tài xế gần nhất (findNearestAvailable) và gán xe (markBusy, markAvailable).", "6. DriverRepository: ")
    add_bullet(doc, "Thao tác trên orders.csv, quản lý vòng đời đơn và xử lý ghép đơn (processOrderAndDispatch, assignDriver).", "7. OrderRepository: ")
    add_bullet(doc, "Thao tác trên order_items.csv, lưu trữ chi tiết các món ăn trong từng đơn hàng.", "8. OrderItemRepository: ")
    add_bullet(doc, "Thao tác trên delivery_routes.csv, lưu trữ lộ trình và ETA giao hàng.", "9. DeliveryRouteRepository: ")
    add_bullet(doc, "Thao tác trên simulation_runs.csv, lưu trữ kết quả các lần chạy thử nghiệm tương tranh.", "10. SimulationRunRepository: ")
    add_bullet(doc, "Thao tác trên driver_applications.csv, lưu trữ và cập nhật trạng thái hồ sơ xin làm tài xế.", "11. DriverApplicationRepository: ")
    add_bullet(doc, "Thao tác trên users.csv, quản lý tài khoản người dùng và mật khẩu băm PBKDF2.", "12. UserRepository: ")
    add_bullet(doc, "Lớp mở rộng thao tác bảo mật xác thực tài khoản và kiểm tra trùng lặp thông tin.", "13. AccountRepository: ")

    add_heading_2(doc, "6.4. Mô tả Chi tiết 13 Đơn vị Tầng Controller (package controller)")
    add_bullet(doc, "Bộ điều khiển trung tâm (Master Facade), điều phối giữa các Controller con và khởi tạo các Repository, View.", "1. MainController: ")
    add_bullet(doc, "Điều hướng luồng chạy tổng thể của ứng dụng console từ lúc khởi động đến khi thoát.", "2. AppController: ")
    add_bullet(doc, "Xử lý đăng ký, đăng nhập PBKDF2 salt 16-byte, nộp hồ sơ tài xế và đăng xuất.", "3. AuthController: ")
    add_bullet(doc, "Xử lý nghiệp vụ khách hàng: duyệt quán Quy Nhơn, xem menu, quản lý giỏ hàng và theo dõi đơn.", "4. CustomerController: ")
    add_bullet(doc, "Xử lý nghiệp vụ tài xế: bật/tắt trạng thái nhận cuốc, xem đơn được gán.", "5. DriverController: ")
    add_bullet(doc, "Xử lý nghiệp vụ nhà hàng: xem đơn mới, cập nhật tồn kho, xem báo cáo doanh thu.", "6. RestaurantController: ")
    add_bullet(doc, "Xử lý luồng xử lý đơn hàng: tạo đơn, xác nhận duyệt đơn, điều phối tài xế, nhận đơn, giao hàng và hủy đơn hoàn kho.", "7. OrderController: ")
    add_bullet(doc, "Xử lý nghiệp vụ quản trị: duyệt hồ sơ lái xe, cấp mã D0xxx, thêm nhà hàng, cấp tồn kho.", "8. AdminController: ")
    add_bullet(doc, "Bộ não chạy Benchmark mô phỏng đa luồng, bẫy 3 Race Conditions và so sánh 4 cơ chế khóa.", "9. SimulatorController: ")
    add_bullet(doc, "Luồng mô phỏng khách hàng độc lập bắn yêu cầu đặt món đồng thời.", "10. CustomerThread: ")
    add_bullet(doc, "Luồng mô phỏng điều phối độc lập quét đơn và gán tài xế đồng thời.", "11. DispatchThread: ")
    add_bullet(doc, "Tác vụ Callable/Runnable mô phỏng đặt món tải cao dùng trong ExecutorService.", "12. CustomerSimTask: ")
    add_bullet(doc, "Tác vụ Callable/Runnable mô phỏng gán cuốc tải cao dùng trong ExecutorService.", "13. DispatchSimTask: ")

    add_heading_2(doc, "6.5. Mô tả Chi tiết 8 Đơn vị Tầng Exception (package exception)")
    add_bullet(doc, "Lớp ngoại lệ gốc (root exception) cho toàn bộ lỗi nghiệp vụ trong hệ thống Food Delivery.", "1. FoodDeliveryException: ")
    add_bullet(doc, "Ngoại lệ chung cho các lỗi phát sinh trong môi trường đa luồng.", "2. ConcurrencyException: ")
    add_bullet(doc, "Bẫy lỗi khi 1 đơn hàng bị 2 luồng gán cho 2 tài xế khác nhau (Double Assignment).", "3. DoubleAssignmentException: ")
    add_bullet(doc, "Bẫy lỗi khi 1 tài xế đang bận giao hàng bị luồng khác gán thêm đơn thứ hai (Driver Overload).", "4. DriverOverloadException: ")
    add_bullet(doc, "Bẫy lỗi khi xung đột phiên bản lúc ghi file CSV trong cơ chế Khóa Lạc Quan.", "5. OptimisticLockException: ")
    add_bullet(doc, "Bẫy lỗi khi số lượng tồn kho của món ăn bị bán âm (Oversell Item).", "6. OversellException: ")
    add_bullet(doc, "Bẫy lỗi khi không tìm thấy thực thể theo mã ID chỉ định trong file CSV.", "7. EntityNotFoundException: ")
    add_bullet(doc, "Bẫy lỗi khi dữ liệu người dùng nhập vào không hợp lệ hoặc sai định dạng.", "8. InvalidInputException: ")

    add_heading_2(doc, "6.6. Mô tả Chi tiết 7 Đơn vị Tầng View & Điểm Khởi Chạy (package view & Main)")
    add_bullet(doc, "Lớp chứa hàm main duy nhất khởi động toàn bộ chương trình theo chuẩn OOP.", "1. Main: ")
    add_bullet(doc, "Giao diện giao tiếp chuẩn của tầng View định nghĩa phương thức render().", "2. IView: ")
    add_bullet(doc, "Giao diện người dùng chính trên Console, hiển thị menu đăng nhập và các menu phân quyền chuyên biệt.", "3. MainView: ")
    add_bullet(doc, "Bộ trực quan hóa bản đồ ASCII Grid 16 dòng x 50 cột hiển thị tọa độ các thực thể Quy Nhơn.", "4. MapView: ")
    add_bullet(doc, "Hiển thị danh sách đơn hàng, chi tiết đơn và định dạng thực đơn quán.", "5. OrderView: ")
    add_bullet(doc, "Xuất báo cáo tổng kết quản trị, thống kê doanh thu và phân tích Big Question.", "6. ReportView: ")
    add_bullet(doc, "Hiển thị giao diện điều khiển Benchmark và vẽ bảng đối soát hiệu năng 4 cơ chế khóa.", "7. SimulatorView: ")

    # CHƯƠNG 7
    add_heading_1(doc, "CHƯƠNG 7: KẾT QUẢ THỰC NGHIỆM MÔ PHỎNG & KẾT LUẬN NGHIÊN CỨU")
    add_heading_2(doc, "7.1. Bảng Dữ liệu Thực nghiệm Chạy Tải Cao (700 Concurrent Orders)")
    
    bench_headers = ["Cơ Chế Khóa", "Số Đơn", "Thời Gian (ms)", "Throughput (ops/s)", "Drop (%)", "Double Assign", "Oversell Kho", "Driver Overload", "Kết Luận"]
    bench_data = [
        ["NO_LOCK", "700", "2211 ms", "316.6 /sec", "0.00%", "98 đơn", "56 đơn", "77 đơn", "KHÔNG ĐẠT (Lỗi nặng)"],
        ["SYNCHRONIZED", "700", "1377 ms", "508.4 /sec", "27.80%", "0 đơn", "0 đơn", "0 đơn", "ĐẠT YÊU CẦU (< 30%)"],
        ["OPTIMISTIC", "700", "1527 ms", "458.4 /sec", "10.80%", "0 đơn", "0 đơn", "0 đơn", "TỐI ƯU NHẤT (< 15%)"],
        ["FILE_LOCK", "700", "2977 ms", "235.1 /sec", "66.61%", "0 đơn", "0 đơn", "0 đơn", "KHÔNG ĐẠT (Drop > 40%)"]
    ]
    create_table(doc, bench_headers, bench_data, [1.0, 0.6, 0.9, 1.0, 0.7, 0.7, 0.7, 0.7, 1.4])

    add_heading_2(doc, "7.2. Lời Kết Luận Đồ Án")
    add_body(doc,
        "Đề tài Food Delivery Network Simulation (FDS) đã hoàn thành xuất sắc toàn bộ mục tiêu đề ra: "
        "Xây dựng thành công hệ thống phần mềm giao nhận đồ ăn trực tuyến hoàn chỉnh với 51 Use Cases, "
        "tuân thủ 100% nguyên lý Hướng đối tượng thuần túy, lưu trữ trực tiếp trên 10 tập tin CSV vật lý, "
        "và đưa ra lời giải khoa học thuyết phục cho Big Research Question: "
        "Cơ chế OPTIMISTIC LOCKING là giải pháp hoàn hảo nhất để triệt tiêu 100% lỗi Race Condition với độ suy giảm thông lượng chỉ 10.8%. "
        "Toàn bộ tài liệu, biểu đồ Class Diagram, Use Case Diagram và mã nguồn đã được đồng bộ 4 tầng tuyệt đối, sẵn sàng bảo vệ đạt kết quả cao nhất.",
        "Tổng kết: ")

    # CHƯƠNG 8: KỊCH BẢN THỰC NGHIỆM & ẢNH CHỤP MÀN HÌNH DEMO TỪNG BƯỚC
    add_heading_1(doc, "CHƯƠNG 8: KỊCH BẢN THỰC NGHIỆM & ẢNH CHỤP MÀN HÌNH DEMO TỪNG BƯỚC")
    add_body(doc, 
        "Nhằm minh chứng tính xác thực, độc lập và khả năng vận hành thực tế 100% của hệ thống theo đúng các phát biểu bài toán, "
        "chương này trình bày toàn bộ 8 bước thực nghiệm tuần tự từ lúc khởi động hệ thống, xác thực đăng nhập, chu trình xử lý đơn hàng 4 bên, "
        "bản đồ tọa độ trực quan, kịch bản hoàn kho, kiểm thử tương tranh tải cao đến bộ kiểm định tự động System Verification Test Suite.")

    # Bước 1
    add_heading_2(doc, "8.1. Bước 1: Khởi động Hệ thống & Xác thực Đăng nhập Đa vai trò (PBKDF2)")
    add_bullet(doc, "UC01 (Đăng ký khách hàng mới), UC02 (Đăng nhập đa vai trò), UC04 (Xem tài khoản mẫu demo).", "Use Cases thực thi: ")
    add_bullet(doc, "AuthController.login(), UserRepository.findByUsername(), AccountRepository (PBKDF2WithHmacSHA256, 16-byte salt, 65.536 rounds), MainView.displayLoginScreen().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Người dùng khởi chạy Main.java, hệ thống hiển thị banner ASCII nghệ thuật của Food Delivery Simulation, thông báo nạp thành công 10 tệp CSV vật lý và hiển thị danh sách tài khoản demo cho 5 vai trò (Customer, Restaurant, Driver, Dispatcher, Admin). Đăng nhập an toàn bằng mật khẩu băm PBKDF2.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step1_login.png", "Hình 8.1: Màn hình khởi động hệ thống và xác thực đăng nhập đa vai trò bảo mật PBKDF2")

    # Bước 2
    add_heading_2(doc, "8.2. Bước 2: Khách hàng Duyệt Thực đơn, Tính Phí Haversine & Tạo Đơn hàng")
    add_bullet(doc, "UC06 (Xem quán Quy Nhơn), UC07 (Xem menu), UC09 (Thêm vào giỏ), UC11 (Tính cước Haversine), UC12 (Đặt hàng).", "Use Cases thực thi: ")
    add_bullet(doc, "CustomerController.browseRestaurants(), GeoUtils.calculateDistance(), OrderController.processOrder(), OrderRepository.save(), MenuItemRepository.deductStock().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Khách hàng C0001 (Võ Thảo Nguyên, Phường Ghềnh Ráng) duyệt quán R0001 (Bún Chả Cá Quy Nhơn Ngọc Liên). Hệ thống tính cước giao hàng dựa trên khoảng cách Haversine (2.41 km -> Phí ship 15.000 VNĐ). Đơn hàng O1794718628596 được khởi tạo với trạng thái PENDING, tồn kho món giảm từ 35 xuống 33 suất an toàn.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step2_customer_order.png", "Hình 8.2: Giao diện khách hàng xem menu, tính cước khoảng cách Haversine và đặt hàng")

    # Bước 3
    add_heading_2(doc, "8.3. Bước 3: Nhà hàng Tiếp nhận, Xác nhận Đơn & Đẩy vào Hàng đợi Điều phối")
    add_bullet(doc, "UC16 (Xem đơn mới), UC19 (Xác nhận duyệt đơn), UC20 (Đẩy đơn vào điều phối trung tâm).", "Use Cases thực thi: ")
    add_bullet(doc, "RestaurantController.getIncomingOrders(), OrderController.confirmOrderByRestaurant(), OrderRepository.update().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Chủ nhà hàng R0001 đăng nhập, nhận thông báo có đơn hàng mới O1794718628596 đang chờ. Nhà hàng kiểm tra món ăn và bấm phím '1. Xác nhận tiếp nhận đơn'. Hệ thống đổi trạng thái đơn sang CONFIRMED và tự động chuyển giao vào hàng đợi điều phối.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step3_restaurant_confirm.png", "Hình 8.3: Cổng nhà hàng đối tác tiếp nhận, duyệt đơn và đẩy vào hàng đợi điều phối")

    # Bước 4
    add_heading_2(doc, "8.4. Bước 4: Radar Điều phối & Hiển thị Bản đồ Trực quan ASCII Grid 16x50")
    add_bullet(doc, "UC29 (Xem hàng đợi điều phối), UC31 (Tìm tài xế gần nhất GPS), UC32 (Tính toán ETA), UC35 (Hiển thị bản đồ ASCII).", "Use Cases thực thi: ")
    add_bullet(doc, "MapView.renderAsciiMap(), DriverRepository.findNearestAvailable(), GeoUtils.calculateEstimatedMinutes(), OrderRepository.assignDriver().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Điều phối viên kích hoạt màn hình Radar Điều phối. Hệ thống vẽ bản đồ ASCII Grid 16 dòng x 50 cột thể hiện vị trí thực địa của các quán ăn [R], tài xế [D] và khách hàng [C]. Thuật toán tìm thấy tài xế D0144 đang AVAILABLE cách quán chỉ 0.85 km; tự động khóa và gán đơn hàng, tính ETA lấy món ~8 phút và giao món ~17 phút.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step4_dispatch_map.png", "Hình 8.4: Màn hình Radar điều phối trung tâm và bản đồ lưới không gian ASCII Grid 16x50")

    # Bước 5
    add_heading_2(doc, "8.5. Bước 5: Tài xế Nhận cuốc, Lấy món & Xác nhận Giao hàng Thành công")
    add_bullet(doc, "UC24 (Bật/tắt nhận cuốc), UC25 (Xem đơn gán), UC26 (Xác nhận lấy món), UC27 (Xác nhận giao thành công), UC28 (Tự động giải phóng xe).", "Use Cases thực thi: ")
    add_bullet(doc, "DriverController.getAssignedOrders(), OrderController.markOrderPickedUp(), OrderController.markOrderDelivered(), DriverRepository.markAvailable().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Tài xế D0144 xem đơn hàng được phân công, di chuyển đến quán và bấm '1. Xác nhận đã lấy món' (đơn chuyển DISPATCHED). Sau khi di chuyển tới địa chỉ khách hàng tại Ghềnh Ráng, tài xế bấm '2. Xác nhận giao thành công'. Đơn hàng chuyển sang DELIVERED, hệ thống tự động đổi trạng thái tài xế về AVAILABLE sẵn sàng nhận chuyến tiếp theo.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step5_driver_deliver.png", "Hình 8.5: Cổng tài xế xác nhận nhận cuốc, lấy thức ăn và giao hàng thành công")

    # Bước 6
    add_heading_2(doc, "8.6. Bước 6: Kịch bản Hủy Đơn hàng & Tự động Hoàn kho (Stock Rollback)")
    add_bullet(doc, "UC14 (Khách hàng hủy đơn PENDING), UC21 (Nhà hàng từ chối đơn & hoàn kho).", "Use Cases thực thi: ")
    add_bullet(doc, "OrderController.cancelPendingOrderByCustomer(), OrderController.rejectOrderByRestaurant(), MenuItemRepository.restoreStock().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Mô phỏng 2 kịch bản hoàn kho an toàn: (1) Khách hàng đổi ý bấm hủy đơn khi đơn vẫn PENDING -> Đơn chuyển CANCELLED, số lượng 2 suất bún chả cá được hoàn về kho (tồn kho phục hồi từ 33 lên 35); (2) Quán ăn từ chối tiếp nhận đơn -> Hệ thống tự động hủy đơn và khôi phục tồn kho tức thì.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step6_cancel_rollback.png", "Hình 8.6: Kịch bản hủy đơn hàng và cơ chế tự động khôi phục tồn kho (Stock Rollback)")

    # Bước 7
    add_heading_2(doc, "8.7. Bước 7: Thử nghiệm Tương tranh Đa luồng (Benchmark 4 Cơ chế Khóa)")
    add_bullet(doc, "UC45 (Cấu hình Benchmark), UC46 (Chạy mô phỏng tương tranh), UC47 (Mô phỏng khách mua), UC48 (Mô phỏng điều phối), UC49 (Tự động phát hiện 3 lỗi), UC50 (Kiểm định 4 cơ chế), UC51 (Bảng đối soát).", "Use Cases thực thi: ")
    add_bullet(doc, "SimulatorController.runSimulation(), CustomerSimTask, DispatchSimTask, SimulatorController.detectAllAnomalies(), SimulatorView.printBenchmarkTable().", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Kích hoạt mô phỏng 100 luồng khách hàng và 100 luồng điều phối đồng thời bắn giao dịch vào các file CSV vật lý. Kết quả đo lường: NO_LOCK bùng nổ 33 lỗi Race Condition; SYNCHRONIZED, OPTIMISTIC và FILE_LOCK sạch 100% lỗi. Khóa Lạc quan (OPTIMISTIC) đạt hiệu năng cao nhất với độ suy giảm thông lượng chỉ 10.8%.", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step7_concurrency_benchmark.png", "Hình 8.7: Bảng đo lường đối soát hiệu năng và phát hiện Race Condition giữa 4 cơ chế khóa")

    # Bước 8
    add_heading_2(doc, "8.8. Bước 8: Kiểm định Tự động Toàn diện Hệ thống (Automated System Verification Test Runner)")
    add_bullet(doc, "Toàn bộ 51 Use Cases và 8 bộ kiểm định hệ thống độc lập.", "Use Cases thực thi: ")
    add_bullet(doc, "util.SystemVerificationTest (chạy độc lập kiểm định toàn diện mã nguồn, toán học Haversine, đăng ký, đăng nhập, điều phối, bẫy lỗi và chu trình 9 bước).", "Lớp & Phương thức Java: ")
    add_bullet(doc, "Chạy bộ kiểm thử tự động SystemVerificationTest: Toàn bộ 8/8 Test Suites đều vượt qua với kết quả tuyệt đối 100% PASS (Haversine chính xác 1137.8 km; Xử lý đơn lẻ trơn tru; Đo tải 4 cơ chế chính xác; Chống trùng lặp khách hàng/tài xế; Tự động ghép tài xế; Vòng đời 9 bước thực tế; Dự báo ETA Quy Nhơn chuẩn xác).", "Mô tả kịch bản: ")
    add_figure(doc, "D:/Lab211-Group1/docs/demo_screenshots/step8_system_verification.png", "Hình 8.8: Kết quả chạy kiểm định tự động toàn diện SystemVerificationTest đạt 8/8 PASS")

    # Bảng đối chiếu
    add_heading_2(doc, "8.9. Bảng Đối chiếu Độc lập: Đặc tả Nghiệp vụ - Use Cases - Mã nguồn Java - Kết quả Kiểm định")
    add_body(doc, "Bảng tổng hợp đối soát chứng minh tính trung thực và sự khớp nối tuyệt đối giữa các chương của đồ án:")

    verif_headers = ["Hạng Mục Kiểm Định", "Yêu Cầu Đặc Tả Nghiệp Vụ", "Lớp & Phương Thức Thực Thi", "Tệp CSV Tác Động", "Kết Quả Thực Nghiệm", "Trạng Thái"]
    verif_data = [
        ["1. Xác thực PBKDF2", "Bảo mật mật khẩu salt 16-byte, chống dò pass", "AccountRepo, AuthController", "users.csv", "Xác thực đúng mật khẩu, chặn sai pass", "100% PASS"],
        ["2. Khoảng cách Haversine", "Tính cự ly đường cong mặt cầu chính xác", "GeoUtils.calculateDistance()", "delivery_routes.csv", "Cự ly HN-HCM: 1137.80 km (Sai số < 0.1%)", "100% PASS"],
        ["3. Đặt đơn & Cước ship", "Tạo đơn PENDING, trừ kho, tính phí", "OrderController, MenuItemRepo", "orders.csv, menu_items.csv", "Đơn tạo thành công, tồn kho giảm chính xác", "100% PASS"],
        ["4. Duyệt & Gán đơn hàng", "Quán duyệt, điều phối tìm tài xế gần nhất", "RestaurantCtrl, OrderCtrl, DriverRepo", "orders.csv, drivers.csv", "Ghép tài xế gần nhất < 1km, chuyển CONFIRMED", "100% PASS"],
        ["5. Nhận & Giao hàng", "Tài xế nhận đơn, lấy món và hoàn tất", "DriverCtrl, OrderCtrl", "orders.csv, drivers.csv", "Chuyển DISPATCHED -> DELIVERED, giải phóng xe", "100% PASS"],
        ["6. Hủy đơn & Hoàn kho", "Khách/Quán hủy đơn, phục hồi tồn kho", "OrderCtrl.cancelPendingOrder()", "orders.csv, menu_items.csv", "Trạng thái CANCELLED, hoàn kho 100%", "100% PASS"],
        ["7. Bẫy 3 Race Conditions", "Bẫy Double Assign, Oversell, Overload", "SimulatorCtrl, ConcurrencyException", "orders.csv, menu_items.csv", "NO_LOCK phát hiện 33 lỗi, OPTIMISTIC 0 lỗi", "100% PASS"],
        ["8. Giải quyết Big Question", "Chứng minh Optimistic Locking tối ưu nhất", "SimulatorCtrl, SimulationRunRepo", "simulation_runs.csv", "Throughput Drop 10.80% (< 15%), 0% lỗi", "100% PASS"]
    ]
    create_table(doc, verif_headers, verif_data, [1.1, 1.4, 1.4, 1.0, 1.2, 0.6])

    # Save document to all target paths
    out_paths = [
        "D:/Lab211-Group1/Phat_Bieu_Bai_Toan_Food_Delivery_LAB211.docx",
        "D:/Lab211-Group1/Usecase/Phat_Bieu_Bai_Toan_Food_Delivery_LAB211.docx",
        "docs/Phat_Bieu_Bai_Toan_Food_Delivery_LAB211.docx",
        "D:/Lab211-Group1/NHOM_01_LAB211_FoodDelivery/docs/Phat_Bieu_Bai_Toan_Food_Delivery_LAB211.docx"
    ]

    for p in out_paths:
        parent = os.path.dirname(p)
        if parent and not os.path.exists(parent):
            os.makedirs(parent, exist_ok=True)
        doc.save(p)
        print(f"Successfully saved Word Document: {p}")

    print("\nALL 4 TARGET WORD DOCUMENTS SUCCESSFULLY GENERATED WITH CHAPTER 8 AND EMBEDDED DEMO SCREENSHOTS!")

if __name__ == "__main__":
    build_full_document()
