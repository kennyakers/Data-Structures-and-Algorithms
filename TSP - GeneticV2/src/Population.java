
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #26
 * 5/30/18
 */
public class Population {

    private ArrayList<Route> routes = new ArrayList<>(GeneticAlgo.POPULATION_SIZE);

    public Population(int populationSize, GeneticAlgo algo) {
        for (int i = 0; i < populationSize; i++) {
            routes.add(new Route(algo.getInitialRoute()));
        }
    }

    public Population(int populationSize, ArrayList<City> cities) {
        for (int i = 0; i < populationSize; i++) {
            routes.add(new Route(cities));
        }
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void sortRoutesByFitness() {
        routes.sort(new Comparator<Route>() {
            @Override
            public int compare(Route route1, Route route2) {
                int result = 0;
                if (route1.getFitness() > route2.getFitness()) {
                    result = -1;
                } else if (route1.getFitness() < route2.getFitness()) {
                    result = 1;
                }
                return result;
            }
        });
    }
}
