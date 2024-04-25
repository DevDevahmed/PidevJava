/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.sql.Date;

public class Reponse {
    private int id;
    private int id_rec_id;
    private String description;
    private int id_user;

    public Reponse() {

    }

    public Reponse(int id, int id_rec_id, String description, int id_user) {
        this.id = id;
        this.id_rec_id = id_rec_id;
        this.description = description;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_rec_id() {
        return id_rec_id;
    }

    public void setId_rec_id(int id_rec_id) {
        this.id_rec_id = id_rec_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", id_rec_id=" + id_rec_id +
                ", description='" + description + '\'' +
                ", id_user=" + id_user +
                '}';
    }
}
