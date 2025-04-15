package project.domain.genericDataStructures;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * An implementation of a priority queue using an array-based heap.
 */
public class HeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {
    /** primary collection of priority queue entries */
    public ArrayList<Entry<K,V>> heap = new ArrayList<>();

    /** Creates an empty priority queue based on the natural ordering of its keys. */
    public HeapPriorityQueue() { super(); }

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     * @param comp comparator defining the order of keys in the priority queue
     */
    public HeapPriorityQueue(Comparator<K> comp) { super(comp); }

    /**
     * Creates a priority queue initialized with the respective
     * key-value pairs.
     * @param keys an array of the initial keys for the priority queue
     * @param values an array of the initial values for the priority queue
     */
    public HeapPriorityQueue(K[] keys, V[] values) {
        super();
        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            heap.add(new PQEntry<>(keys[i], values[i]));
        }
        buildHeap();
    }

    public int parent(int j) { return (j - 1) / 2; }
    public int left(int j) { return 2 * j + 1; }
    public int right(int j) { return 2 * j + 2; }
    public boolean hasLeft(int j) { return left(j) < heap.size(); }
    public boolean hasRight(int j) { return right(j) < heap.size(); }

    /** Exchanges the entries at indices i and j of the array list. */
    public void swap(int i, int j) {
        Entry<K, V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Moves the entry at index j higher, if necessary, to restore the heap property.
     */
    protected void percolateUp(int j) {
        int ind = parent(j);
        while (ind >= 0 && compare(heap.get(j), heap.get(ind)) < 0) {
            swap(ind, j);
            j = ind;
            ind = parent(j);
        }
    }

    /**
     * Moves the entry at index j lower, if necessary, to restore the heap property.
     */
    protected void percolateDown(int j) {
        boolean swaps = true;
        while (hasLeft(j) && swaps) {
            int indLeft = left(j);
            int smallInd = indLeft;
            if (hasRight(j) && compare(heap.get(indLeft), heap.get(right(j))) > 0) {
                smallInd = right(j);
            }
            if (compare(heap.get(j), heap.get(smallInd)) > 0) {
                swap(j, smallInd);
                j = smallInd;
            } else {
                swaps = false;
            }
        }
    }

    /**
     * Performs a batch bottom-up construction of the heap in O(n) time.
     */
    protected void buildHeap() {
        int ind = parent(heap.size() - 1);
        for (int i = ind; i >= 0; i--) {
            percolateDown(i);
        }
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public Entry<K, V> min() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);  // ensure key is valid
        Entry<K, V> newest = new PQEntry<>(key, value);
        heap.add(newest);
        percolateUp(heap.size() - 1);
        return newest;
    }

    @Override
    public Entry<K, V> removeMin() {
        if (heap.isEmpty()) {
            return null;
        }
        Entry<K, V> answer = heap.get(0);
        swap(0, heap.size() - 1);
        heap.remove(heap.size() - 1);
        percolateDown(0);
        return answer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<K, V> entry : heap) {
            sb.append(entry.getKey()).append(" ");
        }
        return sb.toString();
    }

    @Override
    public HeapPriorityQueue<K, V> clone() {
        HeapPriorityQueue<K, V> other = new HeapPriorityQueue<>();
        for (Entry<K, V> entry : heap) {
            other.insert(entry.getKey(), entry.getValue());
        }
        return other;
    }
}
