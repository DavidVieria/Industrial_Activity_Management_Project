package project.ui;

import project.controller.SimulatorController;
import project.controller.WorkstationStatisticsController;
import project.domain.model.WorkStation;

import java.util.Map;

public class WorkstationsStatisticsUI implements Runnable{
    private final SimulatorController simulatorController;
    private final WorkstationStatisticsController workstationStatisticsController;

    public WorkstationsStatisticsUI(SimulatorController simulatorController) {
        this.simulatorController = simulatorController;
        this.workstationStatisticsController = new WorkstationStatisticsController();
    }

    public void run() {
        System.out.printf("\n%-20s | %-20s%n", "WorkStation ID", "Total Time (s)");
        System.out.println("-------------------------------------");
        for (Map.Entry<WorkStation, Integer> entry : workstationStatisticsController.totalTimeWorkStation().entrySet()) {
            System.out.printf("%-20s | %10ds%n", entry.getKey().getWorkStationID(), entry.getValue());
        }

        System.out.printf("\n%-20s | %-10s%n", "WorkStation ID", "Percentage (%)");
        System.out.println("-------------------------------------");
        for (Map.Entry<WorkStation, Double> entry : workstationStatisticsController.percentageWorkstationTimeGlobalTime().entrySet()) {
            System.out.printf("%-20s | %10.2f%%%n", entry.getKey().getWorkStationID(), entry.getValue());
        }
    }
}
