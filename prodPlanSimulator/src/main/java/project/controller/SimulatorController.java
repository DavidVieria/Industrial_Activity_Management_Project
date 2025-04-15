package project.controller;

import project.domain.service.Simulator;
import project.repository.EventRepository;
import project.repository.ArticleRepository;
import project.repository.Repositories;
import project.repository.WorkStationRepository;


public class SimulatorController {

    private Simulator simulator;
    private ArticleRepository articleRepository;
    private WorkStationRepository workStationRepository;
    private EventRepository eventRepository;

    public SimulatorController() {
        this.simulator = new Simulator();
        getArticleRepository();
        getWorkStationRepository();
        getEventRepository();
    }

    public void simulatorWithoutPriority() {
        getEventRepository().clear();
        this.simulator = new Simulator();
        simulator.simulateWithoutPriority(getArticleRepository().getAllArticles(), getWorkStationRepository().getAllWorkStations());
        getEventRepository().addEvents(simulator.getEvents());
    }

    public void simulatorWithPriority() {
        getEventRepository().clear();
        this.simulator = new Simulator();
        simulator.simulateWithPriority(getArticleRepository().getAllArticles(), getWorkStationRepository().getAllWorkStations());
        getEventRepository().addEvents(simulator.getEvents());
    }

    public ArticleRepository getArticleRepository() {
        if (articleRepository == null) {
            Repositories repositories = Repositories.getInstance();
            articleRepository = repositories.getArticleRepository();
        }
        return articleRepository;
    }

    public WorkStationRepository getWorkStationRepository() {
        if (workStationRepository == null) {
            Repositories repositories = Repositories.getInstance();
            workStationRepository = repositories.getWorkStationRepository();
        }
        return workStationRepository;
    }

    public EventRepository getEventRepository() {
        if (eventRepository == null) {
            Repositories repositories = Repositories.getInstance();
            eventRepository = repositories.getEventRepository();
        }
        return eventRepository;
    }

}
