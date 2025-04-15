package project.domain.genericDataStructures;

import java.util.Comparator;

/**
 * A generic comparator that compares two objects based on their depth (level) in descending order.
 * The depth is accessed using the {@code getLevel()} method of the object.
 *
 * @param <K> the type of objects that may be compared by this comparator
 */
public class ReversedComparator<K> implements Comparator<K> {

    /**
     * Compares two objects of type {@code K} based on their depth levels,
     * in descending order (greater depth comes first).
     * Assumes that the objects are instances of {@code NaryTreeNode} and that they
     * have a method {@code getLevel()} to retrieve their depth.
     *
     * @param a the first object to be compared
     * @param b the second object to be compared
     * @return a negative integer if the depth of {@code a} is greater than that of {@code b};
     *         a positive integer if the depth of {@code a} is less than that of {@code b};
     *         zero if their depths are equal
     * @throws IllegalArgumentException if either {@code a} or {@code b} is not an instance of {@code NaryTreeNode}
     */
    @Override
    public int compare(K a, K b) {
        // Ensure both objects are instances of NaryTreeNode
        if (a instanceof NaryTreeNode<?> && b instanceof NaryTreeNode<?>) {
            NaryTreeNode<?> nodeA = (NaryTreeNode<?>) a;
            NaryTreeNode<?> nodeB = (NaryTreeNode<?>) b;

            // Compare the depth levels in descending order
            return Integer.compare(nodeB.getLevel(), nodeA.getLevel());
        }

        // Throw an exception if the objects are not of the expected type
        throw new IllegalArgumentException("Objects compared must be instances of NaryTreeNode.");
    }
}
