package csula.cs4660.graphs.utils;

import com.google.common.base.Stopwatch;
import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A quick parser class to read different format of files
 */
public class Parser {
    private static Stopwatch timer;

    /**
     * Generate a graph data structure from a file. Add Node and Edges to the graph.
     */
    public static Graph readRectangularGridFile(Representation.STRATEGY graphRepresentation, File file) {
        Graph graph = new Graph(Representation.of(graphRepresentation));
        timer = Stopwatch.createStarted();

        // read the file, removes the borders, and then convert the data into Tiles
        Tile[][] grid = convertLinesToTile(removeBordersFromGrid(GraphHelper.readFile(file)));

        // add Nodes and Tiles from the grid into our graph
        for (Tile[] aGrid : grid)
            for (Tile anAGrid : aGrid) {

                if (!anAGrid.getType().startsWith("#")) {
                    Set<Edge> edges = getAdjacentGrid(grid, anAGrid);
                    graph.addNode(new Node<Tile>(anAGrid));
                    edges.forEach(graph::addEdge);
                }
            }

        System.out.println("{FILE: " + file.getName() + ", TIME: " + timer.stop() + "}");
        return graph;
    }

    /**
     * Convert Edges to its corresponding direction "N E W S"
     */
    public static String converEdgesToAction(Collection<Edge> edges) {
        StringBuilder actions = new StringBuilder();
        Tile tile1, tile2;

        for (Edge edge : edges) {
            tile1 = (Tile) edge.getFrom().getData();
            tile2 = (Tile) edge.getTo().getData();

            if (tile1.getY() > tile2.getY())
                actions.append("N");
            else if (tile1.getX() < tile2.getX())
                actions.append("E");
            else if (tile1.getX() > tile2.getX())
                actions.append("W");
            else if (tile1.getY() < tile2.getY())
                actions.append("S");
        }

        return actions.toString();
    }


    /**
     * Return all the neighboring tiles from src that is not an obstacle ("##").
     * If the src tile is itself an obstacle, will return an empty set
     */
    private static Set<Edge> getAdjacentGrid(Tile[][] tiles, Tile src) {
        Set<Edge> result = new HashSet<>();

        if (src.getType().startsWith("#"))
            return new HashSet<>();

        int x = src.getX(), y = src.getY();

        if (x + 1 < tiles[0].length)
            result.add(new Edge(new Node<Tile>(src), new Node<Tile>(tiles[y][x + 1]), 1));
        if (x - 1 >= 0)
            result.add(new Edge(new Node<Tile>(src), new Node<Tile>(tiles[y][x - 1]), 1));
        if (y + 1 < tiles.length)
            result.add(new Edge(new Node<Tile>(src), new Node<Tile>(tiles[y + 1][x]), 1));
        if (y - 1 >= 0)
            result.add(new Edge(new Node<Tile>(src), new Node<Tile>(tiles[y - 1][x]), 1));

        // removes any edges where the tile is an obstacle
        return result.stream().filter(edge ->
                !((Tile) edge.getTo().getData()).getType().startsWith("#")
        ).collect(Collectors.toSet());
    }


    /**
     * Removes the top/bottom borders: first and last line
     * Removes the left/right borders: first and last element of each line
     */
    private static List<String> removeBordersFromGrid(List<String> lines) {
        // remove the first and last line
        List<String> copy = new ArrayList<>(lines);
        copy.remove(0);
        copy.remove(copy.size() - 1);

        // removes the first character and last character of each line
        return copy.stream()
                .map(s -> s.substring(1, s.length() - 1))
                .collect(Collectors.toList());
    }

    /**
     * Convert lines into a 2D array of Tiles where Tile[ROW][COL] will
     * return a Tile with x = COL, y = ROW.
     */
    private static Tile[][] convertLinesToTile(List<String> lines) {
        int numOfRows = lines.size();
        int numOfCols = lines.get(0).length() / 2;
        Tile[][] grid = new Tile[numOfRows][numOfCols];

        // each tile is either "  ", "##" or "@{someNumber}"
        StringBuilder tileType = new StringBuilder();

        // row == y AND col == x
        for (int row = 0; row < lines.size(); row++) {
            // k+=2 because a node is 1 "y" length and 2 "x" length
            for (int col = 0; col < lines.get(row).length(); col += 2) {
                tileType.append(lines.get(row).substring(col, col + 2));
                grid[row][col / 2] = new Tile(col / 2, row, tileType.toString());
                tileType.setLength(0);
            }
        }
        return grid;
    }

}

