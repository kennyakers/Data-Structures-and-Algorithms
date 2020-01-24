
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #26
 * 5/30/18
 */
public class Route {

    private ArrayList<City> cities = new ArrayList<City>();
    private boolean isFitnessChanged = true;
    private double fitness = 0.0;

    public Route(ArrayList<City> cities) {
        this.cities.addAll(cities);
        Collections.shuffle(this.cities);
    }

    public Route(GeneticAlgo algo) {
        algo.getInitialRoute().forEach(x -> cities.add(null));
    }

    public ArrayList<City> getCities() {
        this.isFitnessChanged = true;
        return this.cities;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.cities.toArray());
    }

    public double calculateTotalDistance() {
        int numCities = this.cities.size();
        return (int) (this.cities.stream().mapToDouble(x -> {
            int cityIndex = this.cities.indexOf(x);
            double returnValue = 0;
            if (cityIndex < numCities - 1) {
                returnValue = x.distance(this.cities.get(cityIndex + 1));
            }
            return returnValue;
        }).sum() + this.cities.get(0).distance(this.cities.get(numCities - 1))); // Add distance between end and start
    }

    public double getFitness() {
        if (this.isFitnessChanged) {
            this.fitness = (1 / this.calculateTotalDistance()) * 10000; // Recalculate the fitness
            this.isFitnessChanged = false;
        }
        return this.fitness;
    }

}
