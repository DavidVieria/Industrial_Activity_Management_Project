package project.domain.service;

import project.domain.model.WorkStation;
import project.domain.model.Article;
import project.domain.model.Event;

import java.util.*;

public class Simulator {

    private int currentTime;
    private Map<String, List<WorkStation>> workStationsByOperation;
    private PriorityQueue<Event> eventsQueue;
    private ArticlePriorityQueue itemsQueue;
    private List<Event> events;

    public Simulator () {
        this.currentTime = 0;
        this.workStationsByOperation = new HashMap<>();
        this.eventsQueue = new PriorityQueue<>();
        this.itemsQueue = new ArticlePriorityQueue();
        this.events = new ArrayList<>();
    }

    /**
     * Adds workstations to the simulator and associates them with their respective operations.
     *
     * @param workStations List of workstations to be added and associated with operations.
     */
    private void addWorkStations(List<WorkStation> workStations) {

        for (WorkStation workStation : workStations) {
            workStationsByOperation
                .computeIfAbsent(workStation.getOperation(), k -> new ArrayList<>())
                .add(workStation);
        }

    }

    /**
     * Simulates production without considering item priority, allocating items to available workstations.
     */
    public void simulateWithoutPriority(List<Article> articles, List<WorkStation> workStations) {
        addWorkStations(workStations);

        do {

            updateCurrentTime();

            processCompletedEvents();

            for (Article article : articles) {

                if (!article.isInOperation() && !article.isAllOperationsCompleted()) {

                    processEvent(article);

                }
            }

        } while (!eventsQueue.isEmpty());
    }

    /**
     * Simulates production with priority consideration, allocating items based on their priority to available workstations.
     */
    public void simulateWithPriority(List<Article> articles, List<WorkStation> workStations) {
        addWorkStations(workStations);

        do {

            updateCurrentTime();

            processCompletedEvents();

            for (Article article : articles) {
                if (!article.isInOperation() && !article.isAllOperationsCompleted()) {
                    itemsQueue.addItem(article);
                }
            }

            while (!itemsQueue.isEmpty()) {
                Article article = itemsQueue.getNextItem();

                processEvent(article);

            }

        } while (!eventsQueue.isEmpty());

    }

    private void updateCurrentTime () {
        if (!eventsQueue.isEmpty()) {
            Event event = eventsQueue.peek();
            currentTime = event.getEndTime();
        }
    }

    private void processCompletedEvents() {

        while (!eventsQueue.isEmpty() && eventsQueue.peek().getEndTime() == currentTime) {

            Event event = eventsQueue.poll();
            WorkStation workStation = event.getWorkStation();
            workStation.setAvailable(true);

            Article article = event.getArticle();
            article.setInOperation(false);

        }
    }

    private void processEvent(Article article) {

        String operation = article.getOperations().get(article.getCurrentOperationIndex());
        List<WorkStation> availableWorkStations = new ArrayList<>();

        for (WorkStation workStation : workStationsByOperation.get(operation)) {
            if (workStation.isAvailable()) {
                availableWorkStations.add(workStation);
            }
        }

        if (!availableWorkStations.isEmpty()) {
            WorkStation assignWorkStation = getFasterWorkStation(availableWorkStations);
            assignWorkStation.setAvailable(false);
            article.setInOperation(true);

            Event event = new Event(article, assignWorkStation, currentTime);
            eventsQueue.add(event);
            events.add(event);

            updateItemAfterOperation(article);
        }
    }

    /**
     * Retrieves the workstation with the fastest operation time from a list of available workstations.
     *
     * @param workStations List of available workstations.
     * @return The workstation with the shortest operation time.
     */
    private WorkStation getFasterWorkStation(List<WorkStation> workStations) {
        WorkStation fastestWorkStation = null;
        int fastestTime = Integer.MAX_VALUE;

        for (WorkStation workStation : workStations) {
            if (workStation.getTime() < fastestTime) {
                fastestTime = workStation.getTime();
                fastestWorkStation = workStation;
            }
        }

        return fastestWorkStation;
    }

    /**
     * Updates an item's state after completing an operation, progressing it to the next operation if available.
     *
     * @param article The item to update.
     */
    private void updateItemAfterOperation(Article article) {
        int nextOperationIndex = article.getCurrentOperationIndex() + 1;

        if (nextOperationIndex < article.getOperations().size()) {
            article.setCurrentOperationIndex(nextOperationIndex);
        } else {
            article.setAllOperationsCompleted(true);
        }
    }

    public Map<String, List<WorkStation>> getWorkStationsByOperation() {
        return workStationsByOperation;
    }

    public List<Event> getEvents() {
        return events;
    }

}
