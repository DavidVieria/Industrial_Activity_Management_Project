package project.ui;

import project.controller.BooProductionSimulatorController;
import project.controller.ProductionTreeController;
import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.Node;
import project.domain.model.Operation;
import project.domain.service.OperationHierarchy;


public class BooProductionSimulatorUI implements Runnable {

    private final ProductionTreeController treeController;
    private final BooProductionSimulatorController booProductionSimulatorController;

    public BooProductionSimulatorUI(ProductionTreeController treeController) {
        this.treeController = treeController;
        this.booProductionSimulatorController = new BooProductionSimulatorController(treeController.getTree());
    }

    public void run() {
        printTree(booProductionSimulatorController.avlTree());
    }

    public void printTree(AVL<OperationHierarchy> avlTree) {
        Node<OperationHierarchy> root = avlTree.getRoot();
        if (root != null) {
            System.out.println();
            System.out.println("=== Simulator ===:");
            System.out.println();
            inOrderTraversal(root);
        } else {
            System.out.println("The tree is empty.");
        }
    }

    private void inOrderTraversal(Node<OperationHierarchy> node) {
        if (node != null) {
            inOrderTraversal(node.getLeft());

            System.out.println("New step of the simulator: ");
            for (Operation operation : node.getElement().getOperationsSameHierarchy()) {
                System.out.println("- " + operation.getName());
            }
            System.out.println();

            inOrderTraversal(node.getRight());
        }
    }

}
