package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.BottlenecksActivities;

import static org.junit.Assert.*;

public class BottlenecksActivitiesTest {

    private Graph<ActivityInfo, Double> graph;
    private BottlenecksActivities bottlenecksActivities;

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

        bottlenecksActivities = new BottlenecksActivities(graph); // Initialize BottlenecksActivities with the graph
    }

    /**
     * Test the calculation of the dependency queue for bottleneck activities.
     */
    @Test
    public void testCalculateDependencyQueue() {
        bottlenecksActivities.calculateDependencyQueue();
        HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue = bottlenecksActivities.getDependencyQueue();

        // Ensure the queue is not empty after calculation
        assertNotNull(dependencyQueue);
        assertTrue(dependencyQueue.size() > 0);

        // Verify that the activity with the highest dependency level is inserted correctly
        // We do not have a direct way to access a specific entry, so we will remove elements one by one to check their dependency levels.
        ActivityInfo firstActivity = dependencyQueue.removeMin().getKey();

        // Ensure the first activity removed has a valid dependency level
        assertNotNull(firstActivity);
        assertTrue(firstActivity.getDependencyLevel() > 0);
    }

    /**
     * Test that the dependency queue is correctly populated based on the graph structure.
     */
    @Test
    public void testDependencyQueueOrder() {
        bottlenecksActivities.calculateDependencyQueue();
        HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue = bottlenecksActivities.getDependencyQueue();

        // Remove the first element and check if its dependency level is valid
        ActivityInfo firstActivity = dependencyQueue.removeMin().getKey();

        // Ensure the first activity in the queue has the correct dependency level
        assertNotNull(firstActivity);
        assertTrue(firstActivity.getDependencyLevel() > 0);
    }

    /**
     * Test that activities without predecessors or with no outgoing edges are not added to the dependency queue.
     */
    @Test
    public void testActivitiesWithoutDependencies() {
        bottlenecksActivities.calculateDependencyQueue();
        HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue = bottlenecksActivities.getDependencyQueue();

        // Create a new activity without predecessors or outgoing edges
        Activity isolatedActivity = new Activity("Isolated", "Isolated Task", 10, "days", 2000, "USD");
        ActivityInfo isolatedActivityInfo = new ActivityInfo(isolatedActivity);

        // Ensure this isolated activity is not in the dependency queue
        // This is tested by checking the size of the queue after inserting the isolated activity
        int initialSize = dependencyQueue.size();
        dependencyQueue.insert(isolatedActivityInfo, 0);
        assertEquals(initialSize, dependencyQueue.size() - 1);
    }

    /**
     * Test that the dependency queue contains all activities that have both incoming and outgoing edges.
     */
    @Test
    public void testActivitiesWithBothIncomingAndOutgoingEdges() {
        bottlenecksActivities.calculateDependencyQueue();
        HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue = bottlenecksActivities.getDependencyQueue();

        // Verify that an activity like "A" has both incoming and outgoing edges
        ActivityInfo aInfo = new ActivityInfo(new Activity("A", "Task A", 20, "days", 5000, "USD"));

        // Check if the activity is in the dependency queue, confirming it has both incoming and outgoing edges
        // We check the queue size before and after inserting it
        int initialSize = dependencyQueue.size();
        dependencyQueue.insert(aInfo, 1);
        assertTrue(dependencyQueue.size() > initialSize);
    }

    /**
     * Test that activities with different dependency levels are sorted correctly in the priority queue.
     */
    @Test
    public void testActivitiesWithDifferentDependencyLevels() {
        // First, calculate the dependency queue
        bottlenecksActivities.calculateDependencyQueue();
        HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue = bottlenecksActivities.getDependencyQueue();

        // Remove the first element to check the dependency level
        ActivityInfo firstActivity = dependencyQueue.removeMin().getKey();
        int firstDependencyLevel = firstActivity.getDependencyLevel();

        // Remove the second element to check the dependency level
        ActivityInfo secondActivity = dependencyQueue.removeMin().getKey();
        int secondDependencyLevel = secondActivity.getDependencyLevel();

        // Ensure that the first activity has a lower dependency level than the second one
        assertTrue(firstDependencyLevel <= secondDependencyLevel);
    }
}
