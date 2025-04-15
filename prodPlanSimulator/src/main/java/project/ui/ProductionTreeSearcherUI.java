package project.ui;

import project.controller.ProductionTreeController;
import project.controller.ProductionTreeSearcherController;
import project.domain.service.NodeDetails;

import java.util.Scanner;

public class ProductionTreeSearcherUI implements Runnable {

    private final ProductionTreeController treeController;
    private final ProductionTreeSearcherController searcherController;

    public ProductionTreeSearcherUI(ProductionTreeController treeController) {
        this.treeController = treeController;
        this.searcherController = new ProductionTreeSearcherController(treeController.getTree());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String key = null;

        while (key == null || key.trim().isEmpty()) {
            System.out.print("Enter name/id of an operation/material: ");
            key = scanner.nextLine().trim();
        }

        NodeDetails node = searcherController.findNodeByKey(key);
        if (node == null) {
            System.out.println("\nThere is nothing with the key " + key);
        } else {
            System.out.println("\nNode Details: ");
            System.out.println("- Type: " + node.getType());
            System.out.println("- Quantity: " + node.getQuantity());
            if (node.getParentOperation() != null) {
                System.out.println("- Parent Operation: " + node.getParentOperation().getName());
            }
        }

    }

}
