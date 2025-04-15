package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.service.ActivityInfo;
import project.domain.service.BottlenecksActivities;

public class BottlenecksActivitiesController {
    private Graph<ActivityInfo, Double> graph;

    public BottlenecksActivitiesController(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
    }

    public HeapPriorityQueue<ActivityInfo, Integer> generateDependencyQueue() {
        BottlenecksActivities bottlenecksActivities = new BottlenecksActivities(graph);
        bottlenecksActivities.calculateDependencyQueue();

        return bottlenecksActivities.getDependencyQueue();
    }
}