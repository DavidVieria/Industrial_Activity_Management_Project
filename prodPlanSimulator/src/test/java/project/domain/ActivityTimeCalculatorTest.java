package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.ActivityTimeCalculator;

import static org.junit.Assert.*;

/**
 * Unit tests for the ActivityTimeCalculator class.
 * This class tests the calculation of scheduling times for activities
 * in a directed acyclic graph (DAG).
 */
public class ActivityTimeCalculatorTest {

    private MapGraph<ActivityInfo, Double> graph;

    /**
     * Sets up the test environment by initializing a new MapGraph instance.
     */
    @Before
    public void setUp() {
        graph = new MapGraph<>(true);
    }

    /**
     * Tests the calculation of scheduling times for a simple graph with two activities.
     * Verifies the early start (ES), early finish (EF), late start (LS), late finish (LF) times, and slack for each activity.
     */
    @Test
    public void testCalculateTimes_SimpleGraph() {
        // Setup a simple graph
        Activity startActivity = new Activity("Start", "Start Point");
        Activity taskA = new Activity("A", "Task A", 3, "days", 1000, "USD");
        Activity taskB = new Activity("B", "Task B", 2, "days", 2000, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo A = new ActivityInfo(taskA);
        ActivityInfo B = new ActivityInfo(taskB);
        ActivityInfo end = new ActivityInfo(endActivity);

        graph.addVertex(start);
        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(end);

        graph.addEdge(start, A, 1.0);
        graph.addEdge(A, B, 1.0);
        graph.addEdge(B, end, 1.0);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator(graph);
        MapGraph<ActivityInfo, Double> resultGraph = (MapGraph<ActivityInfo, Double>) calculator.calculateTimes();

        // Verify the results
        assertEquals(0, A.getEs(), 0.01);
        assertEquals(3, A.getEf(), 0.01);
        assertEquals(3, B.getEs(), 0.01);
        assertEquals(5, B.getEf(), 0.01);

        assertEquals(3, A.getLf(), 0.01);
        assertEquals(0, A.getLs(), 0.01);
        assertEquals(5, B.getLf(), 0.01);
        assertEquals(3, B.getLs(), 0.01);

        assertEquals(0, A.getSlack(), 0.01);
        assertEquals(0, B.getSlack(), 0.01);
    }

    /**
     * Tests the calculation of scheduling times for a more complex graph with multiple activities.
     * Verifies the early start (ES), early finish (EF), late start (LS), late finish (LF) times, and slack for each activity.
     */
    @Test
    public void testCalculateTimes_ComplexGraph() {
        // Setup a more complex graph
        Activity startActivity = new Activity("Start", "Start Point");
        Activity taskA = new Activity("A", "Task A", 3, "days", 1000, "USD");
        Activity taskB = new Activity("B", "Task B", 2, "days", 2000, "USD");
        Activity taskC = new Activity("C", "Task C", 4, "days", 3000, "USD");
        Activity taskD = new Activity("D", "Task D", 1, "days", 500, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo A = new ActivityInfo(taskA);
        ActivityInfo B = new ActivityInfo(taskB);
        ActivityInfo C = new ActivityInfo(taskC);
        ActivityInfo D = new ActivityInfo(taskD);
        ActivityInfo end = new ActivityInfo(endActivity);

        graph.addVertex(start);
        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(end);

        graph.addEdge(start, A, 1.0);
        graph.addEdge(A, B, 1.0);
        graph.addEdge(A, C, 1.0);
        graph.addEdge(B, D, 1.0);
        graph.addEdge(C, D, 1.0);
        graph.addEdge(D, end, 1.0);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator(graph);
        MapGraph<ActivityInfo, Double> resultGraph = (MapGraph<ActivityInfo, Double>) calculator.calculateTimes();

        // Verify the results
        assertEquals(0, A.getEs(), 0.01);
        assertEquals(3, A.getEf(), 0.01);
        assertEquals(3, B.getEs(), 0.01);
        assertEquals(5, B.getEf(), 0.01);
        assertEquals(3, C.getEs(), 0.01);
        assertEquals(7, C.getEf(), 0.01);
        assertEquals(7, D.getEs(), 0.01);
        assertEquals(8, D.getEf(), 0.01);

        assertEquals(3, A.getLf(), 0.01);
        assertEquals(0, A.getLs(), 0.01);
        assertEquals(7, B.getLf(), 0.01);
        assertEquals(5, B.getLs(), 0.01);
        assertEquals(7, C.getLf(), 0.01);
        assertEquals(3, C.getLs(), 0.01);
        assertEquals(8, D.getLf(), 0.01);
        assertEquals(7, D.getLs(), 0.01);

        assertEquals(0, A.getSlack(), 0.01);
        assertEquals(2, B.getSlack(), 0.01);
        assertEquals(0, C.getSlack(), 0.01);
        assertEquals(0, D.getSlack(), 0.01);
    }

    /**
     * Tests the calculation of scheduling times for a disconnected graph with two activities.
     * Verifies the early start (ES), early finish (EF), late start (LS), late finish (LF) times, and slack for each activity.
     */
    @Test
    public void testCalculateTimes_DisconnectedGraph() {
        // Setup a disconnected graph
        Activity startActivity = new Activity("Start", "Start Point");
        Activity taskA = new Activity("A", "Task A", 3, "days", 1000, "USD");
        Activity taskB = new Activity("B", "Task B", 2, "days", 2000, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo A = new ActivityInfo(taskA);
        ActivityInfo B = new ActivityInfo(taskB);
        ActivityInfo end = new ActivityInfo(endActivity);

        graph.addVertex(start);
        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(end);

        ActivityTimeCalculator calculator = new ActivityTimeCalculator(graph);
        MapGraph<ActivityInfo, Double> resultGraph = (MapGraph<ActivityInfo, Double>) calculator.calculateTimes();

        // Verify the results
        assertEquals(0, A.getEs(), 0.01);
        assertEquals(3, A.getEf(), 0.01);
        assertEquals(0, B.getEs(), 0.01);
        assertEquals(2, B.getEf(), 0.01);

        assertEquals(3, A.getLf(), 0.01);
        assertEquals(0, A.getLs(), 0.01);
        assertEquals(2, B.getLf(), 0.01);
        assertEquals(0, B.getLs(), 0.01);

        assertEquals(0, A.getSlack(), 0.01);
        assertEquals(0, B.getSlack(), 0.01);
    }
}