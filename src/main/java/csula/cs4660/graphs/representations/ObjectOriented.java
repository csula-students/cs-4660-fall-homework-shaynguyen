package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 * <p>
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented implements Representation {


    private Collection<Node> nodes;
    private Collection<Edge> edges;
    private static Log log = LogFactory.getLog(ObjectOriented.class);


    public ObjectOriented(File file) {
        List<String> lines = readFile(file);
        edges = parseEdges(lines);
        nodes = parseNodes(edges);

    }

    public ObjectOriented() {
        // EMPTY
    }

    private List<String> readFile(File file) {
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(file));
            String line;

            while ((line = inputStream.readLine()) != null) {
                lines.add(line);
            }
            return lines;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Edge> parseEdges(List<String> lines) {
        List<Edge> result = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            result.add(parseEdge(lines.get(i)));
        }
        return result;
    }

    private Edge parseEdge(String line) {
        String[] result = line.split(":");
        Node<Integer> fromNode = new Node(Integer.valueOf(result[0]));
        Node<Integer> toNode = new Node(Integer.valueOf(result[1]));
        Edge edge = new Edge(fromNode, toNode, Integer.valueOf(result[2]));
        return edge;
    }

    private Set<Node> parseNodes(Collection<Edge> edges) {
        Set<Node> nodes = new HashSet<>();
        for (Edge edge : edges)
            nodes.add(edge.getFrom());
        return nodes;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        for (Edge edge : edges)
            if (edge.getFrom().equals(x) && edge.getTo().equals(y))
                return true;

        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {

        if (!nodes.contains(x))
            return new ArrayList<>();

        List<Node> neighbors = new ArrayList<>();

        for (Edge edge : edges)
            if (edge.getFrom().equals(x))
                neighbors.add(edge.getTo());

        return neighbors;
    }

    @Override
    public boolean addNode(Node x) {

        if (nodes.contains(x))
            return false;

        nodes.add(x);
        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        if(!nodes.contains(x))
            return false;

        nodes.remove(x);
        Iterator<Edge> it = edges.iterator();

        while(it.hasNext()) {
            Edge edge = it.next();
            if(edge.getTo().equals(x))
                it.remove();
        }
        return true;
    }

    @Override
    public boolean addEdge(Edge x) {

        if(edges.contains(x))
            return false;

        edges.add(x);
        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        if(!edges.contains(x))
            return false;

        edges.remove(x);
        return true;
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
