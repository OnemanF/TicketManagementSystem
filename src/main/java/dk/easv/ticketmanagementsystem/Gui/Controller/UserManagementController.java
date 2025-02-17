package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.User;
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
import java.util.Optional;

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

    private UserModel userModel;

    public UserManagementController(){
        this.userModel = new UserModel();
    }

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAssignedEvents.setCellValueFactory(new PropertyValueFactory<>("assignedEvents"));

        tblUsers.setItems(userModel.getUsers());
        loadEvents();
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        Optional<User> result = showUserDialog(null);
        result.ifPresent(userModel::addUser);
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
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditUser(ActionEvent event) {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Optional<User> result = showUserDialog(selectedUser);
            result.ifPresent(updatedUser -> {
                selectedUser.setUsername(updatedUser.getUsername());
                selectedUser.setRole(updatedUser.getRole());
                tblUsers.refresh();
            });
        } else {
            showAlert("No user selected", "Please select a user to edit.", Alert.AlertType.WARNING);
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
                userModel.deleteUser(selectedUser);
            }
        } else {
            showAlert("No user selected", "Please select a user to delete.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void handleAssignEvent(ActionEvent event) {
        Event selectedEvent = cmbEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Please select an event.");
            return;
        }

        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Please select a user.");
            return;
        }

        if (!"Coordinator".equalsIgnoreCase(selectedUser.getRole())) {
            showAlert("Only Coordinators can be assigned to events.");
            return;
        }

        selectedUser.assignEvent(selectedEvent);
        tblUsers.refresh();
        showAlert("User " + selectedUser.getUsername() + " assigned to event " + selectedEvent.getName());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
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
                return new User(userModel.getUsers().size() + 1, usernameField.getText(), passwordField.getText(), roleBox.getValue());
            }
            return null;
        });

        return dialog.showAndWait();
    }
}


