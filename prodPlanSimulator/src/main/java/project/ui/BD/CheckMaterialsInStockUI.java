package project.ui.BD;

import project.controller.BD.CheckMaterialsInStockController;

import java.util.Scanner;

public class CheckMaterialsInStockUI implements Runnable {

    private final CheckMaterialsInStockController controller;

    public CheckMaterialsInStockUI() {
        this.controller = new CheckMaterialsInStockController();
    }

    @Override
    public void run() {
        // Ask the user for the Order ID
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Order ID: ");
        String orderIDStr = scanner.nextLine();

        // Convert the Order ID to an integer
        int orderID = Integer.parseInt(orderIDStr);

        // Call the controller method to check if the stock is sufficient
        boolean isStockSufficient = controller.checkMaterialsInStockForOrder(orderID);

        // Display the result
        if (isStockSufficient) {
            System.out.println("The stock is sufficient for Order " + orderID + ".");
        } else {
            System.out.println("The stock is NOT sufficient for Order " + orderID + ".");
        }
    }
}