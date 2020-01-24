
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class UnaryOperator extends Operator {

    protected Node operand;

    public UnaryOperator(Node operand, int precedence) {
        super(precedence);
        this.operand = operand;
    }

    @Override
    public int evaluate() {
        return evaluate(this.operand.evaluate());
    }

    @Override
    public String format() {
        String formatted = this.operator() + this.operand.format();
        if (this.precedence > this.operand.precedence) {
            formatted = "(" + formatted + ")";
        }
        return formatted;
    }

    public abstract int evaluate(int operand);

    public abstract String operator();
}
