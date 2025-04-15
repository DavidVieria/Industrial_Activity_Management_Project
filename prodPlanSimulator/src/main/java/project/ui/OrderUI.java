package project.ui;

import project.controller.BD.GenerateCSVFilesController;
import project.controller.DataLoaderController;
import project.controller.OrderController;
import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.genericDataStructures.Node;
import project.domain.model.*;
import project.domain.service.OperationHierarchy;
import project.io.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderUI implements Runnable {

    private final OrderController orderController;
    private final DataLoaderController dataLoaderController;
    private final GenerateCSVFilesController csvController;

    public OrderUI(DataLoaderController dataLoaderController) {
        this.dataLoaderController = dataLoaderController;
        this.orderController = new OrderController();
        this.csvController = new GenerateCSVFilesController();

    }

    public void run() {
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

        dataLoaderController.readOrdersFiles();
        if (!dataLoaderController.isOrdersDataLoadedSuccessfully()) {
            System.err.println("Failed to load orders file. Exiting.");
            return;
        }

        List<String> productsID = new ArrayList<>();
        for (Order order : dataLoaderController.getOrderRepository().getAllOrders()) {
            productsID.add(order.getItem().getId());
        }
        try {
            csvController.executeMultiplesBoo(productsID);
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

        orderController.processOrders();
        tree();
        System.out.println();
        avl();
        System.out.println();
        simulate();
    }

    public void tree() {
        Map<Order, NaryTree<ProductionElement>> map = orderController.getOrderTreeMap();

        for (Map.Entry<Order, NaryTree<ProductionElement>> entry : map.entrySet()) {
            Order order = entry.getKey();
            System.out.printf("\n=== Order ID %s | %s | %s | %.0f ===%n%n", order.getOrderID(), order.getItem().getId(), order.getPriority(), order.getQuantity());
            System.out.printf("Tree: %s%n", entry.getValue().getRoot().getElement().getItem().getId());
            printTreeRecursive(entry.getValue().getRoot());
        }
        System.out.println();
    }

    private void printTreeRecursive(NaryTreeNode<ProductionElement> node) {
        if (node == null) return;

        boolean currentLine1 = false;

        String operationID = "";
        String operationName = "";
        String quantity = "";
        String itemName = "";
        String itemOpRelation = "";

        if (node.getElement() != null) {
            if (node.getElement().getOperation() != null) {
                operationID = node.getElement().getOperation().getId();
                operationName = node.getElement().getOperation().getName();
                currentLine1 = true;
            }
            if (node.getElement().getQuantity() != 0) {
                quantity = String.valueOf(node.getElement().getQuantity());
            }
            if (node.getElement().getItem() != null) {
                itemName = node.getElement().getItem().getName();
            }
            if (node.getElement().getItemOpRelation() != 0) {
                itemOpRelation = String.valueOf(node.getElement().getItemOpRelation());
                currentLine1 = true;
            }
        }

        String prefix = "|".concat("_".repeat(node.getLevel() * 2 + 1));
        String line1 = String.format("%s %sx: %n%s__ %s (Component) - %sx - %s %s (Operation)",
                prefix,
                quantity,
                prefix,
                itemName,
                itemOpRelation,
                operationID,
                operationName);
        String line2 = String.format("%s %sx: %n%s__ %s (Material)",
                prefix,
                quantity,
                prefix,
                itemName);
        if (currentLine1) {
            System.out.println(line1);
        } else {
            System.out.println(line2);
        }

        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            printTreeRecursive(child);
        }
    }

    public void avl() {
        Map<Order, AVL<OperationHierarchy>> map = orderController.getOrderAVLMap();

        for (Map.Entry<Order, AVL<OperationHierarchy>> entry : map.entrySet()) {
            System.out.printf("=== Product operations sequence: %s ===%n", entry.getKey().getItem().getId());
            printAvl(entry.getValue().getRoot());
            System.out.println();
        }
    }

    private void printAvl(Node<OperationHierarchy> node) {
        if (node != null) {
            printAvl(node.getLeft());

            System.out.println("Upcoming operations: ");
            for (Operation operation : node.getElement().getOperationsSameHierarchy()) {
                System.out.println("- " + operation.getId() + " " + operation.getName());
            }

            printAvl(node.getRight());
        }
    }

    private void simulate() {
        System.out.println("=== Simulator Execution ===\n");
        System.out.printf("%-15s | %-15s | %-15s | %-15s%n", "Item", "WorkStation", "Start Time (s)", "End Time (s)");
        System.out.println("-----------------------------------------------------------------------");
        for (Event event : orderController.getEvents()) {
            System.out.printf("%-15s | %-15s | %-15s | %-15s%n",
                    event.getArticle().getIdArticle(),
                    event.getWorkStation().getWorkStationID(),
                    event.getStartTime() + "s",
                    event.getEndTime() + "s"
            );
        }
        System.out.printf("\nNumber of Events: %d\n", orderController.getEvents().size());
        System.out.println("Simulation completed.");
    }

}
