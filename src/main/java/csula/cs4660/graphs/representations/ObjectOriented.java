package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 * <p>
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented implements Representation {

    private Collection<Node> nodes;
    private Collection<Edge> edges;

    public ObjectOriented(File file) {

        Map<Node, Set<Edge>> map = GraphHelper.parseFileMap(file);
        nodes = new LinkedHashSet<>(map.keySet());
        edges = new LinkedHashSet<>();

        // for each values collection, add all the elements to the "edges"
        map.values().forEach(edges::addAll);
    }

    public ObjectOriented() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        // Check if there exist ANY edge where x "connects" to y
        return edges.stream().anyMatch(e ->
                e.getFrom().equals(x) && e.getTo().equals(y));
    }

    @Override
    public List<Node> neighbors(Node x) {
        // filter edges where "x" originates from and for each edge get the
        // "destination" node.
        return edges.parallelStream().filter(e -> e.getFrom().equals(x))
                .map(Edge::getTo).collect(Collectors.toList());
    }

    @Override
    public boolean addNode(Node x) {
        // add node only if it is "new" - return true
        return nodes.add(x);
    }

    @Override
    public boolean removeNode(Node x) {
        // Removes all edges that connects to x.
        // Return true if there is a change. False if no change
        return edges.removeIf(e -> e.getTo().equals(x));
    }

    @Override
    public boolean addEdge(Edge x) {
        // If edge exist, don't add and return false. Otherwise add; return true
        return edges.add(x);
    }

    @Override
    public boolean removeEdge(Edge x) {
        // If Edge exists remove it and return true. If not, return false
        return edges.remove(x);
    }

    @Override
    public int distance(Node from, Node to) {
        // search through all edges and return the edge value between the two node
        for (Edge edge : edges) {
            if (edge.getFrom().equals(from) && edge.getTo().equals(to))
                return edge.getValue();
        }

        // edges does not exist between the two node. return 0
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        Optional<Node> nodeOptional = Optional.empty();

        for (Node node : nodes) {
            if (node.getData().equals(index)) {
                nodeOptional = Optional.of(node);
            }
        }

        return nodeOptional;
    }
}
