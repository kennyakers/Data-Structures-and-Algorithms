
import java.util.HashMap;
import java.util.Iterator;

public class SymbolGraph implements Iterable<SymbolGraph.Vertex> {

    private HashMap<String, Vertex> map;
    private Array<Vertex> vertices;
    private boolean directed;
    private int edges;

    public SymbolGraph() {
        this(false);
    }

    public SymbolGraph(boolean directed) {
        this.map = new HashMap<String, Vertex>();
        this.vertices = new Array<Vertex>();
        this.directed = directed;
        this.edges = 0;
    }

    public int vertices() {
        return this.vertices.size();
    }

    public int edges() {
        return this.edges;
    }

    public Vertex get(int index) {
        return this.vertices.get(index);
    }

    public Vertex find(String name) {
        Vertex result = this.map.get(name);
        if (result == null) {
            result = new Vertex(name);
            this.vertices.append(result);
        }
        return result;
    }

    public Vertex addVertex(String name) {
        return find(name);
    }

    public void addEdge(int from, int to) {
        addEdge(this.vertices.get(from), this.vertices.get(to));
    }

    public void addEdge(String from, String to) {
        addEdge(find(from), find(to));
    }

    public void addEdge(Vertex from, Vertex to) {
        if (!directed) {
            to.neighbor(from);
        }
        from.neighbor(to);
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this.vertices.iterator();
    }

    public class Vertex {

        private List<Vertex> neighbors;
        private String name;
        private int index;

        public Vertex(String name) {
            this.neighbors = new SinglyLinkedList<Vertex>();
            this.index = SymbolGraph.this.map.size();
            SymbolGraph.this.map.put(name, this);
            this.name = name;
        }

        public int index() {
            return this.index;
        }

        public String name() {
            return this.name;
        }

        public int degree() {
            return this.neighbors.size();
        }

        public boolean isAdjacent(Vertex to) {
            return this.neighbors.contains(to);
        }

        public void neighbor(Vertex neighbor) {
            this.neighbors.prepend(neighbor);
            SymbolGraph.this.edges++;
        }

        public List<Vertex> neighbors() {
            return this.neighbors;
        }

        @Override
        public String toString() {
            return this.name + ": " + this.neighbors;
        }
    }

    private static SymbolGraph buildUS2() {
        SymbolGraph graph = new SymbolGraph(false);
        for (State state : States.states) {
            for (State neighbor : state.neighbors()) {
                graph.addEdge(graph.find(state.code()), graph.find(neighbor.code()));
            }
        }
        return graph;
    }

    
    private static SymbolGraph buildUS() {
        SymbolGraph graph = new SymbolGraph(false);
        for (State state : States.states) {
            if (state.equals(States.AK) || state.equals(States.HI)) {
                continue; // Ignore Alaska and Hawaii. Just looking at the continental US for this homework.
            }
            if (!graph.vertices.contains(graph.find(state.name()))) { // If the graph doesn't already have it, add it
                graph.addVertex(state.name());
            }
            for (State neighbor : state.neighbors()) {
                if (state.equals(States.AK) || state.equals(States.HI)) {
                    continue; // Ignore Alaska and Hawaii. Just looking at the continental US for this homework.
                }
                graph.addEdge(state.name(), neighbor.name());
            }
        }
        return graph;
    }

    private static SymbolGraph buildCanada() {
        SymbolGraph graph = new SymbolGraph(false);
        for (State province : Canada.provinces) {
            if (!graph.vertices.contains(graph.find(province.name()))) { // If the graph doesn't already have it, add it
                graph.addVertex(province.name());
            }
            for (State neighbor : province.neighbors()) {
                graph.addEdge(province.name(), neighbor.name());
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        SymbolGraph graph = buildUS();
        GraphProperties graphProps = new GraphProperties(graph);
        System.out.println("Diameter: " + graphProps.diameter());
        System.out.println("Radius: " + graphProps.radius());
        System.out.println("Center: " + graphProps.center().name());
        System.out.println("Wiener Index: " + graphProps.wienerIndex());
    }
}
