package dk.easv.ticketmanagementsystem.DAO;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void createEvent(Event event) throws SQLException {
        String query = "INSERT INTO Events (id, name, location, date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, event.getId().toString());
            stmt.setString(2, event.getName());
            stmt.setString(3, event.getLocation());
            stmt.setTimestamp(4, Timestamp.valueOf(event.getStartTime()));
            stmt.setString(5, event.getNotes());
            stmt.executeUpdate();
        }
    }

    public void updateEvent(Event event) throws SQLException {
        String query = "UPDATE Events SET name=?, location=?, date=?, description=? WHERE id=?";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getLocation());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getStartTime()));
            stmt.setString(4, event.getNotes());
            stmt.setString(5, event.getId().toString());
            stmt.executeUpdate();
        }
    }

    public void deleteEvent(UUID eventId) throws SQLException {
        String query = "DELETE FROM Events WHERE id=?";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, eventId.toString());
            stmt.executeUpdate();
        }
    }

    public void assignCoordinator(UUID eventId, UUID coordinatorId) throws SQLException {
        String query = "INSERT INTO Event_Coordinators (event_id, coordinator_id) VALUES (?, ?)";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, eventId);
            stmt.setObject(2, coordinatorId);
            stmt.executeUpdate();
        }
    }

    public List<Event> getAssignedEvents(UUID coordinatorId) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.id, e.name, e.date, e.location, e.description " +
                "FROM Events e " +
                "JOIN Event_Coordinators ec ON e.id = ec.event_id " +
                "WHERE ec.coordinator_id = ?";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, coordinatorId);
            ResultSet rs = stmt.executeQuery();

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

    public List<User> getAssignedCoordinators(UUID eventId) throws SQLException {
        List<User> coordinators = new ArrayList<>();
        String query = "SELECT u.id, u.username, u.hashed_password, u.role FROM Users u " +
                "JOIN Event_Coordinators ec ON u.id = ec.coordinator_id " +
                "WHERE ec.event_id = ?";

        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, eventId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User coordinator = new User(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("username"),
                        rs.getString("hashed_password"),
                        rs.getString("role"),
                        false
                );
                coordinators.add(coordinator);
            }
        }
        return coordinators;
    }

    }
