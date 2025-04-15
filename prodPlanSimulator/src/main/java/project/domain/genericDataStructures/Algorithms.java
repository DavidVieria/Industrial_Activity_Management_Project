package project.domain.genericDataStructures;

import java.util.*;

/**
 * This class contains all the generic algorithms used in the project.
 */
public class Algorithms {

    /**
     * Attempts to detect a cycle in a directed graph.
     *
     * @param <V> the type of the vertices in the graph
     * @param <W> the type of the weights in the graph
     * @param graph the directed graph to analyze
     * @return the first detected cycle as a list of vertices, or an empty list if no cycles are found
     */
    public static <V, W> List<V> detectCircularDependency(Graph<V, W> graph) {
        Set<V> visited = new HashSet<>();

        for (V vertex : graph.vertices()) {
            if (!visited.contains(vertex)) {
                List<V> cycle = dfsDetectCycleFromNode(graph, vertex, visited);
                if (!cycle.isEmpty()) {
                    return cycle;
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Helper method using DFS to detect a cycle starting from a given node.
     *
     * @param <V> the type of the vertices in the graph
     * @param <W> the type of the weights in the graph
     * @param graph the directed graph
     * @param startVertex the starting vertex for the search
     * @param visited the set of already visited vertices
     * @return the first detected cycle as a list of vertices, or an empty list if no cycle is found
     */
    private static <V, W> List<V> dfsDetectCycleFromNode(
            Graph<V, W> graph,
            V startVertex,
            Set<V> visited
    ) {
        Deque<V> path = new ArrayDeque<>();
        Map<V, Integer> pathIndex = new HashMap<>();
        Deque<V> recursionStack = new ArrayDeque<>();

        recursionStack.push(startVertex);

        while (!recursionStack.isEmpty()) {
            V current = recursionStack.peek();

            if (!visited.contains(current)) {
                visited.add(current);
                path.addLast(current);
                pathIndex.put(current, path.size() - 1);

                for (V neighbor : graph.adjVertices(current)) {
                    if (!visited.contains(neighbor)) {
                        recursionStack.push(neighbor);
                    } else if (pathIndex.containsKey(neighbor)) {
                        int cycleStart = pathIndex.get(neighbor);
                        return new ArrayList<>(new ArrayList<>(path).subList(cycleStart, path.size()));
                    }
                }
            } else {
                recursionStack.pop();
                path.removeLast();
                pathIndex.remove(current);
            }
        }

        return Collections.emptyList();
    }

    /**
     * Performs a topological sort on a directed graph.
     *
     * @param <V> the type of the vertices in the graph
     * @param graph the graph to sort
     * @return a list of vertices sorted in topological order
     */
    public static <V, W> LinkedList<V> topologicalSort(Graph<V, W> graph) {
        LinkedList<V> result = new LinkedList<>();
        Set<V> visited = new HashSet<>();

        for (V vertex : graph.vertices()) {
            if (!visited.contains(vertex)) {
                dfsTopologicalSort(vertex, graph, visited, result);
            }
        }

        return result;
    }

    /**
     * Depth-First Search (DFS) helper method for topological sorting.
     *
     * @param <V> the type of the vertices in the graph
     * @param current the current vertex being visited
     * @param graph the graph being traversed
     * @param visited a set of already visited vertices
     * @param result the list storing vertices in topological order
     */
    private static <V, W> void dfsTopologicalSort(V current, Graph<V, W> graph, Set<V> visited, LinkedList<V> result) {
        visited.add(current);

        for (V neighbor : graph.adjVertices(current)) {
            if (!visited.contains(neighbor)) {
                dfsTopologicalSort(neighbor, graph, visited, result);
            }
        }

        result.addFirst(current);
    }

    public static <V, W> V findEndVertice(Graph<V, W> graph) {
        for (V vertice : graph.vertices()) {
            if (graph.outDegree(vertice) == 0) {
                return vertice;
            }
        }
        return null;
    }
}
