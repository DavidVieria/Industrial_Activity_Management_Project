package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.NaryTree;
import project.domain.model.Item;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;
import project.domain.service.MaterialQuantityUpdate;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MaterialQuantityUpdate} class.
 */
public class MaterialQuantityUpdateTest {

    private NaryTree<ProductionElement> tree;
    private ProductionElement rootElement;
    private ProductionElement childElement1;
    private ProductionElement childElement2;
    private MaterialQuantityUpdate materialQuantity;

    /**
     * Sets up the test environment by creating a sample N-ary tree of {@link ProductionElement}
     * objects and initializing the {@link MaterialQuantityUpdate} instance.
     */
    @Before
    public void setUp() {
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

        materialQuantity = new MaterialQuantityUpdate(tree.getRoot());
    }

    /**
     * Tests the {@link MaterialQuantityUpdate#updateMaterialQuantities(double)} method to ensure that
     * the root node's quantity is updated correctly.
     */
    @Test
    public void testUpdateMaterialQuantities() {
        double newRootQuantity = 2;
        materialQuantity.updateMaterialQuantities(newRootQuantity);

        assertEquals("The root node quantity should be updated to the new value",
                2, tree.getRoot().getElement().getQuantity(), 0.001);
    }

    /**
     * Tests cascading updates in the N-ary tree structure when the root node's quantity is updated,
     * ensuring all descendant nodes are updated proportionally.
     */
    @Test
    public void testCascadingUpdatesWithMultipleLevels() {
        double newRootQuantity = 3;
        materialQuantity.updateMaterialQuantities(newRootQuantity);

        assertEquals("The root node quantity should be updated correctly",
                3, tree.getRoot().getElement().getQuantity(), 0.001);
        assertEquals("The quantity of the first child node should reflect the cascading update",
                15, tree.getRoot().getChildren().getFirst().getElement().getQuantity(), 0.001);
        assertEquals("The quantity of the last child node should reflect the cascading update",
                9, tree.getRoot().getChildren().getLast().getElement().getQuantity(), 0.001);
    }

    /**
     * Tests cascading updates when the {@link MaterialQuantityUpdate} instance operates on a non-root
     * node, ensuring that only the subtree rooted at the specified node is updated.
     */
    @Test
    public void testCascadingUpdatesWithNodeNotRoot() {
        double newQuantity = 10;
        MaterialQuantityUpdate materialQuantity = new MaterialQuantityUpdate(tree.getRoot().getChildren().getFirst());
        Item item4 = new Item("item4", "new item 4");
        ProductionElement element4 = new ProductionElement(item4, 3);
        tree.getRoot().getChildren().getFirst().addChild(element4);

        materialQuantity.updateMaterialQuantities(newQuantity);

        assertEquals("The root node quantity should remain unchanged",
                1, tree.getRoot().getElement().getQuantity(), 0.001);
        assertEquals("The first child node quantity should be updated to the new value",
                10, tree.getRoot().getChildren().getFirst().getElement().getQuantity(), 0.001);
        assertEquals("The last child node quantity should remain unchanged",
                3, tree.getRoot().getChildren().getLast().getElement().getQuantity(), 0.001);
        assertEquals("The new child node quantity should reflect the cascading update",
                6, tree.getRoot().getChildren().getFirst().getChildren().getFirst().getElement().getQuantity(), 0.001);
    }
}
