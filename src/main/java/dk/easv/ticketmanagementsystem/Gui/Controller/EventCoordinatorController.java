package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.Ticket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;

public class EventCoordinatorController {
    @FXML
    private TableView<Ticket> tblTickets;
    @FXML
    private TableColumn<Ticket, Integer> colTicketID;
    @FXML
    private TableColumn<Ticket, String> colCustomerName;
    @FXML
    private TableColumn<Ticket, String> colTicketType;
    @FXML
    private Button btnAddTicket, btnDeleteTicket;
    @FXML
    private Button btnViewEvents, btnCreateEvent, btnAssignCoordinators, btnLogout;

    private final ObservableList<Ticket> tickets = FXCollections.observableArrayList();



    @FXML
    public void initialize() {
        colTicketID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colTicketType.setCellValueFactory(new PropertyValueFactory<>("ticketType"));

        tblTickets.setItems(tickets);
    }

    @FXML
    private void handleViewEvents(ActionEvent event) {
        loadScene("EventManagement.fxml");
    }

    @FXML
    private void handleCreateEvent(ActionEvent event) {
        loadScene("EventManagement.fxml");
    }

    @FXML
    private void handleAssignCoordinators(ActionEvent event) {
        loadScene("UserManagement.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("Login.fxml");
    }

    @FXML
    private void handleAddTicket(ActionEvent event) {
        Event selectedEvent = getSelectedEvent();
        if (selectedEvent != null) {
            Optional<Ticket> result = showTicketDialog(selectedEvent);
            result.ifPresent(tickets::add);
        } else {
            showAlert("No event selected. Please select an event in Event Management first.");
        }
    }

    private Optional<Ticket> showTicketDialog(Event event) {
        Dialog<Ticket> dialog = new Dialog<>();
        dialog.setTitle("Add Ticket");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField customerNameField = new TextField();
        TextField ticketTypeField = new TextField();

        grid.add(new Label("Customer Name:"), 0, 0);
        grid.add(customerNameField, 1, 0);
        grid.add(new Label("Ticket Type:"), 0, 1);
        grid.add(ticketTypeField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(customerNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Ticket(event, ticketTypeField.getText(), tickets.size() + 1, customerNameField.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }


    private Event getSelectedEvent() {
        return SelectedEventManager.getInstance().getSelectedEvent();
    }

    @FXML
    private void handleDeleteTicket(ActionEvent event) {
        Ticket selectedTicket = tblTickets.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this ticket?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    tickets.remove(selectedTicket);
                }
            });
        }
    }

    private void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketmanagementsystem/" + fxml));
            Parent root = loader.load();

            Stage stage = (Stage) tblTickets.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading " + fxml);
        }
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}

