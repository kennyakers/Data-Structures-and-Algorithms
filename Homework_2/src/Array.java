
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #2
 * 8/31/17
 *
 * @author kakers
 */

import java.util.Iterator;

public class Array<T> implements Iterable<T> {
// Constructors to create an empty Array and
// to create an initialized Array (using an array of T).

    private T[] array; // The array
    private int numValues; // How many values are currently in the array.
    private int storageSize;

    // Need to have variable for the current amount of slots filled, and another var for the total amount of 
    // storage allocated over the life of this array (1 + 2 + 4 = 7, so though it was only doubled twice and is currently
    // size 4, the total storage is 7). 
    public Array() {
        array = (T[]) new Object[1]; // Initalize empty array of size 1.
        numValues = 0; // There are no values in array.
        storageSize = 1; // One slot has been allocated to it so far.
    }

    public Array(T[] values) {
        //int size = (int) Math.pow(2.0, (double) (values.length - 1)); // For the doubling strategy, the size of the array is 2^(n-1).
        this.array = (T[]) new Object[values.length]; // Initalize array.
        this.storageSize = values.length;
        // Copy over the data from values to array.
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = values[i];
            this.numValues++; // Count how many values have been stored in array.
        }
    }

    // The size of the array and storage allocated so far.
    public int size() {
        return this.numValues; // Return how many values are stored in array
    }

    public int storage() {
        return this.storageSize; // Return the number of slots in the entire array (will be >= numValues).
    }

    /*public int length() { // Test method.
        return this.array.length;
    }
     */
    /**
     * Get/set individual elements of the array. Get should throw an exception
     * (an IndexOutOfBoundsException) when the index is past the size of the
     * array. Set should extend the array when the index is past the size of the
     * array.
     */
    public T get(int index) throws IndexOutOfBoundsException {
        if (index > this.array.length - 1) {
            throw new IndexOutOfBoundsException();
        }
        return this.array[index];
    }

    public void set(int index, T value) {
        if (index > this.array.length - 1) { // If the user wants to put a value at an index that is out of bounds...
            this.extendArray(index); // Extend the array to allow fit in @param index.
            this.array[index] = value; // Add @param value to the specified index.         
        } else {
            this.array[index] = value; // Otherwise, set the value, nothing needs to be extended.
        }
        // numValues does not need to be changed, because no values have been removed or added
    }

    /**
     * Extend this array by doubling.
     *
     * @param max the index that should be accessible by the end of the
     * doubling. Ex. If max is 6, the array will double to length 8 in order to
     * accommodate it.
     */
    private void extendArray(int max) {
        int newSize = this.array.length; // Store the current length.
        while (newSize <= max) { // While the array is too small to accommodate max...
            newSize *= 2; // Double it.
        }
        /*
        This was my original strategy for finding exactly how many doubles need to take place.
        I quickly discovered that when the array length is 1, a divide by 0 error occurs and 
        the array size gets multiplied by 1, which does nothing and yields an ArrayIndexOutOfBoundsException
        later on.
        
        int exponent = (int) Math.ceil(Math.log(max) / Math.log(this.array.length)); // Get how many doubles need to take place to be able to accommodate @param max.
        int newSize = this.array.length * (int) Math.pow(2.0, exponent); // The current length multiplied by 2^required doubling factor, yielding the new array's size.
         */
        T[] newArray = (T[]) new Object[newSize]; // Initialize the new array with the new size.
        for (int i = 0; i < this.array.length; i++) { // Copy over each value in this array to the same slot in newArray.
            newArray[i] = this.array[i];
        }
        this.array = newArray; // Replace the old array with the updated one.
        this.storageSize += newSize; // Add the new array length to the storage counter.  
        // numValues does not need to be changed, because no values have been removed or added.
    }

    /**
     * Methods to append and prepend elements to the end/beginning of the array.
     * Again, extend the array (by doubling its size) if necessary.
     */
    public void append(T value) {
        if (numValues == this.array.length) { // If this array has been saturated with values, the array needs to be doubled again.
            this.extendArray(this.array.length); // Extend the array to accommodate one more index.
            this.array[numValues] = value; // Store @param value at the spot right after the last value.
        } else {
            this.array[numValues] = value;
        }
        this.numValues++;
    }

    public void prepend(T value) {
        if (numValues == this.array.length) { // If this array has been saturated with values, the array needs to be doubled again.
            this.extendArray(numValues); // Extend the array.
        }
        for (int i = this.array.length - 1; i > 0; i--) { // Shift everything to the right by one.
            this.array[i] = this.array[i - 1];
        }
        this.array[0] = value; // Put @param value at the beginning.
        this.numValues++;

    }
// A method to check if a given element is in the array.

    public boolean contains(T value) {

        Iterator<T> iterator = this.forwardIterator(); // Using a forward iterator for this one, but it should work with the reverse iterator as well.
        while (iterator.hasNext()) { // While there is still more data in the array...
            if (iterator.next() == value) { // Check if the current value is @param value...
                return true; // Return true.
            }
        }
        return false; // @param value is not in this array.

        /*
        // The original foreach version of this method.
        for (T val : this.array) { // For-each loop, which uses the forward iterator by default.
            // *** .equals OR ==? ***
            if (val == value) { // If the current value is @param value...
                return true; // Return true;
            }
        }
        return false;
         */
    }

    /**
     * Methods (forwardIterator and reverseIterator) which return Iterators
     * which can be used to iterate over the elements of the array in either
     * direction - Don’t forget, this class needs to implement Iterable.
     */
    public Iterator<T> forwardIterator() {
        return this.new ArrayForwardIterator(); // Return the forward iterator.
    }

    public Iterator<T> reverseIterator() {
        return this.new ArrayReverseIterator(); // Return the reverse iterator.
    }

    @Override
    public Iterator<T> iterator() {
        return this.new ArrayForwardIterator(); // By default, return the forward iterator. Is this correct?
    }

    /**
     * @return Comma-separated list enclosed in { }
     */
    @Override
    public String toString() {
        String returnedString = "[";
        for (T value : this.array) { // For every value...
            if (value != null) { // If statement so not "{null}"s get printed.
                returnedString += ("{" + value + "}, "); // Add "{value}, "
            }
        }
        returnedString = returnedString.substring(0, returnedString.length() - 2); // Line solely to remove the last comma (picky, I know).
        returnedString += "]"; // Cap off the string with a ].
        return returnedString; // Return it.
    }

    private class ArrayForwardIterator implements Iterator<T> {

        private int current; // Current index variable.

        public ArrayForwardIterator() {
            this.current = 0; // Initially set to 0.
        }

        @Override
        public boolean hasNext() {
            return !(this.current == Array.this.array.length - 1); // Return true if current is not on the last index of the array.
        }

        @Override
        public T next() {
            /* 
            Next has two jobs to do:
            return the value associated with the current spot in the iteration.
            advance to the next spot in the iteration.
             */
            T result = Array.this.get(this.current);  // Save the current value.
            this.current++; // Advance to the next.
            return result; // Return the current value.
        }
    }

    private class ArrayReverseIterator implements Iterator<T> {

        private int current;

        public ArrayReverseIterator() {
            this.current = Array.this.array.length - 1; // Start at the last index
        }

        @Override
        public boolean hasNext() {
            return !(this.current == 0); // Return true if current is not on first index of the array.
        }

        @Override
        public T next() {
            /* 
            Next has two jobs to do:
            return the value associated with the current spot in the iteration.
            advance to the next spot in the iteration.
             */
            T result = Array.this.get(this.current);  // Save the current value.
            this.current--; // Advance to the next.
            return result; // Return the current value.
        }
    }
}
