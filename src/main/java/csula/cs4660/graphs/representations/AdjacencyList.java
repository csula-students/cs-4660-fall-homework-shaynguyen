package csula.cs4660.graphs.representations;

import com.google.common.collect.Multimap;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.GraphHelper;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 * <p>
 * TODO: please implement the method body
 */
public class AdjacencyList  implements Representation {
    private Multimap<Node, Edge> adjacencyList;
    private Log log;


    public AdjacencyList(File file) {
        log = LogFactory.getLog(AdjacencyList.class);
        adjacencyList = GraphHelper.parseLines(file);
    }

    public AdjacencyList() {
        // Empty
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        // Check if any of edges x belongs to connects to Node y.
        // #Guava #Functional_Programming
        return adjacencyList.get(x).stream()
                .anyMatch(e -> e.getTo().equals(y));
    }

    @Override
    public List<Node> neighbors(Node x) {
        // Apply getTo() to each edges and return the result, which should be all
        // x's neighbors. Taking advantage of Guava and functional programming.
        return adjacencyList.get(x).stream().map(e -> e.getTo())
                .collect(Collectors.toList());
    }

    @Override
    public boolean addNode(Node x) {
        // Don't add existing Node (returns false). Else add it (returns true)
        return adjacencyList.containsKey(x) ? false :  adjacencyList.put(x, null);
    }

    @Override
    public boolean removeNode(Node x) {
        // Stop if the node doesn't exist
        if (!adjacencyList.containsKey(x))
            return false;

        // Remove the node and its values.
        adjacencyList.removeAll(x);

        /* Delete edges that are connected to "x". For 1:2:1, 1:3:1, 2:1:1
         * 1st it.next() returns {key:1, value:{to:1, from:2, value:1}}
         * 2nd it.next() returns {key:1, value:{to:1, from:3, value:1}}
         * 3rd it.next() returns {key:2, value:{to:2, from:1, value:1}}
         * and so on...
        */
        Iterator<Map.Entry<Node, Edge>> it = adjacencyList.entries().iterator();
        while (it.hasNext()) {
            Map.Entry<Node, Edge> entry = it.next();
            Edge edge = entry.getValue();

            if (edge.getTo().equals(x))
                it.remove();
        }

        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        // Don't add existing edge.
        if (adjacencyList.get(x.getFrom()).contains(x))
            return false;

        // Every new node have null, which means no edges.
        // Remove null now there is an edge
        adjacencyList.remove(x.getFrom(), null);

        return adjacencyList.put(x.getFrom(), x);
    }

    @Override
    public boolean removeEdge(Edge x) {
        // https://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Multimap.html
        // If key doesn't exist (fromNode), false. no change?->false, yes? -> true
        return adjacencyList.remove(x.getFrom(), x);
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
