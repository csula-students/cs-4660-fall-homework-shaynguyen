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


    /**
     * @param file
     * @return return a list of lines(string) of the file. if there is an exception,
     * return null instead
     */
    private List<String> readFile(File file) {
        BufferedReader inputStream;
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
        return null;
    }

    private  Map<Node, Collection<Edge>>  parseLines(List<String> lines) {

        Map<Node, Collection<Edge>> nodeMap = new HashMap<>();

        // ignore first line (it contains # of nodes). start from the second
        for (int i = 1; i < lines.size(); i++) {
            // parse each line [fromNode, toNode, edgeValue]
            String[] result = lines.get(i).split(":");

            // instantiate Node and Edge objects
            Node<Integer> fromNode = new Node<>(Integer.valueOf(result[0]));
            Node<Integer> toNode = new Node<>(Integer.valueOf(result[1]));
            Edge edge = new Edge(fromNode, toNode, Integer.valueOf(result[2]));

            // if map already contains the key, add on to the collection
            // else instantiate a collection
            if (nodeMap.containsKey(fromNode)) {
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
        // x's edges
        Collection<Edge> edges = adjacencyList.get(x);

        // is y in any of the edges?
        for (Edge edge : edges) {
            if (edge.getTo().equals(y))
                return true;
        }
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        // if the node doesn't exist, it doesn't have any neighbors
        if (!adjacencyList.containsKey(x))
            return new ArrayList<>();

        // add all x's neighbors into a list
        Collection<Edge> edges = adjacencyList.get(x);
        List<Node> neighbors = new ArrayList<>();
        for (Edge e : edges) {
            neighbors.add(e.getTo());
        }
        return neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        // node already exist?
        if (adjacencyList.containsKey(x))
            return false;

        // add node and instantiate an Edge Collection
        adjacencyList.put(x, new ArrayList<>());
        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        // does the node exist?
        if (!adjacencyList.containsKey(x))
            return false;

        // remove the node. check if OTHER nodes has an edges with it.
        // if so, remove that edge as well
        adjacencyList.remove(x);
        for (Node node : adjacencyList.keySet()) {
            Iterator<Edge> it = adjacencyList.get(node).iterator();

            while (it.hasNext()) {
                Edge edge = it.next();
                if (edge.getTo().equals(x))
                    it.remove();
            }
        }
        return true;
    }


    @Override
    public boolean addEdge(Edge x) {
        // all the edges that x WOULD be a part of
        Collection<Edge> edges = adjacencyList.get(x.getFrom());

        // is x already an existing edge?
        if (edges.contains(x))
            return false;

        edges.add(x);
        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        // does the fromNode exist?
        Node fromNode = x.getFrom();
        if (!adjacencyList.containsKey(fromNode))
            return false;

        // does the toNode exist?
        Collection<Edge> edges = adjacencyList.get(fromNode);
        if (!edges.contains(x))
            return false;

        // remove the Edge and update the map
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
