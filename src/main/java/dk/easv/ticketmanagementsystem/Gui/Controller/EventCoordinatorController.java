package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.*;
import dk.easv.ticketmanagementsystem.BLL.EventBLL;
import dk.easv.ticketmanagementsystem.Gui.Model.EventModel;
import dk.easv.ticketmanagementsystem.Gui.Model.TicketModel;
import dk.easv.ticketmanagementsystem.Interface.IEventService;
import javafx.application.Platform;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TableColumn<Ticket, String> colTicketEmail;
    @FXML
    private Button btnAddTicket, btnDeleteTicket;
    @FXML
    private Button btnViewEvents, btnCreateEvent, btnAssignCoordinators, btnLogout;
    @FXML
    private ComboBox<Event> cmbEventSelection;

    private TicketModel ticketModel;

    private EventModel eventModel;

    public EventCoordinatorController() {
        this.ticketModel = new TicketModel();
    }

    public void setEventModel(EventModel eventModel) {
        this.eventModel = eventModel;
        loadAssignedEvents();
    }

    @FXML
    public void initialize() {
        colTicketID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colTicketType.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
        colTicketEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));

        tblTickets.setItems(ticketModel.getTickets());

        if (eventModel == null){
            IEventService eventService = new EventBLL();
            eventModel = new EventModel(eventService);
        }

            loadAssignedEvents();
            setupEventSelectionListener();
    }


    private void loadAssignedEvents() {
        if (eventModel != null) {
            eventModel.loadEvents();
            cmbEventSelection.setItems(eventModel.getEvents());
        }
    }

    private void setupEventSelectionListener() {
        cmbEventSelection.getSelectionModel().selectedItemProperty().addListener((obs, oldEvent, newEvent) -> {
            if (newEvent != null) {
                SelectedEventManager.getInstance().setSelectedEvent(newEvent);
                ticketModel.loadTicketsForEvent(newEvent);
            }
        });
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
        loadScene("EventManagement.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("Login.fxml");
    }

    @FXML
    private void handleAddTicket(ActionEvent event) {
        Event selectedEvent = cmbEventSelection.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Optional<Ticket> result = showTicketDialog(selectedEvent);
            result.ifPresent(ticket -> {
                ticketModel.addTicket(ticket);
            });
        } else {
            showAlert("Please select an event first.");
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
        TextField customerEmailField = new TextField();

        ComboBox<String> cmbTicketType = new ComboBox<>();
        cmbTicketType.setEditable(true);
        cmbTicketType.getItems().addAll("VIP", "Food Included", "Front Row", "Free Beer", "Standard");

        CheckBox chkSpecialTicket = new CheckBox("Special Food/Drink Ticket");
        ComboBox<String> cmbSpecialTicketType = new ComboBox<>();
        cmbSpecialTicketType.getItems().addAll("One Free Beer", "50% Off One Drink", "1 Set of Free Earplugs");
        cmbSpecialTicketType.setDisable(true);

        chkSpecialTicket.selectedProperty().addListener((obs, oldVal, newVal) -> {
            cmbSpecialTicketType.setDisable(!newVal);
            customerNameField.setDisable(newVal);
            customerEmailField.setDisable(newVal);
            cmbTicketType.setDisable(newVal);
        });

        grid.add(new Label("Customer Name:"), 0, 0);
        grid.add(customerNameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(customerEmailField, 1, 1);
        grid.add(new Label("Ticket Type:"), 0, 2);
        grid.add(cmbTicketType, 1, 2);
        grid.add(chkSpecialTicket, 0, 3);
        grid.add(cmbSpecialTicketType, 1, 3);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(customerNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                UUID ticketId = UUID.randomUUID();
                boolean isSpecial = chkSpecialTicket.isSelected();
                String ticketType = isSpecial ? cmbSpecialTicketType.getValue() : cmbTicketType.getValue();
                return new Ticket(ticketId, event, ticketType, customerNameField.getText(), customerEmailField.getText(), isSpecial);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    @FXML
    private void handleDeleteTicket(ActionEvent event) {
            Ticket selectedTicket = tblTickets.getSelectionModel().getSelectedItem();

            if (selectedTicket == null) {
                System.out.println("Error: No ticket selected.");
                return;
            }

            ticketModel.deleteTicket(selectedTicket);

    }

    @FXML
    private void handlePrintTicket(ActionEvent event) {
        Ticket selectedTicket = tblTickets.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            TicketPrinter.printTicket(selectedTicket);
        } else {
            showAlert("Please select a ticket to print.");
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
            Logger.getLogger(EventCoordinatorController.class.getName()).log(Level.SEVERE, "Error loading " + fxml, e);
            showAlert("Error loading " + fxml);
        }
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}

