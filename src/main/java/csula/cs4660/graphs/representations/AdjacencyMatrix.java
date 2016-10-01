package csula.cs4660.graphs.representations;

import com.google.common.collect.Multimap;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 * <p>
 * TODO: please fill the method body of this class
 */
public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;
    private Log log;

    public AdjacencyMatrix(File file) {
        log = LogFactory.getLog(AdjacencyMatrix.class);

        List<String> lines = GraphHelper.readFile(file);
        Multimap<Node, Edge> multimap = GraphHelper.parseLines(lines);

        // https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#toArray--
        nodes = multimap.keySet().stream().toArray(Node[]::new);

        adjacencyMatrix = convertToMatrix(
                multimap,
                Integer.valueOf(lines.get(0))
        );
    }

    public AdjacencyMatrix() {
        // EMPTY
    }

    private int[][] convertToMatrix(Multimap<Node, Edge> multimap, int nodes) {
        int[][] matrix = new int[nodes][nodes];

        // Because every edges (multimap.values) contain data from two nodes,
        // an adjacency matrix  can be created by using each node data as
        // row & column number and assign that [row][col] element a value of 1
        // to indicate an edge. By default everything else in the matrix is 0.
        for (Edge edge : multimap.values()) {
            int row = (int) edge.getFrom().getData();
            int col = (int) edge.getTo().getData();
            matrix[row][col] = edge.getValue();
        }

        return matrix;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int row = (int) x.getData();
        int col = (int) y.getData();

        // if the value is not zer0, then an edge exist  between the two node
        return adjacencyMatrix[row][col] != 0;
    }

    @Override
    public List<Node> neighbors(Node x) {
        int row = (int) x.getData();

        // http://stackoverflow.com/questions/18552005/is-there-a-concise-way-to-iterate-over-a-stream-with-indices-in-java-8
        // Look at every col in the same row as "X". filter if its value != 0,
        return IntStream.range(0, adjacencyMatrix.length)
                .filter(col -> adjacencyMatrix[row][col] != 0)
                .boxed().map(col -> new Node(col)).collect(Collectors.toList());
    }

    @Override
    public boolean addNode(Node x) {
        // Don't add existing Node.
        if (Arrays.asList(nodes).contains(x))
            return false;

        // Create a "bigger" array and copy its all the old content.
        Node[] copy = new Node[nodes.length + 1];
        System.arraycopy(nodes, 0, copy, 0, nodes.length);

        // Add the new content and update "nodes"
        copy[nodes.length] = x;
        nodes = copy;

        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        // Node doesn't exist
        if (!Arrays.asList(nodes).contains(x))
            return false;

        // Create  new copy of nodes but exclude "x"
        nodes = Arrays.stream(nodes).filter(n -> !n.equals(x))
                .toArray(Node[]::new);

        // all edge to "X" set to 0, all edge FROM "x" set to 0
        int removed = (int) x.getData();
        for (int index = 0; index < adjacencyMatrix.length; index++) {
            adjacencyMatrix[removed][index] = 0;
            adjacencyMatrix[index][removed] = 0;
        }

        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        int row = (int) x.getFrom().getData();
        int col = (int) x.getTo().getData();

        // Do nothing if edge already exist
        if (adjacencyMatrix[row][col] == 1)
            return false;

        // Add Edge
        adjacencyMatrix[row][col] = 1;

        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        int row = (int) x.getFrom().getData();
        int col = (int) x.getTo().getData();

        // Do nothing if edge is non-existing
        if (adjacencyMatrix[row][col] == 0)
            return false;

        // Remove exiting edge
        adjacencyMatrix[row][col] = 0;

        return true;
    }

    @Override
    public int distance(Node from, Node to) {
        int row = (int) from.getData(), col = (int) to.getData();

        // return the edge from between this two node, 0 if none exist.
        // will throw out of bound exception
        return adjacencyMatrix[row][col];
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
