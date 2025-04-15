package project.controller;

import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;
import project.domain.service.CriticalPath;

import java.util.List;

public class CriticalPathController {

    private NaryTree<ProductionElement> tree;

    public CriticalPathController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public List<List<NaryTreeNode<ProductionElement>>> generateCriticalPaths() {
        CriticalPath criticalPath = new CriticalPath(tree);
        return criticalPath.initializeTree();
    }

}