package project.ui;

import project.controller.ActivityGraphController;
import project.controller.CircularDependencyDetectorController;
import project.domain.service.ActivityInfo;

import java.util.List;

public class CircularDependencyDetectorUI implements Runnable {
    private final ActivityGraphController activityGraphController;
    private final CircularDependencyDetectorController detectorController;

    public CircularDependencyDetectorUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.detectorController = new CircularDependencyDetectorController(activityGraphController.getGraph());
    }

    @Override
    public void run() {
        printHeader();

        try {
            List<ActivityInfo> circularDependency = detectorController.detectCircularDependencies();

            if (!circularDependency.isEmpty()) {
                activityGraphController.setDependencyCircularDetector(true);
                printCircularDependencies(circularDependency);
            } else {
                printNoCircularDependencies();
            }
        } catch (Exception e) {
            printErrorMessage(e);
        }

        printFooter();
    }

    private void printHeader() {
        String header = "Detection of Circular Dependencies";
        int lineLength = 53;
        int padding = (lineLength - header.length()) / 2;

        String centeredHeader = "=".repeat(lineLength);
        String centeredText = " ".repeat(padding) + header + " ".repeat(lineLength - padding - header.length());

        System.out.println(centeredHeader);
        System.out.println(centeredText);
        System.out.println(centeredHeader);
    }

    private void printFooter() {
        System.out.println("=====================================================");
    }

    private void printCircularDependencies(List<ActivityInfo> cycle) {
        System.out.println(" ❌ Circular dependencies were found!");
        System.out.println("-----------------------------------------------------");

        printCycle(cycle);
        System.out.println();

        System.out.println("-----------------------------------------------------");
        System.out.println("The graph is not valid due to circular dependencies.");
    }

    private void printNoCircularDependencies() {
        System.out.println(" ✅ No circular dependencies were found. \n\nThe graph is valid!");
    }

    private void printErrorMessage(Exception e) {
        System.err.println(" ❗ Unexpected Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        System.err.println("Please check the graph structure and try again.");
    }

    private void printCycle(List<ActivityInfo> cycle) {
        System.out.println("Circular Dependency Detected:");
        System.out.println("      Size: " + cycle.size() + " activities");
        System.out.print("      Path: ");

        for (int i = 0; i < cycle.size(); i++) {
            ActivityInfo activity = cycle.get(i);
            System.out.print(activity.getActivity().getActID());
            if (i < cycle.size() - 1) {
                System.out.print(" ⟶ ");
            }
        }

        System.out.println("  ⟲ (Return to " + cycle.getFirst().getActivity().getActID() + ")");
    }
}
