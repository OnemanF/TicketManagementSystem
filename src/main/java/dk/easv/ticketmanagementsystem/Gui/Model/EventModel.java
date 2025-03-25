package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.Interface.IEventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventModel {
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObservableList<Event> assignedEvents = FXCollections.observableArrayList();
    private final IEventService eventService;

    public EventModel(IEventService eventService) {
        this.eventService = eventService;
        loadEvents();
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public ObservableList<Event> getAssignedEvents() {
        return assignedEvents;
    }

    public void addEvent(Event event) {
        eventService.createEvent(event);
        events.add(event);
    }

    public void deleteEvent(Event event) {
        eventService.deleteEvent(event.getId());
        events.remove(event);
    }

    public void assignEventToCoordinator(User coordinator, Event event) {
        eventService.assignCoordinator(event, coordinator);
        if (!assignedEvents.contains(event)) {
            assignedEvents.add(event);
        }
        event.addCoordinator(coordinator);
    }

    public void loadEvents() {
        events.clear();
        events.addAll(eventService.getAllEvents());
    }
    public void updateEvent(Event updatedEvent) {
        eventService.updateEvent(updatedEvent);

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(updatedEvent.getId())) {
                events.set(i, updatedEvent);
                break;
            }
        }
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
