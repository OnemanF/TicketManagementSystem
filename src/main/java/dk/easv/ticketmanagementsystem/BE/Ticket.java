package dk.easv.ticketmanagementsystem.BE;

import java.util.UUID;

public class Ticket {
    private UUID id;
    private Event event;
    private String ticketType;
    private String customerName;
    private String customerEmail;
    private boolean isSpecialTicket;
    private String qrCode;

    public Ticket(UUID id, Event event, String ticketType, String customerName, String customerEmail, boolean isSpecialTicket) {
        this.id = id;
        this.event = event;
        this.ticketType = ticketType;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.isSpecialTicket = isSpecialTicket;
        this.qrCode = generateUniqueCode();
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    @Override
    public String toString() {
        return String.format("Ticket[id=%s, Name=%s, Email=%s, Type=%s]",
                id, customerName, customerEmail, ticketType);
    }
}