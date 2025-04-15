package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Graph;
import project.domain.model.Activity;
import project.domain.service.ActivityGraph;
import project.domain.service.ActivityInfo;
import project.repository.ActivityRepository;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ActivityGraph} class, verifying its functionality
 * in creating and managing activity graphs.
 */
public class ActivityGraphTest {

    private ActivityGraph activityGraph;
    private ActivityRepository activityRepository;

    /**
     * Sets up the test environment by initializing the ActivityGraph and
     * ActivityRepository instances.
     */
    @Before
    public void setUp() {
        activityRepository = new ActivityRepository();
        activityGraph = new ActivityGraph(activityRepository);
    }

    /**
     * Tests the addition of an activity to the graph.
     */
    @Test
    public void testAddActivity() {
        Activity activity = new Activity("A1", "Test Activity", 10, "days", 5, "$");
        ActivityInfo activityInfo = new ActivityInfo(activity);

        activityGraph.addActivity(activityInfo);

        Graph<ActivityInfo, Double> graph = activityGraph.getActivityGraph();

        assertTrue("The graph should contain the added activity as a vertex", graph.validVertex(activityInfo));
    }

    /**
     * Tests the addition of a relationship between two activities in the graph.
     */
    @Test
    public void testAddRelationship() {
        Activity activity1 = new Activity("A1", "Activity 1", 10, "days", 5, "$");
        Activity activity2 = new Activity("A2", "Activity 2", 20, "days", 5, "$");
        ActivityInfo actInfo1 = new ActivityInfo(activity1);
        ActivityInfo actInfo2 = new ActivityInfo(activity2);

        activityGraph.addRelationship(actInfo1, actInfo2, 15.0);

        Graph<ActivityInfo, Double> graph = activityGraph.getActivityGraph();

        assertNotNull("The graph should contain an edge between the two activities",
                graph.edge(actInfo1, actInfo2));

        assertEquals("The distance between the activities should match the given value",
                15.0, graph.edge(actInfo1, actInfo2).getWeight(), 0.001);
    }

    /**
     * Tests the successful creation of a graph with valid activities and relationships.
     */
    @Test
    public void testCreateGraphSuccess() {
        Activity activity1 = new Activity("A1", "Activity 1", 10, "days", 5, "$");
        Activity activity2 = new Activity("A2", "Activity 2", 20, "days", 5, "$");

        activityRepository.addActivity(activity1);
        activityRepository.addActivity(activity2);

        Map<Activity, List<String>> predecessorsMap = new HashMap<>();
        predecessorsMap.put(activity2, Collections.singletonList("A1"));
        activityGraph.setPredecessorsActivityMap(predecessorsMap);

        boolean result = activityGraph.createGraph();

        assertTrue("The graph should be created successfully", result);

        Graph<ActivityInfo, Double> graph = activityGraph.getActivityGraph();
        assertTrue("The graph should contain both activities",
                graph.validVertex(new ActivityInfo(activity1)) && graph.validVertex(new ActivityInfo(activity2)));
        assertNotNull("The graph should contain the edge between the two activities",
                graph.edge(new ActivityInfo(activity1), new ActivityInfo(activity2)));
    }

    /**
     * Tests the failure of graph creation when a predecessor activity is not found.
     */
    @Test
    public void testCreateGraphFailure() {
        Activity activity = new Activity("A1", "Activity 1", 10, "days", 5, "$");
        activityRepository.addActivity(activity);

        Map<Activity, List<String>> predecessorsMap = new HashMap<>();
        predecessorsMap.put(activity, Collections.singletonList("InvalidID"));
        activityGraph.setPredecessorsActivityMap(predecessorsMap);

        boolean result = activityGraph.createGraph();

        assertFalse("The graph creation should fail when a predecessor activity is not found", result);
    }

    /**
     * Tests the creation of a complex graph with multiple activities and relationships.
     */
    @Test
    public void testComplexGraph() {
        Activity start = new Activity("Start", "Start Point");
        Activity a = new Activity("A", "Task A", 20, "days", 5000, "USD");
        Activity b = new Activity("B", "Task B", 50, "days", 8000, "USD");
        Activity c = new Activity("C", "Task C", 25, "days", 6000, "USD");
        Activity d = new Activity("D", "Task D", 15, "days", 3000, "USD");
        Activity e = new Activity("E", "Task E", 60, "days", 9000, "USD");
        Activity end = new Activity("End", "End Point");

        activityRepository.addActivity(a);
        activityRepository.addActivity(b);
        activityRepository.addActivity(c);
        activityRepository.addActivity(d);
        activityRepository.addActivity(e);

        Map<Activity, List<String>> predecessorsMap = new HashMap<>();
        predecessorsMap.put(a, Collections.emptyList());
        predecessorsMap.put(b, Collections.emptyList());
        predecessorsMap.put(c, Collections.singletonList("A"));
        predecessorsMap.put(d, Collections.singletonList("C"));
        predecessorsMap.put(e, Collections.singletonList("A"));

        activityGraph.setPredecessorsActivityMap(predecessorsMap);

        boolean result = activityGraph.createGraph();

        assertTrue("The graph should be created successfully", result);

        Graph<ActivityInfo, Double> graph = activityGraph.getActivityGraph();

        assertTrue("The graph should contain all activities",
                graph.validVertex(new ActivityInfo(start)) &&
                        graph.validVertex(new ActivityInfo(a)) &&
                        graph.validVertex(new ActivityInfo(b)) &&
                        graph.validVertex(new ActivityInfo(c)) &&
                        graph.validVertex(new ActivityInfo(d)) &&
                        graph.validVertex(new ActivityInfo(e)) &&
                        graph.validVertex(new ActivityInfo(end)));

        assertNotNull("The graph should contain an edge from Start to A",
                graph.edge(new ActivityInfo(start), new ActivityInfo(a)));
        assertNotNull("The graph should contain an edge from Start to B",
                graph.edge(new ActivityInfo(start), new ActivityInfo(b)));
        assertNotNull("The graph should contain an edge from A to C",
                graph.edge(new ActivityInfo(a), new ActivityInfo(c)));
        assertNotNull("The graph should contain an edge from C to D",
                graph.edge(new ActivityInfo(c), new ActivityInfo(d)));
        assertNotNull("The graph should contain an edge from A to E",
                graph.edge(new ActivityInfo(a), new ActivityInfo(e)));
        assertNotNull("The graph should contain an edge from D to End",
                graph.edge(new ActivityInfo(d), new ActivityInfo(end)));
        assertNotNull("The graph should contain an edge from E to End",
                graph.edge(new ActivityInfo(e), new ActivityInfo(end)));
        assertNotNull("The graph should contain an edge from B to End",
                graph.edge(new ActivityInfo(b), new ActivityInfo(end)));
    }
}
