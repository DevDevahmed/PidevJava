package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.AuthResponseDTO;
import tn.esprit.services.UserServices;
import tn.esprit.services.UserSessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private AnchorPane changepass1_form;

    @FXML
    private AnchorPane changepass2_form;

    @FXML
    private AnchorPane changepass3_form;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button forget_backbtn;

    @FXML
    private Button forget_backtocodeverifbtn;

    @FXML
    private Button forget_backtoemailverif;

    @FXML
    private PasswordField forget_confirmationnewpass;

    @FXML
    private TextField forget_email;

    @FXML
    private PasswordField forget_newpassword;

    @FXML
    private Button forget_proceed2;

    @FXML
    private Button forget_proceedbtn;

    @FXML
    private Button forget_proceedtologinpage;

    @FXML
    private TextField forget_verificationcode;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createaccount;

    @FXML
    private Hyperlink login_forgetpassword;

    @FXML
    private TextField login_idselect;

    @FXML
    private PasswordField login_selectpassword;

    @FXML
    private CheckBox login_showpassword;

    @FXML
    private AnchorPane main_authform;

    @FXML
    private Button register_btn;

    @FXML
    private TextField register_cin;

    @FXML
    private PasswordField register_confirmationpassword;

    @FXML
    private TextField register_email;

    @FXML
    private TextField register_firstname;

    @FXML
    private TextField register_lastname;

    @FXML
    private Button register_loginbtn;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_tel;

    private UserServices userServices;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userServices = new UserServices();
        //login_btn.setOnAction(this::handleLogin);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleLogin() {
        String identifier = login_idselect.getText().trim(); // Can be email or username
        String password = login_selectpassword.getText().trim();

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Missing Information", "Please enter both email/username and password.");
            return;
        }

        // Authenticate user
        User user = userServices.authenticateAndGetUser(identifier, password);
        if (user != null) {
            // Set the user object in the session (replace this with your session management mechanism)
            UserSessionManager.setCurrentUser(user);

            // Redirect user to profile page
            redirectToProfile();
        } else {
            // Display error message to the user
            showAlert("Error", "Authentication Failed", "Invalid email/username or password.");
        }
    }

    private void redirectToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Parent root = loader.load();

            // Get the controller
            Profile controller = loader.getController();

            // Set the stage object
            controller.setStage((Stage) main_authform.getScene().getWindow());

            // Set the stage and scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) main_authform.getScene().getWindow(); // Get the current stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleRegister() {
        String firstName = register_firstname.getText().trim();
        String lastName = register_lastname.getText().trim();
        String email = register_email.getText().trim();
        String password = register_password.getText().trim();
        String confirmPassword = register_confirmationpassword.getText().trim();
        String cinStr = register_cin.getText().trim();
        String telStr = register_tel.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || cinStr.isEmpty() || telStr.isEmpty()) {
            showAlert("Error", "Missing Information", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Password Mismatch", "Password and Confirm Password do not match.");
            return;
        }
        if (userServices.isEmailAlreadyExists(email)) {
            showAlert("Error", "Email Already Exists", "This email is already registered.");
            return;
        }

        try {
            int cin = Integer.parseInt(cinStr);
            int tel = Integer.parseInt(telStr);

            // Create a new user object
            User newUser = new User();
            newUser.setNom(firstName);
            newUser.setPrenom(lastName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setCin(cin);
            newUser.setTel(tel);

            // Add the new user to the database
            userServices.ajouter(newUser);
            System.out.println("User Registered!");
            signup_form.setVisible(false);
            login_form.setVisible(true);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Input", "CIN and Tel must be numeric values.");
        }
    }

    @FXML
    public void switchForm(ActionEvent event){
        if(event.getSource()==register_loginbtn ) {
            signup_form.setVisible(false);
            login_form.setVisible(true);
            changepass1_form.setVisible(false);
            changepass2_form.setVisible(false);
            changepass3_form.setVisible(false);


        }
        else if (event.getSource()==login_createaccount){
            signup_form.setVisible(true);
            login_form.setVisible(false);
            changepass1_form.setVisible(false);
            changepass2_form.setVisible(false);
            changepass3_form.setVisible(false);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}