module com.melocode.tunvistaaaa_j {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    //requires mysql.connector.java;
    requires java.sql;
   // requires org.apache.opennlp.tools;


    opens com.melocode.tunvistaaaa_j to javafx.fxml;
    exports com.melocode.tunvistaaaa_j;
}