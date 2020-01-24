
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class Identity extends UnaryOperator {

    public Identity(Node operand) {
        super(operand, 3); // Unary + and - have a precedence of 3.
    }
}

class Positive extends Identity {

    public Positive(Node operand) {
        super(operand);
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    @Override
    public String operator() {
        return "+";
    }
}

class Negate extends Identity {

    public Negate(Node operand) {
        super(operand);
    }

    @Override
    public int evaluate(int value) {
        return -value;
    }

    @Override
    public String operator() {
        return "-";
    }
}
