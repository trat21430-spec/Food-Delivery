@echo off
chcp 65001 >nul
echo [INFO] Dang bien dich SystemVerificationTest...
if not exist "bin" mkdir "bin"
"C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe" -encoding UTF-8 -d bin -sourcepath src src/util/SystemVerificationTest.java
if %errorlevel% neq 0 (
    echo [ERROR] Bien dich that bai!
    pause
    exit /b %errorlevel%
)
echo [INFO] Chay kiem thu he thong (SystemVerificationTest)...
"C:\Program Files\Java\jdk1.8.0_202\bin\java.exe" -Dfile.encoding=UTF-8 -cp bin util.SystemVerificationTest
pause
