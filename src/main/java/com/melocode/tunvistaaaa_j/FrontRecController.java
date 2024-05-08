package com.melocode.tunvistaaaa_j;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class FrontRecController {

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btn_ajout;

    @FXML
    private Button btn_ann;

    @FXML
    private DatePicker tf_date;

    @FXML
    private TextField tf_des;

    @FXML
    private TextField tf_iduser;
    @FXML
    private BorderPane repp;
    @FXML
    private Button btn_monliste;


    public void loadReclamationControllerFunction() throws IOException {
        // Charger le fichier FXML de ReclamationController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/Reclamation.fxml"));
        Parent parent = loader.load();

        // Obtenir une référence au contrôleur ReclamationController
        ReclamationController reclamationController = loader.getController();

        // Appeler une fonction de ReclamationController
        reclamationController.showReclamation();

        // Remplacez someFunction() par la fonction que vous souhaitez appeler
    }



    @FXML
    void ajouterRec(ActionEvent event) {
        // Get the input values from the text fields
        String description = tf_des.getText();
        String userId = tf_iduser.getText();
        LocalDate localDate = tf_date.getValue();

        // Perform input validation
        if (description.isEmpty() || userId.isEmpty() || localDate == null) {
            // Show an alert if any of the fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }
        // Check if a claim already exists for this user on the current date
        String selectQuery = "SELECT COUNT(*) FROM reclamation WHERE iduser = ? AND date = ?";
        try {
            con = DBConnexion.getConnection();
            PreparedStatement statement = con.prepareStatement(selectQuery);
            statement.setString(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(localDate));

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                // Show an alert if a claim already exists for this user on the current date
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Réclamation déjà ajoutée");
                alert.setContentText("Vous avez déjà ajouté une réclamation pour aujourd'hui.");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // List of forbidden words
        List<String> forbiddenWords = Arrays.asList("raciste", "fuck", "pute");

        // Check for bad words in the description
        for (String word : forbiddenWords) {
            if (description.toLowerCase().contains(word.toLowerCase())) {
                // Show an alert if a forbidden word is found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Description non valide");
                alert.setContentText("La description contient des mots inappropriés.");
                alert.showAndWait();
                return;
            }
        }

        // Insert the new record into the database
        String insert = "INSERT INTO reclamation(description, categorie, etat, iduser, date) VALUES (?, ?, ?, ?, ?)";
        try {
            con = DBConnexion.getConnection();
            st = con.prepareStatement(insert);
            st.setString(1, description);
            String categorie = extraireCategorie(description);
            st.setString(2, categorie);
            st.setString(3, "en attente");
            st.setString(4, userId);
            java.util.Date utilDate = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(5, sqlDate);
            st.executeUpdate();


            // Show a success alert if the insertion is successful
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText("Réclamation ajoutée avec succès");
            successAlert.setContentText("La réclamation a été ajoutée avec succès à la base de données.");
            successAlert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    void annulerRec(ActionEvent event) {

        // Réinitialiser les champs de saisie
        tf_des.clear();
        tf_iduser.clear();
        tf_date.setValue(null); // Réinitialiser la valeur du DatePicker à null
    }




    String extraireCategorie(String description) {
        // Implémentez votre logique NLP ici pour extraire la catégorie de la description
        // Par exemple, vous pouvez utiliser une correspondance de chaîne simple ou une bibliothèque NLP
        // Retournez la catégorie extraite ou une valeur par défaut si aucune catégorie n'est trouvée
        // Pour cet exemple, je vais simplement retourner "Problèmes de réservation" comme catégorie par défaut


        // Convertir la description en minuscules pour une correspondance insensible à la casse
        String descriptionLowerCase = description.toLowerCase();

        // Vérifier les mots-clés dans la description pour déterminer la catégorie
        if (descriptionLowerCase.contains("reservation") || descriptionLowerCase.contains("chambre") || descriptionLowerCase.contains("voyage") || descriptionLowerCase.contains("maison") || descriptionLowerCase.contains("voiture") || descriptionLowerCase.contains("reserver")) {
            return "Problèmes de réservation";
        } else if (descriptionLowerCase.contains("qualite") || descriptionLowerCase.contains("service") || descriptionLowerCase.contains("mauvaise")) {
            return "Qualité des services";
        } else if (descriptionLowerCase.contains("experience") || descriptionLowerCase.contains("client")) {
            return "Expérience client";
        } else if (descriptionLowerCase.contains("facturation") || descriptionLowerCase.contains("paiement") || descriptionLowerCase.contains("facture") || descriptionLowerCase.contains("argent") || descriptionLowerCase.contains("montant") || descriptionLowerCase.contains("cher") || descriptionLowerCase.contains("couteux")) {
            return "Facturation et paiement";
        } else if (descriptionLowerCase.contains("transport")) {
            return "Transport";
        } else {
            // Retourner une catégorie par défaut si aucune correspondance n'est trouvée
            return "Autre";
        }
    }




    @FXML
    public void golisterec(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/listeRepp.fxml"));
            Parent repp = loader.load();

            // Access the current stage from any node in the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(repp);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
