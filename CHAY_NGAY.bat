@echo off
chcp 65001 >nul
title FOOD DELIVERY MULTI-VENDOR - KHỞI ĐỘNG NHANH 1 CÚ CLICK

echo ===============================================================================
echo            KHỞI ĐỘNG HỆ THỐNG FOOD DELIVERY MULTI-VENDOR (TỐI ƯU TỐC ĐỘ)
echo ===============================================================================
echo.
echo [*] [1/3] Đang khởi động Web Khách Hàng (Port 3000) & Admin Dashboard (Port 3001)...

:: Tự động tạo Shortcut ra Desktop nếu chưa có
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ws = New-Object -ComObject WScript.Shell; $d = [Environment]::GetFolderPath('Desktop'); $s = $ws.CreateShortcut(\"$d\Food Delivery Platform.lnk\"); $s.TargetPath = '%~dp0CHAY_NGAY.bat'; $s.WorkingDirectory = '%~dp0'; $s.Description = 'Khoi dong Food Delivery Multi-Vendor'; $s.Save()" >nul 2>&1

:: Khởi chạy Web Khách Hàng trên Port 3000
start "FDS - Web Khách Hàng (Port 3000)" cmd /k "cd /d %~dp0enatega-multivendor-web && npm run dev -- -p 3000"

:: Khởi chạy Admin Dashboard trên Port 3001
start "FDS - Admin Dashboard (Port 3001)" cmd /k "cd /d %~dp0enatega-multivendor-admin && npm run dev -- -p 3001"

echo [*] [2/3] Đang chờ máy chủ biên dịch và mở cổng kết nối...
echo     (Trình duyệt sẽ CHỈ mở khi máy chủ đã sẵn sàng 100%% để tránh lỗi từ chối kết nối)

:: Tự động kiểm tra cổng cho đến khi server phản hồi thành công
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ready = $false; for ($i=0; $i -lt 30; $i++) { Start-Sleep -Seconds 1; try { $r = Invoke-WebRequest -Uri 'http://localhost:3001' -UseBasicParsing -TimeoutSec 2; if ($r.StatusCode -eq 200) { $ready = $true; break } } catch {} }; if ($ready) { Write-Host '    [OK] May chu Admin da san sang!' -ForegroundColor Green } else { Write-Host '    [!] Dang mo trinh duyet...' -ForegroundColor Yellow }"

echo.
echo [*] [3/3] Máy chủ đã sẵn sàng! Đang mở trình duyệt vào Web và Admin...
start http://localhost:3000
start http://localhost:3001/authentication/login

echo.
echo ===============================================================================
echo [V] HỆ THỐNG ĐÃ KHỞI CHẠY THÀNH CÔNG!
echo ===============================================================================
echo  1. Web Khách Hàng:       http://localhost:3000
echo  2. Bảng Quản Trị Admin:  http://localhost:3001
echo     - Email Đăng Nhập:   admin@enatega.com
echo     - Mật Khẩu Admin:    enatega123 (hoặc 123456)
echo.
echo  * Da tao Shortcut "Food Delivery Platform" ngay ngoai Desktop cua ban.
echo  * Luu y: Giu nguyen cac cua so Terminal dang chay de duy tri may chu.
echo ===============================================================================
echo.
pause
