/**
 * Service class for calculating the total production time from a list of events.
 */
package project.domain.service;

import project.domain.model.Event;

import java.util.List;

public class TotalProductionTime {

    private int totalTime;

    /**
     * Constructs a {@code TotalProductionTime} instance with an initial total time of zero.
     */
    public TotalProductionTime() {
        this.totalTime = 0;
    }

    /**
     * Calculates the total production time based on the end times of events in the provided list.
     * The total time is determined as the maximum end time among all events in the list.
     *
     * @param eventList The list of events to evaluate.
     * @return The total production time as an integer.
     */
    public int getTotalTime(List<Event> eventList) {
        for (Event event : eventList) {
            if (event.getEndTime() > totalTime) {
                totalTime = event.getEndTime();
            }
        }
        return totalTime;
    }
}
