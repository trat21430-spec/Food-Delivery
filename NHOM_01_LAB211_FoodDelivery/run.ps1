[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "[INFO] Dang bien dich Food Delivery Network Simulation..." -ForegroundColor Cyan
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }

$sources = Get-ChildItem -Path src -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
$javacPath = "C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
if (-not (Test-Path $javacPath)) {
    $javacPath = "javac"
}
& $javacPath -encoding UTF-8 -d bin -sourcepath src $sources
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Bien dich that bai!" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "[INFO] Khoi dong chuong trinh..." -ForegroundColor Green
$javaPath = "C:\Program Files\Java\jre1.8.0_511\bin\java.exe"
if (-not (Test-Path $javaPath)) {
    $javaPath = "java"
}
& $javaPath -cp bin Main

