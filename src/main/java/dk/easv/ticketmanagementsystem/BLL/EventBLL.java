package dk.easv.ticketmanagementsystem.BLL;

import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.DAO.EventDAL;
import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.Interface.IEventService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class EventBLL implements IEventService {
    private final EventDAL eventDAL = new EventDAL();

    @Override
    public List<Event> getAllEvents() {
        try {
            return eventDAL.getAllEvents();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading events", e);
        }
    }

    @Override
    public void createEvent(Event event) {
        try {
            eventDAL.createEvent(event);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating event", e);
        }
    }

    @Override
    public void updateEvent(Event event) {
        try {
            eventDAL.updateEvent(event);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating event", e);
        }
    }

    @Override
    public void deleteEvent(UUID eventId) {
        try {
            eventDAL.deleteEvent(eventId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting event", e);
        }
    }

    @Override
    public void assignCoordinator(Event event, User coordinator) {
        try {
            eventDAL.assignCoordinator(event, coordinator);
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning coordinator", e);
        }
    }
}
