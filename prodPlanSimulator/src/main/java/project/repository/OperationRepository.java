package project.repository;

import project.domain.model.Operation;

import java.util.ArrayList;
import java.util.List;

public class OperationRepository {

    private List<Operation> operations;

    public OperationRepository(List<Operation> operations) {
        this.operations = operations;
    }

    public OperationRepository() {
        operations = new ArrayList<>();
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public void addOperations(List<Operation> operations) {
        this.operations.addAll(operations);
    }

    public Operation getOperationById(String id) {
        for (Operation operation : operations) {
            if (operation.getId().equals(id)) {
                return operation;
            }
        }
        return null;
    }

    public List<Operation> getAllOperations() {
        return new ArrayList<>(operations);
    }

    public void clearOperations() {
        operations.clear();
    }

    public boolean isEmpty() {
        return operations.isEmpty();
    }

    public int getOperationCount() {
        return operations.size();
    }
}
