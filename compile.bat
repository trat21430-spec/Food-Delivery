@echo off
chcp 65001 > nul
set "JAVAC=C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
if not exist "bin" mkdir bin
dir /s /b src\*.java > sources.txt
"%JAVAC%" -encoding UTF-8 -d bin @sources.txt
if %ERRORLEVEL% equ 0 (
    echo BUILD SUCCESSFUL
) else (
    echo BUILD FAILED with %ERRORLEVEL%
)
