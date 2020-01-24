
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Kenny Akers Mr. Paige Homework #
 *
 */
public class TSPHelper {

    private static int[][] distance;
    private static final SymbolGraph graph = States.graph;

    public static int[][] getDistanceMatrix() {
        int[][] distanceMatrix = new int[graph.vertices()][graph.vertices()];

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                if (graph.getVertex(i).isAdjacent(graph.getVertex(j))) { // If there is a path from i to j
                    int intercityDistance = graph.findEdge(graph.getVertex(i), graph.getVertex(j)).weight();
                    distanceMatrix[i][j] = distanceMatrix[j][i] = intercityDistance;
                } else {
                    distanceMatrix[i][j] = distanceMatrix[j][i] = 0;
                }
            }
        }
        return distanceMatrix;
    }

    public static int distance(SymbolGraph.Vertex from, SymbolGraph.Vertex to) {
        return (int) States.find(from.name()).capital().distance(States.find(to.name()).capital());
    }

    public static void printDistMat() {
        System.out.print("\n    ");
        for (int i = 0; i < graph.vertices(); i++) {
            System.out.print(i + ": " + graph.getVertex(i).name() + "\t");
        }
        System.out.print("\n" + graph.getVertex(0).name() + "| ");
        for (int i = 0; i < distance.length; i++) {
            for (int j = 0; j < distance[0].length; j++) {
                System.out.print(distance[i][j] + "\t");
            }
            if (i < distance.length - 1) {
                System.out.print("\n" + i + ": " + graph.getVertex(i).name() + "| ");
            }
        }
        System.out.println("");
    }

    public static void printTour(ArrayList<SymbolGraph.Vertex> tour) {

        System.out.print("Path (" + tour.size() + "): ");
        for (SymbolGraph.Vertex v : tour) {
            System.out.print(v.name() + " - ");
        }
    }

    public static void printDistMatRaw() {
        for (int i = 0; i < distance.length; i++) {
            for (int j = 0; j < distance[0].length; j++) {
                System.out.print(distance[i][j] + " ");
            }
            System.out.println("");
        }
    }

    private static boolean[] visited;

    public static Array<SymbolGraph.Edge> mst() { // Prim's MST algo
        visited = new boolean[graph.vertices()];
        Array<SymbolGraph.Edge> tree = new Array<>();
        PriorityQueue<SymbolGraph.Edge> pq = new PriorityQueue<>(new EdgeComparator());
        for (SymbolGraph.Edge e : graph.getEdgeList()) {
            pq.add(e);
        }
        visited[0] = true;
        for (SymbolGraph.Edge e : graph.getVertex(0).edges()) { // Add all outgoing edges
            pq.add(e);
        }
        
        boolean proceed = false;
        while (!pq.isEmpty()) {
            SymbolGraph.Edge edge = pq.poll(); // Let E = (v, w)
            tree.append(edge);
            SymbolGraph.Vertex v = edge.from();
            SymbolGraph.Vertex w = edge.to();
            for (SymbolGraph.Edge e : tree) {
                if (e.from().equals(v)) {// If v is already in the tree
                    proceed = true;
                }
            }
            if (!proceed) { // v is already in the tree --> skip
                continue;
            }
            if (!visited[w.index()]) { // If w has not been visited
                visited[w.index()] = true;
                pq.add(edge);
                for (SymbolGraph.Edge e : w.edges()) { // For all edges on w
                    if (!visited[e.to().index()]) {
                        pq.add(graph.findEdge(w, e.to()));
                    }
                }
            }
        }
        return tree;
    }

    private static class EdgeComparator implements Comparator<SymbolGraph.Edge> {

        // Comparator for the PriorityQueue to sort on (based on edge weights).
        // This way, I can store vertices in the queue and still have it order based
        // on their distances from the source.
        @Override
        public int compare(SymbolGraph.Edge e1, SymbolGraph.Edge e2) {
            if (e1.weight() > e2.weight()) {
                return 1;
            }
            return -1;
        }
    }

}
