package dk.easv.ticketmanagementsystem.DAO;

import dk.easv.ticketmanagementsystem.BE.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.SQLException;

public class EventDAL {
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM Events";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.add(new Event(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getString("location"),
                        rs.getString("description")
                ));
            }
        }
        return events;
    }
}
