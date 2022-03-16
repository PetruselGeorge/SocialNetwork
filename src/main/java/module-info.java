module com.example.georgel {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    opens com.example.georgel to javafx.fxml;
    exports com.example.georgel;
    exports com.example.controller;
    opens com.example.controller to javafx.fxml;
    opens com.example.domain to javafx.fxml;
    exports com.example.domain;
}