
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #9
 * 11/3/17
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

public class Homework_9 {

    /**
     * Implement a generic sorting method that implements mergeSort on a singly
     * linked list of objects of type T. Comparisons of objects of type T are
     * performed using the Comparator supplied as an argument to the invocation
     * of your merge sort method. Rules of engagement:
     *
     * You must write an implementation of the top-down mergesort algorithm. The
     * running time of your implementation must be O(n lg n). You must not
     * convert the linked lists to an array; you must use lists directly. You
     * must access the elements of the linked list sequentially. Do NOT write a
     * method which returns the ith element of a list. You must be able to use a
     * comparator to control the sort order (forward or reverse).
     *
     * Suggestions: Write a private generic method (parameterized with a type T)
     * to merge two lists into a single list. Write another private generic
     * method to distribute items in a list evenly to two other lists.
     */
    private static int numCompares = 0; // Variable to count the number of comparisons performed per sort.

    public static <T> SinglyLinkedList<T> mergeSort(SinglyLinkedList<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) { // If the list contains just 1 (or no) node(s)...
            return list; // Just return what was passed in.
        }

        SinglyLinkedList<T> a = new SinglyLinkedList<>(); // First list (roughly half as long as @param list).
        SinglyLinkedList<T> b = new SinglyLinkedList<>(); // Second list (the other part of @param list).

        boolean alternate = true; // Simple boolean flag to alternate between the two lists.
        for (T element : list) {
            if (alternate) {
                a.append(list.removeHead());
                alternate = !alternate; // For the next element, add it to list b.
            } else {
                b.append(list.removeHead());
                alternate = !alternate; // For the next element, add it to list a. 
            }
        }

        a = mergeSort(a, comparator); // Recursively split the two lists
        b = mergeSort(b, comparator);
        return merge(a, b, comparator); // And merge them using the comparator that was passed in.
    }

    // Comparator objects of an anonymous class for sorting in forward and reverse order.
    public static Comparator<String> forwardOrder = new Comparator<String>() { // Combination of class and object declaration.
        // Lookup the java Comparator interface to determine how to implement this anonymous class.
        @Override
        public int compare(String o1, String o2) {
            numCompares++;
            return o1.compareTo(o2);
        }
    };

    public static Comparator<String> reverseOrder = new Comparator<String>() {
        // Should be pretty easy if you figured out how to implement forwardOrder.
        @Override
        public int compare(String o1, String o2) {
            numCompares++;
            return o2.compareTo(o1); // Reverse of above.
        }
    };

    // Generic method that takes comparable objects.
    private static <T> SinglyLinkedList<T> merge(SinglyLinkedList<T> a, SinglyLinkedList<T> b, Comparator<T> comparator) {
        SinglyLinkedList<T> result = new SinglyLinkedList<>(); // The final, sorted list to be returned.
        while (!a.isEmpty() && !b.isEmpty()) { // While neither list is empty
            if (comparator.compare(a.head(), b.head()) < 0) { // If the head of one list is smaller than or equal to the head of the other, add the smaller one.
                result.append(a.removeHead());
            } else {
                result.append(b.removeHead()); // If the two are the same or the head of list b is smaller, add the head of b.
            }
        }
        // Only one of these while loops will be called.
        while (!a.isEmpty()) { // Add the rest of list a.
            result.append(a.removeHead());
        }
        while (!b.isEmpty()) { // Add the rest of list b.
            result.append(b.removeHead());
        }
        return result;
    }

    // Some kind sole wrote a test program for you ... just figure out how to use it.
    public static void main(String[] args) throws FileNotFoundException {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        Comparator<String> comparator = forwardOrder;
        boolean statistics = false;
        boolean file = false;

        for (String arg : args) {
            switch (arg) {
                case "-reverse":
                    comparator = reverseOrder;
                    break;

                case "-forward":
                    comparator = forwardOrder;
                    break;

                case "-stats": // Added to display # of comparisons done for a problem set.
                    statistics = true;
                    break;

                case "-file":
                    file = true;
                    break;
                default:
                    list.append(arg);
                    break;
            }
        }
        if (file) {
            Scanner fileIn = new Scanner(new File("words.txt"));
            while (fileIn.hasNextLine()) {
                list.append(fileIn.nextLine());
            }
            fileIn.close();
        }

        list = mergeSort(list, comparator);

        for (String item : list) {
            System.out.println(item);
        }

        if (statistics) {
            System.out.println("Number of Comparisons: " + numCompares);
        }
    }
}
