/**
 * Represents a group of materials with the same quantity and provides comparison based on quantity.
 */
package project.domain.service;

import project.domain.model.Item;

import java.util.List;

public class MaterialQuantityGroup implements Comparable<MaterialQuantityGroup> {

    private List<Item> materialList;
    private double materialQuantity;
    private boolean increasingOrder;

    /**
     * Constructs a MaterialQuantityGroup with the specified list of materials, their quantity, and sorting order.
     *
     * @param material        The list of {@link Item} objects representing the materials in this group.
     * @param materialQuantity The quantity associated with the materials in this group.
     * @param increasingOrder  A boolean indicating if the sorting should be in ascending order.
     */
    public MaterialQuantityGroup(List<Item> material, double materialQuantity, boolean increasingOrder) {
        this.materialList = material;
        this.materialQuantity = materialQuantity;
        this.increasingOrder = increasingOrder;
    }

    /**
     * Gets the list of materials in this group.
     *
     * @return A {@link List} of {@link Item} objects.
     */
    public List<Item> getMaterialList() {
        return materialList;
    }

    /**
     * Sets the list of materials in this group.
     *
     * @param materialList A {@link List} of {@link Item} objects to be set.
     */
    public void setMaterialList(List<Item> materialList) {
        this.materialList = materialList;
    }

    /**
     * Gets the quantity of materials in this group.
     *
     * @return The material quantity as a double.
     */
    public double getMaterialQuantity() {
        return materialQuantity;
    }

    /**
     * Sets the quantity of materials in this group.
     *
     * @param materialQuantity The material quantity as a double.
     */
    public void setMaterialQuantity(double materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    /**
     * Provides a string representation of this group.
     *
     * @return A string containing the material list and their quantity.
     */
    @Override
    public String toString() {
        return "MaterialQuantityGroup{" +
                "material=" + materialList +
                ", materialQuantity=" + materialQuantity +
                '}';
    }

    /**
     * Compares this MaterialQuantityGroup with another based on their material quantities.
     *
     * @param other The other {@link MaterialQuantityGroup} to compare against.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, 
     *         or greater than the specified object, depending on the sorting order.
     */
    @Override
    public int compareTo(MaterialQuantityGroup other) {
        int comparison = Double.compare(this.materialQuantity, other.materialQuantity);
        return increasingOrder ? comparison : -comparison;
    }
}
