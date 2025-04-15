package project.ui;

import project.controller.ActivityGraphController;
import project.controller.GraphCriticalPathController;

import java.util.List;

public class GraphCriticalPathUI implements Runnable {
    private final ActivityGraphController activityGraphController;
    private final GraphCriticalPathController graphCriticalPathController;

    public GraphCriticalPathUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.graphCriticalPathController = new GraphCriticalPathController(activityGraphController.getGraph());
    }

    @Override
    public void run() {
        if (activityGraphController.isDependencyCircularDetector()) {
            System.out.println("Functionality is not possible as circular dependencies were detected.");
            return;
        }
        if (!activityGraphController.isCalculatedTimes()) {
            System.out.println("Times have not yet been calculated, perform functionality 3.");
            return;
        }

        List<List<String>> criticalPaths = graphCriticalPathController.generateCriticalPaths();
        double totalTimeDuration = graphCriticalPathController.getTotalTimeDuration();
        String unit = activityGraphController.getGraph().vertex(2).getActivity().getDurationUnit();

        if (criticalPaths.isEmpty()) {
            System.out.println("There are no critical paths!");
            System.out.printf("Total time - %.0f %s.\n\n", totalTimeDuration, unit);
        } else if (criticalPaths.size() == 1) {
            System.out.printf("There is %d critical Path!\n", criticalPaths.size());
            System.out.printf("Total time - %.0f %s.\n\n", totalTimeDuration, unit);
            int contador = 1;

            for (List<String> path : criticalPaths) {
                System.out.printf("Critical path %d: ", contador);
                contador++;
                printPathExcludingStartEnd(path);
            }
        } else {
            System.out.printf("There are %d critical Paths!\n", criticalPaths.size());
            System.out.printf("Total time - %.0f %s.\n\n", totalTimeDuration, unit);
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
