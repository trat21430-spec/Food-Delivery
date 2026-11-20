$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# Tao shortcut ra desktop neu chua co
try {
    $ws = New-Object -ComObject WScript.Shell
    $d = [Environment]::GetFolderPath('Desktop')
    $s = $ws.CreateShortcut("$d\Food Delivery (1-Click).lnk")
    $s.TargetPath = "$scriptDir\CHAY_AN_KHONG_HIEN_TERMINAL.vbs"
    $s.WorkingDirectory = "$scriptDir"
    $s.Description = "Khoi dong Food Delivery Platform"
    $s.Save()
} catch {}

# Kiem tra Port 3000
$p3000 = Get-NetTCPConnection -LocalPort 3000 -State Listen -ErrorAction SilentlyContinue
if (-not $p3000) {
    Start-Process -FilePath "cmd.exe" -ArgumentList "/c npx next start -p 3000" -WorkingDirectory "$scriptDir\enatega-multivendor-web" -WindowStyle Hidden
}

# Kiem tra Port 3001
$p3001 = Get-NetTCPConnection -LocalPort 3001 -State Listen -ErrorAction SilentlyContinue
if (-not $p3001) {
    Start-Process -FilePath "cmd.exe" -ArgumentList "/c npx next start -p 3001" -WorkingDirectory "$scriptDir\enatega-multivendor-admin" -WindowStyle Hidden
}

Start-Sleep -Seconds 2
Start-Process "http://localhost:3000"
Start-Process "http://localhost:3001/authentication/login"
