package project.domain.service;

import project.domain.genericDataStructures.Algorithms;
import project.domain.genericDataStructures.Graph;

import java.util.*;

/**
 * Service class responsible for detecting circular dependencies in a directed graph.
 * It identifies the first circular dependency found, if any, in the graph representing activities and their dependencies.
 */
public class CircularDependencyDetector {

    /**
     * Detects a circular dependency in the graph.
     *
     * @param graph the directed graph representing activities and their dependencies
     * @return the first detected cycle as a list of activities, or an empty list if no cycles are found
     */
    public List<ActivityInfo> detectCircularDependency(Graph<ActivityInfo, Double> graph) {
        return Algorithms.detectCircularDependency(graph);
    }
}
