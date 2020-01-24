
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #22
 * 4/20/18
 */
public class BreadthFirstPaths {

    private SymbolGraph graph; // The graph to BFS on
    private boolean[] visited;  // Boolean array that denotes whether a vertex has been visited by BFS.
    private int[] distTo;      // Least number of edges to a neighbor vertex for each vertex

    /**
     * Computes the shortest path between the source vertex and every other
     * vertex in the graph.
     *
     * Much of this class is taken from the BreadthFirstPaths.java in
     * Sedgewick's Algorithms
     *
     * @param graph the graph
     * @param start the vertex to BFS from
     */
    public BreadthFirstPaths(SymbolGraph graph, SymbolGraph.Vertex start) {
        this.graph = graph;
        this.visited = new boolean[graph.vertices()];
        this.distTo = new int[graph.vertices()];
        this.BFS(start);
    }

    private void BFS(SymbolGraph.Vertex start) {
        ListQueue<SymbolGraph.Vertex> q = new ListQueue<>(); // Queue for Breadth First Search
        for (int v = 0; v < this.graph.vertices(); v++) {
            this.distTo[v] = Integer.MAX_VALUE; // Fill with max values
        }
        this.distTo[start.index()] = 0; // Distance to itself is 0
        this.visited[start.index()] = true; // We've visited it
        q.enqueue(start); // Put it on the queue of visited vertices
        SymbolGraph.Vertex vertex = null;
        while (!q.isEmpty()) {
            try { // Try-catch block because Queue.dequeue() throws an UnderflowException
                vertex = q.dequeue();
            } catch (Queue.UnderflowException ex) {
                System.out.println("Underflow exception, returning.");
                break;
            }
            for (SymbolGraph.Vertex neighbor : vertex.neighbors()) {
                if (!visited[neighbor.index()]) {
                    this.distTo[neighbor.index()] = distTo[vertex.index()] + 1;
                    this.visited[neighbor.index()] = true;
                    q.enqueue(neighbor);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex {@code s} (or sources) and
     * vertex {@code v}?
     *
     * @param vertex the vertex
     * @return true if there is a path, and false otherwise
     */
    public boolean hasPathTo(SymbolGraph.Vertex vertex) {
        return visited[vertex.index()];
    }

    /**
     * Returns the number of edges in a shortest path between the source vertex
     * and vertex
     *
     * @param vertex the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(SymbolGraph.Vertex vertex) {
        return distTo[vertex.index()];
    }

    public boolean isConnected() {
        boolean connected = true;
        for (int vertex = 0; vertex < this.graph.vertices(); vertex++) {
            if (!visited[vertex]) {
                connected = false;
                break;
            }
        }
        return connected;
    }
}
