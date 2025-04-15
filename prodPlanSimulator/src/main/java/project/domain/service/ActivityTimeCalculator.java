package project.domain.service;

import project.domain.genericDataStructures.Algorithms;
import project.domain.genericDataStructures.Graph;

import java.util.*;

/**
 * Service class responsible for calculating the scheduling times for activities
 * represented in a directed acyclic graph (DAG). It computes the earliest start (ES),
 * earliest finish (EF), latest start (LS), latest finish (LF), and slack times for each activity.
 * The class uses topological sorting to calculate these values in an efficient manner.
 */
public class ActivityTimeCalculator {

    private final Graph<ActivityInfo, Double> graph;

    /**
     * Constructs an ActivityTimeCalculator with the specified directed acyclic graph (DAG).
     *
     * @param graph the directed acyclic graph (DAG) representing activities and their dependencies
     */
    public ActivityTimeCalculator(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
    }

    /**
     * Calculates the earliest start (ES), earliest finish (EF), latest start (LS),
     * latest finish (LF), and slack times for all activities in the graph.
     * The method computes the values based on topological sorting of the graph.
     *
     * @return the updated graph with calculated scheduling times for each activity
     */
    public Graph<ActivityInfo, Double> calculateTimes() {
        LinkedList<ActivityInfo> topologicalOrder = Algorithms.topologicalSort(graph);

        calculateEarliestStartAndFinish(topologicalOrder);
        calculateLatestStartAndFinish(topologicalOrder);
        calculateSlack();

        return graph;
    }

    /**
     * Calculates the earliest start (ES) and earliest finish (EF) times for each activity
     * in the graph, based on the topological order.
     *
     * @param topologicalOrder the list of activities sorted in topological order
     */
    private void calculateEarliestStartAndFinish(LinkedList<ActivityInfo> topologicalOrder) {
        for (ActivityInfo activityData : topologicalOrder) {
            double es = 0;

            for (ActivityInfo predecessor : graph.incomingVertices(activityData)) {
                es = Math.max(es, predecessor.getEf());
            }

            activityData.setEs(es);
            activityData.setEf(es + activityData.getActivity().getDuration());
        }
    }

    /**
     * Calculates the latest start (LS) and latest finish (LF) times for each activity
     * in reverse topological order.
     *
     * @param topologicalOrder the list of activities sorted in topological order
     */
    private void calculateLatestStartAndFinish(LinkedList<ActivityInfo> topologicalOrder) {
        for (int i = topologicalOrder.size() - 1; i >= 0; i--) {
            ActivityInfo activityData = topologicalOrder.get(i);
            double lf = Double.MAX_VALUE;

            if (graph.outDegree(activityData) == 0) {
                lf = activityData.getEf();
            } else {
                for (ActivityInfo successor : graph.adjVertices(activityData)) {
                    lf = Math.min(lf, successor.getLs());
                }
            }

            activityData.setLf(lf);
            activityData.setLs(lf - activityData.getActivity().getDuration());
        }
    }

    /**
     * Calculates the slack time for each activity, which is the difference between
     * the latest finish (LF) and earliest finish (EF) times.
     * Slack represents the flexibility in scheduling an activity without affecting the project duration.
     */
    private void calculateSlack() {
        for (ActivityInfo activityData : graph.vertices()) {
            activityData.setSlack(activityData.getLf() - activityData.getEf());
        }
    }
}
