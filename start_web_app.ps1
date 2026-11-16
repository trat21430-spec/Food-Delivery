# Start script for Food Delivery Web Server & PWA
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "   FOOD DELIVERY NETWORK SIMULATION - FULLSTACK WEB & MOBILE APP" -ForegroundColor Yellow
Write-Host "      Hệ Thống Đặt Món & Điều Phối Giao Hàng Thực Tế TP Quy Nhơn" -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""

$javac = "C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
$java = "C:\Program Files\Java\jdk1.8.0_202\bin\java.exe"

if (-not (Test-Path $javac)) {
    $javac = "javac"
    $java = "java"
}

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "[1/3] Đang biên dịch mã nguồn Java (src -> bin)..." -ForegroundColor Yellow
$javaFiles = (Get-ChildItem -Path src -Recurse -Filter "*.java").FullName
& $javac -encoding UTF-8 -d bin -cp "src" $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Host "[LỖI] Quá trình biên dịch thất bại!" -ForegroundColor Red
    exit 1
}

Write-Host "[2/3] Biên dịch thành công!" -ForegroundColor Green
Write-Host "[3/3] Đang khởi chạy máy chủ Web HTTP Server tại http://localhost:8080 ..." -ForegroundColor Cyan
Write-Host ""
Write-Host "=====================================================================" -ForegroundColor Magenta
Write-Host "   MÁY CHỦ SẴN SÀNG! Đang tự động mở trình duyệt..." -ForegroundColor White
Write-Host "   Truy cập Web & PWA: http://localhost:8080" -ForegroundColor Yellow
Write-Host "   Bấm Ctrl + C để dừng máy chủ" -ForegroundColor DarkGray
Write-Host "=====================================================================" -ForegroundColor Magenta
Write-Host ""

Start-Process "http://localhost:8080"

& $java -Dfile.encoding=UTF-8 -cp bin web.FoodDeliveryWebServer
