package dk.easv.ticketmanagementsystem.Gui.Controller;

import dk.easv.ticketmanagementsystem.BE.Event;

public class SelectedEventManager {
    private static SelectedEventManager instance;
    private Event selectedEvent;

    private SelectedEventManager() {}

    public static SelectedEventManager getInstance() {
        if (instance == null) {
            instance = new SelectedEventManager();
        }
        return instance;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event event) {
        this.selectedEvent = event;
    }
}

