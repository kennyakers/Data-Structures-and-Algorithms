
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #11
 * 11/21/17
 */

/*
    The MSD Radix Sort algorithm below is a stable algorithm. 

    The sum variable ensures that a subsequent identical key can only be swapped
    to a position AFTER the first occurance of that key. This is due to the fact
    that the sum variable is incremented after a swap. Therefore, once the first
    of several identical keys is moved into its correct position (for that 
    letter, that is), the incrementation of the sum variable ensures that the 
    next item with an identical key will be moved to, at least, the index 
    immediately to the right.

    For this reason, it is not possible, e.g. for a pair of identical items, for
    the second of the pair to come before the first in the final sorted array.
*/
public class Homework_11 {

    private static int cutoff;
    private static int radix = 26; // Since we're sorting strings, use radix 26 for the alphabet.
    private static String[] array;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments!");
            return;
        }
        String kind = "";
        int index = 0;
        int arraySize = args.length; // Sort all arguments.
        for (String arg : args) {
            if (arg.equals("-cutoff")) { // Unless command specifies a cutoff, in which case don't count the "-cutoff #".
                arraySize = args.length - 2;
                break;
            }
        }
        array = new String[arraySize];
        for (String arg : args) {
            switch (arg) {
                case "-cutoff":
                    kind = "cutoff";
                    continue;
                default:
                    if (kind.equals("cutoff")) { // If this number is for the cutoff parameter...
                        try {
                            cutoff = Integer.parseInt(arg); // Try to parse it.
                            kind = ""; // And reset the "kind" variable for later use.
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid value: " + arg);
                            cutoff = 0; // Default to a cutoff of 0.
                            //return;
                        }
                    } else {
                        array[index++] = arg;
                    }
            }
        }
        if (array.length < 1) {
            System.out.println("Nothing to sort!");
            return;
        }
        msdRadixSort(0, array.length, 0);
        for (String s : array) {
            System.out.println(s);
        }
    }

    private static void msdRadixSort(int lo, int hi, int letter) {
        if (letter > array[0].length() - 1) { // If beyond last letter, we're done so return.
            return;
        }

        if (hi - lo <= cutoff) { // If the current array slice is smaller than the specified cutoff...
            insertionSort(lo, hi); // Perform insertion sort on it.
            return;
        }

        int preSum = lo; // Variable for the previous sum.
        int sum = lo; // The current sum of indices.

        boolean letterSorted = false; // Boolean flag to determine if we've sorted all of a letter yet.
        for (int i = 0; i < radix; i++) {
            for (int j = sum; j < hi; j++) {
                char currentChar = array[j].charAt(letter);
                if (currentChar >= 'a' && currentChar <= 'z') { // If lowercase...
                    currentChar -= 'a'; // Subtract 'a' (97) from the current char to bring it into the range 0-25.
                } else if (currentChar >= 'A' && currentChar <= 'Z') { // If uppercase...
                    currentChar -= 'A'; // Subtract 'A' (65) from the current char to bring it into the range 0-25.
                } else {
                    throw new IllegalArgumentException("Illegal character " + currentChar); // Otherwise, something else was entered that can't be sorted.
                }

                if (currentChar == i) { // If this char is the current char represented by i...
                    swap(sum++, j); // Move that letter into the next "sorted" slot.
                    letterSorted = true; // We've sorted that letter.
                }
            }
            if (letterSorted) { // If we're done with that letter...
                msdRadixSort(preSum, sum, letter + 1); // Then recurse on the next one.
                preSum = sum; // And remember what this sum was for use later.
            }
        }
    }

    private static void swap(int a, int b) {
        String temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static void insertionSort(int lo, int hi) {
        for (int i = lo; i < hi; i++) {
            for (int j = i; j > lo && array[j].compareToIgnoreCase(array[j - 1]) < 0; j--) { // While we're not at the end and the current item is smaller than the one to its left...
                swap(j, j - 1); // Swap it with the item to the left.
            }
        }
    }
}
