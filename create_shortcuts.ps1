$ws = New-Object -ComObject WScript.Shell
$desktopPaths = @(
    [Environment]::GetFolderPath('Desktop'),
    "$env:USERPROFILE\Desktop",
    "$env:USERPROFILE\OneDrive\Máy tính",
    "$env:USERPROFILE\OneDrive\Desktop"
) | Where-Object { $_ -ne $null -and (Test-Path $_) } | Select-Object -Unique

foreach ($p in $desktopPaths) {
    # 1. Shortcut Khoi Dong (1-Click)
    $startLnk = Join-Path $p "Food Delivery Platform.lnk"
    $s1 = $ws.CreateShortcut($startLnk)
    $s1.TargetPath = "d:\NHOM_01_LAB211_FoodDelivery\CHAY_AN_KHONG_HIEN_TERMINAL.vbs"
    $s1.WorkingDirectory = "d:\NHOM_01_LAB211_FoodDelivery"
    $s1.Description = "Khoi dong Food Delivery 1-Click"
    $s1.Save()

    # 2. Shortcut Tat Server
    $stopLnk = Join-Path $p "Tat Server Food Delivery.lnk"
    $s2 = $ws.CreateShortcut($stopLnk)
    $s2.TargetPath = "d:\NHOM_01_LAB211_FoodDelivery\DUNG_SERVER.bat"
    $s2.WorkingDirectory = "d:\NHOM_01_LAB211_FoodDelivery"
    $s2.Description = "Tat may chu Food Delivery"
    $s2.Save()

    Write-Output "Shortcuts created in: $p"
}
