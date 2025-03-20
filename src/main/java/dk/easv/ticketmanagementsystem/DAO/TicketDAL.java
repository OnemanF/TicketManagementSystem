package dk.easv.ticketmanagementsystem.DAO;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.Ticket;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TicketDAL {
    public void addTicket(Ticket ticket) throws SQLException {
        String query = "INSERT INTO Tickets (id, event_id, customer_name, ticket_type) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, ticket.getId()); // UUID
            stmt.setObject(2, ticket.getEvent().getId()); // UUID
            stmt.setString(3, ticket.getCustomerName());
            stmt.setString(4, ticket.getTicketType());
            stmt.executeUpdate();
        }
    }

    public Ticket getTicketById(UUID ticketId) throws SQLException {
        String query = "SELECT * FROM Tickets WHERE id = ?";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            UUID.fromString(rs.getString("id")),
                            new Event(UUID.fromString(rs.getString("event_id")), rs.getString("event_name"), rs.getTimestamp("date").toLocalDateTime(), rs.getString("location"), rs.getString("description")),
                            rs.getString("ticket_type"),
                            rs.getString("customer_name")
                    );
                }
            }
        }
        return null;
    }
}
