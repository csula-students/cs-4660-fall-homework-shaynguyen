package csula.cs4660.graphs.representations;

import com.google.common.collect.*;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.util.*;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 * <p>
 */
public class AdjacencyMatrix implements Representation {
    // using biMap to get node index, much faster than searing for it in a list
    // biMap can do an inverse search, search by the value to get the key
    private BiMap<Node, Integer> nodeIndex;
    private int indexCounter = 0;

    // < row, col, edge value >
    private Table<Integer, Integer, Integer> adjacencyMatrix;

    public AdjacencyMatrix(File file) {
        Map<Node, Set<Edge>> map = GraphHelper.parseFileMap(file);
        nodeIndex = HashBiMap.create();

        for (Node n : map.keySet()) {
            nodeIndex.put(n, indexCounter++);
        }
        adjacencyMatrix = convertToMatrix(map);
    }


    public AdjacencyMatrix() {
        nodeIndex = HashBiMap.create();
        adjacencyMatrix = HashBasedTable.create();
    }

    private Table<Integer, Integer, Integer> convertToMatrix(Map<Node, Set<Edge>> map) {
        // custom table with linkedHashMap preserved insertion order
        Table<Integer, Integer, Integer> matrix =
                Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);

        // insert the edge value into their [row][col]
        for (Node node : nodeIndex.keySet()) {
            for (Edge edge : map.get(node)) {
                matrix.put(
                        nodeIndex.get(edge.getFrom()),
                        nodeIndex.get(edge.getTo()),
                        edge.getValue()
                );
            }
        }

        return matrix;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int row = nodeIndex.get(x);
        int col = nodeIndex.get(y);

        // if the value is NOT null and 0, then an edge exist between the two node
        return adjacencyMatrix.get(row, col) != null &&
                adjacencyMatrix.get(row, col) != 0;
    }

    @Override
    public List<Node> neighbors(Node x) {
        Integer row = nodeIndex.get(x);
        List<Node> result = new ArrayList<>();

        // node doesn't not exist, therefore it has no neighbors
        if (row == null)
            return result;

        // add  x's neighbors to result; 0 indicate no edge value
        adjacencyMatrix.row(row).keySet().forEach(i -> {
            Node node = nodeIndex.inverse().get(i);
            // biMap returns null if there are no match
            if (node != null)
                result.add(node);
        });

        return result;
    }

    @Override
    public boolean addNode(Node x) {
        if (nodeIndex.containsKey(x))
            return false;

        nodeIndex.put(x, indexCounter++);
        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        Integer xIndex = nodeIndex.get(x);

        // Node doesn't exist
        if (xIndex == null)
            return false;

        // remove the node from the list
        nodeIndex.remove(x);

        // remove the row from the table by first getting a map of all the
        // rows entry, then delete it one by one
        Map<Integer, Integer> colEdgeValueMap = adjacencyMatrix.row(xIndex);
        for (Map.Entry<Integer, Integer> entry : colEdgeValueMap.entrySet()) {
            adjacencyMatrix.remove(entry.getKey(), entry.getValue());
        }

        // remove the edge by setting the table[row][col] to 0
        adjacencyMatrix.column(xIndex).keySet().forEach(yIndex -> {
            adjacencyMatrix.put(yIndex, xIndex, 0);
        });

        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        // add the nodes if they don' exist so we can add the edge later
        // if we add the nodes, we have to get the updated row and col indexes
        addNode(x.getFrom());
        addNode(x.getTo());


        // if row/col is NOT null and NOT 0 then it means there is already an
        // edge there
        int row = nodeIndex.get(x.getFrom());
        int col = nodeIndex.get(x.getTo());

        if (adjacencyMatrix.get(row, col) != null
                && adjacencyMatrix.get(row, col) != 0)
            return false;

        // this edge is new, add it to the table
        adjacencyMatrix.put(row, col, x.getValue());

        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        Integer row = nodeIndex.get(x.getFrom());
        Integer col = nodeIndex.get(x.getTo());

        // Do nothing if the nodes doesn't exist or if the edge doesn't exist
        if (row == null || col == null || adjacencyMatrix.get(row, col) == null)
            return false;

        // Remove existing edge
        adjacencyMatrix.remove(row, col);

        return true;
    }

    @Override
    public int distance(Node from, Node to) {
        Integer row = nodeIndex.get(from);
        Integer col = nodeIndex.get(to);

        // nodes doesn't exist, therefore the distance between them is 0
        if (row == null || col == null)
            return 0;

        // return the edge value between this two node, 0 if none exist.
        Integer distance = adjacencyMatrix.get(row, col);
        if (distance == null)
            return 0;

        return distance;
    }

    @Override
    public Optional<Node> getNode(int index) {
        Optional<Node> nodeOptional = Optional.empty();

        for (Node node : nodeIndex.keySet()) {
            if (node.getData().equals(index)) {
                nodeOptional = Optional.of(node);
            }
        }

        return nodeOptional;
    }
}
