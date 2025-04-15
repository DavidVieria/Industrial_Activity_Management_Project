/**
 * Represents a generic node in a binary tree structure.
 * Each node contains an element and references to its left and right child nodes.
 *
 * @param <E> The type of element stored in the node.
 */
package project.domain.genericDataStructures;

public class Node<E> {

    private E element;
    private Node<E> left;
    private Node<E> right;

    /**
     * Constructs a new node with the specified element and child nodes.
     *
     * @param element    The element to store in the node.
     * @param leftChild  The left child of the node.
     * @param rightChild The right child of the node.
     */
    public Node(E element, Node<E> leftChild, Node<E> rightChild) {
        this.element = element;
        this.left = leftChild;
        this.right = rightChild;
    }

    /**
     * Retrieves the element stored in the node.
     *
     * @return The element stored in the node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Retrieves the left child of the node.
     *
     * @return The left child node, or {@code null} if no left child exists.
     */
    public Node<E> getLeft() {
        return left;
    }

    /**
     * Retrieves the right child of the node.
     *
     * @return The right child node, or {@code null} if no right child exists.
     */
    public Node<E> getRight() {
        return right;
    }

    /**
     * Updates the element stored in the node.
     *
     * @param element The new element to store in the node.
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Updates the left child of the node.
     *
     * @param leftChild The new left child node.
     */
    public void setLeft(Node<E> leftChild) {
        this.left = leftChild;
    }

    /**
     * Updates the right child of the node.
     *
     * @param rightChild The new right child node.
     */
    public void setRight(Node<E> rightChild) {
        this.right = rightChild;
    }
}
