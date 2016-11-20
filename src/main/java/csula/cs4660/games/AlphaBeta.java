package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class AlphaBeta {
    public static Node getBestMove(Graph graph, Node root, Integer depth, Integer alpha, Integer beta, Boolean max) {
        /* Locate node from the graph */
        Node source = graph.getNode(root).get();

        /* Arrived end of tree, time to back track */
        if (depth == 0 || graph.neighbors(source).isEmpty())
            return source;

        /* Evaluated the successors, explore the subtree */
        Integer bestValue = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        Integer myAlpha = alpha;
        Integer myBeta = beta;

        for (Node node : graph.neighbors(source)) {
            Integer value = minMaxValue(getBestMove(graph, node, depth - 1, myAlpha, myBeta, !max));
            bestValue = getBestValue(value, bestValue, max);

            /* Update alpha or beta values */
            if (max)
                myAlpha = Math.max(bestValue, myAlpha);
            else
                myBeta = Math.min(bestValue, myBeta);

            /* Pruning: Nodes are prune when "beta" <= "alpha"
            *  no need to check the rest of the subtree */
            if (myBeta <= myAlpha)
                break;
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
