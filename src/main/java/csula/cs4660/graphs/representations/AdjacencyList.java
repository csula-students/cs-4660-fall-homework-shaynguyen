package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.*;
import java.util.*;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 * <p>
 * TODO: please implement the method body
 */
public class AdjacencyList implements Representation {
    private Map<Node, Collection<Edge>> adjacencyList;

    public AdjacencyList(File file) {
        List<String> lines = readFile(file);
        adjacencyList = parseLines(lines);
    }

    public AdjacencyList() {
        // Empty
    }

    public Map<Node, Collection<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    private List<String> readFile(File file) {
        BufferedReader inputStream = null;
        List<String> lines = new ArrayList<>();

        try {
            inputStream = new BufferedReader(new FileReader(file));
            String l;

            while ((l = inputStream.readLine()) != null) {
                lines.add(l);
            }

            return lines;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if there is an exception return null;
        return null;
    }

    private Map<Node, Collection<Edge>> parseLines(List<String> lines) {

        // return value
        Map<Node, Collection<Edge>> nodeMap = new HashMap<Node, Collection<Edge>>();

        // the first line indicate the numbers of nodes, so we can ignore it and
        // starts on the the second, index = 1
        for (int i = 1; i < lines.size(); i++) {
            // parse each line [from, to, value]
            String[] result = lines.get(i).split(":");

            // instantiate Node and Edge objects
            Node<Integer> fromNode = new Node<Integer>(Integer.valueOf(result[0]));
            Node<Integer> toNode = new Node<Integer>(Integer.valueOf(result[1]));
            Edge edge = new Edge(fromNode, toNode,  Integer.valueOf(result[2]));

            // if map already contains the key, add on to the collection
            // else instantiate a collection
            if(nodeMap.containsKey(fromNode)) {
                Collection<Edge> edges = nodeMap.get(fromNode);
                edges.add(edge);
                nodeMap.put(fromNode, edges);
            } else {
                Collection<Edge> edges = new ArrayList<Edge>();
                edges.add(edge);
                nodeMap.put(fromNode, edges);
            }
        }

        return nodeMap;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        return null;
    }

    @Override
    public boolean addNode(Node x) {
        return false;
    }

    @Override
    public boolean removeNode(Node x) {
        return false;
    }

    @Override
    public boolean addEdge(Edge x) {
        return false;
    }

    @Override
    public boolean removeEdge(Edge x) {
        return false;
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
