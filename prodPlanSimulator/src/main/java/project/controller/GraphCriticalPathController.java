package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;
import project.domain.service.GraphCriticalPath;

import java.util.List;

public class GraphCriticalPathController {
    private Graph<ActivityInfo, Double> activityGraph;

    public GraphCriticalPathController(Graph<ActivityInfo, Double> activityGraph) {
        this.activityGraph = activityGraph;
    }

    public List<List<String>> generateCriticalPaths() {
        GraphCriticalPath graphCriticalPath = new GraphCriticalPath(activityGraph);
        return graphCriticalPath.generateCriticalPathsList();
    }

    public double getTotalTimeDuration() {
        GraphCriticalPath graphCriticalPath = new GraphCriticalPath(activityGraph);
        return graphCriticalPath.calculateTotalTimeDuration();
    }
}
