package project.controller;

import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;
import project.domain.service.QualityCheck;

public class QualityCheckController {

    private NaryTree<ProductionElement> tree;

    public QualityCheckController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> generatePriorityQueue() {
        QualityCheck qualityCheck = new QualityCheck(tree);
        qualityCheck.inicializeRoot();

        return qualityCheck.getQualityCheckQueue();
    }
}