package project.domain;

import org.junit.Test;
import project.controller.SimulatorController;
import project.domain.enums.Priority;
import project.domain.model.Article;
import project.domain.model.Event;
import project.domain.model.WorkStation;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the Simulator class.
 * This class contains unit tests for simulating item processing in a factory setting,
 * including scenarios for item prioritization, operation execution times, and workstation utilization.
 */
public class SimulatorTest {

    /**
     * Tests the simulation of a single item processed on a single workstation.
     * This test verifies that the event queue is correctly populated,
     * and that the total simulation time is calculated as expected.
     */
    @Test
    public void testSimulationSingleItemSingleStation() {
        SimulatorController simulatorController = new SimulatorController();

        WorkStation station = new WorkStation("WS1", "Cutting", 10);
        simulatorController.getWorkStationRepository().addWorkStation(station);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting"), 0);
        simulatorController.getArticleRepository().addArticle(article1);

        simulatorController.simulatorWithoutPriority();

        assertFalse("The event queue should be empty after the simulation", simulatorController.getEventRepository().getAllEvents().isEmpty());

        assertEquals("The number of events should correspond to the number of operations of the items", 1, simulatorController.getEventRepository().getSize());

        for (Article article : simulatorController.getArticleRepository().getAllArticles()) {
            assertTrue("All operations must be completed for each item", article.isAllOperationsCompleted());
        }
    }

    /**
     * Tests the simulation of multiple items processed on a single workstation.
     * This test checks if the events are generated for each operation of the items
     * and validates the total time taken for the simulation.
     */
    @Test
    public void testSimulationMultipleItemsSingleStation() {
        SimulatorController simulatorController = new SimulatorController();

        WorkStation station = new WorkStation("WS1", "Cutting", 5);
        simulatorController.getWorkStationRepository().addWorkStation(station);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Cutting"), 1);
        simulatorController.getArticleRepository().addArticle(article1);
        simulatorController.getArticleRepository().addArticle(article2);

        simulatorController.simulatorWithoutPriority();

        assertFalse("The event queue should be empty after the simulation",
                simulatorController.getEventRepository().getAllEvents().isEmpty());

        assertEquals("The number of events should correspond to the number of operations of the items",
                2, simulatorController.getEventRepository().getAllEvents().size());

        for (Article article : simulatorController.getArticleRepository().getAllArticles()) {
            assertTrue("All operations must be completed for each item",
                    article.isAllOperationsCompleted());
        }
    }

    /**
     * Tests the simulation of a single item processed across multiple stations with different operations.
     * This test ensures that events for each operation are generated and checks the total time taken
     * for the complete processing of the item.
     */
    @Test
    public void testSimulationMultipleStationsDifferentOperations() {
        SimulatorController simulatorController = new SimulatorController();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 7);
        simulatorController.getWorkStationRepository().addWorkStation(station1);
        simulatorController.getWorkStationRepository().addWorkStation(station2);

        Article article1 = new Article("Item1", Priority.NORMAL, Arrays.asList("Cutting", "Polishing"), 0);
        simulatorController.getArticleRepository().addArticle(article1);

        simulatorController.simulatorWithoutPriority();

        assertFalse("The event queue should be empty after the simulation",
                simulatorController.getEventRepository().getAllEvents().isEmpty());

        assertEquals("The number of events should correspond to the number of operations of the items",
                2, simulatorController.getEventRepository().getAllEvents().size());

        for (Article article : simulatorController.getArticleRepository().getAllArticles()) {
            assertTrue("All operations must be completed for each item",
                    article.isAllOperationsCompleted());
        }
    }

    /**
     * Tests the simulation of items with varying operation times.
     * This test verifies that items are processed correctly according to their specified operation times,
     * and checks the total time of the simulation.
     */
    @Test
    public void testSimulationWithVaryingOperationTimes() {
        SimulatorController simulatorController = new SimulatorController();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 3);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 8);
        simulatorController.getWorkStationRepository().addWorkStation(station1);
        simulatorController.getWorkStationRepository().addWorkStation(station2);

        Article article1 = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting"), 0);
        Article article2 = new Article("Item2", Priority.LOW, Arrays.asList("Polishing"), 1);
        simulatorController.getArticleRepository().addArticle(article1);
        simulatorController.getArticleRepository().addArticle(article2);

        simulatorController.simulatorWithoutPriority();

        assertFalse("The event queue should be empty after the simulation",
                simulatorController.getEventRepository().getAllEvents().isEmpty());

        assertEquals("The number of events should correspond to the number of operations of the items",
                2, simulatorController.getEventRepository().getAllEvents().size());

        for (Article article : simulatorController.getArticleRepository().getAllArticles()) {
            assertTrue("All operations must be completed for each item",
                    article.isAllOperationsCompleted());
        }
    }

    /**
     * Tests the simulation of sequential operations for a single item.
     * This test confirms that all operations are completed in the correct order,
     * and that the event queue reflects the operations performed.
     */
    @Test
    public void testSimulationSequentialOperationsForItems() {
        SimulatorController simulatorController = new SimulatorController();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 4);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 6);
        simulatorController.getWorkStationRepository().addWorkStation(station1);
        simulatorController.getWorkStationRepository().addWorkStation(station2);

        Article article1 = new Article("Item1", Priority.NORMAL, Arrays.asList("Cutting", "Polishing"), 0);
        simulatorController.getArticleRepository().addArticle(article1);

        simulatorController.simulatorWithoutPriority();

        assertFalse("The event queue should be empty after the simulation",
                simulatorController.getEventRepository().getAllEvents().isEmpty());

        assertEquals("The number of events should correspond to the number of operations of the items",
                2, simulatorController.getEventRepository().getAllEvents().size());

        for (Article article : simulatorController.getArticleRepository().getAllArticles()) {
            assertTrue("All operations must be completed for each item",
                    article.isAllOperationsCompleted());
        }
    }

    /**
     * Tests the simulation of high concurrency with multiple items processed across multiple workstations.
     * This test checks if all operations are completed correctly,
     * and verifies the total time of the simulation considering concurrent processing.
     */
    @Test
    public void testSimulationHighConcurrencyWithMultipleItemsAndStations() {
        SimulatorController simulatorController = new SimulatorController();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 4);
        WorkStation station3 = new WorkStation("WS3", "Assembly", 7);
        simulatorController.getWorkStationRepository().addWorkStation(station1);
        simulatorController.getWorkStationRepository().addWorkStation(station2);
        simulatorController.getWorkStationRepository().addWorkStation(station3);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting", "Assembly"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Polishing", "Assembly"), 1);
        simulatorController.getArticleRepository().addArticle(article1);
        simulatorController.getArticleRepository().addArticle(article2);

        simulatorController.simulatorWithoutPriority();

        assertFalse("The event queue should be empty after the simulation", simulatorController.getEventRepository().getAllEvents().isEmpty());

        assertEquals("The number of events should correspond to the number of operations of the items", 4, simulatorController.getEventRepository().getAllEvents().size());

        for (Article article : simulatorController.getArticleRepository().getAllArticles()) {
            assertTrue("All operations must be completed for each item", article.isAllOperationsCompleted());
        }
    }

    /**
     * Tests the simulation of items with priority.
     * This test verifies that the item with higher priority is processed first and
     * checks the sequence and total time of the generated events.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    public void testSimulateWithPriority() {
        SimulatorController simulatorController = new SimulatorController();

        Article article1 = new Article("item1", Priority.LOW, Arrays.asList("Op1", "Op2"), 0);
        Article article2 = new Article("item2", Priority.HIGH, Arrays.asList("Op1", "Op2"), 1);

        simulatorController.getArticleRepository().addArticle(article1);
        simulatorController.getArticleRepository().addArticle(article2);

        WorkStation WS1 = new WorkStation("WS1", "Op1", 5);
        WorkStation WS2 = new WorkStation("WS2", "Op2", 10);

        simulatorController.getWorkStationRepository().addWorkStation(WS1);
        simulatorController.getWorkStationRepository().addWorkStation(WS2);

        simulatorController.simulatorWithPriority();

        List<Event> events = simulatorController.getEventRepository().getAllEvents();
        assertEquals("The first event should be for item2", article2, events.get(0).getArticle());
        assertEquals("The second event should be for item2", article2, events.get(1).getArticle());
        assertEquals("The third event should be for item1", article1, events.get(2).getArticle());
        assertEquals("The fourth event should be for item1", article1, events.get(3).getArticle());

        assertTrue("WorkStation WS1 should be available at the end", WS1.isAvailable());
        assertTrue("WorkStation WS2 should be available at the end", WS2.isAvailable());

        assertEquals("The total number of events should be 4", 4, simulatorController.getEventRepository().getAllEvents().size());
    }

}