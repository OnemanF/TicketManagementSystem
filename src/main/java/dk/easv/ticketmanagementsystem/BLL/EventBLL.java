package dk.easv.ticketmanagementsystem.BLL;

import dk.easv.ticketmanagementsystem.DAO.EventDAL;
import dk.easv.ticketmanagementsystem.BE.Event;

import java.sql.SQLException;
import java.util.List;

public class EventBLL {
    private EventDAL eventDAL = new EventDAL();

    public List<Event> getAllEvents() throws SQLException {
        return eventDAL.getAllEvents();
    }
}
