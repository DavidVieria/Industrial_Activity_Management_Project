/**
 * Represents a node in an n-ary tree structure.
 * Each node can store a generic element, have a reference to its parent,
 * maintain a list of children, and track its level in the tree.
 *
 * @param <E> The type of element stored in the node.
 */
package project.domain.genericDataStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NaryTreeNode<E> {

    private E element;
    private NaryTreeNode<E> parent;
    private List<NaryTreeNode<E>> children;
    int level;

    /**
     * Constructs a new NaryTreeNode with the specified element and parent.
     * The node's level is determined based on its parent's level.
     *
     * @param element The element to store in the node.
     * @param parent  The parent node. If null, this node is the root.
     */
    public NaryTreeNode(E element, NaryTreeNode<E> parent) {
        this.element = element;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.level = (parent == null) ? 0 : parent.level + 1;
    }

    /**
     * Gets the element stored in the node.
     *
     * @return The element stored in the node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Gets the parent node of this node.
     *
     * @return The parent node, or null if this node is the root.
     */
    public NaryTreeNode<E> getParent() {
        return parent;
    }

    /**
     * Gets the list of children nodes of this node.
     *
     * @return A list of children nodes.
     */
    public List<NaryTreeNode<E>> getChildren() {
        return children;
    }

    /**
     * Adds a child node to this node with the specified element.
     *
     * @param childData The element to store in the new child node.
     * @return The newly created child node.
     */
    public NaryTreeNode<E> addChild(E childData) {
        NaryTreeNode<E> child = new NaryTreeNode<>(childData, this);
        this.children.add(child);
        return child;
    }

    /**
     * Sets the element stored in this node.
     *
     * @param element The new element to store in the node.
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Sets the parent node of this node.
     *
     * @param parent The new parent node.
     */
    public void setParent(NaryTreeNode<E> parent) {
        this.parent = parent;
    }

    /**
     * Gets the level of this node in the tree.
     * The root node is at level 0.
     *
     * @return The level of the node.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of this node in the tree.
     *
     * @param level The new level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Checks if this node is equal to another object.
     * Two nodes are considered equal if their elements, parents, children, and levels are equal.
     *
     * @param o The object to compare with.
     * @return True if the nodes are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaryTreeNode<?> that = (NaryTreeNode<?>) o;
        return level == that.level && Objects.equals(element, that.element) &&
                Objects.equals(parent, that.parent) && Objects.equals(children, that.children);
    }

    /**
     * Computes the hash code of the node based on its element, parent, children, and level.
     *
     * @return The hash code of the node.
     */
    @Override
    public int hashCode() {
        return Objects.hash(element, parent, children, level);
    }

    /**
     * Returns a string representation of the node, including its element, parent, and children.
     *
     * @return A string representation of the node.
     */
    @Override
    public String toString() {
        return "ProductionNode{" +
                "element=" + element +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
