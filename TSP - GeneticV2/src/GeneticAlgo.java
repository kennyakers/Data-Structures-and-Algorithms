
import java.util.ArrayList;

/**
 * Kenny Akers Mr. Paige Homework #26 5/30/18
 */
public class GeneticAlgo {

    public static int POPULATION_SIZE; // Number of routes in a generation.
    public static double MUTATION_RATE; // Probability that a chromosome (route) gene (city in that route) will randomly mutate (0-1).
    public static int TOURNAMENT_SELECTION_SIZE;// Using Tournament selection to do chromosome (route) crossover selection.
    public static int NUM_ELITE_ROUTES; // Elite routes are not subjected to crossover or mutation from one generation to the next.
    public static int NUM_GENERATIONS; // Number of generations

    private ArrayList<City> initialRoute = null;

    public GeneticAlgo(ArrayList<City> initialRoute) {
        this.initialRoute = initialRoute;
    }

    public ArrayList<City> getInitialRoute() {
        return this.initialRoute;
    }

    public Population evolve(Population population) {
        return this.mutatePopulation(this.crossoverPopulation(population));
    }

    public Population crossoverPopulation(Population population) {
        Population crossoverPopulation = new Population(population.getRoutes().size(), this);
        for (int i = 1; i < NUM_ELITE_ROUTES; i++) { // Put the elite routes in the crossover population.
            crossoverPopulation.getRoutes().set(i, population.getRoutes().get(i));
        }
        for (int i = NUM_ELITE_ROUTES; i < crossoverPopulation.getRoutes().size(); i++) {
            // Now for the rest of the population, use Tournament Selection to select the best.
            // The route at index 0 should be the fittest because we're sorting by fitness.
            Route route1 = this.selectPopulation(population).getRoutes().get(0);
            Route route2 = this.selectPopulation(population).getRoutes().get(0);
            crossoverPopulation.getRoutes().set(i, this.crossoverRoute(route1, route2));
        }
        return crossoverPopulation;
    }

    public Population mutatePopulation(Population population) {
        // For each of the non-elite routes, mutate them by calling mutateRoute();
        for (Route r : population.getRoutes()) {
            if (population.getRoutes().indexOf(r) >= NUM_ELITE_ROUTES) {
                this.mutateRoute(r);
            }
        }
        return population;
    }

    // Combine (crossover) two routes to create a "child" route
    public Route crossoverRoute(Route route1, Route route2) {
        // Starts by creating an intermediate crossover route, consisting of ~half the cities of route1
        // and the rest null. It then calls crossoverRouteHelper to determine what to place in those null spots.
        Route crossover = new Route(this);
        Route tempRoute1 = route1;
        Route tempRoute2 = route2;

        if (Math.random() < 0.5) { // Swap the two half the time for more randomness.
            tempRoute1 = route2;
            tempRoute2 = route1;
        }

        // Fill the intermediate crossover route half full from cities from tempRoute1.
        for (int i = 0; i < crossover.getCities().size() / 2; i++) {
            crossover.getCities().set(i, tempRoute1.getCities().get(i));
        }

        return this.crossoverRouteHelper(crossover, tempRoute2);
    }

    private Route crossoverRouteHelper(Route crossover, Route route) {
        // If a city is not already in the crossover route, then do a for loop to add it from @param route
        for (City c : route.getCities()) {
            if (!crossover.getCities().contains(c)) {
                for (int i = 0; i < route.getCities().size(); i++) {
                    if (crossover.getCities().get(i) == null) {
                        crossover.getCities().set(i, c);
                        break;
                    }
                }
            }
        }
        return crossover;
    }

    // Selects the population using Tournament Selection
    public Population selectPopulation(Population population) {
        Population tournament = new Population(TOURNAMENT_SELECTION_SIZE, this);
        for (int i = 0; i < TOURNAMENT_SELECTION_SIZE; i++) {
            tournament.getRoutes().set(i, population.getRoutes().get((int) (Math.random() * population.getRoutes().size())));
        }
        tournament.sortRoutesByFitness();
        return tournament;
    }

    public Route mutateRoute(Route route) {
        // Pick two cities and swap them.
        for (City c : route.getCities()) {
            if (Math.random() < MUTATION_RATE) {
                int y = (int) (route.getCities().size() * Math.random());
                City cityY = route.getCities().get(y);
                route.getCities().set(route.getCities().indexOf(c), cityY);
                route.getCities().set(y, c);
            }
        }
        return route;
    }

}
