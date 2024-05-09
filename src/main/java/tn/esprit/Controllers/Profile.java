package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.entities.User;
import tn.esprit.services.UserServices;
import tn.esprit.services.UserSessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class Profile implements Initializable {

    @FXML
    private TextField CIN;

    @FXML
    private TextField ConfirmPasswordField;

    @FXML
    private TextField Email;

    @FXML
    private TextField esm;

    @FXML
    private TextField la9ab;

    @FXML
    private Button ModifierPictureBtn;

    @FXML
    private Button NbadelPassword;

    @FXML
    private Label NemchilelPassword;

    @FXML
    private HBox NemchilelProfile;

    @FXML
    private Label Nemchilelprofile;

    @FXML
    private TextField NewPasswordField;

    @FXML
    private Label NomPrenom;

    @FXML
    private TextField OldPasswordField;

    @FXML
    private HBox PasswordSideNav;

    @FXML
    private ImageView PictureLabel;

    @FXML
    private Button SaveBtn;

    @FXML
    private ImageView TaswiraChange;

    @FXML
    private Label UserFIrstLastName;

    @FXML
    private Button khourouj;

    @FXML
    private Button nbadeltaswira;

    @FXML
    private Button no5rej;

    @FXML
    private BorderPane passwordSection;

    @FXML
    private Label profile;

    @FXML
    private BorderPane profileSection;

    @FXML
    private TextField tel;
    private Connection cnx;

    private UserSessionManager sessionManager;
    private UserServices userServices;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSessionManager(UserSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (sessionManager == null) {
            sessionManager = new UserSessionManager();
        }

        // Initialize UserServices
        userServices = new UserServices();

        // Retrieve user data and display in labels
        User user = sessionManager.getCurrentUser();
        if (user != null) {
            UserFIrstLastName.setText(user.getNom() + " " + user.getPrenom());
            NomPrenom.setText(user.getNom() + " " + user.getPrenom());
        } else {
            // Handle case when user is not logged in
            // You can redirect the user to the login page or display a message
            System.out.println("User not logged in");
        }

        // By default, show profile section and hide password section
        profileSection.setVisible(true);
        passwordSection.setVisible(false);
    }

    @FXML
    void saveProfileChanges(ActionEvent event) {
        User currentUser = sessionManager.getCurrentUser(); // Get the current user from session manager
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User not logged in", "Please log in again.");
            return;
        }

        // Retrieve modified profile information from text fields
        String newEmail = Email.getText();
        String newLastName = la9ab.getText();
        String newFirstName = esm.getText();
        int newCin;
        int newTel;
        try {
            newCin = Integer.parseInt(CIN.getText());
            newTel = Integer.parseInt(tel.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input", "CIN and Tel must be numeric values.");
            return;
        }

        // Check if the email already exists (excluding the current user)
        if (!newEmail.equals(currentUser.getEmail()) && userServices.isEmailAlreadyExists(newEmail)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email already exists", "Please choose a different email.");
            return;
        }

        // Update user's profile information in the database
        currentUser.setEmail(newEmail);
        currentUser.setNom(newLastName);
        currentUser.setPrenom(newFirstName);
        currentUser.setCin(newCin);
        currentUser.setTel(newTel);
        userServices.modifier(currentUser);

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Updated", "Your profile has been successfully updated.");
    }


    @FXML
    void savePasswordChanges(ActionEvent event) {
        // Retrieve new password information from text fields
        String oldPassword = OldPasswordField.getText();
        String newPassword = NewPasswordField.getText();
        String confirmPassword = ConfirmPasswordField.getText();

        // Check if new password matches confirmation
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password Mismatch", "New password does not match the confirmation.");
            return;
        }

        // Retrieve user's current password from the database
        User user = sessionManager.getCurrentUser();
        if (user != null) {
            // Check if the provided old password matches the stored hashed password
            if (BCrypt.checkpw(oldPassword, user.getPassword())) {
                // Hash the new password before updating it
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedNewPassword);

                // Update user's password in the database
                userServices.modifier(user);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password Updated", "Your password has been successfully updated.");

                // Redirect user to login page
                redirectToLogin(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Incorrect Password", "Please enter the correct old password.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "User Not Logged In", "No user is currently logged in.");
        }
    }

    @FXML
    void logout(ActionEvent event) {
        // Clear user session and redirect to login page
        sessionManager.clearUserSession();
        // Redirect user to login page
        redirectToLogin(event);
    }

    private void redirectToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Obtain the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void navigateToProfileSection() {
        profileSection.setVisible(true);
        passwordSection.setVisible(false);
    }

    @FXML
    private void navigateToPasswordSection() {
        profileSection.setVisible(false);
        passwordSection.setVisible(true);
    }

}
