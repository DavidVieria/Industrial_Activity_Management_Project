package project.domain.service;

import project.domain.genericDataStructures.Algorithms;
import project.domain.genericDataStructures.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static project.domain.genericDataStructures.Algorithms.findEndVertice;

/**
 * Provides functionality for calculating critical paths in a project graph.
 */
public class GraphCriticalPath {

    private Graph<ActivityInfo, Double> graph;

    /**
     * Constructs a GraphCriticalPath instance with the given graph.
     *
     * @param graph the graph representing activities and their dependencies
     */
    public GraphCriticalPath(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
    }

    /**
     * Gets the graph associated with this instance.
     *
     * @return the graph
     */
    public Graph<ActivityInfo, Double> getGraph() {
        return graph;
    }

    /**
     * Sets the graph for this instance.
     *
     * @param graph the graph to set
     */
    public void setGraph(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
    }

    /**
     * Generates a list of critical paths in the graph. Each critical path is represented as a list
     * of activity IDs in reverse order (from the end activity to the start activity).
     *
     * @return a list of critical paths
     */
    public List<List<String>> generateCriticalPathsList() {

        ActivityInfo endVertice = findEndVertice(graph);

        List<List<String>> criticalPaths = new ArrayList<>();

        for (ActivityInfo vertice : graph.incomingVertices(endVertice)) {
            if (vertice.getSlack() == 0) {
                List<String> path = new ArrayList<>();

                boolean isCriticalPath = findPath(vertice, path);

                if (isCriticalPath) {
                    Collections.reverse(path);
                    criticalPaths.add(path);
                }
            }
        }

        return criticalPaths;
    }

    /**
     * Calculates the total duration of the project by finding the maximum early finish (EF) value
     * among all vertices in the graph.
     *
     * @return the total time duration of the project
     */
    public double calculateTotalTimeDuration() {
        double max_Ef = 0;
        for (ActivityInfo vertice : graph.vertices()) {
            if (vertice.getEf() > max_Ef) {
                max_Ef = vertice.getEf();
            }
        }
        return max_Ef;
    }

    /**
     * Recursively finds a critical path starting from the given vertex.
     *
     * @param vertice the starting vertex
     * @param path the list to store the path
     * @return true if a critical path is found, false otherwise
     */
    private boolean findPath(ActivityInfo vertice, List<String> path) {
        if (vertice.getSlack() != 0) {
            return false;
        }

        path.add(vertice.getActivity().getActID());

        if (graph.inDegree(vertice) == 0) {
            return true;
        }

        Collection<ActivityInfo> predecessors = graph.incomingVertices(vertice);
        for (ActivityInfo predecessor : predecessors) {
            if (findPath(predecessor, path)) {
                return true;
            }
        }

        return false;
    }
}
