/**
 * Unit tests for the {@link HeapPriorityQueue} class, which implements a priority queue using a heap structure.
 * The queue operates on {@link NaryTreeNode} instances of {@link ProductionElement} with integer priorities.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Entry;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;

import static org.junit.Assert.*;
import java.util.List;

public class HeapPriorityQueueTest {

    private HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> queue;
    private NaryTreeNode<ProductionElement> rootNode;
    private NaryTreeNode<ProductionElement> childNode1;
    private NaryTreeNode<ProductionElement> childNode2;
    private NaryTreeNode<ProductionElement> childNode3;

    /**
     * Sets up the test environment, initializing a {@link HeapPriorityQueue}
     * and creating a tree structure of {@link NaryTreeNode} objects containing {@link ProductionElement}.
     */
    @Before
    public void setUp() {
        Item item1 = new Item("Id1", "Name1");
        Item item2 = new Item("Id2", "Name2");
        Item item3 = new Item("Id3", "Name3");
        Item item4 = new Item("Id4", "Name4");

        Operation operation1 = new Operation("IdOp1", "NameOp1");
        Operation operation2 = new Operation("IdOp2", "NameOp2");
        Operation operation3 = new Operation("IdOp3", "NameOp3");
        Operation operation4 = new Operation("IdOp4", "NameOp4");

        ProductionElement rootElement = new ProductionElement(item1, operation1, 1);
        ProductionElement childElement1 = new ProductionElement(item2, operation2, 1);
        ProductionElement childElement2 = new ProductionElement(item3, operation3, 1);
        ProductionElement childElement3 = new ProductionElement(item4, operation4, 1);

        rootNode = new NaryTreeNode<>(rootElement, null);
        childNode1 = new NaryTreeNode<>(childElement1, rootNode);
        childNode2 = new NaryTreeNode<>(childElement2, childNode1);
        childNode3 = new NaryTreeNode<>(childElement3, childNode2);

        rootNode.addChild(childNode1.getElement());
        rootNode.addChild(childNode2.getElement());
        rootNode.addChild(childNode3.getElement());

        queue = new HeapPriorityQueue<>();
    }

    /**
     * Tests adding nodes to the queue and verifies the size and minimum element.
     */
    @Test
    public void testAddNodeToQueue() {
        queue.insert(rootNode, 0);
        queue.insert(childNode1, 1);
        queue.insert(childNode2, 1);
        queue.insert(childNode3, 1);

        assertEquals(4, queue.size());

        Entry<NaryTreeNode<ProductionElement>, Integer> minEntry = queue.min();
        assertEquals(rootNode, minEntry.getKey());
        assertEquals(Integer.valueOf(0), minEntry.getValue());
    }

    /**
     * Tests removing the minimum element from the queue and verifies the removed elements and updated size.
     */
    @Test
    public void testRemoveMinFromQueue() {
        queue.insert(rootNode, 0);
        queue.insert(childNode1, 1);
        queue.insert(childNode2, 1);
        queue.insert(childNode3, 1);

        Entry<NaryTreeNode<ProductionElement>, Integer> minEntry = queue.removeMin();

        assertEquals(rootNode, minEntry.getKey());
        assertEquals(Integer.valueOf(0), minEntry.getValue());

        minEntry = queue.removeMin();
        assertEquals(childNode1, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());
    }

    /**
     * Tests the state of the priority queue after multiple insertions and verifies the heap structure.
     */
    @Test
    public void testPriorityQueueAfterInsertions() {
        queue.insert(rootNode, 0);
        queue.insert(childNode1, 1);
        queue.insert(childNode2, 1);
        queue.insert(childNode3, 2);

        assertEquals(4, queue.size());

        List<Entry<NaryTreeNode<ProductionElement>, Integer>> entries = queue.heap;
        assertEquals(Integer.valueOf(0), entries.get(0).getValue());
        assertEquals(Integer.valueOf(1), entries.get(1).getValue());
        assertEquals(Integer.valueOf(1), entries.get(2).getValue());
        assertEquals(Integer.valueOf(2), entries.get(3).getValue());
    }

    /**
     * Tests the behavior of removing the minimum element when the queue is empty.
     */
    @Test
    public void testRemoveMinWhenEmpty() {
        assertTrue(queue.isEmpty());

        queue.insert(rootNode, 0);
        queue.insert(childNode1, 1);

        queue.removeMin();
        queue.removeMin();

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests the size of the queue after insertions and removals.
     */
    @Test
    public void testSizeAfterInsertsAndRemoves() {
        queue.insert(rootNode, 0);
        queue.insert(childNode1, 1);
        queue.insert(childNode2, 2);

        assertEquals(3, queue.size());

        queue.removeMin();
        assertEquals(2, queue.size());

        queue.removeMin();
        assertEquals(1, queue.size());

        queue.removeMin();
        assertEquals(0, queue.size());
    }

    /**
     * Tests inserting nodes with the same priority and verifies their order of removal.
     */
    @Test
    public void testInsertNodesWithSamePriority() {
        queue.insert(rootNode, 1);
        queue.insert(childNode1, 1);
        queue.insert(childNode2, 1);
        queue.insert(childNode3, 1);

        assertEquals(4, queue.size());

        Entry<NaryTreeNode<ProductionElement>, Integer> minEntry = queue.removeMin();
        assertEquals(rootNode, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());

        minEntry = queue.removeMin();
        assertEquals(childNode1, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());

        minEntry = queue.removeMin();
        assertEquals(childNode2, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());

        minEntry = queue.removeMin();
        assertEquals(childNode3, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());
    }

    /**
     * Tests removing all nodes from the queue and verifies the size after each removal.
     */
    @Test
    public void testRemoveAllNodes() {
        queue.insert(rootNode, 0);
        queue.insert(childNode1, 1);
        queue.insert(childNode2, 2);
        queue.insert(childNode3, 3);

        assertEquals(4, queue.size());

        Entry<NaryTreeNode<ProductionElement>, Integer> minEntry = queue.removeMin();
        assertEquals(rootNode, minEntry.getKey());
        assertEquals(Integer.valueOf(0), minEntry.getValue());
        assertEquals(3, queue.size());

        minEntry = queue.removeMin();
        assertEquals(childNode1, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());
        assertEquals(2, queue.size());

        minEntry = queue.removeMin();
        assertEquals(childNode2, minEntry.getKey());
        assertEquals(Integer.valueOf(2), minEntry.getValue());
        assertEquals(1, queue.size());

        minEntry = queue.removeMin();
        assertEquals(childNode3, minEntry.getKey());
        assertEquals(Integer.valueOf(3), minEntry.getValue());
        assertEquals(0, queue.size());
    }

    /**
     * Tests the behavior of a queue containing only a single element.
     */
    @Test
    public void testSingleElementQueue() {
        queue.insert(rootNode, 1);

        assertEquals(1, queue.size());

        Entry<NaryTreeNode<ProductionElement>, Integer> minEntry = queue.removeMin();
        assertEquals(rootNode, minEntry.getKey());
        assertEquals(Integer.valueOf(1), minEntry.getValue());

        assertTrue(queue.isEmpty());
    }

}
