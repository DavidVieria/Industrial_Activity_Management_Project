package project.repository;

import project.domain.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private List<Item> items;

    public ItemRepository(List<Item> items) {
        this.items = items;
    }

    public ItemRepository() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addItems(List<Item> items) {
        this.items.addAll(items);
    }

    /**
     * Retrieves an item by its ID.
     *
     * @param itemId the ID of the item to retrieve
     * @return the item with the specified ID, or null if not found
     */
    public Item getItemById(String itemId) {
        for (Item item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves all items in the repository.
     *
     * @return a list of all items
     */
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Clears all items from the repository.
     */
    public void clearItems() {
        items.clear();
    }

    /**
     * Checks if the repository is empty.
     *
     * @return true if there are no items in the repository, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets the count of items in the repository.
     *
     * @return the number of items in the repository
     */
    public int getItemCount() {
        return items.size();
    }
}
