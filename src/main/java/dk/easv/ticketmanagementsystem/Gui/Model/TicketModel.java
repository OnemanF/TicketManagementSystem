package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.Ticket;
import dk.easv.ticketmanagementsystem.BLL.TicketBLL;
import dk.easv.ticketmanagementsystem.Interface.ITicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketModel {
    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
    private final ObservableList<Ticket> filteredTickets = FXCollections.observableArrayList();
    private final ITicketService ticketService = new TicketBLL(); // Use interface

    public ObservableList<Ticket> getTickets() {
        return filteredTickets;
    }

    public void addTicket(Ticket ticket) {
        ticketService.addTicket(ticket);
        loadTicketsForEvent(ticket.getEvent());
    }

    public void deleteTicket(Ticket ticket) {
        ticketService.deleteTicket(ticket.getId());
        loadTicketsForEvent(ticket.getEvent());
    }

    public void loadTicketsForEvent(Event event) {
        filteredTickets.clear();
        try {
            filteredTickets.addAll(ticketService.getTicketsByEvent(event.getId()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load tickets", e);
        }
    }
}


