package dk.easv.ticketmanagementsystem.BE;

import java.time.LocalDateTime;

public class Event {
    private int id;
    private String name;
    private LocalDateTime startTime;
    private String location;
    private String notes;

    public Event(String notes, String location, LocalDateTime startTime, String name, int id) {
        this.notes = notes;
        this.location = location;
        this.startTime = startTime;
        this.name = name;
        this.id = id;
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


}
