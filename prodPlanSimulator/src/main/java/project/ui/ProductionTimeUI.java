package project.ui;

import project.controller.BD.GenerateCSVFilesController;
import project.controller.DataLoaderController;
import project.controller.ProductionTimeController;
import project.domain.genericDataStructures.NaryTree;
import project.domain.model.*;
import project.io.DataLoader;
import java.util.List;
import java.util.Scanner;

public class ProductionTimeUI implements Runnable{

    private final ProductionTimeController productionTimeController;
    private final DataLoaderController dataLoaderController;
    private final GenerateCSVFilesController csvController;

    public ProductionTimeUI(DataLoaderController dataLoaderController) {
        this.dataLoaderController = dataLoaderController;
        this.productionTimeController = new ProductionTimeController();
        this.csvController = new GenerateCSVFilesController();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce the Product ID: ");
        String itemID = sc.nextLine();

        while (!itemID.equalsIgnoreCase("leave")) {

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

            productionTimeController.function(itemID);
            System.out.println();
            simulate();

            System.out.printf("\nProduct ID: %s\nAverage Production Time: %d", itemID, productionTimeController.getTotalProductionTime());

            System.out.println("\nIntroduce another Product ID or 'leave' to end.");
            itemID = sc.nextLine();
        }
    }

    private void simulate() {
        System.out.println("=== Simulator Execution ===\n");
        System.out.printf("%-15s | %-15s | %-15s | %-15s%n", "Item", "WorkStation", "Start Time (s)", "End Time (s)");
        System.out.println("-----------------------------------------------------------------------");
        for (Event event : productionTimeController.getEvents()) {
            System.out.printf("%-15s | %-15s | %-15s | %-15s%n",
                    event.getArticle().getIdArticle(),
                    event.getWorkStation().getWorkStationID(),
                    event.getStartTime() + "s",
                    event.getEndTime() + "s"
            );
        }
        System.out.printf("\nNumber of Events: %d\n", productionTimeController.getEvents().size());
        System.out.println("Simulation completed.");
    }
}
