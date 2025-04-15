package project.controller;

import project.domain.service.OperationStatistics;
import project.repository.EventRepository;
import project.repository.Repositories;

import java.util.Map;

public class OperationStatisticsController {
    private EventRepository eventRepository;

    public OperationStatisticsController() {
        getEventRepository();
    }

    public Map<String, Double> averageTimePerOperation() {
        OperationStatistics object = new OperationStatistics();
        object.addEventByOperation(eventRepository.getAllEvents());
        object.executionTimeOperation();
        object.averageTimePerOperation();
        return object.getAverageTimeOperation();
    }

    public Map<String, Integer> getWaitTimePerOperation() {
        OperationStatistics object = new OperationStatistics();
        object.eventsByItem(eventRepository.getAllEvents());
        OperationStatistics operationObject = new OperationStatistics();
        operationObject.waitTimePerOperation(object.getEventsByItem());
        return operationObject.getWaitTimeOperation();
    }

    public EventRepository getEventRepository() {
        if (eventRepository == null) {
            Repositories repositories = Repositories.getInstance();
            eventRepository = repositories.getEventRepository();
        }
        return eventRepository;
    }
}
