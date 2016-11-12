package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Depth first search
 */
public class DFS implements SearchStrategy {

    Log log = LogFactory.getLog(DFS.class);
    Map<Node, Node> parents;

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        // used to iterate neighbors neighbors neighbors etc ...
        Stack<Node> stack = new Stack<>();

        // init parents
        parents = new HashMap<>();

        // add source, the root node, to the stack
        stack.push(source);

        // from the source node in the stack, getPathToDist and find a solution if any.
        Optional<Node> goal = findGoal(graph, stack, new ArrayList<>(), dist);

        // if the goal is found, return a list of edge to the goal from the source
        if (goal.isPresent()) return build(graph, goal.get());
        else return new ArrayList<>();
    }


    public Optional<Node> findGoal(Graph graph, Stack<Node> stack, List<Node> visited, Node dist) {
        Optional<Node> nodeOptional = Optional.empty();

        // depth fully explored, did not find goal
        if (stack.isEmpty())
            return nodeOptional;

        // get all the neighbors of the node on top of the stack
        Node current = stack.peek();
        List<Node> neighbors = graph.neighbors(current);


        // if a node is the dist, then return that node else recursively search
        // each node neighbors
        for (Node node : neighbors) {
            if (node.equals(dist)) {
                parents.put(node, current);
                return nodeOptional.of(node);
            } else if (!visited.contains(node)) {
                stack.add(node);
                parents.put(node, current);
                return findGoal(graph, stack, visited, dist);
            }
        }

        // the current node has no neighbor or you explored them all, remove it
        // from the stack and try another node
        visited.add(stack.pop());
        return findGoal(graph, stack, visited, dist);
    }


    public List<Edge> build(Graph graph, Node goal) {
        List<Edge> edges = new ArrayList<>();

        // backtrack and add the edges
        while (parents.get(goal) != null) {
            edges.add(
                    new Edge(parents.get(goal), goal, graph.distance(parents.get(goal), goal)));
            goal = parents.get(goal);
        }

        // because we build this list while back tracking, we need to reverse it
        Collections.reverse(edges);
        return edges;
    }

}
