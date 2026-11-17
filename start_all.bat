@echo off
chcp 65001 >nul
title FOOD DELIVERY MULTI-VENDOR - ALL-IN-ONE STARTER

echo ===============================================================================
echo            KHỞI CHẠY ĐỒNG LOẠT HỆ THỐNG FOOD DELIVERY MULTI-VENDOR
echo ===============================================================================
echo.
echo [1] Chạy Web Khách Hàng (Port 3000) + Admin Dashboard (Port 3001)
echo [2] Chạy Toàn Bộ 5 Phân Hệ (Web Khách, Admin, App Quán, App Shipper, App Khách)
echo [3] Chỉ cài đặt toàn bộ thư viện (npm install all)
echo.
set /p choice="Nhập lựa chọn của bạn [1, 2 hoặc 3, mặc định là 1]: "

if "%choice%"=="" set choice=1

if "%choice%"=="3" goto INSTALL_ALL
if "%choice%"=="2" goto RUN_ALL_5
if "%choice%"=="1" goto RUN_CORE_WEB

:RUN_CORE_WEB
echo.
echo [*] Đang khởi động Web Khách Hàng (Port 3000)...
start "FDS - Web Khách Hàng (Port 3000)" cmd /k "cd /d %~dp0enatega-multivendor-web && npm run dev -- -p 3000"

echo [*] Đang khởi động Admin Dashboard (Port 3001)...
start "FDS - Admin Dashboard (Port 3001)" cmd /k "cd /d %~dp0enatega-multivendor-admin && npm run dev -- -p 3001"

echo.
echo [*] Đang mở trình duyệt vào các hệ thống...
timeout /t 5 >nul
start http://localhost:3000
start http://localhost:3001
echo.
echo [V] Hệ thống Web & Admin đã được kích hoạt thành công!
echo     - Web Khách Hàng: http://localhost:3000
echo     - Admin Dashboard: http://localhost:3001
pause
exit /b 0

:RUN_ALL_5
echo.
echo [*] Đang khởi động Cửa sổ 1: Web Khách Hàng (Port 3000)...
start "FDS - Web Khách Hàng (Port 3000)" cmd /k "cd /d %~dp0enatega-multivendor-web && npm run dev -- -p 3000"

echo [*] Đang khởi động Cửa sổ 2: Admin Dashboard (Port 3001)...
start "FDS - Admin Dashboard (Port 3001)" cmd /k "cd /d %~dp0enatega-multivendor-admin && npm run dev -- -p 3001"

echo [*] Đang khởi động Cửa sổ 3: App Quán Ăn (Merchant / Store)...
start "FDS - App Quán Ăn (Store)" cmd /k "cd /d %~dp0enatega-multivendor-store && npx expo start"

echo [*] Đang khởi động Cửa sổ 4: App Tài Xế (Rider / Shipper)...
start "FDS - App Tài Xế (Rider)" cmd /k "cd /d %~dp0enatega-multivendor-rider && npx expo start"

echo [*] Đang khởi động Cửa sổ 5: App Khách Hàng Mobile...
start "FDS - App Khách Hàng Mobile" cmd /k "cd /d %~dp0enatega-multivendor-app && npx expo start"

echo.
echo [*] Đang mở trình duyệt vào Web Khách & Admin...
timeout /t 5 >nul
start http://localhost:3000
start http://localhost:3001
echo.
echo [V] Đã khởi chạy đồng loạt cả 5 phân hệ trong các cửa sổ riêng biệt!
pause
exit /b 0

:INSTALL_ALL
echo.
echo [*] Đang cài đặt dependencies cho Web Khách Hàng...
cd /d %~dp0enatega-multivendor-web && call npm install --legacy-peer-deps
echo [*] Đang cài đặt dependencies cho Admin Dashboard...
cd /d %~dp0enatega-multivendor-admin && call npm install --legacy-peer-deps
echo [*] Đang cài đặt dependencies cho App Quán Ăn...
cd /d %~dp0enatega-multivendor-store && call npm install --legacy-peer-deps
echo [*] Đang cài đặt dependencies cho App Tài Xế...
cd /d %~dp0enatega-multivendor-rider && call npm install --legacy-peer-deps
echo [*] Đang cài đặt dependencies cho App Khách Hàng...
cd /d %~dp0enatega-multivendor-app && call npm install --legacy-peer-deps
echo.
echo [V] Đã hoàn tất cài đặt toàn bộ thư viện!
pause
exit /b 0
