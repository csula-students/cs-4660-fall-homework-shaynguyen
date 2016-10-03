package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.*;

/**
 * Breadth first search
 */
public class BFS implements SearchStrategy {

    // map a node to its most "recent parent" node.
    private Map<Node, Node> parents;

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        // used to iterate neighbors at each level
        Queue<Node> queue = new LinkedList<>();

        // init Map
        parents = new HashMap<>();

        // add source, the root node, to the queue
        queue.add(source);

        // find the parent node of dist
        Node endTile = findGoalInNeighbors(graph, queue, new ArrayList<>(), dist);

        // if no paths exist from source to dist, returns an empty list
        if (endTile == null)
            return new ArrayList<>();
        else
            return getPathToDist(graph, endTile, dist);
    }

    /**
     * @param queue queue with the root node (source node)
     * @return a node that "leads" to the dist node based on the current queue.
     * if the node is null then it means there are no path to "dist"
     */
    private Node findGoalInNeighbors(Graph graph, Queue<Node> queue, List<Node> visited, Node dist) {

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // mark each node as visited, so we don't traverse through it again
            // as another node's neighbor. if the goal is found, return the node
            // that "leads" to "dist" node
            for (Node node : graph.neighbors(current)) {

                if (!visited.contains(node)) {
                    visited.add(node);
                    parents.put(node, current);
                    queue.add(node);

                    if (dist.equals(node)) {
                        return current;
                    }
                }
            }
        }
        // no path exist to "dist'
        return null;
    }


    /**
     * @param endTile the parent node tht points to the goal node
     * @param dist    the goal node
     * @return a list of edges generated from backtracking from the endTile node
     * to the starting starting node.
     */

    private List<Edge> getPathToDist(Graph graph, Node endTile, Node dist) {
        List<Edge> edges = new ArrayList<>();

        // add the edge TO the destination first since we going to backtrack...
        edges.add(new Edge(endTile, dist, graph.distance(endTile, dist)));

        // backtrack "up" the graph, adding each edge to a "result" list
        while (parents.containsKey(endTile)) {
            Node from = parents.get(endTile), to = endTile;

            edges.add(new Edge(from, to, graph.distance(from, to)));
            endTile = parents.get(endTile);
        }

        // backtrack starts from goal to start, thus we need to reverse it
        Collections.reverse(edges);

        return edges;
    }
}
