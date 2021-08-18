@echo off
del /s /q *.class 2>NUL
javac.exe -d ./bin --module-path ./lib_windows/ --add-modules=javafx.controls,javafx.fxml --source-path ./src %*
