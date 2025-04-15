package project.domain.genericDataStructures;

/**
 * Interface for a key-value pair used in data structures such as priority queues.
 * It defines methods for accessing the key and the associated value stored in the entry.
 *
 * @param <K> the type of key stored in the entry
 * @param <V> the type of value associated with the key
 */
public interface Entry<K, V> {

    /**
     * Returns the key stored in this entry.
     *
     * @return the key of this entry
     */
    K getKey();

    /**
     * Returns the value stored in this entry.
     *
     * @return the value of this entry
     */
    V getValue();
}
