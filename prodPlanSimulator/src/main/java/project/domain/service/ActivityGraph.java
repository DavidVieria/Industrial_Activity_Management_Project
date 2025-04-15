package project.domain.service;

import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.io.DataLoader;
import project.repository.ActivityRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for managing a graph of activities and their relationships.
 */
public class ActivityGraph {

    private Graph<ActivityInfo, Double> activityGraph;
    private Map<Activity, List<String>> predecessorsActivityMap;
    private Map<Activity, ActivityInfo> activityInfoMap;
    private final ActivityRepository activityRepository;

    /**
     * Constructs an ActivityGraph instance.
     *
     * @param activityRepository the repository for accessing activity data
     */
    public ActivityGraph(ActivityRepository activityRepository) {
        activityGraph = new MapGraph<>(true);
        activityInfoMap = new HashMap<>();
        predecessorsActivityMap = DataLoader.getPredecessorsActivityMap();
        this.activityRepository = activityRepository;
    }

    /**
     * Adds an activity to the graph.
     *
     * @param act the activity information to be added
     */
    public void addActivity(ActivityInfo act) {
        activityGraph.addVertex(act);
    }

    /**
     * Adds a relationship between two activities in the graph.
     *
     * @param actOrigin      the origin activity information
     * @param actDestination the destination activity information
     * @param distance       the distance (weight) between the activities
     */
    public void addRelationship(ActivityInfo actOrigin, ActivityInfo actDestination, double distance) {
        activityGraph.addEdge(actOrigin, actDestination, distance);
    }

    /**
     * Creates the activity graph by connecting activities and their predecessors.
     *
     * @return true if the graph was successfully created, false otherwise
     */
    public boolean createGraph() {

        ActivityInfo startActivityInfo = new ActivityInfo(new Activity("Start", "Start Point"));
        ActivityInfo endActivityInfo = new ActivityInfo(new Activity("End", "End Point"));
        activityGraph.addVertex(startActivityInfo);
        activityGraph.addVertex(endActivityInfo);

        for (Activity activity : activityRepository.getAllActivities()) {
            ActivityInfo activityInfo = new ActivityInfo(activity);
            activityInfoMap.put(activity, activityInfo);
        }

        for (Map.Entry<Activity, List<String>> entry : predecessorsActivityMap.entrySet()) {
            Activity activity = entry.getKey();
            List<String> predecessorsIds = entry.getValue();
            ActivityInfo activityInfo = activityInfoMap.get(activity);
            addActivity(activityInfo);

            if (predecessorsIds.isEmpty()) {
                addRelationship(startActivityInfo, activityInfo, activityInfo.getActivity().getDuration());
            } else {
                for (String predecessorID : predecessorsIds) {
                    Activity prevAct = activityRepository.getActivityByID(predecessorID);
                    ActivityInfo prevActInfo = activityInfoMap.get(prevAct);
                    if (prevAct == null) {
                        return false;
                    } else {
                        addRelationship(prevActInfo, activityInfo, activityInfo.getActivity().getDuration());
                    }
                }
            }
        }

        addEdgesToEndActivity(endActivityInfo);
        return true;
    }

    /**
     * Adds edges from activities without successors to the end activity.
     *
     * @param endActivityInfo the information about the end activity
     */
    public void addEdgesToEndActivity(ActivityInfo endActivityInfo) {
        for (Activity activity : activityRepository.getAllActivities()) {
            boolean hasSuccessor = false;
            for (List<String> predecessors : predecessorsActivityMap.values()) {
                if (predecessors.contains(activity.getActID())) {
                    hasSuccessor = true;
                    break;
                }
            }

            if (!hasSuccessor) {
                ActivityInfo activityInfo = activityInfoMap.get(activity);
                addRelationship(activityInfo, endActivityInfo, endActivityInfo.getActivity().getDuration());
            }
        }
    }

    /**
     * Gets the activity graph.
     *
     * @return the activity graph
     */
    public Graph<ActivityInfo, Double> getActivityGraph() {
        return activityGraph;
    }

    /**
     * Sets the activity graph.
     *
     * @param activityGraph the new activity graph
     */
    public void setActivityGraph(Graph<ActivityInfo, Double> activityGraph) {
        this.activityGraph = activityGraph;
    }

    /**
     * Gets the map of activities to their predecessors.
     *
     * @return the map of predecessors
     */
    public Map<Activity, List<String>> getPredecessorsActivityMap() {
        return predecessorsActivityMap;
    }

    /**
     * Sets the map of activities to their predecessors.
     *
     * @param predecessorsActivityMap the new map of predecessors
     */
    public void setPredecessorsActivityMap(Map<Activity, List<String>> predecessorsActivityMap) {
        this.predecessorsActivityMap = predecessorsActivityMap;
    }
}
