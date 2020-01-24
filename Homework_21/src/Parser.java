
/**
 * Kenny Akers 
 * Mr. Paige 
 * Homework #21
 * 4/13/18
 */
public class Parser {

    private static String expression;
    private static Scanner scanner;
    private static Scanner.Token currentToken;
    private static Scanner.Token tempToken;
    private static boolean verdict;

    private static boolean parseExpression() {
        // Expr --> ID AssignOp Expr
        // Expr --> Group
        tempToken = currentToken;
        verdict = parseGroup();
        if (tempToken.kind().equals(Scanner.Token.Kind.IDENTIFIER)) {
            switch (currentToken.kind()) {
                case EQUAL:
                case PLUS_EQUAL:
                case MINUS_EQUAL:
                case MULT_EQUAL:
                case DIV_EQUAL:
                case MOD_EQUAL:
                    currentToken = scanner.next();
                    verdict = parseExpression();
            }
        }
        return verdict;
    }

    private static boolean parseGroup() {
        // Group --> Group AddSubOp Term
        // Group --> Term
        boolean result = parseTerm();
        switch (currentToken.kind()) {
            case PLUS:
            case MINUS:
                currentToken = scanner.next();
                result = parseTerm();
        }
        return result;
    }

    private static boolean parseTerm() {
        // Term --> Term MultOp Factor
        // Term --> Factor 

        boolean result = parseFactor();
        switch (currentToken.kind()) {
            case MULTIPLY:
            case DIVIDE:
            case MOD:
                currentToken = scanner.next();
                result = parseFactor();
        }
        return result;
    }

    private static boolean parseFactor() {
        // Factor --> UnaryOp Factor
        // Factor --> Atom
        switch (currentToken.kind()) {
            case PLUS:
            case MINUS:
                currentToken = scanner.next();
                return parseFactor();
            default:
                return parseAtom();
        }
    }

    private static boolean parseAtom() {
        // Atom --> ID
        // Atom --> #
        // Atom --> ID IncDecOp   ****Moved this from Factor****
        // Atom --> IncDecOp ID  ****Moved this from Factor****
        // Atom --> (Expr)

        switch (currentToken.kind()) {
            case INTEGER:
                currentToken = scanner.next();
                return true;
            case IDENTIFIER:
                currentToken = scanner.next();
                if (currentToken.kind().equals(Scanner.Token.Kind.PLUS_PLUS) || currentToken.kind().equals(Scanner.Token.Kind.MINUS_MINUS)) { // For Atom --> ID IncDecOp
                    currentToken = scanner.next(); // Skip over the ++ or --
                }
                return true;
            case PLUS_PLUS:
            case MINUS_MINUS:
                currentToken = scanner.next();
                if (currentToken.kind().equals(Scanner.Token.Kind.IDENTIFIER)) { // For Atom --> IncDecOp ID
                    currentToken = scanner.next(); // Skip over the ID
                    return true;
                }
                printError("parseAtom", Scanner.Token.Kind.IDENTIFIER, currentToken.kind());
                return false; // No ID after, so not a valid Atom
            case LEFT_PARENTHESIS:
                currentToken = scanner.next();
                if (parseExpression()) {
                    if (currentToken.kind().equals(Scanner.Token.Kind.RIGHT_PARENTHESIS)) {
                        currentToken = scanner.next();
                        return true; // Got a (, then the expression was good, then got a ), so this is a good (Expr). 
                    } else {
                        printError("parseAtom", Scanner.Token.Kind.RIGHT_PARENTHESIS, currentToken.kind());
                        return false;
                    }
                } else {
                    System.out.println("ERROR in parseAtom: Invalid expression after '('");
                    return false;
                }
            default:
                return false; // Not an atom;
        }
    }

    private static void printError(String methodName, Scanner.Token.Kind expectedKind, Scanner.Token.Kind receivedKind) {
        System.out.println("ERROR in " + methodName + ": Expected " + expectedKind + ", but got " + receivedKind);
    }

    public static boolean isValidExpression(String expression) {
        verdict = false;
        scanner = new Scanner(expression);
        currentToken = scanner.next();
        return parseExpression() && currentToken.kind().equals(Scanner.Token.Kind.END_OF_LINE);
    }

}
