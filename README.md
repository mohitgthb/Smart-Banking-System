# Smart-Banking-System

## Compile and Run on Windows

Run these commands from the project root in Command Prompt:

```cmd
if not exist out mkdir out

javac --module-path "C:\Users\mohit\Downloads\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml -cp "lib\mysql-connector-j-8.3.0.jar" -d out @sources.txt
```

Start the JavaFX application:

```cmd
java --module-path "C:\Users\mohit\Downloads\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml -cp "out;.;lib\mysql-connector-j-8.3.0.jar" ui.MainApp
```

The `.;` in the classpath includes the project root so the FXML files can be loaded.
