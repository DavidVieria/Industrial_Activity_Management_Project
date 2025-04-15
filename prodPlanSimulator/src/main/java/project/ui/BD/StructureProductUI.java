package project.ui.BD;

import project.controller.BD.GenerateCSVFilesController;
import project.controller.BD.StructureBOMController;
import project.controller.BD.StructureBOOController;

import java.io.IOException;
import java.util.Scanner;

public class StructureProductUI implements Runnable {

    private final StructureBOMController controllerBOM;
    private final StructureBOOController controllerBOO;
    private final GenerateCSVFilesController dataController;

    public StructureProductUI() {
        controllerBOM = new StructureBOMController();
        controllerBOO = new StructureBOOController();
        dataController = new GenerateCSVFilesController();
    }

    public void run() {

        System.out.println("\n\n=== BOM & BOO ===");
        Scanner scanner = new Scanner(System.in);
        boolean validProduct = false;

        while (!validProduct) {
            System.out.print("Enter the Product ID: ");
            String productID = scanner.nextLine();

            if (dataController.checkProductExists(productID)) {
                validProduct = true;
                System.out.println("Starting the BOM/BOO chart generation...");
                dataController.executeBoo(productID);
                dataController.executeItems();
                dataController.executeOperation();
                try {
                    controllerBOM.execute();
                    controllerBOO.execute();
                    System.out.println("Generation completed. Check the PNG files.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Product ID not found. Please enter a valid Product ID.");
            }
        }
    }

}
