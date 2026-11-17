@echo off
chcp 65001 >nul
title Food Delivery Quy Nhon - Fullstack Web & App

echo =====================================================================
echo    FOOD DELIVERY NETWORK SIMULATION - FULLSTACK WEB & MOBILE APP
echo       Hệ Thống Đặt Món & Điều Phối Giao Hàng Thực Tế TP Quy Nhơn
echo =====================================================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
set PATH=%JAVA_HOME%\bin;%PATH%

if not exist bin mkdir bin

echo [1/3] Đang biên dịch mã nguồn Java (src -> bin)...
javac -encoding UTF-8 -d bin -cp src src\web\FoodDeliveryWebServer.java src\controller\*.java src\model\*.java src\util\*.java src\service\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [LỖI] Quá trình biên dịch thất bại! Vui lòng kiểm tra lỗi bên trên.
    pause
    exit /b 1
)

echo [2/3] Biên dịch thành công!
echo [3/3] Đang khởi chạy máy chủ Web HTTP Server tại http://localhost:8080 ...
echo.
echo =====================================================================
echo    MÁY CHỦ SẴN SÀNG!
echo    - Truy cập Trình duyệt Web / Ứng dụng PWA: http://localhost:8080
echo    - Bấm Ctrl + C để dừng máy chủ
echo =====================================================================
echo.

start http://localhost:8080

java -Dfile.encoding=UTF-8 -cp bin web.FoodDeliveryWebServer

pause
