/**
 * Utility class providing methods for common operations on data structures.
 */
package project.domain.genericDataStructures;

import java.util.List;

public class Utils {

    /**
     * Sorts a list of elements using a Binary Search Tree (BST).
     * The elements are inserted into a BST and then retrieved in sorted order using an in-order traversal.
     *
     * @param <E>         The type of elements in the list, which must implement {@code Comparable}.
     * @param listUnsorted The unsorted list of elements to be sorted.
     * @return An {@code Iterable} containing the elements in sorted order.
     */
    public static <E extends Comparable<E>> Iterable<E> sortByBST(List<E> listUnsorted) {
        BST<E> bstFromList = new BST<>();
        for (E item : listUnsorted) {
            bstFromList.insert(item);
        }
        return bstFromList.inOrder();
    }
}
