/**
 * A service class for calculating and managing operation statistics,
 * including execution time, average time, and waiting time for operations.
 */
package project.domain.service;

import project.domain.model.Article;
import project.domain.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationStatistics {

    private Map<Article, List<Event>> eventsByItem;
    private Map<String, List<Event>> eventsByOperation;
    private Map<String, Integer> totalTimeOperation;
    private Map<String, Double> averageTimeOperation;
    private Map<String, Integer> waitTimeOperation;

    /**
     * Constructs an OperationStatistics instance, initializing all internal maps.
     */
    public OperationStatistics() {
        eventsByItem = new HashMap<>();
        eventsByOperation = new HashMap<>();
        totalTimeOperation = new HashMap<>();
        averageTimeOperation = new HashMap<>();
        waitTimeOperation = new HashMap<>();
    }

    /**
     * Populates the {@code eventsByOperation} map by grouping events based on their operations.
     *
     * @param events A list of {@link Event} objects to be grouped.
     */
    public void addEventByOperation(List<Event> events) {
        for (Event event : events) {
            String operation = event.getWorkStation().getOperation();
            eventsByOperation
                    .computeIfAbsent(operation, k -> new ArrayList<>())
                    .add(event);
        }
    }

    /**
     * Retrieves the events grouped by their operations.
     *
     * @return A map where the key is the operation name and the value is a list of events for that operation.
     */
    public Map<String, List<Event>> getEventsByOperation() {
        return eventsByOperation;
    }

    /**
     * Calculates the total execution time for each operation based on their events.
     */
    public void executionTimeOperation() {
        for (Map.Entry<String, List<Event>> entry : eventsByOperation.entrySet()) {
            String operation = entry.getKey();
            List<Event> events = entry.getValue();

            int totalTime = 0;
            for (Event event : events) {
                totalTime += event.getEndTime() - event.getStartTime();
            }

            totalTimeOperation.put(operation, totalTime);
        }
    }

    /**
     * Retrieves the total execution time for each operation.
     *
     * @return A map where the key is the operation name and the value is the total execution time.
     */
    public Map<String, Integer> getExecutionTimeOperation() {
        return totalTimeOperation;
    }

    /**
     * Populates the {@code eventsByItem} map by grouping events based on the items they involve.
     *
     * @param events A list of {@link Event} objects to be grouped.
     */
    public void eventsByItem(List<Event> events) {
        for (Event event : events) {
            Article article = event.getArticle();
            eventsByItem.computeIfAbsent(article, k -> new ArrayList<>()).add(event);
        }
    }

    /**
     * Retrieves the events grouped by items.
     *
     * @return A map where the key is the {@link Article} and the value is a list of events for that article.
     */
    public Map<Article, List<Event>> getEventsByItem() {
        return eventsByItem;
    }

    /**
     * Calculates the average execution time for each operation.
     */
    public void averageTimePerOperation() {
        for (Map.Entry<String, Integer> entry : totalTimeOperation.entrySet()) {
            String operation = entry.getKey();
            Integer globalTimeOperation = entry.getValue();
            int count = eventsByOperation.get(operation).size();

            double avg = (double) globalTimeOperation / count;
            averageTimeOperation.put(operation, avg);
        }
    }

    /**
     * Retrieves the average execution time for each operation.
     *
     * @return A map where the key is the operation name and the value is the average execution time.
     */
    public Map<String, Double> getAverageTimeOperation() {
        return averageTimeOperation;
    }

    /**
     * Updates the waiting time for a specific operation.
     *
     * @param operation    The name of the operation.
     * @param waitingTime  The waiting time to be added.
     */
    private void updateWaitTimeOperation(String operation, int waitingTime) {
        waitTimeOperation.merge(operation, waitingTime, Integer::sum);
    }

    /**
     * Calculates the waiting time for each operation based on events grouped by items.
     *
     * @param eventsByItem A map where the key is an {@link Article} and the value is a list of events for that article.
     */
    public void waitTimePerOperation(Map<Article, List<Event>> eventsByItem) {
        for (Article article : eventsByItem.keySet()) {
            List<Event> itemEvents = eventsByItem.get(article);

            for (int i = 1; i < itemEvents.size(); i++) {
                Event actualEvent = itemEvents.get(i);
                Event previousEvent = itemEvents.get(i - 1);
                int waitingTime = actualEvent.getStartTime() - previousEvent.getEndTime();
                String operation = actualEvent.getWorkStation().getOperation();

                updateWaitTimeOperation(operation, waitingTime);
            }

            Event firstEventItem = itemEvents.getFirst();
            int initialWaitingTime = firstEventItem.getStartTime();
            String operation = firstEventItem.getWorkStation().getOperation();

            updateWaitTimeOperation(operation, initialWaitingTime);
        }
    }

    /**
     * Retrieves the waiting time for each operation.
     *
     * @return A map where the key is the operation name and the value is the total waiting time.
     */
    public Map<String, Integer> getWaitTimeOperation() {
        return waitTimeOperation;
    }
}
