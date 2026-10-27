@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
echo Compiling MVC sources...
javac -encoding UTF-8 -d bin src\model\*.java src\exception\*.java src\repository\*.java src\controller\*.java src\view\*.java src\Main.java src\DataGenerator.java
if errorlevel 1 (
  echo COMPILE FAILED
  exit /b 1
)
echo Compile OK. Starting Main...
java -cp bin Main
pause
