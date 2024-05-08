package com.melocode.tunvistaaaa_j;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ReclamationCellFactory implements Callback<ListView<Reclamation>, ListCell<Reclamation>> {
    @Override
    public ListCell<Reclamation> call(ListView<Reclamation> param) {
        return new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText("ID: " + item.getId() + "\n"
                            + "Description: " + item.getDescription() + "\n"
                            + "Categorie: " + item.getCategorie() + "\n"
                            + "Etat: " + item.getEtat() + "\n"
                            + "ID User: " + item.getIduser() + "\n"
                            + "Date: " + item.getDate());
                } else {
                    setText(null);
                }
            }
        };
    }
}
