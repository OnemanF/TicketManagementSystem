package dk.easv.ticketmanagementsystem.BE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {
    private UUID id;
    private String username;
    private String hashedPassword;
    private String role;
    private boolean isActive;
    private List<Event> assignedEvents;

    public User(UUID id, String username, String hashedPassword, String role, boolean isActive) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.assignedEvents = new ArrayList<>();
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean verifyPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return BCrypt.verifyer().verify(password.toCharArray(), this.hashedPassword).verified;
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String password) {

        if (password != null && !password.startsWith("$2a$")) {
            this.hashedPassword = hashPassword(password);
        } else {
            this.hashedPassword = password;
        }
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Event> getAssignedEvents() { return assignedEvents; }

    public void assignEvent(Event event) {
        if (!assignedEvents.contains(event)) {
            this.assignedEvents.add(event);
            event.addCoordinator(this);
        }
    }

    public String getAssignedEventsString() {
        return assignedEvents.stream().map(Event::getName).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return getUsername();
    }
}


