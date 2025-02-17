package dk.easv.ticketmanagementsystem.BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventManager {
    private static final EventManager instance = new EventManager();
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private Event selectedEvent;

    private EventManager() {}

    public static EventManager getInstance() {
        return instance;
    }

    public ObservableList<Event> getEvents() {
        return events;
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