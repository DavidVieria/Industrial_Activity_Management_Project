package project.ui;

import project.controller.BD.GenerateCSVFilesController;
import project.controller.DataLoaderController;
import project.controller.ExportProductionSimulatorController;
import project.domain.genericDataStructures.NaryTree;
import project.domain.model.*;
import project.io.DataLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ExportProductionSimulatorUI implements Runnable {

    private final ExportProductionSimulatorController exportController;
    private final DataLoaderController dataLoaderController;
    private final GenerateCSVFilesController csvController;

    public ExportProductionSimulatorUI(DataLoaderController dataLoaderController) {
        this.dataLoaderController = dataLoaderController;
        this.exportController = new ExportProductionSimulatorController();
        this.csvController = new GenerateCSVFilesController();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce the Product ID: ");
        String itemID = sc.nextLine();

            dataLoaderController.getItemRepository().clearItems();
            dataLoaderController.getOperationRepository().clearOperations();
            dataLoaderController.getProductionTreeRepository().clearTrees();
            dataLoaderController.getArticleRepository().clearArticles();
            dataLoaderController.getWorkStationRepository().clearWorkStations();

            try {
                System.out.println("Starting the import of workstation operating times and processing orders..");
                csvController.executeWorkstation();
                csvController.executeOperation();
                csvController.executeItems();
            } catch (Exception e) {
                System.out.println("Error importing workstation operating times: " + e.getMessage());
                return;
            }

            try {
                List<WorkStation> workStations = DataLoader.loadWorkStations("files\\workstationsBD.csv");
                List<Item> items = DataLoader.loadItems("files\\itemsBD.csv");
                List<Operation> operations = DataLoader.loadOperations("files\\operationsBD.csv");

                if (workStations.isEmpty() || items.isEmpty() || operations.isEmpty()) {
                    System.out.println("Error importing workstation operating times");
                    return;
                } else {
                    dataLoaderController.getWorkStationRepository().addWorkStations(workStations);
                    dataLoaderController.getOperationRepository().addOperations(operations);
                    dataLoaderController.getItemRepository().addItems(items);
                }
            } catch (Exception e) {
                System.out.println("Error importing workstation operating times");
                return;
            }

            boolean validProduct = false;
            for (Item item : dataLoaderController.getItemRepository().getAllItems()) {
                if (item.getId().equals(itemID)) {
                    validProduct = true;
                }
            }

            if (!validProduct) {
                System.out.println("Invalid Product ID");
                return;
            }

            try {
                csvController.executeBoo(itemID);
            } catch (Exception e) {
                System.out.println("Error when importing BOO from order items: " + e.getMessage());
                return;
            }

            List<NaryTree<ProductionElement>> trees = DataLoader.loadProductionTree("files\\booBD.csv", dataLoaderController.getItemRepository(), dataLoaderController.getOperationRepository());
            if (trees.isEmpty()) {
                System.err.println("Error when importing BOO from order items.");
                return;
            }

            dataLoaderController.getProductionTreeRepository().addProductionTrees(trees);

        try {
            exportController.function(itemID);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println();
        simulate();
        System.out.println();
        maps();
        System.out.println();
        System.out.println("=== File 'instructionsBD.txt' generated successfully. ===");

    }

    private void simulate() {
        System.out.println("=== Simulator Execution ===\n");
        System.out.printf("%-15s | %-15s | %-15s | %-15s%n", "Item", "WorkStation", "Start Time (s)", "End Time (s)");
        System.out.println("-----------------------------------------------------------------------");
        for (Event event : exportController.getEvents()) {
            System.out.printf("%-15s | %-15s | %-15s | %-15s%n",
                    event.getArticle().getIdArticle(),
                    event.getWorkStation().getWorkStationID(),
                    event.getStartTime() + "s",
                    event.getEndTime() + "s"
            );
        }
        System.out.printf("\nNumber of Events: %d\n", exportController.getEvents().size());
        System.out.println("Simulation completed.");
    }

    private void maps() {
        System.out.println("=== Adapting ids to numbers ===\n");
        System.out.printf("%-20s | %-10s%n", "Workstation ID", "Number");
        System.out.println("-----------------------------------------");
        for (Map.Entry<String, Integer> entry : exportController.getWorkstationsNumbers().entrySet()) {
            System.out.printf("%-20s | %-10d%n", entry.getKey(), entry.getValue());
        }

        System.out.println();
        System.out.printf("%-20s | %-10s%n", "Operation ID", "Number");
        System.out.println("-----------------------------------------");
        for (Map.Entry<String, Integer> entry : exportController.getOperationsNumbers().entrySet()) {
            System.out.printf("%-20s | %-10d%n", entry.getKey(), entry.getValue());
        }
    }
}
