/**
 * This class is responsible for performing a topological sort on activities represented in a directed graph.
 * It uses a generic graph structure to determine the order of activities based on dependencies.
 */
package project.domain.service;

import project.domain.genericDataStructures.Algorithms;
import project.domain.genericDataStructures.Graph;

import java.util.LinkedList;

/**
 * Provides functionality to sort activities in a graph topologically.
 * This is useful for scheduling tasks where order matters due to dependencies.
 */
public class TopologicalSortActivities {

    /**
     * Stores the result of the topological sort as a list of ActivityInfo objects.
     */
    private LinkedList<ActivityInfo> topologicalSort;

    /**
     * Default constructor initializes an empty list for storing the topological sort result.
     */
    public TopologicalSortActivities() {
        topologicalSort = new LinkedList<>();
    }

    /**
     * Performs a topological sort on the given graph of activities.
     *
     * @param graph the directed graph containing activities and their dependencies.
     * @return a LinkedList of ActivityInfo objects in topologically sorted order.
     */
    public LinkedList<ActivityInfo> topologicalSortActivities(Graph<ActivityInfo, Double> graph) {
        topologicalSort = Algorithms.topologicalSort(graph);
        return topologicalSort;
    }
}
