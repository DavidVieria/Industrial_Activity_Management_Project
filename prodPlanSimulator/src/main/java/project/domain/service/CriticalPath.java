package project.domain.service;

import project.domain.genericDataStructures.Entry;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.ReversedComparator;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code CriticalPath} class provides methods for identifying the critical paths in a production tree.
 * It maintains a priority queue of production elements ordered by their depth in the tree and allows for the
 * tracing of critical paths from the root to the leaves based on the operations associated with the production elements.
 */
public class CriticalPath {

    private HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> criticalPathQueue;
    private NaryTree<ProductionElement> tree;

    /**
     * Constructs a {@code CriticalPath} instance with the specified production tree.
     * Initializes the priority queue for critical paths.
     *
     * @param tree the production tree to be analyzed
     */
    public CriticalPath(NaryTree<ProductionElement> tree) {
        this.criticalPathQueue = new HeapPriorityQueue<>(new ReversedComparator<>());
        this.tree = tree;
    }

    /**
     * Returns the priority queue for critical paths.
     *
     * @return the priority queue of critical path nodes
     */
    public HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> getCriticalPathQueue() {
        return criticalPathQueue;
    }

    /**
     * Sets the priority queue for critical paths.
     *
     * @param criticalPathQueue the new priority queue for critical paths
     */
    public void setCriticalPathQueue(HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> criticalPathQueue) {
        this.criticalPathQueue = criticalPathQueue;
    }

    /**
     * Returns the production tree being analyzed.
     *
     * @return the production tree
     */
    public NaryTree<ProductionElement> getTree() {
        return tree;
    }

    /**
     * Sets the production tree to be analyzed.
     *
     * @param tree the new production tree
     */
    public void setTree(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    /**
     * Initializes the tree by traversing from the root and adding nodes to the critical path queue.
     * Starts the traversal with depth 0.
     *
     * @return a list of critical paths based on the priority queue
     */
    public List<List<NaryTreeNode<ProductionElement>>> initializeTree() {
        NaryTreeNode<ProductionElement> root = tree.getRoot();
        if (root != null) {
            traverseAndAddToQueue(root, 0); // Start from depth 0
        }
        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> priorityQueue = getCriticalPathQueue();
        return getCriticalPaths(priorityQueue);
    }

    /**
     * Recursively traverses the tree and adds nodes to the critical path queue based on their depth.
     *
     * @param node the current node being traversed
     * @param level the current depth level of the node
     */
    public void traverseAndAddToQueue(NaryTreeNode<ProductionElement> node, int level) {
        // Checks if the node contains an operation
        Operation op = node.getElement().getOperation();
        if (op != null) {
            // Adds the node to the priority queue with its depth
            criticalPathQueue.insert(node, level);
        }

        // Recursively processes the child nodes, incrementing the depth
        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            traverseAndAddToQueue(child, level + 1); // Depth increases with each level
        }
    }

    /**
     * Retrieves all critical paths from the priority queue.
     * A critical path is a path from the root to a leaf where all nodes have the maximum depth.
     *
     * @param priorityQueue the priority queue containing nodes ordered by depth
     * @return a list of critical paths, each represented as a list of nodes
     */
    public List<List<NaryTreeNode<ProductionElement>>> getCriticalPaths(HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> priorityQueue) {
        // List to store the critical paths
        List<List<NaryTreeNode<ProductionElement>>> criticalPaths = new ArrayList<>();

        // Check if the priority queue is empty
        if (priorityQueue == null || priorityQueue.isEmpty()) {
            return criticalPaths; // Return an empty list
        }

        // 1. Get the maximum depth from the first entry without removing it
        int maxLevel = priorityQueue.min().getValue();

        // 2. Process only the nodes with the maximum depth
        while (!priorityQueue.isEmpty() && priorityQueue.min().getValue() == maxLevel) {
            // Remove the next node with the maximum depth
            Entry<NaryTreeNode<ProductionElement>, Integer> entry = priorityQueue.removeMin();
            NaryTreeNode<ProductionElement> node = entry.getKey();

            // 3. Trace the path from the node to the root
            List<NaryTreeNode<ProductionElement>> path = new ArrayList<>();
            tracePathToRoot(node, path);

            // 4. Add the path (reversed to go from root to leaf)
            Collections.reverse(path);
            criticalPaths.add(path);
        }

        return criticalPaths;
    }

    /**
     * Recursively traces the path from a given node to the root.
     * The path is added to the provided list in reverse order (root -> leaf).
     *
     * @param node the current node to trace from
     * @param path the list to store the nodes on the path
     */
    public void tracePathToRoot(NaryTreeNode<ProductionElement> node, List<NaryTreeNode<ProductionElement>> path) {
        if (node == null) {
            return;
        }

        // Adds the current node to the path
        path.add(node);

        // Checks if the node has a parent and traces recursively
        if (node.getParent() != null) {
            tracePathToRoot(node.getParent(), path);
        }
    }

    /**
     * Returns a string representation of the {@code CriticalPath} object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "CriticalPath{" +
                "operationQueue =" + criticalPathQueue.toString() +
                ", tree =" + tree.toString() +
                '}';
    }
}
