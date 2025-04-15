/**
 * Represents the details of a node, including its type, associated quantity,
 * and its parent operation in a production or processing context.
 */
package project.domain.service;

import project.domain.enums.NodeType;
import project.domain.model.Operation;

public class NodeDetails {

    private NodeType type;
    private double quantity;
    private Operation parentOperation;

    /**
     * Constructs a NodeDetails instance with the specified type, quantity, and parent operation.
     *
     * @param type            The type of the node, represented by {@link NodeType}.
     * @param quantity        The quantity associated with the node.
     * @param parentOperation The parent {@link Operation} associated with the node.
     */
    public NodeDetails(NodeType type, double quantity, Operation parentOperation) {
        this.type = type;
        this.quantity = quantity;
        this.parentOperation = parentOperation;
    }

    /**
     * Constructs a NodeDetails instance with the specified type and parent operation,
     * defaulting the quantity to 0.
     *
     * @param type            The type of the node, represented by {@link NodeType}.
     * @param parentOperation The parent {@link Operation} associated with the node.
     */
    public NodeDetails(NodeType type, Operation parentOperation) {
        this.type = type;
        this.quantity = 0;
        this.parentOperation = parentOperation;
    }

    /**
     * Gets the type of the node.
     *
     * @return The type of the node as a {@link NodeType}.
     */
    public NodeType getType() {
        return type;
    }

    /**
     * Gets the quantity associated with the node.
     *
     * @return The quantity associated with the node.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Gets the parent operation of the node.
     *
     * @return The parent {@link Operation}, or {@code null} if none is set.
     */
    public Operation getParentOperation() {
        return parentOperation;
    }

    /**
     * Returns a string representation of the NodeDetails instance.
     * Includes the type, quantity (if greater than 0), and parent operation name (if not null).
     *
     * @return A string representation of the NodeDetails.
     */
    @Override
    public String toString() {
        return "NodeDetails{" +
                "type=" + type +
                (quantity != 0 ? ", quantity=" + quantity : "") +
                ", parentOperation=" + (parentOperation != null ? parentOperation.getName() : "None") +
                '}';
    }
}
