package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BE.UserManager;
import dk.easv.ticketmanagementsystem.Gui.Model.EventModel;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventManagementController {
    @FXML
    private TableView<Event> tblEvents;
    @FXML
    private ComboBox<User> cmbCoordinators;
    @FXML
    private TableColumn<Event, String> colEventName;
    @FXML
    private TableColumn<Event, String> colLocation;
    @FXML
    private TableColumn<Event, String> colDate;
    @FXML
    private TableColumn<Event, LocalDateTime> colStartTime;
    @FXML
    private TableColumn<Event, String> colNotes;
    @FXML
    private Button btnAddEvent, btnEditEvent, btnDeleteEvent, btnAssignCoordinatorToMyEvent;

    private EventModel eventModel;

    public EventManagementController(){
        this.eventModel = new EventModel();
    }

    @FXML
    public void initialize() {
        ImageView addIcon = new ImageView(getClass().getResource("/dk/easv/ticketmanagementsystem/Icons/add.png").toString());
        addIcon.setFitWidth(24);
        addIcon.setFitHeight(24);
        btnAddEvent.setGraphic(addIcon);
        ImageView editIcon = new ImageView(getClass().getResource("/dk/easv/ticketmanagementsystem/Icons/edit.png").toString());
        editIcon.setFitWidth(24);
        editIcon.setFitHeight(24);
        btnEditEvent.setGraphic(editIcon);
        ImageView deleteIcon = new ImageView(getClass().getResource("/dk/easv/ticketmanagementsystem/Icons/delete.png").toString());
        deleteIcon.setFitWidth(24);
        deleteIcon.setFitHeight(24);
        btnDeleteEvent.setGraphic(deleteIcon);

        colEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        tblEvents.setItems(eventModel.getEvents());

        tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                SelectedEventManager.getInstance().setSelectedEvent(newSelection);
            }
        });

        loadAvailableCoordinators();
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        Optional<Event> result = showAddEventDialog();
        result.ifPresent(eventModel::addEvent);
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

                if (eventName.isEmpty() || location.isEmpty() || notes.isEmpty() || datePicker.getValue() == null) {
                    showAlert("All fields must be filled!");
                    return null;
                }

                LocalTime time;
                try {
                    time = LocalTime.parse(timeField.getText());
                } catch (DateTimeParseException e) {
                    showAlert("Invalid time format! Use HH:mm");
                    return null;
                }

                LocalDateTime startTime = LocalDateTime.of(datePicker.getValue(), time);

                UUID newEventId = UUID.randomUUID();  // Generate a unique UUID instead of an int

                return new Event(newEventId, eventName, startTime, location, notes);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private Optional<Event> showEditEventDialog(Event event) {
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Edit Event");

        ButtonType saveButtonType = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        TextField eventNameField = new TextField(event.getName());
        TextField locationField = new TextField(event.getLocation());
        TextField notesField = new TextField(event.getNotes());
        DatePicker datePicker = new DatePicker(event.getDate());
        TextField timeField = new TextField(event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        grid.add(new Label("Event Name:"), 0, 0);
        grid.add(eventNameField, 1, 0);
        grid.add(new Label("Location:"), 0, 1);
        grid.add(locationField, 1, 1);
        grid.add(new Label("Notes:"), 0, 2);
        grid.add(notesField, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(new Label("Start Time (HH:mm):"), 0, 4);
        grid.add(timeField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(eventNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    LocalTime time = LocalTime.parse(timeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalDateTime startTime = LocalDateTime.of(datePicker.getValue(), time);

                    event.setName(eventNameField.getText());
                    event.setLocation(locationField.getText());
                    event.setNotes(notesField.getText());
                    event.setStartTime(startTime);

                    return event;
                } catch (DateTimeParseException e) {
                    showAlert("Invalid time format! Use HH:mm");
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Please select an event to edit.");
            return;
        }

        Optional<Event> result = showEditEventDialog(selectedEvent);
        result.ifPresent(updatedEvent -> tblEvents.refresh());
    }

    @FXML
    private void handleDeleteEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            eventModel.deleteEvent(selectedEvent);
        }
    }

    @FXML
    private void handleAssignCoordinatorToMyEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            showAlert("Please select an event.");
            return;
        }

        Optional<User> result = showAssignCoordinatorDialog();

        result.ifPresent(coordinator -> {
            eventModel.assignEventToCoordinator(coordinator, selectedEvent);
            showAlert("Assigned " + coordinator.getUsername() + " to " + selectedEvent.getName());
        });
    }

    private Optional<User> showAssignCoordinatorDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Assign Coordinator");

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        ComboBox<User> comboCoordinators = new ComboBox<>();
        comboCoordinators.setItems(getAvailableCoordinators());

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

    private void loadAvailableCoordinators() {
        ObservableList<User> coordinators = UserManager.getInstance().getCoordinators();
        cmbCoordinators.setItems(FXCollections.observableArrayList(coordinators));
    }

    private ObservableList<User> getAvailableCoordinators() {
        return FXCollections.observableArrayList(UserManager.getInstance().getCoordinators());
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        loadScene("EventCoordinator.fxml");
    }

    private void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketmanagementsystem/" + fxml));
            Parent root = loader.load();

            Stage stage = (Stage) tblEvents.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlertLoad("Error loading " + fxml);
        }
    }

    private void showAlertLoad(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}

