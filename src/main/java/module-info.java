module com.macondo.mayhemarena {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.macondo.mayhemarena to javafx.fxml, com.almasb.fxgl.all;
    opens com.macondo.mayhemarena.game to com.almasb.fxgl.all;
    exports com.macondo.mayhemarena;
    exports com.macondo.mayhemarena.config;
    exports com.macondo.mayhemarena.entity;
}
