
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #2
 * 8/31/17
 *
 * @author kakers
 */
public class Homework_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        boolean containsCity = false;

        if (args.length == 0) { // If no arguments are given (Ex. java Homework_2)...
            System.out.println("Please provide arguments!"); // Print accordingly.
            return; // Do not proceed.
        }
        Tour tour = new Tour(); // Create an empty tour.
        for (String arg : args) {
            try {
                if (Capitals.find(arg) == null) {
                    System.out.println("Could not find city or state \"" + arg + "\". Omitted."); // No city was found for that argument, so don't add it and let the user know.
                } else {
                    City city = new City(Capitals.find(arg).name(), Capitals.find(arg).state(), Capitals.find(arg).latitude(), Capitals.find(arg).longitude()); // Construct a new city.
                    //System.out.println("Adding: " + city); // Testing.
                    tour.append(city); // Add the city to the tour.
                    containsCity = true;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Bad argument: " + e.getMessage());
            }
        }
        if (containsCity) { // If there is at least one city in the tour, print it (this is also to safeguard against substring-ing any empty/too short strings).
            //System.out.println("Current Array Size: " + tour.arrayLength()); // Testing.
            System.out.println("Tour: " + tour); // Print the tour.
            if (tour.size() == 1) { // Just a simply flag to let users know if their "tour" consists of only one city, which is pretty odd.
                System.out.println("Only 1 city in tour! Continuing...");
            } else {
                System.out.println("Number of Cities: " + tour.size()); // Print the length of the tour.
            }
            System.out.print("Tour Length: ");
            System.out.printf("%.2f", tour.length()); // Print the total distance of the tour (includes drive back to starting city).
            System.out.print(" kilometers, ");
            System.out.printf("%.2f", (tour.length() / 1.609));
            System.out.println(" miles\nTotal Storage Allocated: " + tour.tour.storage()); // Print the total storage allocated.
        }
    }

}
