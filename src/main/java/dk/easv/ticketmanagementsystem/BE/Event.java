package dk.easv.ticketmanagementsystem.BE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Event {
    private final UUID id;
    private String name;
    private LocalDateTime startTime;
    private String location;
    private String notes;
    private final List<User> assignedCoordinators  = new ArrayList<>();

    public Event(UUID id, String name, LocalDateTime startTime, String location, String notes) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.notes = (notes != null) ? notes : "";
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDate getDate() {
        return startTime.toLocalDate();
    }

    public LocalTime getTime() {
        return startTime.toLocalTime();
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public List<User> getCoordinators() {
        return Collections.unmodifiableList(assignedCoordinators );
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
        this.notes = (notes != null) ? notes : "";
    }

    public void addCoordinator(User coordinator) {
        if (coordinator != null && !assignedCoordinators.contains(coordinator)) {
            assignedCoordinators.add(coordinator);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Event)) return false;
        Event event = (Event) obj;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public void setCoordinators(List<User> coordinators) {
        assignedCoordinators.clear();
        assignedCoordinators.addAll(coordinators);
    }

    public List<User> getAssignedCoordinators() {
        return Collections.unmodifiableList(assignedCoordinators);
    }

}