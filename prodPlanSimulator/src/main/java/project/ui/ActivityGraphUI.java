package project.ui;

import project.controller.ActivityGraphController;
import project.controller.BD.StructureGraphController;
import project.controller.DataLoaderController;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;
import project.domain.service.ActivityInfo;
import project.ui.menu.MenuItem;
import project.ui.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityGraphUI implements Runnable {

    private final ActivityGraphController graphController;
    private final StructureGraphController structureGraphController;
    private final DataLoaderController dataLoaderController;
    private Graph<ActivityInfo, Double> graph;

    public ActivityGraphUI(DataLoaderController dataLoaderController) {
        this.dataLoaderController = dataLoaderController;
        this.graph = new MapGraph<>(true);
        this.graphController = new ActivityGraphController(graph);
        this.structureGraphController = new StructureGraphController();

    }

    public void run() {
        dataLoaderController.readActivitiesFiles();

        if (!dataLoaderController.isActivitiesDataLoadedSuccessfully()) {
            System.err.println("Failed to load data files. Exiting.");
            return;
        }

        if (graphController.graph() == null) {
            System.out.println("Invalid file.\nThere are activities with non-existent preceding activities.");
            return;
        } else {
            graph = graphController.graph();
            printGraph();
            try {
                if (structureGraphController.execute(graph)) {
                    System.out.println("\nGraph generation completed. Check the PNG files.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Detect Circular Dependencies", new CircularDependencyDetectorUI(graphController)));
        options.add(new MenuItem("Topological sort of project activities", new TopologicalSortActivitiesUI(graphController)));
        options.add(new MenuItem("Calculate Earliest and Latest Start and Finish Times", new CalculateTimesUI(graphController)));
        options.add(new MenuItem("Export Project Schedule to CSV", new ExportScheduleUI(graphController)));
        options.add(new MenuItem("Identify the Critical Path(s)", new GraphCriticalPathUI(graphController)));
        options.add(new MenuItem("Identify Bottlenecks Activities in the Project Graph", new BottlenecksActivitiesUI(graphController)));
        options.add(new MenuItem("Simulate Project Delays and their Impact", new SimulateProjectDelaysUI(graphController)));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n=== Project Activity Menu ===");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    public void printGraph() {
        if (graph != null) {
            System.out.println();
            for (int i = 0; i < graph.numVertices(); i++) {
                for (int j = 0; j < graph.numVertices(); j++) {
                    if (graph.edge(i, j) != null) {
                        ActivityInfo activityLeft = graph.edge(i, j).getVOrig();
                        ActivityInfo activityRight = graph.edge(i, j).getVDest();

                        String leftInfo = formatActivityInfo(activityLeft);
                        String rightInfo = formatActivityInfo(activityRight);

                        System.out.printf("%s --> %s\n", leftInfo, rightInfo);
                    }
                }
            }
        } else {
            System.out.println("The Graph is empty.");
        }
    }

    private String formatActivityInfo(ActivityInfo activityInfo) {
        String actID = activityInfo.getActivity().getActID();
        String description = activityInfo.getActivity().getDescription();
        double duration = activityInfo.getActivity().getDuration();
        String durationUnit = activityInfo.getActivity().getDurationUnit();
        double cost = activityInfo.getActivity().getCost();
        String costUnit = activityInfo.getActivity().getCostUnit();

        if (duration == 0 || cost == 0) {
            return String.format("%s", description);
        } else {
            return String.format("%s: %.0f %s / %.2f %s", actID, duration, durationUnit, cost, costUnit);
        }
    }

}
