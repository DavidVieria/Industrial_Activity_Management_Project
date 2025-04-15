/**
 * Unit tests for the {@link NaryTree} class, verifying its basic operations such as retrieving the root,
 * finding nodes, and merging subtrees.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;

import static org.junit.Assert.*;

public class NaryTreeTest {

    private NaryTree<String> tree;

    /**
     * Sets up the test environment by initializing a new {@link NaryTree} with a root element.
     */
    @Before
    public void setUp() {
        tree = new NaryTree<>("Root");
    }

    /**
     * Tests the {@link NaryTree#getRoot()} method to ensure the root node is correctly retrieved
     * and has the expected element.
     */
    @Test
    public void testGetRoot() {
        assertNotNull("Root node should not be null", tree.getRoot());
        assertEquals("Root node should have correct element", "Root", tree.getRoot().getElement());
    }

    /**
     * Tests the {@link NaryTree#findNode(Object)} method to ensure it finds a node
     * with the given element when it exists in the tree.
     */
    @Test
    public void testFindNodeExists() {
        NaryTreeNode<String> rootNode = tree.getRoot();
        rootNode.addChild("Child1");

        NaryTreeNode<String> result = tree.findNode("Child1");
        assertNotNull("Node with element 'Child1' should be found", result);
        assertEquals("Found node should have element 'Child1'", "Child1", result.getElement());
    }

    /**
     * Tests the {@link NaryTree#findNode(Object)} method to ensure it returns {@code null}
     * when the element is not found in the tree.
     */
    @Test
    public void testFindNodeNotExists() {
        NaryTreeNode<String> result = tree.findNode("NonExistent");
        assertNull("Node with element 'NonExistent' should not be found", result);
    }

    /**
     * Tests the {@link NaryTree#mergeTree(NaryTreeNode, NaryTree)} method to ensure a subtree
     * is correctly merged into the main tree under a specified parent node.
     */
    @Test
    public void testMergeTree() {
        NaryTree<String> subtree = new NaryTree<>("SubtreeRoot");
        subtree.getRoot().addChild("SubtreeChild");

        NaryTreeNode<String> rootNode = tree.getRoot();
        tree.mergeTree(rootNode, subtree);

        assertEquals("Root should have one child after merge", 1, rootNode.getChildren().size());
        assertEquals("Merged child should have correct element", "SubtreeChild", rootNode.getChildren().get(0).getElement());
    }

}
