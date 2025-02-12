package dk.easv.ticketmanagementsystem.BE;

public class Ticket {
    private int id;
    private String customerName;
    private String ticketType;
    private Event event;

    public Ticket(Event event, String ticketType, int id, String customerName) {
        this.event = event;
        this.ticketType = ticketType;
        this.id = id;
        this.customerName = customerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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