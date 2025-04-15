package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityGraph;
import project.domain.service.ActivityInfo;
import project.repository.ActivityRepository;
import project.repository.Repositories;

public class ActivityGraphController {

    private ActivityRepository activityRepository;
    private Graph<ActivityInfo, Double> graph;
    private boolean dependencyCircularDetector;
    private boolean calculatedTimes;

    public ActivityGraphController(Graph<ActivityInfo, Double> graph) {
        getActivityRepository();
        this.graph = graph;
        this.dependencyCircularDetector = false;
        this.calculatedTimes = false;
    }

    public Graph<ActivityInfo, Double> graph() {
        ActivityGraph object = new ActivityGraph(activityRepository);
        boolean success = object.createGraph();;
        if (success) {
            graph = object.getActivityGraph();
            return graph;
        }
        return null;
    }

    public Graph<ActivityInfo, Double> getGraph() {
        return graph;
    }

    public ActivityRepository getActivityRepository() {
        if (activityRepository == null) {
            Repositories repositories = Repositories.getInstance();
            activityRepository = repositories.getActivityRepository();
        }
        return activityRepository;
    }

    public boolean isDependencyCircularDetector() {
        return dependencyCircularDetector;
    }

    public void setDependencyCircularDetector(boolean dependencyCircularDetector) {
        this.dependencyCircularDetector = dependencyCircularDetector;
    }

    public boolean isCalculatedTimes() {
        return calculatedTimes;
    }

    public void setCalculatedTimes(boolean calculatedTimes) {
        this.calculatedTimes = calculatedTimes;
    }
}
