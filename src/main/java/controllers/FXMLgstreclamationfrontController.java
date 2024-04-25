package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceReclamation;
import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FXMLgstreclamationfrontController implements Initializable {

    @FXML
    private AnchorPane anchore;

    @FXML
    private ListView<Reclamation> lvtype;

    @FXML
    private TextField tfTitre;

    @FXML
    private ComboBox<String> cbType;

    @FXML
    private TextField tfDesc;

    private final ServiceReclamation str = new ServiceReclamation();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set custom cell factory to style list items
        lvtype.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reclamation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    // Create VBox to hold item details
                    VBox vbox = new VBox();
                    Label titreLabel = new Label("Titre: " + item.getTitre());
                    Label typeLabel = new Label("Type: " + item.getType());
                    Label descLabel = new Label("Description: " + item.getDesc());
                    Label etatLabel = new Label("Etat: " + item.isEtat());

                    // Add labels to VBox
                    vbox.getChildren().addAll(titreLabel, typeLabel, descLabel,etatLabel);

                    // Apply CSS styles
                    vbox.getStyleClass().add("list-item-vbox");
                    titreLabel.getStyleClass().add("list-item-label");
                    typeLabel.getStyleClass().add("list-item-label");
                    descLabel.getStyleClass().add("list-item-label");
                    etatLabel.getStyleClass().add("list-item-label");

                    // Set VBox as the graphic of the list cell
                    setGraphic(vbox);
                }
            }
        });

        displayData();

        // Set listener for selection change
        lvtype.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Populate fields with selected item data
                tfTitre.setText(newValue.getTitre());
                cbType.setValue(newValue.getType());
                tfDesc.setText(newValue.getDesc());
            }
        });
    }

    @FXML
    private void modifier(ActionEvent event) {
        Reclamation selectedReclamation = lvtype.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Update the selected reclamation
            selectedReclamation.setTitre(tfTitre.getText());
            String selectedType = cbType.getValue(); // Use getValue() for ComboBox
            selectedReclamation.setType(selectedType);
            selectedReclamation.setDesc(tfDesc.getText());
            str.modifier(selectedReclamation);

            // Display confirmation alert
            showAlert("Modification", "La réclamation a été modifiée avec succès.");

            // Refresh the display
            displayData();
        }
    }

    @FXML
    private void supprimer(ActionEvent event) {
        Reclamation selectedReclamation = lvtype.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Remove selected reclamation from the list
            lvtype.getItems().remove(selectedReclamation);
            str.supprimer(selectedReclamation.getId());

            // Display confirmation alert
            showAlert("Suppression", "La réclamation a été supprimée avec succès.");

            // Refresh the display
            displayData();
        }
    }

    @FXML
    private void GoToAdd(ActionEvent event) {

        Stage stageclose=(Stage)((Node)event.getSource()).getScene().getWindow();
        stageclose.close();
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/FXMLreclamationfront.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage=new Stage();
            primaryStage.setTitle("Reclamation!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(com.example.gestion_reclamation.Reclamation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayData() {
        ObservableList<Reclamation> dataList = FXCollections.observableArrayList(str.getAll());
        lvtype.setItems(dataList);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
