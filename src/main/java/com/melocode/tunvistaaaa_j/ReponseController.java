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
import java.util.ResourceBundle;

public class ReponseController implements Initializable {


    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btn_ajoutrep;

    @FXML
    private Button btn_annrep;

    @FXML
    private Button btn_modrep;

    @FXML
    private Button btn_reclamation;

    @FXML
    private Button btn_reponse;

    @FXML
    private Button btn_statistique;

    @FXML
    private Button btn_supprep;

    @FXML
    private Pane inner_pane;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private TextField tf_descReponse;

    @FXML
    private TextField tf_idreclamation;

    @FXML
    private TextField tf_iduserReponse;

    @FXML
    private TextField txt_serach;
    @FXML
    private Button btn_reclamation1;
    @FXML
    private ListView<Reponse> table_reponse;
    @FXML
    private Button btn_gRep;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the cell value factory to display multiple attributes
        table_reponse.setItems(getReponses());
        table_reponse.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reponse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + " | Description: " + item.getDescription() +
                            " | ID Reclamation: " + item.getIdrec() + " | ID User: " + item.getIduser());
                }
            }
        });

        // Add listener to the search text field to trigger search on typing
        txt_serach.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReponse(newValue.trim().toLowerCase());
        });
    }

    private void searchReponse(String searchQuery) {
        // Create a filtered list to hold the filtered reponse objects
        ObservableList<Reponse> filteredList = FXCollections.observableArrayList();

        // If search query is empty, show all reponses
        if (searchQuery.isEmpty()) {
            table_reponse.setItems(getReponses());
            return;
        }

        // Iterate through the original list of reponses and add matching ones to the filtered list
        for (Reponse reponse : getReponses()) {
            // Convert relevant fields to lowercase for case-insensitive search
            String description = reponse.getDescription().toLowerCase();
            String idrec = reponse.getIdrec().toLowerCase();
            String iduser = reponse.getIduser().toLowerCase();

            // Check if any field contains the search query
            if (description.contains(searchQuery) || idrec.contains(searchQuery) || iduser.contains(searchQuery)) {
                filteredList.add(reponse);
            }
        }

        // Update the list view with the filtered list
        table_reponse.setItems(filteredList);
    }

    public ObservableList<Reponse> getReponses() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();
        String query = "SELECT id, description, idrec, iduser FROM reponse";
        con = DBConnexion.getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reponse rep = new Reponse();
                rep.setId(rs.getInt("id"));
                rep.setDescription(rs.getString("description"));
                rep.setIdrec(rs.getString("idrec"));
                rep.setIduser(rs.getString("iduser"));
                reponses.add(rep);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reponses;
    }

    public void initData(Reclamation selectedReclamation) {
        tf_idreclamation.setText(String.valueOf(selectedReclamation.getId()));
        tf_iduserReponse.setText(selectedReclamation.getIduser());
        // You can initialize other fields if needed
    }


    @FXML
    void ajouterRep(ActionEvent event) {
        // Get the data from the input fields
        String description = tf_descReponse.getText();
        String idReclamation = tf_idreclamation.getText(); // Assuming this is obtained from the selected reclamation
        String idUser = tf_iduserReponse.getText(); // Assuming this is obtained from the logged-in user

        // Validate the input fields
        if (description.isEmpty() || idReclamation.isEmpty() || idUser.isEmpty()) {
            // Display an error message if any of the fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incomplete Information");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        // Insert the new Reponse object into the database
        String insertQuery = "INSERT INTO reponse(description, idrec, iduser) VALUES (?, ?, ?)";
        String updateReclamation = "UPDATE reclamation SET etat = 'actif' WHERE id = ?";
        con = DBConnexion.getConnection();
        try {
            st = con.prepareStatement(insertQuery);
            st.setString(1, description);
            st.setString(2, idReclamation);
            st.setString(3, idUser);
            st.executeUpdate();

            // Execute the update statement to change the reclamation status to 'actif'
            st = con.prepareStatement(updateReclamation);
            st.setString(1, idReclamation);
            st.executeUpdate();

            showReponse();

            // Show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Reponse Added Successfully");
            successAlert.setContentText("The reponse has been added successfully to the database.");
            successAlert.showAndWait();

            // Clear the input fields
            tf_descReponse.clear();
            tf_idreclamation.clear();
            tf_iduserReponse.clear();
        } catch (SQLException e) {
            // Display an error message if the insertion fails
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Database Error");
            errorAlert.setContentText("An error occurred while adding the reponse to the database. Please try again.");
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }


    @FXML
    void annulerRep(ActionEvent event) {

        // Réinitialiser les champs de saisie
        tf_descReponse.clear();
        tf_idreclamation.clear();
        tf_iduserReponse.clear(); // Réinitialiser la valeur du DatePicker à null
        btn_modrep.setDisable(false); // Activer le bouton modifier
        table_reponse.getSelectionModel().clearSelection(); // Désélectionner toute ligne dans la table

    }

    @FXML
    void getData(MouseEvent event) {
        Reponse reponse = table_reponse.getSelectionModel().getSelectedItem();
        if (reponse != null) {
            tf_descReponse.setText(reponse.getDescription());
            tf_idreclamation.setText(reponse.getIdrec());
            tf_iduserReponse.setText(reponse.getIduser());
            btn_modrep.setDisable(false); // Enable the modifier button when an item is selected
        } else {
            btn_modrep.setDisable(true); // Disable the modifier button when no item is selected
        }
    }


    @FXML
    void modifierRep(ActionEvent event) {
        Reponse reponse = table_reponse.getSelectionModel().getSelectedItem();
        if (reponse != null) {
            String updateQuery = "UPDATE reponse SET description = ?, idrec = ?, iduser = ? WHERE id = ?";
            con = DBConnexion.getConnection();
            try {
                st = con.prepareStatement(updateQuery);
                st.setString(1, tf_descReponse.getText());
                st.setString(2, tf_idreclamation.getText());
                st.setString(3, tf_iduserReponse.getText()); // Corrected to use getText() instead of String.valueOf()

                st.setInt(4, reponse.getId()); // Corrected index to 4
                st.executeUpdate();
                showReponse();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    void supprimerRep(ActionEvent event) {

        Reponse reponse = (Reponse) table_reponse.getSelectionModel().getSelectedItem();
        if (reponse != null) {
            String deleteQuery = "DELETE FROM reponse WHERE id = ?";
            con = DBConnexion.getConnection();
            try {
                st = con.prepareStatement(deleteQuery);
                st.setInt(1, reponse.getId());
                st.executeUpdate();
                showReponse();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void showReponse() {
        ObservableList<Reponse> list = getReponses();
        table_reponse.setItems(list);
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
    public void sceneSwitch(ActionEvent actionEvent) {
    }

    @FXML
    public void goreponse(ActionEvent actionEvent) {
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

    public void setResponse(String response) {
        tf_descReponse.setText(response);
    }

    @FXML
    public void ajouterReppp(ActionEvent actionEvent) {

        // Get the data from the input fields
        String idReclamation = tf_idreclamation.getText(); // Assuming this is obtained from the selected reclamation

        // Validate the input field
        if (idReclamation.isEmpty()) {
            // Display an error message if the field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incomplete Information");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        // Get the category of the reclamation based on its ID
        String categorie = obtenirCategorieReclamation(Integer.parseInt(idReclamation));

        // Generate the response based on the category
        String description = genererReponseEnFonctionDeCategorie(categorie);

        // Set the generated response to the description text field
        tf_descReponse.setText(description);
    }


    private String obtenirCategorieReclamation(int reclamationId) {
        String categorie = null;

        // Query to fetch the category of the reclamation based on its ID
        String query = "SELECT categorie FROM reclamation WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Get the database connection
            connection = DBConnexion.getConnection();

            // Prepare the statement
            statement = connection.prepareStatement(query);
            statement.setInt(1, reclamationId);

            // Execute the query
            resultSet = statement.executeQuery();

            // Check if a result is returned
            if (resultSet.next()) {
                // Get the category from the result set
                categorie = resultSet.getString("categorie");
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        } finally {
            // Close the database resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return categorie;
    }


    private String genererReponseEnFonctionDeCategorie(String categorie) {
        String reponse = "";

        // Implémentez la logique pour générer la réponse en fonction de la catégorie de la réclamation
        switch (categorie) {
            case "Problèmes de réservation":
                reponse = " Nous avons bien reçu votre réclamation. Nous allons traiter votre demande dans les plus brefs délais.";
                break;
            case "Qualité des services":
                reponse = " Votre réclamation a été transmise à notre équipe technique. Nous vous tiendrons informé(e) de l'avancement de votre dossier.";
                break;
            default:
                reponse = " Nous avons bien reçu votre réclamation. Nous allons la traiter dans les plus brefs délais.";
                break;
        }

        return reponse;
    }
}
