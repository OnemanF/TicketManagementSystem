package dk.easv.ticketmanagementsystem.BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventManager {
    private static EventManager instance = null;
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObservableList<Event> assignedEvents = FXCollections.observableArrayList();
    private Event selectedEvent;

    private EventManager() {}

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public ObservableList<Event> getAssignedEvents() {
        return assignedEvents;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event event) {
        this.selectedEvent = event;
    }
}