package dk.easv.ticketmanagementsystem.DAO;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.Ticket;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketDAL {
    public void addTicket(Ticket ticket) throws SQLException {
        String query = "INSERT INTO Tickets (id, event_id, customer_name, customer_email, ticket_type, qr_code, barcode) VALUES (?, ?, ?, ?, ?, NEWID(), NEWID())";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, ticket.getId());
            stmt.setObject(2, ticket.getEvent().getId());
            stmt.setString(3, ticket.getCustomerName());
            stmt.setString(4, ticket.getCustomerEmail());
            stmt.setString(5, ticket.getTicketType());
            stmt.executeUpdate();
        }
    }

    public List<Ticket> getTicketsByEvent(UUID eventId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT id, customer_name, customer_email, ticket_type, event_id FROM Tickets WHERE event_id = ? OR event_id IS NULL";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID ticketId = UUID.fromString(rs.getString("id"));
                    String ticketType = rs.getString("ticket_type");
                    String customerName = rs.getString("customer_name");
                    String customerEmail = rs.getString("customer_email");
                    UUID eventUUID = rs.getObject("event_id", UUID.class);

                    Event event = (eventUUID != null) ? new Event(
                            eventUUID, "Event Name Placeholder",
                            LocalDateTime.now(), "Placeholder Location",
                            "Placeholder Notes"
                    ) : null;

                    boolean isSpecialTicket = (eventUUID == null);
                    Ticket ticket = new Ticket(ticketId, event, ticketType, customerName, customerEmail, isSpecialTicket);
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }

    public void deleteTicket(UUID ticketId) throws SQLException {
        String query = "DELETE FROM Tickets WHERE id = ?";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, ticketId);
            stmt.executeUpdate();
        }
    }

}
