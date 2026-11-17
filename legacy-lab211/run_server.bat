@echo off
chcp 65001 > nul
set "JAVA=C:\Program Files\Java\jdk1.8.0_202\bin\java.exe"
"%JAVA%" -Dfile.encoding=UTF-8 -cp "d:\NHOM_01_LAB211_FoodDelivery\bin" web.FoodDeliveryWebServer 8080
