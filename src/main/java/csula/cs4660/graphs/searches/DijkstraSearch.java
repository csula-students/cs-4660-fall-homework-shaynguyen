package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.*;

/**
 * As name, dijkstra search using graph structure
 */
public class DijkstraSearch implements SearchStrategy {

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        // keep track of the total distance from the source node to the other nodes
        Map<Node, Integer> distances = new HashMap<>();
        // distance from the source node to itself is 0
        distances.put(source, 0);

        // queue that investigate the node with the shortest distance first
        Queue<NodeTuple> queue = new PriorityQueue<>((n1, n2) -> {
            int distance1 = n1.value, distance2 = n2.value;

            if (distance1 < distance2) return -1;
            else if (distance1 == distance2) return 0;
            else return 1;
        });
        // add the source node to the queue, shortest node to source is itself
        queue.add(new NodeTuple(source, source, 0));

        // this is the dijkstra shortest path table
        // the shortest path to the "key" node is from the "value" node
        Map<Node, Node> dijkstraTable = buildDijkstraShortestPathTable(graph, queue, distances);

        return findShortestPath(graph, dijkstraTable, source, dist);
    }

    public Map<Node, Node> buildDijkstraShortestPathTable
            (Graph graph, Queue<NodeTuple> queue, Map<Node, Integer> distances) {

        // dijkstra shortest path table
        Map<Node, Node> table = new HashMap<>();

        // find the shortest path to all the nodes
        while (!queue.isEmpty()) {

            NodeTuple tuple = queue.poll();
            Node current = tuple.key;

            // add an entry to the shortest path table
            // reads: "the closet node to the key is the parent"
            table.put(tuple.key, tuple.parent);

            // update the cost to all the adjacent nodes
            for (Node node : graph.neighbors(current)) {
                // first time seeing this node, sets the distance to it as "infinite"
                if (!distances.containsKey(node))
                    distances.put(node, Integer.MAX_VALUE);

                // add the total (shortest) distance to the current node with the
                // edge value from the current node to this node
                int newDistance = distances.get(current) + graph.distance(current, node);

                // update distance value if the newDistance is shorter
                if (newDistance < distances.get(node)) {
                    distances.put(node, newDistance);

                    // update the value in the queue
                    queue.removeIf(t -> t.key.equals(node));
                    // reads: this node is has the shortest path to the current node
                    queue.add(new NodeTuple(node, current, newDistance));
                }
            }
        }
        return table;
    }

    private List<Edge> findShortestPath
            (Graph graph, Map<Node, Node> shortestPath, Node source, Node dist) {

        List<Edge> result = new ArrayList<>();
        Node goal = new Node(dist.getData());

        // no entry for dist means no path to dist
        if (!shortestPath.containsKey(dist))
            return new ArrayList<>();

        // get the node with the shortest path to dist, and then get node the
        // node with the shortest path to THAT node and so on until we reach
        // the source node. the result is the path from the source to dist
        while (!goal.equals(source)) {
            result.add(new Edge(shortestPath.get(goal), goal,
                    graph.distance(shortestPath.get(goal), goal))
            );
            goal = shortestPath.get(goal);
        }
        Collections.reverse(result);

        return result;
    }

    /**
     * This class holds the node that is closet to the "key" node (based on a source
     * node) and their distance.
     */
    class NodeTuple {
        Node key;
        Node parent;
        int value;

        NodeTuple(Node key, Node parent, int value) {
            this.key = key;
            this.parent = parent;
            this.value = value;
        }
        public String toString() {
            return key + "," + parent + "," + "value ";
        }
    }
}
