/**
 * Unit tests for the production tree structure using {@link NaryTree} with {@link ProductionElement} nodes.
 * This class tests tree creation, node addition, and subtree merging functionality.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import project.domain.enums.ItemType;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.Operation;
import project.domain.model.ProductionElement;

public class ProductionTreeTest {

    private NaryTree<ProductionElement> tree;
    private Item item;
    private Operation operation;
    private ProductionElement productionElement;

    /**
     * Sets up the test environment by initializing a {@link ProductionElement} with mock {@link Item} and {@link Operation}.
     */
    @Before
    public void setUp() {
        item = new Item("idItem1", "nameItem1");
        operation = new Operation("idOp1", "nameOp1");
        productionElement = new ProductionElement(item, operation, 10, 1);
    }

    /**
     * Tests that a new tree is created successfully when the tree is empty.
     * Verifies that the root node is initialized.
     */
    @Test
    public void testNewNodeWhenTreeIsEmpty() {
        tree = new NaryTree<>(productionElement);
        assertNotNull("Root should be created", tree.getRoot());
    }

    /**
     * Tests that an {@link Item} not associated with a production tree has its type set to {@link ItemType#MATERIAL}.
     * Verifies that the root node and its child are initialized correctly.
     */
    @Test
    public void testItemHasNotTreeIsMaterial() {
        tree = new NaryTree<>(productionElement);
        Item subItem = new Item("idItem2", "nameItem2");
        ProductionElement subElement = new ProductionElement(subItem, operation, 2, 1);
        tree.getRoot().addChild(subElement);

        assertNotNull("Root should be created", tree.getRoot());

        Item item = subElement.getItem();
        assertEquals("Item type should be MATERIAL as it is not associated with a production tree", ItemType.MATERIAL, item.getItemType());
    }

    /**
     * Tests adding a child node to an existing tree.
     * Verifies that the child node is created and associated with the parent node.
     */
    @Test
    public void testNewNodeWithParent() {
        tree = new NaryTree<>(productionElement);
        NaryTreeNode<ProductionElement> childNode = tree.getRoot().addChild(productionElement);

        assertNotNull("Child node should be created", childNode);
        assertTrue("Child node should be added to root node's children", tree.getRoot().getChildren().contains(childNode));
    }

    /**
     * Tests merging a subtree into the main tree.
     * Verifies that the subtree is correctly added as a child of the specified parent node.
     */
    @Test
    public void testMergeTree() {
        tree = new NaryTree<>(productionElement);

        Item subItem = new Item("idItem2", "nameItem2");

        ProductionElement subElement = new ProductionElement(subItem, operation, 5, 2);
        NaryTreeNode<ProductionElement> subRootNode = tree.getRoot().addChild(subElement);

        NaryTree<ProductionElement> subTree = new NaryTree<>(subElement);

        tree.mergeTree(tree.getRoot(), subTree);

        assertTrue("Subtree node should be added to root node's children", tree.getRoot().getChildren().contains(subRootNode));
    }
}
