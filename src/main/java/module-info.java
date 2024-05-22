module com.example.astrahakaton {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.astrahakaton to javafx.fxml;
    exports com.example.astrahakaton;
}