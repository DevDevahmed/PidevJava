package com.melocode.tunvistaaaa_j;

import java.time.LocalDate;
import java.util.Date;

public class Reclamation {

    private int id ;
    private String description ;
    private String categorie ;
    private String etat ;
    private String iduser;
    private Date date;


    public Reclamation(String description, String categorie, String etat, String iduser, LocalDate date) {
    }

    public Reclamation() {
        // Vous pouvez initialiser les valeurs par défaut ici si nécessaire
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getIduser() {
        return this.iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "description='" + description + '\'' +
                ", categorie='" + categorie + '\'' +
                ", etat='" + etat + '\'' +
                ", iduser='" + iduser + '\'' +
                ", date=" + date +
                '}';
    }



}
