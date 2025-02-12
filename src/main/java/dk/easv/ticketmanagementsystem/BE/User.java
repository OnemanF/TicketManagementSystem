package dk.easv.ticketmanagementsystem.BE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private List<Event> assignedEvents; // List of event names assigned to coordinators

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.assignedEvents = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Event> getAssignedEvents() {
        return assignedEvents;
    }

    public void assignEvent(Event event) {
        if (!assignedEvents.contains(event)) {
            this.assignedEvents.add(event);
            event.addCoordinator(this);  // Ensure event also tracks assigned coordinators
        }
    }

    public String getAssignedEventsString() {
        return assignedEvents.stream().map(Event::getName).collect(Collectors.joining(", "));
    }

}

