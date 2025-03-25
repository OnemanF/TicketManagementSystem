package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.Gui.Model.LoginModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;

    private LoginModel loginModel;

    public LoginController(){
        this.loginModel = new LoginModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void handleLogin(ActionEvent actionEvent) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();


        User user = loginModel. getAuthenticatedUser(username, password);

        if (user != null) {
            switch (user.getRole().toLowerCase()) {
                case "admin":
                    loadDashboard("UserManagement.fxml");
                    break;
                case "event coordinator":
                    loadDashboard("EventManagement.fxml");
                    break;
                default:
                    showError("Role not recognized.");
                    break;
            }
        } else {
            showError("Invalid credentials! Try again.");
        }
    }

    private void loadDashboard(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load dashboard.");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}


