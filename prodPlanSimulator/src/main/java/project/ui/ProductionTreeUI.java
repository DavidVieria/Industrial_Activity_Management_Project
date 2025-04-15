package project.ui;

import project.controller.DataLoaderController;
import project.controller.ProductionTreeController;
import project.domain.model.ProductionElement;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.ui.menu.MenuItem;
import project.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductionTreeUI implements Runnable {
    private final ProductionTreeController productionTreeController;
    private final DataLoaderController dataLoaderController;
    private NaryTree<ProductionElement> tree;

    public ProductionTreeUI(DataLoaderController dataLoaderController) {
        this.productionTreeController = new ProductionTreeController(tree);
        this.dataLoaderController = dataLoaderController;
    }

    public void run() {
        dataLoaderController.readProductionTreeFiles();

        if (!dataLoaderController.isProductionTreeDataLoadedSuccessfully()) {
            System.err.println("Failed to load data files. Exiting.");
            return;
        }

        productionTree();

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Efficiently search for specification operations or materials", new ProductionTreeSearcherUI(productionTreeController)));
        options.add(new MenuItem("View all materials used in production, organized by quantity", new TrackingMaterialQuantitiesUI(productionTreeController)));
        options.add(new MenuItem("Prioritize Quality Checks", new QualityCheckUI(productionTreeController)));
        options.add(new MenuItem("Update material quantities in the production tree", new MaterialQuantityUpdateUI(productionTreeController)));
        options.add(new MenuItem("Calculate the total quantity for each Material", new CalculateMaterialsQuantityUI(productionTreeController)));
        options.add(new MenuItem("Identify and prioritize critical path operations", new CriticalPathUI(productionTreeController)));
        options.add(new MenuItem("Put the components into production in the simulator", new BooProductionSimulatorUI(productionTreeController)));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n=== Production Tree Menu ===");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    private void productionTree() {
        productionTreeController.mergeTrees();

        Scanner scanner = new Scanner(System.in);
        String finalProduct = null;
        NaryTree<ProductionElement> tree = null;

        while (tree == null) {
            System.out.print("Enter the Final Product ID or Name: ");
            finalProduct = scanner.nextLine();

            tree = productionTreeController.getTreeByProduct(finalProduct);

            if (tree == null) {
                System.out.println("Invalid Product ID/Name. Please try again.");
            }
        }

        productionTreeController.setTree(tree);

        System.out.println("\n=== Production Trees ===\n");
        System.out.printf("Tree: %s%n%n", tree.getRoot().getElement().getItem().getName());
        printTree(tree.getRoot());
        System.out.println();
    }

    private void printTree(NaryTreeNode<ProductionElement> root) {
        printTreeRecursive(root);
    }

    private void printTreeRecursive(NaryTreeNode<ProductionElement> node) {
        if (node == null) return;

        boolean currentLine1 = false;

        String operationName = "";
        String quantity = "";
        String itemName = "";
        String itemOpRelation = "";

        if (node.getElement() != null) {
            if (node.getElement().getOperation() != null) {
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
        String line1 = String.format("%s %sx: %n%s__ %s (Component) - %sx - %s (Operation)",
                prefix,
                quantity,
                prefix,
                itemName,
                itemOpRelation,
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


}