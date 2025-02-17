package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.EventManager;
import dk.easv.ticketmanagementsystem.BE.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketModel {
    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
    private final ObservableList<Ticket> filteredTickets = FXCollections.observableArrayList();

    public ObservableList<Ticket> getTickets() {
        return filteredTickets;
    }

    public void addTicket(Ticket ticket) {
        allTickets.add(ticket);
        loadTicketsForEvent(ticket.getEvent());
    }

    public void deleteTicket(Ticket ticket) {
        allTickets.remove(ticket);
        loadTicketsForEvent(ticket.getEvent());
    }

    public void loadTicketsForEvent(Event event) {
        filteredTickets.clear();
        for (Ticket ticket : allTickets) {
            if (ticket.getEvent().equals(event)) {
                filteredTickets.add(ticket);
            }
        }
    }
}


