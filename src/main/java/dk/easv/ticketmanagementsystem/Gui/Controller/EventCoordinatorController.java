package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.awt.event.ActionEvent;

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
        new Alert(Alert.AlertType.INFORMATION, "View My Events clicked").show();
    }

    @FXML
    private void handleCreateEvent(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Create Event clicked").show();
    }

    @FXML
    private void handleAssignCoordinators(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Assign Coordinators clicked").show();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Logging out...").show();
    }

    @FXML
    private void handleAddTicket(ActionEvent event) {
        tickets.add(new Ticket(1, "VIP", tickets.size() + 1, "Customer Name"));
    }

    @FXML
    private void handleDeleteTicket(ActionEvent event) {
        Ticket selectedTicket = tblTickets.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            tickets.remove(selectedTicket);
        }
    }
}

