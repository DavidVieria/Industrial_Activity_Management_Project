package project.ui;

import project.controller.FlowDependencyController;
import project.controller.SimulatorController;
import project.domain.model.WorkStation;

import java.util.Map;

public class FlowDependencyUI implements Runnable {
    private final SimulatorController simulatorController;
    private final FlowDependencyController flowDependencyController;

    public FlowDependencyUI(SimulatorController simulatorController) {
        this.simulatorController = simulatorController;
        this.flowDependencyController = new FlowDependencyController();
    }

    public void run() {
        System.out.println("\nWorkstation Dependency Flow:");
        for (WorkStation ws : flowDependencyController.generateWorkstationFlow().keySet()) {
            System.out.print(ws.getWorkStationID() + ": [");
            Map<WorkStation, Integer> dependencies = flowDependencyController.generateWorkstationFlow().get(ws);
            boolean first = true;

            for (Map.Entry<WorkStation, Integer> dependency : dependencies.entrySet()) {
                if (!first) {
                    System.out.print(", ");
                }
                System.out.print("(" + dependency.getKey().getWorkStationID() + ", " + dependency.getValue() + ")");
                first = false;
            }
            System.out.println("]");
        }
    }

}
