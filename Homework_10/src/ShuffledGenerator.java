
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #10
 * 11/10/17
 */
public class ShuffledGenerator {

    public static void main(String[] args) {
        int n = 0;
        for (String arg : args) { // Read the value of n from command line ("-n 5" for example).
            switch (arg) {
                case "-n":
                    continue;
                default:
                    try {
                        n = Integer.parseInt(arg); // Get the number of values the user wants.
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid value: " + arg);
                        return;
                    }
            }
        }

        int[] array = new int[n]; // Create an array of the specified size to hold those values.

        for (int i = 0; i < n; i++) {
            array[i] = i; // Fill array with values 0 - n-1.
        }

        /**
         * To shuffle an array a of n elements (indices 0..n-1): for i from n−1
         * down to 1 do: j ← random integer such that 0 ≤ j ≤ i exchange a[j]and
         * a[i]
         */
        for (int i = n - 1; i >= 1; i--) { // Knuth shuffle.
            int j = (int) (Math.random() * i) + 1; // Generate random integer from 0 ≤ n.
            int temp = array[i]; // Swap array[i] and array[j].
            array[i] = array[j];
            array[j] = temp;
        }

        for (int val : array) { // Output shuffled array to standard output to be used in Homework_10.java.
            StdOut.println(val);
        }
    }

}
