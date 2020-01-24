
import java.util.HashMap;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #21
 * 4/13/18
 */

public class Expression {

    public static HashMap<String, Integer> symbols = new HashMap<>();

    private static enum Precedence {
        HIGHEST, UNARY, MULTIPLY, ADD, ASSIGN, LOW;

        public boolean lower(Precedence other) {
            return this.ordinal() < other.ordinal();
        }

        public boolean higher(Precedence other) {
            return this.ordinal() > other.ordinal();
        }
    };

    public static abstract class Node {

        public abstract int evaluate();

        public abstract String image();

        protected abstract Precedence precedence();

        protected boolean lowerPrecedence(Node other) {
            return this.precedence().lower(other.precedence());
        }
    }

    // -- Leaves --------------------------------------------------------------
    public static abstract class Leaf extends Node {

        protected Precedence precedence() {
            return Precedence.HIGHEST;
        }
    }

    public static class Literal extends Leaf {

        private int value;

        public Literal(int value) {
            this.value = value;
        }

        @Override
        public int evaluate() {
            return value;
        }

        @Override
        public String image() {
            return "" + value;
        }
    }

    public static class Identifier extends Leaf {

        private String name;

        public Identifier(String name) {
            this.name = name;
        }

        public String name() {
            return this.name;
        }

        @Override
        public int evaluate() {
            return symbols.get(name);
        }

        @Override
        public String image() {
            return this.name;
        }
    }

    // -- Unary Operators -----------------------------------------------------
    public abstract static class UnaryOperator extends Node {

        protected Node operand;

        public UnaryOperator(Node operand) {
            this.operand = operand;
        }

        @Override
        protected Precedence precedence() {
            return Precedence.UNARY;
        }

        @Override
        public int evaluate() {
            return evaluate(this.operand.evaluate());
        }

        @Override
        public String image() {
            String operand = this.operand.image();
            if (this.lowerPrecedence(this.operand)) {
                operand = parenthesize(operand);
            }
            return operator() + operand;
        }

        public abstract int evaluate(int operand);

        public abstract String operator();
    }

    public static class Identity extends UnaryOperator {

        public Identity(Node operand) {
            super(operand);
        }

        @Override
        public int evaluate(int value) {
            return this.operand.evaluate();
        }

        @Override
        public String operator() {
            return "+";
        }
    }

    public static class Negate extends UnaryOperator {

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

    // -- Unary L-Value Operators ---------------------------------------------
    public abstract static class UnaryUpdateOperator extends Node {

        protected Identifier operand;

        public UnaryUpdateOperator(Identifier operand) {
            this.operand = operand;
        }

        protected Precedence precedence() {
            return Precedence.UNARY;
        }

        public abstract int evaluate(String id);

        public abstract String operator();

    }

    public abstract static class UnaryPrefixOperator extends UnaryUpdateOperator {

        public UnaryPrefixOperator(Identifier operand) {
            super(operand);
        }

        @Override
        public int evaluate() {
            return evaluate(this.operand.name());
        }

        @Override
        public String image() {
            return operator() + this.operand.image();
        }
    }

    public abstract static class UnaryPostfixOperator extends UnaryUpdateOperator {

        public UnaryPostfixOperator(Identifier operand) {
            super(operand);
        }

        @Override
        public int evaluate() {
            return evaluate(this.operand.name());
        }

        @Override
        public String image() {
            return this.operand.image() + operator();
        }
    }

    public static class PrefixIncrement extends UnaryPrefixOperator {

        public PrefixIncrement(Identifier operand) {
            super(operand);
        }

        @Override
        public int evaluate(String id) {
            int value = symbols.get(id);
            symbols.put(id, ++value);
            return value;
        }

        @Override
        public String operator() {
            return "++";
        }
    }

    public static class PrefixDecrement extends UnaryPrefixOperator {

        public PrefixDecrement(Identifier operand) {
            super(operand);
        }

        @Override
        public int evaluate(String id) {
            int value = symbols.get(id);
            symbols.put(id, --value);
            return value;
        }

        @Override
        public String operator() {
            return "--";
        }
    }

    public static class PostfixIncrement extends UnaryPostfixOperator {

        public PostfixIncrement(Identifier operand) {
            super(operand);
        }

        @Override
        public int evaluate(String id) {
            int value = symbols.get(id);
            symbols.put(id, value+1);
            return value;
        }

        @Override
        public String operator() {
            return "++";
        }
    }

    public static class PostfixDecrement extends UnaryPostfixOperator {

        public PostfixDecrement(Identifier operand) {
            super(operand);
        }

        @Override
        public int evaluate(String id) {
            int value = symbols.get(id);
            symbols.put(id, value--);
            return value;
        }

        @Override
        public String operator() {
            return "--";
        }
    }

    // -- Binary Operators ----------------------------------------------------
    public abstract static class BinaryOperator extends Node {

        protected Node left;
        protected Node right;

        public BinaryOperator(Node left, Node right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int evaluate() {
            return evaluate(this.left.evaluate(), this.right.evaluate());
        }

        @Override
        public String image() {
            String left = this.left.image();
            String right = this.right.image();

            if (this.lowerPrecedence(this.left)) {
                left = parenthesize(left);
            }
            if (this.lowerPrecedence(this.right)) {
                right = parenthesize(right);
            }
            return left + " " + operator() + " " + right;
        }

        public abstract int evaluate(int left, int right);

        public abstract String operator();
    }

    public static class Add extends BinaryOperator {

        public Add(Node left, Node right) {
            super(left, right);
        }

        @Override
        protected Precedence precedence() {
            return Precedence.ADD;
        }

        @Override
        public int evaluate(int left, int right) {
            return left + right;
        }

        @Override
        public String operator() {
            return "+";
        }
    }

    public static class Subtract extends BinaryOperator {

        public Subtract(Node left, Node right) {
            super(left, right);
        }

        @Override
        protected Precedence precedence() {
            return Precedence.ADD;
        }

        @Override
        public int evaluate(int left, int right) {
            return left - right;
        }

        @Override
        public String operator() {
            return "-";
        }
    }

    public static class Multiply extends BinaryOperator {

        public Multiply(Node left, Node right) {
            super(left, right);
        }

        @Override
        protected Precedence precedence() {
            return Precedence.MULTIPLY;
        }

        @Override
        public int evaluate(int left, int right) {
            return left * right;
        }

        @Override
        public String operator() {
            return "*";
        }
    }

    public static class Divide extends BinaryOperator {

        public Divide(Node left, Node right) {
            super(left, right);
        }

        @Override
        protected Precedence precedence() {
            return Precedence.MULTIPLY;
        }

        @Override
        public int evaluate(int left, int right) {
            return left / right;
        }

        @Override
        public String operator() {
            return "/";
        }
    }

    public static class Mod extends BinaryOperator {

        public Mod(Node left, Node right) {
            super(left, right);
        }

        @Override
        protected Precedence precedence() {
            return Precedence.MULTIPLY;
        }

        @Override
        public int evaluate(int left, int right) {
            return left % right;
        }

        @Override
        public String operator() {
            return "%";
        }
    }

    // -- Assignment Operators ------------------------------------------------
    public abstract static class AssignmentOperator extends Node {

        protected Identifier left;
        protected Node right;

        public AssignmentOperator(Identifier left, Node right) {
            this.left = left;
            this.right = right;
        }

        @Override
        protected Precedence precedence() {
            return Precedence.ASSIGN;
        }

        @Override
        public int evaluate() {
            return evaluate(left.name(), this.right.evaluate());
        }

        @Override
        public String image() {
            return this.left.image() + " " + operator() + " " + this.right.image();
        }

        public abstract int evaluate(String id, int value);

        public abstract String operator();
    }

    public static class Assign extends AssignmentOperator {

        public Assign(Identifier left, Node right) {
            super(left, right);
        }

        @Override
        public int evaluate(String id, int value) {
            symbols.put(id, value);
            return value;
        }

        @Override
        public String operator() {
            return "=";
        }
    }

    public static class AddTo extends AssignmentOperator {

        public AddTo(Identifier left, Node right) {
            super(left, right);
        }

        @Override
        public int evaluate(String id, int value) {
            int result = symbols.get(id);
            result += value;
            symbols.put(id, result);
            return result;
        }

        @Override
        public String operator() {
            return "+=";
        }
    }

    public static class SubtractFrom extends AssignmentOperator {

        public SubtractFrom(Identifier left, Node right) {
            super(left, right);
        }

        @Override
        public int evaluate(String id, int value) {
            int result = symbols.get(id);
            result -= value;
            symbols.put(id, result);
            return result;
        }

        @Override
        public String operator() {
            return "-=";
        }
    }

    public static class MultiplyBy extends AssignmentOperator {

        public MultiplyBy(Identifier left, Node right) {
            super(left, right);
        }

        @Override
        public int evaluate(String id, int value) {
            int result = symbols.get(id);
            result *= value;
            symbols.put(id, result);
            return result;
        }

        @Override
        public String operator() {
            return "*=";
        }
    }

    public static class DivideBy extends AssignmentOperator {

        public DivideBy(Identifier left, Node right) {
            super(left, right);
        }

        @Override
        public int evaluate(String id, int value) {
            int result = symbols.get(id);
            result /= value;
            symbols.put(id, result);
            return result;
        }

        @Override
        public String operator() {
            return "/=";
        }
    }

    public static class ModBy extends AssignmentOperator {

        public ModBy(Identifier left, Node right) {
            super(left, right);
        }

        @Override
        public int evaluate(String id, int value) {
            int result = symbols.get(id);
            result %= value;
            symbols.put(id, result);
            return result;
        }

        @Override
        public String operator() {
            return "%=";
        }
    }

    private static String parenthesize(String s) {
        return "(" + s + ")";
    }

    public static void main(String[] args) {
        Node x = new Assign(new Identifier("X"), new Literal(5));
        Node y = new Assign(new Identifier("Y"), new Multiply(new Literal(2), new Subtract(new Identifier("X"), new Literal(1))));
        Node z = new Assign(new Identifier("Z"), new Divide(new Negate(new Add(new Identifier("Y"), new Literal(2))), new Identifier("X")));

        Node zz = new AddTo(new Identifier("Z"), new PrefixIncrement(new Identifier("X")));

        System.out.println(x.image() + " ==> " + x.evaluate());
        System.out.println(y.image() + " ==> " + y.evaluate());
        System.out.println(z.image() + " ==> " + z.evaluate());
        System.out.println(zz.image() + " ==> " + zz.evaluate());
    }
}
