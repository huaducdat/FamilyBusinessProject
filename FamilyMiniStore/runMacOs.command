#!/bin/bash

cd "$(dirname "$0")"

java \
--module-path "./javafx-sdk-mac/lib" \
--add-modules javafx.controls,javafx.fxml \
-jar control-app.jar

echo ""
echo "Press ENTER to exit..."

read