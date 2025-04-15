/**
 * Unit tests for the {@link OperationStatistics} class, which processes statistics related to operations
 * including event handling, execution time, average time, and wait times for each operation.
 */
package project.domain;

import org.junit.Test;
import project.domain.enums.Priority;
import project.domain.model.Article;
import project.domain.model.Event;
import project.domain.model.WorkStation;
import project.domain.service.OperationStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OperationStatisticsTest {

    /**
     * Tests the addition of events by operation, verifying that events are correctly grouped
     * by their associated operation names.
     */
    @Test
    public void addEventByOperation() {

        OperationStatistics operationStatistics = new OperationStatistics();
        List<Event> events = new ArrayList<>();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 4);
        WorkStation station3 = new WorkStation("WS3", "Assembly", 7);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting", "Assembly"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Polishing", "Assembly"), 1);

        events.add(new Event(article2, station2, 0));
        events.add(new Event(article1, station1, 0));
        events.add(new Event(article2, station3, 4));
        events.add(new Event(article1, station3, 11));

        operationStatistics.addEventByOperation(events);

        Map<String, List<Event>> eventsOperation = operationStatistics.getEventsByOperation();

        assertEquals("The number of events for Cutting should be 1", 1, eventsOperation.get("Cutting").size());
        assertEquals("The number of events for Polishing should be 1", 1, eventsOperation.get("Polishing").size());
        assertEquals("The number of events for Assembly should be 2", 2, eventsOperation.get("Assembly").size());
    }

    /**
     * Tests the calculation of execution time for each operation, verifying that the total execution time
     * for each operation is correctly computed.
     */
    @Test
    public void executionTimeOperation() {

        OperationStatistics operationStatistics = new OperationStatistics();
        List<Event> events = new ArrayList<>();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 4);
        WorkStation station3 = new WorkStation("WS3", "Assembly", 7);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting", "Assembly"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Polishing", "Assembly"), 1);

        events.add(new Event(article2, station2, 0));
        events.add(new Event(article1, station1, 0));
        events.add(new Event(article2, station3, 4));
        events.add(new Event(article1, station3, 11));

        operationStatistics.addEventByOperation(events);
        operationStatistics.executionTimeOperation();

        Map<String, Integer> executionTimeOperation = operationStatistics.getExecutionTimeOperation();

        assertEquals("The execution time for Cutting should be 5", (Integer) 5, executionTimeOperation.get("Cutting"));
        assertEquals("The execution time for Polishing should be 4", (Integer) 4, executionTimeOperation.get("Polishing"));
        assertEquals("The execution time for Assembly should be 14", (Integer) 14, executionTimeOperation.get("Assembly"));
    }

    /**
     * Tests the case where an operation has zero execution time and verifies that
     * such operations are not included in the execution times map.
     */
    @Test
    public void executionTimeOperationZeroTimeNotPresent() {

        OperationStatistics operationStatistics = new OperationStatistics();
        List<Event> events = new ArrayList<>();

        Article article1 = new Article("item1", Priority.NORMAL, Arrays.asList("Op1"), 0);

        WorkStation ws1 = new WorkStation("WS1", "Op1", 5);
        WorkStation ws2 = new WorkStation("WS2", "Op2", 10);

        events.add(new Event(article1, ws1, 0));

        operationStatistics.addEventByOperation(events);
        operationStatistics.executionTimeOperation();

        Map<String, Integer> executionTimes = operationStatistics.getExecutionTimeOperation();

        assertEquals("The execution time for Op1 should be 5", (Integer) 5, executionTimes.get("Op1"));
        assertFalse("Op2 should not be present in the execution times map", executionTimes.containsKey("Op2"));
    }

    /**
     * Tests the calculation of the average time for each operation based on the events
     * and verifies that the computed average times match the expected values.
     */
    @Test
    public void averageTimePerOperation() {

        OperationStatistics operationStatistics = new OperationStatistics();
        List<Event> events = new ArrayList<>();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 4);
        WorkStation station3 = new WorkStation("WS3", "Assembly", 7);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting", "Assembly"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Polishing", "Assembly"), 1);

        events.add(new Event(article2, station2, 0));
        events.add(new Event(article1, station1, 0));
        events.add(new Event(article2, station3, 4));
        events.add(new Event(article1, station3, 11));

        operationStatistics.addEventByOperation(events);
        operationStatistics.executionTimeOperation();
        operationStatistics.averageTimePerOperation();

        Map<String, Double> averageTimeOperation = operationStatistics.getAverageTimeOperation();

        assertEquals("The average time for Cutting should be 5.0", 5.0, averageTimeOperation.get("Cutting"), 0.1);
        assertEquals("The average time for Polishing should be 4.0", 4.0, averageTimeOperation.get("Polishing"), 0.1);
        assertEquals("The average time for Assembly should be 7.0", 7.0, averageTimeOperation.get("Assembly"), 0.1);
    }

    /**
     * Tests the calculation of wait times for each operation. Verifies that the computed wait times
     * for each operation are correctly calculated and match the expected values.
     */
    @Test
    public void waitTimePerOperation() {

        OperationStatistics operationStatistics = new OperationStatistics();
        List<Event> events = new ArrayList<>();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 5);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 4);
        WorkStation station3 = new WorkStation("WS3", "Assembly", 7);

        Article article1 = new Article("Item1", Priority.LOW, Arrays.asList("Cutting", "Assembly"), 0);
        Article article2 = new Article("Item2", Priority.HIGH, Arrays.asList("Polishing", "Assembly"), 1);

        events.add(new Event(article2, station2, 0));
        events.add(new Event(article1, station1, 0));
        events.add(new Event(article2, station3, 4));
        events.add(new Event(article1, station3, 11));

        operationStatistics.eventsByItem(events);
        operationStatistics.waitTimePerOperation(operationStatistics.getEventsByItem());

        Map<String, Integer> waitTimes = operationStatistics.getWaitTimeOperation();

        assertEquals("The wait time for Cutting should be 0", 0, waitTimes.get("Cutting").intValue());
        assertEquals("The wait time for Polishing should be 0", 0, waitTimes.get("Polishing").intValue());
        assertEquals("The wait time for Assembly should be 6", 6, waitTimes.get("Assembly").intValue());
    }
}
