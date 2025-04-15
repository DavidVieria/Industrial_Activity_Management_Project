/**
 * Unit tests for the TopologicalSortActivities class.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.TopologicalSortActivities;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class TopologicalSortActivitiesTest {

    private TopologicalSortActivities topologicalSortActivities;
    private Graph<ActivityInfo, Double> graph;

    /**
     * Sets up the test environment by initializing the graph and its vertices.
     * Adds edges to simulate dependencies between activities.
     */
    @Before
    public void setUp() {
        topologicalSortActivities = new TopologicalSortActivities();
        graph = new MapGraph<>(true);

        Activity start = new Activity("Start", "Start Point", 0, "days", 0, "USD");
        Activity a = new Activity("A", "Task A", 20, "days", 5000, "USD");
        Activity b = new Activity("B", "Task B", 50, "days", 8000, "USD");
        Activity c = new Activity("C", "Task C", 25, "days", 6000, "USD");
        Activity d = new Activity("D", "Task D", 15, "days", 3000, "USD");
        Activity e = new Activity("E", "Task E", 60, "days", 9000, "USD");
        Activity end = new Activity("End", "End Point", 0, "days", 0, "USD");

        ActivityInfo startInfo = new ActivityInfo(start);
        ActivityInfo aInfo = new ActivityInfo(a);
        ActivityInfo bInfo = new ActivityInfo(b);
        ActivityInfo cInfo = new ActivityInfo(c);
        ActivityInfo dInfo = new ActivityInfo(d);
        ActivityInfo eInfo = new ActivityInfo(e);
        ActivityInfo endInfo = new ActivityInfo(end);

        graph.addEdge(startInfo, aInfo, 20.0);
        graph.addEdge(startInfo, bInfo, 50.0);
        graph.addEdge(aInfo, cInfo, 25.0);
        graph.addEdge(bInfo, endInfo, 0.0);
        graph.addEdge(cInfo, dInfo, 15.0);
        graph.addEdge(aInfo, eInfo, 60.0);
        graph.addEdge(dInfo, endInfo, 0.0);
        graph.addEdge(eInfo, endInfo, 0.0);
    }

    /**
     * Tests the topological sorting algorithm on a graph with multiple vertices.
     * Verifies that the output respects the dependencies among activities.
     */
    @Test
    public void testTopologicalSortActivities() {
        LinkedList<ActivityInfo> sortedList = topologicalSortActivities.topologicalSortActivities(graph);

        assertEquals("The size of the sorted list should match the number of vertices.", 7, sortedList.size());
        assertTrue("The sorted list should start with the Start activity.", indexOf(sortedList, "Start") < indexOf(sortedList, "A"));
        assertTrue("The sorted list should start with the Start activity.", indexOf(sortedList, "Start") < indexOf(sortedList, "B"));
        assertTrue("Activity A should appear before activity C.", indexOf(sortedList, "A") < indexOf(sortedList, "C"));
        assertTrue("Activity A should appear before activity E.", indexOf(sortedList, "A") < indexOf(sortedList, "E"));
        assertTrue("Activity C should appear before activity D.", indexOf(sortedList, "C") < indexOf(sortedList, "D"));
        assertTrue("Activity B should appear before the End activity.", indexOf(sortedList, "B") < indexOf(sortedList, "End"));
        assertTrue("Activity D should appear before the End activity.", indexOf(sortedList, "D") < indexOf(sortedList, "End"));
        assertTrue("Activity E should appear before the End activity.", indexOf(sortedList, "E") < indexOf(sortedList, "End"));
    }

    /**
     * Tests the topological sorting algorithm on an empty graph.
     * Verifies that the result is an empty list.
     */
    @Test
    public void testTopologicalSortWithEmptyGraph() {
        Graph<ActivityInfo, Double> emptyGraph = new MapGraph<>(true);
        LinkedList<ActivityInfo> sortedList = topologicalSortActivities.topologicalSortActivities(emptyGraph);

        assertTrue("The sorted list should be empty for an empty graph.", sortedList.isEmpty());
    }

    /**
     * Tests the topological sorting algorithm on a graph with a single vertex.
     * Verifies that the result contains the single vertex.
     */
    @Test
    public void testTopologicalSortSingleVertex() {
        Graph<ActivityInfo, Double> singleVertexGraph = new MapGraph<>(true);
        Activity singleActivity = new Activity("A", "Single Task", 10, "days", 1000, "USD");
        ActivityInfo singleInfo = new ActivityInfo(singleActivity);
        singleVertexGraph.addVertex(singleInfo);

        LinkedList<ActivityInfo> sortedList = topologicalSortActivities.topologicalSortActivities(singleVertexGraph);

        assertEquals("The sorted list should contain one activity.", 1, sortedList.size());
        assertEquals("The sorted list should contain the single activity A.", "A", sortedList.getFirst().getActivity().getActID());
    }

    /**
     * Utility method to find the index of an activity in the sorted list by its ID.
     *
     * @param list the sorted list of activities
     * @param id   the ID of the activity to find
     * @return the index of the activity in the list, or -1 if not found
     */
    private int indexOf(LinkedList<ActivityInfo> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getActivity().getActID().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
