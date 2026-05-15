@echo off

cd /d "%~dp0"

java ^
--module-path "javafx-sdk-win\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-jar control-app.jar

pause