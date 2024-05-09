package com.melocode.tunvistaaaa_j;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReclamationController implements Initializable {

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btn_ajout;

    @FXML
    private Button btn_ann;

    @FXML
    private Button btn_mod;

    @FXML
    private Button btn_supp;

    @FXML
    private ListView<Reclamation> tableviewrec;
    @FXML
    private Pagination pagination;





    @FXML
    private Pane inner_pane;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private TextField txt_serach;






    private RealBadWordApiClient badWordApiClient;
    @FXML
    private Button btn_reclamation;
    @FXML
    private Button btn_reponse;
    @FXML
    private Button btn_statistique;
    @FXML
    private Button btn_reprec;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReclamation();
        tableviewrec.refresh();
        // Add listener to the search text field to trigger search on typing
        txt_serach.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReclamation(newValue.trim().toLowerCase());
        });
    }

    private void searchReclamation(String searchQuery) {
        // Create a filtered list to hold the filtered reclamation objects
        ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();

        // If search query is empty, show all reclamations
        if (searchQuery.isEmpty()) {
            tableviewrec.setItems(getReclamations());
            return;
        }

        // Iterate through the original list of reclamations and add matching ones to the filtered list
        for (Reclamation reclamation : getReclamations()) {
            // Convert relevant fields to lowercase for case-insensitive search
            String description = reclamation.getDescription().toLowerCase();
            String categorie = reclamation.getCategorie().toLowerCase();
            String etat = reclamation.getEtat().toLowerCase();
            String iduser = reclamation.getIduser().toLowerCase();

            // Check if any field contains the search query
            if (description.contains(searchQuery) || categorie.contains(searchQuery) || etat.contains(searchQuery) || iduser.contains(searchQuery)) {
                filteredList.add(reclamation);
            }
        }

        // Update the table view with the filtered list
        tableviewrec.setItems(filteredList);
    }


    // Update the method to use ListView instead of TableView
    public ObservableList<Reclamation> getReclamations() {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String query = "SELECT id, description, categorie, etat, iduser, date FROM reclamation";

        try (Connection con = DBConnexion.getConnection();
             PreparedStatement st = con.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setId(rs.getInt("id"));
                rec.setDescription(rs.getString("description"));
                rec.setCategorie(rs.getString("categorie"));
                rec.setEtat(rs.getString("etat"));
                rec.setIduser(rs.getString("iduser"));
                rec.setDate(rs.getDate("date")); // Set the date directly
                reclamations.add(rec);
            }
        } catch (SQLException e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
        }
        return reclamations;
    }



    // Update the method to use ListView instead of TableView
    public void showReclamation() {
        ObservableList<Reclamation> list = getReclamations();
        tableviewrec.setItems(list);
    }

    @FXML
    private DatePicker tf_date;

    @FXML
    private TextField tf_des;

    @FXML
    private TextField tf_iduser;



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
            showReclamation();

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





    String extraireCategorie(String description) {
        // Implémentez votre logique NLP ici pour extraire la catégorie de la description
        // Par exemple, vous pouvez utiliser une correspondance de chaîne simple ou une bibliothèque NLP
        // Retournez la catégorie extraite ou une valeur par défaut si aucune catégorie n'est trouvée
        // Pour cet exemple, je vais simplement retourner "Problèmes de réservation" comme catégorie par défaut


        // Convertir la description en minuscules pour une correspondance insensible à la casse
        String descriptionLowerCase = description.toLowerCase();

        // Vérifier les mots-clés dans la description pour déterminer la catégorie
        if (descriptionLowerCase.contains("reservation") || descriptionLowerCase.contains("hotel") || descriptionLowerCase.contains("voyage") || descriptionLowerCase.contains("date") || descriptionLowerCase.contains("maison") || descriptionLowerCase.contains("reserver")) {
            return "Problèmes de réservation";
        } else if (descriptionLowerCase.contains("qualite") || descriptionLowerCase.contains("service") || descriptionLowerCase.contains("mauvaise")) {
            return "Qualité des services";
        } else if (descriptionLowerCase.contains("experience") || descriptionLowerCase.contains("client")) {
            return "Expérience client";
        }
        else if (descriptionLowerCase.contains("facturation") || descriptionLowerCase.contains("facture") || descriptionLowerCase.contains("paiement") || descriptionLowerCase.contains("cher") || descriptionLowerCase.contains("montant")) {
            return "facturation et paiement";
        } else {
            // Retourner une catégorie par défaut si aucune correspondance n'est trouvée
            return "Autre";
        }
    }


    @FXML
    void getData(MouseEvent event) {
        Reclamation reclamation = tableviewrec.getSelectionModel().getSelectedItem();
        if (reclamation != null) {
            tf_des.setText(reclamation.getDescription());
            tf_iduser.setText(reclamation.getIduser());
            // Convert java.util.Date to LocalDate
            LocalDate localDate = reclamation.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            tf_date.setValue(localDate);
        }
        btn_mod.setDisable(true);
    }


    @FXML
    void annulerRec(ActionEvent event) {
        // Réinitialiser les champs de saisie
        tf_des.clear();
        tf_iduser.clear();
        tf_date.setValue(null); // Réinitialiser la valeur du DatePicker à null
        btn_mod.setDisable(false); // Activer le bouton modifier
        tableviewrec.getSelectionModel().clearSelection(); // Désélectionner toute ligne dans la table
    }


    @FXML
    void modifierRec(ActionEvent event) {
        Reclamation reclamation = tableviewrec.getSelectionModel().getSelectedItem();
        if (reclamation != null) {
            // Vérifier si la réclamation est modifiable
            if (estModifiable(reclamation)) {
                String updateQuery = "UPDATE reclamation SET description = ?, iduser = ?, date = ?, categorie = ? WHERE id = ?";
                con = DBConnexion.getConnection();
                try {
                    st = con.prepareStatement(updateQuery);
                    st.setString(1, tf_des.getText());
                    st.setString(2, tf_iduser.getText());

                    // Convertir la date sélectionnée en java.sql.Date
                    LocalDate localDate = tf_date.getValue();
                    if (localDate != null) {
                        java.util.Date utilDate = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        st.setDate(3, sqlDate);
                    } else {
                        // Si la date est null, définir la valeur de la colonne "date" dans la requête SQL à null
                        st.setNull(3, java.sql.Types.DATE);
                    }

                    // Mettre à jour la catégorie en fonction de la nouvelle description
                    String categorie = extraireCategorie(tf_des.getText());
                    st.setString(4, categorie);

                    st.setInt(5, reclamation.getId());
                    st.executeUpdate();
                    showReclamation();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Réclamation non modifiable
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Impossible de modifier");
                alert.setHeaderText(null);
                alert.setContentText("La réclamation ne peut pas être modifiée car elle a plus de 48 heures.");
                alert.showAndWait();
            }
        }
    }

    private boolean estModifiable(Reclamation reclamation) {
        if (reclamation.getDate() == null) {
            return false; // If the date is null, the reclamation cannot be modified
        }

        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime dateReclamation = dateToLocalDateTime(reclamation.getDate());
        long heuresDiff = ChronoUnit.HOURS.between(dateReclamation, maintenant);
        return heuresDiff < 48;
    }



    private LocalDateTime dateToLocalDateTime(Date date) {
        // Convert java.sql.Date to java.util.Date
        java.util.Date utilDate = new java.util.Date(date.getTime());

        // Convert java.util.Date to java.time.LocalDateTime
        return LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());
    }

    @FXML
    void supprimerRec(ActionEvent event) {
        Reclamation reclamation = tableviewrec.getSelectionModel().getSelectedItem();
        if (reclamation != null) {
            String deleteQuery = "DELETE FROM reclamation WHERE id = ?";
            con = DBConnexion.getConnection();
            try {
                st = con.prepareStatement(deleteQuery);
                st.setInt(1, reclamation.getId());
                st.executeUpdate();
                showReclamation();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Deprecated
    public void sceneSwitch(ActionEvent actionEvent) {
    }

    @FXML
    public void reponseRec(ActionEvent actionEvent) {
        Reclamation selectedReclamation = tableviewrec.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Reponse.fxml"));
                Parent root = loader.load();

                // Access the controller and set data
                ReponseController reponseController = loader.getController();
                reponseController.initData(selectedReclamation);

                // Access the current stage from any node in the scene
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Display an error message if no reclamation is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Reclamation Selected");
            alert.setContentText("Please select a reclamation before proceeding.");
            alert.showAndWait();
        }
    }  ;
    // Méthode pour générer une réponse en fonction de la catégorie
    private String generateResponse(String category) {
        // Implémentez votre logique de génération de réponse ici en fonction de la catégorie
        return "Réponse générée pour la catégorie : " + category;
    }

    // Méthode pour mettre à jour la table de réponses dans la base de données
    private void updateResponse(String response, int reclamtionId) {
        try {

            String query = "UPDATE reponse SET description = ? WHERE id_reclamation = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, response);
            preparedStatement.setInt(2, reclamtionId);
            preparedStatement.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void goreponse(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Reponse.fxml"));
            Parent root = loader.load();

            // Access the current stage from any node in the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goreclamation(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Reclamation.fxml"));
            Parent root = loader.load();

            // Access the current stage from any node in the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gostatistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/statistique.fxml"));
            Parent root = loader.load();

            // Access the current stage from any node in the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
