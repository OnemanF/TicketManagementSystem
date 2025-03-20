package dk.easv.ticketmanagementsystem.BE;

import java.util.UUID;

public class Ticket {
    private UUID id;
    private String customerName;
    private String ticketType;
    private Event event;

    public Ticket(UUID id, Event event, String ticketType, String customerName) {
        this.id = id;
        this.event = event;
        this.ticketType = ticketType;
        this.customerName = customerName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}