package dk.easv.ticketmanagementsystem.Interface;

import dk.easv.ticketmanagementsystem.BE.Ticket;

import java.util.List;
import java.util.UUID;

public interface ITicketService {
    void addTicket(Ticket ticket);
    List<Ticket> getTicketsByEvent(UUID eventId);
    void deleteTicket(UUID ticketId);
}
