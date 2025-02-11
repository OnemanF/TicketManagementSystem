package dk.easv.ticketmanagementsystem.Gui.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private Button btnManageUsers;
    @FXML
    private Button btnAssignCoordinators;
    @FXML
    private Button btnLogout;

    @FXML
    private void handleManageUsers(ActionEvent event) {
        loadScene("UserManagement.fxml", "User Management");
    }

    @FXML
    private void handleAssignCoordinators(ActionEvent event) {
        showAlert("Assign Coordinators clicked.");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        showAlert("Logging out...");
        loadScene("Login.fxml", "Login");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) btnManageUsers.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert("Failed to load: " + title);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

}
