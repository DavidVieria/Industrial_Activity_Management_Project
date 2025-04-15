/**
 * This class manages flow dependencies between workstations based on events related to articles.
 */
package project.domain.service;

import project.domain.model.WorkStation;
import project.domain.model.Article;
import project.domain.model.Event;

import java.util.*;

public class FlowDependency {

    private Map<Article, List<Event>> eventsByItem;
    private Map<WorkStation, Map<WorkStation, Integer>> flowDependencyWorkStation;

    /**
     * Constructs a FlowDependency object with empty data structures.
     */
    public FlowDependency() {
        eventsByItem = new HashMap<>();
        flowDependencyWorkStation = new HashMap<>();
    }

    /**
     * Populates the events by item map by grouping events by their associated articles.
     *
     * @param events A list of events to group by their associated articles.
     */
    public void eventsByItem(List<Event> events) {
        for (Event event : events) {
            Article article = event.getArticle();
            eventsByItem.computeIfAbsent(article, k -> new ArrayList<>())
                    .add(event);
        }
    }

    /**
     * Retrieves the mapping of articles to their corresponding events.
     *
     * @return A map where the key is an article and the value is a list of events associated with it.
     */
    public Map<Article, List<Event>> getEventsByItem() {
        return eventsByItem;
    }

    /**
     * Generates workstation dependencies based on the events grouped by articles.
     *
     * @param eventsByItem A map where the key is an article and the value is a list of events associated with it.
     */
    public void generateDependencies(Map<Article, List<Event>> eventsByItem) {
        for (Article article : eventsByItem.keySet()) {
            List<Event> itemEvents = eventsByItem.get(article);

            for (int i = 0; i < itemEvents.size() - 1; i++) {
                WorkStation actualWorkStation = itemEvents.get(i).getWorkStation();
                WorkStation nextWorkStation = itemEvents.get(i + 1).getWorkStation();

                flowDependencyWorkStation
                        .computeIfAbsent(actualWorkStation, k -> new HashMap<>())
                        .merge(nextWorkStation, 1, Integer::sum);
            }
        }
    }

    /**
     * Sorts the workstation dependencies based on the total flow from each workstation.
     *
     * @param workStationDependencies A map where the key is a workstation and the value is another map of workstations and flow counts.
     * @return A sorted map of workstation dependencies based on total flow.
     */
    private Map<WorkStation, Map<WorkStation, Integer>> sortWorkStationFlow(Map<WorkStation, Map<WorkStation, Integer>> workStationDependencies) {
        Map<WorkStation, Integer> flowSums = new HashMap<>();

        for (Map.Entry<WorkStation, Map<WorkStation, Integer>> entry : workStationDependencies.entrySet()) {
            int sum = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            flowSums.put(entry.getKey(), sum);
        }

        List<Map.Entry<WorkStation, Integer>> sortedEntries = new ArrayList<>(flowSums.entrySet());
        sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Map<WorkStation, Map<WorkStation, Integer>> sortedWorkStationDependencies = new LinkedHashMap<>();
        for (Map.Entry<WorkStation, Integer> entry : sortedEntries) {
            sortedWorkStationDependencies.put(entry.getKey(), workStationDependencies.get(entry.getKey()));
        }

        return sortedWorkStationDependencies;
    }

    /**
     * Retrieves the flow dependency map between workstations, sorted by total flow.
     *
     * @return A sorted map where the key is a workstation, and the value is another map of workstations and flow counts.
     */
    public Map<WorkStation, Map<WorkStation, Integer>> getFlowDependencyWorkStation() {
        return sortWorkStationFlow(flowDependencyWorkStation);
    }
}
