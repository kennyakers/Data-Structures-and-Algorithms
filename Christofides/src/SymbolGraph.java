
import java.util.Iterator;

public class SymbolGraph implements Iterable<SymbolGraph.Vertex> {
    
    private HashMap<String, Vertex> map;
    private Array<Vertex> vertices;
    private Array<Edge> edges;
    private boolean directed;
    
    public SymbolGraph() {
        this(false);
    }
    
    public SymbolGraph(boolean directed) {
        this.map = new HashMap<>();
        this.vertices = new Array<>();
        this.edges = new Array<>();
        this.directed = directed;
    }
    
    public int vertices() {
        return this.vertices.size();
    }
    
    public int edges() {
        return this.edges.size();
    }
    
    public Array<Edge> getEdgeList() {
        return this.edges;
    }
    
    public Vertex getVertex(int index) {
        return this.vertices.get(index);
    }
    
    public Edge getEdge(int index) {
        return this.edges.get(index);
    }
    
    public Vertex findVertex(String name) {
        Vertex result = this.map.find(name);
        if (result == null) {
            result = new Vertex(name);
            this.vertices.append(result);
        }
        return result;
    }
    
    public Edge findEdge(Vertex from, Vertex to) {
        for (Edge e : this.edges) {
            if (e.equals(from, to)) {
                return e;
            }
        }
        return null;
    }
    
    public Vertex addVertex(String name) {
        return findVertex(name);
    }
    
    public void addEdge(int from, int to) {
        addEdge(this.vertices.get(from), this.vertices.get(to));
    }
    
    public void addEdge(String from, String to) {
        addEdge(findVertex(from), findVertex(to));
    }
    
    public void addEdge(Vertex from, Vertex to) {
        if (!directed) {
            to.neighbor(from);
        }
        from.neighbor(to);
        addEdge(new Edge(to, from));
    }
    
    public void addEdge(Edge e) {
        edges.append(e);
    }
    
    @Override
    public Iterator<Vertex> iterator() {
        return this.vertices.iterator();
    }
    
    public class Vertex {
        
        private List<Vertex> neighbors;
        private List<Edge> edges;
        private String name;
        private int index;
        
        public Vertex(String name) {
            this.neighbors = new SinglyLinkedList<Vertex>();
            this.edges = new SinglyLinkedList<Edge>();
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
            this.edges.prepend(new Edge(this, neighbor));
            SymbolGraph.this.edges.append(new Edge(this, neighbor));
        }
        
        public List<Vertex> neighbors() {
            return this.neighbors;
        }
        
        public List<Edge> edges() {
            return this.edges;
        }
        
        @Override
        public String toString() {
            return this.name + ": " + this.neighbors;
        }
    }
    
    public class Edge {
        
        private Vertex from;
        private Vertex to;
        private int weight;
        
        public Edge(Vertex from, Vertex to) {
            this.from = from;
            this.to = to;
            this.weight = TSPHelper.distance(from, to);
        }
        
        public int weight() {
            return this.weight;
        }
        
        public Vertex from() {
            return this.from;
        }
        
        public Vertex to() {
            return this.to;
        }
        
        public boolean equals(Edge other) {
            return this.equals(other.from, other.to);
        }
        
        public boolean equals(Vertex otherFrom, Vertex otherTo) {
            return this.from.equals(otherFrom) && this.to.equals(otherTo);
        }
        
        @Override
        public String toString() {
            return from.name() + "-" + to.name();
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
