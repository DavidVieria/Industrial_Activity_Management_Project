/**
 * Enum representing the types of nodes in a production or process tree structure.
 * A node can represent either an {@code OPERATION} or a {@code MATERIAL}.
 */
package project.domain.enums;

public enum NodeType {
    /**
     * Represents a node that corresponds to an operation in the production process.
     */
    OPERATION,

    /**
     * Represents a node that corresponds to a material in the production process.
     */
    MATERIAL;

    /**
     * Provides a user-friendly string representation of the node type.
     *
     * @return A string describing the node type, such as "Operation" or "Material".
     */
    @Override
    public String toString() {
        switch (this) {
            case OPERATION:
                return "Operation";
            case MATERIAL:
                return "Material";
            default:
                return "Unknown type";
        }
    }
}
