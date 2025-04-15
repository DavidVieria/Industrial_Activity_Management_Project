package project.controller;

import project.domain.service.FlowDependency;
import project.domain.model.WorkStation;
import project.repository.EventRepository;
import project.repository.Repositories;

import java.util.Map;

public class FlowDependencyController {

    private EventRepository eventRepository;

    public FlowDependencyController() {
        getEventRepository();
    }

    public Map<WorkStation, Map<WorkStation, Integer>> generateWorkstationFlow() {
        FlowDependency object = new FlowDependency();
        object.eventsByItem(eventRepository.getAllEvents());
        object.generateDependencies(object.getEventsByItem());
        return object.getFlowDependencyWorkStation();
    }

    public EventRepository getEventRepository() {
        if (eventRepository == null) {
            Repositories repositories = Repositories.getInstance();
            eventRepository = repositories.getEventRepository();
        }
        return eventRepository;
    }
}
