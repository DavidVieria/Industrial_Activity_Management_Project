package project.domain.service;

import project.domain.enums.Priority;
import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.genericDataStructures.Node;
import project.domain.model.*;
import project.repository.OrderRepository;
import project.repository.ProductionTreeRepository;
import project.repository.WorkStationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the processing of orders, including tree cloning, material updates, and simulation.
 */
public class ProcessOrders {

    private Map<Order, NaryTree<ProductionElement>> orderTreeMap;
    private Map<Order, AVL<OperationHierarchy>> orderAVLMap;
    private List<Article> articleList;
    private List<Event> eventList;

    /**
     * Constructs an instance of ProcessOrders, initializing internal data structures.
     */
    public ProcessOrders() {
        orderTreeMap = new HashMap<>();
        orderAVLMap = new HashMap<>();
        articleList = new ArrayList<>();
        eventList = new ArrayList<>();
    }

    /**
     * Processes all orders from the given repositories, creating trees, updating materials,
     * and simulating production based on priorities.
     *
     * @param orderRepository the repository containing orders
     * @param treeRepository the repository containing production trees
     */
    public void processOrders(OrderRepository orderRepository, ProductionTreeRepository treeRepository, WorkStationRepository workStationRepository) {

        int index = 0;
        for (Order order : orderRepository.getAllOrders()) {
            NaryTree<ProductionElement> tree = treeRepository.getTreeByFinalProduct(order.getItem().getId());
            NaryTree<ProductionElement> clonedTree = cloneTree(tree);

            MaterialQuantityUpdate materialQuantityUpdate = new MaterialQuantityUpdate(clonedTree.getRoot());
            materialQuantityUpdate.updateMaterialQuantities(order.getQuantity());
            orderTreeMap.put(order, clonedTree);

            BooProductionSimulator booProductionSimulator = new BooProductionSimulator();
            booProductionSimulator.initializeRoot(clonedTree);
            AVL<OperationHierarchy> avl = booProductionSimulator.elementsByDepth();
            orderAVLMap.put(order, avl);

            createArticles(avl, order, index, clonedTree);
        }

        SimulatorAdapted simulator = new SimulatorAdapted();
        simulator.simulateWithPriority(articleList, workStationRepository.getAllWorkStations());
        eventList.addAll(simulator.getEvents());

    }

    /**
     * Creates articles from the given AVL and associates them with the given order.
     *
     * @param avl the AVL containing operation hierarchies
     * @param order the order associated with the AVL
     * @param index the index of the order
     */
    private void createArticles(AVL<OperationHierarchy> avl, Order order, int index, NaryTree<ProductionElement> tree) {
        String id = order.getItem().getId();
        Priority priority = order.getPriority();
        List<List<String>> listOfOperationsList = new ArrayList<>();
        traverseAvl(avl.getRoot(), tree, listOfOperationsList);
        for (int i = 0; i < listOfOperationsList.size(); i++) {
            Article article = new Article(id, priority, listOfOperationsList.get(i), index);
            articleList.add(article);
            index++;
        }
    }

    /**
     * Traverses the AVL tree to gather operations and create workstations.
     *
     * @param node the root node of the AVL
     */
    private void traverseAvl(Node<OperationHierarchy> node, NaryTree<ProductionElement> tree, List<List<String>> listOfOperationsList) {

        if (node != null) {
            traverseAvl(node.getLeft(), tree, listOfOperationsList);

            for (Operation operation : node.getElement().getOperationsSameHierarchy()) {
                traverseTree(tree.getRoot(), operation, listOfOperationsList);
            }

            traverseAvl(node.getRight(), tree, listOfOperationsList);
        }

    }

    private void traverseTree (NaryTreeNode<ProductionElement> node, Operation operation, List<List<String>> listOfOperationsList) {

        if (node.getElement().getOperation() != null) {
            if (node.getElement().getOperation().getId().equals(operation.getId())) {
                int num = node.getChildren().size();
                for (int i = 0; i < num; i++) {
                    List<String> operationsList = new ArrayList<>();
                    operationsList.add(operation.getId());
                    sequenceOperations(operationsList, node);
                    listOfOperationsList.add(operationsList);
                }
            }
        }

        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            traverseTree(child, operation, listOfOperationsList);
        }
    }

    private void sequenceOperations (List<String> operations, NaryTreeNode<ProductionElement> node) {
        if (node.getParent() != null) {
            NaryTreeNode<ProductionElement> nodeParent = node.getParent();
            operations.add(nodeParent.getElement().getOperation().getId());

            sequenceOperations(operations, nodeParent);
        }
    }

    /**
     * Clones a given N-ary tree, creating a deep copy.
     *
     * @param tree the original tree to clone
     * @return a cloned copy of the tree
     */
    public NaryTree<ProductionElement> cloneTree(NaryTree<ProductionElement> tree) {
        ProductionElement clonedRoot = new ProductionElement(tree.getRoot().getElement().getItem(),
                tree.getRoot().getElement().getOperation(),
                tree.getRoot().getElement().getQuantity(),
                tree.getRoot().getElement().getItemOpRelation());
        NaryTree<ProductionElement> clonedTree = new NaryTree<>(clonedRoot);
        cloneNode(tree.getRoot(), clonedTree.getRoot());
        return clonedTree;
    }

    /**
     * Recursively clones the nodes of a given tree.
     *
     * @param node the current node in the original tree
     * @param clonedNode the corresponding node in the cloned tree
     */
    private void cloneNode(NaryTreeNode<ProductionElement> node, NaryTreeNode<ProductionElement> clonedNode) {

        for (NaryTreeNode<ProductionElement> child : node.getChildren()) {
            ProductionElement clonedElement = new ProductionElement(child.getElement().getItem(),
                    child.getElement().getOperation(),
                    child.getElement().getQuantity(),
                    child.getElement().getItemOpRelation());
            NaryTreeNode<ProductionElement> clonedChild = clonedNode.addChild(clonedElement);
            cloneNode(child, clonedChild);
        }
    }

    /**
     * Gets the map of orders to their associated production trees.
     *
     * @return the map of orders to production trees
     */
    public Map<Order, NaryTree<ProductionElement>> getOrderTreeMap() {
        return orderTreeMap;
    }

    /**
     * Gets the map of orders to their associated AVL trees of operation hierarchies.
     *
     * @return the map of orders to AVL trees
     */
    public Map<Order, AVL<OperationHierarchy>> getOrderAVLMap() {
        return orderAVLMap;
    }

    /**
     * Gets the list of simulation events generated during order processing.
     *
     * @return the list of events
     */
    public List<Event> getEventList() {
        return eventList;
    }
}
