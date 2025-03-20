package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BE.UserManager;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;
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
import java.sql.SQLException;

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

        if (loginModel.authenticate(username, password)) {
            User user = loginModel.getAuthenticatedUser(username, password);

            if (user != null) {
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    loadDashboard("UserManagement.fxml");
                } else if ("Event Coordinator".equalsIgnoreCase(user.getRole())) {
                    loadDashboard("EventManagement.fxml");
                } else {
                    showError("Role not recognized.");
                }
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


