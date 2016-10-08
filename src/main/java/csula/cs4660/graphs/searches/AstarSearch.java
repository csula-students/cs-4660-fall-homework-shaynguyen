package csula.cs4660.graphs.searches;

import com.google.common.base.Stopwatch;
import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Perform A* search
 */
public class AstarSearch implements SearchStrategy {
    // used to measure performance of A*
    private Stopwatch timer;
    private Log log = LogFactory.getLog(AstarSearch.class);

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        timer = Stopwatch.createStarted();
        int nodeCounter = 0;

        Map<Node, Integer> gScore = new HashMap<>();
        gScore.put(source, 0);

        Map<Node, Integer> fScore = new HashMap<>();
        fScore.put(source, heuristic(source, dist));

        Map<Node, Node> parents = new HashMap<>();
        parents.put(source, source);

        Queue<Node<Tile>> frontier = new PriorityQueue<Node<Tile>>((n1, n2) -> {
            int n1Score = fScore.get(n1), n2Score = fScore.get(n2);

            if (n1Score < n2Score) {
                return -1;
            } else if (n1Score == n2Score) {
                int g1score = gScore.get(n1);
                int g2score = gScore.get(n2);

                if (g1score < g2score) return 0;
                else if (g1score > g2score) return 7;
                else {
                    if (isNorth(n1, n2)) return 2;
                    else if (isEast(n1, n2)) return 3;
                    else if (isWest(n1, n2)) return 4;
                    else if (isSouth(n1, n2)) return 5;
                    else return 6;
                }

            } else return 7;
        });
        frontier.add(source);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            nodeCounter++;

            if (current.equals(dist)) {
                List<Edge> edges = new ArrayList<>();

                while (!parents.get(dist).equals(dist)) {
                    edges.add(new Edge(parents.get(dist), dist, 1));
                    dist = parents.get(dist);
                }
                Collections.reverse(edges);
                log.info("{GRAPH: " + graph + ", TIME:" + timer.stop() +
                        " , NODES: " + nodeCounter + "}");
                return edges;
            }

            for (Node node : graph.neighbors(current)) {

                // distance from one node to a neighbor node is always
                // 1 in this grid
                int tempGScore = gScore.get(current) + 1;

                if (!gScore.containsKey(node) || tempGScore < gScore.get(node)) {
                    gScore.put(node, tempGScore);
                    fScore.put(node, tempGScore + heuristic(node, dist));
                    frontier.add(node);
                    parents.put(node, current);
                }
            }

        }

        log.error("{Graph: " + graph + ", Time: " + timer.stop() +
                "SOURCE: " + source + ", GOAL: " + dist + "}");
        return new ArrayList<>();
    }


    private int heuristic(Node current, Node dist) {
        Tile srcTile = (Tile) current.getData();
        Tile distTile = (Tile) dist.getData();

        int dx = Math.abs(srcTile.getX() - distTile.getX());
        int dy = Math.abs(srcTile.getY() - distTile.getY());

        return dx + dy;
    }

    private boolean isNorth(Node src, Node goal) {
        Tile a = (Tile) src.getData();
        Tile b = (Tile) goal.getData();
        return a.getY() > b.getY();
    }

    private boolean isSouth(Node src, Node goal) {
        Tile a = (Tile) src.getData();
        Tile b = (Tile) goal.getData();
        return a.getY() < b.getY();
    }

    private boolean isWest(Node src, Node goal) {
        Tile a = (Tile) src.getData();
        Tile b = (Tile) goal.getData();
        return a.getX() > b.getX();
    }

    private boolean isEast(Node src, Node goal) {
        Tile a = (Tile) src.getData();
        Tile b = (Tile) goal.getData();
        return a.getX() < b.getX();
    }
}
