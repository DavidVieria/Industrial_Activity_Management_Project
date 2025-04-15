package project.ui;

import project.controller.ProductionTreeController;
import project.controller.TrackingMaterialQuantitiesController;
import project.domain.genericDataStructures.BST;
import project.domain.genericDataStructures.Node;
import project.domain.model.Item;
import project.domain.service.MaterialQuantityGroup;

import java.util.Scanner;


public class TrackingMaterialQuantitiesUI implements Runnable  {
    private final TrackingMaterialQuantitiesController trackingController;
    private final ProductionTreeController treeController;

    public TrackingMaterialQuantitiesUI(ProductionTreeController treeController) {
        this.treeController = treeController;
        this.trackingController = new TrackingMaterialQuantitiesController(treeController.getTree());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (true) {
            System.out.println("Select the display order:");
            System.out.println("1 - Ascending");
            System.out.println("2 - Descending");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    break;
                } else {
                    System.out.println("Invalid option. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.next();
            }
        }

        boolean ascendingOrder = (choice == 1);
        BST<MaterialQuantityGroup> bst = trackingController.trackingMaterialQuantities(ascendingOrder);
        printTree(bst);
    }

    public void printTree(BST<MaterialQuantityGroup> bstTree) {
        Node<MaterialQuantityGroup> root = bstTree.getRoot();
        if (root != null) {
            System.out.println();
            inOrderTraversal(root);
        } else {
            System.out.println("The tree is empty.");
        }
    }

    private void inOrderTraversal(Node<MaterialQuantityGroup> node) {
        if (node != null) {
            inOrderTraversal(node.getLeft());

            double quantity = node.getElement().getMaterialQuantity();
            System.out.println("Materials with quantity " + quantity + ":");
            for (Item material : node.getElement().getMaterialList()) {
                System.out.println("- " + material.getName());
            }
            System.out.println();

            inOrderTraversal(node.getRight());
        }
    }

}
