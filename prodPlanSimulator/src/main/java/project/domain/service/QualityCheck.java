package project.domain.service;

import project.domain.genericDataStructures.DefaultComparator;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;

import java.util.*;

/**
 * The {@code QualityCheck} class provides methods for managing the quality check process on a production tree.
 * It maintains a priority queue that holds the nodes of the production tree, sorted by depth (level),
 * and allows initialization of the tree and the insertion of nodes into the queue.
 */
public class QualityCheck {

    private HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> qualityCheckQueue;

    private NaryTree<ProductionElement> tree;

    /**
     * Constructs a {@code QualityCheck} instance with the specified production tree.
     * Initializes the priority queue for quality check nodes.
     *
     * @param tree the production tree to be checked
     */
    public QualityCheck(NaryTree<ProductionElement> tree) {
        qualityCheckQueue = new HeapPriorityQueue<>(new DefaultComparator<>());
        this.tree = tree;
    }

    /**
     * Returns the priority queue used for the quality check.
     *
     * @return the priority queue for quality check nodes
     */
    public HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> getQualityCheckQueue() {
        return qualityCheckQueue;
    }

    /**
     * Returns the production tree being checked.
     *
     * @return the production tree
     */
    public NaryTree<ProductionElement> getTree() {
        return tree;
    }

    /**
     * Sets the priority queue for the quality check.
     *
     * @param qualityCheckQueue the new priority queue
     */
    public void setQualityCheckQueue(HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> qualityCheckQueue) {
        this.qualityCheckQueue = qualityCheckQueue;
    }

    /**
     * Sets the production tree to be checked.
     *
     * @param tree the new production tree
     */
    public void setTree(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    /**
     * Compares this {@code QualityCheck} instance to another object.
     *
     * @param o the object to compare with
     * @return {@code true} if both objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualityCheck qualityCheck = (QualityCheck) o;
        return qualityCheckQueue.equals(qualityCheck.qualityCheckQueue)
                && tree.equals(qualityCheck.tree);
    }

    /**
     * Returns a hash code value for this {@code QualityCheck} instance.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(qualityCheckQueue, tree);
    }

    /**
     * Returns a string representation of this {@code QualityCheck} instance.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Quality Check:  " +
                "Production Node Queue = '" + qualityCheckQueue.toString() + '\'' +
                ", Tree = '" + tree + '\'';
    }

    /**
     * Initializes the root node of the production tree and starts adding nodes to the quality check queue.
     * The root node is added with a depth level of 0.
     */
    public void inicializeRoot() {
        NaryTreeNode<ProductionElement> root = tree.getRoot();
        if (root != null) {
            addNodesRecursively(root, 0); // Root depth is 0
        }
    }

    /**
     * Recursively adds nodes to the priority queue based on their depth level.
     *
     * @param node the current node to add
     * @param level the depth level of the current node
     */
    public void addNodesRecursively(NaryTreeNode<ProductionElement> node, int level) {
        qualityCheckQueue.insert(node, level); // Adds the node to the priority queue

        // Recursively add all child nodes, incrementing the depth level
        List<NaryTreeNode<ProductionElement>> children = node.getChildren();
        for (NaryTreeNode<ProductionElement> child : children) {
            addNodesRecursively(child, level + 1); // Increase the depth level for each child node
        }
    }
}
