/**
 * A service class that calculates statistics for workstations, including
 * total time and percentage of global time used by each workstation.
 */
package project.domain.service;

import project.domain.model.Event;
import project.domain.model.WorkStation;

import java.util.*;

public class WorkstationStatistics {

    private Map<WorkStation, Integer> totalTimeWorkStation;
    private Map<WorkStation, Double> percentageTimeWorkStation;

    /**
     * Constructs a WorkstationStatistics instance, initializing internal maps.
     */
    public WorkstationStatistics() {
        totalTimeWorkStation = new HashMap<>();
        percentageTimeWorkStation = new HashMap<>();
    }

    /**
     * Calculates the total time used by each workstation based on a list of events.
     *
     * @param events A list of {@link Event} objects containing workstation data.
     */
    public void totalTimeWorkStation(List<Event> events) {
        for (Event event : events) {
            WorkStation workStation = event.getWorkStation();
            totalTimeWorkStation.merge(workStation, workStation.getTime(), Integer::sum);
        }
    }

    /**
     * Retrieves the total time used by each workstation.
     *
     * @return A map where the key is the {@link WorkStation} and the value is the total time used.
     */
    public Map<WorkStation, Integer> getTotalTimeWorkStation() {
        return totalTimeWorkStation;
    }

    /**
     * Sums the global time used across all workstations.
     *
     * @return The sum of all workstation times.
     */
    private int sumGlobalTime() {
        int sum = 0;
        for (Map.Entry<WorkStation, Integer> entry : totalTimeWorkStation.entrySet()) {
            Integer value = entry.getValue();
            sum += value;
        }
        return sum;
    }

    /**
     * Calculates the percentage of global time used by each workstation and sorts the results.
     */
    public void percentageTimeWorkStationGlobalTime() {
        int globalTime = sumGlobalTime();

        for (Map.Entry<WorkStation, Integer> entry : totalTimeWorkStation.entrySet()) {
            WorkStation workStation = entry.getKey();
            Integer value = entry.getValue();

            Double percentage = ((double) value / globalTime) * 100;
            percentageTimeWorkStation.put(workStation, percentage);
        }

        // Sort the map by percentage values
        List<Map.Entry<WorkStation, Double>> sortedList = new ArrayList<>(percentageTimeWorkStation.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        percentageTimeWorkStation = new LinkedHashMap<>();
        for (Map.Entry<WorkStation, Double> entry : sortedList) {
            percentageTimeWorkStation.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Retrieves the percentage of global time used by each workstation.
     *
     * @return A map where the key is the {@link WorkStation} and the value is the percentage of global time used.
     */
    public Map<WorkStation, Double> getPercentageTimeWorkStation() {
        return percentageTimeWorkStation;
    }
}
