/**
 * Interface defining the operations of a Binary Search Tree (BST).
 *
 * @param <E> The type of elements stored in the BST. Must implement {@link Comparable}.
 */
package project.domain.genericDataStructures;

import java.util.List;
import java.util.Map;

public interface BSTInterface<E> {

    /**
     * Checks if the BST is empty.
     *
     * @return {@code true} if the BST is empty, otherwise {@code false}.
     */
    public boolean isEmpty();

    /**
     * Inserts an element into the BST.
     *
     * @param element The element to be inserted.
     */
    public void insert(E element);

    /**
     * Removes an element from the BST.
     *
     * @param element The element to be removed.
     */
    public void remove(E element);

    /**
     * Calculates the number of nodes in the BST.
     *
     * @return The size of the BST.
     */
    public int size();

    /**
     * Calculates the height of the BST.
     *
     * @return The height of the BST.
     */
    public int height();

    /**
     * Finds the smallest element in the BST.
     *
     * @return The smallest element in the BST, or {@code null} if the tree is empty.
     */
    public E smallestElement();

    /**
     * Performs an in-order traversal of the BST.
     *
     * @return An iterable collection of elements in in-order.
     */
    public Iterable<E> inOrder();

    /**
     * Performs a pre-order traversal of the BST.
     *
     * @return An iterable collection of elements in pre-order.
     */
    public Iterable<E> preOrder();

    /**
     * Performs a post-order traversal of the BST.
     *
     * @return An iterable collection of elements in post-order.
     */
    public Iterable<E> posOrder();

    /**
     * Groups nodes by their levels in the BST.
     *
     * @return A map where keys represent levels and values are lists of elements at each level.
     */
    public Map<Integer, List<E>> nodesByLevel();
}
