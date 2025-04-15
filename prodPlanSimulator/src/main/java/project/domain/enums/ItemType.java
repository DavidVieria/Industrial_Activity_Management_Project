/**
 * Enum representing the type of an item in a production or material management context.
 * An item can be either a {@code COMPONENT} or a {@code MATERIAL}.
 */
package project.domain.enums;

public enum ItemType {
    /**
     * Represents an item that is a component, typically part of a larger assembly.
     */
    COMPONENT,

    /**
     * Represents an item that is a raw material used in production or manufacturing.
     */
    MATERIAL
}
