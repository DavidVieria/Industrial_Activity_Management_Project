package project.domain.service;

import project.domain.enums.NodeType;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;

import java.util.HashMap;
import java.util.Map;

/**
 * A service class for searching and retrieving information about nodes in a production tree
 * represented as an N-ary tree of {@link ProductionElement} instances.
 */
public class ProductionTreeSearcher {

    private Map<String, NaryTreeNode<ProductionElement>> elementNodeMap;

    /**
     * Constructs a new {@link ProductionTreeSearcher} with an empty map to store
     * key-node mappings.
     */
    public ProductionTreeSearcher() {
        this.elementNodeMap = new HashMap<>();
    }

    /**
     * Builds a map that associates keys (e.g., item IDs, item names, operation IDs,
     * operation names) with their corresponding nodes in the given production tree.
     *
     * @param tree the N-ary tree of {@link ProductionElement} instances to build the map from.
     */
    public void buildElementNodeMap(NaryTree<ProductionElement> tree) {
        NaryTreeNode<ProductionElement> node = tree.getRoot();
        buildElementNodeMapRecursive(node);
    }

    /**
     * Recursively traverses the tree and populates the map with key-node mappings
     * for each node and its associated data.
     *
     * @param node the current node being processed.
     */
    private void buildElementNodeMapRecursive(NaryTreeNode<ProductionElement> node) {
        ProductionElement element = node.getElement();
        elementNodeMap.put(element.getItem().getId(), node);
        elementNodeMap.put(element.getItem().getName(), node);

        if (element.getOperation() != null) {
            elementNodeMap.put(element.getOperation().getId(), node);
            elementNodeMap.put(element.getOperation().getName(), node);
        }

        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            buildElementNodeMapRecursive(child);
        }
    }

    /**
     * Finds a node in the tree by a given key, such as an item ID, item name, operation ID,
     * or operation name.
     *
     * @param key the key to search for in the element-node map.
     * @return the corresponding {@link NaryTreeNode} if found, or {@code null} if not.
     */
    public NaryTreeNode<ProductionElement> findNodeByKey(String key) {
        return elementNodeMap.get(key);
    }

    /**
     * Retrieves details about a node based on a given key, including its type, quantity,
     * and parent operation (if applicable).
     *
     * @param key the key to search for in the element-node map.
     * @return a {@link NodeDetails} object containing the node's details, or {@code null} if
     *         the node is not found.
     */
    public NodeDetails nodeDetails(String key) {
        NaryTreeNode<ProductionElement> node = findNodeByKey(key);
        NodeDetails nodeDetails = null;

        if (node == null) {
            return nodeDetails;
        }

        ProductionElement element = node.getElement();
        NaryTreeNode<ProductionElement> parentNode = node.getParent();

        if (key.equals(element.getItem().getId())) {
            nodeDetails = new NodeDetails(
                    NodeType.MATERIAL,
                    element.getQuantity(),
                    (parentNode != null ? parentNode.getElement().getOperation() : null)
            );
        } else if (key.equals(element.getItem().getName())) {
            nodeDetails = new NodeDetails(
                    NodeType.MATERIAL,
                    element.getQuantity(),
                    (parentNode != null ? parentNode.getElement().getOperation() : null)
            );
        } else if (key.equals(element.getOperation().getId())) {
            nodeDetails = new NodeDetails(
                    NodeType.OPERATION,
                    (parentNode != null ? parentNode.getElement().getOperation() : null)
            );
        } else if (key.equals(element.getOperation().getName())) {
            nodeDetails = new NodeDetails(
                    NodeType.OPERATION,
                    (parentNode != null ? parentNode.getElement().getOperation() : null)
            );
        }

        return nodeDetails;
    }

    /**
     * Retrieves the map that associates keys with nodes in the production tree.
     *
     * @return the map containing key-node mappings.
     */
    public Map<String, NaryTreeNode<ProductionElement>> getElementNodeMap() {
        return elementNodeMap;
    }
}
