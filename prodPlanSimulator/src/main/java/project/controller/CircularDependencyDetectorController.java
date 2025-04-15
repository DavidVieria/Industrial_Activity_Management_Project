package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;
import project.domain.service.CircularDependencyDetector;
import java.util.List;

public class CircularDependencyDetectorController {
    public final Graph<ActivityInfo, Double> graph;
    private final CircularDependencyDetector detector;

    public CircularDependencyDetectorController(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
        this.detector = new CircularDependencyDetector();
    }

    public List<ActivityInfo> detectCircularDependencies() {
        return detector.detectCircularDependency(graph);
    }
}
