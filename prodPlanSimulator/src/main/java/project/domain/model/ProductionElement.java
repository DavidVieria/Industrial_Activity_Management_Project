/**
 * Represents a production element in a manufacturing or production system.
 * A production element associates an item with an operation, a quantity,
 * and an optional hierarchical structure represented by a tree of production elements.
 */
package project.domain.model;

import project.domain.enums.ItemType;
import project.domain.genericDataStructures.NaryTree;

public class ProductionElement {

    private Item item;
    private Operation operation;
    private double quantity;
    private int itemOpRelation;
    private NaryTree<ProductionElement> productionTree;

    /**
     * Constructs a ProductionElement with an item, operation, quantity, and item-operation relation.
     *
     * @param item           The item associated with the production element.
     * @param operation      The operation associated with the production element.
     * @param quantity       The quantity of the item.
     * @param itemOpRelation The relation between the item and the operation.
     */
    public ProductionElement(Item item, Operation operation, double quantity, int itemOpRelation) {
        this.item = item;
        this.operation = operation;
        this.quantity = quantity;
        this.itemOpRelation = itemOpRelation;
        this.productionTree = null;
    }

    /**
     * Constructs a ProductionElement with an item and a specified quantity.
     *
     * @param item     The item associated with the production element.
     * @param quantity The quantity of the item.
     */
    public ProductionElement(Item item, double quantity) {
        this.item = item;
        this.operation = null;
        this.quantity = quantity;
        this.itemOpRelation = 0;
        this.productionTree = null;
    }

    /**
     * Constructs a ProductionElement with an item, operation, and item-operation relation,
     * assuming a default quantity of 1.
     *
     * @param item           The item associated with the production element.
     * @param operation      The operation associated with the production element.
     * @param itemOpRelation The relation between the item and the operation.
     */
    public ProductionElement(Item item, Operation operation, int itemOpRelation) {
        this.item = item;
        this.operation = operation;
        this.quantity = 1;
        this.itemOpRelation = itemOpRelation;
        this.productionTree = null;
    }

    /**
     * Gets the item associated with this production element.
     *
     * @return The item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the item associated with this production element.
     *
     * @param item The item to set.
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets the operation associated with this production element.
     *
     * @return The operation.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Sets the operation associated with this production element.
     *
     * @param operation The operation to set.
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Gets the quantity of the item in this production element.
     *
     * @return The quantity.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the item in this production element.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the relation between the item and the operation.
     *
     * @return The item-operation relation.
     */
    public int getItemOpRelation() {
        return itemOpRelation;
    }

    /**
     * Sets the relation between the item and the operation.
     *
     * @param itemOpRelation The item-operation relation to set.
     */
    public void setItemOpRelation(int itemOpRelation) {
        this.itemOpRelation = itemOpRelation;
    }

    /**
     * Gets the production tree associated with this production element.
     *
     * @return The production tree.
     */
    public NaryTree<ProductionElement> getProductionTree() {
        return productionTree;
    }

    /**
     * Sets the production tree associated with this production element.
     * Setting the tree automatically sets the item's type to COMPONENT.
     *
     * @param productionTree The production tree to set.
     */
    public void setProductionTree(NaryTree<ProductionElement> productionTree) {
        item.setItemType(ItemType.COMPONENT);
        this.productionTree = productionTree;
    }

    /**
     * Returns a string representation of the production element.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "ProductionElement{" +
                "item=" + item +
                ", operation=" + operation +
                ", quantity=" + quantity +
                ", itemOpRelation=" + itemOpRelation +
                ", productionTree=" + productionTree +
                '}';
    }
}
