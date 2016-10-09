package csula.cs4660.graphs.representations;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
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
    private List<Node> nodes;
    // < row, col, edge value >
    private Table<Integer, Integer, Integer> adjacencyMatrix;

    public AdjacencyMatrix(File file) {
        Map<Node, Set<Edge>> map = GraphHelper.parseFileMap(file);
        nodes = new ArrayList<>(map.keySet());
        adjacencyMatrix = convertToMatrix(map);
    }


    public AdjacencyMatrix() {
        nodes = new ArrayList<>();
        adjacencyMatrix = HashBasedTable.create();
    }

    private Table<Integer, Integer, Integer> convertToMatrix(Map<Node, Set<Edge>> map) {
        // custom table with linkedHashMap preserved insertion order
        Table<Integer, Integer, Integer> matrix =
                Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);

        // insert the edge value into their [row][col]
        for (Node node : nodes) {
            for (Edge edge : map.get(node)) {
                matrix.put(
                        nodes.indexOf(edge.getFrom()),
                        nodes.indexOf(edge.getTo()),
                        edge.getValue()
                );
            }
        }

        return matrix;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int row = nodes.indexOf(x);
        int col = nodes.indexOf(y);

        // if the value is NOT null and 0, then an edge exist between the two node
        return adjacencyMatrix.get(row, col) != null &&
                adjacencyMatrix.get(row, col) != 0;
    }

    @Override
    public List<Node> neighbors(Node x) {
        int row = nodes.indexOf(x);

        List<Node> result = new ArrayList<>();

        // node doesn't not exist, therefore it has no neighbors
        if (row == -1)
            return result;

        // add  x's neighbors to result; 0 indicate no edge value
        adjacencyMatrix.row(row).keySet().forEach(i -> {
            result.add(nodes.get(i));
        });


        return result;
    }

    @Override
    public boolean addNode(Node x) {
        // Don't add existing Node.
        if (nodes.indexOf(x) > -1)
            return false;

        // Add node to list
        nodes.add(x);

        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        int xIndex = nodes.indexOf(x);

        // Node doesn't exist
        if (xIndex == -1)
            return false;

        // remove the node from the list
        nodes.remove(xIndex);

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
        int row = nodes.indexOf(x.getFrom());
        int col = nodes.indexOf(x.getTo());

        if (row == -1) {
            addNode(x.getFrom());
            row = nodes.size() - 1;
        }
        if (col == -1) {
            addNode(x.getTo());
            col = nodes.size() - 1;
        }

        // if row/col is NOT null and NOT 0 then it means there is already an
        // edge there
        if (adjacencyMatrix.get(row, col) != null
                && adjacencyMatrix.get(row, col) != 0)
            return false;

        // this edge is new, add it to the table
        adjacencyMatrix.put(row, col, x.getValue());

        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        int row = nodes.indexOf(x.getFrom());
        int col = nodes.indexOf(x.getTo());

        // Do nothing if the nodes doesn't exist or if the edge doesn't exist
        if (row == 1 || col == -1 || adjacencyMatrix.get(row, col) == null)
            return false;

        // Remove existing edge
        adjacencyMatrix.remove(row, col);

        return true;
    }

    @Override
    public int distance(Node from, Node to) {
        int row = nodes.indexOf(from);
        int col = nodes.indexOf(to);

        // nodes doesn't exist, therefore the distance between them is 0
        if (row == -1 || col == -1)
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

        for (Node node : nodes) {
            if (node.getData().equals(index)) {
                nodeOptional = Optional.of(node);
            }
        }

        return nodeOptional;
    }
}
