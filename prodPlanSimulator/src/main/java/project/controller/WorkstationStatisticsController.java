package project.controller;

import project.domain.model.WorkStation;
import project.domain.service.WorkstationStatistics;
import project.repository.EventRepository;
import project.repository.Repositories;

import java.util.Map;

public class WorkstationStatisticsController {
    private EventRepository eventRepository;

    public WorkstationStatisticsController() {
        getEventRepository();
    }


    public Map<WorkStation, Integer> totalTimeWorkStation() {
        WorkstationStatistics object = new WorkstationStatistics();
        object.totalTimeWorkStation(eventRepository.getAllEvents());
        return object.getTotalTimeWorkStation();
    }

    public Map<WorkStation, Double> percentageWorkstationTimeGlobalTime() {
        WorkstationStatistics object = new WorkstationStatistics();
        object.totalTimeWorkStation(eventRepository.getAllEvents());
        object.percentageTimeWorkStationGlobalTime();
        return object.getPercentageTimeWorkStation();
    }

    public EventRepository getEventRepository() {
        if (eventRepository == null) {
            Repositories repositories = Repositories.getInstance();
            eventRepository = repositories.getEventRepository();
        }
        return eventRepository;
    }
}
