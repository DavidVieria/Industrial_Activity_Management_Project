/**
 * Test class for {@link BST}.
 * This class includes unit tests to verify the functionality of a Binary Search Tree (BST),
 * such as insertion, deletion, traversal methods, and structural properties like height and size.
 */
package project.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import project.domain.genericDataStructures.BST;

public class BSTTest {
    Integer[] arr = {20,15,10,13,8,17,40,50,30,7};
    int[] height = {0,1,2,3,3,3,3,3,3,4};
    Integer[] inorderT = {7,8,10,13,15,17,20,30,40,50};
    Integer[] preorderT = {20, 15, 10, 8, 7, 13, 17, 40, 30, 50};
    Integer[] posorderT = {7, 8, 13, 10, 17, 15, 30, 50, 40, 20};

    BST<Integer> instance;

    /**
     * Default constructor for the test class.
     */
    public BSTTest() {
    }

    /**
     * Sets up the test environment by initializing the BST instance and populating it
     * with a predefined set of integers before each test case.
     */
    @Before
    public void setUp() {
        instance = new BST<>();
        for (int i : arr)
            instance.insert(i);
    }

    /**
     * Tests the {@link BST#size()} method to verify the correct size of the tree
     * after various operations, including insertion and duplication handling.
     */
    @Test
    public void testSize() {
        assertEquals("size should be = 10", instance.size(), arr.length);

        BST<String> sInstance = new BST<>();
        assertEquals("size should be = 0", sInstance.size(), 0);
        sInstance.insert("A");
        assertEquals("size should be = 1", sInstance.size(), 1);
        sInstance.insert("B");
        assertEquals("size should be = 2", sInstance.size(), 2);
        sInstance.insert("A");
        assertEquals("size should be = 2", sInstance.size(), 2);
    }

    /**
     * Tests the {@link BST#(Object)} method to ensure elements are correctly added
     * and duplicates are not counted.
     */
    @Test
    public void testInsert() {
        int[] arr = {20,15,10,13,8,17,40,50,30,20,15,10};
        BST<Integer> instance = new BST<>();
        for (int i = 0; i < 9; i++) {
            instance.insert(arr[i]);
            assertEquals("size should be = " + (i + 1), instance.size(), i + 1);
        }
        for (int i = 9; i < arr.length; i++) {
            instance.insert(arr[i]);
            assertEquals("size should be = 9", instance.size(), 9);
        }
    }

    /**
     * Tests the {@link BST#(Object)} method to verify that elements can be
     * correctly removed from the BST, and the size decreases accordingly.
     */
    @Test
    public void testRemove() {
        int qtd = arr.length;
        instance.remove(999);
        assertEquals("size should be = " + qtd, instance.size(), qtd);
        for (int i = 0; i < arr.length; i++) {
            instance.remove(arr[i]);
            qtd--;
            assertEquals("size should be = " + qtd, qtd, instance.size());
        }
        instance.remove(999);
        assertEquals("size should be = 0", 0, instance.size());
    }

    /**
     * Tests the {@link BST#isEmpty()} method to check if the tree correctly identifies
     * when it is empty or not.
     */
    @Test
    public void testIsEmpty() {
        assertFalse("the BST should be NOT empty", instance.isEmpty());
        instance = new BST<>();
        assertTrue("the BST should be empty", instance.isEmpty());

        instance.insert(11);
        assertFalse("the BST should be NOT empty", instance.isEmpty());

        instance.remove(11);
        assertTrue("the BST should be empty", instance.isEmpty());
    }

    /**
     * Tests the {@link BST#height()} method to verify the height of the tree
     * at different stages of insertion.
     */
    @Test
    public void testHeight() {
        instance = new BST<>();
        assertEquals("height should be = -1", instance.height(), -1);
        for (int idx = 0; idx < arr.length; idx++) {
            instance.insert(arr[idx]);
            assertEquals("height should be = " + height[idx], instance.height(), height[idx]);
        }
        instance = new BST<>();
        assertEquals("height should be = -1", instance.height(), -1);
    }

    /**
     * Tests the {@link BST#smallestElement()} method to ensure the smallest element
     * in the tree is correctly identified and updated after removals.
     */
    @Test
    public void testSmallestElement() {
        assertEquals(Integer.valueOf(7), instance.smallestElement());
        instance.remove(7);
        assertEquals(Integer.valueOf(8), instance.smallestElement());
        instance.remove(8);
        assertEquals(Integer.valueOf(10), instance.smallestElement());
    }

    /**
     * Tests the {@link BST#nodesByLevel()} method to verify the correct mapping of nodes
     * at each level of the BST.
     */
    @Test
    public void testProcessBstByLevel() {
        Map<Integer, List<Integer>> expResult = new HashMap<>();
        expResult.put(0, Arrays.asList(new Integer[]{20}));
        expResult.put(1, Arrays.asList(new Integer[]{15, 40}));
        expResult.put(2, Arrays.asList(new Integer[]{10, 17, 30, 50}));
        expResult.put(3, Arrays.asList(new Integer[]{8, 13}));
        expResult.put(4, Arrays.asList(new Integer[]{7}));

        Map<Integer, List<Integer>> result = instance.nodesByLevel();

        for (Map.Entry<Integer, List<Integer>> e : result.entrySet()) {
            assertEquals(expResult.get(e.getKey()), e.getValue());
        }
    }

    /**
     * Tests the {@link BST#inOrder()} method to verify the correctness of the in-order traversal.
     */
    @Test
    public void testInOrder() {
        List<Integer> lExpected = Arrays.asList(inorderT);
        assertEquals("inOrder should be " + lExpected.toString(), lExpected, instance.inOrder());
    }

    /**
     * Tests the {@link BST#preOrder()} method to verify the correctness of the pre-order traversal.
     */
    @Test
    public void testPreOrder() {
        List<Integer> lExpected = Arrays.asList(preorderT);
        assertEquals("preOrder should be " + lExpected.toString(), lExpected, instance.preOrder());
    }

    /**
     * Tests the {@link BST#posOrder()} method to verify the correctness of the post-order traversal.
     */
    @Test
    public void testPosOrder() {
        List<Integer> lExpected = Arrays.asList(posorderT);
        assertEquals("posOrder should be " + lExpected.toString(), lExpected, instance.posOrder());
    }
}




