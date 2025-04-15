/**
 * Represents an item used in a production or inventory system.
 * An item has an identifier, a name, and a type that categorizes it.
 */
package project.domain.model;

import project.domain.enums.ItemType;

public class Item {

    private String id;
    private String name;
    private ItemType itemType;

    /**
     * Constructs an Item with the specified ID and name.
     * The default item type is set to MATERIAL.
     *
     * @param id   The unique identifier for the item.
     * @param name The name of the item.
     */
    public Item(String id, String name) {
        this.id = id;
        this.name = name;
        this.itemType = ItemType.MATERIAL;
    }

    /**
     * Gets the unique identifier of the item.
     *
     * @return The item's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the item.
     *
     * @return The item's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the item.
     *
     * @return The item's type.
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Sets the type of the item.
     *
     * @param itemType The item type to set.
     */
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * Returns a string representation of the item, including its ID and name.
     *
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
        return "Item: {" +
                "id ='" + id + '\'' +
                ", name ='" + name + '\'' +
                '}';
    }
}
