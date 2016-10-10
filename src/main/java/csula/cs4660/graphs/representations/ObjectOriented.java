package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 * <p>
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented implements Representation {

    private List<Node> nodes;
    private List<Edge> edges;

    private Log log = LogFactory.getLog(ObjectOriented.class);
    private Node previousNode = null;

    public ObjectOriented(File file) {

        Map<Node, Set<Edge>> map = GraphHelper.parseFileMap(file);
        nodes = new ArrayList<>(map.keySet());
        edges = new ArrayList<>();

        // for each values collection, add all the elements to the "edges"
        map.values().forEach(edges::addAll);

        for (Edge e : edges) {
            nodes.get(nodes.indexOf(e.getFrom())).neighbors.add(e.getTo());
        }
    }

    public ObjectOriented() {
        nodes = new ArrayList<>();
        edges = new LinkedList<>();
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        // Check if there exist ANY edge where x "connects" to y
        return nodes.get(nodes.indexOf(x)).neighbors.contains(y);
    }

    @Override
    public List<Node> neighbors(Node x) {
        // filter edges where "x" originates from and for each edge get the
        // "destination" node.
        if(previousNode != null && previousNode.equals(x))
            return previousNode.neighbors;

        return nodes.get(nodes.indexOf(x)).neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        // add node only if it is "new" - return true
        previousNode = x;
        return nodes.indexOf(x) <= -1 && nodes.add(x);
    }

    @Override
    public boolean removeNode(Node x) {
        // Removes all edges that connects to x.
        // Return true if there is a change. False if no change
        if (!nodes.contains(x))
            return false;

        for (Node node : nodes) {
            node.neighbors.remove(x);
        }
        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        // If edge exist, don't add and return false. Otherwise add; return true
        if (previousNode != null && previousNode.equals(x.getFrom())) {
            edges.add(x);
            return previousNode.neighbors.add(x.getTo());
        }

        int fromIndex = nodes.indexOf(x.getFrom());
        if (nodes.get(fromIndex).neighbors.contains(x.getTo()))
            return false;

        edges.add(x);
        return nodes.get(fromIndex).neighbors.add(x.getTo());
    }

    @Override
    public boolean removeEdge(Edge x) {
        // If Edge exists remove it and return true. If not, return false

        if (edges.contains(x)) {
            edges.remove(x);
            return nodes.get(nodes.indexOf(x.getFrom())).neighbors.remove(x.getTo());
        }

        return false;
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
