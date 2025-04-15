package project.ui.BD;

import project.controller.BD.TriggerPreventCyclesController;

import java.util.Scanner;

public class TriggerPreventCyclesUI implements Runnable {

    private final TriggerPreventCyclesController controller;

    public TriggerPreventCyclesUI() {
        this.controller = new TriggerPreventCyclesController();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the operation ID: ");
        int operationID = scanner.nextInt();

        System.out.print("Enter the part ID: ");
        String partID = scanner.next();  // Changed to next() to read a string

        System.out.print("Enter the quantity: ");
        int quantity = scanner.nextInt();

        String result = controller.insertOrUpdateOperationInput(operationID, partID, quantity);
        System.out.println("\n" + result);
    }
}