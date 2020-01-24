
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #4
 * 9/15/17
 */
public class ArrayQueue<T> implements Queue<T> {

    private Array<T> array; // Implement a queue using the resizing array class written previously. 
    private int maxCapacity;

    public ArrayQueue(int capacity) { // Constructor to specify the maximum capacity.
        array = new Array(); // Create new array with inital size of 1.
        this.maxCapacity = capacity; // Keep track of the max capacity so the array never gets bigger than it.
    }

    @Override
    public boolean isEmpty() { // Return true if the size is 0.
        return array.size() == 0;
    }

    @Override
    public T head() throws Queue.EmptyException { // Return the first element (at index 0). 
        try {
            return array.get(0);
        } catch (IndexOutOfBoundsException e) {
            // This catch block will only be reached if array.get fails and raises an IndexOutOfBoundsException. 
            // This means that the queue is empty, so the corresponding exception is thrown.
            throw new Queue.EmptyException();
        }
    }

    @Override
    public void enqueue(T item) throws Queue.OverflowException {
        if (this.array.size() == this.maxCapacity) { // If the max capacity has already been reached...
            // Adding (enqueue-ing) another item will trigger an overflow exception.
            throw new Queue.OverflowException();
        }
        array.append(item); // Otherwise, add it to the end.
    }

    @Override
    public T dequeue() throws Queue.UnderflowException {
        try {
            T temp = array.get(0); // Store the value that was at the beginning (head).
            array.remove(temp); // Then remove that value.
            return temp; // Then return it.
        } catch (IndexOutOfBoundsException e) {
            // Catch block if there's nothing at that spot = underflow.
            throw new Queue.UnderflowException();
        }

    }

    @Override
    public String toString() { // To string for printing the queue.
        return array.toString();
    }
}
