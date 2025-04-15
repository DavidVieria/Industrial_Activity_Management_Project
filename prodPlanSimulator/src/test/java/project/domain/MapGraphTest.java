package project.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

import project.domain.genericDataStructures.Edge;
import project.domain.genericDataStructures.Graph;
import project.domain.genericDataStructures.MapGraph;

/**
 *
 * @author DEI-ISEP
 *
 */
public class MapGraphTest {

    final ArrayList<String> co = new ArrayList<>(Arrays.asList( "A", "A", "B", "C", "C", "D", "E", "E"));
    final ArrayList <String> cd = new ArrayList<>(Arrays.asList("B", "C", "D", "D", "E", "A", "D", "E"));
    final ArrayList <Integer> cw = new ArrayList<>(Arrays.asList( 1,  2 ,  3 ,  4 ,  5 ,  6 ,  7 ,  8 ));

    final ArrayList <String> ov = new ArrayList<>(Arrays.asList( "A",  "B",  "C" ,  "D" ,  "E" ));
    MapGraph<String, Integer> instance = null;

    @Before
    public void initializeGraph() {
        instance = new MapGraph<>(true) ;
    }

    /**
     * Test of copy constructor of class Graph.
     */
    @Test
    public void testCopyConstructor() {
        System.out.println("Test copy constructor");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        Graph<String,Integer> g = new MapGraph<>(instance);
        assertEquals("The graphs should be from the same class", instance.getClass(), g.getClass());
        assertEquals("The graphs should have equal contents", instance, g);
    }

    /**
     * Test of isDirected method, of class Graph.
     */
    @Test
    public void testIsDirected() {
        System.out.println("Test isDirected");

        assertTrue("result should be true", instance.isDirected());
        instance = new MapGraph<>(false);
        assertFalse("result should be false", instance.isDirected());
    }

    /**
     * Test of numVertices method, of class Graph.
     */
    @Test
    public void testNumVertices() {
        System.out.println("Test numVertices");

        assertEquals("result should be zero", 0, instance.numVertices());
        instance.addVertex("A");
        assertEquals("result should be one", 1, instance.numVertices());
        instance.addVertex("B");
        assertEquals("result should be two", 2, instance.numVertices());
        instance.removeVertex("A");
        assertEquals("result should be one", 1, instance.numVertices());
        instance.removeVertex("B");
        assertEquals("result should be zero", 0, instance.numVertices());
    }

    /**
     * Test of vertices method, of class Graph.
     */
    @Test
    public void testVertices() {
        System.out.println("Test vertices");

        assertEquals("vertices should be empty", 0, instance.vertices().size());

        instance.addVertex("A");
        instance.addVertex("B");

        Collection<String> cs = instance.vertices();
        assertEquals("Must have 2 vertices", 2, cs.size());
        cs.removeAll(Arrays.asList("A","B"));
        assertEquals("Vertices should be A and B", 0, cs.size());

        instance.removeVertex("A");

        cs = instance.vertices();
        assertEquals("Must have 1 vertex", 1, cs.size());
        cs.removeAll(Arrays.asList("B"));
        assertEquals("Vertex should be B", 0, cs.size());

        instance.removeVertex("B");
        cs = instance.vertices();
        assertEquals("Must not have any vertex", 0, cs.size());
    }

    /**
     * Test of validVertex method, of class Graph.
     */
    @Test
    public void testValidVertex() {
        System.out.println("Test validVertex");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        for (String v : co)
            assertTrue("vertices should exist", instance.validVertex(v));

        assertFalse("vertex should not exist", instance.validVertex("Z"));
    }

    /**
     * Test of key method, of class Graph.
     */
    @Test
    public void testKey() {
        System.out.println("Test key");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        for (int i = 0; i < ov.size(); i++)
            assertEquals("vertices should exist", Integer.valueOf(i), Integer.valueOf(instance.key(ov.get(i))));

        assertEquals("vertex should not exist", Integer.valueOf(-1), Integer.valueOf(instance.key("Z")));
    }

    /**
     * Test of testAdjVertices method, of class Graph.
     */
    @Test
    public void testAdjVertices() {
        System.out.println("Test adjVertices");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        Collection <String> cs = instance.adjVertices("A");
        assertEquals("Num adjacents should be 2", 2, cs.size());
        cs.removeIf(s -> s.equals("B") || s.equals("C"));
        assertEquals("Adjacents should be B and C", 0, cs.size());

        cs = instance.adjVertices("B");
        assertEquals("Num adjacents should be 1", 1, cs.size());
        cs.removeIf(s -> s.equals("D"));
        assertEquals("Adjacents should be D", 0, cs.size());

        cs = instance.adjVertices("E");
        assertEquals("Num adjacents should be 2", 2, cs.size());
        cs.removeIf(s -> s.equals("D") || s.equals("E"));
        assertEquals("Adjacents should be D and E", 0, cs.size());
    }

    /**
     * Test of numEdges method, of class Graph.
     */
    @Test
    public void testNumEdges() {
        System.out.println("Test numEdges");

        assertEquals("result should be zero", 0, instance.numEdges());

        instance.addEdge("A","B",1);
        assertEquals("result should be one", 1, instance.numEdges());

        instance.addEdge("A","C",2);
        assertEquals("result should be two", 2, instance.numEdges());

        instance.removeEdge("A","B");
        assertEquals("result should be one", 1, instance.numEdges());

        instance.removeEdge("A","C");
        assertEquals("result should be zero", 0, instance.numEdges());
    }

    /**
     * Test of edges method, of class Graph.
     */
    /**
     * Test of edges method, of class Graph.
     */
    @Test
    public void testEdges() {
        System.out.println("Test Edges");

        assertEquals("edges should be empty", 0, instance.edges().size());

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        Collection <Edge<String,Integer>> ced = instance.edges();
        assertEquals("Must have 8 edges", 8, ced.size());
        for (int i = 0; i < co.size(); i++) {
            int finalI = i;
            ced.removeIf(e -> e.getVOrig().equals(co.get(finalI)) && e.getVDest().equals(cd.get(finalI)) && e.getWeight().equals(cw.get(finalI)));
        }
        assertEquals("Edges should be as inserted", 0, ced.size());

        instance.removeEdge("A","B");
        ced = instance.edges();
        assertEquals("Must have 7 edges", 7, ced.size());
        for (int i = 1; i < co.size(); i++) {
            int finalI = i;
            ced.removeIf(e -> e.getVOrig().equals(co.get(finalI)) && e.getVDest().equals(cd.get(finalI)) && e.getWeight().equals(cw.get(finalI)));
        }
        assertEquals("Edges should be as inserted", 0, ced.size());

        instance.removeEdge("E","E");
        ced = instance.edges();
        assertEquals("Must have 6 edges", 6, ced.size());
        for (int i = 1; i < co.size()-1; i++) {
            int finalI = i;
            ced.removeIf(e -> e.getVOrig().equals(co.get(finalI)) && e.getVDest().equals(cd.get(finalI)) && e.getWeight().equals(cw.get(finalI)));
        }
        assertEquals("Edges should be as inserted", 0, ced.size());

        instance.removeEdge("A","C"); instance.removeEdge("B","D");
        instance.removeEdge("C","D"); instance.removeEdge("C","E");
        instance.removeEdge("D","A"); instance.removeEdge("E","D");

        assertEquals("edges should be empty", 0, instance.edges().size());
    }

    /**
     * Test of getEdge method, of class Graph.
     */
    @Test
    public void testGetEdge() {
        System.out.println("Test getEdge");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        for (int i = 0; i < co.size(); i++)
            assertEquals("edge between " + co.get(i) + " - " + cd.get(i) + " should be " + cw.get(i), cw.get(i), instance.edge(co.get(i), cd.get(i)).getWeight());

        assertNull("edge should be null", instance.edge("A","E"));
        assertNull("edge should be null", instance.edge("D","B"));
        instance.removeEdge("D","A");
        assertNull("edge should be null", instance.edge("D","A"));
    }

    /**
     * Test of getEdge by key method, of class Graph.
     */
    @Test
    public void testGetEdgeByKey() {
        System.out.println("Test getEdge");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        for (int i = 0; i < co.size(); i++)
            assertEquals("edge between " + co.get(i) + " - " + cd.get(i) + " should be " + cw.get(i), cw.get(i), instance.edge(instance.key(co.get(i)), instance.key(cd.get(i))).getWeight());

        assertNull("edge should be null", instance.edge(instance.key("A"), instance.key("E")));
        assertNull("edge should be null", instance.edge(instance.key("D"), instance.key("B")));
        instance.removeEdge("D","A");
        assertNull("edge should be null", instance.edge(instance.key("D"), instance.key("A")));
    }

    /**
     * Test of outDegree method, of class Graph.
     */
    @Test
    public void testOutDegree() {
        System.out.println("Test outDegree");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertEquals("degree should be -1", -1, instance.outDegree("G"));
        assertEquals("degree should be 2", 2, instance.outDegree("A"));
        assertEquals("degree should be 1", 1, instance.outDegree("B"));
        assertEquals("degree should be 2", 2, instance.outDegree("E"));
    }

    /**
     * Test of inDegree method, of class Graph.
     */
    @Test
    public void testInDegree() {
        System.out.println("Test inDegree");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertEquals("degree should be -1", -1, instance.inDegree("G"));
        assertEquals("degree should be 1", 1, instance.inDegree("A"));
        assertEquals("degree should be 3", 3, instance.inDegree("D"));
        assertEquals("degree should be 2", 2, instance.inDegree("E"));
    }

    /**
     * Test of outgoingEdges method, of class Graph.
     */
    @Test
    public void testOutgoingEdges() {
        System.out.println("Test outgoingEdges");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        Collection <Edge<String,Integer>> coe = instance.outgoingEdges("C");
        assertEquals("Outgoing edges of vert C should be 2", 2, coe.size());
        coe.removeIf(e -> e.getWeight() == 4 || e.getWeight() == 5);
        assertEquals("Outgoing edges of vert C should be 4 and 5", 0, coe.size());

        coe = instance.outgoingEdges("E");
        assertEquals("Outgoing edges of vert E should be 2", 2, coe.size());
        coe.removeIf(e -> e.getWeight() == 7 || e.getWeight() == 8);
        assertEquals("Outgoing edges of vert E should be 7 and 8", 0, coe.size());

        instance.removeEdge("E","E");

        coe = instance.outgoingEdges("E");
        assertEquals("Outgoing edges of vert E should be 1", 1, coe.size());
        coe.removeIf(e -> e.getWeight() == 7);
        assertEquals("Outgoing edges of vert E should be 7", 0, coe.size());

        instance.removeEdge("E","D");

        coe = instance.outgoingEdges("E");
        assertEquals("Outgoing edges of vert E should be empty", 0, coe.size());
    }

    /**
     * Test of incomingEdges method, of class Graph.
     */
    @Test
    public void testIncomingEdges() {
        System.out.println("Test incomingEdges");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        Collection <Edge<String,Integer>> cie = instance.incomingEdges("D");
        assertEquals("Incoming edges of vert C should be 3", 3, cie.size());
        cie.removeIf(e -> e.getWeight() == 3 || e.getWeight() == 4 || e.getWeight() == 7);
        assertEquals("Incoming edges of vert C should be 3, 4 and 7", 0, cie.size());

        cie = instance.incomingEdges("E");
        assertEquals("Incoming edges of vert E should be 2", 2, cie.size());
        cie.removeIf(e -> e.getWeight() == 5 || e.getWeight() == 8);
        assertEquals("Incoming edges of vert C should be 5 and 8", 0, cie.size());

        instance.removeEdge("E","E");

        cie = instance.incomingEdges("E");
        assertEquals("Incoming edges of vert E should be 1", 1, cie.size());
        cie.removeIf(e -> e.getWeight() == 5);
        assertEquals("Incoming edges of vert C should be 5", 0, cie.size());

        instance.removeEdge("C","E");

        cie = instance.incomingEdges("E");
        assertEquals("Incoming edges of vert C should be empty", 0, cie.size());
    }

    /**
     * Test of removeVertex method, of class Graph.
     */
    @Test
    public void testRemoveVertex() {
        System.out.println("Test removeVertex");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertEquals("Num vertices should be 5", 5, instance.numVertices());
        assertEquals("Num vertices should be 8", 8, instance.numEdges());
        instance.removeVertex("A");
        assertEquals("Num vertices should be 4", 4, instance.numVertices());
        assertEquals("Num vertices should be 5", 5, instance.numEdges());
        instance.removeVertex("B");
        assertEquals("Num vertices should be 3", 3, instance.numVertices());
        assertEquals("Num vertices should be 4", 4, instance.numEdges());
        instance.removeVertex("C");
        assertEquals("Num vertices should be 2", 2, instance.numVertices());
        assertEquals("Num vertices should be 3", 2, instance.numEdges());
        instance.removeVertex("D");
        assertEquals("Num vertices should be 1", 1, instance.numVertices());
        assertEquals("Num vertices should be 2", 1, instance.numEdges());
        instance.removeVertex("E");
        assertEquals("Num vertices should be 0", 0, instance.numVertices());
        assertEquals("Num vertices should be 0", 0, instance.numEdges());
    }

    /**
     * Test of removeEdge method, of class Graph.
     */
    /**
     * Test of removeEdge method, of class Graph.
     */
    @Test
    public void testRemoveEdge() {
        System.out.println("Test removeEdge");

        assertEquals("Num edges should be 0", 0, instance.numEdges());

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertEquals("Num edges should be 5", 5, instance.numVertices());
        assertEquals("Num edges should be 8", 8, instance.numEdges());

        for (int i = 0; i < co.size() - 1; i++) {
            instance.removeEdge(co.get(i), cd.get(i));
            Collection<Edge<String, Integer>> ced = instance.edges();
            int expected = co.size() - i - 1;
            assertEquals("Expected size is " + expected, expected, ced.size());
            for (int j = i + 1; j < co.size(); j++) {
                int finalJ = j;
                ced.removeIf(e -> e.getVOrig().equals(co.get(finalJ)) && e.getVDest().equals(cd.get(finalJ)) && e.getWeight().equals(cw.get(finalJ)));
            }
            assertEquals("Expected size is 0", 0, ced.size());
        }
    }

    /**
     * Test of toString method, of class Graph.
     */
    @Test
    public void testClone() {
        System.out.println("Test Clone");

        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertEquals("Num vertices should be 5", 5, instance.numVertices());
        assertEquals("Num vertices should be 8", 8, instance.numEdges());

        Graph<String, Integer> instClone = instance.clone();

        for (int i = 0; i < co.size(); i++) {
            Edge<String, Integer> ec = instClone.edge(co.get(i), cd.get(i));
            assertEquals("VOrig should be " + co.get(i), co.get(i), ec.getVOrig());
            assertEquals("VDest should be " + cd.get(i), cd.get(i), ec.getVDest());
            assertEquals("Weight should be " + cw.get(i), cw.get(i), ec.getWeight());
        }

        for (String v : co)
            instClone.removeVertex(v);

        assertEquals("Num vertices should be 5", 5, instance.numVertices());
        assertEquals("Num vertices should be 8", 8, instance.numEdges());
        assertEquals("Num vertices should be 0", 0, instClone.numVertices());
        assertEquals("Num vertices should be 0", 0, instClone.numEdges());
    }

    @Test
    public void testEquals() {
        System.out.println("Test Equals");

        for (int i = 0; i <co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        MapGraph<String,Integer> otherInst =new MapGraph<>(true) ;
        for (int i = 0; i <co.size(); i++)
            otherInst.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertEquals("Graphs should be equal", instance, otherInst);

        otherInst.removeVertex("A");

        assertNotEquals("Graphs should NOT be equal", instance, otherInst);

        instance.removeVertex("A");

        assertEquals("Graphs should be equal", instance, otherInst);

        otherInst.removeEdge("C", "E");

        assertNotEquals("Graphs should NOT be equal", instance, otherInst);

        instance.removeEdge("C", "E");

        assertEquals("Graphs should be equal", instance, otherInst);
    }

    @Test
    public void testUnDirectedGraph() {
        instance = new MapGraph<>(false);

        for (int i = 0; i <co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        for (int i = 0; i < co.size(); i++) {
            Edge<String, Integer> ec = instance.edge(co.get(i), cd.get(i));
            assertEquals(co.get(i), ec.getVOrig());
            assertEquals(cd.get(i), ec.getVDest());
            assertEquals(cw.get(i), ec.getWeight());
            Edge<String, Integer> ecu = instance.edge(cd.get(i), co.get(i));
            assertEquals(cd.get(i), ecu.getVOrig());
            assertEquals(co.get(i), ecu.getVDest());
            assertEquals(cw.get(i), ecu.getWeight());
        }

        instance.removeEdge(co.get(0), cd.get(0));

        for (int i = 1; i < co.size(); i++) {
            Edge<String, Integer> ec = instance.edge(co.get(i), cd.get(i));
            assertEquals(co.get(i), ec.getVOrig());
            assertEquals(cd.get(i), ec.getVDest());
            assertEquals(cw.get(i), ec.getWeight());
            Edge<String, Integer> ecu = instance.edge(cd.get(i), co.get(i));
            assertEquals(cd.get(i), ecu.getVOrig());
            assertEquals(co.get(i), ecu.getVDest());
            assertEquals(cw.get(i), ecu.getWeight());
        }
    }
}
