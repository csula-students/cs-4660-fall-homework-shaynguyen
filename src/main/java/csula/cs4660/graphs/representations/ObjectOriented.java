package csula.cs4660.graphs.representations;

import com.google.common.collect.Multimap;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 * <p>
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented  implements Representation {

    private Collection<Node> nodes;
    private Collection<Edge> edges;
    private static Log log;


    public ObjectOriented(File file) {
        log = LogFactory.getLog(ObjectOriented.class);

        Multimap<Node, Edge> multimap = GraphHelper.parseLines(file);
        nodes =  new ArrayList<>(multimap.keySet());
        edges = new ArrayList<>(multimap.values());
    }

    public ObjectOriented() {
        // EMPTY
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
        return edges.stream().filter(e -> e.getFrom().equals(x))
                .map(e -> e.getTo()).collect(Collectors.toList());
    }

    @Override
    public boolean addNode(Node x) {
        // If node exist, return false, else add the node (return true)
        return nodes.contains(x) ? false : nodes.add(x);
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
        return edges.contains(x) ? false : edges.add(x);
    }

    @Override
    public boolean removeEdge(Edge x) {
        // If Edge exists remove it and return true. If not, return false
        return edges.contains(x) ? edges.remove(x) : false;
    }

    @Override
    public int distance(Node from, Node to) {
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
