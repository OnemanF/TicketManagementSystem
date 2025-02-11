package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;

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
    private Button btnAddUser, btnEditUser, btnDeleteUser, btnAssignEventToCoordinator;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAssignedEvents.setCellValueFactory(new PropertyValueFactory<>("assignedEvents"));

        tblUsers.setItems(users);
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        users.add(new User(users.size() + 1, "NewUser", "password"));
    }

    @FXML
    private void handleEditUser(ActionEvent event) {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setUsername("UpdatedUser");
            tblUsers.refresh();
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            users.remove(selectedUser);
        }
    }

    @FXML
    private void handleAssignEvent(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Assign Event to Coordinator clicked").show();
    }
}
