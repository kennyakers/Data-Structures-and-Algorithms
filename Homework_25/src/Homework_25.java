
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Kenny Akers Mr. Paige Homework #25 5/13/18
 */
public class Homework_25 {

    private static SymbolGraph graph;
    private static SymbolGraph.Vertex source;
    private static int[] distTo;
    private static SymbolGraph.Vertex[] pred;
    private static PriorityQueue<SymbolGraph.Vertex> pq;
    private static Stack<SymbolGraph.Vertex> stack;

    public static void main(String[] args) {
        graph = States.graph;
        distTo = new int[graph.vertices()];
        pred = new SymbolGraph.Vertex[graph.vertices()];
        source = graph.find(args[0]); // The first state is the source vertex.
        pq = new PriorityQueue<>(new VertexComparator());
        stack = new Stack<>();
        dijkstra();

        for (int i = 1; i < args.length; i++) { // Go through the destination vertices.
            if (States.find(args[i]) == null) { // Destination does not exist
                System.out.print(args[i] + " is not a state. ");
                if (i == args.length - 1) { // If last one
                    System.out.println("Ending.");
                } else { // There are still more destinations
                    System.out.println("Ignoring.");
                }
                continue;
            }
            SymbolGraph.Vertex destination = graph.find(args[i]);
            System.out.print(source.name() + " to " + destination.name() + " is " + distTo[destination.index()] + " miles: ");
            SymbolGraph.Vertex currentVertex = destination;
            while (!currentVertex.equals(source)) {
                stack.push(currentVertex);
                currentVertex = pred[currentVertex.index()];
            }
            System.out.print(source.name());
            while (!stack.isEmpty()) {
                System.out.print(", " + stack.pop().name());
            }
            System.out.println("");

        }
    }

    private static void relaxEdge(SymbolGraph.Vertex from, SymbolGraph.Vertex to) {
        int distance = distTo[from.index()] + distance(from, to);
        if (distTo[to.index()] > distance) { // Found a better weight
            distTo[to.index()] = distance;
            pred[to.index()] = from;
            pq.add(to); // Add updates the value if pq already contains it, so no need to check with contains().
        }
    }

    private static void relaxVertex(SymbolGraph.Vertex vertex) {
        for (SymbolGraph.Vertex neighbor : vertex.neighbors()) {
            relaxEdge(vertex, neighbor);
        }
    }

    private static int distance(SymbolGraph.Vertex from, SymbolGraph.Vertex to) {
        return (int) States.find(from.name()).capital().distance(States.find(to.name()).capital());
    }

    private static void dijkstra() {
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Integer.MAX_VALUE;
        }
        distTo[source.index()] = 0;
        pq.add(source);
        while (!pq.isEmpty()) {
            SymbolGraph.Vertex v = pq.poll();
            relaxVertex(v);
        }
    }

    private static class VertexComparator implements Comparator<SymbolGraph.Vertex> {

        // Comparator for the PriorityQueue to sort on (based on edge weights).
        // This way, I can store vertices in the queue and still have it order based
        // on their distances from the source.
        @Override
        public int compare(SymbolGraph.Vertex destination1, SymbolGraph.Vertex destination2) {
            if (Homework_25.distance(Homework_25.source, destination1) > Homework_25.distance(Homework_25.source, destination2)) {
                return 1;
            }
            return -1;
        }
    }
}
