package project.ui.BD;

import project.controller.BD.GenerateCSVFilesController;

import java.util.Scanner;

public class GenerateCSVFilesUI implements Runnable {

    private final GenerateCSVFilesController controller;

    public GenerateCSVFilesUI() {
        controller = new GenerateCSVFilesController();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean validProduct = false;

        while (!validProduct) {
            System.out.print("Enter the Product ID: ");
            String productID = scanner.nextLine();

            if (controller.checkProductExists(productID)) {
                validProduct = true;
                System.out.println("Starting the CSV Files generation...");
                controller.executeBoo(productID);
                controller.executeItems();
                controller.executeOperation();
                System.out.println("Generation completed. Check the CSV files.");
            } else {
                System.out.println("Product ID not found. Please enter a valid Product ID.");
            }
        }
    }

}
