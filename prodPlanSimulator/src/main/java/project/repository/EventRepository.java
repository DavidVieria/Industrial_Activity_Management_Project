package project.repository;

import project.domain.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing a collection of events.
 * This class provides methods to add, retrieve, and manage events.
 */
public class EventRepository {

    private List<Event> events;

    /**
     * Constructs an empty EventRepository.
     */
    public EventRepository() {
        this.events = new ArrayList<>();
    }

    /**
     * Adds a new event to the repository.
     *
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    public void addEvents(List<Event> events) {
        this.events.addAll(events);
    }

    /**
     * Retrieves all events in the repository.
     *
     * @return a list of all events
     */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public int getSize() {
        return events.size();
    }

    /**
     * Checks if the repository is empty.
     *
     * @return true if there are no events, false otherwise
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }

    /**
     * Clears all events from the repository.
     */
    public void clear() {
        events.clear();
    }
}
