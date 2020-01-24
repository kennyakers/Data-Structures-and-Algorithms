
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
        Vertex result = this.map.find(name);
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
            SymbolGraph.this.map.add(name, this);
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

        public String toString() {
            return this.name + ": " + this.neighbors;
        }
    }

    public static SymbolGraph read(boolean directed) {
        SymbolGraph graph = new SymbolGraph(directed);

        String line = Input.readString();
        while (line != null) {
            int comma = line.indexOf(',');
            if (comma >= 0) {
                String from = line.substring(0, comma);
                String to = line.substring(comma + 1);
                graph.addEdge(from, to);
            } else {
                graph.addVertex(line);
            }
            line = Input.readString();
        }
        return graph;
    }
}
