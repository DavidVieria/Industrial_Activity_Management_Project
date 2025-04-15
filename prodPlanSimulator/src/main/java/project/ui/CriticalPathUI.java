package project.ui;

import project.controller.CriticalPathController;
import project.controller.ProductionTreeController;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;

import java.util.List;

public class CriticalPathUI implements Runnable {
    private CriticalPathController criticalPathController;
    private final ProductionTreeController productionTreeController;

    public CriticalPathUI(ProductionTreeController productionTreeController) {
        this.productionTreeController = productionTreeController;
        this.criticalPathController = new CriticalPathController(productionTreeController.getTree());
    }

    @Override
    public void run() {

        List<List<NaryTreeNode<ProductionElement>>> criticalPaths = criticalPathController.generateCriticalPaths();

        System.out.println();
        int size = criticalPaths.size();
        if (size == 1) {
            System.out.println("There is only 1 critical path!");
        } else {
            System.out.printf("There are %d critical paths!\n", size);
        }

        for (List<NaryTreeNode<ProductionElement>> path : criticalPaths) {
            System.out.print("Critical Path: ");
            for (NaryTreeNode<ProductionElement> node : path) {
                System.out.print(node.getElement().getOperation().getName() + " -> ");
            }
            System.out.println("END");
        }
    }
}