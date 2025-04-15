package project.ui.BD;

import project.controller.BD.TriggerOperationController;

import java.util.Scanner;

public class TriggerOperationUI implements Runnable {

    private final TriggerOperationController controller;

    public TriggerOperationUI() {
        this.controller = new TriggerOperationController();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the operation ID: ");
        int operationID = scanner.nextInt();

        System.out.print("Enter the operation type ID: ");
        int operationTypeID = scanner.nextInt();

        System.out.print("Enter the expected execution time: ");
        double expectedExecutionTime = scanner.nextDouble();

        String result = controller.insertOperation(operationID, operationTypeID, expectedExecutionTime);
        System.out.println("\n" + result);
    }
}
