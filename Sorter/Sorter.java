
import java.util.Random;
import java.awt.Color;

public class Sorter {

    // A sorting algorithm visualization program.
    private static enum Algorithm {
        BUBBLE,
        SHAKER,
        INSERTION,
        SELECTION,
        SHELL,
        QUICK,
        QUICK3WAY,
        TOP_DOWN_MERGE,
        BOTTOM_UP_MERGE,
        HEAP,
        LSD_RADIX,
        MSD_RADIX,
        STOOGE,
        BUG1,
        BUG2,
        NONE,
    }

    private static enum Ordering { // Possible orderings of array to be sorted
        INORDER,
        REVERSED,
        SHUFFLED,
        RANDOM
    }

    private static enum Pivot { // Pivoting strategies for quicksort
        FIRST,
        MIDDLE,
        LAST,
        MEDIAN
    }

    private static Pivot pivot = Pivot.MIDDLE;		// Pivot stragegy for quicksort
    private static int cutoff = 1;					// Cutoff value for recursive sorts
    private static int radix = 4;					// Radix value for radix sorts.

    // Range of integers for the unsorted array.
    private static int min = 0;
    private static int max = 0;

    // Drawing speed control.
    private static int speed = 10;
    private static int getDelay = 25;
    private static int setDelay = 100;

    // Statistics.
    private static class Statistics {

        public static int compares = 0;
        public static int writes = 0;
        public static int reads = 0;
        public static int swaps = 0;
    }

    // -- Initial Order Utilities --------------------------------------------- 
    public static int[] ordered(int min, int max, int count) {
        // An array of count integers from min .. max in ascending order.
        int[] result = new int[count];
        double range = max - min;

        for (int i = 0; i < count; i++) {
            result[i] = min + (int) (i * range / count);
        }
        return result;
    }

    public static int[] reversed(int min, int max, int count) {
        // An array of count integers from min .. max in descending order.
        int[] result = new int[count];
        double range = max - min;

        for (int i = 0; i < count; i++) {
            result[count - i - 1] = min + (int) (i * range / count);
        }
        return result;
    }

    public static int[] shuffle(int[] a, int seed) {
        // Perform a Knuth shuffle on the input array.
        Random random = (seed != 0) ? new Random(seed) : new Random();
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int j = i + random.nextInt(n - i);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return a;
    }

    public static int[] shuffle(int[] a, int seed, double probability) {
        // Perform a Knuth shuffle on the input array with probability p.
        Random random = (seed != 0) ? new Random(seed) : new Random();
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            if (random.nextDouble() < probability) {
                int j = i + random.nextInt(n - i);
                int t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
        }
        return a;
    }

    public static int[] unordered(int min, int max, int count, int seed) {
        // A randomly shuffled array of the integers from min .. max.
        return shuffle(ordered(min, max, count), seed);
    }

    public static int[] random(int min, int max, int count, int seed) {
        // An array of integers randomly choose from min .. max.
        Random random = (seed != 0) ? new Random(seed) : new Random();
        int[] result = new int[count];
        long spread = (max - min);
        int number;

        for (int i = 0; i < count; i++) {
            if (spread > 0) {
                number = min + random.nextInt((int) spread);
            } else {
                number = random.nextInt();
            }
            result[i] = number;
        }
        return result;
    }

    // -- Screen Drawing/Update Utilties -------------------------------------- 
    public static void box(double x, double y, double width, double height, Color color) {

        // Draw a box on the screen with lower left coordinates (x, y)
        // having the specified width and height and fill color.
        double xCenter = x + width / 2.0;
        double yCenter = y + height / 2.0;
        StdDraw.setPenColor(color);
        StdDraw.filledRectangle(xCenter, yCenter, width / 2.0, height / 2.0);
    }

    public static void display(int[] a, int index, Color color) {
        // Display the box for the array element a[i] in the specified color.
        int value = a[index];
        box(index, 0.0, 1.0, max, StdDraw.WHITE);
        box(index, 0.0, 1.0, value, color);
    }

    public static void display(int[] a, int index) {
        // Display the box for the array elemement a[i] in the default color.
        display(a, index, StdDraw.DARK_GRAY);
    }

    public static void display(int[] a, Color color) {
        // Display boxes for the entire array in the specified color.
        int n = a.length;
        for (int i = 0; i < n; i++) {
            display(a, i, color);
        }
    }

    public static void display(int[] a) {
        // Display boxes for the entire array in the default color.
        int n = a.length;
        for (int i = 0; i < n; i++) {
            display(a, i);
        }
    }

    public static void highlight(int[] a, int index, Color color, int delay) {

        // Draw the box for a[index] in another color
        // Delay for the specified amount of time.
        // Redraw the box for a[index] in original color
        display(a, index, color);
        StdDraw.pause(delay);
        StdDraw.show();
        display(a, index);
    }

    public static void highlight(int[] a, int index1, int index2, Color color, int delay) {

        // Draw the boxes for a[index1] & a[index2] in another color
        // Delay for the specified amount of time.
        // Redraw the boxes for a[index1] & a[index2] in original color
        display(a, index1, color);
        display(a, index2, color);
        StdDraw.pause(delay);
        StdDraw.show();
        display(a, index1);
        display(a, index2);
    }

    // -- Sorting Utilities --------------------------------------------------- 
    public static int get(int[] a, int index) {				// Read a[i]
        Statistics.reads++;
        highlight(a, index, StdDraw.BLACK, getDelay);
        return a[index];
    }

    public static void set(int[] a, int index, int value) { // Write a[i[]
        Statistics.writes++;
        a[index] = value;
        highlight(a, index, StdDraw.RED, setDelay);
    }

    public static boolean less(int left, int right) {		// Compare x & y
        Statistics.compares++;
        return left < right;
    }

    public static boolean less(int[] a, int index1, int index2) { 	// Compare a[i] & a[j]
        Statistics.reads += 2;
        highlight(a, index1, index2, StdDraw.BLACK, getDelay);
        return less(a[index1], a[index2]);
    }

    public static void swap(int[] a, int index1, int index2) {		// Swap a[i] & a[j]
        Statistics.writes += 2;
        Statistics.reads += 2;
        Statistics.swaps++;
        int value1 = a[index1];
        int value2 = a[index2];
        a[index1] = value2;
        a[index2] = value1;
        highlight(a, index1, index2, StdDraw.RED, setDelay);
    }

    // -- Buggy Bubble Sort --------------------------------------------------------- 
    public static void buggyBubbleSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int swaps = 0;
            for (int j = 1; j < n - 1; j++) {
                if (!less(a, j - 1, j)) {
                    swap(a, j + 1, j);
                    swaps++;
                }
            }
            if (swaps == 0) {
                break;
            }
        }
    }

    // -- Bubble Sort --------------------------------------------------------- 
    public static void bubbleSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int swaps = 0;
            for (int j = 0; j < n - i - 1; j++) {
                if (less(a, j + 1, j)) {
                    swap(a, j + 1, j);
                    swaps++;
                }
            }
            if (swaps == 0) {
                break;
            }
        }
    }

    // -- Shaker Sort --------------------------------------------------------- 
    public static void shakerSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int swaps = 0;
            for (int j = i; j < n - i - 1; j++) {
                if (less(a, j + 1, j)) {
                    swap(a, j + 1, j);
                    swaps++;
                }
            }
            if (swaps == 0) {
                break;
            }

            swaps = 0;
            for (int j = n - i - 1; j > i; j--) {
                if (less(a, j, j - 1)) {
                    swap(a, j, j - 1);
                    swaps++;
                }
            }
            if (swaps == 0) {
                break;
            }
        }
    }

    // -- Selection Sort ------------------------------------------------------ 
    public static void selectionSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (less(a, j, min)) {
                    min = j;
                }
            }
            swap(a, i, min);
        }
    }

    // -- Buggy Insertion Sort ------------------------------------------------------ 
    public static void buggyInsertionSort(int[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(a, j, j - 1); j--) {
                swap(a, j, j - 1);
            }
            swap(a, i + 1, i);
        }
    }

    // -- Insertion Sort ------------------------------------------------------ 
    public static void insertionSort(int[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            // int value = get(a, i);
            for (int j = i; j > 0 && less(a, j, j - 1); j--) {
                swap(a, j, j - 1);
            }
        }
    }

    private static void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++) {
            int x = get(a, i);
            int j = i - 1;
            while (j >= lo && less(x, get(a, j))) {
                set(a, j + 1, get(a, j));
                j--;
            }
            set(a, j + 1, x);
        }
    }

    // -- Shell Sort ---------------------------------------------------------- 
    public static void shellSort(int[] a) {
        int n = a.length;
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }
        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && less(a, j, j - h); j -= h) {
                    swap(a, j, j - h);
                }
            }
            h /= 3;
        }
    }

    // -- Quicksort ----------------------------------------------------------- 
    private static int median(int[] array, int index1, int index2, int index3) {
        int a = get(array, index1);
        int b = get(array, index2);
        int c = get(array, index3);

        if (a < b && a < c) {			// a is smallest
            if (b < c) {
                return index2;
            } else {
                return index3;
            }
        } else if (b < a && b < c) {	// b is smallest
            if (c < a) {
                return index3;
            } else {
                return index1;
            }
        } else {						// c is smallest
            if (a < b) {
                return index1;
            } else {
                return index2;
            }
        }
    }

    private static void quickSort(int[] a, int lo, int hi) {

        if (hi - lo <= cutoff) {
            insertionSort(a, lo, hi);
            return;
        }

        int i = lo;
        int j = hi;
        int v = 0;

        switch (pivot) {
            case FIRST:
                v = get(a, lo);
                break;
            case MIDDLE:
                v = get(a, (lo + hi) / 2);
                break;
            case LAST:
                v = get(a, hi);
                break;
            case MEDIAN:
                v = median(a, lo, hi, (lo + hi) / 2);
                break;
        }

        do {
            while (less(get(a, i), v)) {
                i++;
            }
            while (less(v, get(a, j))) {
                j--;
            }
            if (i <= j) {
                swap(a, i++, j--);
            }
        } while (i <= j);

        quickSort(a, lo, j);
        quickSort(a, i, hi);
    }

    public static void quickSort(int[] a) {
        int n = a.length;
        quickSort(a, 0, n - 1);
    }

    // -- 3-Way Quicksort ----------------------------------------------------- 
    private static void quick3Sort(int[] a, int lo, int hi) {
        if (hi - lo <= cutoff) {
            insertionSort(a, lo, hi);
            return;
        }

        switch (pivot) {
            case FIRST:
                break;
            case MIDDLE:
                swap(a, lo, (lo + hi) / 2);
                break;
            case LAST:
                swap(a, lo, hi);
                break;
            case MEDIAN:
                swap(a, lo, median(a, lo, hi, (lo + hi) / 2));
                break;
            default:
                break;
        }

        int lt = lo;
        int gt = hi;
        int i = lo + 1;
        int v = get(a, lo);

        while (i <= gt) {
            int x = get(a, i);
            if (less(x, v)) {
                swap(a, lt++, i++);
            } else if (less(v, x)) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        quick3Sort(a, lo, lt - 1);
        quick3Sort(a, gt + 1, hi);
    }

    public static void quick3Sort(int[] a) {
        quick3Sort(a, 0, a.length - 1);
    }

    // -- Merge Sorts --------------------------------------------------------- 
    private static void merge(int[] a, int lo, int mid, int hi) {
        int[] b = new int[a.length];

        int i = lo;
        int j = mid + 1;
        int k = lo;

        while (i <= mid && j <= hi) {
            if (less(a, i, j)) {
                b[k++] = get(a, i++);
            } else {
                b[k++] = get(a, j++);
            }
        }

        while (i <= mid) {
            b[k++] = get(a, i++);
        }

        while (j <= hi) {
            b[k++] = get(a, j++);
        }

        for (i = lo; i <= hi; i++) {
            set(a, i, b[i]);
        }
    }

    private static void topDownMergeSort(int[] a, int lo, int hi) {
        if (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            topDownMergeSort(a, lo, mid);
            topDownMergeSort(a, mid + 1, hi);
            merge(a, lo, mid, hi);
        }
    }

    public static void topDownMergeSort(int[] a) {
        int n = a.length;
        topDownMergeSort(a, 0, n - 1);
    }

    public static void bottomUpMergeSort(int[] a) {
        int N = a.length;
        for (int size = 1; size < N; size += size) {
            for (int lo = 0; lo < N - size; lo += 2 * size) {
                int hi = Math.min(lo + 2 * size - 1, N - 1);
                int mid = lo + size - 1;
                merge(a, lo, mid, hi);
            }
        }
    }

    // -- Heap Sort ----------------------------------------------------------- 
    private static void sink(int[] a, int k, int N) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(a, j, j + 1)) {
                j++;
            }
            if (!less(a, k, j)) {
                break;
            }
            swap(a, k, j);
            k = j;
        }
    }

    private static void heapSort(int[] a) {
        int N = a.length - 1;
        for (int k = N / 2; k >= 0; k--) {
            sink(a, k, N);
        }
        while (N > 0) {
            swap(a, 0, N--);
            sink(a, 0, N);
        }
    }

    // -- LSD Radix Sort ------------------------------------------------------ 
    private static int numberDigits(int value, int radix) {
        int digits = 0;
        while (value > 0) {
            value /= radix;
            digits++;
        }
        return digits;
    }

    private static int getDigit(int value, int radix, int digit) {
        while (digit > 0) {
            value /= radix;
            digit--;
        }
        return value % radix;
    }

    private static int max(int[] a) {
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }
        return max;
    }

    private static void radixSortLSD(int[] a, int radix, int digit) {
        int n = a.length;
        int count[] = new int[radix];
        for (int i = 0; i < radix; i++) {
            count[i] = 0;
        }

        for (int i = 0; i < n; i++) {
            int index = getDigit(get(a, i), radix, digit);
            count[index]++;
        }

        int sum = 0;
        for (int i = 0; i < radix; i++) {
            int temp = count[i];
            count[i] = sum;
            sum += temp;
        }

        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            int index = getDigit(get(a, i), radix, digit);
            b[count[index]++] = get(a, i);
        }

        for (int i = 0; i < n; i++) {
            set(a, i, b[i]);
        }
    }

    public static void radixSortLSD(int[] a) {
        int digits = numberDigits(max(a), radix);
        for (int d = 0; d < digits; d++) {
            radixSortLSD(a, radix, d);
        }
    }

    // -- MSD Radix Sort ------------------------------------------------------ 
    private static void radixSortMSD(int[] a, int lo, int hi, int radix, int digit) {
        if (hi - lo <= cutoff) {
            insertionSort(a, lo, hi);
            return;
        }

        int count[] = new int[radix];
        int first[] = new int[radix];
        int last[] = new int[radix];
        for (int i = 0; i < radix; i++) {
            count[i] = 0;
            first[i] = 0;
            last[i] = 0;
        }

        for (int i = lo; i <= hi; i++) {
            int index = getDigit(get(a, i), radix, digit);
            count[index]++;
        }

        int sum = 0;
        for (int i = 0; i < radix; i++) {
            int temp = count[i];
            count[i] = sum;
            first[i] = lo + sum;
            sum += temp;
            last[i] = lo + sum - 1;
        }

        int[] b = new int[a.length];
        for (int i = lo; i <= hi; i++) {
            int index = getDigit(get(a, i), radix, digit);
            b[lo + count[index]++] = get(a, i);
        }

        for (int i = lo; i <= hi; i++) {
            set(a, i, b[i]);
        }

        if (digit > 0) {
            for (int d = 0; d < radix; d++) {
                radixSortMSD(a, first[d], last[d], radix, digit - 1);
            }
        }
    }

    public static void radixSortMSD(int[] a) {
        int digits = numberDigits(max(a), radix);
        int hi = a.length - 1;
        int lo = 0;
        radixSortMSD(a, lo, hi, radix, digits - 1);
    }

    // -- Stooge Sort --------------------------------------------------------- 
    public static void stoogeSort(int[] a, int lo, int hi) {
        if (less(a, hi, lo)) {
            swap(a, hi, lo);
        }
        if ((hi - lo + 1) > 2) {
            int t = (hi - lo + 1) / 3;
            stoogeSort(a, lo, hi - t);
            stoogeSort(a, lo + t, hi);
            stoogeSort(a, lo, hi - t);
        }
    }

    public static void stoogeSort(int[] a) {
        int n = a.length;
        stoogeSort(a, 0, n - 1);
    }

    // -- Main Program ---------------------------------------------------------- 
    // Command line options:
    //
    //	Sorting Algorithm:
    //
    //	-bubble
    //	-shaker
    //	-insertion
    //	-selection
    //	-shell
    //	-quick
    //	-quick3			// 3-way quicksort
    //	-td_merge		// top-down mergesort (abbreviation: -td)
    //	-bu_merge		// bottom-up mergesort (abbreivate: -bu)
    //	-lsd_radix		// least significant digit first radix sort (-lsd)
    //	-msd_radix		// most significant digit first radix sort (-msd)
    //	-stooge
    //
    //	Sorting Algorithm parameters:
    //
    //	-radix r		// The radix to be used for radix sorts.
    //	-pivot stragegy	// The pivoting stragegy to be used for quicksort:
    //	-pivot first	//		Use the leftmost element as the pivot
    //	-pivot last		//		Use the rightmost element as the pivot
    //	-pivot middle	//		Use the middle element as the pivot
    //	-pivot median	//		Use the median of the left, middle and right elements
    //	-cutoff n		// The cutoff value (array size) for recursive sorts at which
    //					//		the sub-array will be sorted using insertion sort.
    //
    //	Initial array order:
    //
    //	-inorder		// Integers min .. max in ascending order
    //	-reversed		// Integers min .. max in descending order
    //	-shuffled		// Integers min .. max in shuffled order
    //	-random			// Integers selected randomly from min .. max
    //	-seed n			// Seed (integer) to be used for random number generator
    //					//		Using the same seed will produce the same initial
    //					//		array order - good if comparing different sorts
    //
    //	-count n		// Size of the array to be sorted (abbreviation: -n)
    //	-min n			// Minimum value for array elements (default 0)
    //	-max n			// Maximum value for array elements (default n, i.e., count)
    //	-probability p	// The array will be Kunth shuffled with probability p.
    //					// 		Used with -inorder or -reversed to produce an almost
    //					// 		sorted array (-probability 0.05) or less is good.
    //
    //	Display:
    //
    //	-width n		// Width of the canvas in pixels (default 600)
    //	-height n		// Height of the canvas in pixels (default 600)
    //	-speed n		// Governs the speed at which the display is updated
    //					//		Swaps will delay for 4 seconds / speed (default = 100 msec)
    //					//		Reads will delay for 1 second / speed (default = 25 msec)
    //	-statistics		// Display statistics on the console when the sort is completed.
    public static void main(String[] args) {
        String option = "";
        String kind = "";
        double dvalue = 0.0;
        int value = 0;

        Algorithm algorithm = Algorithm.NONE;
        Ordering ordering = Ordering.SHUFFLED;
        double shuffleProbability = 0.0;

        int count = 0;

        int height = 600;
        int width = 600;

        int seed = 0;
        double speed = 40.0;
        boolean statistics = false;

        for (String arg : args) {
            switch (arg) {
                case "-bug1":
                    algorithm = Algorithm.BUG1;
                    break;

                case "-bug2":
                    algorithm = Algorithm.BUG2;
                    break;

                case "-bubble":
                    algorithm = Algorithm.BUBBLE;
                    break;

                case "-shaker":
                    algorithm = Algorithm.SHAKER;
                    break;

                case "-insertion":
                    algorithm = Algorithm.INSERTION;
                    break;

                case "-selection":
                    algorithm = Algorithm.SELECTION;
                    break;

                case "-shell":
                    algorithm = Algorithm.SHELL;
                    break;

                case "-quick":
                    algorithm = Algorithm.QUICK;
                    break;

                case "-quick3":
                    algorithm = Algorithm.QUICK3WAY;
                    break;

                case "-td":
                case "-merge":
                case "-td_merge":
                case "-top_down":
                case "-top_down_merge":
                    algorithm = Algorithm.TOP_DOWN_MERGE;
                    break;

                case "-bu":
                case "-bu_merge":
                case "-bottom_up":
                case "-bottom_up_merge":
                    algorithm = Algorithm.BOTTOM_UP_MERGE;
                    break;

                case "-heap":
                    algorithm = Algorithm.HEAP;
                    break;

                case "-lsd_radix":
                case "-lsd":
                    algorithm = Algorithm.LSD_RADIX;
                    break;

                case "-msd_radix":
                case "-msd":
                    algorithm = Algorithm.MSD_RADIX;
                    break;

                case "-stooge":
                    algorithm = Algorithm.STOOGE;
                    break;

                case "-inorder":
                case "-ordered":
                case "-forward":
                    ordering = Ordering.INORDER;
                    break;

                case "-reverse":
                case "-reversed":
                    ordering = Ordering.REVERSED;
                    break;

                case "-shuffle":
                case "-shuffled":
                case "-unordered":
                    ordering = Ordering.SHUFFLED;
                    break;

                case "-random":
                    ordering = Ordering.RANDOM;
                    break;

                case "-statistics":
                case "-stats":
                    statistics = true;
                    break;

                case "-n":
                case "-min":
                case "-max":
                case "-count":
                case "-radix":
                case "-cutoff":
                case "-width":
                case "-height":
                case "-seed":
                    option = arg;
                    kind = "int";
                    break;

                case "-speed":
                case "-probability":
                    option = arg;
                    kind = "double";
                    break;

                case "-pivot":
                    option = arg;
                    kind = "String";
                    break;

                default:
                    switch (kind) {
                        case "int":
                            try {
                                value = Integer.parseInt(arg);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid value: " + arg);
                                return;
                            }
                            break;

                        case "double":
                            try {
                                dvalue = Double.parseDouble(arg);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid value: " + arg);
                                return;
                            }
                            break;

                        default:
                            break;
                    }

                    switch (option) {
                        case "-seed":
                            seed = value;
                            break;

                        case "-min":
                            min = value;
                            break;

                        case "-max":
                            max = value;
                            break;

                        case "-radix":
                            radix = value;
                            break;

                        case "-cutoff":
                            cutoff = value;
                            break;

                        case "-width":
                            width = value;
                            break;

                        case "-height":
                            height = value;
                            break;

                        case "-speed":
                            speed = dvalue;
                            break;

                        case "-probability":
                            shuffleProbability = dvalue;
                            break;

                        case "-count":
                        case "-n":
                        case "-N":
                        default:
                            count = value;
                            break;

                        case "-pivot":
                            switch (arg) {
                                case "LO":
                                case "lo":
                                case "first":
                                    pivot = Pivot.FIRST;
                                    break;

                                case "HI":
                                case "hi":
                                case "last":
                                    pivot = Pivot.LAST;
                                    break;

                                case "MID":
                                case "mid":
                                case "middle":
                                    pivot = Pivot.MIDDLE;
                                    break;

                                case "MEDIAN":
                                case "median":
                                    pivot = Pivot.MEDIAN;
                                    break;
                            }
                            break;
                    }
                    option = "";
                    kind = "";
            }
        }

        // Set option values.
        if (max == min && count == 0) {
            min = 0;
            max = 100;
            count = 100;
        } else if (count == 0) {
            count = max - min;
        } else if (max == min) {
            max = count;
            min = 0;
        }

        if (speed > 0.0) {
            getDelay = (int) (1000.0 / speed);
            setDelay = (int) (4000.0 / speed);
        }

        // Construct the array to be sorted.
        int[] array;
        switch (ordering) {
            case INORDER: // 1 .. N in ascending order (actually MIN .. MAX in ascending order)
                array = ordered(min, max, count);
                break;

            case REVERSED: // 1 .. N in descending order
                array = reversed(min, max, count);
                break;

            case SHUFFLED: // 1 .. N randomly shuffled
                array = shuffle(ordered(min, max, count), seed);
                break;

            case RANDOM: // N integers randomly selected from the uniform distribution MIN .. MAX.
                array = random(min, max, count, seed);
                break;

            default:
                array = ordered(min, max, count);
                break;
        }

        // Perform a Knuth shuffle on the above ordering with probability p.
        // Allows producing arrays that ALMOST but not quite in order.
        if (shuffleProbability > 0.0) {
            array = shuffle(array, seed, shuffleProbability);
        }

        // Set up the drawing window.
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0.0, count);
        StdDraw.setYscale(0.0, max);
        display(array);

        // Run the appropriate sorting algorithm.
        StopwatchCPU stopwatch = new StopwatchCPU();

        switch (algorithm) {
            case BUG1:
                buggyBubbleSort(array);
                break;

            case BUG2:
                buggyInsertionSort(array);
                break;

            case BUBBLE:
                bubbleSort(array);
                break;

            case SHAKER:
                shakerSort(array);
                break;

            case INSERTION:
                insertionSort(array);
                break;

            case SELECTION:
                selectionSort(array);
                break;

            case SHELL:
                shellSort(array);
                break;

            case QUICK:
                quickSort(array);
                break;

            case QUICK3WAY:
                quick3Sort(array);
                break;

            case TOP_DOWN_MERGE:
                topDownMergeSort(array);
                break;

            case BOTTOM_UP_MERGE:
                bottomUpMergeSort(array);
                break;

            case HEAP:
                heapSort(array);
                break;

            case LSD_RADIX:
                radixSortLSD(array);
                break;

            case MSD_RADIX:
                radixSortMSD(array);
                break;

            case STOOGE:
                stoogeSort(array);
                break;

            default:
                break;
        }

        // Display stats if requested.
        if (statistics) {
            double time = stopwatch.elapsedTime();
            System.out.println("Time     = " + time);
            System.out.println("Swaps    = " + Statistics.swaps);
            System.out.println("Reads    = " + Statistics.reads);
            System.out.println("Writes   = " + Statistics.writes);
            System.out.println("Compares = " + Statistics.compares);
        }
    }

}
