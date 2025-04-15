package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;
import project.domain.service.TopologicalSortActivities;

import java.util.LinkedList;

public class TopologicalSortActivitiesController {

    public TopologicalSortActivitiesController() {
    }

    public LinkedList<ActivityInfo> topologicalSortActivities(Graph<ActivityInfo, Double> graph) {
        TopologicalSortActivities object = new TopologicalSortActivities();
        return object.topologicalSortActivities(graph);
    }
}