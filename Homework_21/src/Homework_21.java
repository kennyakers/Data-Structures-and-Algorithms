
import java.io.Console;

/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #21
 * 4/13/18
 */
public class Homework_21 {

    private static String expression;
    private static Scanner scanner;
    private static Scanner.Token currentToken;
    private static Scanner.Token tempToken;

    private static ExpressionTree parseExpression() throws UndefinedVariableException {
        // Expr --> ID AssignOp Expr
        // Expr --> Group
        tempToken = currentToken; // Store the possible ID
        ExpressionTree result = parseGroup();
        if (tempToken.kind().equals(Scanner.Token.Kind.IDENTIFIER)) {
            switch (currentToken.kind()) {
                case EQUAL:
                case PLUS_EQUAL:
                case MINUS_EQUAL:
                case MULT_EQUAL:
                case DIV_EQUAL:
                case MOD_EQUAL:
                    // tempToken: ID
                    // tempToken2: operator
                    Scanner.Token assignOp = currentToken;
                    currentToken = scanner.next();
                    result = new AssignmentExpressionTree(tempToken, assignOp, parseExpression());
            }
        }
        return result;
    }

    private static ExpressionTree parseGroup() throws UndefinedVariableException {
        // Group --> Group AddSubOp Term
        // Group --> Term
        ExpressionTree result = parseTerm();
        switch (currentToken.kind()) {
            case PLUS:
            case MINUS:
                tempToken = currentToken; // Store the operator
                currentToken = scanner.next();
                result = new BinaryOperatorExpressionTree(result, tempToken, parseTerm());
        }
        return result;
    }

    private static ExpressionTree parseTerm() throws UndefinedVariableException {
        // Term --> Term MultOp Factor
        // Term --> Factor

        ExpressionTree result = parseFactor();
        switch (currentToken.kind()) {
            case MULTIPLY:
            case DIVIDE:
            case MOD:
                tempToken = currentToken; // Store the operator
                currentToken = scanner.next();
                result = new BinaryOperatorExpressionTree(result, tempToken, parseFactor());
        }
        return result;
    }

    private static ExpressionTree parseFactor() throws UndefinedVariableException {
        // Factor --> UnaryOp Factor
        // Factor --> Atom
        ExpressionTree result = null;
        switch (currentToken.kind()) {
            case PLUS:
            case MINUS:
                tempToken = currentToken; // Store the + or -
                currentToken = scanner.next();
                result = new UnaryOperatorExpressionTree(tempToken, parseAtom());
                break;
            default:
                result = parseAtom();
        }
        return result;
    }

    private static ExpressionTree parseAtom() throws UndefinedVariableException {
        // Atom --> ID
        // Atom --> #
        // Atom --> ID IncDecOp
        // Atom --> IncDecOp ID
        // Atom --> (Expr)

        ExpressionTree result = null;
        switch (currentToken.kind()) {
            case IDENTIFIER:
                tempToken = currentToken; // Store the ID
                currentToken = scanner.next(); // Go to next to look for postfix ++ or --
                if (currentToken.kind().equals(Scanner.Token.Kind.PLUS_PLUS) || currentToken.kind().equals(Scanner.Token.Kind.MINUS_MINUS)) { // For Atom --> ID IncDecOp)
                    result = new PostfixExpressionTree(currentToken.kind(), tempToken);
                    currentToken = scanner.next(); // Skip over the ++ or --
                } else {
                    result = new IdentifierExpressionTree(tempToken);
                }
                break;
            case INTEGER:
                result = new IntegerExpressionTree(currentToken);
                currentToken = scanner.next();
                break;
            case PLUS_PLUS: // Atom --> IncDecOp ID
                currentToken = scanner.next();
                result = new PrefixExpressionTree(Scanner.Token.Kind.PLUS_PLUS, currentToken);
                break;
            case MINUS_MINUS: // Atom --> IncDecOp ID
                currentToken = scanner.next();
                result = new PrefixExpressionTree(Scanner.Token.Kind.MINUS_MINUS, currentToken);
                break;
            case LEFT_PARENTHESIS:
                currentToken = scanner.next(); // Go to first token of the expression.
                result = parseExpression(); // Parse it
                currentToken = scanner.next(); // Skip over the ')'
                break;
            default:
                System.out.println("ERROR in parseAtom");
        }
        return result;
    }

    public static void main(String[] args) {
        Console console = System.console();
        String prompt = "Expression: ";
        expression = console.readLine(prompt);
        ExpressionTree result = null;
        while (expression != null && expression.length() > 0) {
            if (Parser.isValidExpression(expression)) {
                scanner = new Scanner(expression);
                currentToken = scanner.next();
                try {
                    result = parseExpression();
                    try {
                        System.out.println("Result: " + result.evaluate());

                    } catch (EvaluationException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (UndefinedVariableException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Invalid Expression: " + expression);
            }
            expression = console.readLine(prompt);
        }
    }

    private static class ExpressionTree {

        protected Expression.Node root;
        protected ExpressionTree leftSubtree;
        protected ExpressionTree rightSubtree;

        public ExpressionTree() {
            this.root = null;
            this.leftSubtree = null;
            this.rightSubtree = null;
        }

        public ExpressionTree(Expression.Node root) {
            this.root = root;
        }

        private int evaluate() throws EvaluationException {
            try {
                return root.evaluate();
            } catch (NullPointerException e) {
                throw new EvaluationException(this);
            }
        }
    }

    private static class AssignmentExpressionTree extends ExpressionTree {

        public AssignmentExpressionTree(Scanner.Token id, Scanner.Token operator, ExpressionTree expr) throws UndefinedVariableException {
            if (expr.root instanceof Expression.Identifier) {
                checkIfDefined(expr.root.image());
            }
            switch (operator.kind()) {
                case EQUAL:
                    this.root = new Expression.Assign(new Expression.Identifier(id.toString()), expr.root);
                    break;
                case PLUS_EQUAL:
                    checkIfDefined(id.toString());
                    this.root = new Expression.AddTo(new Expression.Identifier(id.toString()), expr.root);
                    break;
                case MINUS_EQUAL:
                    checkIfDefined(id.toString());
                    this.root = new Expression.SubtractFrom(new Expression.Identifier(id.toString()), expr.root);
                    break;
                case MULT_EQUAL:
                    checkIfDefined(id.toString());
                    this.root = new Expression.MultiplyBy(new Expression.Identifier(id.toString()), expr.root);
                    break;
                case DIV_EQUAL:
                    checkIfDefined(id.toString());
                    this.root = new Expression.DivideBy(new Expression.Identifier(id.toString()), expr.root);
                    break;
                case MOD_EQUAL:
                    checkIfDefined(id.toString());
                    this.root = new Expression.ModBy(new Expression.Identifier(id.toString()), expr.root);
                    break;
                default:
                    System.out.println("Bad AssignOp: " + operator);
            }
        }
    }

    private static class BinaryOperatorExpressionTree extends ExpressionTree {

        public BinaryOperatorExpressionTree(ExpressionTree left, Scanner.Token operator, ExpressionTree right) throws UndefinedVariableException {
            if (left.root instanceof Expression.Identifier) {
                checkIfDefined(left.root.image());
            } else if (right.root instanceof Expression.Identifier) {
                checkIfDefined(right.root.image());
            }
            switch (operator.kind()) {
                case MULTIPLY:
                    this.root = new Expression.Multiply(left.root, right.root);
                    break;
                case DIVIDE:
                    this.root = new Expression.Divide(left.root, right.root);
                    break;
                case MOD:
                    this.root = new Expression.Mod(left.root, right.root);
                    break;
                case PLUS:
                    this.root = new Expression.Add(left.root, right.root);
                    break;
                case MINUS:
                    this.root = new Expression.Subtract(left.root, right.root);
                    break;
                default:
                    System.out.println("Bad BinaryOp: " + operator);
            }
        }
    }

    private static class UnaryOperatorExpressionTree extends ExpressionTree {

        public UnaryOperatorExpressionTree(Scanner.Token operator, ExpressionTree operand) throws UndefinedVariableException {
            if (operand.root instanceof Expression.Identifier) {
                checkIfDefined(operand.root.image());
            }
            switch (operator.kind()) {
                case MINUS:
                    this.root = new Expression.Negate(operand.root);
                    break;
                case PLUS:
                    this.root = new Expression.Identity(operand.root);
                    break;
                default:
                    System.out.println("Bad UnaryOp: " + operator);
            }
        }
    }

    private static class PrefixExpressionTree extends ExpressionTree {

        public PrefixExpressionTree(Scanner.Token.Kind operator, Scanner.Token id) throws UndefinedVariableException {
            checkIfDefined(id.toString());
            switch (operator) {
                case MINUS_MINUS:
                    this.root = new Expression.PrefixDecrement(new Expression.Identifier(id.toString()));
                    break;
                case PLUS_PLUS:
                    this.root = new Expression.PrefixIncrement(new Expression.Identifier(id.toString()));
                    break;
                default:
                    System.out.println("Bad PrefixOp: " + operator);
            }
        }
    }

    private static class PostfixExpressionTree extends ExpressionTree {

        public PostfixExpressionTree(Scanner.Token.Kind operator, Scanner.Token id) throws UndefinedVariableException {
            checkIfDefined(id.toString());
            switch (operator) {
                case MINUS_MINUS:
                    this.root = new Expression.PostfixDecrement(new Expression.Identifier(id.toString()));
                    break;
                case PLUS_PLUS:
                    this.root = new Expression.PostfixIncrement(new Expression.Identifier(id.toString()));
                    break;
                default:
                    System.out.println("Bad PostfixOp: " + operator);
            }
        }
    }

    private static class IntegerExpressionTree extends ExpressionTree {

        public IntegerExpressionTree(Scanner.Token number) {
            this.root = new Expression.Literal(Integer.valueOf(number.toString()));
        }
    }

    private static class IdentifierExpressionTree extends ExpressionTree {

        public IdentifierExpressionTree(Scanner.Token id) {
            this.root = new Expression.Identifier(id.toString());
        }
    }

    private static class UndefinedVariableException extends Exception {

        public UndefinedVariableException(String variable) {
            super("Variable " + "\"" + variable + "\"" + " was not defined");
        }
    }

    private static class EvaluationException extends Exception {

        public EvaluationException(ExpressionTree tree) {
            super("The expression " + "\"" + tree.root.image() + "\"" + " cannot be evaluated");
        }
    }

    private static void checkIfDefined(String variable) throws UndefinedVariableException {
        if (!Expression.symbols.containsKey(variable)) {
            throw new UndefinedVariableException(variable);
        }
    }
}
