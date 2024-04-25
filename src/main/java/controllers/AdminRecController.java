package controllers;

import entities.Reclamation;
import entities.Reponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import services.ServiceReclamation;
import services.ServiceRepons;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminRecController implements Initializable {

    @FXML
    private ListView<Reclamation> lvReclamations;
    @FXML
    private Label lblTitre;
    @FXML
    private Label lblType;
    @FXML
    private Label lblDesc;
    @FXML
    private TextArea responseTextArea;
    private final ServiceReclamation str = new ServiceReclamation();
    private final ServiceRepons serviceReponse = new ServiceRepons();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set custom cell factory to style list items
        lvReclamations.setCellFactory(param -> new ListCell<>() {
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
        lvReclamations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Populate fields with selected item data
                lblTitre.setText(newValue.getTitre());
                lblType.setText(newValue.getType());
                lblDesc.setText(newValue.getDesc());


            }
        });
    }

    @FXML
    private void repondreButtonClicked() {
        Reclamation selectedReclamation = lvReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            String response = responseTextArea.getText();
            // Check if response field is empty
            if (response.isEmpty()) {
                showAlert("Réponse vide", "Veuillez saisir une réponse avant de valider.");
            } else {
                // Create a new response object
                Reponse reponse = new Reponse(1,selectedReclamation.getId(), response,1);

                // Add the response
                serviceReponse.ajouter(reponse);

                // Update the status of the reclamation to "en cours"
                selectedReclamation.setEtat("Done");
                str.modifier(selectedReclamation);

                // Display confirmation alert
                showAlert("Réponse ajoutée", "La réponse a été ajoutée avec succès.");

                // Clear response text area
                responseTextArea.clear();

                // Refresh the display
                displayData();
            }
        } else {
            showAlert("Sélectionner une réclamation", "Veuillez sélectionner une réclamation avant de répondre.");
        }
    }

    @FXML
    private void supprimer(ActionEvent event) {
        Reclamation selectedReclamation = lvReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Remove selected reclamation from the list
            lvReclamations.getItems().remove(selectedReclamation);
            str.supprimer(selectedReclamation.getId());

            // Display confirmation alert
            showAlert("Suppression", "La réclamation a été supprimée avec succès.");

            // Refresh the display
            displayData();
        }
    }
    private void displayData() {
        ObservableList<Reclamation> dataList = FXCollections.observableArrayList(str.getAll());
        lvReclamations.setItems(dataList);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Other methods for handling actions like supprimer and GoToAdd
}
