package dk.easv.ticketmanagementsystem.BE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Event {
    private int id;
    private String name;
    private LocalDateTime startTime;
    private String location;
    private String notes;
    private List<User> assignedCoordinators; // Stores users assigned to this event

    public Event(int id, String name, LocalDateTime startTime, String location, String notes) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.location = location;
        this.notes = notes;
        this.assignedCoordinators = new ArrayList<>();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getAssignedCoordinators() {
        return assignedCoordinators;
    }

    public void addCoordinator(User user) {
        if (!assignedCoordinators.contains(user)) {
            this.assignedCoordinators.add(user);
            user.assignEvent(this);  // Ensure the user also tracks this event
        }
    }

    public String getCoordinatorsString() {
        return assignedCoordinators.stream().map(User::getUsername).collect(Collectors.joining(", "));
    }
}
