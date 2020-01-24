
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class AssignmentOperator extends BinaryOperator {

    public AssignmentOperator(Node left, Node right) {
        super(left, right, 6); // Assignment operators have a precedence of 6.
    }
}

class AddAssignment extends AssignmentOperator {

    public AddAssignment(Identifier left, Node right) {
        super(left, right);
    }

    @Override
    public int evaluate() {
        SymbolTable.table.put(this.left.format(), this.left.evaluate() + this.right.evaluate());
        return this.left.evaluate() + this.right.evaluate();
    }

    @Override
    public String operator() {
        return "+=";
    }
}

class SubAssignment extends AssignmentOperator {

    public SubAssignment(Identifier left, Node right) {
        super(left, right);
    }

    public int evaluate() {
        SymbolTable.table.put(this.left.format(), this.left.evaluate() - this.right.evaluate());
        return this.left.evaluate() - this.right.evaluate();
    }

    @Override
    public String operator() {
        return "-=";
    }
}

class MultiplyAssignment extends AssignmentOperator {

    public MultiplyAssignment(Identifier left, Node right) {
        super(left, right);
    }

    @Override
    public int evaluate() {
        SymbolTable.table.put(this.left.format(), this.left.evaluate() * this.right.evaluate());
        return this.left.evaluate() * this.right.evaluate();
    }

    @Override
    public String operator() {
        return "*=";
    }
}

class DivideAssignment extends AssignmentOperator {

    public DivideAssignment(Identifier left, Node right) {
        super(left, right);
    }

    @Override
    public int evaluate() {
        SymbolTable.table.put(this.left.format(), this.left.evaluate() / this.right.evaluate());
        return this.left.evaluate() / this.right.evaluate();
    }

    @Override
    public String operator() {
        return "/=";
    }
}

class ModAssignment extends AssignmentOperator {

    public ModAssignment(Identifier left, Node right) {
        super(left, right);
    }

    @Override
    public int evaluate() {
        SymbolTable.table.put(this.left.format(), this.left.evaluate() % this.right.evaluate());
        return this.left.evaluate() % this.right.evaluate();
    }

    @Override
    public String operator() {
        return "%=";
    }
}

class Assignment extends AssignmentOperator {

    public Assignment(Identifier left, Node right) {
        super(left, right);
    }

    @Override
    public int evaluate() {
        SymbolTable.table.put(this.left.format(), this.right.evaluate());
        return this.left.evaluate(); // Return the thing on the left?
    }

    @Override
    public String operator() {
        return "=";
    }
}
