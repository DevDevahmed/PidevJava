module com.example.gestion_reclamation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires twilio;
    requires org.apache.pdfbox;
    opens com.example.gestion_reclamation to javafx.fxml;
    opens controllers to javafx.fxml;
    opens entities to javafx.base; // Open the entities package to javafx.base

    exports com.example.gestion_reclamation;
    exports controllers;

}