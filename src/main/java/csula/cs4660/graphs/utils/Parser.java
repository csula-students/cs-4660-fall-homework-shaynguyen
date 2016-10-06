package csula.cs4660.graphs.utils;

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
    public static Graph readRectangularGridFile(Representation.STRATEGY graphRepresentation, File file) {
        Graph graph = new Graph(Representation.of(graphRepresentation));
        // TODO: implement the rectangular file read and add node with edges to graph

        Tile[][] grid = convertLinesToTile(formatGridLines(GraphHelper.readFile(file)));

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                List<Edge> edges = getAdjacentGrid(grid, grid[row][col]);

                graph.addNode(new Node(grid[row][col]));
                for (Edge e : edges)
                    graph.addEdge(e);
            }
        }
        return graph;
    }

    static List<Edge> getAdjacentGrid(Tile[][] tiles, Tile src) {
        List<Edge> result = new ArrayList<>();

        int x = src.getX(), y = src.getY();


        if (x + 1 < tiles[0].length) {
            result.add(new Edge(new Node(src), new Node(tiles[y][x + 1]), 1));
        }
        if (x - 1 >= 0) {
            result.add(new Edge(new Node(src), new Node(tiles[y][x - 1]), 1));
        }
        if (y + 1 < tiles.length) {
            result.add(new Edge(new Node(src), new Node(tiles[y + 1][x]), 1));
        }
        if (y - 1 >= 0) {
            result.add(new Edge(new Node<Tile>(src), new Node(tiles[y - 1][x]), 1));
        }
        return result.stream().filter(e -> {
            Tile fromTile = (Tile) e.getFrom().getData();
            Tile toTile = (Tile) e.getTo().getData();

            return !fromTile.getType().equals("##") && !toTile.getType().equals("##");
        }).collect(Collectors.toList());
    }

    public static String converEdgesToAction(Collection<Edge> edges) {
        // TODO: convert a list of edges to a list of actionkk
        String actions = "";

        for (Edge edge : edges) {
            Tile src = (Tile) edge.getFrom().getData();
            Tile adjacent = (Tile) edge.getTo().getData();

            if (src.getX() > adjacent.getX())
                actions += "W";
            else if (src.getX() < adjacent.getX())
                actions += "E";
            else if (src.getY() > adjacent.getY())
                actions += "N";
            else if (src.getY() < adjacent.getY())
                actions += "S";
        }
        return actions;
    }


    static List<String> formatGridLines(List<String> lines) {
        // remove the first and last line
        List<String> copy = new ArrayList<>(lines);
        copy.remove(0);
        copy.remove(copy.size() - 1);

        // removes the first character and last character of each line
        return copy.stream()
                .map(s -> s.substring(1, s.length() - 1))
                .collect(Collectors.toList());
    }

    static Tile[][] convertLinesToTile(List<String> lines) {
        int numOfRows = lines.size();
        int numOfCols = lines.get(0).length() / 2;

        Tile[][] result = new Tile[numOfRows][numOfCols];

        for (int i = 0; i < lines.size(); i++) {

            for (int k = 0; k < lines.get(i).length(); k += 2) {
                String s = "";
                s += lines.get(i).charAt(k);
                s += lines.get(i).charAt(k + 1);

                result[i][k / 2] = new Tile(k / 2, i, s);
            }
        }
        return result;
    }

    public File getFile(String filename) {
        return new File(getClass().getClassLoader().getResource(filename).getFile());
    }

    public static void main(String[] args) {
        Tile[][] tiles = convertLinesToTile(formatGridLines(GraphHelper.readFile(new Parser().getFile("grid-shay.txt"))));
        System.out.println(tiles[1][4]);

        System.out.println(getAdjacentGrid(tiles, tiles[1][4]));

    }


}

