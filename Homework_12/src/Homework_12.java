
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #12
 * 12/1/17
 */
public class Homework_12 {

    private static MinPriorityQueue<Integer> minQ;
    private static MaxPriorityQueue<Integer> maxQ;

    public static void main(String[] args) {
        if (StdIn.isEmpty()) {
            System.out.println("No numbers.");
            return;
        }
        int[] array = StdIn.readAllInts();
        minQ = new MinPriorityQueue<>(array.length);
        maxQ = new MaxPriorityQueue<>(array.length);

        for (int num : array) { // Add each number to one of the heaps.
            add(num);
        }
        System.out.println("Median: " + getMedian());
    }

    private static double getMedian() {
        if (maxQ.size() > minQ.size()) {
            return maxQ.max();
        } else if (minQ.size() > maxQ.size()) {
            return minQ.min();
        }
        return (maxQ.max() + minQ.min()) / 2.0;
    }

    private static void add(int item) {
        if (maxQ.size() == 0 || item < maxQ.max()) { // If this number is less than the root of the minQ, or it's the first number.
            maxQ.add(item);
        } else {
            minQ.add(item);
        }
        rebalance();
    }

    private static void rebalance() {
        if (Math.abs(minQ.size() - maxQ.size()) >= 2) { // If there is a size difference of 2 or more...
            if (minQ.size() > maxQ.size()) { // And minQ is the bigger one...
                maxQ.add(minQ.removeMin()); // Add the root to the smaller queue (maxQ).
            } else { // Otherwise, maxQ is the bigger one.
                minQ.add(maxQ.removeMax()); // So add the root of maxQ to the smaller queue (minQ).
            }
        }
    }
}
