package project.ui;

import project.controller.ActivityGraphController;
import project.controller.SimulateProjectDelaysController;
import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SimulateProjectDelaysUI implements Runnable {

    private ActivityGraphController activityGraphController;
    private SimulateProjectDelaysController simulateProjectDelaysController;

    public SimulateProjectDelaysUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.simulateProjectDelaysController = new SimulateProjectDelaysController(activityGraphController.getGraph());
    }

    public void run() {
        if (activityGraphController.isDependencyCircularDetector()) {
            System.out.println("Functionality is not possible as circular dependencies were detected.");
            return;
        }
        if (!activityGraphController.isCalculatedTimes()) {
            System.out.println("Times have not yet been calculated, perform functionality 3.");
            return;
        }

        Map<String, Double> delayedActivitiesMap = collectDelayedActivities();
        boolean success= simulateProjectDelaysController.simulateDelay(delayedActivitiesMap);

        if (!success) {
            System.out.println("\nActivities were not found.");
        } else {
            printGraph(simulateProjectDelaysController.getGraph());
            printCriticalPaths(simulateProjectDelaysController.getGraphCriticalPathResult());

            String unit = simulateProjectDelaysController.getGraph().vertex(2).getActivity().getDurationUnit();

            if (simulateProjectDelaysController.getImpactDuration() == 0) {
                System.out.printf("\nDespite the delay in activities, it did not affect the duration of the project (%.0f %s).", simulateProjectDelaysController.getDurationProjectWithDelay(), unit);
            } else {
                System.out.println("\nProject duration:");
                System.out.printf("- Before simulation delay: %.0f %s", simulateProjectDelaysController.getDurationProjectNoDelay(), unit);
                System.out.printf("\n- After simulation delay: %.0f %s", simulateProjectDelaysController.getDurationProjectWithDelay(), unit);

                if (simulateProjectDelaysController.getImpactDuration() < 0) {
                    System.out.printf("\n\nProject duration was advanced by %.0f %s.", simulateProjectDelaysController.getImpactDuration(), unit);
                }
                if (simulateProjectDelaysController.getImpactDuration() > 0) {
                    System.out.printf("\n\nProject duration was delayed by %.0f %s.", simulateProjectDelaysController.getImpactDuration(), unit);
                }
            }
        }
    }

    private Map<String, Double> collectDelayedActivities() {
        Map<String, Double> delayedActivitiesMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the activity ID: ");
            String activityId = scanner.nextLine().trim();

            System.out.print("Enter the delay duration (in units): ");
            double delayDuration;
            try {
                String input = scanner.nextLine().trim().replace(",", ".");
                delayDuration = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for duration. Please enter a valid number.");
                continue;
            }

            delayedActivitiesMap.put(activityId, delayDuration);

            String response;
            while (true) {
                System.out.print("Do you want to add another activity? (yes/no): ");
                response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("yes") || response.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid response. Please enter 'yes' or 'no'.");
                }
            }

            if (response.equals("no")) {
                break;
            }
        }
        return delayedActivitiesMap;
    }

    public void printGraph(Graph<ActivityInfo, Double> graph) {
        if (graph != null) {
            System.out.println();
            for (ActivityInfo activityInfo : graph.vertices()) {
                if (formatActivityInfoWithTiming(activityInfo) != null) {
                    System.out.println(formatActivityInfoWithTiming(activityInfo));
                }
            }
        } else {
            System.out.println("The Graph is empty.");
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
            return String.format("%s (%.0f %s / %.2f %s): ES=%.0f, LS=%.0f, EF=%.0f, LF=%.0f, Slack=%.0f",
                    actID, duration, durationUnit, cost, costUnit,
                    activityInfo.getEs(), activityInfo.getLs(), activityInfo.getEf(), activityInfo.getLf(), activityInfo.getSlack());
        }
    }

    public void printCriticalPaths (List<List<String>> criticalPaths) {
        System.out.println();

        if (criticalPaths.isEmpty()) {
            System.out.println("There are no critical paths!");
        } else if (criticalPaths.size() == 1) {
            System.out.printf("There is %d critical Path!\n", criticalPaths.size());
            int contador = 1;

            for (List<String> path : criticalPaths) {
                System.out.printf("Critical path %d: ", contador);
                contador++;
                for (String vertice : path) {
                    System.out.printf("%s -> ", vertice);
                }
                System.out.println("END");
            }
        } else {
            System.out.printf("There is %d critical Paths!\n", criticalPaths.size());
            int contador = 1;

            for (List<String> path : criticalPaths) {
                System.out.printf("Critical path %d: ", contador);
                contador++;
                printPathExcludingStartEnd(path);
            }
        }

    }

    private void printPathExcludingStartEnd(List<String> path) {
        boolean isFirst = true;
        for (String vertice : path) {
            if (!"Start".equals(vertice) && !"End".equals(vertice)) {
                if (!isFirst) {
                    System.out.print(" -> ");
                }
                System.out.print(vertice);
                isFirst = false;
            }
        }
        System.out.println();
    }

}