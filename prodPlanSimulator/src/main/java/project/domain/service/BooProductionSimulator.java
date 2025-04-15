/**
 * A service class that simulates the production process and organizes operations by their depth
 * in a hierarchical structure.
 */
package project.domain.service;

import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooProductionSimulator {

    private Map<Integer, List<Operation>> operationsAtDepth;

    /**
     * Constructs a new BooProductionSimulator, initializing the operations at depth map.
     */
    public BooProductionSimulator() {
        operationsAtDepth = new HashMap<>();
    }

    /**
     * Initializes the root of the tree and starts adding nodes recursively to build
     * the operations hierarchy.
     *
     * @param tree The {@link NaryTree} of {@link ProductionElement} to process.
     */
    public void initializeRoot(NaryTree<ProductionElement> tree) {
        NaryTreeNode<ProductionElement> root = tree.getRoot();
        if (root != null) {
            addNodesRecursively(root);
        }
    }

    /**
     * Recursively traverses the tree, adding operations to the map based on their depth.
     *
     * @param node The current {@link NaryTreeNode} being processed.
     */
    private void addNodesRecursively(NaryTreeNode<ProductionElement> node) {
        if (node.getElement().getOperation() != null) {
            operationsAtDepth.putIfAbsent(node.getLevel(), new ArrayList<>());
            operationsAtDepth.get(node.getLevel()).add(node.getElement().getOperation());
        }

        List<NaryTreeNode<ProductionElement>> children = node.getChildren();
        for (NaryTreeNode<ProductionElement> child : children) {
            addNodesRecursively(child);
        }
    }

    /**
     * Organizes operations by depth in an AVL tree structure.
     *
     * @return An {@link AVL} tree of {@link OperationHierarchy}, sorted by depth.
     */
    public AVL<OperationHierarchy> elementsByDepth() {

        AVL<OperationHierarchy> avlTree = new AVL<>();

        for (Map.Entry<Integer, List<Operation>> entry : operationsAtDepth.entrySet()) {
            int depth = entry.getKey();
            List<Operation> operationsAtDepth = entry.getValue();
            OperationHierarchy operationHierarchy = new OperationHierarchy(operationsAtDepth, depth);
            avlTree.insert(operationHierarchy);
        }

        return avlTree;
    }

    /**
     * Retrieves the map of operations organized by depth.
     *
     * @return A map where the key is the depth level and the value is a list of {@link Operation}s at that depth.
     */
    public Map<Integer, List<Operation>> getOperationsAtDepth() {
        return operationsAtDepth;
    }
}
