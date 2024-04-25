package controllers;
import com.example.gestion_reclamation.Reclamation;
import entities.Statut;
import javafx.scene.control.ComboBox;
import services.ServiceReclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLreclamationfrontController implements Initializable {

    @FXML
    private AnchorPane anchore;
    @FXML
    private TextField tftitre;
    @FXML
    private TextArea tadesc;
    @FXML
    private ImageView img;
    @FXML
    private ComboBox<String> cbType; // Make sure to specify the type for ComboBox


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate the ComboBox
        // Add your actual types here
    }

    @FXML
    private void ajouter(ActionEvent event) {
        String saisieErreur = controleDeSaisie();

        if (saisieErreur.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur ajout reclamation");
            alert.setContentText(saisieErreur);
            alert.show();
        } else if (tftitre.getText().trim().length() < 6) { // Check if title length is less than 6
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur ajout reclamation");
            alert.setContentText("Le titre doit contenir au moins 6 lettres.");
            alert.show();
        } else {
            entities.Reclamation r = new entities.Reclamation();
            r.setTitre(tftitre.getText());
            r.setDesc(tadesc.getText());
            String selectedType = cbType.getValue(); // Use getValue() for ComboBox
            r.setType(selectedType);
            r.setEtat("NON_TRAITE");
            r.setId_user(1);

            ServiceReclamation sr = new ServiceReclamation();
            sr.ajouter(r);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reclamation");
            alert.setContentText("ajout reclamation avec succes");
            alert.showAndWait();
            //sms();
            tftitre.clear();
            tadesc.clear();
        }
    }


    public String controleDeSaisie() {
        String erreur = "";
        if (tftitre.getText().trim().isEmpty()) {
            erreur += "Titre vide!\n";
        }
        if (cbType.getValue() == null) { // Check if ComboBox value is null
            erreur += "Type vide!\n";
        }

        if (tadesc.getText().trim().isEmpty()) {
            erreur += "Description vide!\n";
        }
        return erreur;
    }

    @FXML
    private void gstReclamation(ActionEvent event) {
        Stage stageclose = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageclose.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLgstreclamationfront.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Reclamation!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Reclamation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void goadmin(ActionEvent event) {
        Stage stageclose = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageclose.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AdminRec.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Reclamation!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Reclamation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
