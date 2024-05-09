package tn.esprit.services;

import tn.esprit.entities.User;
import tn.esprit.tools.MyDb;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServices {

    private Connection cnx;

    public UserServices() {
        cnx = MyDb.getInstance().getCnx();
    }

    public void ajouter(User user) {
        // Check if the email already exists
        if (emailExists(user.getEmail())) {
            System.out.println("Error: Email already exists.");
            return;
        }

        String sql = "INSERT INTO user (nom, prenom, email, cin, password, role, tel) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getCin());
            // Hash the password before storing it
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            statement.setString(5, hashedPassword);
            // Set the role based on the email address
            String role = user.getEmail().endsWith("@esprit.tn") ? "ROLE_ADMIN" : "ROLE_TOURIST";
            statement.setString(6, role);
            statement.setInt(7, user.getTel());

            statement.executeUpdate();
            System.out.println("User Added!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modifier(User user) {
        // Check if the email already exists
        if (emailExists(user.getEmail(), user.getId())) {
            System.out.println("Error: Email already exists.");
            return;
        }

        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, cin = ?, password = ?, role = ?, tel = ? WHERE id = ?";

        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getCin());
            // Hash the password before updating it
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            statement.setString(5, hashedPassword);
            // Set the role based on the email address
            String role = user.getEmail().endsWith("@esprit.tn") ? "ROLE_ADMIN" : "ROLE_TOURIST";
            statement.setString(6, role);
            statement.setInt(7, user.getTel());
            statement.setInt(8, user.getId());

            statement.executeUpdate();
            System.out.println("User Updated!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void supprimer(int userId) {
        String sql = "DELETE FROM user WHERE id = ?";

        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setInt(1, userId);

            statement.executeUpdate();
            System.out.println("User Deleted!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<User> lister() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return users;
    }

    // Method to check if email already exists
    private boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE email = ?";
        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            return count > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return true; // Assuming an error means email exists to prevent insertion/update
        }
    }

    // Overloaded method to check if email already exists excluding the current user
    private boolean emailExists(String email, int userId) {
        String sql = "SELECT COUNT(*) AS count FROM user WHERE email = ? AND id != ?";
        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, email);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            return count > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return true; // Assuming an error means email exists to prevent insertion/update
        }
    }
    public boolean isEmailAlreadyExists(String email) {
        return emailExists(email);
    }


    public boolean authenticate(String identifier, String password) {
        String sql = "SELECT password FROM user WHERE email = ? OR nom = ?";
        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, identifier);
            statement.setString(2, identifier);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                // Check if the hashed password matches the provided password
                return BCrypt.checkpw(password, hashedPassword);
            } else {
                // No user found with the provided email or username
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    public User getUserByEmailOrUsername(String identifier) {
        String sql = "SELECT * FROM user WHERE email = ? OR nom = ?";
        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, identifier);
            statement.setString(2, identifier);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCin(rs.getInt("cin"));
                user.setTel(rs.getInt("tel"));
                return user;
            } else {
                // No user found with the provided email or username
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public User authenticateAndGetUser(String identifier, String password) {
        String sql = "SELECT * FROM user WHERE email = ? OR nom = ?";
        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, identifier);
            statement.setString(2, identifier);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCin(rs.getInt("cin"));
                user.setTel(rs.getInt("tel"));

                // Check if the hashed password matches the provided password
                if (BCrypt.checkpw(password, user.getPassword())) {
                    return user;
                } else {
                    // Password doesn't match
                    return null;
                }
            } else {
                // No user found with the provided email or username
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


}
