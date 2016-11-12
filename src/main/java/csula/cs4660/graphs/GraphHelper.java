package csula.cs4660.graphs;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import csula.cs4660.graphs.representations.Representation;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by shay on 9/11/16.
 */
public class GraphHelper {

    /**
     * @param file
     * @return return a list of lines(string) of the file. if there is an exception,
     * return null instead
     */
    public static List<String> readFile(File file) {
        try {
            // https://github.com/google/guava/wiki/IOExplained
            return Files.asCharSource(file, Charsets.UTF_8).readLines();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Node, Set<Edge>> parseLinesMap(List<String> lines) {
        // each node is map to a list of edges, some node may contain an empty list
        Map<Node, Set<Edge>> map = new HashMap<>();

        // first line of file indicates the number of nodes in the graph (index 0)
        // example: 11 indicates 10 nodes from 0 - 10.
        int nodesNumber = Integer.valueOf(lines.get(0));

        // initialize the map with empty list
        for (int i = 0; i < nodesNumber; i++) {
            map.put(new Node(i), new LinkedHashSet<>());
        }

        // parse the text and add the edges into their corresponding list
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(":");
            Edge edge = new Edge(values[0], values[1], values[2]);

            // use the "from" node to retrieve the key, and add the entire edge to
            // the value pair
            map.get(edge.getFrom()).add(edge);
        }

        return map;
    }

    public static Map<Node, Set<Edge>> parseFileMap(File file) {
        List<String> lines = readFile(file);
        return parseLinesMap(lines);
    }
}
