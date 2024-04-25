/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.DataSource;
import entities.Reclamation;
import entities.Reponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author winxspace
 */
public class ServiceRepons implements Iservice<Reponse>{
    Connection conn;
    public ServiceRepons(){
        conn=DataSource.getInstance().getCnx();
    }

    public ServiceRepons(LocalDate value, String text, String text0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void ajouter(Reponse t) {
        try{String query = "INSERT INTO `reponse` "
             + "(`id_rec_id`,  `description`, `id_user`) "
             + "VALUES "
             + "('"+t.getId_rec_id()+"', '"+t.getDescription()+"', "
             + "'"+t.getId_user()+"')";
            Statement st=conn.createStatement();
            st.executeUpdate(query);
        } 
        catch (SQLException ex) {
            Logger.getLogger(ServiceRepons.class.getName()).log(Level.SEVERE, null, ex);
    }
    }




    @Override
    public void supprimer(int id)  {
       try {
            String query="DELETE FROM `reponse` WHERE id="+id;
            Statement st=conn.createStatement();
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceRepons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modifier(Reponse p) {

    }

    @Override
    public List<Reponse> getAll() {
     List<Reponse> lre=new ArrayList<>();
        try {
            String query="SELECT * FROM `reponse`";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                Reponse re=new Reponse();
                re.setId_rec_id(rs.getInt("id_rec_id"));

                re.setDescription(rs.getString("description"));
                re.setId_user(rs.getInt("id_user"));
                re.setId(rs.getInt("id"));
                lre.add(re);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceRepons.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lre; 
    }

    @Override
    public Reponse getOneById(int id) {
        return null;
    }


}
