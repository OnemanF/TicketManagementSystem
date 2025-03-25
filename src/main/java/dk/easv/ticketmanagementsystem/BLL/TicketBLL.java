package dk.easv.ticketmanagementsystem.BLL;

import dk.easv.ticketmanagementsystem.BE.Ticket;
import dk.easv.ticketmanagementsystem.DAO.TicketDAL;
import dk.easv.ticketmanagementsystem.Interface.ITicketService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TicketBLL implements ITicketService {
    private final TicketDAL ticketDAL = new TicketDAL();

    public void addTicket(Ticket ticket) {
        try {
            ticketDAL.addTicket(ticket);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding ticket", e);
        }
    }

    public List<Ticket> getTicketsByEvent(UUID eventId) {
        try {
            return ticketDAL.getTicketsByEvent(eventId);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading tickets", e);
        }
    }

    public void deleteTicket(UUID ticketId) {
        try {
            ticketDAL.deleteTicket(ticketId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting ticket", e);
        }
    }
}
