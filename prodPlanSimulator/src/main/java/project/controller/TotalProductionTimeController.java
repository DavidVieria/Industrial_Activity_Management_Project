package project.controller;

import project.domain.service.TotalProductionTime;
import project.repository.EventRepository;
import project.repository.Repositories;

public class TotalProductionTimeController {

    private EventRepository eventRepository;

    public TotalProductionTimeController() {
        getEventRepository();
    }

    public int getTotalTime () {
        TotalProductionTime object = new TotalProductionTime();
        return object.getTotalTime(eventRepository.getAllEvents());
    }

    public EventRepository getEventRepository() {
        if (eventRepository == null) {
            Repositories repositories = Repositories.getInstance();
            eventRepository = repositories.getEventRepository();
        }
        return eventRepository;
    }
}
