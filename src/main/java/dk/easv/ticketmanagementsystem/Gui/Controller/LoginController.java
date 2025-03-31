package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.Gui.Model.LoginModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() {
                return loginModel.getAuthenticatedUser(username, password);
            }
        };

        loginTask.setOnSucceeded(event -> {
            User user = loginTask.getValue();
            if (user != null) {
                Platform.runLater(() -> {
                    switch (user.getRole().toLowerCase()) {
                        case "admin":
                            loadDashboard("UserManagement.fxml", actionEvent);
                            break;
                        case "coordinator":
                            loadDashboard("EventCoordinator.fxml", actionEvent);
                            break;
                        default:
                            showError("Role not recognized.");
                            break;
                    }
                });
            } else {
                showError("Invalid credentials! Try again.");
            }
        });

        loginTask.setOnFailed(event -> {
            Platform.runLater(() -> showError("An error occurred during login."));
        });

        new Thread(loginTask).start();
    }


    private void loadDashboard(String fxmlFile, ActionEvent actionEvent) {
        try {
            String resourcePath = "/dk/easv/ticketmanagementsystem/" + fxmlFile;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));

            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found at: " + resourcePath);
            }

            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}


