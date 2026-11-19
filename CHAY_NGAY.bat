@echo off
chcp 65001 >nul
title FOOD DELIVERY MULTI-VENDOR - KHỞI ĐỘNG HỆ THỐNG

echo ===============================================================================
echo            KHỞI ĐỘNG HỆ THỐNG FOOD DELIVERY MULTI-VENDOR
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
    echo [*] Web Khách Hàng (Port 3000) đã đang chạy sẵn trong hệ thống.
) else (
    echo [*] Đang khởi động Web Khách Hàng (Port 3000)...
    start "FDS - Web Khách Hàng (Port 3000)" cmd /k "cd /d %~dp0enatega-multivendor-web && npm run dev -- -p 3000"
)

if %PORT_3001_RUNNING% NEQ 0 (
    echo [*] Admin Dashboard (Port 3001) đã đang chạy sẵn trong hệ thống.
) else (
    echo [*] Đang khởi động Admin Dashboard (Port 3001)...
    start "FDS - Admin Dashboard (Port 3001)" cmd /k "cd /d %~dp0enatega-multivendor-admin && npm run dev -- -p 3001"
)

echo.
echo [*] Đang kiểm tra trạng thái máy chủ (sẽ tự động mở trình duyệt khi sẵn sàng 100%%)...

:: Chờ tối đa 90 giây cho lần đầu tiên biên dịch (JIT compile)
powershell -NoProfile -ExecutionPolicy Bypass -Command "& { $wOk = $false; $aOk = $false; for ($i=1; $i -le 90; $i++) { if (-not $aOk) { try { $r = Invoke-WebRequest -Uri 'http://localhost:3001' -UseBasicParsing -TimeoutSec 2; if ($r.StatusCode -eq 200) { $aOk = $true } } catch {} }; if (-not $wOk) { try { $r = Invoke-WebRequest -Uri 'http://localhost:3000' -UseBasicParsing -TimeoutSec 2; if ($r.StatusCode -eq 200) { $wOk = $true } } catch {} }; if ($aOk -and $wOk) { break }; Write-Host ('   > Dang cho may chu bien dich giay thu ' + $i + '/90 (Admin: ' + ($aOk ? 'San sang' : 'Dang nap...') + ' | Web: ' + ($wOk ? 'San sang' : 'Dang nap...') + ')') -ForegroundColor Cyan; Start-Sleep -Seconds 1 }; if ($aOk -and $wOk) { Write-Host '   [OK] Tat ca may chu da san sang 100%!' -ForegroundColor Green } else { Write-Host '   [!] Hoan tat, dang mo trinh duyet...' -ForegroundColor Yellow } }"

echo.
echo [*] Đang mở trình duyệt vào Web Khách Hàng và Admin Dashboard...
start http://localhost:3000
start http://localhost:3001/authentication/login

echo.
echo ===============================================================================
echo [V] HỆ THỐNG ĐÃ SẴN SÀNG SỬ DỤNG!
echo ===============================================================================
echo  1. Web Khách Hàng:       http://localhost:3000
echo  2. Bảng Quản Trị Admin:  http://localhost:3001/authentication/login
echo     - Email Đăng Nhập:    admin@enatega.com
echo     - Mật Khẩu Admin:     enatega123 (hoặc 123456)
echo.
echo  * Mẹo: Nếu trang hiển thị chậm trong lần đầu, chỉ cần bấm F5 (Refresh).
echo ===============================================================================
echo.
pause
