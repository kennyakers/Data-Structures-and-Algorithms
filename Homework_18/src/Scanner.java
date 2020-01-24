
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #18
 * 2/15/18
 */

import java.io.Console;
import java.util.Iterator;

public class Scanner implements Iterator<Scanner.Token>, Iterable<Scanner.Token> {

    public static class Token {

        protected int start;
        protected Kind kind;

        private static enum Kind {
            IDENTIFIER,
            INTEGER,
            LEFT_PARENTHESIS,
            RIGHT_PARENTHESIS,
            PLUS,
            MINUS,
            TIMES,
            DIVIDE,
            MOD,
            PLUSPLUS,
            MINUSMINUS,
            EQUALS,
            PLUS_EQUALS,
            MINUS_EQUALS,
            TIMES_EQUALS,
            DIVIDE_EQUALS,
            MOD_EQUALS,
            ERROR // For everything else.
        };

        public Token(Kind kind, int start) {
            this.kind = kind;
            this.start = start;
        }

        public Kind kind() { // What kind of token is this one?
            return this.kind;
        }

        public int start() { // Starting character position
            return this.start;
        }

        @Override
        public String toString() {
            switch (this.kind) {
                case LEFT_PARENTHESIS:
                    return "(";
                case RIGHT_PARENTHESIS:
                    return ")";
                case PLUS:
                    return "+";
                case MINUS:
                    return "-";
                case TIMES:
                    return "*";
                case DIVIDE:
                    return "/";
                case MOD:
                    return "%";
                case PLUSPLUS:
                    return "++";
                case MINUSMINUS:
                    return "--";
                case EQUALS:
                    return "=";
                case PLUS_EQUALS:
                    return "+=";
                case MINUS_EQUALS:
                    return "-=";
                case TIMES_EQUALS:
                    return "*=";
                case DIVIDE_EQUALS:
                    return "/=";
                case MOD_EQUALS:
                    return "%=";
                default:
                    return "Invalid token";
            }
        }
    }

    public static class IntegerToken extends Token {

        private final int value;

        public IntegerToken(int start, int value) {
            super(Token.Kind.INTEGER, start);
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        // Override toString to return the value (converted to a string)
        @Override
        public String toString() {
            return "" + this.value;
        }
    }

    public static class IdentifierToken extends Token {

        private final String name;

        public IdentifierToken(int start, String name) {
            super(Token.Kind.IDENTIFIER, start);
            this.name = name;
        }

        public String name() {
            return this.name;
        }

        // Override toString to return the identifier name
        @Override
        public String toString() {
            return this.name;
        }
    }

    private int start; // Stores where the token started.
    private int currentPos; // Counter for the current position.
    private String expression; // The entered expression.

    public Scanner(String expression) {
        this.expression = expression;
        this.start = 0;
        this.currentPos = 0;
    }

    @Override
    public boolean hasNext() { // Returns true if there are more characters to process.
        return this.currentPos < this.expression.length();
    }

    @Override
    public Token next() { // Returns the next token in the expression.
        Token.Kind kind = Token.Kind.ERROR; // The default kind for each token is an error.
        int masterState = 0; // State tracker for the "master" FSM (which houses the Integer, Variable, and Symbol FSM's).
        int numberState = 0; // State tracker for the Integer FSM.
        int variableState = 0; // State tracker for the Variable FSM.
        int symbolState = 0; // State tracker for the Symbol FSM.
        int state = 0; // State tracker for the various sub-FSM's within the Symbol FSM.
        boolean intUnderscores = false; // Flag denoting if this integer token contains underscores (for printing at the end).
        boolean stop = false; // Flag that stops the below while loop (true when a token has finished being processed).
        String returnedString; // The portion of the expression to be returned at the end after being parsed.
        char c; // The current character in the expression.

        while (!stop && this.currentPos < this.expression.length()) { // While this token is not finished and we're not at the end of the expression...
            c = this.expression.charAt(this.currentPos); // Store the current character.
            switch (masterState) { // Master FSM 
                case 0: // Start state to determine where to transition on the next iteration.
                    if (Character.isDigit(c)) { // If c is a number...
                        masterState = 1; // Go to Integer FSM.
                        kind = Token.Kind.INTEGER;
                    } else if (Character.isLetter(c)) { // If c is a letter...
                        masterState = 2; // Go to Variable FSM
                        kind = Token.Kind.IDENTIFIER;
                    } else if (this.isSymbol(c)) { // If c is a symbol (operator, etc.)
                        masterState = 3; // Go to Symbol FSM
                    } else if (c == ' ') { // If c is a whitespace character...
                        this.currentPos++; // Ignore and go the next character.
                    } else { // Otherwise, this is an invalid character.
                        kind = Token.Kind.ERROR;
                        start = this.currentPos++; // Ignore and go the next character.
                        stop = true; // Stop because this token is definitely an invalid one.
                    }
                    break;
                case 1: // Integer FSM
                    switch (numberState) {
                        case 0: // Start state
                            numberState = 1; // Go to integer state.
                            start = currentPos; // Remember where this token started.
                            break;
                        case 1: // Integer state
                            if (c == '_') {
                                numberState = 2; // Go to _ state.
                                intUnderscores = true;
                            } else if (Character.isDigit(c)) { // If c is a number.
                                numberState = 1; // Loop back on this case.
                            } else {
                                this.currentPos--; // Error state, so decrement currentPos to nullify the increment at the end.
                                numberState = 3; // Go to error state. This token is done.
                            }
                            break;
                        case 2: // '_' state
                            if (Character.isDigit(c)) {
                                numberState = 1; // Go back to Integer state
                            } else {
                                numberState = 3; // Go to error state
                            }
                            break;
                        case 3:
                            stop = true; // Stop
                            this.currentPos++;
                            break;
                    }
                    this.currentPos += stop ? -1 : 1; // If we have to stop, decrement the position for the next iteration, otherwise increment.
                    break;
                case 2: // Variable FSM
                    intUnderscores = false;
                    switch (variableState) {
                        case 0: // Start state
                            variableState = 1;
                            start = currentPos;
                            break;
                        case 1: // Variable state
                            if (Character.isLetter(c)) {
                                variableState = 1; // Loop back on itself.
                            } else if (Character.isDigit(c)) {
                                variableState = 2; // Go to number state.
                            } else if (c == '_') {
                                variableState = 3; // Go to _ state.
                            } else {
                                variableState = 4; // Go to error state.
                            }
                            break;
                        case 2: // Number state
                            if (Character.isLetter(c)) {
                                variableState = 1; // Go back to variable state.
                            } else if (Character.isDigit(c)) {
                                variableState = 2; // Loop back on itself.
                            } else if (c == '_') {
                                variableState = 3; // Go to _ state
                            } else {
                                variableState = 4; // Go to error state.
                            }
                            break;
                        case 3: // '_' state
                            if (Character.isLetter(c)) {
                                variableState = 1; // Go back to variable state
                            } else if (Character.isDigit(c)) {
                                variableState = 2; // Go back to number state.
                            } else {
                                this.currentPos--;
                                variableState = 4; // Go to error state.
                            }
                            break;
                        case 4:
                            stop = true;
                            break;
                    }
                    this.currentPos += stop ? -1 : 1;
                    break;
                case 3: // Symbol FSM
                    intUnderscores = false;
                    switch (symbolState) {
                        case 0: // Start state
                            switch (c) {
                                case '+':
                                    symbolState = 1;
                                    break;
                                case '-':
                                    symbolState = 2;
                                    break;
                                case '*':
                                    symbolState = 3;
                                    break;
                                case '/':
                                    symbolState = 4;
                                    break;
                                case '%':
                                    symbolState = 5;
                                    break;
                                case '(':
                                    symbolState = 6;
                                    break;
                                case ')':
                                    symbolState = 7;
                                    break;
                                case '=':
                                    symbolState = 8;
                                    break;
                            }
                            break;
                        case 1: // + FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.PLUS;
                                    break;
                                case 1:
                                    switch (c) {
                                        case '+':
                                            // Second + ?
                                            kind = Token.Kind.PLUSPLUS;
                                            this.currentPos++; // Account for this added char by incrementing the position (applied to all double char symbols in this FSM).
                                            state = 2; // Go to error state
                                            break;
                                        case '=': // += ?
                                            kind = Token.Kind.PLUS_EQUALS;
                                            this.currentPos++;
                                            state = 2;
                                            break;
                                        default:
                                            state = 2;
                                            break;
                                    }
                                    break;
                                case 2:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? -1 : 1;
                            break;
                        case 2: // - FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.MINUS;
                                    break;
                                case 1:
                                    switch (c) {
                                        case '-':
                                            // Second - ?
                                            kind = Token.Kind.MINUSMINUS;
                                            this.currentPos++;
                                            state = 2; // Go to error state
                                            break;
                                        case '=':
                                            // -= ?
                                            kind = Token.Kind.MINUS_EQUALS;
                                            this.currentPos++;
                                            state = 2;
                                            break;
                                        default:
                                            state = 2;
                                            break;
                                    }
                                    break;
                                case 2:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? -1 : 1;
                            break;
                        case 3: // * FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.TIMES;
                                    break;
                                case 1:
                                    if (c == '=') { // *= ?
                                        kind = Token.Kind.TIMES_EQUALS;
                                        this.currentPos++;
                                        state = 2;
                                    } else {
                                        state = 2;
                                    }
                                    break;
                                case 2:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? -1 : 1;
                            break;
                        case 4: // / FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.DIVIDE;
                                    break;
                                case 1:
                                    if (c == '=') { // /= ?
                                        kind = Token.Kind.DIVIDE_EQUALS;
                                        this.currentPos++;
                                        state = 2;
                                    } else {
                                        state = 2;
                                    }
                                    break;
                                case 2:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? -1 : 1;
                            break;
                        case 5: // % FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.MOD;
                                    break;
                                case 1:
                                    if (c == '=') { // = ?
                                        kind = Token.Kind.MOD_EQUALS;
                                        this.currentPos++;
                                        state = 2;
                                    } else {
                                        state = 2;
                                    }
                                    break;
                                case 2:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? -1 : 1;

                            break;
                        case 6: // ( FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.LEFT_PARENTHESIS;
                                    break;
                                case 1:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? 0 : 1;
                            break;
                        case 7: // ) FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.RIGHT_PARENTHESIS;
                                    break;
                                case 1:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? 0 : 1;
                            break;
                        case 8: // = FSM
                            switch (state) {
                                case 0:
                                    state = 1;
                                    start = currentPos;
                                    kind = Token.Kind.EQUALS;
                                    break;
                                case 1:
                                    stop = true;
                                    break;
                            }
                            this.currentPos += stop ? 0 : 1;
                            break;
                    }
            }
        }
        returnedString = expression.substring(start, currentPos);
        switch (kind) { // What kind of Token object needs to be returned?
            case INTEGER:
                if (intUnderscores) {
                    returnedString = returnedString.replaceAll("_", ""); // Remove the underscores in the number.
                }
                return new IntegerToken(start, Integer.valueOf(returnedString)); // Return this integer token.
            case IDENTIFIER:
                return new IdentifierToken(start, returnedString); // Return this variable token.
            default:
                return new Token(kind, start); // Otherwise, return this token.
        }
    }

    private boolean isSymbol(char c) { // Helper method to determine whether @param c is a valid symbol.
        return c == '(' || c == ')' || c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    @Override
    public Iterator<Scanner.Token> iterator() {
        return this;
    }

    public static void main(String[] args) {
        Console console = System.console();
        String line = console.readLine("Expression: ");
        while (line != null) {
            if (line.equals("quit")) {
                return;
            }
            Scanner scanner = new Scanner(line);
            for (Token token : scanner) {
                System.out.print(token);
                System.out.println(" at " + token.start());

            }
            line = console.readLine("Expression: ");
        }
    }
}
