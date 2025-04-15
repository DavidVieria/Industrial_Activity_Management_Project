/**
 * Implementation of an AVL tree, a self-balancing binary search tree.
 * The tree ensures that the height difference (balance factor) between the left and right subtrees of any node
 * is at most 1, maintaining logarithmic height for efficient operations.
 *
 * @param <E> The type of elements stored in the tree. The elements must implement {@link Comparable}.
 */
package project.domain.genericDataStructures;

public class AVL<E extends Comparable<E>> extends BST<E> {

    /**
     * Calculates the balance factor of a given node.
     * The balance factor is the difference in height between the right and left subtrees.
     *
     * @param node The node for which to calculate the balance factor.
     * @return The balance factor of the node.
     */
    private int balanceFactor(Node<E> node) {
        return height(node.getRight()) - height(node.getLeft());
    }

    /**
     * Performs a right rotation on the given node.
     *
     * @param node The node to rotate.
     * @return The new root of the subtree after the rotation.
     */
    private Node<E> rightRotation(Node<E> node) {
        Node<E> leftson = node.getLeft();
        node.setLeft(leftson.getRight());
        leftson.setRight(node);
        node = leftson;
        return node;
    }

    /**
     * Performs a left rotation on the given node.
     *
     * @param node The node to rotate.
     * @return The new root of the subtree after the rotation.
     */
    private Node<E> leftRotation(Node<E> node) {
        Node<E> rightson = node.getRight();
        node.setRight(rightson.getLeft());
        rightson.setLeft(node);
        node = rightson;
        return node;
    }

    /**
     * Performs a double rotation (left-right or right-left) on the given node.
     *
     * @param node The node to rotate.
     * @return The new root of the subtree after the rotation.
     */
    private Node<E> twoRotations(Node<E> node) {
        if (balanceFactor(node) < -1) {
            node.setLeft(leftRotation(node.getLeft()));
            node = rightRotation(node);
        }
        if (balanceFactor(node) > 1) {
            node.setRight(rightRotation(node.getRight()));
            node = leftRotation(node);
        }
        return node;
    }

    /**
     * Balances the given node by performing necessary rotations to maintain AVL properties.
     *
     * @param node The node to balance.
     * @return The new root of the subtree after balancing.
     */
    private Node<E> balanceNode(Node<E> node) {
        if (balanceFactor(node) < -1) {
            if (balanceFactor(node.getLeft()) <= 0)
                node = rightRotation(node);
            else
                node = twoRotations(node);
        }
        if (balanceFactor(node) > 1) {
            if (balanceFactor(node.getRight()) >= 0)
                node = leftRotation(node);
            else
                node = twoRotations(node);
        }
        return node;
    }

    /**
     * Inserts a new element into the AVL tree, maintaining balance.
     *
     * @param element The element to insert.
     */
    @Override
    public void insert(E element) {
        root = insert(element, root);
    }

    /**
     * Helper method for inserting an element into a subtree.
     *
     * @param element The element to insert.
     * @param node    The root of the subtree where the element will be inserted.
     * @return The new root of the subtree after insertion.
     */
    private Node<E> insert(E element, Node<E> node) {
        if (node == null)
            return new Node<>(element, null, null);

        if (element == node.getElement()) {
            node.setElement(element);
        } else {
            if (node.getElement().compareTo(element) > 0) {
                node.setLeft(insert(element, node.getLeft()));
                node = balanceNode(node);
            } else {
                node.setRight(insert(element, node.getRight()));
                node = balanceNode(node);
            }
        }
        return node;
    }

    /**
     * Removes an element from the AVL tree, maintaining balance.
     *
     * @param element The element to remove.
     */
    @Override
    public void remove(E element) {
        root = remove(element, getRoot());
    }

    /**
     * Helper method for removing an element from a subtree.
     *
     * @param element The element to remove.
     * @param node    The root of the subtree where the element will be removed.
     * @return The new root of the subtree after removal.
     */
    private Node<E> remove(E element, Node<E> node) {
        if (node == null)
            return null;

        if (node.getElement() == element) {
            if (node.getLeft() == null && node.getRight() == null)
                return null;
            if (node.getLeft() == null)
                return node.getRight();
            if (node.getRight() == null)
                return node.getLeft();
            E smallElem = smallestElement(node.getRight());
            node.setElement(smallElem);
            node.setRight(remove(smallElem, node.getRight()));
            node = balanceNode(node);
        } else if (node.getElement().compareTo(element) > 0) {
            node.setLeft(remove(element, node.getLeft()));
            node = balanceNode(node);
        } else {
            node.setRight(remove(element, node.getRight()));
            node = balanceNode(node);
        }
        return node;
    }

    /**
     * Compares this AVL tree to another object for equality.
     *
     * @param otherObj The object to compare with.
     * @return {@code true} if the trees are equal, {@code false} otherwise.
     */
    public boolean equals(Object otherObj) {
        if (this == otherObj)
            return true;

        if (otherObj == null || this.getClass() != otherObj.getClass())
            return false;

        AVL<E> second = (AVL<E>) otherObj;
        return equals(root, second.root);
    }

    /**
     * Compares two subtrees for equality.
     *
     * @param root1 The root of the first subtree.
     * @param root2 The root of the second subtree.
     * @return {@code true} if the subtrees are equal, {@code false} otherwise.
     */
    public boolean equals(Node<E> root1, Node<E> root2) {
        if (root1 == null && root2 == null)
            return true;
        else if (root1 != null && root2 != null) {
            if (root1.getElement().compareTo(root2.getElement()) == 0) {
                return equals(root1.getLeft(), root2.getLeft())
                        && equals(root1.getRight(), root2.getRight());
            } else
                return false;
        } else return false;
    }
}
