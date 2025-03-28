package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BE.UserManager;
import dk.easv.ticketmanagementsystem.BLL.EventBLL;
import dk.easv.ticketmanagementsystem.Gui.Model.EventModel;
import dk.easv.ticketmanagementsystem.Gui.Model.UserModel;
import dk.easv.ticketmanagementsystem.Interface.IEventService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private TableColumn<Event, String> colEventName, colLocation, colDate, colNotes;
    @FXML
    private TableColumn<Event, LocalDateTime> colStartTime;
    @FXML
    private Button btnAddEvent, btnEditEvent, btnDeleteEvent, btnAssignCoordinatorToMyEvent, btnBackToDashboard;
    @FXML
    private AnchorPane anchorPane;

    private final EventModel eventModel;
    private UserModel userModel;

    public EventManagementController() {
        IEventService eventService = new EventBLL();
        this.eventModel = new EventModel(eventService);
        this.userModel = new UserModel();
    }

    @FXML
    public void initialize() {
        setupIcons();
        setupTable();
        setupEventListeners();
        loadAvailableCoordinators();

        cmbCoordinators.setOnMouseClicked(event -> loadAvailableCoordinators());

        btnAssignCoordinatorToMyEvent.setOnAction(this::handleAssignCoordinator);
    }

    private void setupIcons() {
        btnAddEvent.setGraphic(createIcon("/dk/easv/ticketmanagementsystem/Icons/add.png"));
        btnEditEvent.setGraphic(createIcon("/dk/easv/ticketmanagementsystem/Icons/edit.png"));
        btnDeleteEvent.setGraphic(createIcon("/dk/easv/ticketmanagementsystem/Icons/delete.png"));
    }

    private ImageView createIcon(String path) {
        ImageView icon = new ImageView(getClass().getResource(path).toString());
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        return icon;
    }

    private void setupTable() {
        colEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        tblEvents.setItems(eventModel.getEvents());
    }

    private void setupEventListeners() {
        tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                SelectedEventManager.getInstance().setSelectedEvent(newSelection);
            }
        });
    }

    @FXML
    private void handleAddEvent() {
        Optional<Event> result = showAddEventDialog();
        result.ifPresent(event -> {
            eventModel.addEvent(event);
            tblEvents.refresh();
        });
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
                try {
                    LocalTime time = LocalTime.parse(timeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalDateTime startTime = LocalDateTime.of(datePicker.getValue(), time);

                    return new Event(UUID.randomUUID(), eventNameField.getText(), startTime, locationField.getText(), notesField.getText());
                } catch (DateTimeParseException e) {
                    showAlert("Invalid time format! Use HH:mm");
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    eventModel.deleteEvent(selectedEvent);
                }
            });
        } else {
            showAlert("Please select an event to delete.");
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketmanagementsystem/EventCoordinator.fxml"));
            Parent root = loader.load();

            EventCoordinatorController controller = loader.getController();
            controller.setEventModel(eventModel);

            Stage stage = (Stage) btnBackToDashboard.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error loading Event Coordinator Dashboard.");
        }
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Optional<Event> result = showEditEventDialog(selectedEvent);
            result.ifPresent(updatedEvent -> {
                eventModel.updateEvent(updatedEvent);
                tblEvents.refresh();
            });
        } else {
            showAlert("Please select an event to edit.");
        }
    }

    private Optional<Event> showEditEventDialog(Event event) {
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Edit Event");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        TextField eventNameField = new TextField(event.getName());
        TextField locationField = new TextField(event.getLocation());
        TextField notesField = new TextField(event.getNotes());
        DatePicker datePicker = new DatePicker(event.getDate());
        TextField timeField = new TextField(event.getTime().toString());

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

                    return new Event(event.getId(), eventNameField.getText(), startTime, locationField.getText(), notesField.getText());
                } catch (DateTimeParseException e) {
                    showAlert("Invalid time format! Use HH:mm");
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void loadAvailableCoordinators() {
        try {
            List<User> coordinators = UserManager.getInstance().getCoordinators();
            cmbCoordinators.setItems(FXCollections.observableArrayList(coordinators));
        } catch (Exception e) {
            showAlert("Error loading coordinators.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAssignCoordinator(ActionEvent event) {
        Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            showAlert("Please select an event.");
            return;
        }

        loadAvailableCoordinators();

        User selectedCoordinator = cmbCoordinators.getSelectionModel().getSelectedItem();

        if (selectedCoordinator == null) {
            showAlert("Please select a coordinator.");
            return;
        }

        try {
            eventModel.assignEventToCoordinator(selectedCoordinator, selectedEvent);

            eventModel.loadAssignedEvents(selectedCoordinator);

            showAlert("Coordinator assigned successfully!");
            tblEvents.refresh();
        } catch (Exception e) {
            showAlert("Error assigning coordinator.");
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}

