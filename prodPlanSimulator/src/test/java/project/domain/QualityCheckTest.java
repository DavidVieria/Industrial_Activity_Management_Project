/**
 * Unit tests for the QualityCheck class, which validates and performs operations
 * related to quality checks in a production n-ary tree.
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
import project.domain.service.QualityCheck;

import static org.junit.Assert.*;
import java.util.List;

public class QualityCheckTest {

    /**
     * Instance of QualityCheck used for testing.
     */
    private QualityCheck qualityCheck;

    /**
     * N-ary tree representing the production structure used in the tests.
     */
    private NaryTree<ProductionElement> tree;

    /**
     * Initializes a mock production tree and the QualityCheck instance before each test.
     */
    @Before
    public void setUp() {
        // Setup of test data: creating production tree nodes
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

        // Construct the n-ary tree structure
        tree = new NaryTree<>(root); // Initialize tree with the root node
        NaryTreeNode<ProductionElement> child1node = tree.getRoot().addChild(child1);
        NaryTreeNode<ProductionElement> child2node = tree.getRoot().addChild(child2); // Multiple children at the root
        NaryTreeNode<ProductionElement> child3node = tree.getRoot().addChild(child3);

        // Initialize QualityCheck with the tree
        qualityCheck = new QualityCheck(tree);
    }

    /**
     * Tests the inicializeRoot method, verifying if nodes are added to the priority queue correctly.
     */
    @Test
    public void testInicializeRoot() {
        // Act
        qualityCheck.inicializeRoot();

        // Assert: Verify the priority queue contains nodes with correct depths
        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> queue = qualityCheck.getQualityCheckQueue();
        assertNotNull(queue);
        assertEquals(4, queue.size()); // Check that all nodes are added

        // Verify the depth of the root node
        assertEquals(Integer.valueOf(0), queue.min().getValue()); // The root node should be at level 0
    }

    /**
     * Tests the addNodesRecursively method, ensuring nodes are added with the correct depth values.
     */
    @Test
    public void testAddNodesRecursively() {
        // Arrange
        NaryTreeNode<ProductionElement> root = tree.getRoot();

        // Act
        qualityCheck.addNodesRecursively(root, 0);

        // Assert
        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> queue = qualityCheck.getQualityCheckQueue();
        assertNotNull(queue);
        assertEquals(4, queue.size()); // Check all nodes are added

        // Verify the depth levels of nodes
        List<Entry<NaryTreeNode<ProductionElement>, Integer>> entries = queue.heap;
        assertEquals(Integer.valueOf(0), entries.get(0).getValue()); // root depth 0
        assertEquals(Integer.valueOf(1), entries.get(1).getValue()); // child1 depth 1
        assertEquals(Integer.valueOf(1), entries.get(2).getValue()); // child2 depth 1
        assertEquals(Integer.valueOf(1), entries.get(3).getValue()); // child3 depth 1
    }

    /**
     * Tests the behavior when multiple nodes are present at the same tree level.
     */
    @Test
    public void testMultipleNodesAtSameLevel() {
        // Setup tree with multiple nodes at the same level
        Item item1 = new Item("Id1", "Name1");
        Item item2 = new Item("Id2", "Name2");
        Item item3 = new Item("Id3", "Name3");
        Item item4 = new Item("Id4", "Name4");
        Item item5 = new Item("Id5", "Name5");

        Operation operation1 = new Operation("IdOp1", "NameOp1");
        Operation operation2 = new Operation("IdOp2", "NameOp2");
        Operation operation3 = new Operation("IdOp3", "NameOp3");
        Operation operation4 = new Operation("IdOp4", "NameOp4");
        Operation operation5 = new Operation("IdOp5", "NameOp5");

        ProductionElement root = new ProductionElement(item1, operation1, 1);
        ProductionElement child1 = new ProductionElement(item2, operation2, 1);
        ProductionElement child2 = new ProductionElement(item3, operation3, 1);
        ProductionElement child3 = new ProductionElement(item4, operation4, 1);
        ProductionElement child4 = new ProductionElement(item5, operation5, 1);

        tree = new NaryTree<>(root);
        NaryTreeNode<ProductionElement> child1node = tree.getRoot().addChild(child1);
        NaryTreeNode<ProductionElement> child2node = tree.getRoot().addChild(child2); // Multiple children at root level
        NaryTreeNode<ProductionElement> child3node = tree.getRoot().addChild(child3);
        NaryTreeNode<ProductionElement> child4node = tree.getRoot().addChild(child4); // Another child node at root level

        qualityCheck = new QualityCheck(tree);

        // Act
        qualityCheck.addNodesRecursively(tree.getRoot(), 0);

        // Assert
        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> queue = qualityCheck.getQualityCheckQueue();
        assertNotNull(queue);
        assertEquals(5, queue.size()); // Total 5 nodes

        // Verify node depth levels
        List<Entry<NaryTreeNode<ProductionElement>, Integer>> entries = queue.heap;
        assertEquals(Integer.valueOf(0), entries.get(0).getValue()); // root at depth 0
        assertEquals(Integer.valueOf(1), entries.get(1).getValue()); // child1 at depth 1
        assertEquals(Integer.valueOf(1), entries.get(2).getValue()); // child2 at depth 1
        assertEquals(Integer.valueOf(1), entries.get(3).getValue()); // child3 at depth 1
        assertEquals(Integer.valueOf(1), entries.get(4).getValue()); // child4 at depth 1
    }

    /**
     * Tests the addition of nodes at multiple levels in a complex tree structure.
     */
    @Test
    public void testAddNodesAtMultipleLevels() {
        // Create a more complex tree with multiple levels
        Item item1 = new Item("Id1", "Name1");
        Item item2 = new Item("Id2", "Name2");
        Item item3 = new Item("Id3", "Name3");
        Item item4 = new Item("Id4", "Name4");
        Item item5 = new Item("Id5", "Name5");
        Item item6 = new Item("Id6", "Name6");

        Operation operation1 = new Operation("IdOp1", "NameOp1");
        Operation operation2 = new Operation("IdOp2", "NameOp2");
        Operation operation3 = new Operation("IdOp3", "NameOp3");
        Operation operation4 = new Operation("IdOp4", "NameOp4");
        Operation operation5 = new Operation("IdOp5", "NameOp5");
        Operation operation6 = new Operation("IdOp6", "NameOp6");

        ProductionElement root = new ProductionElement(item1, operation1, 1);
        ProductionElement child1 = new ProductionElement(item2, operation2, 1);
        ProductionElement child2 = new ProductionElement(item3, operation3, 1);
        ProductionElement child3 = new ProductionElement(item4, operation4, 1);
        ProductionElement child4 = new ProductionElement(item5, operation5, 1);
        ProductionElement child5 = new ProductionElement(item6, operation6, 1);

        tree = new NaryTree<>(root);
        NaryTreeNode<ProductionElement> child1node = tree.getRoot().addChild(child1);
        NaryTreeNode<ProductionElement> child2node = tree.getRoot().addChild(child2);
        NaryTreeNode<ProductionElement> child3node = tree.getRoot().addChild(child3);
        NaryTreeNode<ProductionElement> child4node = tree.getRoot().addChild(child4);
        NaryTreeNode<ProductionElement> child5node = tree.getRoot().addChild(child5);

        // Adding more child nodes to different levels
        NaryTreeNode<ProductionElement> grandchild1 = child1node.addChild(new ProductionElement(item2, operation1, 1));
        NaryTreeNode<ProductionElement> grandchild2 = child1node.addChild(new ProductionElement(item3, operation2, 1));
        NaryTreeNode<ProductionElement> grandchild3 = child2node.addChild(new ProductionElement(item4, operation3, 1));

        qualityCheck = new QualityCheck(tree);

        // Act: Use the recursive method to add nodes
        qualityCheck.addNodesRecursively(tree.getRoot(), 0);

        // Assert: Check if nodes are added correctly, levels should be 0, 1, 2 for grandchildren
        HeapPriorityQueue<NaryTreeNode<ProductionElement>, Integer> queue = qualityCheck.getQualityCheckQueue();
        assertNotNull(queue);
        assertEquals(9, queue.size()); // Total nodes: root + 5 children + 3 grandchildren

        List<Entry<NaryTreeNode<ProductionElement>, Integer>> entries = queue.heap;

        // Sort the entries by their depth to check levels properly
        entries.sort((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()));

        assertEquals(Integer.valueOf(0), entries.get(0).getValue()); // root at level 0
        assertEquals(Integer.valueOf(1), entries.get(1).getValue()); // child1 at level 1
        assertEquals(Integer.valueOf(1), entries.get(2).getValue()); // child2 at level 1
        assertEquals(Integer.valueOf(1), entries.get(3).getValue()); // child3 at level 1
        assertEquals(Integer.valueOf(1), entries.get(4).getValue()); // child4 at level 1
        assertEquals(Integer.valueOf(1), entries.get(5).getValue()); // child5 at level 1
        assertEquals(Integer.valueOf(2), entries.get(6).getValue()); // grandchild1 at level 2
        assertEquals(Integer.valueOf(2), entries.get(7).getValue()); // grandchild2 at level 2
        assertEquals(Integer.valueOf(2), entries.get(8).getValue()); // grandchild3 at level 2
    }

}
