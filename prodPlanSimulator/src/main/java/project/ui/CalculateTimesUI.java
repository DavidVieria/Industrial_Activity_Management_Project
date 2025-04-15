package project.ui;

import project.controller.ActivityGraphController;
import project.controller.CalculateTimesController;
import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;

public class CalculateTimesUI implements Runnable {

    private final ActivityGraphController activityGraphController;
    private final CalculateTimesController timesController;

    public CalculateTimesUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.timesController = new CalculateTimesController(activityGraphController.getGraph());
    }

    public void run() {
        if (activityGraphController.isDependencyCircularDetector()) {
            System.out.println("Functionality is not possible as circular dependencies were detected.");
            return;
        }

        try {
            Graph<ActivityInfo, Double> updatedGraph = timesController.calculateTimes();

            if (updatedGraph != null && !updatedGraph.vertices().isEmpty()) {
                System.out.println("\nVertices updated with calculated times --------------------------------\n");
                printVertices(updatedGraph);
            } else {
                printNoVerticesMessage();
            }
        } catch (Exception e) {
            printErrorMessage(e);
        }

        activityGraphController.setCalculatedTimes(true);
    }

    private void printNoVerticesMessage() {
        System.out.println(" ⚠️ No activities found to update times.");
    }

    private void printErrorMessage(Exception e) {
        System.err.println(" ❗ Error calculating times: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        System.err.println("Please check the graph structure and try again.");
    }

    private void printVertices(Graph<ActivityInfo, Double> updatedGraph) {
        for (ActivityInfo activityInfo : updatedGraph.vertices()) {
            String formattedInfo = formatActivityInfoWithTiming(activityInfo);
            if (formattedInfo != null) {
                System.out.println(formattedInfo);
            }
        }
    }

    private String formatActivityInfoWithTiming(ActivityInfo activityInfo) {
        String actID = activityInfo.getActivity().getActID();
        String description = activityInfo.getActivity().getDescription();
        double duration = activityInfo.getActivity().getDuration();
        String durationUnit = activityInfo.getActivity().getDurationUnit();
        double cost = activityInfo.getActivity().getCost();
        String costUnit = activityInfo.getActivity().getCostUnit();

        if (duration == 0 || cost == 0) {
            return null;
        } else {
            return String.format(" • %s (%.0f %s / %.2f %s): ES=%.0f, LS=%.0f, EF=%.0f, LF=%.0f, Slack=%.0f",
                    actID, duration, durationUnit, cost, costUnit,
                    activityInfo.getEs(), activityInfo.getLs(), activityInfo.getEf(), activityInfo.getLf(), activityInfo.getSlack());
        }
    }
}
