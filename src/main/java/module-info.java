module scheduleapp.scheduleapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens scheduleapp.scheduleapp to javafx.fxml;
    exports scheduleapp.scheduleapp;
}