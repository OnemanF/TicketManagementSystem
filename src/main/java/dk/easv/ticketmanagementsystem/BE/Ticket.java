package dk.easv.ticketmanagementsystem.BE;

public class Ticket {
    private int id;
    private String customerName;
    private String ticketType;
    private int eventId;

    public Ticket(int eventId, String ticketType, int id, String customerName) {
        this.eventId = eventId;
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

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


}
