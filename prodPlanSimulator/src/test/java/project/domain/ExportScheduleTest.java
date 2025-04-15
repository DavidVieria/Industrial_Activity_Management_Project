/**
 * Test class for ExportSchedule functionality.
 * Validates the process of generating and exporting project schedule data based on graph representation of activities.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;
import project.domain.model.Activity;
import project.domain.service.ActivityInfo;
import project.domain.service.ActivityTimeCalculator;
import project.domain.service.ExportSchedule;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExportScheduleTest {

    /**
     * Graph representing activities and their dependencies.
     */
    private Graph<ActivityInfo, Double> graph;

    /**
     * Name of the test file to export schedule data.
     */
    private String testFileName = "testSchedule.csv";

    /**
     * Sets up the graph with sample activities and relationships for testing.
     */
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
    }

    /**
     * Tests the generation of schedule data as a string.
     */
    @Test
    public void testGenerateScheduleData() {
        try {
            String scheduleData = ExportSchedule.generateScheduleData(graph);

            // Validate the presence of the header line
            assertTrue(scheduleData.contains("act_id;cost;duration;es;ls;ef;lf;slack;prev_act_id1;prev_act_id2;prev_act_id3;prev_act_id4;prev_act_id5"));

            // Validate the presence of activity data
            for (ActivityInfo activityInfo : graph.vertices()) {
                Activity activity = activityInfo.getActivity();

                assertTrue(scheduleData.contains(activity.getActID()));
                assertTrue(scheduleData.contains(String.valueOf(activity.getCost())));
                assertTrue(scheduleData.contains(String.valueOf(activity.getDuration())));
                assertTrue(scheduleData.contains(String.valueOf(activityInfo.getEs())));
                assertTrue(scheduleData.contains(String.valueOf(activityInfo.getLs())));
                assertTrue(scheduleData.contains(String.valueOf(activityInfo.getEf())));
                assertTrue(scheduleData.contains(String.valueOf(activityInfo.getLf())));
                assertTrue(scheduleData.contains(String.valueOf(activityInfo.getSlack())));

                for (ActivityInfo predecessor : graph.incomingVertices(activityInfo)) {
                    assertTrue(scheduleData.contains(predecessor.getActivity().getActID()));
                }
            }
        } catch (Exception e) {
            fail("Error during schedule data generation: " + e.getMessage());
        }
    }

    /**
     * Tests the export of project schedule data to a file.
     */
    @Test
    public void testExportProjectSchedule() {
        try {
            ExportSchedule.exportProjectSchedule(graph, testFileName);

            // Validate that the file was created
            File file = new File(testFileName);
            assertTrue("The file was not created", file.exists());

            // Validate the content of the file
            String fileContent = new String(Files.readAllBytes(Paths.get(testFileName)));
            assertTrue("The file does not contain the expected data", fileContent.contains("act_id;cost;duration;es;ls;ef;lf;slack"));

            // Cleanup: delete the test file
            Files.delete(Paths.get(testFileName));
        } catch (IOException e) {
            fail("Error during schedule export: " + e.getMessage());
        }
    }
}
