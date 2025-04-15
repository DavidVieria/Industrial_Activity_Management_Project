package project.ui;

import project.controller.SimulatorController;
import project.controller.TotalProductionTimeController;

public class TotalProductionTimeUI implements Runnable{
    private final SimulatorController simulatorController;
    private final TotalProductionTimeController totalProductionTimeController;

    public TotalProductionTimeUI(SimulatorController simulatorController) {
        this.simulatorController = simulatorController;
        this.totalProductionTimeController = new TotalProductionTimeController();
    }

    public void run() {
        System.out.println("\nTotal production time: " + totalProductionTimeController.getTotalTime() + "s");
    }
}
