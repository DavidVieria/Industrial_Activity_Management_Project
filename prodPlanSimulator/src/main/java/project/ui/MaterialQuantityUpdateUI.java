package project.ui;

import project.controller.MaterialQuantityUpdateController;
import project.controller.ProductionTreeController;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;

import java.util.Scanner;

public class MaterialQuantityUpdateUI implements Runnable {

    private final ProductionTreeController treeController;
    private final MaterialQuantityUpdateController materialQuantityController;

    public MaterialQuantityUpdateUI(ProductionTreeController treeController) {
        this.treeController = treeController;
        this.materialQuantityController = new MaterialQuantityUpdateController(treeController.getTree());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String key = null;
        double quantity = 0;

        while (key == null || key.trim().isEmpty()) {
            System.out.print("Enter name/id of an operation/material: ");
            key = scanner.nextLine().trim();
        }

        NaryTreeNode<ProductionElement> node = materialQuantityController.findNodeByKey(key);
        if (node == null) {
            System.out.println("\nThere is nothing with the key " + key);
        } else {
            while (quantity == 0) {
                System.out.print("Enter the new quantity for the material: ");
                quantity = Double.parseDouble(scanner.nextLine().trim());
            }
            NaryTree<ProductionElement> tree = materialQuantityController.updateMaterialQuantities(key, quantity);
            System.out.println();
            printTree(tree.getRoot());
        }
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