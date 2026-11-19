@echo off
chcp 65001 >nul
title DỪNG TẤT CẢ SERVER FOOD DELIVERY

echo ===============================================================================
echo            ĐANG DỪNG TẤT CẢ SERVER FOOD DELIVERY (PORT 3000 & 3001)
echo ===============================================================================
echo.

powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-NetTCPConnection -LocalPort 3000, 3001 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue; Write-Host ('   [X] Da tat tien trinh PID: ' + $_) -ForegroundColor Red }"

echo.
echo [V] Đã giải phóng hoàn tất cổng 3000 và 3001!
echo.
pause
