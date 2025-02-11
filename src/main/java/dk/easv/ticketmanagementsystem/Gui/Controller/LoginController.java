package dk.easv.ticketmanagementsystem.Gui.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
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

    public LoginController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){


    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        String dashboard = null;
        if ("admin".equals(username) && "password".equals(password)) {
            dashboard = "AdminDashboard.fxml";
        } else if ("coordinator".equals(username) && "password".equals(password)) {
            dashboard = "EventCoordinator.fxml";
        }

        if (dashboard != null) {
            loadDashboard(dashboard);
        } else {
            showError("Invalid credentials! Try again.");
        }
    }

    private void loadDashboard(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load dashboard.");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}


