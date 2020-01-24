/**
 * Kenny Akers
 * Mr. Paige
 * Homework #10
 * 11/10/17
 */
public class Problem_2_2_21 {

    public static void main(String[] args) {

        String[] list1 = new String[0];
        String[] list2 = new String[0];
        String[] list3 = new String[0];

        int rowNumber = 0;
        int counter = 0; // Counter that keeps track of how many times each list was added to. Used to ensure that every
        int n = 0;
        int index1 = 0;
        int index2 = 0;
        int index3 = 0;

        for (String arg : args) {
            switch (arg) {
                case "-first":
                    rowNumber = 1;
                    continue;
                case "-second":
                    rowNumber = 2;
                    continue;
                case "-third":
                    rowNumber = 3;
                    continue;
                case "-n":
                    rowNumber = 0;
                    continue;
                default:

                    try {
                        if (rowNumber == 1) {
                            list1[index1++] = arg;
                            counter++;
                        } else if (rowNumber == 2) {
                            list2[index2++] = arg;
                            counter++;
                        } else if (rowNumber == 3) {
                            list3[index3++] = arg;
                            counter++;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("No array size specified. First argument must be -n followed by the array size.");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Too many elements in array. Ensure that each list has the amount of elements specified by -n.");
                    }
                    if (rowNumber == 0) {
                        try {
                            n = Integer.parseInt(arg);
                            list1 = new String[n];
                            list2 = new String[n];
                            list3 = new String[n];
                            continue;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input for n: " + arg);
                            continue;
                        }
                    }
            }
        }

        /*if (counter != (3 * n)) { // If the three lists weren't add to n times, then exit because uneven arrays.
            System.out.println("Unbalanced Arrays! Ensure that all of the arrays have the same amount of elements.");
            return;
        }
         */
        System.out.print("List 1: ");
        for (String s : list1) {
            System.out.print(s + " ");
        }

        System.out.print("\nList 2: ");
        for (String s : list2) {
            System.out.print(s + " ");
        }

        System.out.print("\nList 3: ");
        for (String s : list3) {
            System.out.print(s + " ");
        }

        System.out.println("");

        list1 = mergeSort(list1, 0, n - 1); // Sort the first 2 lists.
        list2 = mergeSort(list2, 0, n - 1); // Sort the first 2 lists.

        System.out.print("List 1: ");
        for (String s : list1) {
            System.out.print(s + " ");
        }

        System.out.print("\nList 2: ");
        for (String s : list2) {
            System.out.print(s + " ");
        }
        System.out.println("");

        String commonName = "None";

        for (String s : list3) { // Iterate over the last, unsorted row.
            int i = binarySearch(list1, s); // Index of the key in list1;
            int j = binarySearch(list2, s); // Index of the key in list2.
            if (i != -1 && j != -1) { // If s is present in both lists.
                commonName = list1[i];
                break;
            }
        }

        System.out.println("Common Name: " + commonName); // This will be executed if a common name was not found (i.e. the break was not executed).

    }

    private static int binarySearch(String array[], String key) {
        int low = 0;
        int high = array.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;
            if (array[mid].compareToIgnoreCase(key) < 0) {
                low = mid + 1;
            } else if (array[mid].compareToIgnoreCase(key) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1; // If @param key is not in the array, return -1;
    }
    
        private static String[] mergeSort(String[] a, int lo, int hi) {
        if (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            mergeSort(a, lo, mid);
            mergeSort(a, mid + 1, hi);
            return merge(a, lo, mid, hi);
        }
        return null;
    }

    private static String[] merge(String[] a, int lo, int mid, int hi) {
        String[] b = new String[a.length];

        int i = lo;
        int j = mid + 1;
        int k = lo;

        while (i <= mid && j <= hi) {
            if (a[i].compareToIgnoreCase(a[j]) < 0) {
                b[k++] = a[i++];
            } else {
                b[k++] = a[j++];
            }
        }

        while (i <= mid) {
            b[k++] = a[i++];
        }

        while (j <= hi) {
            b[k++] = a[j++];
        }

        return b;
    }
}
