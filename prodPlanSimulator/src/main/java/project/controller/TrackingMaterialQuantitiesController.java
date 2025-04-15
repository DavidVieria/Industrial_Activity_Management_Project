package project.controller;

import project.domain.genericDataStructures.BST;
import project.domain.genericDataStructures.NaryTree;
import project.domain.model.ProductionElement;
import project.domain.service.MaterialQuantityGroup;
import project.domain.service.MaterialQuantitiesSearcher;

public class TrackingMaterialQuantitiesController {

    private NaryTree<ProductionElement> tree;

    public TrackingMaterialQuantitiesController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public BST<MaterialQuantityGroup> trackingMaterialQuantities(boolean increasingOrder) {
        MaterialQuantitiesSearcher object = new MaterialQuantitiesSearcher();
        return object.trackingMaterialQuantities(tree, increasingOrder);
    }

}