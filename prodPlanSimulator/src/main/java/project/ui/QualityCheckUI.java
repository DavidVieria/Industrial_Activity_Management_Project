package project.ui;

import project.controller.ProductionTreeController;
import project.controller.QualityCheckController;
import project.domain.genericDataStructures.Entry;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;

public class QualityCheckUI implements Runnable {
    private QualityCheckController qualityCheckController;
    private final ProductionTreeController productionTreeController;

    public QualityCheckUI(ProductionTreeController productionTreeController) {
        this.productionTreeController = productionTreeController;
        qualityCheckController = new QualityCheckController(productionTreeController.getTree());
    }

    @Override
    public void run() {

        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> priorityQueue = qualityCheckController.generatePriorityQueue();

        if (priorityQueue == null || priorityQueue.isEmpty()) {
            System.out.println("Priority Queue is empty or not initialized.");
            return;
        }

        System.out.println("Quality Check Operations in Order of Priority:");

        while (!priorityQueue.isEmpty()) {

            Entry<NaryTreeNode<ProductionElement>, Integer> entry = priorityQueue.removeMin();
            NaryTreeNode<ProductionElement> node = entry.getKey();

            ProductionElement productionElement = node.getElement();
            Item item = productionElement.getItem();
            Operation operation = productionElement.getOperation();
            int level = entry.getValue();

            System.out.printf(
                    "Item : %-25s || Operation : %-20s || Depth : %-5d\n",
                    (item != null ? item.getName() : "No Item"),
                    (operation != null ? operation.getName() : "None"),
                    level
            );
        }
    }

}