/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Reclamation;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import Util.DataSource;

/**
 *
 * @author Ghass
 */
public class ServiceReclamation implements Iservice <Reclamation> {

    Connection cnx = DataSource.getInstance().getCnx();
    @Override
    public void ajouter(Reclamation c) {
        try {
            String req = "INSERT INTO reclamation (titre,description,type,etat,id_user) VALUES ('" + c.getTitre() + "','" + c.getDesc() + "', '" + c.getType()+ "','" + c.isEtat() + "','" + c.getId_user() + "')";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("card created !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        try {
            String req = "DELETE FROM reclamation WHERE id = " + id;
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Transaction deleted !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifier(Reclamation c) {
        try {
            String req = "UPDATE reclamation SET titre='" + c.getTitre() +"',description = '" + c.getDesc() + "', type = '" + c.getType() + "', etat = '" + c.isEtat() + "',id_user = '" + c.getId_user() + "'  WHERE reclamation.id = " + c.getId();
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("card updated !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> list = new ArrayList<>();
        try {
            String req = "Select * from reclamation";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {

                Reclamation c = new Reclamation(rs.getInt(1),rs.getString(2), rs.getString("description"), rs.getString(4),rs.getString("etat"),rs.getInt("id_user"));
                list.add(c);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }

    @Override
    public Reclamation getOneById(int id) {
        Reclamation c = null;
        try {
            String req = "Select * from reclamation WHERE reclamation.id ='"+id+"'";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {

                c = new Reclamation(rs.getInt(1),rs.getString(2), rs.getString("description"), rs.getString(4),rs.getString("etat"),rs.getInt("id_user"));

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return c;
    }






}