@echo off
chcp 65001 >nul
echo [INFO] Dang bien dich Food Delivery Network Simulation...
if not exist "bin" mkdir "bin"
<<<<<<< HEAD
"C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe" -encoding UTF-8 -d bin -sourcepath src src/Main.java
=======

set JAVAC_CMD="C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
set JAVA_CMD="C:\Program Files\Java\jdk1.8.0_202\bin\java.exe"

if not exist %JAVAC_CMD% (
    set JAVAC_CMD=javac
    set JAVA_CMD=java
)

%JAVAC_CMD% -encoding UTF-8 -d bin -sourcepath src src/Main.java
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
if %errorlevel% neq 0 (
    echo [ERROR] Bien dich that bai! Vui long kiem tra lai ma nguon.
    pause
    exit /b %errorlevel%
)
echo [INFO] Khoi dong chuong trinh...
<<<<<<< HEAD
"C:\Program Files\Java\jdk1.8.0_202\bin\java.exe" -Dfile.encoding=UTF-8 -cp bin Main
=======
%JAVA_CMD% -Dfile.encoding=UTF-8 -cp bin Main
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
pause
