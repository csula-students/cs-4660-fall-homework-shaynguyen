package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.util.*;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 */
public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;

    public AdjacencyMatrix(File file) {

        Map<Node, Set<Edge>> map = GraphHelper.parseFileMap(file);

        // https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#toArray--
        nodes = map.keySet().stream().toArray(Node[]::new);
        adjacencyMatrix = convertToMatrix(map);
    }

    public AdjacencyMatrix() {
        nodes = new Node[0];
        adjacencyMatrix = new int[0][0];
    }

    private int[][] convertToMatrix(Map<Node, Set<Edge>> map) {
        int[][] matrix = new int[map.size()][map.size()];

        // get all the edge object into a single list
        List<Edge> edges = new ArrayList<>();
        map.values().forEach(edges::addAll);

        // insert the edge value into their [row][col]
        for (Edge edge : edges) {
            int row = (int) edge.getFrom().getData();
            int col = (int) edge.getTo().getData();

            matrix[row][col] = edge.getValue();
        }
        return matrix;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int row = findIndexOfNode(x);
        int col = findIndexOfNode(y);

        // if the value is not zer0, then an edge exist  between the two node
        return adjacencyMatrix[row][col] != 0;
    }

    @Override
    public List<Node> neighbors(Node x) {
        int row = findIndexOfNode(x);
        List<Node> result = new ArrayList<>();

        // node doesn't not exist, therefore it has no neighbors
        if (row == -1)
            return result;

        // add  x's neighbors to result; 0 indicate no edge value
        for (int col = 0; col < adjacencyMatrix[row].length; col++) {
            if (adjacencyMatrix[row][col] != 0)
                result.add(nodes[col]);
        }


        return result;
    }

    @Override
    public boolean addNode(Node x) {
        // Don't add existing Node.
        if (findIndexOfNode(x) > -1)
            return false;

        // Create a "bigger" array and copy its all the old content.
        Node[] copy = new Node[nodes.length + 1];
        System.arraycopy(nodes, 0, copy, 0, nodes.length);

        // Add the new content and update "nodes"
        nodes = copy;
        copy[nodes.length - 1] = x;

        // create a bigger matrix and copy the cold content onto it
        int[][] copyMatrix = new int[adjacencyMatrix.length + 1][adjacencyMatrix.length + 1];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, copyMatrix[i], 0, adjacencyMatrix[i].length);
        }
        adjacencyMatrix = copyMatrix;


        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        int index = findIndexOfNode(x);

        // Node doesn't exist
        if (index == -1)
            return false;

        // Create  new copy of nodes but exclude "x"
        Node[] copy = new Node[nodes.length - 1];
        for (int i = 0, k = 0; i < nodes.length; i++) {
            // don't copy this one, this is x...
            if (i == index) continue;

            // copy everything else!
            copy[k++] = nodes[i];
        }

        // create a copy of matrix but excludes "X"
        int[][] copyMatrix = new int[adjacencyMatrix.length - 1][adjacencyMatrix.length - 1];

        for (int i = 0, row = 0; i < adjacencyMatrix.length; i++) {
            // don't copy this one, this is the node we are removing
            if (i == index) continue;

            for (int k = 0, col = 0; k < adjacencyMatrix[i].length; k++) {
                // this is the edge value of the node we are removing
                if (k == index) continue;

                copyMatrix[row][col++] = adjacencyMatrix[i][k];
            }

            // update row here instead of loop init b/c we use "continue" to skip
            // therefore it as to be down here.
            row++;
        }

        // update our class variable
        nodes = copy;
        adjacencyMatrix = copyMatrix;

        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        int row = findIndexOfNode(x.getFrom());
        int col = findIndexOfNode(x.getTo());

        // add the nodes if they don' exist so we can add the edge later
        if (row == -1)
            addNode(x.getFrom());
        if (col == -1)
            addNode(x.getTo());

        // if we add the nodes, we have to get the updated row and col indexes
        if (row == -1 || col == -1) {
            row = findIndexOfNode(x.getFrom());
            col = findIndexOfNode(x.getTo());
        }

        if (adjacencyMatrix[row][col] != 0)
            return false;

        // Add Edge
        adjacencyMatrix[row][col] = x.getValue();

        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        int row = findIndexOfNode(x.getFrom());
        int col = findIndexOfNode(x.getTo());

        // Do nothing if the nodes doesn't exist or if the edge doesn't exist
        if (row == 1 || col == -1 || adjacencyMatrix[row][col] == 0)
            return false;


        // Remove exiting edge
        adjacencyMatrix[row][col] = 0;

        return true;
    }

    @Override
    public int distance(Node from, Node to) {
        int row = findIndexOfNode(from);
        int col = findIndexOfNode(to);

        // nodes doesn't exist, therefore the distance between them is 0
        if (row == -1 || col == -1)
            return 0;

        // return the edge from between this two node, 0 if none exist.
        return adjacencyMatrix[row][col];
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

    private int findIndexOfNode(Node x) {
        // -1 indicates node not found.
        int index = -1;

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].equals(x))
                index = i;
        }

        return index;
    }

    @Override
    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = Arrays.asList(nodes).iterator();
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
