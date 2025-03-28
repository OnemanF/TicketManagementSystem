package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.Ticket;
import dk.easv.ticketmanagementsystem.BLL.TicketBLL;
import dk.easv.ticketmanagementsystem.Gui.Controller.SelectedEventManager;
import dk.easv.ticketmanagementsystem.Interface.ITicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

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
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        Event event = ticket.getEvent();
        if (event == null) {
            System.out.println("Warning: Ticket event is null. Attempting to find event.");
            event = SelectedEventManager.getInstance().getSelectedEvent();
        }

        if (event == null) {
            throw new IllegalStateException("Cannot delete ticket without an event.");
        }

        ticketService.deleteTicket(ticket.getId());
        loadTicketsForEvent(event);
    }


    public void loadTicketsForEvent(Event event) {
        filteredTickets.clear();
        try {
            List<Ticket> tickets = ticketService.getTicketsByEvent(event.getId());


            tickets.forEach(ticket -> ticket.setEvent(event));

            filteredTickets.addAll(tickets);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load tickets", e);
        }
    }
}


