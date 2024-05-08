package com.melocode.tunvistaaaa_j;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public class App extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            // Charger le premier fichier FXML
            Parent parent1 = FXMLLoader.load(getClass().getResource("/Fxml/RecFront.fxml"));
            Scene scene1 = new Scene(parent1);

            // Charger le deuxième fichier FXML
            Parent parent2 = FXMLLoader.load(getClass().getResource("/Fxml/Reclamation.fxml"));
            Scene scene2 = new Scene(parent2);

            // Créer deux stages pour les deux scènes
            Stage stage1 = new Stage();
            Stage stage2 = new Stage();

            // Définir les titres des stages
            stage1.setTitle("CRUD");
            stage2.setTitle("Reclamation");

            // Définir les scènes pour les stages
            stage1.setScene(scene1);
            stage2.setScene(scene2);

            // Afficher les stages
            stage1.show();
            stage2.show();
        }

    public static void main(String[] args) {
        launch();
    }
}
