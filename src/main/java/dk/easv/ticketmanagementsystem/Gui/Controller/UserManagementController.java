package dk.easv.ticketmanagementsystem.Gui.Controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import dk.easv.ticketmanagementsystem.Gui.Model.UserModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
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
    private UserModel userModel;
    private ObservableList<User> users = FXCollections.observableArrayList();

    public UserManagementController() {
        this.userBLL = new UserBLL();
        this.userModel = new UserModel();
    }

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAssignedEvents.setCellValueFactory(new PropertyValueFactory<>("assignedEvents"));

        try {
            users.addAll(userBLL.getAllUsers()); // Load users from database
            tblUsers.setItems(users);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load users from database!", Alert.AlertType.ERROR);
        }

        loadEvents();
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        Optional<User> result = showUserDialog(null);
        result.ifPresent(user -> {
            try {
                userModel.addUser(user.getUsername(), user.getHashedPassword(), user.getRole());
                users.setAll(userModel.getUsers()); // Refresh UI
                showAlert("Success", "User added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) { // Catch generic exception
                showAlert("Error", "Error saving user to database!", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        });
    }

    private void loadEvents() {
        cmbEvents.setItems(EventManager.getInstance().getEvents());
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
                    users.set(users.indexOf(selectedUser), updatedUser); // Refresh UI
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
                    users.remove(selectedUser); // Remove from UI
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
    private void handleAssignEvent(ActionEvent event) {
        Event selectedEvent = cmbEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Warning", "Please select an event.", Alert.AlertType.WARNING);
            return;
        }

        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Warning", "Please select a user.", Alert.AlertType.WARNING);
            return;
        }

        if (!"Coordinator".equalsIgnoreCase(selectedUser.getRole())) {
            showAlert("Warning", "Only Coordinators can be assigned to events.", Alert.AlertType.WARNING);
            return;
        }

        selectedUser.assignEvent(selectedEvent);
        tblUsers.refresh();
        showAlert("Success", "User " + selectedUser.getUsername() + " assigned to event " + selectedEvent.getName(), Alert.AlertType.INFORMATION);
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
                // Hash the password
                String hashedPassword = BCrypt.withDefaults().hashToString(12, passwordField.getText().toCharArray());
                UUID id = (user != null) ? user.getId() : UUID.randomUUID();

                // Create the user without re-hashing the password
                return new User(id, usernameField.getText(), hashedPassword, roleBox.getValue(), false);
            }
            return null;
        });

        return dialog.showAndWait();
    }
}

