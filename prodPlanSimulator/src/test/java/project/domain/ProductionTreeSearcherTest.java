package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.enums.NodeType;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;
import project.domain.service.NodeDetails;
import project.domain.service.ProductionTreeSearcher;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ProductionTreeSearcher} class.
 */
public class ProductionTreeSearcherTest {

    private ProductionTreeSearcher searcher;
    private NaryTree<ProductionElement> tree;
    private ProductionElement rootElement;
    private ProductionElement childElement1;
    private ProductionElement childElement2;

    /**
     * Sets up the test environment by initializing a {@link ProductionTreeSearcher}
     * and creating a sample N-ary tree of {@link ProductionElement} instances.
     */
    @Before
    public void setUp() {
        searcher = new ProductionTreeSearcher();

        Item item1 = new Item("item1", "root item");
        Item item2 = new Item("item2", "child item 1");
        Item item3 = new Item("item3", "child item 2");

        Operation op1 = new Operation("op1", "root operation");
        Operation op2 = new Operation("op2", "child operation");

        rootElement = new ProductionElement(item1, op1, 1, 1);
        childElement1 = new ProductionElement(item2, op2, 5, 2);
        childElement2 = new ProductionElement(item3, 3);

        tree = new NaryTree<>(rootElement);

        tree.getRoot().addChild(childElement1);
        tree.getRoot().addChild(childElement2);
    }

    /**
     * Tests the {@link ProductionTreeSearcher#buildElementNodeMap(NaryTree)} method to ensure
     * that the element-node map is populated correctly.
     */
    @Test
    public void testBuildElementNodeMap() {
        searcher.buildElementNodeMap(tree);

        assertNotNull(searcher.getElementNodeMap());
        assertTrue(searcher.getElementNodeMap().containsKey("item1"));
        assertTrue(searcher.getElementNodeMap().containsKey("root item"));
        assertTrue(searcher.getElementNodeMap().containsKey("op1"));
        assertTrue(searcher.getElementNodeMap().containsKey("root operation"));

        assertEquals(tree.getRoot(), searcher.getElementNodeMap().get("item1"));
        assertEquals(tree.getRoot().getChildren().getFirst(), searcher.getElementNodeMap().get("item2"));
        assertEquals(tree.getRoot().getChildren().getLast(), searcher.getElementNodeMap().get("item3"));
    }

    /**
     * Tests the {@link ProductionTreeSearcher#findNodeByKey(String)} method to verify that
     * nodes can be found by their associated keys.
     */
    @Test
    public void testFindNodeByKey() {
        searcher.buildElementNodeMap(tree);

        NaryTreeNode<ProductionElement> node = searcher.findNodeByKey("item2");
        assertNotNull(node);
        assertEquals(tree.getRoot().getChildren().getFirst(), node);

        node = searcher.findNodeByKey("item3");
        assertNotNull(node);
        assertEquals(tree.getRoot().getChildren().getLast(), node);
    }

    /**
     * Tests the {@link ProductionTreeSearcher#nodeDetails(String)} method to ensure it correctly
     * retrieves details for material nodes.
     */
    @Test
    public void testNodeDetailsForMaterial() {
        searcher.buildElementNodeMap(tree);

        NodeDetails details = searcher.nodeDetails("item2");
        assertNotNull(details);
        assertEquals(NodeType.MATERIAL, details.getType());
        assertEquals(5, details.getQuantity(), 0.001);
        assertEquals("op1", details.getParentOperation().getId());
    }

    /**
     * Tests the {@link ProductionTreeSearcher#nodeDetails(String)} method to ensure it correctly
     * retrieves details for operation nodes.
     */
    @Test
    public void testNodeDetailsForOperation() {
        searcher.buildElementNodeMap(tree);

        NodeDetails details = searcher.nodeDetails("op2");
        assertNotNull(details);
        assertEquals(NodeType.OPERATION, details.getType());
        assertEquals("op1", details.getParentOperation().getId());
    }

    /**
     * Tests the {@link ProductionTreeSearcher#nodeDetails(String)} method when the node has no
     * parent operation, ensuring it handles such cases correctly.
     */
    @Test
    public void testNodeDetailsForNodeWithoutParentOperation() {
        searcher.buildElementNodeMap(tree);

        NodeDetails details = searcher.nodeDetails("item1");
        assertNotNull(details);
        assertNull(details.getParentOperation());
    }

    /**
     * Tests the {@link ProductionTreeSearcher#nodeDetails(String)} method when the key does not
     * exist in the element-node map, ensuring it returns {@code null}.
     */
    @Test
    public void testNodeDetailsForNonExistentNode() {
        NodeDetails details = searcher.nodeDetails("nonexistent");
        assertNull(details);
    }
}
