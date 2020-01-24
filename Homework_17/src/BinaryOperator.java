
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class BinaryOperator extends Node {

    protected Node left;
    protected Node right;

    public BinaryOperator(Node left, Node right, int precedence) {
        super(precedence); // Binary operators can have either precedence 4 or 5.
        this.left = left;
        this.right = right;
    }

    @Override
    public String format() {

        String formatted = this.left.format() + " " + operator() + " " + this.right.format();
        if (/*this.precedence > this.left.precedence ||*/this.precedence < this.right.precedence) {
            formatted = "(" + formatted + ")";
        }
        return formatted;

    }

    public abstract String operator();

}
