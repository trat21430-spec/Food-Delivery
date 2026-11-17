@echo off
setlocal
chcp 65001 >nul
echo [INFO] Dang bien dich Food Delivery Network Simulation...
if not exist "bin" mkdir "bin"
dir /s /b src\*.java > sources.txt

set JAVAC_CMD="C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
if not exist %JAVAC_CMD% set JAVAC_CMD=javac

%JAVAC_CMD% -encoding UTF-8 -d bin -sourcepath src @sources.txt
if exist sources.txt del sources.txt

if %errorlevel% neq 0 (
    echo [ERROR] Bien dich that bai! Vui long kiem tra lai ma nguon.
    pause
    exit /b %errorlevel%
)

echo [INFO] Khoi dong chuong trinh...
set JAVA_CMD="C:\Program Files\Java\jre1.8.0_511\bin\java.exe"
if not exist %JAVA_CMD% set JAVA_CMD=java

%JAVA_CMD% -cp bin Main
endlocal
