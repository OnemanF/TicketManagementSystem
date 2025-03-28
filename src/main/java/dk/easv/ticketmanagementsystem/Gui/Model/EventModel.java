package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.EventBLL;
import dk.easv.ticketmanagementsystem.Interface.IEventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class EventModel {
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObservableList<Event> assignedEvents = FXCollections.observableArrayList();
    private final IEventService eventService;
    private final EventBLL eventBLL;

    public EventModel(IEventService eventService) {
        this.eventService = eventService;
        this.eventBLL = new EventBLL();
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
        eventBLL.assignCoordinator(event.getId(), coordinator.getId());

        loadAssignedCoordinators(event);

        if (!assignedEvents.contains(event)) {
            assignedEvents.add(event);
        }
        event.addCoordinator(coordinator);
        loadAssignedEvents(coordinator);
    }

    public void loadAssignedEvents(User coordinator) {
        assignedEvents.clear();
        List<Event> assignedEventsList = eventBLL.getAssignedEvents(coordinator.getId());
        assignedEvents.addAll(assignedEventsList);
    }

    public void loadAssignedCoordinators(Event event) {
        List<User> coordinators = eventBLL.getAssignedCoordinators(event);
        event.setCoordinators(coordinators);
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
}
