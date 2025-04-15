package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.ActivityTimeCalculator;
import project.domain.service.GraphCriticalPath;

import static org.junit.Assert.*;

import java.util.List;

public class GraphCriticalPathTest {

    private Graph<ActivityInfo, Double> graph;
    private GraphCriticalPath criticalPath;

    @Before
    public void setUp() {
        graph = new MapGraph<>(true);

        // Create sample activities with attributes
        Activity start = new Activity("Start", "Start Point", 0, "days", 0, "USD");
        Activity a = new Activity("A", "Task A", 20, "days", 5000, "USD");
        Activity b = new Activity("B", "Task B", 50, "days", 8000, "USD");
        Activity c = new Activity("C", "Task C", 25, "days", 6000, "USD");
        Activity d = new Activity("D", "Task D", 15, "days", 3000, "USD");
        Activity e = new Activity("E", "Task E", 60, "days", 9000, "USD");
        Activity end = new Activity("End", "End Point", 0, "days", 0, "USD");

        // Create activity information objects
        ActivityInfo startInfo = new ActivityInfo(start);
        ActivityInfo aInfo = new ActivityInfo(a);
        ActivityInfo bInfo = new ActivityInfo(b);
        ActivityInfo cInfo = new ActivityInfo(c);
        ActivityInfo dInfo = new ActivityInfo(d);
        ActivityInfo eInfo = new ActivityInfo(e);
        ActivityInfo endInfo = new ActivityInfo(end);

        // Define relationships between activities
        graph.addEdge(startInfo, aInfo, 20.0);
        graph.addEdge(startInfo, bInfo, 50.0);
        graph.addEdge(aInfo, cInfo, 25.0);
        graph.addEdge(bInfo, endInfo, 0.0);
        graph.addEdge(cInfo, dInfo, 15.0);
        graph.addEdge(aInfo, eInfo, 60.0);
        graph.addEdge(dInfo, endInfo, 0.0);
        graph.addEdge(eInfo, endInfo, 0.0);

        // Calculate activity times based on the graph
        ActivityTimeCalculator calculator = new ActivityTimeCalculator(graph);
        calculator.calculateTimes();

        criticalPath = new GraphCriticalPath(graph); // Initialize GraphCriticalPath with the graph
    }

    /**
     * Tests the generation of critical paths.
     */
    @Test
    public void testGenerateCriticalPathsList() {
        List<List<String>> criticalPaths = criticalPath.generateCriticalPathsList();

        assertNotNull(criticalPaths);
        assertFalse(criticalPaths.isEmpty());
        assertTrue(criticalPaths.get(0).contains("A"));
        assertTrue(criticalPaths.get(0).contains("E"));
        assertFalse(criticalPaths.get(0).contains("End"));
    }

    /**
     * Tests the calculation of the total time duration of the project.
     */
    @Test
    public void testCalculateTotalTimeDuration() {
        double totalTime = criticalPath.calculateTotalTimeDuration();

        assertTrue(totalTime > 0);
        assertEquals(80, totalTime, 0.01);
    }

    /**
     * Tests the total number of activities in the graph.
     */
    @Test
    public void testTotalNumberOfActivities() {
        // Test the total number of activities in the graph after setup
        int totalActivities = graph.vertices().size();

        // Assert the expected number of activities in the graph
        assertEquals(7, totalActivities); // Start, A, B, C, D, E (total of 7)
    }

    /**
     * Tests the behavior of the system with a single path in the graph.
     */
    @Test
    public void testSingleCriticalPath() {
        Activity start = new Activity("Start", "Start Point", 0, "days", 0, "USD");
        Activity end = new Activity("End", "End Point", 0, "days", 0, "USD");

        ActivityInfo startInfo = new ActivityInfo(start);
        ActivityInfo endInfo = new ActivityInfo(end);

        Graph<ActivityInfo, Double> singlePathGraph = new MapGraph<>(true);
        singlePathGraph.addEdge(startInfo, endInfo, 10.0); // Adiciona aresta de Start a End com tempo de 10 dias

        ActivityTimeCalculator calculator = new ActivityTimeCalculator(singlePathGraph);
        calculator.calculateTimes();

        GraphCriticalPath singlePathCriticalPath = new GraphCriticalPath(singlePathGraph);
        List<List<String>> criticalPaths = singlePathCriticalPath.generateCriticalPathsList();

        assertNotNull(criticalPaths);
        assertEquals(1, criticalPaths.size());
        assertNotEquals(2, criticalPaths.get(0).size());
    }

    /**
     * Tests the total duration of the longest path in the graph.
     */
    @Test
    public void testLongestPathDuration() {
        // Use the GraphCriticalPath object to get the longest path duration
        double longestPathDuration = criticalPath.calculateTotalTimeDuration();

        // Assert that the longest path duration is correct
        assertTrue(longestPathDuration >= 60);  // Assumes the longest path takes at least 60 days
    }

    /**
     * Tests the behavior with a graph where multiple critical paths exist.
     */
    @Test
    public void testMultipleCriticalPaths() {
        // Setup example graph with multiple critical paths
        Activity start = new Activity("Start", "Start Point", 0, "days", 0, "USD");
        Activity a = new Activity("A", "Task A", 30, "days", 5000, "USD");
        Activity b = new Activity("B", "Task B", 30, "days", 8000, "USD");
        Activity end = new Activity("End", "End Point", 0, "days", 0, "USD");

        // Create ActivityInfo objects
        ActivityInfo startInfo = new ActivityInfo(start);
        ActivityInfo aInfo = new ActivityInfo(a);
        ActivityInfo bInfo = new ActivityInfo(b);
        ActivityInfo endInfo = new ActivityInfo(end);

        // Create a graph to define the relationships
        Graph<ActivityInfo, Double> multiplePathsGraph = new MapGraph<>(true);

        // Define the edges leading to multiple critical paths
        multiplePathsGraph.addEdge(startInfo, aInfo, 30.0);  // Start -> A (30)
        multiplePathsGraph.addEdge(aInfo, endInfo, 40.0);    // A -> End (40)

        multiplePathsGraph.addEdge(startInfo, bInfo, 30.0);  // Start -> B (30)
        multiplePathsGraph.addEdge(bInfo, endInfo, 40.0);    // B -> End (40)

        // Calculate activity times
        ActivityTimeCalculator calculator = new ActivityTimeCalculator(multiplePathsGraph);
        calculator.calculateTimes();

        // Initialize the GraphCriticalPath object
        GraphCriticalPath multiplePathsCriticalPath = new GraphCriticalPath(multiplePathsGraph);
        List<List<String>> criticalPaths = multiplePathsCriticalPath.generateCriticalPathsList();

        // Debugging: Print out all critical paths
        System.out.println("Critical Paths:");
        for (List<String> path : criticalPaths) {
            System.out.println(path);
        }

        // Assert that we have two critical paths
        assertNotNull(criticalPaths);
        assertEquals(2, criticalPaths.size()); // Expecting 2 critical paths (both with 70 days)

        // Validate that each critical path contains the correct sequence of activities
        // Path 1: Start -> A
        assertTrue(criticalPaths.get(0).contains("Start"));
        assertTrue(criticalPaths.get(0).contains("A"));

        // Path 2: Start -> B
        assertTrue(criticalPaths.get(1).contains("Start"));
        assertTrue(criticalPaths.get(1).contains("B"));
    }
}
