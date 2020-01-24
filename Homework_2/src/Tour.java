
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #2
 * 8/31/17
 *
 * @author kakers
 */

import java.util.Iterator;

public class Tour implements Iterable<City> {

    // Constructors to create an empty Tour and
    // to create an initialized Tour.
    // Check that the tour is valid.
    public Array<City> tour;

    public Tour() {
        tour = new Array();
    }

    public Tour(City[] cities) {
        tour = new Array(cities);
    }

    public int size() { // Number of cities on the tour
        return tour.size();
    }

    /*public int arrayLength() { // Test method
        return tour.length();
    }
     */
    public double length() { // Length (distance travelled) of the tour
        double totalDistance = 0.0;
        for (int index = 0; index < tour.size() - 1; index++) {
            totalDistance += tour.get(index).distance(tour.get(index + 1)); // Add the distance between the current city and the next one to totalDistance.
        }
        totalDistance += tour.get(tour.size() - 1).distance(tour.get(0)); // Also add the distance from the last city back to the starting city.
        return totalDistance;
    }

    // Methods to append/prepend cities to the end/beginning of a tour.
    // Checks should be made that the tour remains valid.
    public void append(City city) {
        if (tour.contains(city)) {
            return; // Duplicate cities are not allowed in the tour, so return.
        }
        tour.append(city); // The city is not a duplicate, so append it.
    }

    public void prepend(City city) {
        if (tour.contains(city)) {
            return; // Duplicate cities are not allowed in the tour, so return.
        }
        tour.prepend(city); // The city is not a duplicate, so prepend it.
    }

    // This class should implement Iterable and of course
    // export an Iterator to traverse the cities in the tour.
    @Override
    public Iterator<City> iterator() {
        return this.new TourIterator();
    }

    // And finally, a method to display a tour:
    @Override
    public String toString() {
        return tour.toString(); // Return Array's toString method, which prints the array: {city, city, city...}.
    }

    private class TourIterator implements Iterator<City> {

        private City currentCity;
        private int currentCityIndex; // Counter to keep track of the current city's index, solely for use when the next city needs to be stored in currentCity in the next() method.

        public TourIterator() {
            this.currentCityIndex = 0;
            this.currentCity = tour.get(this.currentCityIndex);
        }

        @Override
        public boolean hasNext() {
            return !(this.currentCity == tour.get(tour.size() - 1)); // Return true if the current is not on the last city of the tour.
        }

        @Override
        public City next() {
            /* 
            Next has two jobs to do:
            return the value associated with the current spot in the iteration.
            advance to the next spot in the iteration.
             */
            City result = this.currentCity;  // Save the current city.
            this.currentCity = tour.get(this.currentCityIndex + 1); // Store the next city in currentCity.
            return result; // Return the current city.
        }
    }
}
