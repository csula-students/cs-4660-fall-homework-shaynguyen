package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

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
        // create logger
        log = LogFactory.getLog(AdjacencyMatrix.class);

        // instantiate filed variables
        List<String> lines = readFile(file);
        parseMatrix(lines);

    }


    public AdjacencyMatrix() {
        // EMPTY
    }

    private List<String> readFile(File file) {
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            String line;

            // read each line and add it into the list of lines
            while ((line = inputStream.readLine()) != null) {
                lines.add(line);
            }

            // everything is good, return the list
            return lines;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        // if there is anything wrong ...
        return null;
    }

    private void parseMatrix(List<String> lines) {
        // ignore the first line, it is the node number
        int size = lines.size() - 1;
        int[][] matrix = new int[size][size];

        int numOfNodes = Integer.valueOf(lines.get(0));
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Node> listNode = new ArrayList<>();


        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            int row = Integer.valueOf(line.split(":")[0]);
            int col = Integer.valueOf(line.split(":")[1]);

            if (map.containsKey(row)) {
                List<Integer> toNodes = map.get(row);
                toNodes.add(col);
                map.put(row, toNodes);
            } else {
                List<Integer> toNodes = new ArrayList<>();
                toNodes.add(col);
                map.put(row, toNodes);
                listNode.add(new Node(row));
            }
        }

        adjacencyMatrix = covertMapToMatrix(map, numOfNodes);
        nodes = new Node[listNode.size()];
        nodes = listNode.toArray(nodes);
    }


    private int[][] covertMapToMatrix(Map<Integer, List<Integer>> map, int numOfNodes) {
        int[][] matrix = new int[numOfNodes][numOfNodes];

        for (Integer key : map.keySet()) {
            List<Integer> toNodes = map.get(key);
            for (Integer toNode : toNodes) {
                matrix[key][toNode] = 1;
            }
        }
        return matrix;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int xData = (int) x.getData();
        int yData = (int) y.getData();

        if (adjacencyMatrix[xData][yData] == 1)
            return true;

        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        List<Node> neighbor = new ArrayList<>();
        int x$ = (int) x.getData();

        for (int i = 0; i < adjacencyMatrix[x$].length; i++) {
            int y = adjacencyMatrix[x$][i];
            if (y == 1)
                neighbor.add(new Node(i));
        }

        log.info(neighbor);
        return neighbor;
    }

    @Override
    public boolean addNode(Node x) {

        if (Arrays.asList(nodes).contains(x))
            return false;

        Node[] nodeArr = new Node[nodes.length + 1];
        for (int i = 0; i < nodes.length - 1; i++) {
            nodeArr[i] = nodes[i];
        }
        nodeArr[nodes.length - 1] = x;
        nodes = nodeArr;
        return true;
    }

    @Override
    public boolean removeNode(Node x) {

        if(!Arrays.asList(nodes).contains(x))
            return false;

        Node[] nodeArr = new Node[nodes.length - 1];
        List<Node> nodeList = new ArrayList<>();

        for(Node n : nodes)
            nodeList.add(n);

        nodeList.remove(x);

        for(int i=0; i < nodeArr.length; i++)
            nodeArr[i] = nodeList.get(i);

        nodes = nodeArr;

        int row = (int) x.getData();
        for(int i=0; i < adjacencyMatrix[row].length; i++)
            adjacencyMatrix[row][i] = 0;


        for(int i=0; i < adjacencyMatrix.length; i++) {
            for(int k=0; k <adjacencyMatrix[i].length; k++) {
                if(k == row){
                    adjacencyMatrix[i][k] = 0;
                }
            }
        }
        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        int $x = (int) x.getFrom().getData();
        int $y = (int) x.getTo().getData();

        if(adjacencyMatrix[$x][$y] == 1)
            return false;

        adjacencyMatrix[$x][$y] = 1;
        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        int $x = (int) x.getFrom().getData();
        int $y = (int) x.getTo().getData();

        if(adjacencyMatrix[$x][$y] == 0)
            return false;

        adjacencyMatrix[$x][$y] = 1;
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
