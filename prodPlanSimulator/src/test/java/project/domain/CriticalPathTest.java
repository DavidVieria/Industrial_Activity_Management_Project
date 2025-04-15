/**
 * Tests for the CriticalPath class, which implements critical path logic in an n-ary tree.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.Entry;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;
import project.domain.service.CriticalPath;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class CriticalPathTest {

    /**
     * Instance of CriticalPath used in the tests.
     */
    private CriticalPath criticalPath;

    /**
     * N-ary production tree used in the tests.
     */
    private NaryTree<ProductionElement> tree;

    /**
     * Initial setup before each test, creating a mock production tree.
     */
    @Before
    public void setUp() {
        // Create mock items and operations for the production tree
        Item item1 = new Item("Id1", "Name1");
        Item item2 = new Item("Id2", "Name2");
        Item item3 = new Item("Id3", "Name3");
        Item item4 = new Item("Id4", "Name4");

        Operation operation1 = new Operation("IdOp1", "NameOp1");
        Operation operation2 = new Operation("IdOp2", "NameOp2");
        Operation operation3 = new Operation("IdOp3", "NameOp3");
        Operation operation4 = new Operation("IdOp4", "NameOp4");

        ProductionElement root = new ProductionElement(item1, operation1, 1);
        ProductionElement child1 = new ProductionElement(item2, operation2, 1);
        ProductionElement child2 = new ProductionElement(item3, operation3, 1);
        ProductionElement child3 = new ProductionElement(item4, operation4, 1);

        // Set up the production tree
        tree = new NaryTree<>(root); // Initialize tree with the root node
        NaryTreeNode<ProductionElement> child1node = tree.getRoot().addChild(child1);
        NaryTreeNode<ProductionElement> child2node = child1node.addChild(child2);
        NaryTreeNode<ProductionElement> child3node = child2node.addChild(child3);

        criticalPath = new CriticalPath(tree); // Initialize CriticalPath with the tree
    }

    /**
     * Tests the initialization of the tree and the generation of critical paths.
     */
    @Test
    public void testInitializeTree() {
        List<List<NaryTreeNode<ProductionElement>>> criticalPaths = criticalPath.initializeTree();

        assertNotNull(criticalPaths);
        assertFalse(criticalPaths.isEmpty());
        assertFalse(criticalPaths.getFirst().isEmpty());
    }

    /**
     * Tests the traverseAndAddToQueue method to verify if nodes are correctly added to the queue.
     */
    @Test
    public void testTraverseAndAddToQueue() {
        criticalPath.traverseAndAddToQueue(tree.getRoot(), 0);

        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> queue = criticalPath.getCriticalPathQueue();
        assertNotNull(queue);
        assertFalse(queue.isEmpty());

        List<Entry<NaryTreeNode<ProductionElement>, Integer>> entries = queue.heap;

        assertEquals(Integer.valueOf(3), entries.get(0).getValue());
        assertEquals(Integer.valueOf(2), entries.get(1).getValue());
        assertEquals(Integer.valueOf(1), entries.get(2).getValue());
        assertEquals(Integer.valueOf(0), entries.get(3).getValue());
    }

    /**
     * Tests the tracePathToRoot method to ensure the path from a node to the root is traced correctly.
     */
    @Test
    public void testTracePathToRoot() {
        NaryTreeNode<ProductionElement> node = tree.getRoot().getChildren().get(0).getChildren().get(0).getChildren().get(0);

        List<NaryTreeNode<ProductionElement>> path = new ArrayList<>();
        criticalPath.tracePathToRoot(node, path);

        assertFalse(path.isEmpty());
        assertEquals(tree.getRoot(), path.get(path.size() - 1));
    }

    /**
     * Tests the tracePathToRoot method with an intermediate-level node.
     */
    @Test
    public void testTracePathToRootWithIntermediateNode() {
        NaryTreeNode<ProductionElement> node = tree.getRoot().getChildren().get(0).getChildren().get(0);

        List<NaryTreeNode<ProductionElement>> path = new ArrayList<>();
        criticalPath.tracePathToRoot(node, path);

        assertFalse(path.isEmpty());
        assertEquals(tree.getRoot(), path.get(path.size() - 1));
    }

    /**
     * Tests the behavior of the system with a tree containing only the root node.
     */
    @Test
    public void testTreeWithOnlyRoot() {
        Item item1 = new Item("Id1", "Name1");
        Operation operation1 = new Operation("IdOp1", "NameOp1");
        ProductionElement root = new ProductionElement(item1, operation1, 1);

        NaryTree<ProductionElement> treeWithOnlyRoot = new NaryTree<>(root);
        CriticalPath criticalPathWithOnlyRoot = new CriticalPath(treeWithOnlyRoot);

        List<List<NaryTreeNode<ProductionElement>>> criticalPaths = criticalPathWithOnlyRoot.initializeTree();

        assertNotNull(criticalPaths);
        assertFalse(criticalPaths.isEmpty());
        assertEquals(1, criticalPaths.size());
        assertEquals(1, criticalPaths.get(0).size());
        assertEquals(root, criticalPaths.get(0).get(0).getElement());
    }

    /**
     * Tests the behavior of the system with a tree containing only a single critical path.
     */
    @Test
    public void testSingleCriticalPath() {
        Item item1 = new Item("Id1", "Name1");
        Item item2 = new Item("Id2", "Name2");
        Item item3 = new Item("Id3", "Name3");

        Operation operation1 = new Operation("IdOp1", "NameOp1");
        Operation operation2 = new Operation("IdOp2", "NameOp2");
        Operation operation3 = new Operation("IdOp3", "NameOp3");

        ProductionElement root = new ProductionElement(item1, operation1, 1);
        ProductionElement child1 = new ProductionElement(item2, operation2, 1);
        ProductionElement child2 = new ProductionElement(item3, operation3, 1);

        NaryTree<ProductionElement> treeWithSinglePath = new NaryTree<>(root);
        NaryTreeNode<ProductionElement> child1Node = treeWithSinglePath.getRoot().addChild(child1);
        child1Node.addChild(child2);

        CriticalPath criticalPathWithSinglePath = new CriticalPath(treeWithSinglePath);

        List<List<NaryTreeNode<ProductionElement>>> criticalPaths = criticalPathWithSinglePath.initializeTree();

        assertNotNull(criticalPaths);
        assertFalse(criticalPaths.isEmpty());
        assertEquals(1, criticalPaths.size());
        assertEquals(3, criticalPaths.get(0).size());

        assertEquals(root, criticalPaths.get(0).get(0).getElement());
        assertEquals(child1, criticalPaths.get(0).get(1).getElement());
        assertEquals(child2, criticalPaths.get(0).get(2).getElement());
    }
}
