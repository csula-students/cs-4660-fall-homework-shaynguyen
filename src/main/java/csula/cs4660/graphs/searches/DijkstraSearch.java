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
        // this is the dijkstra shortest path table
        // Read: "the shortest path to the 'key' node is from the 'value' node"
        Map<Node, Node> dijkstraTable = buildDijkstraTable(graph, source);

        // using the dijkstra table, look up the shortest path from source to dist
        return findShortestPath(graph, dijkstraTable, source, dist);
    }

    /**
     * Build a table with the shortest path to a node based on the source node.
     * for example: (Node-X -> Node-Y) , (Node-Y -> Node-Src),
     * (Node-Src -> Node-Src) indicates that the shortest path to
     * Node-X is Node-Y, and the shortest path to Node-Y is the source node.
     */
    public Map<Node, Node> buildDijkstraTable(Graph graph, Node source) {
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

        // dijkstra shortest path table
        Map<Node, Node> table = new HashMap<>();

        // find the shortest path to all the nodes
        while (!queue.isEmpty()) {
            NodeTuple nodeTuple = queue.poll();
            Node current = nodeTuple.key;

            // add an entry to the shortest path table
            // reads: "the shortest path to the key node is the distance from
            // the source node to the parent node"
            table.put(nodeTuple.key, nodeTuple.parent);

            // update the cost to all the adjacent nodes
            for (Node neighbor : graph.neighbors(current)) {
                // first time seeing this node, sets the distance to it as "infinite"
                if (!distances.containsKey(neighbor))
                    distances.put(neighbor, Integer.MAX_VALUE);

                // add the total (shortest) distance to the current node with the
                // distance value from the current node to this neighbor node
                int newDistance = distances.get(current) + graph.distance(current, neighbor);

                // update distance value if the newDistance is shorter
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);

                    // update the value in the queue by first removing the tuple
                    // then adding a similar tuple but with the new distance
                    queue.removeIf(tuple -> tuple.key.equals(neighbor));
                    queue.add(new NodeTuple(neighbor, current, newDistance));
                }
            }
        }
        return table;
    }

    /**
     * Use the dijkstra table able to look up the shortest path from the
     * source Node to the dist Node. If there are no path, the method will
     * return an empty list  instead.
     */
    private List<Edge> findShortestPath(
            Graph graph, Map<Node, Node> dijkstraTable, Node source, Node dist) {

        List<Edge> result = new ArrayList<>();
        Node goal = new Node(dist.getData());

        // no entry for dist means no path to dist
        if (!dijkstraTable.containsKey(dist))
            return new ArrayList<>();

        // get the node with the shortest path to dist, and then get node the
        // node with the shortest path to THAT node and so on until we reach
        // the source node. the result is the path from the source to dist
        while (!goal.equals(source)) {
            Node from = dijkstraTable.get(goal);
            result.add(new Edge(from, goal, graph.distance(from, goal)));
            goal = dijkstraTable.get(goal);
        }
        // the result will be reversed because we starts from the end to the root
        Collections.reverse(result);

        return result;
    }

    /**
     * This class holds the node that is closet to the "key" node (based on a source
     * node) and their distance.
     */
    private class NodeTuple {
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
