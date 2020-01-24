
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #23
 * 4/27/18
 */
public class IsTopologicalSort {

    public static void main(String[] args) {
        SymbolGraph graph = SymbolGraph.read(true);
        Array<String> visited = new Array<>();
        boolean verdict = true;
        for (String arg : args) { // For each inputted vertex...
            visited.append(arg); // Add it to the order 
            for (SymbolGraph.Vertex vertex : graph.find(arg).neighbors()) { // For each of this vertex's neighbors...
                if (visited.contains(vertex.name())) {
                    /*  
                    If the inputted sort list already has one of this vertex's neighbors
                    then we know that it came before this vertex, which is not a valid 
                    topographical sort order. This is because a neighbor (i.e. a destination node) 
                    cannot come before its source node (the node from which we came).
                     */
                    verdict = false; // Not a valid order.
                    break; // Done.
                }
            }
        }
        System.out.println(verdict);
    }

}
