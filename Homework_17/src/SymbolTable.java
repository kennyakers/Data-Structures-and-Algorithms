
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
import java.util.HashMap;

public class SymbolTable {

    // Class to hold a static class so all other classes can utilize it
    // while maintaining data continuity across all of them.
    private static final int CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.5f;

    public static HashMap<String, Integer> table;

    public static void initialize() {
        table = new HashMap<>(CAPACITY, LOAD_FACTOR);
    }
}
