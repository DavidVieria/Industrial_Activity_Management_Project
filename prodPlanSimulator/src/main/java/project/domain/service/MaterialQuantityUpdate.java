package project.domain.service;

import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;

/**
 * A service class for managing and updating material quantities in a hierarchical structure
 * represented by a N-ary tree of ProductionElement nodes.
 */
public class MaterialQuantityUpdate {

    private NaryTreeNode<ProductionElement> node;

    /**
     * Constructs a MaterialQuantity instance with the specified root node.
     *
     * @param node the root node of the N-ary tree containing ProductionElement instances.
     */
    public MaterialQuantityUpdate(NaryTreeNode<ProductionElement> node) {
        this.node = node;
    }

    /**
     * Updates the material quantities for the current node and all its descendants
     * proportionally, based on the provided new quantity.
     *
     * @param newQuantity the new quantity to set for the root node. The change is applied
     *                    proportionally to all descendant nodes.
     */
    public void updateMaterialQuantities(double newQuantity) {
        double oldQuantity = node.getElement().getQuantity();
        double change = newQuantity / oldQuantity;
        cascadingUpdates(node, change);
    }

    /**
     * Recursively updates the material quantities for the given node and its children,
     * applying the specified proportional change.
     *
     * @param node   the current node to update.
     * @param change the proportional change to apply to the quantity.
     */
    private void cascadingUpdates(NaryTreeNode<ProductionElement> node, double change) {
        double currentQuantity = node.getElement().getQuantity();
        node.getElement().setQuantity(currentQuantity * change);

        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            cascadingUpdates(child, change);
        }
    }

    /**
     * Gets the root node of this MaterialQuantity instance.
     *
     * @return the root node of the N-ary tree.
     */
    public NaryTreeNode<ProductionElement> getNode() {
        return node;
    }
}
