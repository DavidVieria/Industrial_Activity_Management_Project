package project.domain.service;

import project.domain.enums.Priority;
import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.genericDataStructures.Node;
import project.domain.model.*;
import project.repository.ProductionTreeRepository;
import project.repository.WorkStationRepository;

import java.util.ArrayList;
import java.util.List;

public class AverageProductionTime {

    private NaryTree<ProductionElement> tree;
    private AVL<OperationHierarchy> AVL;
    private List<Article> articleList;
    private List<Event> eventList;

    public AverageProductionTime(ProductionTreeRepository treeRepository, String itemID) {
        tree = treeRepository.getTreeByFinalProduct(itemID);
        AVL = new AVL<>();
        articleList = new ArrayList<>();
        eventList = new ArrayList<>();
    }

    public void function(String itemID, ProductionTreeRepository treeRepository, WorkStationRepository workStationRepository) {
        int index = 0;

        NaryTree<ProductionElement> tree = treeRepository.getTreeByFinalProduct(itemID);

        BooProductionSimulator booProductionSimulator = new BooProductionSimulator();
        booProductionSimulator.initializeRoot(tree);
        AVL<OperationHierarchy> avl = booProductionSimulator.elementsByDepth();

        createArticles(avl, itemID, index, tree);

        SimulatorAdapted simulator = new SimulatorAdapted();
        simulator.simulateWithoutPriority(articleList, workStationRepository.getAllWorkStations());
        eventList.addAll(simulator.getEvents());
    }

    public int getTotalProductionTime() {
        TotalProductionTime object = new TotalProductionTime();

        return object.getTotalTime(eventList);
    }

    private void createArticles(AVL<OperationHierarchy> avl, String itemID, int index, NaryTree<ProductionElement> tree) {
        List<List<String>> listOfOperationsList = new ArrayList<>();
        traverseAvl(avl.getRoot(), tree, listOfOperationsList);
        for (int i = 0; i < listOfOperationsList.size(); i++) {
            Article article = new Article(itemID, Priority.NORMAL, listOfOperationsList.get(i), index);
            articleList.add(article);
            index++;
        }
    }

    private void traverseAvl(Node<OperationHierarchy> node, NaryTree<ProductionElement> tree, List<List<String>> listOfOperationsList) {

        if (node != null) {
            traverseAvl(node.getLeft(), tree, listOfOperationsList);

            for (Operation operation : node.getElement().getOperationsSameHierarchy()) {
                traverseTree(tree.getRoot(), operation, listOfOperationsList);
            }

            traverseAvl(node.getRight(), tree, listOfOperationsList);
        }

    }

    private void traverseTree(NaryTreeNode<ProductionElement> node, Operation operation, List<List<String>> listOfOperationsList) {

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

    private void sequenceOperations(List<String> operations, NaryTreeNode<ProductionElement> node) {
        if (node.getParent() != null) {
            NaryTreeNode<ProductionElement> nodeParent = node.getParent();
            operations.add(nodeParent.getElement().getOperation().getId());

            sequenceOperations(operations, nodeParent);
        }
    }

    public NaryTree<ProductionElement> getTree() {
        return tree;
    }

    public project.domain.genericDataStructures.AVL<OperationHierarchy> getAVL() {
        return AVL;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
