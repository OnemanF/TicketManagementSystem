package dk.easv.ticketmanagementsystem.DAO;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.Ticket;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = "SELECT id, customer_name, customer_email, ticket_type FROM Tickets WHERE event_id = ?";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new Ticket(
                            UUID.fromString(rs.getString("id")),
                            null,  // Event can be set externally
                            rs.getString("ticket_type"),
                            rs.getString("customer_name"),
                            rs.getString("customer_email")
                    ));
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
