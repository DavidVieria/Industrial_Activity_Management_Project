package project.controller;

import project.domain.genericDataStructures.AVL;
import project.domain.model.ProductionElement;
import project.domain.genericDataStructures.NaryTree;
import project.domain.service.BooProductionSimulator;
import project.domain.service.OperationHierarchy;

public class BooProductionSimulatorController {

    private NaryTree<ProductionElement> tree;

    public BooProductionSimulatorController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public AVL<OperationHierarchy> avlTree() {
        BooProductionSimulator object = new BooProductionSimulator();
        object.initializeRoot(tree);
        return object.elementsByDepth();
    }

}
