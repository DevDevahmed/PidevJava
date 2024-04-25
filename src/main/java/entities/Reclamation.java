/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;


public class Reclamation {

    private int id;
    private String titre;
    private String desc;
    private String type;
    private String etat;
    private int id_user;

    public Reclamation(){}

    public Reclamation(int id,String titre, String desc, String type, String etat, int user) {
        this.id = id;
        this.titre=titre;
        this.desc = desc;
        this.type = type;
        this.etat = etat;
        this.id_user = user;
    }

    @Override
    public String toString() {
        return "reclamation{" + "id=" + id + ", titre=" + titre +  ", desc=" + desc + ", type=" + type + ", etat=" + etat + ", id_user=" + id_user + '}';
    }

    public Reclamation(String titre,String desc, String type, String etat, int id_user) {
        this.titre=titre;
        this.desc = desc;
        this.type = type;
        this.etat = etat;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
    public String getTitre() {
        return titre;
    }

    public String getType() {
        return type;
    }

    public String isEtat() {
        return etat;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reclamation other = (Reclamation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}