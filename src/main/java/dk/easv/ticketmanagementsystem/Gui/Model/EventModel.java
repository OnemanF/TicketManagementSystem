package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventModel {
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObservableList<Event> assignedEvents = FXCollections.observableArrayList();

    public ObservableList<Event> getEvents() {
        return events;
    }

    public ObservableList<Event> getAssignedEvents() {
        return assignedEvents;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void deleteEvent(Event event) {
        events.remove(event);
    }

    public void assignEventToCoordinator(User coordinator, Event event) {
        if (!assignedEvents.contains(event)) {
            assignedEvents.add(event);
        }
        event.addCoordinator(coordinator);
    }

    public void loadAssignedEvents(User coordinator) {
        assignedEvents.clear();
        for (Event event : events) {
            if (event.getAssignedCoordinators().contains(coordinator)) {
                assignedEvents.add(event);
            }
        }
    }
}
