package project.controller;

import project.domain.service.OperationStatistics;
import project.repository.EventRepository;
import project.repository.Repositories;

import java.util.Map;

public class ExecutionTimeOperationController {

    private EventRepository eventRepository;

    public ExecutionTimeOperationController() {
        getEventRepository();
    }

    public Map<String, Integer> executionTimeOperation() {
        OperationStatistics object = new OperationStatistics();
        object.addEventByOperation(eventRepository.getAllEvents());
        object.executionTimeOperation();
        return object.getExecutionTimeOperation();
    }

    public EventRepository getEventRepository() {
        if (eventRepository == null) {
            Repositories repositories = Repositories.getInstance();
            eventRepository = repositories.getEventRepository();
        }
        return eventRepository;
    }
}
