package dk.easv.ticketmanagementsystem.Gui.Controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.EventBLL;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import dk.easv.ticketmanagementsystem.Gui.Model.EventModel;
import dk.easv.ticketmanagementsystem.Gui.Model.UserModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserManagementController {
    @FXML
    private TableView<User> tblUsers;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, String> colAssignedEvents;
    @FXML
    private ComboBox<Event> cmbEvents;
    @FXML
    private Button btnAddUser, btnEditUser, btnDeleteUser, btnAssignEventToCoordinator, btnLogout;

    private UserBLL userBLL;
    private final UserModel userModel = UserModel.getInstance();
    private EventModel eventModel;
    private final EventBLL eventBLL = new EventBLL();

    public UserManagementController() {
        this.userBLL = new UserBLL();
        this.eventModel = new EventModel(new EventBLL());
    }

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAssignedEvents.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAssignedEventsString()));

        tblUsers.setItems(userModel.getUsers());

        cmbEvents.setItems(eventModel.getAllEvents());

        userModel.loadUsersFromDatabase();
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        Optional<User> result = showUserDialog(null);
        result.ifPresent(user -> {
            try {
                userModel.addUser(user.getUsername(), user.getHashedPassword(), user.getRole());
                showAlert("Success", "User added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Error saving user to database!", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void HandleLogout(ActionEvent event) {
        loadScene("Login.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketmanagementsystem/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load scene!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditUser(ActionEvent event) {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Optional<User> result = showUserDialog(selectedUser);
            result.ifPresent(updatedUser -> {
                try {
                    userBLL.updateUser(updatedUser);
                    int index = userModel.getUsers().indexOf(selectedUser);
                    if (index != -1) {
                        userModel.getUsers().set(index, updatedUser);
                    }
                    tblUsers.refresh();
                    showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Error", "Error updating user!", Alert.AlertType.ERROR);
                }
            });
        } else {
            showAlert("Warning", "Please select a user to edit.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Are you sure you want to delete " + selectedUser.getUsername() + "?");
            confirmation.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    userBLL.deleteUser(selectedUser);
                    userModel.getUsers().remove(selectedUser); // ✅ Correctly updates UI
                    showAlert("Success", "User deleted successfully!", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Error", "Error deleting user!", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Warning", "Please select a user to delete.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAssignEvent() {
        try {
            User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
            Event selectedEvent = cmbEvents.getSelectionModel().getSelectedItem();

            if (selectedUser == null || selectedEvent == null) {
                throw new IllegalArgumentException("Please select a coordinator and an event!");
            }

            if (!"coordinator".equalsIgnoreCase(selectedUser.getRole())) {
                throw new IllegalArgumentException("Only coordinators can be assigned events!");
            }

            eventBLL.assignCoordinator(selectedEvent.getId(), selectedUser.getId());

            userModel.updateAssignedEvents(selectedUser);
            tblUsers.refresh();

        } catch (IllegalArgumentException e) {
            handleException("Input Error", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            handleException("Unexpected Error", "An unexpected error occurred.", Alert.AlertType.ERROR);
        }
    }

    private void handleException(String title, String message, Alert.AlertType alertType) {
        showAlert(title, message, alertType);
        System.err.println("ERROR: " + message);
    }


    private Optional<User> showUserDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Add User" : "Edit User");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 20, 20));

        TextField usernameField = new TextField(user != null ? user.getUsername() : "");
        usernameField.setPrefWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Admin", "Event Coordinator");
        roleBox.setPrefWidth(200);
        if (user != null) roleBox.setValue(user.getRole());

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(usernameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                UUID id = (user != null) ? user.getId() : UUID.randomUUID();

                return new User(id, usernameField.getText(), passwordField.getText(), roleBox.getValue(), true);
            }
            return null;
        });

        return dialog.showAndWait();
    }
}

