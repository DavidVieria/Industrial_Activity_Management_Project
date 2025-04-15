package project.domain.service;

import project.domain.enums.Priority;
import project.domain.genericDataStructures.*;
import project.domain.model.*;
import project.repository.ProductionTreeRepository;
import project.repository.WorkStationRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExportProductionSimulator {

    private NaryTree<ProductionElement> tree;
    private AVL<OperationHierarchy> AVL;
    private List<Article> articleList;
    private List<Event> eventList;
    private Map<String, Integer> workstations;
    private Map<String, Integer> operations;
    private Map<String, List<String>> workstationOperations;

    public ExportProductionSimulator(ProductionTreeRepository treeRepository, String itemID) {
        tree = treeRepository.getTreeByFinalProduct(itemID);
        AVL = new AVL<>();
        articleList = new ArrayList<>();
        eventList = new ArrayList<>();
        workstations = new HashMap<>();
        operations = new HashMap<>();
        workstationOperations = new LinkedHashMap<>();
    }

    public void productionSimulator(String itemID, ProductionTreeRepository treeRepository, WorkStationRepository workStationRepository) {
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

    public AVL<OperationHierarchy> getAVL() {
        return AVL;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void exportWorkstationsAndOperations() {
        int workstationNumber = 0;
        for (Event event : eventList) {
            String workstationID = event.getWorkStation().getWorkStationID();
            if (!workstations.containsKey(workstationID)) {
                workstationNumber++;
                workstations.put(workstationID, workstationNumber);
            }
        }

        int operationNumber = 0;
        for (Event event : eventList) {
            for (String operationID : event.getArticle().getOperations()) {
                if (!operations.containsKey(operationID)) {
                    operationNumber++;
                    operations.put(operationID, operationNumber);
                }
            }
        }

        for (Event event : eventList) {
            WorkStation workStation = event.getWorkStation();
            workstationOperations
                    .computeIfAbsent(workStation.getWorkStationID(), k -> new ArrayList<>())
                    .add(workStation.getOperation());
        }

    }

    public void export(String fileName) throws IOException {
        exportWorkstationsAndOperations();
        String scheduleData = generateData();
        exportToFile(scheduleData, fileName);
    }

    public String generateData() {
        StringBuilder data = new StringBuilder();

        data.append("id_machine;machine_state;operation_number;operation_number;machine_state;machine_state;operation_number;operation_number;machine_state;\n");

        for (Map.Entry<String, List<String>> entry : workstationOperations.entrySet()) {
            String machineId = entry.getKey();
            Integer machineNumber = workstations.get(machineId);

            List<String> operationsIds = entry.getValue();

            for (int i = 0; i + 3 < operationsIds.size(); i += 4) {
                data.append(machineNumber).append(";");


                data.append("ON;").append(operations.get(operationsIds.get(i))).append(";")
                        .append(operations.get(operationsIds.get(i+1))).append(";OFF;");

                data.append("ON;").append(operations.get(operationsIds.get(i+2))).append(";")
                        .append(operations.get(operationsIds.get(i+3))).append(";OFF;");

                data.append("\n");
            }
        }

        return data.toString();
    }

    public void exportToFile(String scheduleData, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(scheduleData);
            writer.flush();
        }
    }

    public Map<String, Integer> getWorkstations() {
        return workstations;
    }

    public Map<String, Integer> getOperations() {
        return operations;
    }
}
