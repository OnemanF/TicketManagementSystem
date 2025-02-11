package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

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
    private Button btnAddEvent, btnEditEvent, btnDeleteEvent;

    private final ObservableList<Event> events = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colEventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        tblEvents.setItems(events);
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        events.add(new Event("New Notes", "New Location", LocalDateTime.now(), "New Event", events.size() + 1));
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
}

