
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class mainProgram {
    
    private static SymbolGraph theGraph;

    public static void main(String[] args) throws IOException {
        Benchmark benchmark = new Benchmark();

        benchmark.startMark();
        theGraph = States.graph;

        int[][] distances = TSPHelper.getDistanceMatrix();

        ArrayList<Vertex> unitedList = min_weight_and_unite(TSPHelper.mst(), distances);

        Hierholzer Hierholzer = new Hierholzer(unitedList, distances);
        LinkedList<Vertex> eulerTour = Hierholzer.run();

        ShortCut answer = new ShortCut(eulerTour);
        ArrayList<Vertex> TSP = answer.run();

        double howManyMinutesToRunTSP = 0.1;
        TwoOpt TwoOpt = new TwoOpt(TSP, distances, howManyMinutesToRunTSP);
        TSP = TwoOpt.run();

        FinalAnswer(TSP, distances);
        benchmark.endMark();
        double time = ((benchmark.resultTime() / 60.00) / 1000.00);
        System.out.println("Program took: " + time + " minutes");

    }

    /*
	This splits even and odds from each other and then creates a "perfect matching" (it doesn't actually,
	but attempts something close to), and then reconnects the graph. Please note this creates a Eulerian Multigraph
	which means edges can be connected to each other twice. 2 edges "A-B" "A-B" can exist from the same Vertex.
	 * */
    public static ArrayList<SymbolGraph.Edge> min_weight_and_unite(Array<SymbolGraph.Edge> mst, int[][] distances) {
        ArrayList<SymbolGraph.Edge> oddNumbers = new ArrayList<>();
        for (int i = 0; i < mst.size(); i++) {
            if (mst.get(i).from().neighbors().size() % 2 == 1) {
                oddNumbers.add(mst.get(i));
            }
        }

        //This will create edges between two odd vertices.
        while (oddNumbers.isEmpty() == false) {
            int distance = Integer.MAX_VALUE;
            int indexToRemove = -1;

            for (int i = 1; i < oddNumbers.size(); i++) {
                int checkDistance = distances[oddNumbers.get(0).from().index()][oddNumbers.get(i).from().index()];
                if (checkDistance < distance) {
                    distance = checkDistance;
                    indexToRemove = i;
                }
            }
            SymbolGraph.Edge fromZeroToPairEdge = theGraph.new Edge(oddNumbers.get(indexToRemove).from(), oddNumbers.get(indexToRemove).to());

            SymbolGraph.Edge fromPairEdgeToZero = theGraph.new Edge(oddNumbers.get(indexToRemove).to(), oddNumbers.get(indexToRemove).from());
            oddNumbers.get(0).from().neighbors.fromZeroToPairEdge);
            oddNumbers.get(indexToRemove).connectedVertices.add(fromPairEdgeToZero);

            oddNumbers.remove(indexToRemove);
            oddNumbers.remove(0);

        }

        return MinimumSpanningTree;
    }

    static void FinalAnswer(ArrayList<Vertex> TSP, int[][] distances) {
        try {
            PrintWriter writer = new PrintWriter(("TSP.tour"));
            int totalDistance = 0;

            int lineFormatting = 0;
            for (int i = 0; i < TSP.size() - 1; i++) {

                System.out.print(TSP.get(i).getID() + " ");
                if (lineFormatting == 20) {
                    lineFormatting = 0;
                    System.out.println();
                }
                lineFormatting++;

                totalDistance += distances[TSP.get(i).getID()][TSP.get(i + 1).getID()];
            }
            System.out.println();

            totalDistance += distances[TSP.get(0).getID()][TSP.get(TSP.size() - 1).getID()];
            System.out.println("Total distance covered of the " + TSP.size() + " vertices is: " + totalDistance);

            writer.println(totalDistance);

            for (int i = 0; i < TSP.size(); i++) {
                writer.println(TSP.get(i).getID());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
