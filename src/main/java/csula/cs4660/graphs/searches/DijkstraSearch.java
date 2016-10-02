package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;

import java.io.File;
import java.util.*;

/**
 * Created by eric on 9/24/16.
 */
public class DijkstraSearch implements SearchStrategy {

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {

        // keep track of distance form the source node to any node
        Map<Node, Integer> distances = new HashMap<>();
        distances.put(source, 0);

        // value = the node closet to the key. if node is not in this map,
        // no paths exist to that node.
        Map<Node, Node> shortestPath = new HashMap<>();

        // investigate the node with the shortest distance first
        Queue<NodeTuple> queue = new PriorityQueue<>((n1, n2) -> {
            int distance1 = n1.value, distance2 = n2.value;

            if (distance1 < distance2) return -1;
            else if (distance1 == distance2) return 0;
            else return 1;
        });
        // default value
        queue.add(new NodeTuple(source, source, 0));

        // find the shortest path to all the nodes
        while (!queue.isEmpty()) {

            NodeTuple tuple = queue.poll();
            Node current = tuple.key;

            shortestPath.put(tuple.key, tuple.parent);

            // update the cost to all the adjacent nodes
            for (Node node : graph.neighbors(current)) {

                // first time seeing this node, sets the distance to it as "infinite"
                if (!distances.containsKey(node))
                    distances.put(node, Integer.MAX_VALUE);

                // the total shortest distance to the "current" node
                int totalDistanceToCurrent = distances.get(current);
                // the distance between the two nodes
                int edgeValue = graph.distance(current, node);
                // the total distance from the source node to this one
                int newDistance = edgeValue + totalDistanceToCurrent;

                // update distance value if the newDistance is shorter
                if (newDistance < distances.get(node)) {
                    distances.put(node, newDistance);

                    // update the value in the queue
                    queue.removeIf(t -> t.key.equals(node));
                    queue.add(new NodeTuple(node, current, newDistance));
                }
            }
        }

        if (!shortestPath.containsKey(dist)) {
            return new ArrayList<>();
        } else {
            List<Edge> result = new ArrayList<>();
            Node dog = dist;
            while (!dog.equals(source)) {
                result.add(new Edge(shortestPath.get(dog), dog, 1));
                dog = shortestPath.get(dog);
            }
            Collections.reverse(result);
            return result;
        }
    }

    public static void main(String[] args) {
        File file = new File("/Users/shay/cs4660-fall-2016/src/main/resources/graph-3.txt");
        Graph graph = new Graph(Representation.of(Representation.STRATEGY.ADJACENCY_LIST, file));
        System.out.println(
                graph.search(new DijkstraSearch(), new Node(6), new Node(7))
        );
    }

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
