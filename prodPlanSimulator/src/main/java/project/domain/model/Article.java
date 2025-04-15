package project.domain.model;

import project.domain.enums.Priority;

import java.util.List;
import java.util.Objects;

/**
 * A class representing an item with an ID, priority, operations to perform,
 * and various states related to its processing.
 */
public class Article {

    private String articleID;
    private Priority priority;
    private List<String> operations;
    private int currentOperationIndex;
    private boolean inOperation;
    private boolean allOperationsCompleted;
    private final int insertionIndex;

    /**
     * Constructs an Item with the specified parameters.
     *
     * @param articleID              the unique identifier of the item
     * @param priority            the priority of the item
     * @param operations          the list of operations to be performed on the item
     * @param insertionIndex      the index at which the item is inserted
     */
    public Article(String articleID, Priority priority, List<String> operations, int insertionIndex) {
        this.articleID = articleID;
        this.priority = priority;
        this.operations = operations;
        this.currentOperationIndex = 0;
        this.inOperation = false;
        this.allOperationsCompleted = false;
        this.insertionIndex = insertionIndex;
    }

    /**
     * Gets the item's ID.
     *
     * @return the item ID
     */
    public String getIdArticle() {
        return articleID;
    }

    /**
     * Sets the item's ID.
     *
     * @param itemID the new item ID
     */
    public void setIdArticle(String itemID) {
        this.articleID = itemID;
    }

    /**
     * Gets the list of operations for the item.
     *
     * @return the list of operations
     */
    public List<String> getOperations() {
        return operations;
    }

    /**
     * Sets the list of operations for the item.
     *
     * @param operations the new list of operations
     */
    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    /**
     * Gets the priority of the item.
     *
     * @return the item's priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the item.
     *
     * @param priority the new priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Gets the current index of the operation being performed.
     *
     * @return the current operation index
     */
    public int getCurrentOperationIndex() {
        return currentOperationIndex;
    }

    /**
     * Sets the current index of the operation being performed.
     *
     * @param currentOperationIndex the new current operation index
     */
    public void setCurrentOperationIndex(int currentOperationIndex) {
        this.currentOperationIndex = currentOperationIndex;
    }

    /**
     * Checks if the item is currently in operation.
     *
     * @return true if the item is in operation, false otherwise
     */
    public boolean isInOperation() {
        return inOperation;
    }

    /**
     * Sets the operation state of the item.
     *
     * @param inOperation the new operation state
     */
    public void setInOperation(boolean inOperation) {
        this.inOperation = inOperation;
    }

    /**
     * Checks if all operations for the item have been completed.
     *
     * @return true if all operations are completed, false otherwise
     */
    public boolean isAllOperationsCompleted() {
        return allOperationsCompleted;
    }

    /**
     * Sets the completion state of all operations for the item.
     *
     * @param allOperationsCompleted the new completion state
     */
    public void setAllOperationsCompleted(boolean allOperationsCompleted) {
        this.allOperationsCompleted = allOperationsCompleted;
    }

    /**
     * Gets the insertion index of the item.
     *
     * @return the insertion index
     */
    public int getInsertionIndex() {
        return insertionIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return currentOperationIndex == article.currentOperationIndex
                && inOperation == article.inOperation
                && allOperationsCompleted == article.allOperationsCompleted
                && insertionIndex == article.insertionIndex
                && Objects.equals(articleID, article.articleID)
                && priority == article.priority
                && Objects.equals(operations, article.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleID, priority, operations, currentOperationIndex, inOperation, allOperationsCompleted, insertionIndex);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + articleID + '\'' +
                ", priority=" + priority +
                ", operations=" + operations +
                '}';
    }
}
