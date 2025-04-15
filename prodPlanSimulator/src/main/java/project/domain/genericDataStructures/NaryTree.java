/**
 * Represents a generic n-ary tree structure, where each node can have multiple children.
 * This structure supports operations such as finding a node and merging subtrees.
 *
 * @param <E> The type of elements stored in the tree.
 */
package project.domain.genericDataStructures;

public class NaryTree<E> {

    private NaryTreeNode<E> root;

    /**
     * Constructs an N-ary tree with the specified root data.
     *
     * @param rootData The data to be stored in the root node of the tree.
     */
    public NaryTree(E rootData) {
        this.root = new NaryTreeNode<>(rootData, null);
    }

    /**
     * Gets the root node of the tree.
     *
     * @return The root node of the tree.
     */
    public NaryTreeNode<E> getRoot() {
        return root;
    }

    /**
     * Sets the root node of the tree.
     *
     * @param root The root node to set.
     */
    public void setRoot(NaryTreeNode<E> root) {
        this.root = root;
    }

    /**
     * Finds a node in the tree that contains the specified element.
     *
     * @param element The element to find in the tree.
     * @return The node containing the element, or {@code null} if not found.
     */
    public NaryTreeNode<E> findNode(E element) {
        return findNodeRecursive(root, element);
    }

    /**
     * Recursively searches for a node containing the specified element starting from the given node.
     *
     * @param node    The node to start the search from.
     * @param element The element to find.
     * @return The node containing the element, or {@code null} if not found.
     */
    private NaryTreeNode<E> findNodeRecursive(NaryTreeNode<E> node, E element) {
        if (node == null) return null;
        if (node.getElement().equals(element)) return node;
        for (NaryTreeNode<E> child : node.getChildren()) {
            NaryTreeNode<E> result = findNodeRecursive(child, element);
            if (result != null) return result;
        }
        return null;
    }

    /**
     * Merges a subtree into the current tree by adding all children of the root of the subtree
     * to the target node in the current tree.
     *
     * @param targetNode The node in the current tree to which the subtree will be merged.
     * @param subtree    The subtree to merge into the current tree.
     */
    public void mergeTree(NaryTreeNode<E> targetNode, NaryTree<E> subtree) {
        for (NaryTreeNode<E> child : subtree.getRoot().getChildren()) {
            NaryTreeNode<E> clonedSubtreeRoot = cloneSubtree(child, targetNode);
            targetNode.getChildren().add(clonedSubtreeRoot);
        }
    }

    /**
     * Recursively clones a subtree, creating a copy of the structure starting from the given node.
     *
     * @param node   The node to clone.
     * @param parent The parent node for the cloned subtree.
     * @return The root node of the cloned subtree.
     */
    private NaryTreeNode<E> cloneSubtree(NaryTreeNode<E> node, NaryTreeNode<E> parent) {
        NaryTreeNode<E> newNode = new NaryTreeNode<>(node.getElement(), parent);
        for (NaryTreeNode<E> child : node.getChildren()) {
            newNode.getChildren().add(cloneSubtree(child, newNode));
        }
        return newNode;
    }

    /**
     * Returns a string representation of the n-ary tree, showing the root's element.
     *
     * @return A string representation of the tree.
     */
    @Override
    public String toString() {
        return "ProductionTree{" +
                "root=" + (root != null ? root.getElement() : "null") +
                '}';
    }
}
