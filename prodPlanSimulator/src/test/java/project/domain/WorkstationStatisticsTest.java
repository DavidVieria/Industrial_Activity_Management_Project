/**
 * Unit tests for the {@link WorkstationStatistics} class, which calculates
 * the total and percentage execution times of workstations based on events.
 */
package project.domain;

import org.junit.Test;
import project.domain.enums.Priority;
import project.domain.model.Article;
import project.domain.model.Event;
import project.domain.model.WorkStation;
import project.domain.service.WorkstationStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WorkstationStatisticsTest {

    /**
     * Tests the calculation of total execution time for each workstation
     * using a list of {@link Event} objects. Verifies that the calculated times
     * match the expected values.
     */
    @Test
    public void testTotalTimeWorkStation() {

        WorkstationStatistics workstationStatistics = new WorkstationStatistics();
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

        workstationStatistics.totalTimeWorkStation(events);

        Map<WorkStation, Integer> totalTime = workstationStatistics.getTotalTimeWorkStation();

        assertEquals("The total time for WS1 (Op1) should be 5", (Integer) 5, totalTime.get(station1));
        assertEquals("The total time for WS2 (Op2) should be 4", (Integer) 4, totalTime.get(station2));
        assertEquals("The total time for WS3 (Op3) should be 14", (Integer) 14, totalTime.get(station3));
    }

    /**
     * Tests that a workstation with zero execution time is not included
     * in the total time map. Verifies the absence of such workstations.
     */
    @Test
    public void totalTimeWorkStationZeroTimeNotPresent() {
        WorkstationStatistics workstationStatistics = new WorkstationStatistics();
        List<Event> events = new ArrayList<>();

        Article article1 = new Article("item1", Priority.NORMAL, Arrays.asList("Op1"), 0);

        WorkStation ws1 = new WorkStation("WS1", "Op1", 5);
        WorkStation ws2 = new WorkStation("WS2", "Op2", 10);

        events.add(new Event(article1, ws1, 0));

        workstationStatistics.totalTimeWorkStation(events);

        Map<WorkStation, Integer> totalTime = workstationStatistics.getTotalTimeWorkStation();

        assertEquals("The execution time for WS1 should be 5", (Integer) 5, totalTime.get(ws1));
        assertFalse("WS2 should not be present in the execution times map", totalTime.containsKey(ws2));
    }

    /**
     * Tests the calculation of the percentage of global execution time
     * contributed by each workstation. Verifies that the calculated
     * percentages match the expected values with a precision of 0.1.
     */
    @Test
    public void testPercentageTimeWorkStationGlobalTime() {
        WorkstationStatistics workstationStatistics = new WorkstationStatistics();
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

        workstationStatistics.totalTimeWorkStation(events);
        workstationStatistics.percentageTimeWorkStationGlobalTime();

        Map<WorkStation, Double> percentageTime = workstationStatistics.getPercentageTimeWorkStation();

        double percentageOp1 = percentageTime.get(station1);
        double percentageOp2 = percentageTime.get(station2);
        double percentageOp3 = percentageTime.get(station3);

        assertEquals("The percentage of WS1 should be 21.7%", 21.7, percentageOp1, 0.1);
        assertEquals("The percentage of WS2 should be 17.3%", 17.3, percentageOp2, 0.1);
        assertEquals("The percentage of WS3 should be 60.8%", 60.8, percentageOp3, 0.1);
    }

}
