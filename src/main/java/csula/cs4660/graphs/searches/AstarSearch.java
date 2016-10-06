package csula.cs4660.graphs.searches;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;
import csula.cs4660.graphs.utils.Parser;

import java.io.File;
import java.util.*;

/**
 * Perform A* search
 */
public class AstarSearch implements SearchStrategy {

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {

        Map<Node, Integer> gScore = new HashMap<>();
        gScore.put(source, 0);

        Map<Node, Integer> fScore = new HashMap<>();
        fScore.put(source, heuristic(source, dist));

        Map<Node, Node> parents = new HashMap<>();
        parents.put(source, source);

        List<Node> visited = new ArrayList<>();

        Queue<Node<Tile>> frontier = new PriorityQueue<Node<Tile>>((n1, n2) -> {
            int n1Score = fScore.get(n1), n2Score = fScore.get(n2);
            int x1 = n1.getData().getX();
            int x2 = n2.getData().getX();
            int y1 = n1.getData().getY();
            int y2 = n2.getData().getY();

            if (n1Score < n2Score) {
                return -1;
            }
            else if (n1Score == n2Score) {
                if(y1 < y2) return -1;
                else if(y1 > y2) return 1;
                else if(x1 < x2) return -1;
                else if(x1 > x2) return 1;
                return 0;
            }
            else return 1;

        });
        frontier.add(source);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            visited.add(current);

            System.out.println("CURRENT: " + current);
            for(Node n : frontier) {
                System.out.print("\t" + n + ":" +  fScore.get(n) + " | ") ;
            }
            System.out.println();

            if (current.equals(dist)) {
                List<Edge> edges = new ArrayList<>();

                while (!parents.get(dist).equals(dist)) {
                    edges.add(new Edge(parents.get(dist), dist, 1));
                    dist = parents.get(dist);
                }
                Collections.reverse(edges);
                return edges;
            }

            for (Node node : graph.neighbors(current)) {

                int tempGScore = gScore.get(current) + graph.distance(current, node);

                if (!gScore.containsKey(node) || tempGScore < gScore.get(node)) {
                    gScore.put(node, tempGScore);
                    fScore.put(node, tempGScore + heuristic(node, dist));
                    frontier.add(node);
                    parents.put(node, current);
                }
            }

        }

        System.out.println("NO SOLUTION");
        return new ArrayList<>();
    }


    public int heuristic(Node source, Node dist) {
        Tile srcTile = (Tile) source.getData();
        Tile distTile = (Tile) dist.getData();

        int dx = Math.abs(srcTile.getX() - distTile.getX());
        int dy = Math.abs(srcTile.getY() - distTile.getY());

        return  dx + dy;
    }


    public static void main(String[] args) {

        Graph graph;

//         graph = Parser.readRectangularGridFile(
//                Representation.STRATEGY.ADJACENCY_LIST,
//                new File("/Users/shay/cs4660-fall-2016/src/main/resources/grid-1.txt"));
//
//
//        System.out.println(
//                Parser.converEdgesToAction(
//                        graph.search(
//                                new AstarSearch(),
//                                new Node<>(new Tile(3, 0, "@1")),
//                                new Node<>(new Tile(4, 4, "@6")))
//                ));

        graph = Parser.readRectangularGridFile(
                Representation.STRATEGY.ADJACENCY_LIST,
                new File("/Users/shay/cs4660-fall-2016/src/main/resources/grid-shay.txt"));


        System.out.println(
                Parser.converEdgesToAction(
                        graph.search(
                                new AstarSearch(),
                                new Node<>(new Tile(0, 2, "@1")),
                                new Node<>(new Tile(1, 0, "@2")))
                ));


//        graph = Parser.readRectangularGridFile(
//                Representation.STRATEGY.ADJACENCY_LIST,
//                new File("/Users/shay/cs4660-fall-2016/src/main/resources/grid-3.txt"));
//
//
//        System.out.println(
//                Parser.converEdgesToAction(
//                        graph.search(
//                                new AstarSearch(),
//                                new Node<>(new Tile(3, 0, "@1")),
//                                new Node<>(new Tile(2, 7, "@2")))
//                ));

//        graph = Parser.readRectangularGridFile(
//                Representation.STRATEGY.ADJACENCY_LIST,
//                new File("/Users/shay/cs4660-fall-2016/src/main/resources/grid-4.txt"));
//
//
//        System.out.println(
//                Parser.converEdgesToAction(
//                        graph.search(
//                                new AstarSearch(),
//                                new Node<>(new Tile(4, 0, "@1")),
//                                new Node<>(new Tile(6, 201, "@4")))
//                ));

//        graph = Parser.readRectangularGridFile(
//                Representation.STRATEGY.ADJACENCY_LIST,
//                new File("/Users/shay/cs4660-fall-2016/src/main/resources/grid-5.txt"));
//
//
//        System.out.println(
//                Parser.converEdgesToAction(
//                        graph.search(
//                                new AstarSearch(),
//                                new Node<>(new Tile(4, 0, "@1")),
//                                new Node<>(new Tile(6, 201, "@4")))
//                ));
    }
}
