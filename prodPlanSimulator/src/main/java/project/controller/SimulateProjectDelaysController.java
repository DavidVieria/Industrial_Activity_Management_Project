package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;
import project.domain.service.ActivityTimeCalculator;
import project.domain.service.GraphCriticalPath;
import project.domain.service.SimulateProjectDelays;

import java.util.List;
import java.util.Map;

public class SimulateProjectDelaysController {

    private Graph<ActivityInfo, Double> graph;
    private SimulateProjectDelays object;

    public SimulateProjectDelaysController(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
        object = new SimulateProjectDelays(graph.clone());
    }

    public boolean simulateDelay(Map<String, Double> delayedActivitiesMap) {

        boolean success = object.updateGraphWithDelays(delayedActivitiesMap);

        if (!success) {
            return false;
        }

        ActivityTimeCalculator activityTimeCalculator = new ActivityTimeCalculator(graph);
        GraphCriticalPath graphCriticalPath = new GraphCriticalPath(graph);

        object.impact(activityTimeCalculator, graphCriticalPath);
        return true;
    }

    public Graph<ActivityInfo, Double> getGraph() {
        return object.getGraph();
    }

    public List<List<String>> getGraphCriticalPathResult() {
        return object.getCriticalPaths();
    }

    public double getDurationProjectNoDelay() {
        return object.getDurationProjectNoDelay();
    }

    public double getDurationProjectWithDelay() {
        return object.getDurationProjectWithDelay();
    }

    public double getImpactDuration() {
        return object.getImpactDuration();
    }

}