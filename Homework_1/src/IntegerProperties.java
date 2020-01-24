
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #1 
 * 8/25/17
 */
public class IntegerProperties {

    public static class FactoringException extends Exception { // Exception handler for when a factoring error occurs.

        public FactoringException(String message) { // Constructor which takes the error message.
            super(message); // Call Exception's constructor to handle the message. 
        }
    }

    private enum Type { // Enums to label classifications
        PRIME,
        PERFECT,
        AMICABLE,
        SOCIABLE,
        ASOCIAL
    }
    // ArrayList to hold the list of numbers that will be printed 
    // if the number is amicable or sociable.
    private ArrayList<BigInteger> arrayToPrint = new ArrayList();

    /**
     * @param number
     * @throws IntegerProperties.FactoringException
     *
     * This is the master classification method in this class, which assigns an
     * enum type to a variable based on the nested-if tree below.
     */
    public void classify(BigInteger number) throws FactoringException {

        if (number.intValue() <= 0) {
            throw new FactoringException("Bad value: " + number + " cannot be classified");
        }

        Type classification = Type.ASOCIAL;
        if (!isPrime(number)) { // If number is not prime...
            if (!isPerfect(number)) { // If number is also not perfect...
                if (!isAmicable(number)) { // If number is also not amicable...
                    if (!isSociable(number)) { // Finally, if the number is also not sociable...
                        classification = Type.ASOCIAL; // It is none of the above
                    } else {
                        classification = Type.SOCIABLE; // It is sociable with a period of 3 or more (if the period was 2 it would be returned as AMICABLE).
                    }
                } else {
                    classification = Type.AMICABLE; // It is AMICABLE.
                }
            } else {
                classification = Type.PERFECT; // It is PERFECT.
            }
        } else {
            classification = Type.PRIME; // It is PRIME.
        }

        // If the array of numbers has stuff in it, add it to the output.
        if (arrayToPrint.size() > 1) {
            System.out.println(number + ": " + classification + " " + arrayToPrint);
        } else {
            // Otherwise, just print the number and its classification.
            System.out.println(number + ": " + classification);
        }
        arrayToPrint.clear(); // Clear the list for the next operation (if there is one).
    }

    /**
     * @param number
     * @return ArrayList of @param number's factors in BigInteger form.
     * @throws FactoringException
     */
    private ArrayList<BigInteger> getFactors(BigInteger number) throws FactoringException {
        ArrayList<BigInteger> list = new ArrayList(); // List to store factors.
        if (number.intValue() <= 0) { // First check if number is 0 or negative.
            // If so, throw the custom exception.
            throw new FactoringException("Bad value: " + number + " cannot be factored");
        }
        // For loop to find the factors of number.
        for (int i = 1; i < number.intValue(); i++) {
            if (number.intValue() % i == 0) { // If number % i is 0, i is a factor.
                list.add(BigInteger.valueOf(i)); // So add it to the list.
                //System.out.println(i);
            }
        }

        return list; // Return the list.
    }

    /**
     * @param number
     * @return The aliquot sum of @param number
     */
    private BigInteger getAliquotSum(BigInteger number) {
        BigInteger sum = BigInteger.ZERO; // Initalize the sum to 0.
        // Try-catch to resolve any factoring issues that may arise.
        try {
            for (BigInteger num : getFactors(number)) {
                sum = sum.add(num); // Get the sum of the factors.
            }
        } catch (FactoringException e) {
        }
        //System.out.println(sum); // Testing.
        return sum; // Return that sum
    }

    /**
     * @param number
     * @return True if @param number is a prime number, false if it is not
     */
    private boolean isPrime(BigInteger number) {
        // Try-catch to resolve any factoring issues that may arise.
        try {
            // For number to be prime, its only factors must be 1 and itself 
            // (itself is not included in the list in the getFactors method).
            if (getFactors(number).size() == 1) {
                return true;
            }
        } catch (FactoringException e) {
        }
        return false; // If the code reaches here, number is not prime.
    }

    /**
     * @param number
     * @return True if @param number is a perfect number, false if it is not.
     */
    private boolean isPerfect(BigInteger number) {
        // For a number to be perfect, it must equal its aliquot sum.
        if (number.equals(getAliquotSum(number))) {
            return true; // If the two are equal, number is perfect.
        }
        return false; // Otherwise, number is not perfect.
    }

    /**
     * @param number
     * @return True if @param number is an amicable number, false if it is not.
     */
    private boolean isAmicable(BigInteger number) {
        BigInteger aliquotSum1 = getAliquotSum(number); // Aliquot sum of number (Ex. 284)
        BigInteger aliquotSum2 = getAliquotSum(aliquotSum1); // Aliquot sum of the above aliquot sum (Ex. 220)
        if (number.equals(aliquotSum2)) { // If the number equals the aliquot sum OF ITS ALIQUOT SUM, then it is amicable (i.e. social with a period of 2).
            arrayToPrint.add(number); // So add those to numbers to the print array.
            arrayToPrint.add(aliquotSum1);
            return true; // And return true.
        }
        return false; // False otherwise.

    }

    /**
     * @param num1
     * @param num2
     * @return True if @param num1 is amicable to @param num2, and vice-versa.
     *
     * This overloaded method exists specifically for numbers with the case of
     * 562, in which the sequence starts off asocial, but then loops infinitely
     * between two amicable numbers (Ex. 220 and 284).
     */
    private boolean isAmicable(BigInteger num1, BigInteger num2) {
        return getAliquotSum(num1).equals(num2) && getAliquotSum(num2).equals(num1);
    }

    /**
     * @param number
     * @return ArrayList of the aliquot sequence of @param number in BigInteger
     * form.
     * 
     * EDIT: Somehow, I just realized that I do not use this method anywhere else
     * in the program. I likely could use it to replace code elsewhere, but oh well.
     * I'll keep it here because I liked writing it.
     */
    private ArrayList<BigInteger> getAliquotSequence(BigInteger number) {
        ArrayList<BigInteger> aliquotSequence = new ArrayList(); // List to store the values.
        aliquotSequence.add(number); // An aliquot sequence on a number always starts with itself;
        BigInteger lastNumAdded = number; // Store the last number that was added.
        while (!lastNumAdded.equals(BigInteger.ZERO)) { // As long as the last number is not 0 (which would signify the start of an infinite sequence)...
            aliquotSequence.add(getAliquotSum(lastNumAdded)); // Add the aliquot sum of the predecessor.
            lastNumAdded = getAliquotSum(lastNumAdded); // And remember the current value for the next iteration.
        }
        //System.out.println("Sequence: " + aliquotSequence); // Testing.
        return aliquotSequence; // Finally, return the sequence.

    }

    /**
     * @param number
     * @return True if @param number is sociable, false if it is not.
     */
    private boolean isSociable(BigInteger number) {
        ArrayList<BigInteger> list = new ArrayList(); // List for the sequence.
        BigInteger lastValue = number; // Variable to keep track of the predecessor.
        list.add(lastValue); // Add the value to the start as the starting point.

        /**
         * This do-while loop adds the aliquot sum of the previous number as
         * long as: 1. The last-added value is not 0 (which would signify the
         * beginning of an infinite sequence). 2. The last-added value is not
         * the same number as the first (which would signify another period of
         * repetition, so we can stop now because the number is sociable). I
         * used a do-while instead of a while loop because of the order in which
         * the condition is tested. If I used a while loop, the condition would
         * be false the first time it tried to run (because
         *
         * @param number would be the only number in the list). The do-while
         * avoids this issue by adding another value first and THEN testing.
         */
        do {
            //System.out.println("Adding: " + getAliquotSum(lastValue));
            if (list.get(list.size() - 1).equals(BigInteger.ZERO)) {
                //System.out.println(list);
                return false;
            } else {
                list.add(getAliquotSum(lastValue)); // Add the aliquot sum of that value.
                lastValue = getAliquotSum(lastValue); // Remember that value.
                /**
                 * If statement to catch the special case of 562, where the
                 * aliquot sequence repeats infinitely between 220 and 284.
                 *
                 * As for the 54,176 issue, upon generating an aliquot sequence
                 * of it, the sequence starts off not sociable (52546, 36158,
                 * 18922, 9464) but then repeats infinitely when it reaches the
                 * sociable number 12496. This makes it especially difficult to
                 * detect because in order to detect a sociable number, it must
                 * be run infinitely.
                 */

                // If the last value and its predecessor are amicable to each other,
                // then this is the start of a periodic cycle (Ex. in the case of 
                // 562) so we can return false. This mechanism is specifically
                // designed to catch that.
                if (isAmicable(list.get(list.size() - 2), list.get(list.size() - 1))) {
                    return false;
                }
            }
        } while (!list.get(0).equals(list.get(list.size() - 1)));

        list.remove(list.size() - 1); // Remove the duplicate number at the end.

        //System.out.print(list); // Testing.
        arrayToPrint = list; // Remember the list for output later.

        return true; // And finally return true because the number is sociable.
    }
}
