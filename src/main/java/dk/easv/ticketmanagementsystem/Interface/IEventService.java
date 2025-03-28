package dk.easv.ticketmanagementsystem.Interface;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;

import java.util.List;
import java.util.UUID;

public interface IEventService {
    List<Event> getAllEvents();
    void createEvent(Event event);
    void updateEvent(Event event);
    void deleteEvent(UUID eventId);
    void assignCoordinator(UUID eventId, UUID coordinatorId);
    List<User> getAssignedCoordinators(Event event);
}
