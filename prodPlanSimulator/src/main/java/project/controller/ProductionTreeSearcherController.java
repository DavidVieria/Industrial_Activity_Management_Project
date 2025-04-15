package project.controller;

import project.domain.model.ProductionElement;
import project.domain.genericDataStructures.NaryTree;
import project.domain.service.NodeDetails;
import project.domain.service.ProductionTreeSearcher;


public class ProductionTreeSearcherController {

    private NaryTree<ProductionElement> tree;

    public ProductionTreeSearcherController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public NodeDetails findNodeByKey(String key) {
        ProductionTreeSearcher object = new ProductionTreeSearcher();
        object.buildElementNodeMap(tree);
        return object.nodeDetails(key);
    }

}
