module una.sistema.backend.proyecto2sistemadespachobackend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.logging;
    requires javafx.graphics;
    requires com.google.gson;

    // Opens para JavaFX
    opens una.sistema.backend.proyecto2sistemadespachobackend.model to javafx.fxml, com.google.gson;
    opens una.sistema.backend.proyecto2sistemadespachobackend.servicios to com.google.gson;


    // Exports
    exports una.sistema.backend.proyecto2sistemadespachobackend.model;
    exports una.sistema.backend.proyecto2sistemadespachobackend.servicios;
}