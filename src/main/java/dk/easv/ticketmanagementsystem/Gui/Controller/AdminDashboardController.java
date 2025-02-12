package dk.easv.ticketmanagementsystem.Gui.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private Button btnManageUsers;
    @FXML
    private Button btnLogout;

    @FXML
    private void handleManageUsers(ActionEvent event) {
        try {
            loadScene(event, "UserManagement.fxml");
        } catch (IOException e) {
            showAlert("Error loading User Management screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            loadScene(event, "Login.fxml");
        } catch (IOException e) {
            showAlert("Error returning to Login.");
            e.printStackTrace();
        }
    }

    private void loadScene(ActionEvent event, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketmanagementsystem/" + fxml));
        Parent root = loader.load();

        // Getting current stage from the button
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
