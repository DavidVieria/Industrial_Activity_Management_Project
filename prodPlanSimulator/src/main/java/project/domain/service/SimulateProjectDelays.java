/**
 * This class simulates delays in a project by modifying activity durations in a project graph.
 * It calculates the impact of delays on the project duration and identifies critical paths.
 */
package project.domain.service;

import project.domain.genericDataStructures.Graph;

import java.util.List;
import java.util.Map;

public class SimulateProjectDelays {

    /** The graph representing the project's activities and dependencies */
    private Graph<ActivityInfo, Double> graph;

    /** The list of critical paths in the project */
    private List<List<String>> criticalPaths;

    /** The original project duration without any delays */
    private double durationProjectNoDelay;

    /** The project duration considering delays */
    private double durationProjectWithDelay;

    /** The impact of the delays on the project's duration */
    private double impactDuration;

    /**
     * Constructor for SimulateProjectDelays.
     *
     * @param graph The graph representing the project's activities and dependencies.
     */
    public SimulateProjectDelays(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
        this.criticalPaths = null;
        this.impactDuration = 0;
    }

    /**
     * Updates the project graph with delays for specified activities.
     *
     * @param delayedActivities A map where the key is the activity ID and the value is the delay duration.
     * @return True if all activities were found and updated, otherwise false.
     */
    public boolean updateGraphWithDelays(Map<String, Double> delayedActivities) {
        for (String actID : delayedActivities.keySet()) {
            boolean found = false;
            for (ActivityInfo activityInfo : graph.vertices()) {
                if (activityInfo.getActivity().getActID().equals(actID)) {
                    double duration = activityInfo.getActivity().getDuration();
                    double delay = delayedActivities.get(actID);
                    activityInfo.getActivity().setDuration(duration + delay);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the impact of delays on the project's duration and updates critical paths.
     *
     * @param timeCalculator The calculator used to update activity start and finish times.
     * @param graphCriticalPath The critical path analyzer for the project graph.
     */
    public void impact(ActivityTimeCalculator timeCalculator, GraphCriticalPath graphCriticalPath) {

        // Record the original project duration without delays
        durationProjectNoDelay = graphCriticalPath.calculateTotalTimeDuration();

        // Update graph with recalculated times
        graph = timeCalculator.calculateTimes();

        // Generate the list of critical paths
        criticalPaths = graphCriticalPath.generateCriticalPathsList();

        // Record the project duration with delays
        durationProjectWithDelay = graphCriticalPath.calculateTotalTimeDuration();

        // Calculate the duration impact
        impactDuration = durationProjectWithDelay - durationProjectNoDelay;
    }

    /**
     * @return The updated project graph.
     */
    public Graph<ActivityInfo, Double> getGraph() {
        return graph;
    }

    /**
     * @return The list of critical paths in the project.
     */
    public List<List<String>> getCriticalPaths() {
        return criticalPaths;
    }

    /**
     * @return The original project duration without delays.
     */
    public double getDurationProjectNoDelay() {
        return durationProjectNoDelay;
    }

    /**
     * @return The project duration considering delays.
     */
    public double getDurationProjectWithDelay() {
        return durationProjectWithDelay;
    }

    /**
     * @return The impact of delays on the project's duration.
     */
    public double getImpactDuration() {
        return impactDuration;
    }
}
