
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #10
 * 11/10/17
 */
public class Homework_10 {

    private static int[] array;
    private static int cutoff;
    private static Pivot pivot;
    private static int depth;
    private static int maxDepth;
    
    private static enum Pivot {
        LEFT,
        RIGHT,
        MIDDLE,
        MEDIAN,
        RANDOM
    }
    
    public static void main(String[] args) {
        String kind = "";
        for (String arg : args) { // args are the arguments passed into Homework_10.java
            switch (arg) {
                case "-cutoff":
                    kind = "cutoff";
                    continue;
                case "-pivot":
                    kind = "pivot";
                    continue;
                default:
                    if (kind.equals("cutoff")) { // If this number is for the cutoff parameter...
                        try {
                            cutoff = Integer.parseInt(arg); // Try to parse it.
                            kind = ""; // And reset the "kind" variable for later use.
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid value: " + arg);
                            cutoff = 0; // Default to a cutoff of 0.
                            return;
                        }
                    } else if (kind.equals("pivot")) { // Switch chain to determine what kind of pivot to use based on user input.
                        switch (arg) {
                            case "left":
                                pivot = pivot.LEFT;
                                break;
                            case "right":
                                pivot = pivot.RIGHT;
                                break;
                            case "middle":
                                pivot = pivot.MIDDLE;
                                break;
                            case "median":
                                pivot = pivot.MEDIAN;
                                break;
                            case "random":
                                pivot = pivot.RANDOM;
                                break;
                            default:
                                pivot = pivot.LEFT; // Default to left.
                                break;
                        }
                        kind = ""; // Reset kind.
                    }
            }
        }
        array = StdIn.readAllInts(); // The numbers from ShuffledGenerator.java are on standard output, so read them in to array.

        quickSort(0, array.length - 1); // Quicksort the array.
        insertionSort(array); // "Run a single insertion sort after the quicksort completes."
        System.out.println("Recursive depth: " + maxDepth); // Print out the recursion depth.
    }

    private static void quickSort(int lo, int hi) {
        if (hi - lo <= cutoff) { // If the current list is smaller than the specified cutoff...
            return; // Return - insertion sort will handle the rest.
        }

        depth++; // We just entered quicksort, thus recursing one more level.
        maxDepth = Math.max(maxDepth, depth); // Keep track of the "deepest" level so far.

        // Pointers
        int i = lo;
        int j = hi;

        // Pivot
        int p = array[lo];

        switch (pivot) {
            case LEFT:
                break; // The pivot is by default set to array[lo] (the leftmost value).
            case MIDDLE:
                p = array[(lo + hi) / 2]; // Set the pivot to be the middle number
                break;
            case RIGHT:
                p = array[hi]; // Set the pivot to be array[hi] (the rightmost value).
                break;
            case MEDIAN:
                p = getMedian(lo, (lo + hi) / 2, hi); // Use the median-of-three technique to avoid O(n^2).
                break;
        }

        do {
            while (array[i] < p) { // Move the i pointer up the slice while the current value is less than the pivot.
                i++;
            }
            while (array[j] > p) { // Move the j pointer down the slice while the current value is more than the pivot.
                j--;
            }
            if (i <= j) { // If the two pointers haven't crossed...
                swap(i++, j--); // Swap those two values (array[i] is ≥ than the pivot and array[j] is ≤ than the pivot).
            }
        } while (i <= j); // Keep doing this as long as i and j haven't crossed.

        quickSort(lo, j); // Then recurse on the two partitions.
        quickSort(i, hi);
        depth--; // Once out of the recursion, decrement the depth.
    }

    private static int[] insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) { // For every number except the 1st...
            int value = array[i]; // Store the current value that we'll move to the sorted portion.
            int j = i; // The index of the next number in the unsorted portion.
            while (j > 0 && array[j - 1] > value) {
                // While we're not at the beginning of the array AND the next number (at left) is bigger than "value"...
                array[j] = array[j - 1]; // Shift this number to the right.
                j--; // Move to the next number (to the left).
            }
            array[j] = value; // Insert "value" into the sorted portion of the list at index "j".
        }
        return array;
    }

    private static void swap(int i, int j) {
        int temp = array[i]; // Swap array[i] and array[j].
        array[i] = array[j];
        array[j] = temp;
    }

    private static int getMedian(int lo, int mid, int hi) {
        int[] arr = {array[lo], array[mid], array[hi]}; // Make an array of the three values.
        return insertionSort(arr)[1]; // Sort them using insertion sort and return the middle number.
    }

}
