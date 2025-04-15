/**
 * Test class for {@link BooProductionSimulator}.
 * This class includes unit tests for verifying the functionality of the BooProductionSimulator,
 * such as operations at specific depths in a production tree and the creation of AVL trees
 * based on operation hierarchies.
 */
package project.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.NaryTree;
import project.domain.model.Operation;
import project.domain.model.Item;
import project.domain.model.ProductionElement;
import project.domain.service.BooProductionSimulator;
import project.domain.service.OperationHierarchy;
import java.util.List;
import java.util.Map;

public class BooProductionSimulatorTest {

    private BooProductionSimulator simulator;
    private NaryTree<ProductionElement> tree;

    /**
     * Sets up the test environment by initializing the simulator before each test case.
     */
    @Before
    public void setUp() {
        simulator = new BooProductionSimulator();
    }

    /**
     * Tests {@link BooProductionSimulator#getOperationsAtDepth()} with a tree containing only the root node.
     * Verifies that the correct operation is present at depth 0.
     */
    @Test
    public void getOperationsAtDepth_WithOnlyRoot() {
        Item item = new Item("item1", "Item 1");
        Operation operation = new Operation("op1", "Operation 1");
        ProductionElement element = new ProductionElement(item, operation, 10.0, 1);

        tree = new NaryTree<>(element);

        simulator.initializeRoot(tree);

        Map<Integer, List<Operation>> operationsAtDepth = simulator.getOperationsAtDepth();
        assertEquals(1, operationsAtDepth.size());
        assertTrue(operationsAtDepth.get(0).contains(operation));
    }

    /**
     * Tests {@link BooProductionSimulator#getOperationsAtDepth()} with a tree where each depth level has a single node.
     * Verifies the correct operations are assigned to their respective depths.
     */
    @Test
    public void getOperationsAtDepth_OneNodePerDepth() {
        Item item1 = new Item("item1", "Item 1");
        Item item2 = new Item("item2", "Item 2");
        Operation operation1 = new Operation("op1", "Operation 1");
        Operation operation2 = new Operation("op2", "Operation 2");

        ProductionElement element1 = new ProductionElement(item1, operation1, 10.0, 1);
        ProductionElement element2 = new ProductionElement(item2, operation2, 5.0, 2);

        tree = new NaryTree<>(element1);
        tree.getRoot().addChild(element2);

        simulator.initializeRoot(tree);

        Map<Integer, List<Operation>> operationsAtDepth = simulator.getOperationsAtDepth();

        assertEquals(2, operationsAtDepth.size());
        assertTrue(operationsAtDepth.get(0).contains(operation1));
        assertTrue(operationsAtDepth.get(1).contains(operation2));
    }

    /**
     * Tests {@link BooProductionSimulator#getOperationsAtDepth()} with a tree where multiple nodes exist at the same depth.
     * Verifies the correct operations are assigned to their respective depths.
     */
    @Test
    public void getOperationsAtDepth_MoreNodePerDepth() {
        Item item1 = new Item("item1", "Item 1");
        Item item2 = new Item("item2", "Item 2");
        Item item3 = new Item("item3", "Item 3");
        Operation operation1 = new Operation("op1", "Operation 1");
        Operation operation2 = new Operation("op2", "Operation 2");
        Operation operation3 = new Operation("op3", "Operation 3");

        ProductionElement element1 = new ProductionElement(item1, operation1, 10.0, 1);
        ProductionElement element2 = new ProductionElement(item2, operation2, 5.0, 2);
        ProductionElement element3 = new ProductionElement(item3, operation3, 3.0, 3);

        tree = new NaryTree<>(element1);
        tree.getRoot().addChild(element2);
        tree.getRoot().addChild(element3);

        simulator.initializeRoot(tree);

        Map<Integer, List<Operation>> operationsAtDepth = simulator.getOperationsAtDepth();

        assertEquals(2, operationsAtDepth.size());
        assertTrue(operationsAtDepth.get(0).contains(operation1));
        assertTrue(operationsAtDepth.get(1).contains(operation2));
        assertTrue(operationsAtDepth.get(1).contains(operation3));
    }

    /**
     * Tests {@link BooProductionSimulator#elementsByDepth()} to verify the creation of an AVL tree with {@link OperationHierarchy}.
     * Validates that the AVL tree contains the correct hierarchies for operations at each depth level.
     */
    @Test
    public void testElementsByDepth_createsAvlTreeWithOperationHierarchy() {
        Item item1 = new Item("item1", "Item 1");
        Item item2 = new Item("item2", "Item 2");
        Operation operation1 = new Operation("op1", "Operation 1");
        Operation operation2 = new Operation("op2", "Operation 2");

        ProductionElement element1 = new ProductionElement(item1, operation1, 10.0, 1);
        ProductionElement element2 = new ProductionElement(item2, operation2, 5.0, 2);

        tree = new NaryTree<>(element1);
        tree.getRoot().addChild(element2);

        simulator.initializeRoot(tree);

        AVL<OperationHierarchy> result = simulator.elementsByDepth();

        List<Operation> operationsDepth0 = simulator.getOperationsAtDepth().get(0);
        List<Operation> operationsDepth1 = simulator.getOperationsAtDepth().get(1);

        OperationHierarchy hierarchyDepth0 = new OperationHierarchy(operationsDepth0, 0);
        OperationHierarchy hierarchyDepth1 = new OperationHierarchy(operationsDepth1, 1);

        assertNotNull(result);
        assertTrue(result.find(result.getRoot(), hierarchyDepth0) != null);
        assertTrue(result.find(result.getRoot(), hierarchyDepth1) != null);
    }
}
