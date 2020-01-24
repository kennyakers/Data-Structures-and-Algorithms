
import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #26
 * 5/30/18
 */
public class Homework_26 {

    public static ArrayList<City> initialRoute;
    private static boolean verbose = false;

    public static void main(String[] args) {

        Console console = System.console();
        if (console == null) {
            Scanner sc = new Scanner(System.in); // Use scanner instead
            // Get number of generations.
            System.out.println("Number of generations to breed: ");
            GeneticAlgo.NUM_GENERATIONS = sc.nextInt();

            // Get size of population.
            System.out.println("Size of population: ");
            GeneticAlgo.POPULATION_SIZE = sc.nextInt();

            // Get mutation rate
            System.out.println("Mutation rate: ");
            GeneticAlgo.MUTATION_RATE = sc.nextDouble();

            System.out.println("Tournament selection size: ");
            GeneticAlgo.TOURNAMENT_SELECTION_SIZE = sc.nextInt();

            // Get number of elite routes
            System.out.println("Number of elite routes: ");
            GeneticAlgo.NUM_ELITE_ROUTES = sc.nextInt();

            System.out.println("Print routes (will disable timing)? (true/false): ");
            verbose = sc.nextBoolean();
        } else { // Use the console/terminal
            // Get number of generations.
            String line = console.readLine("Number of generations to breed: ").trim();
            String response = getCommand(line);
            GeneticAlgo.NUM_GENERATIONS = Integer.parseInt(response);

            // Get size of population.
            line = console.readLine("Size of population: ").trim();
            response = getCommand(line);
            GeneticAlgo.POPULATION_SIZE = Integer.parseInt(response);

            // Get mutation rate
            line = console.readLine("Mutation rate: ").trim();
            response = getCommand(line);
            GeneticAlgo.MUTATION_RATE = Double.parseDouble(response);

            line = console.readLine("Tournament selection size: ").trim();
            response = getCommand(line);
            GeneticAlgo.TOURNAMENT_SELECTION_SIZE = Integer.parseInt(response);

            // Get number of elite routes
            line = console.readLine("Number of elite routes: ").trim();
            response = getCommand(line);
            GeneticAlgo.NUM_ELITE_ROUTES = Integer.parseInt(response);

            line = console.readLine("Print routes (will disable timing)? (true/false): ").trim();
            response = getCommand(line);
            verbose = Boolean.parseBoolean(response);
        }

        initialRoute = new ArrayList<>();
        for (State s : States.states48) { // Add all the states (including DC) to the initial route
            initialRoute.add(s.capital());

        }
        double start = System.currentTimeMillis();

        Population population = new Population(GeneticAlgo.POPULATION_SIZE, initialRoute); // Create a population based on this initial route.
        population.sortRoutesByFitness(); // Sort them by fitness (ascending)
        GeneticAlgo algo = new GeneticAlgo(initialRoute);
        int numGenerations = 0;
        if (verbose) {
            printHeading(numGenerations++);
            printPopulation(population);
        }

        while (numGenerations < GeneticAlgo.NUM_GENERATIONS) { // Iterate through generations, evolving each time.
            if (verbose) {
                printHeading(++numGenerations); // Print the heading for this population
            } else {
                ++numGenerations;
            }
            population = algo.evolve(population); // Evolve the population
            population.sortRoutesByFitness(); // Sort the routes (fittest route at index 0)
            if (verbose) {
                printPopulation(population); // Print the current population
                printBestRoute(population.getRoutes().get(0), numGenerations, false); // Print the best one
            }
        }
        printBestRoute(population.getRoutes().get(0), numGenerations, true);

        if (!verbose) {
            double secondsTaken = (System.currentTimeMillis() - start) / 1000;
            System.out.println("\nFinished in " + (int) (secondsTaken / 60) + " minutes " + (int) (secondsTaken % 60) + " seconds");
        }

    }

    /**
     *
     * @param route the best route
     * @param genNum the number of the current generation
     * @param summary whether or not we are done with this simulation and should
     * print the best overall
     */
    private static void printBestRoute(Route route, int genNum, boolean summary) {
        if (summary) {
            System.out.print("\nBest route after " + genNum + " generations [" + (int) route.calculateTotalDistance() + " miles]: ");
            for (int i = 0; i < route.getCities().size(); i++) { // Print the route
                System.out.print(route.getCities().get(i).state().code() + ", ");
            }
            // Append the starting vertex to the end (to make it a tour). 
            // The distance from end to start is already accounted for in the total distance calculation, so this is a formatting thing.
            System.out.println(route.getCities().get(0).state().code());
        } else {
            System.out.print("\nGENERATION #" + genNum + " BEST ROUTE [" + (int) route.calculateTotalDistance() + " miles]: ");
            for (int i = 0; i < route.getCities().size(); i++) { // Print the route
                System.out.print(route.getCities().get(i).state().code() + ", ");
            }
            System.out.println(route.getCities().get(0).state().code()); // Complete the tour (return to start - same as above).
        }
    }

    public static void printPopulation(Population population) {
        for (Route x : population.getRoutes()) {
            for (int i = 0; i < x.getCities().size(); i++) { // Print the route
                System.out.print(x.getCities().get(i).state().code() + ", ");
            }
            System.out.println(x.getCities().get(0).state().code() + " | " + String.format("%.5f", x.getFitness()) + " | " + (int) x.calculateTotalDistance());
        }
    }

    public static void printHeading(int generationNumber) { // Printing + formatting method
        System.out.println("\n> Generation # " + generationNumber);
        String headingColumn1 = "Route";
        String remainingHeadingColumns = "Fitness | Distance (miles)";
        int cityNamesLength = 0;
        for (int i = 0; i < initialRoute.size(); i++) {
            cityNamesLength += initialRoute.get(i).state().code().length();
        }
        int arrayLength = cityNamesLength + initialRoute.size() * 2;
        int partialLength = (arrayLength - headingColumn1.length()) / 2;
        for (int i = 0; i < partialLength; i++) {
            System.out.print(" ");
        }
        System.out.print(headingColumn1);
        for (int i = 0; i < partialLength; i++) {
            System.out.print(" ");
        }
        if ((arrayLength % 2) == 0) {
            System.out.print(" ");
        }
        System.out.println(" | " + remainingHeadingColumns);
        cityNamesLength += remainingHeadingColumns.length() + 3;
        for (int i = 0; i < cityNamesLength + initialRoute.size() * 2; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    private static String getArgument(String line, int index) {
        String[] words = line.split("\\s");
        return words.length > index ? words[index] : "";
    }

    private static String getCommand(String line) {
        return getArgument(line, 0);
    }

}
