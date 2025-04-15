package project.controller;

import project.domain.genericDataStructures.NaryTree;
import project.domain.model.Item;
import project.domain.model.ProductionElement;
import project.domain.service.MaterialQuantitiesSearcher;
import java.util.Map;

public class CalculateMaterialsQuantityController {

    private NaryTree<ProductionElement> tree;

    public CalculateMaterialsQuantityController(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public Map<Item, Double> materialsAndQuantity() {
        MaterialQuantitiesSearcher object = new MaterialQuantitiesSearcher();
        return object.materialsAndQuantity(tree);
    }

}
