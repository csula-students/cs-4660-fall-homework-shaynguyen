package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class MiniMax {

    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        // TODO: implement minimax to retrieve best move
        // NOTE: you should mutate graph and node as you traverse and update value

        /* Locate node from the graph */
        Node source = graph.getNode(root).get();

        /* Arrived end of tree, time to back track */
        if (depth == 0 || graph.neighbors(source).isEmpty()) {
            return source;
        }

        /* Evaluated the successors, explore the subtree */
        Integer bestValue = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Node node : graph.neighbors(source)) {
            Integer value = minMaxValue (getBestMove(graph, node, depth - 1, !max));
            bestValue = getBestValue(value, bestValue, max);
        }

        /* Mutate the nodes */
        updateMinMaxValue(source, bestValue);
        return source;
    }

    /**
     * Retrieve Node's MinMaxState value. For conveniences and readability
     */
    private static int minMaxValue(Node node) {
        return ((MiniMaxState) node.getData()).getValue();
    }

    /**
     * Setters for Node's MinMaxState value field. For conveniences and readability
     */
    private static void updateMinMaxValue(Node<MiniMaxState> node, int value) {
        node.getData().setValue(value);
    }

    /*
     * Calculate the max or min value depending on whose turn (boolean) it is
     */
    private static int getBestValue(int value, int bestValue, boolean max) {
        return max ? Math.max(value, bestValue) : Math.min(value, bestValue);
    }
}
