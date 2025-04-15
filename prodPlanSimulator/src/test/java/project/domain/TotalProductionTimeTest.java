package project.domain;

import org.junit.Test;
import project.domain.enums.Priority;
import project.domain.model.Article;
import project.domain.model.Event;
import project.domain.model.WorkStation;
import project.domain.service.TotalProductionTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the Simulator class.
 * This class contains unit tests for simulating item processing in a factory setting,
 * including scenarios for item prioritization, operation execution times, and workstation utilization.
 */
public class TotalProductionTimeTest {

    /**
     * Tests the simulation of high concurrency with multiple items processed across multiple workstations.
     * This test checks if all operations are completed correctly,
     * and verifies the total time of the simulation considering concurrent processing.
     */
    @Test
    public void testTotalProductionTime() {
        TotalProductionTime totalProductionTime = new TotalProductionTime();
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

        assertEquals("The total time of the simulation should be correct", 18, totalProductionTime.getTotalTime(events));

    }

    @Test
    public void testTotalProductionTimeWithDifferentLastEvent() {
        TotalProductionTime totalProductionTime = new TotalProductionTime();
        List<Event> events = new ArrayList<>();

        WorkStation station1 = new WorkStation("WS1", "Cutting", 10);
        WorkStation station2 = new WorkStation("WS2", "Polishing", 5);

        Article article1 = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting"), 0);
        Article article2 = new Article("Item2", Priority.LOW , Arrays.asList("Polishing"), 1);

        events.add(new Event(article1, station1, 0));
        events.add(new Event(article2, station2, 0));

        assertEquals("The total time of the simulation should be correct", 10, totalProductionTime.getTotalTime(events));

    }
}