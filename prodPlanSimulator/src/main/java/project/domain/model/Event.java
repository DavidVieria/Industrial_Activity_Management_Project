package project.domain.model;

import java.util.Objects;

/**
 * A class representing an event involving an item and a workstation,
 * defined by its start and end times. This class implements the
 * Comparable interface to allow events to be compared based on their end times.
 */
public class Event implements Comparable<Event> {

    private Article article;
    private WorkStation workStation;
    private int startTime;
    private int endTime;

    /**
     * Constructs an Event with the specified parameters.
     *
     * @param article        the item associated with the event
     * @param workStation the workstation where the event takes place
     * @param startTime   the start time of the event
     */
    public Event(Article article, WorkStation workStation, int startTime) {
        this.article = article;
        this.workStation = workStation;
        this.startTime = startTime;
        this.endTime = startTime + workStation.getTime();
    }

    /**
     * Gets the item associated with the event.
     *
     * @return the associated item
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Gets the workstation where the event takes place.
     *
     * @return the associated workstation
     */
    public WorkStation getWorkStation() {
        return workStation;
    }

    /**
     * Gets the start time of the event.
     *
     * @return the start time
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return the end time
     */
    public int getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return startTime == event.startTime
                && endTime == event.endTime
                && Objects.equals(article, event.article)
                && Objects.equals(workStation, event.workStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(article, workStation, startTime, endTime);
    }

    @Override
    public String toString() {
        return "Event:  " +
                "Item ID = '" + article.getIdArticle() + '\'' +
                ", Workstation ID = '" + workStation.getWorkStationID() + '\'' +
                ", Start time = " + startTime +
                ", End time = " + endTime;
    }

    /**
     * Compares this event with another event based on their end times.
     *
     * @param o the event to be compared
     * @return a negative integer, zero, or a positive integer as this event's
     *         end time is less than, equal to, or greater than the specified event's end time
     */
    @Override
    public int compareTo(Event o) {
        return Integer.compare(this.getEndTime(), o.getEndTime());
    }
}
