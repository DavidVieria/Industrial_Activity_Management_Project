package project.ui;

import project.controller.OperationStatisticsController;
import project.controller.SimulatorController;

import java.util.Map;

public class OperationStatisticsUI implements Runnable{
    private final SimulatorController simulatorController;
    private final OperationStatisticsController operationStatisticsController;

    public OperationStatisticsUI(SimulatorController simulatorController) {
        this.simulatorController = simulatorController;
        this.operationStatisticsController = new OperationStatisticsController();
    }

    public void run() {
        System.out.printf("\n%-20s | %-20s%n", "Operation", "Average Time (s)");
        System.out.println("-----------------------------------------");
        for (Map.Entry<String, Double> entry : operationStatisticsController.averageTimePerOperation().entrySet()) {
            System.out.printf("%-20s | %10.2fs%n", entry.getKey(), entry.getValue());
        }

        System.out.printf("\n%-20s | %-10s%n", "Operation", "Waiting Time (s)");
        System.out.println("-------------------------------");
        for (Map.Entry<String, Integer> entry : operationStatisticsController.getWaitTimePerOperation().entrySet()) {
            System.out.printf("%-20s | %10ds%n", entry.getKey(), entry.getValue());
        }
    }
}
