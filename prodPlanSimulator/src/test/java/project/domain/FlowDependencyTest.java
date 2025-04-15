/**
 * Unit tests for the {@link FlowDependency} class, verifying the generation of dependencies 
 * between workstations based on a list of events.
 */
package project.domain;

import org.junit.Test;
import project.domain.enums.Priority;
import project.domain.model.Article;
import project.domain.model.Event;
import project.domain.model.WorkStation;
import project.domain.service.FlowDependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlowDependencyTest {

    /**
     * Tests the {@link FlowDependency#generateDependencies(Map)} method to ensure that dependencies 
     * between workstations are correctly generated based on the sequence of events.
     */
    @Test
    public void generateDependencies() {

        FlowDependency flowDependency = new FlowDependency();
        List<Event> events = new ArrayList<>();

        // Creating workstations
        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 4);
        WorkStation station3 = new WorkStation("WS3", "Assembly", 7);

        // Creating articles
        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting", "Assembly"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Polishing", "Assembly"), 1);

        // Adding events for the articles and workstations
        events.add(new Event(article2, station2, 0));
        events.add(new Event(article1, station1, 0));
        events.add(new Event(article2, station3, 4));
        events.add(new Event(article1, station3, 11));

        // Generating dependencies
        flowDependency.eventsByItem(events);
        flowDependency.generateDependencies(flowDependency.getEventsByItem());

        Map<WorkStation, Map<WorkStation, Integer>> flow = flowDependency.getFlowDependencyWorkStation();

        // Assertions for the generated dependencies
        assertTrue("The flow should contain WS1", flow.containsKey(station1));
        assertTrue("The flow should contain WS2", flow.containsKey(station2));

        Map<WorkStation, Integer> dependenciesFromWs1 = flow.get(station1);
        Map<WorkStation, Integer> dependenciesFromWs2 = flow.get(station2);

        assertTrue("WS1 should lead to WS3", dependenciesFromWs1.containsKey(station3));
        assertEquals("WS1 to WS3 should occur 1 time", 1, dependenciesFromWs1.get(station3).intValue());

        assertTrue("WS2 should lead to WS3", dependenciesFromWs2.containsKey(station3));
        assertEquals("WS2 to WS3 should occur 1 time", 1, dependenciesFromWs2.get(station3).intValue());
    }

    /**
     * Tests the {@link FlowDependency#generateDependencies(Map)} method when no flow 
     * exists between workstations, ensuring the flow map is empty.
     */
    @Test
    public void testNoFlowBetweenWorkstations() {
        FlowDependency flowDependency = new FlowDependency();
        List<Event> events = new ArrayList<>();

        // Creating an article with one operation
        Article article1 = new Article("item1", Priority.NORMAL, Arrays.asList("Op1"), 0);

        // Creating workstations with no flow
        WorkStation ws1 = new WorkStation("WS1", "Op1", 5);
        WorkStation ws2 = new WorkStation("WS2", "Op2", 10);

        // Adding a single event
        events.add(new Event(article1, ws1, 0));

        // Generating dependencies
        flowDependency.eventsByItem(events);
        flowDependency.generateDependencies(flowDependency.getEventsByItem());

        Map<WorkStation, Map<WorkStation, Integer>> flow = flowDependency.getFlowDependencyWorkStation();

        // Asserting the flow map is empty
        assertTrue(flow.isEmpty());
    }
}
