@echo off
chcp 65001 >nul
title FOOD DELIVERY MULTI-VENDOR - KHỞI ĐỘNG TỐC ĐỘ CAO (1 GIÂY)

echo ===============================================================================
echo            KHỞI ĐỘNG HỆ THỐNG FOOD DELIVERY MULTI-VENDOR (1-CLICK)
echo ===============================================================================
echo.

:: Tạo Shortcut Desktop nếu chưa có
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ws = New-Object -ComObject WScript.Shell; $d = [Environment]::GetFolderPath('Desktop'); $s = $ws.CreateShortcut(\"$d\Food Delivery Platform.lnk\"); $s.TargetPath = '%~dp0CHAY_NGAY.bat'; $s.WorkingDirectory = '%~dp0'; $s.Description = 'Khoi dong Food Delivery Multi-Vendor'; $s.Save()" >nul 2>&1

:: Kiểm tra xem Port 3000 và 3001 đã chạy chưa
powershell -NoProfile -ExecutionPolicy Bypass -Command "exit ((Get-NetTCPConnection -LocalPort 3000 -State Listen -ErrorAction SilentlyContinue | Measure-Object).Count)"
set PORT_3000_RUNNING=%ERRORLEVEL%

powershell -NoProfile -ExecutionPolicy Bypass -Command "exit ((Get-NetTCPConnection -LocalPort 3001 -State Listen -ErrorAction SilentlyContinue | Measure-Object).Count)"
set PORT_3001_RUNNING=%ERRORLEVEL%

if %PORT_3000_RUNNING% NEQ 0 (
    echo [*] Web Khách Hàng (Port 3000) đã sẵn sàng.
) else (
    echo [*] Đang khởi động Web Khách Hàng (Port 3000)...
    start "FDS - Web Khách Hàng (Port 3000)" /min cmd /k "cd /d %~dp0enatega-multivendor-web && npx next start -p 3000"
)

if %PORT_3001_RUNNING% NEQ 0 (
    echo [*] Admin Dashboard (Port 3001) đã sẵn sàng.
) else (
    echo [*] Đang khởi động Admin Dashboard (Port 3001)...
    start "FDS - Admin Dashboard (Port 3001)" /min cmd /k "cd /d %~dp0enatega-multivendor-admin && npx next start -p 3001"
)

echo.
echo [*] Đang mở trình duyệt vào Web Khách Hàng và Admin Dashboard...
timeout /t 2 /nobreak >nul
start http://localhost:3000
start http://localhost:3001/authentication/login

echo.
echo ===============================================================================
echo [V] HỆ THỐNG ĐÃ KHỞI CHẠY THÀNH CÔNG!
echo ===============================================================================
echo  1. Web Khách Hàng:       http://localhost:3000
echo  2. Bảng Quản Trị Admin:  http://localhost:3001/authentication/login
echo     - Email Đăng Nhập:    admin@enatega.com
echo     - Mật Khẩu Admin:     enatega123 (hoặc 123456)
echo.
echo  * Cac cua so Terminal may chu da duoc tu dong thu nho (minimize) xuong Taskbar.
echo  * Khi muon tat he thong, chi can chay file DUNG_SERVER.bat
echo ===============================================================================
echo.
pause
