package com.melocode.tunvistaaaa_j;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class listeRepController implements Initializable {

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;


        @FXML
        private Text btn_listeRec;

        @FXML
        private Button btn_retour;

        @FXML
        private Button btn_suppp;

    @FXML
    private BorderPane repp;
    @FXML
    private TableView tab_repliste;
    @FXML
    private TableColumn coldesc;
    @FXML
    private TableColumn coletat;
    @FXML
    private TableColumn coliduser;
    @FXML
    private TableColumn colid;
    @FXML
    private TableColumn colcateg;
    @FXML
    private TableColumn coldate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Charger les données de la table reclamation depuis la base de données
        showReclamation();
        tab_repliste.refresh();

    }


    public void showReclamation() {
        ObservableList<Reclamation> list = loadReclamationData();
        tab_repliste.setItems(list);
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coldesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colcateg.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        coletat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        coliduser.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    // Méthode pour charger les données de la table reclamation depuis la base de données
    public ObservableList<Reclamation> loadReclamationData() {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String query = "SELECT id, description, categorie, etat, iduser, date FROM reclamation";
        con = DBConnexion.getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Reclamation rec = new Reclamation();
                rec.setId(rs.getInt("id"));
                rec.setDescription(rs.getString("description"));
                rec.setCategorie(rs.getString("categorie"));
                rec.setEtat(rs.getString("etat"));
                rec.setIduser(rs.getString("iduser"));
                rec.setDate(rs.getDate("date"));
                reclamations.add(rec);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reclamations;
    }







    // Conversion d'une date SQL en LocalDate
    private LocalDate convertToLocalDate(Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime()).toLocalDate();
        } else {
            return null;
        }
    }





    @FXML
        void listeofRec(MouseEvent event) {

    }

        @FXML
        void retour(ActionEvent event) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/RecFront.fxml"));
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

        @FXML
        void supp_rep(ActionEvent event) {

        }


    @FXML
    public void getData(Event event) {
    }
}


