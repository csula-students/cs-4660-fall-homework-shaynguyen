package csula.cs4660.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The fundamental class to hold data
 * <p>
 * We will be using Generic Programming to hold dynamic type of data --
 * http://www.tutorialspoint.com/java/java_generics.htm
 */
public class Node<T> {

    private final T data;
    private Set<Node> neighbors;

    public Node(T data) {
        this.data = data;
        this.neighbors = new HashSet<>();
    }

    public T getData() {
        return data;
    }

    public boolean addNeighbor(Node node) {
        return neighbors.add(node);
    }

    public boolean removeNeighbor(Node node) {
        return neighbors.remove(node);
    }

    public List<Node> getNeighbors() {
        return new ArrayList<>(neighbors);
    }

    public boolean isNeighbor(Node node) {
        return neighbors.contains(node);
    }
    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Node))
            return false;

        Node<?> node = (Node<?>) o;

        // if data is not null, check if it is the same as the arg data
        // if data IS null, check if both data, this and the arg, are null
        return getData() != null ? getData().equals(node.getData()) : node.getData() == null;

    }

    @Override
    public int hashCode() {
        return getData() != null ? getData().hashCode() : 0;
    }
}
