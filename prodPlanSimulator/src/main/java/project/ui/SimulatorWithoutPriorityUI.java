package project.ui;

import project.controller.DataLoaderController;
import project.controller.SimulatorController;
import project.domain.model.Event;
import project.ui.menu.MenuItem;
import project.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SimulatorWithoutPriorityUI implements Runnable{
    private final SimulatorController simulatorController;
    private final DataLoaderController dataLoaderController;

    public SimulatorWithoutPriorityUI(DataLoaderController dataLoaderController) {
        this.dataLoaderController = dataLoaderController;
        this.simulatorController = new SimulatorController();
    }

    public void run() {
        dataLoaderController.readSimulatorFiles();

        if (!dataLoaderController.isSimulatorDataLoadedSuccessfully()) {
            System.err.println("Failed to load data files. Exiting simulator.");
            return;
        }

        simulate();

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Calculate total production time for the items", new TotalProductionTimeUI(simulatorController)));
        options.add(new MenuItem("Calculate execution times for each operation", new ExecutionTimeOperationUI(simulatorController)));
        options.add(new MenuItem("List machines with total operation time and percentages relative to the time", new WorkstationsStatisticsUI(simulatorController)));
        options.add(new MenuItem("Calculate average execution times per operation and waiting times", new OperationStatisticsUI(simulatorController)));
        options.add(new MenuItem("Listing representing the flow dependency between machines", new FlowDependencyUI(simulatorController)));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n=== Simulator Menu ===");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    private void simulate() {
        simulatorController.simulatorWithoutPriority();
        System.out.println("\n\n=== Simulator Execution ===\n");
        System.out.printf("\n%-15s | %-15s | %-15s | %-15s%n", "Item", "WorkStation", "Start Time (s)", "End Time (s)");
        System.out.println("-----------------------------------------------------------------------");
        for (Event event : simulatorController.getEventRepository().getAllEvents()) {
            System.out.printf("%-15s | %-15s | %-15s | %-15s%n",
                    event.getArticle().getIdArticle(),
                    event.getWorkStation().getWorkStationID(),
                    event.getStartTime() + "s",
                    event.getEndTime() + "s"
            );
        }
        System.out.printf("\nNumber of Events: %d\n", simulatorController.getEventRepository().getSize());
        System.out.println("\nSimulation completed.");
    }

}
