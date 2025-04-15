/**
 * Unit tests for the {@link MaterialQuantitiesSearcher} class, which calculates and tracks
 * material quantities in a production structure represented by an N-ary tree.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import project.domain.genericDataStructures.BST;
import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.Item;
import project.domain.model.ProductionElement;
import project.domain.service.MaterialQuantitiesSearcher;
import project.domain.service.MaterialQuantityGroup;

import java.util.Map;

import static org.junit.Assert.*;

public class MaterialQuantitiesSearcherTest {

    private MaterialQuantitiesSearcher searcher;
    private NaryTree<ProductionElement> tree;
    private Item rootItem;
    private ProductionElement rootElement;
    private Item childItem1, childItem2, childItem3, grandChildItem1, grandChildItem2;
    private ProductionElement element1, element2, element3, grandElement1, grandElement2;

    /**
     * Initializes the test environment by creating a {@link MaterialQuantitiesSearcher},
     * an {@link NaryTree} structure, and various {@link ProductionElement} and {@link Item} instances.
     */
    @Before
    public void setUp() {
        searcher = new MaterialQuantitiesSearcher();

        rootItem = new Item("1", "RootItem");
        childItem1 = new Item("2", "ChildItem1");
        childItem2 = new Item("3", "ChildItem2");
        childItem3 = new Item("4", "ChildItem3");
        grandChildItem1 = new Item("5", "GrandChildItem1");
        grandChildItem2 = new Item("6", "GrandChildItem2");

        rootElement = new ProductionElement(rootItem, 10.0);
        element1 = new ProductionElement(childItem1, 5.0);
        element2 = new ProductionElement(childItem2, 15.0);
        element3 = new ProductionElement(childItem3, 10.0);
        grandElement1 = new ProductionElement(grandChildItem1, 3.0);
        grandElement2 = new ProductionElement(grandChildItem2, 7.0);

        tree = new NaryTree<>(rootElement);
    }

    /**
     * Tests the material tracking functionality with a tree containing a single node.
     * Verifies that the resulting BST contains one element representing the root node.
     */
    @Test
    public void testTrackingMaterialQuantitiesWithSingleNode() {
        BST<MaterialQuantityGroup> result = searcher.trackingMaterialQuantities(tree, true);
        assertNotNull("BST should not be null", result);
        assertEquals("BST should have 1 element", 1, result.size());
    }

    /**
     * Tests the material tracking functionality with a tree containing multiple nodes.
     * Verifies that the resulting BST includes all nodes in the tree.
     */
    @Test
    public void testTrackingMaterialQuantitiesWithMultipleNodes() {
        NaryTreeNode<ProductionElement> rootNode = tree.getRoot();
        rootNode.addChild(element1);
        rootNode.addChild(element2);

        BST<MaterialQuantityGroup> result = searcher.trackingMaterialQuantities(tree, true);

        assertNotNull("BST should not be null", result);
        assertEquals("BST should have 3 elements", 3, result.size());
    }

    /**
     * Tests the material tracking functionality when nodes in the tree have identical quantities.
     * Verifies that such nodes are grouped into a single entry in the resulting BST.
     */
    @Test
    public void testTrackingMaterialsWithSameQuantities() {
        NaryTreeNode<ProductionElement> rootNode = tree.getRoot();
        rootNode.addChild(new ProductionElement(childItem1, 10.0));
        rootNode.addChild(new ProductionElement(childItem2, 10.0));

        BST<MaterialQuantityGroup> result = searcher.trackingMaterialQuantities(tree, true);

        assertEquals("BST should have 1 element as quantities are the same", 1, result.size());
    }

    /**
     * Tests the calculation of materials and their respective quantities in the tree.
     * Verifies that the resulting map includes all materials with their correct quantities.
     */
    @Test
    public void testMaterialsAndQuantities() {
        NaryTreeNode<ProductionElement> rootNode = tree.getRoot();
        rootNode.addChild(element1);
        rootNode.addChild(element3);

        Map<Item, Double> result = searcher.materialsAndQuantity(tree);

        assertEquals("Should contain 3 items", 3, result.size());
        assertEquals("RootItem quantity should be 10.0", 10.0, result.get(rootItem), 0.001);
    }

    /**
     * Tests the calculation of materials and their quantities in a tree with multiple levels.
     * Verifies that the resulting map includes materials at all levels of the tree.
     */
    @Test
    public void testMaterialsAndQuantitiesWithDeepTree() {
        NaryTreeNode<ProductionElement> rootNode = tree.getRoot();
        NaryTreeNode<ProductionElement> level1 = rootNode.addChild(element1);
        level1.addChild(grandElement1);
        level1.addChild(grandElement2);

        Map<Item, Double> result = searcher.materialsAndQuantity(tree);

        assertEquals("Should contain 4 items", 4, result.size());
        assertEquals("RootItem quantity should be 10.0", 10.0, result.get(rootItem), 0.001);
        assertEquals("GrandChildItem1 quantity should be 3.0", 3.0, result.get(grandChildItem1), 0.001);
    }

    /**
     * Tests the consistency of the materials and quantities calculation after multiple calls.
     * Verifies that subsequent calls produce the same results.
     */
    @Test
    public void testMaterialsAndQuantitiesConsistencyAfterMultipleCalls() {
        NaryTreeNode<ProductionElement> rootNode = tree.getRoot();
        rootNode.addChild(element1);
        NaryTreeNode<ProductionElement> node = rootNode.addChild(element2);
        node.addChild(element1);

        Map<Item, Double> result = searcher.materialsAndQuantity(tree);

        assertEquals("Should contain 3 items", 3, result.size());
        assertEquals("ChildItem1 quantity should be 10.0", 10.0, result.get(childItem1), 0.001);
    }
}
