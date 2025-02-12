package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class EventManagementController {
    @FXML
    private TableView<Event> tblEvents;
    @FXML
    private TableColumn<Event, String> colEventName;
    @FXML
    private TableColumn<Event, String> colLocation;
    @FXML
    private TableColumn<Event, String> colDate;
    @FXML
    private TableColumn<Event, LocalDateTime> colStartTime;
    @FXML
    private Button btnAddEvent, btnEditEvent, btnDeleteEvent, btnAssignCoordinatorToMyEvent;

    private final ObservableList<Event> events = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        tblEvents.setItems(events);

        tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                SelectedEventManager.getInstance().setSelectedEvent(newSelection);
            }
        });
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        Optional<Event> result = showAddEventDialog();
        result.ifPresent(events::add);
    }

    private Optional<Event> showAddEventDialog() {
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Add New Event");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        TextField eventNameField = new TextField();
        TextField locationField = new TextField();
        TextField notesField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();

        grid.add(new Label("Event Name:"), 0, 0);
        grid.add(eventNameField, 1, 0);
        grid.add(new Label("Location:"), 0, 1);
        grid.add(locationField, 1, 1);
        grid.add(new Label("Notes:"), 0, 2);
        grid.add(notesField, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(new Label("Start Time:"), 0, 4);
        grid.add(timeField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(eventNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String eventName = eventNameField.getText();
                String location = locationField.getText();
                String notes = notesField.getText();
                LocalTime time;
                try {
                    time = LocalTime.parse(timeField.getText());
                } catch (DateTimeParseException e) {
                    showAlert("Invalid time format! Use HH:mm");
                    return null;
                }
                LocalDateTime startTime = LocalDateTime.of(datePicker.getValue(), time);


                Event newEvent = new Event(events.size() + 1, eventName, startTime, location, notes);
                return newEvent;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            selectedEvent.setName("Updated Event");
            tblEvents.refresh();
        }
    }

    @FXML
    private void handleDeleteEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            events.remove(selectedEvent);
        }
    }

    @FXML
    private void handleAssignCoordinatorToMyEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            showAlert("Please select an event to assign a coordinator.");
            return;
        }

        Optional<User> result = showAssignCoordinatorDialog();
        result.ifPresent(coordinator -> {
            selectedEvent.addCoordinator(coordinator);
            System.out.println("Assigned " + coordinator.getUsername() + " to Event: " + selectedEvent.getName());
        });
    }

    private Optional<User> showAssignCoordinatorDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Assign Coordinator");

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        ComboBox<User> comboCoordinators = new ComboBox<>();
        comboCoordinators.setItems(FXCollections.observableArrayList(getAvailableCoordinators()));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.add(new Label("Select Coordinator:"), 0, 0);
        grid.add(comboCoordinators, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType && comboCoordinators.getValue() != null) {
                return comboCoordinators.getValue();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private List<User> getAvailableCoordinators() {
        return List.of(
                new User(1, "coordinator1", "password1", "Coordinator"),
                new User(2, "coordinator2", "password2", "Coordinator")
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

