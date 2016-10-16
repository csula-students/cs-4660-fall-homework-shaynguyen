package csula.cs4660.quizes;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;
import csula.cs4660.quizes.models.State;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.*;

/**
 * Here is your quiz entry point and your app
 */
public class App {

    Log log = LogFactory.getLog(App.class);

    public App() {

    }

    public Graph buildGraph(String srcID) {
        log.info("Building graph...");

        State src = Client.getState(srcID).get();

        // everything in this queue will be added to the graph
        Graph graph = new Graph(Representation.of(Representation.STRATEGY.ADJACENCY_LIST));
        Queue<State> graphQueue = new LinkedList<>();
        graphQueue.add(src);

        // used to avoid adding duplicates nodes to the queue
        Set<State> visited = new HashSet<>();
        visited.add(src);

        // keep track of the numbers of nodes created, should be 983
        int nodeCounter = 1;

        while (!graphQueue.isEmpty()) {
            State current = graphQueue.poll();
            current = Client.getState(current.getId()).get();
            System.out.println("node_counter: " + nodeCounter++);

            // wrap states into nodes, add it to the graph with its edges as well
            Node from = new Node<>(current);
            graph.addNode(from);
            for (State neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    graphQueue.add(neighbor);
                    visited.add(neighbor);
                    Node to = new Node<>(neighbor);

                    neighbor = Client.getState(neighbor.getId()).get();
                    graph.addEdge(new Edge(from, to, getDistance(current, neighbor)));
                }
            }
        }

        log.info("Graph complete...");
        return graph;
    }

    private List<Edge> BFS(Graph graph, String srcID, String destID) {
        State src = Client.getState(srcID).get();
        State dest = Client.getState(destID).get();

        // place state inside the node containers
        Node<State> srcNode = new Node<>(src);
        Node<State> destNode = new Node<>(dest);

        // key=child, value=parent's of child
        Map<Node, Node> parents = new HashMap<>();

        // use queue for BFS and add the source node to it
        Queue<Node> queue = new LinkedList<>();
        queue.add(srcNode);

        // the states you already visited
        Set<Node> visited = new HashSet<>();

        //  bfs search here
        while (!queue.isEmpty()) {
            // add the poped element to visited so we don't revisited again
            Node<State> current = queue.poll();
            visited.add(current);

            // check each neighbor for the dest or add them to the queue if possible
            for (Node neighbor : graph.neighbors(current)) {

                // keep a record of the parents-child combo; use for backtracking
                if (!visited.contains(neighbor)) {
                    parents.put(neighbor, current);
                    queue.add(neighbor);
                }

                // return the first path found
                if (isEqualState(neighbor, destNode)) {
                    System.out.println("FOUND BFS");
                    return constructPath(graph, parents, neighbor, destNode);
                }
            }
        }
        // empty when found no path to this (you screwed up)
        System.out.println("DID NOT FIND BFS");
        return new ArrayList<>();
    }

    private List<Edge> constructPath(Graph graph, Map<Node, Node> parents, Node srcNode, Node destNode) {
        List<Edge> path = new ArrayList<>();

        while (parents.containsKey(destNode)) {
            Node from = parents.get(destNode), to = destNode;
            path.add(new Edge(from, to, graph.distance(from, to)));
            destNode = parents.get(destNode);
        }

        Collections.reverse(path);
        return path;
    }

    private boolean isEqualState(Node a, Node b) {
        State x = (State) a.getData();
        State y = (State) b.getData();

        return x.getId().equals(y.getId());
    }


    public List<Edge> dijkstraSearch(Graph graph, String srcID, String destID) {
        State src = Client.getState(srcID).get();
        State dest = Client.getState(destID).get();

        // wrap the state inside nodes class
        Node srcNode = new Node<>(src);
        Node destNode = new Node<>(dest);

        // keep track of the distance from src node, to the key (node)
        Map<Node, Integer> gScore = new HashMap<>();
        gScore.put(srcNode, 0);

        // value= node is the parent of key= node
        Map<Node, Node> parents = new HashMap<>();
        parents.put(srcNode, srcNode);

        // prioritize the node that came from an edge with the higher event effect
        Queue<Node> queue = new PriorityQueue<>((a, b) -> {
            int score1 = gScore.get(a);
            int score2 = gScore.get(b);

            if (score1 > score2) return -1;
            else if (score1 == score2) return 0;
            else return 1;
        });
        queue.add(srcNode);

        // DIJKSTRA / A*
        while (!queue.isEmpty()) {
            Node current = queue.poll();


            if (isEqualState(current, destNode)) {
                System.out.println("FOUND IT");
                return findShortestPath(graph, parents, current, destNode);
            }

            for (Node neighbor : graph.neighbors(current)) {

                int tempGScore = gScore.get(current) + graph.distance(current, neighbor);

                if (!gScore.containsKey(neighbor) || tempGScore > gScore.get(neighbor)) {
                    gScore.put(neighbor, tempGScore);
                    parents.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // did not find a solution with Dijkstra
        return new ArrayList<>();
    }


    private List<Edge> findShortestPath(Graph graph, Map<Node, Node> parents, Node srcNode, Node destNode) {

        List<Edge> result = new ArrayList<>();

        System.out.println("Constructing a path...");

        while (!parents.get(destNode).equals(destNode)) {
            Node from = parents.get(destNode);
            result.add(new Edge(from, destNode, graph.distance(from, destNode)));
            destNode = parents.get(destNode);
        }
        // the result will be reversed because we starts from the end to the root
        Collections.reverse(result);

        System.out.println("Found a solution for dijkstra: " + result.size());
        return result;
    }


    public int getDistance(State from, State neighbor) {
        return Client.stateTransition(from.getId(), neighbor.getId()).get().getEvent().getEffect();
    }

    private void printPath(List<Edge> path) {
        for (Edge edge : path) {
            State from = (State) edge.getFrom().getData();
            State to = (State) edge.getTo().getData();
            System.out.println("{fromID: " + from.getId() + ", fromLoc: "
                    + from.getLocation().getName() + ", toID: " +
                    to.getId() + ",  toLoc: " +
                    to.getLocation().getName() + " : " + edge.getValue()
                    + "}");
        }
    }


    public static void main(String[] args) throws IOException {
        App app = new App();
        String srcID = "10a5461773e8fd60940a56d2e9ef7bf4";
        String destID = "e577aa79473673f6158cc73e0e5dc122";

        // parse data from server
        Graph graph = app.buildGraph(srcID);

        // BFS
        List<Edge> path1 = app.BFS(graph, srcID, destID);
        app.printPath(path1);

        // DIJKSTRA
        List<Edge> path2 = app.dijkstraSearch(graph, srcID, destID);
        app.printPath(path2);
    }
}
