/**
 * Represents a hierarchy of operations with a specific depth level.
 * This class provides functionality to manage operations grouped by hierarchy and supports comparison based on depth.
 */
package project.domain.service;

import project.domain.model.Operation;
import project.domain.model.ProductionElement;

import java.util.List;

public class OperationHierarchy implements Comparable<OperationHierarchy> {

    private List<Operation> operationsSameHierarchy;
    private int depth;

    /**
     * Constructs an OperationHierarchy with a list of operations in the same hierarchy and their depth.
     *
     * @param operationsSameHierarchy A list of {@link Operation} objects belonging to the same hierarchy level.
     * @param depth                   The depth of the hierarchy as an integer.
     */
    public OperationHierarchy(List<Operation> operationsSameHierarchy, int depth) {
        this.operationsSameHierarchy = operationsSameHierarchy;
        this.depth = depth;
    }

    /**
     * Retrieves the list of operations in the same hierarchy.
     *
     * @return A {@link List} of {@link Operation} objects.
     */
    public List<Operation> getOperationsSameHierarchy() {
        return operationsSameHierarchy;
    }

    /**
     * Sets the list of operations in the same hierarchy.
     *
     * @param operationsSameHierarchy A {@link List} of {@link Operation} objects to be set.
     */
    public void setOperationsSameHierarchy(List<Operation> operationsSameHierarchy) {
        this.operationsSameHierarchy = operationsSameHierarchy;
    }

    /**
     * Gets the depth of the current hierarchy.
     *
     * @return The depth as an integer.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Sets the depth of the current hierarchy.
     *
     * @param depth The depth as an integer.
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Provides a string representation of this OperationHierarchy.
     *
     * @return A string containing the operations in the same hierarchy and their depth.
     */
    @Override
    public String toString() {
        return "OperationHierarchy{" +
                "operationsSameHierarchy=" + operationsSameHierarchy +
                ", depth=" + depth +
                '}';
    }

    /**
     * Compares this OperationHierarchy object with another based on their depth.
     * The comparison is in descending order (higher depth comes first).
     *
     * @param o The other {@link OperationHierarchy} to compare with.
     * @return A negative integer, zero, or a positive integer as this object's depth
     *         is greater than, equal to, or less than the specified object's depth.
     */
    @Override
    public int compareTo(OperationHierarchy o) {
        return Integer.compare(o.depth, this.depth);
    }
}
