package project.domain.service;

import project.domain.genericDataStructures.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Provides functionality to export project schedules to a file.
 */
public class ExportSchedule {

    /**
     * Exports the project schedule data from the given graph to a specified file.
     *
     * @param graph the graph representing the project schedule
     * @param fileName the name of the file to export the schedule to
     * @throws IOException if an error occurs while writing to the file
     */
    public static void exportProjectSchedule(Graph<ActivityInfo, Double> graph, String fileName) throws IOException {
        String scheduleData = generateScheduleData(graph);
        exportToFile(scheduleData, fileName);
    }

    /**
     * Generates a string representation of the project schedule data in CSV format.
     *
     * @param graph the graph representing the project schedule
     * @return a string containing the schedule data in CSV format
     */
    public static String generateScheduleData(Graph<ActivityInfo, Double> graph) {
        StringBuilder data = new StringBuilder();

        // Add the header row
        data.append("act_id;cost;duration;es;ls;ef;lf;slack;prev_act_id1;prev_act_id2;prev_act_id3;prev_act_id4;prev_act_id5\n");

        // Iterate through all vertices in the graph
        for (int i = 0; i < graph.numVertices(); i++) {
            ActivityInfo activityInfo = graph.vertex(i);

            // Extract activity details
            String act_id = activityInfo.getActivity().getActID();
            double cost = activityInfo.getActivity().getCost();
            double duration = activityInfo.getActivity().getDuration();
            double es = activityInfo.getEs();
            double ls = activityInfo.getLs();
            double ef = activityInfo.getEf();
            double lf = activityInfo.getLf();
            double slack = activityInfo.getSlack();

            // Append activity details to the data string
            data.append(act_id).append(";").append(cost).append(";").append(duration).append(";")
                    .append(es).append(";").append(ls).append(";").append(ef).append(";").append(lf)
                    .append(";").append(slack);

            // Append predecessors' activity IDs
            for (ActivityInfo predecessor : graph.incomingVertices(activityInfo)) {
                data.append(";").append(predecessor.getActivity().getActID());
            }

            // Add a newline for the next activity
            data.append("\n");
        }

        return data.toString();
    }

    /**
     * Writes the given schedule data to a specified file.
     *
     * @param scheduleData the schedule data to write
     * @param fileName the name of the file to write to
     * @throws IOException if an error occurs while writing to the file
     */
    public static void exportToFile(String scheduleData, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(scheduleData);
            writer.flush();
        }
    }
}
