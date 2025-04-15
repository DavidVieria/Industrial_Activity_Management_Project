package project.controller;

import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;
import project.domain.genericDataStructures.NaryTree;
import project.domain.service.MaterialQuantityUpdate;
import project.domain.service.ProductionTreeSearcher;

public class MaterialQuantityUpdateController {

    private NaryTree<ProductionElement> tree;

    public MaterialQuantityUpdateController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public NaryTreeNode<ProductionElement> findNodeByKey(String key) {
        ProductionTreeSearcher object = new ProductionTreeSearcher();
        object.buildElementNodeMap(tree);
        return object.findNodeByKey(key);
    }

    public NaryTree<ProductionElement> updateMaterialQuantities(String key, double newQuantity) {
        MaterialQuantityUpdate materialQuantity = new MaterialQuantityUpdate(findNodeByKey(key));
        materialQuantity.updateMaterialQuantities(newQuantity);
        return tree;
    }
}