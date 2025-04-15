package project.domain.service;

import project.domain.genericDataStructures.DependencyComparator;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.HeapPriorityQueue;

import java.util.Collection;

/**
 * Class responsible for identifying bottleneck activities based on their dependency levels
 * in a directed graph of activities.
 */
public class BottlenecksActivities {

    private HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue;
    private Graph<ActivityInfo, Double> graph;

    /**
     * Constructs a BottlenecksActivities instance.
     *
     * @param graph the graph containing activities and their dependencies
     */
    public BottlenecksActivities(Graph<ActivityInfo, Double> graph) {
        this.dependencyQueue = new HeapPriorityQueue<>(new DependencyComparator<>());
        this.graph = graph;
    }

    /**
     * Gets the priority queue of activities sorted by dependency level.
     *
     * @return the dependency priority queue
     */
    public HeapPriorityQueue<ActivityInfo, Integer> getDependencyQueue() {
        return dependencyQueue;
    }

    /**
     * Sets the priority queue of activities sorted by dependency level.
     *
     * @param dependencyQueue the dependency priority queue to set
     */
    public void setDependencyQueue(HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue) {
        this.dependencyQueue = dependencyQueue;
    }

    /**
     * Gets the graph containing activities and their dependencies.
     *
     * @return the graph
     */
    public Graph<ActivityInfo, Double> getGraph() {
        return graph;
    }

    /**
     * Sets the graph containing activities and their dependencies.
     *
     * @param graph the graph to set
     */
    public void setGraph(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
    }

    /**
     * Calculates the dependency queue by analyzing the in-degree of each vertex (activity)
     * in the graph. Activities with both incoming and outgoing edges are considered,
     * and their dependency levels are calculated based on the number of predecessors.
     */
    public void calculateDependencyQueue() {
        int numberOfPredecessors;

        for (ActivityInfo vertice : graph.vertices()) {
            if (graph.inDegree(vertice) != 0 && graph.outDegree(vertice) != 0) {
                Collection<ActivityInfo> predecessors = graph.incomingVertices(vertice);
                numberOfPredecessors = predecessors.size();
                vertice.setDependencyLevel(numberOfPredecessors);
                dependencyQueue.insert(vertice, vertice.getDependencyLevel());
            }
        }
    }
}
