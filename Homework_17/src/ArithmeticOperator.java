
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class ArithmeticOperator extends BinaryOperator {

    public ArithmeticOperator(Node left, Node right, int precedence) {
        super(left, right, precedence); // Arithmetic operators can have either a precedence of 4 or 5
    }
}

class Addition extends ArithmeticOperator {

    public Addition(Node left, Node right) {
        super(left, right, 5); // Addition has a precedence of 5.
    }

    @Override
    public int evaluate() {
        return this.left.evaluate() + this.right.evaluate();
    }

    @Override
    public String operator() {
        return "+";
    }
}

class Subtraction extends ArithmeticOperator {

    public Subtraction(Node left, Node right) {
        super(left, right, 5); // Subtraction has a precedence of 5.
    }

    @Override
    public int evaluate() {
        return this.left.evaluate() - this.right.evaluate();
    }

    @Override
    public String operator() {
        return "-";
    }
}

class Division extends ArithmeticOperator {

    public Division(Node left, Node right) {
        super(left, right, 4); // Division has a precedence of 4.
    }

    @Override
    public int evaluate() {
        return this.left.evaluate() / this.right.evaluate();
    }

    @Override
    public String operator() {
        return "/";
    }
}

class Multiplication extends ArithmeticOperator {

    public Multiplication(Node left, Node right) {
        super(left, right, 4); // Multiplication has a precedence of 4.
    }

    @Override
    public int evaluate() {
        return this.left.evaluate() * this.right.evaluate();
    }

    @Override
    public String operator() {
        return "*";
    }
}

class Modulus extends ArithmeticOperator {

    public Modulus(Node left, Node right) {
        super(left, right, 4); // Modulus has a precedence of 4.
    }

    @Override
    public int evaluate() {
        return this.left.evaluate() % this.right.evaluate();
    }

    @Override
    public String operator() {
        return "%";
    }
}
