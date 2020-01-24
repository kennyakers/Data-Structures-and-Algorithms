
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class IncrementDecrement extends UnaryOperator {

    public IncrementDecrement(Identifier operand, int precedence) {
        super(operand, precedence); // An increment or decrement can have a precedence of 1 or 2.
    }
}

class PostIncrement extends IncrementDecrement {

    public PostIncrement(Identifier operand) {
        super(operand, 1); // Postfix unary operators have a precedence of 1.
    }

    @Override
    public int evaluate(int value) {
        SymbolTable.table.replace(this.operand.format(), value + 1); // Add 1 to the value. How to access variable?
        return value - 1; // Return the original value.
    }

    @Override
    public String operator() {
        return "++";
    }
}

class PostDecrement extends IncrementDecrement {

    public PostDecrement(Identifier operand) {
        super(operand, 1); // Postfix unary operators have a precedence of 1.
    }

    @Override
    public int evaluate(int value) {
        SymbolTable.table.replace(this.operand.format(), value - 1);
        return value + 1; // Return the original value.
    }

    @Override
    public String operator() {
        return "--";
    }
}

class PreIncrement extends IncrementDecrement {

    public PreIncrement(Identifier operand) {
        super(operand, 2); // Prefix unary operators have a precedence of 2.
    }

    @Override
    public int evaluate(int value) {
        SymbolTable.table.replace(this.operand.format(), value + 1); // Add 1 to the value.
        return value; // Return new value.
    }

    @Override
    public String operator() {
        return "++";
    }
}

class PreDecrement extends IncrementDecrement {

    public PreDecrement(Identifier operand) {
        super(operand, 2); // Prefix unary operators have a precedence of 2.
    }

    @Override
    public int evaluate(int value) {
        SymbolTable.table.replace(this.operand.format(), value - 1); // Subtract 1 from the value.
        return value; // Return new value.
    }

    @Override
    public String operator() {
        return "--";
    }
}
