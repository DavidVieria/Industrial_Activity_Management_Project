/**
 * A generic implementation of a Binary Search Tree (BST).
 *
 * @param <E> The type of elements stored in the BST. Must implement {@link Comparable}.
 */
package project.domain.genericDataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BST<E extends Comparable<E>> implements BSTInterface<E> {

    protected Node<E> root = null;

    /**
     * Constructs an empty Binary Search Tree.
     */
    public BST() {
        root = null;
    }

    /**
     * Gets the root node of the BST.
     *
     * @return The root node.
     */
    public Node<E> getRoot() {
        return root;
    }

    /**
     * Checks if the BST is empty.
     *
     * @return {@code true} if the BST is empty, otherwise {@code false}.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Searches for a node containing the specified element in the BST.
     *
     * @param node    The starting node for the search.
     * @param element The element to search for.
     * @return The node containing the element, or {@code null} if not found.
     */
    public Node<E> find(Node<E> node, E element) {
        if (node == null) {
            return null;
        }
        if (element == null) {
            return null;
        }
        if (node.getElement().compareTo(element) > 0) {
            return find(node.getLeft(), element);
        } else if (node.getElement().compareTo(element) < 0) {
            return find(node.getRight(), element);
        }
        return node;
    }

    /**
     * Inserts an element into the BST.
     *
     * @param element The element to insert.
     */
    public void insert(E element) {
        root = insert(element, root);
    }

    private Node<E> insert(E element, Node<E> node) {
        if (node == null) {
            return new Node<>(element, null, null);
        }
        if (node.getElement().compareTo(element) > 0) {
            node.setLeft(insert(element, node.getLeft()));
        } else {
            if (node.getElement().compareTo(element) < 0) {
                node.setRight(insert(element, node.getRight()));
            }
        }
        return node;
    }

    /**
     * Removes an element from the BST.
     *
     * @param element The element to remove.
     */
    public void remove(E element) {
        root = remove(element, getRoot());
    }

    private Node<E> remove(E element, Node<E> node) {
        if (node == null) {
            return null;
        }
        if (element.compareTo(node.getElement()) == 0) {
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            if (node.getLeft() == null) {
                return node.getRight();
            }
            if (node.getRight() == null) {
                return node.getLeft();
            }
            E min = smallestElement(node.getRight());
            node.setElement(min);
            node.setRight(remove(min, node.getRight()));
        } else if (element.compareTo(node.getElement()) < 0) {
            node.setLeft(remove(element, node.getLeft()));
        } else {
            node.setRight(remove(element, node.getRight()));
        }
        return node;
    }

    /**
     * Calculates the number of nodes in the BST.
     *
     * @return The size of the BST.
     */
    public int size() {
        return size(root);
    }

    private int size(Node<E> node) {
        if (node == null) {
            return 0;
        }
        return (1 + size(node.getLeft()) + size(node.getRight()));
    }

    /**
     * Calculates the height of the BST.
     *
     * @return The height of the BST.
     */
    public int height() {
        return height(root);
    }

    protected int height(Node<E> node) {
        if (node == null) {
            return -1;
        }
        Integer hl = height(node.getLeft());
        Integer hr = height(node.getRight());
        return (1 + ((hl < hr) ? hr : hl));
    }

    /**
     * Finds the smallest element in the BST.
     *
     * @return The smallest element, or {@code null} if the tree is empty.
     */
    public E smallestElement() {
        return smallestElement(root);
    }

    public E smallestElement(Node<E> node) {
        if (node == null) {
            return null;
        }
        if (node.getLeft() == null) {
            return node.getElement();
        }
        return smallestElement(node.getLeft());
    }

    /**
     * Performs an in-order traversal of the BST.
     *
     * @return An iterable collection of elements in in-order.
     */
    public Iterable<E> inOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null) {
            inOrderSubtree(root, snapshot);
        }
        return snapshot;
    }

    private void inOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }
        inOrderSubtree(node.getLeft(), snapshot);
        snapshot.add(node.getElement());
        inOrderSubtree(node.getRight(), snapshot);
    }

    /**
     * Performs a pre-order traversal of the BST.
     *
     * @return An iterable collection of elements in pre-order.
     */
    public Iterable<E> preOrder() {
        List<E> preOrderList = new ArrayList<>();
        preOrderSubtree(root, preOrderList);
        return preOrderList;
    }

    private void preOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }
        snapshot.add(node.getElement());
        preOrderSubtree(node.getLeft(), snapshot);
        preOrderSubtree(node.getRight(), snapshot);
    }

    /**
     * Performs a post-order traversal of the BST.
     *
     * @return An iterable collection of elements in post-order.
     */
    public Iterable<E> posOrder() {
        List<E> postOrderList = new ArrayList<>();
        posOrderSubtree(root, postOrderList);
        return postOrderList;
    }

    private void posOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }
        posOrderSubtree(node.getLeft(), snapshot);
        posOrderSubtree(node.getRight(), snapshot);
        snapshot.add(node.getElement());
    }

    /**
     * Groups nodes by their levels in the BST.
     *
     * @return A map where keys are levels, and values are lists of elements at each level.
     */
    public Map<Integer, List<E>> nodesByLevel() {
        HashMap<Integer, List<E>> levelMap = new HashMap<>();
        processBstByLevel(root, levelMap, 0);
        return levelMap;
    }

    private void processBstByLevel(Node<E> node, Map<Integer, List<E>> result, int level) {
        if (node == null) {
            return;
        }
        if (result.containsKey(level)) {
            result.get(level).add(node.getElement());
        } else {
            List<E> newList = new ArrayList<>();
            newList.add(node.getElement());
            result.put(level, newList);
        }

        processBstByLevel(node.getLeft(), result, level + 1);
        processBstByLevel(node.getRight(), result, level + 1);
    }
}
