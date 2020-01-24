
import java.math.BigInteger;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #1 
 * 8/25/17
 */
public class Homework_1 {

    public static void main(String[] args) {
        IntegerProperties property = new IntegerProperties();
      
        for (String arg : args) {
            try {
                BigInteger value = BigInteger.valueOf(Integer.parseInt(arg));
                property.classify(value);
            } catch (IntegerProperties.FactoringException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
