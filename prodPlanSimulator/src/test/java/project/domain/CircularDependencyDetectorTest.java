package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.CircularDependencyDetector;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the CircularDependencyDetector class.
 * This class tests the detection of circular dependencies in a directed graph of activities.
 */
public class CircularDependencyDetectorTest {

    private CircularDependencyDetector circularDependencyDetector;
    private MapGraph<ActivityInfo, Double> activityGraph;

    /**
     * Sets up the test environment before each test.
     * Initializes the CircularDependencyDetector and the activity graph.
     */
    @Before
    public void setUp() {
        circularDependencyDetector = new CircularDependencyDetector();
        activityGraph = new MapGraph<>(true);
    }

    /**
     * Tests the detection of a single circular dependency in a directed graph.
     * Verifies that the cycle is correctly detected.
     */
    @Test
    public void testDetectSingleCircularDependency() {
        // Setup graph with a single circular dependency
        Activity startActivity = new Activity("Start", "Start Point");
        Activity taskE = new Activity("E", "Task E", 5, "days", 1000, "USD");
        Activity taskF = new Activity("F", "Task F", 15, "days", 3000, "USD");
        Activity taskG = new Activity("G", "Task G", 20, "days", 4000, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo E = new ActivityInfo(taskE);
        ActivityInfo F = new ActivityInfo(taskF);
        ActivityInfo G = new ActivityInfo(taskG);
        ActivityInfo end = new ActivityInfo(endActivity);

        activityGraph.addVertex(start);
        activityGraph.addVertex(E);
        activityGraph.addVertex(F);
        activityGraph.addVertex(G);
        activityGraph.addVertex(end);

        activityGraph.addEdge(start, E, 1.0);
        activityGraph.addEdge(E, F, 1.0);
        activityGraph.addEdge(F, G, 1.0);
        activityGraph.addEdge(G, E, 1.0);
        activityGraph.addEdge(G, end, 1.0);

        List<ActivityInfo> detectedCycle = circularDependencyDetector.detectCircularDependency(activityGraph);

        // Verify that one cycle is detected and its size is 3
        assertEquals("Expected cycle of size 3", 3, detectedCycle.size());
        assertTrue("Cycle should contain E", detectedCycle.contains(E));
        assertTrue("Cycle should contain F", detectedCycle.contains(F));
        assertTrue("Cycle should contain G", detectedCycle.contains(G));
    }

    /**
     * Tests the detection of multiple circular dependencies in a directed graph.
     * Verifies that the first detected cycle is correctly identified.
     */
    @Test
    public void testDetectMultipleCircularDependencies() {
        // Setup graph with multiple circular dependencies
        Activity startActivity = new Activity("Start", "Start Point");
        Activity taskH = new Activity("H", "Task H", 10, "days", 2500, "USD");
        Activity taskI = new Activity("I", "Task I", 20, "days", 5000, "USD");
        Activity taskJ = new Activity("J", "Task J", 30, "days", 7500, "USD");
        Activity taskK = new Activity("K", "Task K", 40, "days", 10000, "USD");
        Activity taskL = new Activity("L", "Task L", 50, "days", 12500, "USD");
        Activity taskM = new Activity("M", "Task M", 60, "days", 15000, "USD");
        Activity taskN = new Activity("N", "Task N", 70, "days", 17500, "USD");
        Activity taskO = new Activity("O", "Task O", 80, "days", 20000, "USD");
        Activity taskP = new Activity("P", "Task P", 90, "days", 22500, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo H = new ActivityInfo(taskH);
        ActivityInfo I = new ActivityInfo(taskI);
        ActivityInfo J = new ActivityInfo(taskJ);
        ActivityInfo K = new ActivityInfo(taskK);
        ActivityInfo L = new ActivityInfo(taskL);
        ActivityInfo M = new ActivityInfo(taskM);
        ActivityInfo N = new ActivityInfo(taskN);
        ActivityInfo O = new ActivityInfo(taskO);
        ActivityInfo P = new ActivityInfo(taskP);
        ActivityInfo end = new ActivityInfo(endActivity);

        activityGraph.addVertex(start);
        activityGraph.addVertex(H);
        activityGraph.addVertex(I);
        activityGraph.addVertex(J);
        activityGraph.addVertex(K);
        activityGraph.addVertex(L);
        activityGraph.addVertex(M);
        activityGraph.addVertex(N);
        activityGraph.addVertex(O);
        activityGraph.addVertex(P);
        activityGraph.addVertex(end);

        activityGraph.addEdge(start, H, 1.0);
        activityGraph.addEdge(H, I, 1.0);
        activityGraph.addEdge(I, J, 1.0);
        activityGraph.addEdge(J, H, 1.0);
        activityGraph.addEdge(J, K, 1.0);
        activityGraph.addEdge(K, I, 1.0);
        activityGraph.addEdge(L, M, 1.0);
        activityGraph.addEdge(M, N, 1.0);
        activityGraph.addEdge(N, L, 1.0);
        activityGraph.addEdge(O, P, 1.0);
        activityGraph.addEdge(P, O, 1.0);
        activityGraph.addEdge(P, end, 1.0);

        List<ActivityInfo> detectedCycle = circularDependencyDetector.detectCircularDependency(activityGraph);

        // Verify that a cycle is detected
        assertFalse("Expected at least one circular dependency", detectedCycle.isEmpty());
    }

    /**
     * Tests the detection of a self-referential circular dependency.
     * Verifies that the self-referential cycle is correctly detected.
     */
    @Test
    public void testDetectSelfReferentialCircularDependency() {
        // Setup graph with a self-referential circular dependency
        Activity startActivity = new Activity("Start", "Start Point");
        Activity selfActivity = new Activity("Self", "Self Activity", 10, "days", 1000, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo self = new ActivityInfo(selfActivity);
        ActivityInfo end = new ActivityInfo(endActivity);

        activityGraph.addVertex(start);
        activityGraph.addVertex(self);
        activityGraph.addVertex(end);

        activityGraph.addEdge(start, self, 1.0);
        activityGraph.addEdge(self, self, 1.0); // Self-referential cycle: Self -> Self
        activityGraph.addEdge(self, end, 1.0);

        List<ActivityInfo> detectedCycle = circularDependencyDetector.detectCircularDependency(activityGraph);

        // Verify that the self-referential cycle is detected and its size is 1
        assertEquals("Expected cycle of size 1", 1, detectedCycle.size());
        assertTrue("Cycle should contain Self", detectedCycle.contains(self));
    }

    /**
     * Tests the detection of no circular dependencies in a simple directed graph.
     * Verifies that no cycles are detected.
     */
    @Test
    public void testDetectNoCircularDependencies() {
        // Setup graph with no circular dependencies
        Activity startActivity = new Activity("Start", "Start Point");
        Activity taskA = new Activity("A", "Task A", 10, "days", 2000, "USD");
        Activity taskB = new Activity("B", "Task B", 20, "days", 4000, "USD");
        Activity endActivity = new Activity("End", "End Point");

        ActivityInfo start = new ActivityInfo(startActivity);
        ActivityInfo A = new ActivityInfo(taskA);
        ActivityInfo B = new ActivityInfo(taskB);
        ActivityInfo end = new ActivityInfo(endActivity);

        activityGraph.addVertex(start);
        activityGraph.addVertex(A);
        activityGraph.addVertex(B);
        activityGraph.addVertex(end);

        activityGraph.addEdge(start, A, 1.0);
        activityGraph.addEdge(A, B, 1.0);
        activityGraph.addEdge(B, end, 1.0);

        List<ActivityInfo> detectedCycle = circularDependencyDetector.detectCircularDependency(activityGraph);

        // Verify that no cycles are detected
        assertTrue("Expected no circular dependencies", detectedCycle.isEmpty());
    }

    /**
     * Tests the detection of no circular dependencies in an empty graph.
     * Verifies that no cycles are detected.
     */
    @Test
    public void testDetectNoCircularDependenciesInEmptyGraph() {
        List<ActivityInfo> detectedCycle = circularDependencyDetector.detectCircularDependency(activityGraph);

        // Verify that no cycles are detected
        assertTrue("Expected no circular dependencies", detectedCycle.isEmpty());
    }
}
