/**
 * Unit tests for the SimulateProjectDelays class.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.ActivityTimeCalculator;
import project.domain.service.GraphCriticalPath;
import project.domain.service.SimulateProjectDelays;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SimulateProjectDelaysTest {

    private Graph<ActivityInfo, Double> graph;
    private SimulateProjectDelays simulateProjectDelays;
    private ActivityTimeCalculator timeCalculator1;
    private GraphCriticalPath graphCriticalPath1;
    private ActivityInfo aInfo;
    private ActivityInfo bInfo;
    private ActivityInfo cInfo;
    private ActivityInfo dInfo;
    private ActivityInfo eInfo;

    /**
     * Sets up the test environment by creating a sample graph of activities.
     * Initializes dependencies and prepares the graph with edges and vertices.
     */
    @Before
    public void setUp() {
        graph = new MapGraph<>(true);

        Activity start = new Activity("Start", "Start Point");
        Activity a = new Activity("A", "Task A", 20, "days", 5000, "USD");
        Activity b = new Activity("B", "Task B", 50, "days", 8000, "USD");
        Activity c = new Activity("C", "Task C", 25, "days", 6000, "USD");
        Activity d = new Activity("D", "Task D", 15, "days", 3000, "USD");
        Activity e = new Activity("E", "Task E", 60, "days", 9000, "USD");
        Activity end = new Activity("End", "End Point");

        ActivityInfo startInfo = new ActivityInfo(start);
        aInfo = new ActivityInfo(a);
        bInfo = new ActivityInfo(b);
        cInfo = new ActivityInfo(c);
        dInfo = new ActivityInfo(d);
        eInfo = new ActivityInfo(e);
        ActivityInfo endInfo = new ActivityInfo(end);

        graph.addVertex(startInfo);
        graph.addVertex(aInfo);
        graph.addVertex(bInfo);
        graph.addVertex(cInfo);
        graph.addVertex(dInfo);
        graph.addVertex(eInfo);
        graph.addVertex(endInfo);

        graph.addEdge(startInfo, aInfo, 20.0);
        graph.addEdge(startInfo, bInfo, 50.0);
        graph.addEdge(aInfo, cInfo, 25.0);
        graph.addEdge(bInfo, endInfo, 0.0);
        graph.addEdge(cInfo, dInfo, 15.0);
        graph.addEdge(aInfo, eInfo, 60.0);
        graph.addEdge(dInfo, endInfo, 0.0);
        graph.addEdge(eInfo, endInfo, 0.0);

        timeCalculator1 = new ActivityTimeCalculator(graph);
        graphCriticalPath1 = new GraphCriticalPath(graph);
        timeCalculator1.calculateTimes();

        simulateProjectDelays = new SimulateProjectDelays(graph);
    }

    /**
     * Tests if delays are correctly applied to the graph by updating activity durations.
     */
    @Test
    public void testUpdateGraphWithDelays() {
        Map<String, Double> delays = new HashMap<>();
        delays.put("A", 5.0);  // Adds 5 days of delay to "A"
        delays.put("C", 10.0); // Adds 10 days of delay to "C"

        boolean updated = simulateProjectDelays.updateGraphWithDelays(delays);

        assertTrue("Delays should be successfully applied", updated);

        for (ActivityInfo activityInfo : graph.vertices()) {
            String actID = activityInfo.getActivity().getActID();
            if ("A".equals(actID)) {
                assertEquals("The duration of activity A should be 25 days", 25.0, activityInfo.getActivity().getDuration(), 0.001);
            } else if ("C".equals(actID)) {
                assertEquals("The duration of activity C should be 35 days", 35.0, activityInfo.getActivity().getDuration(), 0.001);
            }
        }
    }

    /**
     * Tests the behavior when attempting to apply delays to a non-existent activity.
     */
    @Test
    public void testUpdateGraphWithDelaysNonExistingActivity() {
        Map<String, Double> delays = Collections.singletonMap("X", 5.0); // Non-existent activity

        boolean updated = simulateProjectDelays.updateGraphWithDelays(delays);

        assertFalse("The method should return false for non-existent activities", updated);
    }

    /**
     * Tests the calculation of project impact duration after applying delays.
     */
    @Test
    public void testImpactDurationCalculation() {

        Map<String, Double> delays = new HashMap<>();
        delays.put("A", 5.0); // Adds 5 days to "A"
        delays.put("C", 10.0); // Adds 10 days to "C"

        simulateProjectDelays.updateGraphWithDelays(delays);

        ActivityTimeCalculator timeCalculator2 = new ActivityTimeCalculator(graph);
        GraphCriticalPath graphCriticalPath2 = new GraphCriticalPath(graph);

        simulateProjectDelays.impact(timeCalculator2, graphCriticalPath2);

        double durationBeforeDelays = simulateProjectDelays.getDurationProjectNoDelay();
        double durationAfterDelays = simulateProjectDelays.getDurationProjectWithDelay();
        double impactDuration = simulateProjectDelays.getImpactDuration();

        assertEquals("The initial project duration should match the expected value before delays",
                80,
                durationBeforeDelays,
                0.001);

        assertEquals("The project duration with delays should match the expected value",
                85.0,
                durationAfterDelays,
                0.001);

        assertEquals("The calculated impact should match the difference between durations with and without delays",
                5.0,
                impactDuration,
                0.001);

    }

    /**
     * Tests the recalculation of activity times after applying delays.
     */
    @Test
    public void testImpactCalculateTimes() {
        Map<String, Double> delays = new HashMap<>();
        delays.put("A", 5.0);
        delays.put("C", 10.0);
        simulateProjectDelays.updateGraphWithDelays(delays);
        ActivityTimeCalculator timeCalculator2 = new ActivityTimeCalculator(graph);
        GraphCriticalPath graphCriticalPath2 = new GraphCriticalPath(graph);
        simulateProjectDelays.impact(timeCalculator2, graphCriticalPath2);

        assertEquals(0, aInfo.getEs(), 0.01);
        assertEquals(25, aInfo.getEf(), 0.01);
        assertEquals(0, aInfo.getLs(), 0.01);
        assertEquals(25, aInfo.getLf(), 0.01);
        assertEquals(0, aInfo.getSlack(), 0.01);
        assertEquals(0, bInfo.getEs(), 0.01);
        assertEquals(50, bInfo.getEf(), 0.01);
        assertEquals(35, bInfo.getLs(), 0.01);
        assertEquals(85, bInfo.getLf(), 0.01);
        assertEquals(35, bInfo.getSlack(), 0.01);
        assertEquals(25, cInfo.getEs(), 0.01);
        assertEquals(60, cInfo.getEf(), 0.01);
        assertEquals(35, cInfo.getLs(), 0.01);
        assertEquals(70, cInfo.getLf(), 0.01);
        assertEquals(10, cInfo.getSlack(), 0.01);
        assertEquals(60, dInfo.getEs(), 0.01);
        assertEquals(75, dInfo.getEf(), 0.01);
        assertEquals(70, dInfo.getLs(), 0.01);
        assertEquals(85, dInfo.getLf(), 0.01);
        assertEquals(10, dInfo.getSlack(), 0.01);
    }

    @Test
    public void testImpactCriticalPath() {
        Map<String, Double> delays = new HashMap<>();
        delays.put("C", 20.0);  // Adds 20 days to "C"

        simulateProjectDelays.updateGraphWithDelays(delays);
        ActivityTimeCalculator timeCalculator2 = new ActivityTimeCalculator(graph);
        GraphCriticalPath graphCriticalPath2 = new GraphCriticalPath(graph);
        simulateProjectDelays.impact(timeCalculator2, graphCriticalPath2);

        List<List<String>> criticalPaths = simulateProjectDelays.getCriticalPaths();

        assertNotNull(criticalPaths);
        assertEquals(2, criticalPaths.size());

        // Critical Path 1: A - C - D
        assertTrue(criticalPaths.get(0).contains("A"));
        assertTrue(criticalPaths.get(0).contains("C"));
        assertTrue(criticalPaths.get(0).contains("D"));

        // Critical Path 1: A - E
        assertTrue(criticalPaths.get(1).contains("A"));
        assertTrue(criticalPaths.get(1).contains("E"));
    }
}
