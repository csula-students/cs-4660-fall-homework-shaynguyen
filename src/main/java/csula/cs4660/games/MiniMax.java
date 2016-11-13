package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class MiniMax {

    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        // TODO: implement minimax to retrieve best move
        // NOTE: you should mutate graph and node as you traverse and update value


        // Locate the root node in the graph
        Node source = graph.getNode(root).get();

        // Arrived to the end, now backtrack up the tree
        if (depth == 0 || graph.neighbors(source).isEmpty()) {
            return source;
        }

        if (max) {
            Integer bestValue = Integer.MIN_VALUE;
            for (Node node : graph.neighbors(source)) {
                Integer value = ((MiniMaxState) getBestMove(graph, node, depth - 1, false).getData()).getValue();
                bestValue = Math.max(bestValue, value);
            }
            updateMinMaxValue(source, bestValue);
            return source;

        } else {
            Integer bestValue = Integer.MAX_VALUE;
            for (Node node : graph.neighbors(source)) {
                Integer value = ((MiniMaxState) getBestMove(graph, node, depth - 1, true).getData()).getValue();
                bestValue = Math.min(bestValue, value);
            }
            updateMinMaxValue(source, bestValue);
            return source;
        }
    }

    private static void updateMinMaxValue(Node<MiniMaxState> node, int value) {
        node.getData().setValue(value);
    }
}
