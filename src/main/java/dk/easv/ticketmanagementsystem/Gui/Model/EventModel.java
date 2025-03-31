package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.EventBLL;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import dk.easv.ticketmanagementsystem.Interface.IEventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

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

    public ObservableList<Event> getAllEvents() {
        return events;
    }

    public ObservableList<Event> getEvents() {
        return events;
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
        loadAssignedEvents(coordinator);
    }

    public void loadAssignedEvents(User coordinator) {
        assignedEvents.clear();
        assignedEvents.addAll(eventBLL.getAssignedEvents(coordinator.getId()));
    }

    public boolean isCoordinatorAssigned(User coordinator, Event event) {
        try {
            return eventBLL.isCoordinatorAssigned(event.getId(), coordinator.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadEvents() {
        events.clear();
        events.addAll(eventService.getAllEvents());

        System.out.println("Loaded events: " + events.size());
        for (Event e : events) {
            System.out.println("Event: " + e.getName());
        }
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
