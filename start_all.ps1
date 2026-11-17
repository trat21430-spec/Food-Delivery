<#
.SYNOPSIS
    One-click runner for all Food Delivery Multi-Vendor modules
#>
Write-Host "===============================================================================" -ForegroundColor Cyan
Write-Host "           KHỞI CHẠY ĐỒNG LOẠT HỆ THỐNG FOOD DELIVERY MULTI-VENDOR            " -ForegroundColor Yellow
Write-Host "===============================================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "[1] Chạy Web Khách Hàng (:3000) + Admin Dashboard (:3001) - (Khuyên dùng)" -ForegroundColor Green
Write-Host "[2] Chạy Toàn Bộ 5 Phân Hệ (Web Khách, Admin, Store App, Rider App, Mobile App)" -ForegroundColor Green
Write-Host "[3] Cài đặt dependencies cho tất cả phân hệ (npm install)" -ForegroundColor Green
Write-Host ""

$choice = Read-Host "Nhập lựa chọn của bạn [1, 2 hoặc 3, mặc định là 1]"
if ([string]::IsNullOrWhiteSpace($choice)) { $choice = "1" }

$root = $PSScriptRoot

switch ($choice) {
    "1" {
        Write-Host "`n[*] Đang khởi động Web Khách Hàng (Port 3000)..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-web'; npm run dev -- -p 3000"
        
        Write-Host "[*] Đang khởi động Admin Dashboard (Port 3001)..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-admin'; npm run dev -- -p 3001"
        
        Start-Sleep -Seconds 4
        Start-Process "http://localhost:3000"
        Start-Process "http://localhost:3001"
        Write-Host "`n[V] Đã mở trình duyệt vào Web Khách (:3000) và Admin (:3001)!" -ForegroundColor Green
    }
    "2" {
        Write-Host "`n[*] Đang khởi động 1/5: Web Khách Hàng (Port 3000)..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-web'; npm run dev -- -p 3000"

        Write-Host "[*] Đang khởi động 2/5: Admin Dashboard (Port 3001)..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-admin'; npm run dev -- -p 3001"

        Write-Host "[*] Đang khởi động 3/5: App Quán Ăn (Store)..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-store'; npx expo start"

        Write-Host "[*] Đang khởi động 4/5: App Tài Xế (Rider)..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-rider'; npx expo start"

        Write-Host "[*] Đang khởi động 5/5: App Khách Hàng Mobile..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\enatega-multivendor-app'; npx expo start"

        Start-Sleep -Seconds 4
        Start-Process "http://localhost:3000"
        Start-Process "http://localhost:3001"
        Write-Host "`n[V] Đã mở toàn bộ 5 phân hệ cùng lúc!" -ForegroundColor Green
    }
    "3" {
        Write-Host "`n[*] Đang cài đặt dependencies cho tất cả các phân hệ..." -ForegroundColor Yellow
        $folders = @("enatega-multivendor-web", "enatega-multivendor-admin", "enatega-multivendor-store", "enatega-multivendor-rider", "enatega-multivendor-app")
        foreach ($f in $folders) {
            Write-Host "`n--> Cài đặt $f..." -ForegroundColor Cyan
            Push-Location "$root\$f"
            npm install --legacy-peer-deps
            Pop-Location
        }
        Write-Host "`n[V] Cài đặt hoàn tất!" -ForegroundColor Green
    }
}
