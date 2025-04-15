package project.domain.genericDataStructures;

import java.util.Comparator;

/**
 * An abstract base class that simplifies the implementation of the {@link PriorityQueue} interface.
 * Provides shared functionality for managing entries and comparisons within a priority queue.
 *
 * @param <K> the type of keys maintained by the priority queue
 * @param <V> the type of values associated with the keys
 */
public abstract class AbstractPriorityQueue<K, V> implements PriorityQueue<K, V> {

    //---------------- nested PQEntry class ----------------
    /**
     * A concrete implementation of the {@link Entry} interface to be used within
     * a {@link PriorityQueue} implementation.
     *
     * @param <K> the type of key stored in this entry
     * @param <V> the type of value stored in this entry
     */
    protected static class PQEntry<K, V> implements Entry<K, V> {
        private K k;  // key
        private V v;  // value

        /**
         * Constructs an entry with the specified key and value.
         *
         * @param key the key to associate with this entry
         * @param value the value to associate with this entry
         */
        public PQEntry(K key, V value) {
            k = key;
            v = value;
        }

        /**
         * Returns the key stored in this entry.
         *
         * @return the key of this entry
         */
        @Override
        public K getKey() {
            return k;
        }

        /**
         * Returns the value stored in this entry.
         *
         * @return the value of this entry
         */
        @Override
        public V getValue() {
            return v;
        }

        /**
         * Updates the key of this entry.
         *
         * @param key the new key to associate with this entry
         */
        protected void setKey(K key) {
            k = key;
        }

        /**
         * Updates the value of this entry.
         *
         * @param value the new value to associate with this entry
         */
        protected void setValue(V value) {
            v = value;
        }
    } //----------- end of nested PQEntry class -----------

    // Instance variable for an AbstractPriorityQueue
    /**
     * The comparator that defines the ordering of keys in the priority queue.
     */
    private Comparator<K> comp;

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     *
     * @param c the comparator defining the order of keys in the priority queue
     */
    protected AbstractPriorityQueue(Comparator<K> c) {
        comp = c;
    }

    /**
     * Creates an empty priority queue based on the natural ordering of its keys.
     */
    protected AbstractPriorityQueue() {
        this(new DefaultComparator<K>());
    }

    /**
     * Compares two entries based on their keys.
     *
     * @param a the first entry to compare
     * @param b the second entry to compare
     * @return a negative integer if the key of {@code a} is less than the key of {@code b};
     *         a positive integer if the key of {@code a} is greater than the key of {@code b};
     *         zero if the keys are equal
     */
    protected int compare(Entry<K, V> a, Entry<K, V> b) {
        return comp.compare(a.getKey(), b.getKey());
    }

    /**
     * Validates whether a key is compatible with the comparator.
     *
     * @param key the key to validate
     * @return {@code true} if the key is valid, {@code false} otherwise
     * @throws IllegalArgumentException if the key is not comparable
     */
    protected boolean checkKey(K key) throws IllegalArgumentException {
        try {
            return (comp.compare(key, key) == 0);  // Tests if the key can be compared to itself
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible key");
        }
    }

    /**
     * Tests whether the priority queue is empty.
     *
     * @return {@code true} if the priority queue contains no elements, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
