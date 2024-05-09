package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.Controllers.Login;

import java.util.Objects;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("WanderWise");
            primaryStage.setScene(new Scene(root, 900, 600));

            // Set the stage instance to the controller
            Login controller = loader.getController();
            controller.setStage(primaryStage);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
