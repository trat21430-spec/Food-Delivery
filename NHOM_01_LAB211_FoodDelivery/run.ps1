<<<<<<< HEAD
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_202"
$java = "$env:JAVA_HOME\bin\java.exe"
$javac = "$env:JAVA_HOME\bin\javac.exe"
=======
$javac = "javac"
$java = "java"
if (Test-Path "C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe") {
    $javac = "C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
    $java = "C:\Program Files\Java\jdk1.8.0_202\bin\java.exe"
}
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
if (-not (Test-Path "bin")) { 
    New-Item -ItemType Directory -Path "bin" -Force | Out-Null 
}

<<<<<<< HEAD
Write-Host "[INFO] Dang bien dich du an bang JDK 1.8..." -ForegroundColor Cyan
=======
Write-Host "[INFO] Dang bien dich du an..." -ForegroundColor Cyan
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
& $javac -encoding UTF-8 -d bin -sourcepath src src/Main.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "[INFO] Khoi dong ung dung Food Delivery tren Terminal..." -ForegroundColor Green
    & $java "-Dfile.encoding=UTF-8" -cp bin Main
} else {
    Write-Host "[ERROR] Bien dich that bai! Vui long kiem tra lai ma nguon." -ForegroundColor Red
}
