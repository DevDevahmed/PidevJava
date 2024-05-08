package com.melocode.tunvistaaaa_j;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Objects;

public class sceneSwitch {
    public sceneSwitch(HBox currentHBox , String fxml) throws IOException {
        HBox nextHBox = FXMLLoader.load(Objects.requireNonNull(App.class.getResource(fxml)));
        currentHBox.getChildren().removeAll();
        currentHBox.getChildren().setAll(nextHBox);

    }
}
