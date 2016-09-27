package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by eric on 9/24/16.
 */
public class DFS implements SearchStrategy {

    Log log = LogFactory.getLog(DFS.class);

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        // used to iterate neighbors neighbors neighbors etc ...
        Stack<Node> stack = new Stack<>();

        // add source, the root node, to the stack
        source.setVisited(true);
        stack.push(source);

        // from the source node in the stack, getPathToDist and find a solution if any.
        return getPathToDist(graph, stack, new ArrayList<>(), dist);
    }

    private List<Edge> getPathToDist(Graph graph, Stack<Node> stack,
                                     List<Edge> history, Node dist) {

        // did not find a solution, return an empty list
        if (stack.isEmpty())
            return new ArrayList<>();

        // get the node on top of the stack and get all its neighbors
        Node current = stack.peek();
        List<Node> neighbors = graph.neighbors(current);


        // if the node is the goal, add it to history and return history
        // otherwise recursively check its node neighbor
        for (Node node : neighbors) {
            if (node.equals(dist)) {
                history.add(new Edge(current, node, graph.distance(current, node)));
                return history;
            } else {

                // nodes can share neighbor nodes, ignore the visited ones
                if (!node.isVisited()) {
                    stack.add(node);

                    // if the node have a neighbor, the dist could be it, otherwise
                    // no neighbor indicate no path for this route.
                    List<Edge> historyCopy = new ArrayList<>(history);
                    if (!graph.neighbors(node).isEmpty()) {
                        historyCopy.add(new Edge(current, node, graph.distance(current, node)));
                    }

                    // getPathToDist  "deeper" into the graph to find the dist node
                    return getPathToDist(graph, stack, historyCopy, dist);
                }
            }
        }

        // there are no (more) neighbors. time to backtrack. set node visited
        // to true so we don't visit it again as another node's neighbor.
        stack.pop().setVisited(true);
        return getPathToDist(graph, stack, history, dist);
    }
}
