/**
 * This class provides functionalities for tracking and managing material quantities in a production system.
 */
package project.domain.service;

import project.domain.genericDataStructures.BST;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.ProductionElement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaterialQuantitiesSearcher {

    private BST<MaterialQuantityGroup> materialsBST;
    private Map<Double, List<Item>> quantityMaterialMap;
    private Map<Item, Double> materialsMap;

    /**
     * Constructs a MaterialQuantitiesSearcher with initialized data structures.
     */
    public MaterialQuantitiesSearcher() {
        this.materialsBST = new BST<>();
        this.quantityMaterialMap = new HashMap<>();
        this.materialsMap = new LinkedHashMap<>();
    }

    /**
     * Tracks and organizes material quantities into a binary search tree.
     *
     * @param tree            A {@link NaryTree} of {@link ProductionElement} representing the production hierarchy.
     * @param increasingOrder A boolean indicating if the materials should be ordered in ascending order.
     * @return A binary search tree of {@link MaterialQuantityGroup}, sorted based on the material quantities.
     */
    public BST<MaterialQuantityGroup> trackingMaterialQuantities(NaryTree<ProductionElement> tree, boolean increasingOrder) {
        NaryTreeNode<ProductionElement> node = tree.getRoot();
        trackingMaterialQuantitiesRecursive(node);

        for (Map.Entry<Double, List<Item>> entry : quantityMaterialMap.entrySet()) {
            MaterialQuantityGroup materialWithSameQuantity = new MaterialQuantityGroup(entry.getValue(), entry.getKey(), increasingOrder);
            materialsBST.insert(materialWithSameQuantity);
        }

        return materialsBST;
    }

    /**
     * Recursively tracks material quantities in the production hierarchy and stores them in a map.
     *
     * @param node The current node in the {@link NaryTree} being traversed.
     */
    private void trackingMaterialQuantitiesRecursive(NaryTreeNode<ProductionElement> node) {
        Item item = node.getElement().getItem();
        double quantity = node.getElement().getQuantity();

        quantityMaterialMap.computeIfAbsent(quantity, k -> new ArrayList<>()).add(item);

        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            trackingMaterialQuantitiesRecursive(child);
        }
    }

    /**
     * Calculates and retrieves a map of materials and their total quantities.
     *
     * @param tree A {@link NaryTree} of {@link ProductionElement} representing the production hierarchy.
     * @return A map where the key is an {@link Item} and the value is the total quantity of that item.
     */
    public Map<Item, Double> materialsAndQuantity(NaryTree<ProductionElement> tree) {
        NaryTreeNode<ProductionElement> node = tree.getRoot();

        if (node != null) {
            materialsAndQuantityRecursive(node);
        }

        return materialsMap;
    }

    /**
     * Recursively calculates material quantities and accumulates them in a map.
     *
     * @param node The current node in the {@link NaryTree} being traversed.
     */
    private void materialsAndQuantityRecursive(NaryTreeNode<ProductionElement> node) {
        Item item = node.getElement().getItem();
        double quantity = node.getElement().getQuantity();

        // Accumulate quantity in the map
        materialsMap.merge(item, quantity, Double::sum);

        // Recursion for the children
        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            materialsAndQuantityRecursive(child);
        }
    }
}
