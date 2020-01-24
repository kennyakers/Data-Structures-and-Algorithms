
/**
 * Kenny Akers
 * Mr. Paige
 * Insertion Sort Presentation Code
 * 10/25/17
 */
public class Main {

    private static int[] numbers;

    public static void main(String[] args) {
        numbers = new int[args.length];
        int i = 0;
        System.out.print("Original: ");
        for (String arg : args) { // Print the original array.
            System.out.print(arg + " ");
            try { 
                numbers[i++] = Integer.parseInt(arg);
            }
            catch (NumberFormatException e) {
                System.out.print("\nInvalid number: \"" + arg + "\", ignoring...");
            }
        }
        insertionSort();
        System.out.print("\nSorted: ");
        for (Integer num : numbers) { // Print the sorted array.
            System.out.print(num + " ");
        }
        System.out.println("");
    }

    public static void insertionSort() {
        for (int i = 1; i < numbers.length; i++) { // For every number except the 1st...
            int value = numbers[i]; // Store the current value that we'll move to the sorted portion.
            int j = i; // The index of the next number in the unsorted portion.
            while (j > 0 && numbers[j - 1] > value) {
                // While we're not at the beginning of the array AND the next number (at left) is bigger than "value"...
                numbers[j] = numbers[j - 1]; // Shift this number to the right.
                j--; // Move to the next number (to the left).
            }
            numbers[j] = value; // Insert "value" into the sorted portion of the list at index "j".
        }
    }

}
