package dk.easv.ticketmanagementsystem.BE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Event {
    private final UUID id;
    private String name;
    private LocalDateTime startTime;
    private String location;
    private String notes;
    private final List<User> assignedCoordinators;

    public Event(UUID id, String name, LocalDateTime startTime, String location, String notes) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.notes = notes != null ? notes : "";
        this.assignedCoordinators = new ArrayList<>();
    }

    public String getNotes() {
        return notes;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return startTime.toLocalDate();
    }

    public LocalTime getTime() {
        return startTime.toLocalTime();
    }

    public List<User> getAssignedCoordinators() {
        return Collections.unmodifiableList(assignedCoordinators);
    }

    public void addCoordinator(User user) {
        if (user != null && !assignedCoordinators.contains(user)) {
            assignedCoordinators.add(user);
            user.assignEvent(this);
        }
    }

    public String getCoordinatorsString() {
        return assignedCoordinators.stream()
                .map(User::getUsername)
                .collect(Collectors.joining(", "));
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
    }

    public void setLocation(String location) {
        this.location = Objects.requireNonNull(location, "Location cannot be null");
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
        //return String.format("%s (%s)", name, getStartTime().toString());
    }
}
