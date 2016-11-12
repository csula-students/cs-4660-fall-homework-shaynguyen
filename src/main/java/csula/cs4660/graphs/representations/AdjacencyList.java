package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 */
public class AdjacencyList implements Representation {
    private Map<Node, Set<Edge>> adjacencyList;

    public AdjacencyList(File file) {
        adjacencyList = GraphHelper.parseFileMap(file);
    }

    public AdjacencyList() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        // Check if any of edges where node "x" connects to Node "y".
        return adjacencyList.get(x).stream()
                .anyMatch(edge -> edge.getTo().equals(y));
    }

    @Override
    public List<Node> neighbors(Node x) {
        // Retrieves the "destination" node from all the Edge "x" originates from
        return adjacencyList.get(x).parallelStream().map(Edge::getTo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addNode(Node x) {
        // Don't add existing Node
        if (adjacencyList.containsKey(x))
            return false;

        // add "x" to the adjacencyList with a default value
        adjacencyList.put(x, new LinkedHashSet<>());
        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        // Stop if the node doesn't exist
        if (!adjacencyList.containsKey(x))
            return false;

        // Remove the node and its values.
        adjacencyList.remove(x);

        // remove all the edges that is TO node "x"
        Iterator<Map.Entry<Node, Set<Edge>>> mapIt = adjacencyList.entrySet().iterator();
        while (mapIt.hasNext()) {
            Map.Entry<Node, Set<Edge>> entry = mapIt.next();
            Iterator<Edge> edgeIt = entry.getValue().iterator();

            while (edgeIt.hasNext()) {
                Edge edge = edgeIt.next();
                if (edge.getTo().equals(x))
                    edgeIt.remove();
            }
        }

        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        // add edge to edge set, return true if there is a change
        return adjacencyList.get(x.getFrom()).add(x);

    }

    @Override
    public boolean removeEdge(Edge x) {
        // remove teh edge if it exist. return true if a removal occur.
        return adjacencyList.get(x.getFrom()).remove(x);
    }

    @Override
    public int distance(Node from, Node to) {

        // return the edge value from an edge that node "from" and "to" are part of
        Collection<Edge> neighbors = adjacencyList.get(from);
        for (Edge edge : neighbors) {
            if (edge.getTo().equals(to))
                return edge.getValue();
        }

        // no edge exist between the 2 node, return 0
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        Optional<Node> nodeOptional = Optional.empty();

        for (Node node : adjacencyList.keySet()) {
            if (node.getData().equals(index)) {
                nodeOptional = Optional.of(node);
            }
        }

        return nodeOptional;
    }

    @Override
    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = adjacencyList.keySet().iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
}
