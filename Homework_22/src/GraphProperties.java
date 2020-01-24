
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #22
 * 4/20/18
 */
public class GraphProperties {

    private final SymbolGraph graph; // The graph
    private int diameter; // Definition: Maximum eccentricity of a graph
    private int radius; // Definition: Minimum eccentricity of a graph
    private int wienerIndex; // Definition: The sum of the lengths of the shortest paths between all pairs of vertices
    private SymbolGraph.Vertex center; // Definition: A vertex whose eccentricity is the radius
    private final int numComponents; // The number of components in this graph

    private boolean[] visited;
    private int[] eccentricities;

    public GraphProperties(SymbolGraph graph) {
        this.graph = graph;
        this.visited = new boolean[graph.vertices()];
        this.eccentricities = new int[graph.vertices()];
        this.numComponents = this.countComponents();

        if (!this.computeEccentricities()) { // If the graph is not connected, return.
            return;
        }

        this.diameter = this.computeDiameter();
        this.radius = this.computeRadius();
        this.center = this.computeCenter();
        this.wienerIndex = this.computeWienerIndex();
    }

    private boolean computeEccentricities() {
        for (int i = 0; i < this.graph.vertices(); i++) {
            BreadthFirstPaths bfp = new BreadthFirstPaths(this.graph, this.graph.get(i));
            if (!bfp.isConnected()) {
                System.out.println("Graph is not connected");
                return false;
            }
            int eccentricity = 0;
            for (int h = 0; h < this.graph.vertices(); h++) {
                if (bfp.hasPathTo(this.graph.get(h)) && bfp.distTo(this.graph.get(h)) > eccentricity) {
                    eccentricity = bfp.distTo(this.graph.get(h));
                }
            }
            this.eccentricities[i] = eccentricity;
        }
        return true;
    }

    private int computeWienerIndex() {
        for (int vertex = 0; vertex < graph.vertices(); vertex++) {
            BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, this.graph.get(vertex));
            for (int otherVertex = 0; otherVertex < graph.vertices(); otherVertex++) {
                if (otherVertex != vertex) {
                    this.wienerIndex += breadthFirstPaths.distTo(this.graph.get(otherVertex));
                }
            }
        }
        return this.wienerIndex;
    }

    private int countComponents() {
        int count = 0;
        for (int v = 0; v < this.graph.vertices(); v++) {
            if (!this.visited[v]) {
                this.DFS(this.graph.get(v));
                count++;

            }
        }
        return count;
    }

    private void DFS(SymbolGraph.Vertex start) {
        if (!this.visited[start.index()]) {
            this.visited[start.index()] = true;
            for (SymbolGraph.Vertex v : start.neighbors()) {
                this.DFS(v);
            }
        }
    }

    private int computeDiameter() {
        int diam = this.eccentricities[0];
        for (int i = 1; i < this.eccentricities.length; i++) {
            if (this.eccentricities[i] > diam) {
                diam = this.eccentricities[i];
            }
        }
        return diam;
    }

    private int computeRadius() {
        int rad = this.eccentricities[0];
        for (int i = 1; i < this.eccentricities.length; i++) {
            if (this.eccentricities[i] < rad) {
                rad = this.eccentricities[i];
            }
        }
        return rad;
    }

    private SymbolGraph.Vertex computeCenter() {
        for (int i = 0; i < this.eccentricities.length; i++) {
            if (this.radius == this.eccentricities[i]) {
                return this.graph.get(i);
            }
        }
        return null;
    }

    public int diameter() {
        return this.diameter;
    }

    public int radius() {
        return this.radius;
    }

    public SymbolGraph.Vertex center() {
        return this.center;
    }

    public int wienerIndex() {
        return this.wienerIndex;
    }

    public int numberComponents() {
        return this.numComponents;
    }

}
