package project.domain.service;

import project.domain.model.Article;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Represents a priority queue for items, which organizes items based on their priority
 * and insertion index. This class allows for efficient retrieval of the highest priority items.
 */
public class ArticlePriorityQueue {

    private PriorityQueue<Article> articleQueue;

    /**
     * Constructs an ItemPriorityQueue. Initializes the priority queue to use
     * a comparator that sorts items based on their priority and insertion index.
     */
    public ArticlePriorityQueue() {
        this.articleQueue = new PriorityQueue<>(Comparator
                .comparing(Article::getPriority)
                .thenComparingInt(Article::getInsertionIndex));
    }

    /**
     * Adds an item to the priority queue.
     *
     * @param article the item to be added to the queue
     */
    public void addItem(Article article) {
        articleQueue.add(article);
    }

    /**
     * Retrieves and removes the item with the highest priority from the queue.
     *
     * @return the next item with the highest priority, or null if the queue is empty
     */
    public Article getNextItem() {
        return articleQueue.poll();
    }

    /**
     * Checks if the priority queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return articleQueue.isEmpty();
    }
}