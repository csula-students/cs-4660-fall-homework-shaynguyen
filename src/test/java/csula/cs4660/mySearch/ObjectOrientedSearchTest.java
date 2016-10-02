package csula.cs4660.mySearch;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;
import csula.cs4660.graphs.searches.BFS;
import csula.cs4660.graphs.searches.DFS;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ObjectOrientedSearchTest {
    private Graph graph1;
    private Graph graph2;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file1 = new File(classLoader.getResource("homework-1/graph-1.txt").getFile());
        File file2 = new File(classLoader.getResource("homework-1/graph-2.txt").getFile());

        graph1 = new Graph(
                Representation.of(
                        Representation.STRATEGY.OBJECT_ORIENTED,
                        file1
                )
        );

        graph2 = new Graph(
                Representation.of(
                        Representation.STRATEGY.OBJECT_ORIENTED,
                        file2
                )
        );
    }

    // bfs graph 1
    @Test
    public void testGraph1BfsFrom0to7() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(7), 1)
                ),
                graph1.search(new BFS(), new Node(0), new Node(7))
        );
    }

    @Test
    public void testGraph1BfsFrom8to7() {
        assertEquals(
                new ArrayList<>(),
                graph1.search(new BFS(), new Node(8), new Node(7))
        );
    }

    @Test
    public void testGraph1BfsFrom1to8() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(1), new Node(3), 1),
                        new Edge(new Node(3), new Node(10), 1),
                        new Edge(new Node(10), new Node(8), 1)
                ),
                graph1.search(new BFS(), new Node(1), new Node(8))
        );
    }

    @Test
    public void testGraph1BfsFrom0To10() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(10), 1)
                ),
                graph1.search(new BFS(), new Node(0), new Node(10))
        );
    }

    // dfs graph 1
    @Test
    public void testGraph1DfsFrom0to7() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(7), 1)
                ),
                graph1.search(new DFS(), new Node(0), new Node(7))
        );
    }

    @Test
    public void testGraph1DfsFrom8to7() {
        assertEquals(
                new ArrayList<>(),
                graph1.search(new DFS(), new Node(8), new Node(7))
        );
    }

    @Test
    public void testGraph1DfsFrom1to8() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(1), new Node(2), 1),
                        new Edge(new Node(2), new Node(4), 1),
                        new Edge(new Node(4), new Node(5), 1),
                        new Edge(new Node(5), new Node(0), 1),
                        new Edge(new Node(0), new Node(7), 1),
                        new Edge(new Node(7), new Node(8), 1)
                ),
                graph1.search(new DFS(), new Node(1), new Node(8))
        );
    }

    @Test
    public void testGraph1DfsFrom0To10() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(7), 1),
                        new Edge(new Node(7), new Node(10), 1)
                ),
                graph1.search(new DFS(), new Node(0), new Node(10))
        );
    }

    // graph 2 BFS
    @Test
    public void testGraph2BfsFrom0To1() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(1), 4)
                ),
                graph2.search(new BFS(), new Node(0), new Node(1))
        );
    }

    @Test
    public void testGraph2BfsFrom0To5() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(3), 1),
                        new Edge(new Node(3), new Node(5), 11)
                ),
                graph2.search(new BFS(), new Node(0), new Node(5))
        );
    }

    @Test
    public void testGraph2BfsFrom5To0() {
        assertEquals(
                new ArrayList<>(),
                graph2.search(new BFS(), new Node(5), new Node(0))
        );
    }

    // graph 2 DFS
    @Test
    public void testGraph2DfsFrom0To1() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(1), 4)
                ),
                graph2.search(new DFS(), new Node(0), new Node(1))
        );
    }

    @Test
    public void testGraph2DfsFrom0To5() {
        assertEquals(
                Arrays.asList(
                        new Edge(new Node(0), new Node(1), 4),
                        new Edge(new Node(1), new Node(2), 7),
                        new Edge(new Node(2), new Node(5), 2)
                ),
                graph2.search(new DFS(), new Node(0), new Node(5))
        );
    }

    @Test
    public void testGraph2DfsFrom5To0() {
        assertEquals(
                new ArrayList<>(),
                graph2.search(new DFS(), new Node(5), new Node(0))
        );
    }

}
