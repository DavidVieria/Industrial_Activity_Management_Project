package project.domain.genericDataStructures;

/**
 * An interface for a priority queue that stores key-value pairs.
 * A priority queue allows efficient insertion and retrieval of entries
 * based on their keys, where the entry with the minimal key is given the highest priority.
 *
 * @param <K> the type of keys maintained by this priority queue
 * @param <V> the type of values associated with the keys
 */
public interface PriorityQueue<K, V> {

    /**
     * Returns the number of items in the priority queue.
     *
     * @return the number of items currently stored in the priority queue
     */
    int size();

    /**
     * Tests whether the priority queue is empty.
     *
     * @return {@code true} if the priority queue contains no elements, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Inserts a new key-value pair into the priority queue and returns the entry created.
     *
     * @param key   the key of the new entry
     * @param value the value associated with the key
     * @return the entry containing the newly inserted key-value pair
     * @throws IllegalArgumentException if the key is not compatible with the priority queue's comparator
     */
    Entry<K, V> insert(K key, V value) throws IllegalArgumentException;

    /**
     * Returns (but does not remove) an entry with the minimal key in the priority queue.
     * If the priority queue is empty, this method returns {@code null}.
     *
     * @return the entry with the minimal key, or {@code null} if the queue is empty
     */
    Entry<K, V> min();

    /**
     * Removes and returns an entry with the minimal key in the priority queue.
     * If the priority queue is empty, this method returns {@code null}.
     *
     * @return the removed entry with the minimal key, or {@code null} if the queue is empty
     */
    Entry<K, V> removeMin();
}
