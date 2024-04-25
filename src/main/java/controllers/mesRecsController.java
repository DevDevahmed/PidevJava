/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entities.Reclamation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import services.ServiceReclamation;

/**
 * FXML Controller class
 *
 */
public class mesRecsController implements Initializable {

    @FXML
    private ScrollPane scrollpane;
    private ServiceReclamation sr = new ServiceReclamation();


    /**
     * Initializes the controller class.
     */
    public void table() {
        List<Reclamation> offres = sr.getAll();
        VBox vBox = new VBox();

        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(30);

        HBox hBox = new HBox();

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(100);//

        int count = 0;
        for (Reclamation offre : offres) {
            VBox box = createOffreBox(offre);

            hBox.getChildren().add(box);
            count++;

            if (count == 1) {
                vBox.getChildren().add(hBox);
                hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(100);
                count = 0;
            }
        }

        if (count > 0) {
            vBox.getChildren().add(hBox);
        }

        scrollpane.setContent(vBox);
        scrollpane.setFitToWidth(true);
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        table();
    }

    private VBox createOffreBox(Reclamation offre) {
        VBox box = new VBox();

        box.setAlignment(Pos.CENTER);
        box.setSpacing(30);
        box.setUserData(offre.getId()); // set the ID as the user data for the VBox

        Label titre = new Label("Type :  " + offre.getType());

        Button bb = new Button();
        bb.setText("Supprimer");

        Label user = new Label("Ayoub");
        Label etat = new Label();
        if (offre.isEtat() == "Non Traitée") {
            etat = new Label("Etat :               Non Traitée");

        } else {
            etat = new Label("Etat :                      Traitée");
        }

        Label voir = new Label("Contenu      :" + offre.getDesc());
        Label sep = new Label("____________________________________________________________________________________________________________________");

        user.setStyle("-fx-text-fill : Blue;");
        voir.setStyle("-fx-text-fill : Black;");
        etat.setStyle("-fx-text-fill : Red;");

        voir.setFont(Font.font("Serif", FontWeight.LIGHT, 23));
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 29));

        user.setWrapText(true);
        box.getChildren().addAll(titre, voir, etat, bb, sep);

        bb.setOnMouseClicked(event -> {

            sr.supprimer(offre.getId());
            table();

        });

        /*bb.setOnMouseClicked(event -> {
            
            sp.supprimer(offre.getId());
            table();
            
        });
        
          box.setOnMouseClicked(event -> {
                
                idd = (offre.getId());
                Poste selected=sp.getOneById(offre.getId());
                tftitre.setText(selected.getTitre());
                tfdesc.setText(selected.getDesc());
                combodomaine.setItems(sd.getalls());
                File imagef = new File(selected.getImg());
                ImagePath=imagef.toURI().toString();
                Image image = new Image(imagef.toURI().toString());
                img.setImage(image);
         
                
                
              
        });

         */
        return box;
    }
}