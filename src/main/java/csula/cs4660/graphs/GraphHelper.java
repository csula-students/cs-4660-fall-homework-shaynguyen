package csula.cs4660.graphs;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by shay on 9/11/16.
 */
public class GraphHelper {

    /**
     * @param file
     * @return return a list of lines(string) of the file. if there is an exception,
     * return null instead
     */
    public static  List<String> readFile(File file) {
        try {
            // https://github.com/google/guava/wiki/IOExplained
            return Files.asCharSource(file, Charsets.UTF_8).readLines();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param lines
     * @return Return a parsed AdjacencyLit
     */
    public static  Multimap<Node, Edge> parseLines(List<String> lines) {
        Multimap<Node, Edge> multimap = ArrayListMultimap.create();
        // Starts on the line because the first line list the number of nodes.
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(":");
            Edge edge = new Edge(values[0], values[1], values[2]);
            multimap.put(edge.getFrom(), edge);
        }
        return multimap;
    }

    public static  Multimap<Node, Edge> parseLines(File file) {
        List<String> lines = readFile(file);
        return parseLines(lines);
    }
}
