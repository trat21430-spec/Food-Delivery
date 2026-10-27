@echo off
chcp 65001 >nul
echo [INFO] Dang bien dich SystemVerificationTest...
if not exist "bin" mkdir "bin"

set JAVAC_CMD="C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
set JAVA_CMD="C:\Program Files\Java\jdk1.8.0_202\bin\java.exe"

if not exist %JAVAC_CMD% (
    set JAVAC_CMD=javac
    set JAVA_CMD=java
)

%JAVAC_CMD% -encoding UTF-8 -d bin -sourcepath src src/util/SystemVerificationTest.java
if %errorlevel% neq 0 (
    echo [ERROR] Bien dich that bai!
    pause
    exit /b %errorlevel%
)
echo [INFO] Chay kiem thu he thong (SystemVerificationTest)...
%JAVA_CMD% -Dfile.encoding=UTF-8 -cp bin util.SystemVerificationTest
pause
