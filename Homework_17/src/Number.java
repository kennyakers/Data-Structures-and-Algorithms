
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public class Number extends Leaf {

    private final int value;

    public Number(int value) {
        super(7); // Numbers have a precedence of 4.
        this.value = value;
    }

    @Override
    public int evaluate() {
        return this.value;
    }

    @Override
    public String format() {
        return "" + this.value;
    }

}
