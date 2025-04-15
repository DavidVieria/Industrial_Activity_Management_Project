package project.ui;

import project.controller.ExecutionTimeOperationController;
import project.controller.SimulatorController;

import java.util.Map;

public class ExecutionTimeOperationUI implements Runnable{
    private final SimulatorController simulatorController;
    private final ExecutionTimeOperationController executionTimeOperationController;

    public ExecutionTimeOperationUI(SimulatorController simulatorController) {
        this.simulatorController = simulatorController;
        executionTimeOperationController = new ExecutionTimeOperationController();
    }

    public void run() {
        System.out.printf("\n%-20s | %-20s%n", "Operation", "Execution Time (s)");
        System.out.println("-----------------------------------------");
        for (Map.Entry<String, Integer> entry : executionTimeOperationController.executionTimeOperation().entrySet()) {
            System.out.printf("%-20s | %10ds%n", entry.getKey(), entry.getValue());
        }
    }
}
