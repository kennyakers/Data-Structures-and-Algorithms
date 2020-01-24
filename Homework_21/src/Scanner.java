
import java.io.Console;
import java.util.Iterator;

public class Scanner implements Iterator<Scanner.Token>, Iterable<Scanner.Token> {

    public static class Token {
        public static enum Kind {
            INTEGER("int"),
            IDENTIFIER("id"),
            LEFT_PARENTHESIS("("),
            RIGHT_PARENTHESIS(")"),
            PLUS("+"),
            MINUS("-"),
            MULTIPLY("*"),
            DIVIDE("/"),
            MOD("%"),
            PLUS_PLUS("++"),
            MINUS_MINUS("--"),
            EQUAL("="),
            PLUS_EQUAL("+="),
            MINUS_EQUAL("-="),
            MULT_EQUAL("*="),
            DIV_EQUAL("/="),
            MOD_EQUAL("%="),
            END_OF_LINE("eol"),
            ERROR("err");

            private String image;

            private Kind(String image) {
                this.image = image;
            }

            @Override
            public String toString() {
                return this.image;
            }
        };

        private Kind kind;
        private int start; // Starting position on the input line
        private int end;   // ending position on the input line

        public Token(Kind kind, int start, int end) {
            this.kind = kind;
            this.start = start;
            this.end = end;
        }

        public Kind kind() {
            return this.kind;
        }

        public int start() {
            return this.start;
        }

        public int end() {
            return this.end;
        }

        @Override
        public String toString() {
            return this.kind.toString();
        }
    }

    // Integer tokens also have a numeric value.
    public static class IntegerToken extends Token {

        private int value;

        public IntegerToken(int start, int end, int value) {
            super(Token.Kind.INTEGER, start, end);
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }

    // Identifier tokens also have the identifier name.
    public static class IdentifierToken extends Token {

        private String name;

        public IdentifierToken(int start, int end, String name) {
            super(Token.Kind.IDENTIFIER, start, end);
            this.name = name;
        }

        public String name() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    // States for the scanner Finite State Machine:
    private static enum State {
        START,
        INTEGER,
        INTEGER_UNDERSCORE,
        IDENTIFIER,
        IDENTIFIER_UNDERSCORE,
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MOD,
        PLUS_PLUS,
        MINUS_MINUS,
        EQUAL,
        PLUS_EQUAL,
        MINUS_EQUAL,
        MULT_EQUAL,
        DIV_EQUAL,
        MOD_EQUAL,
        ERROR;

        // The next state function (given the current state and the input character):
        public State nextState(char c) {
            switch (this) {
                case START:
                    switch (c) {
                        case '(':
                            return LEFT_PARENTHESIS;
                        case ')':
                            return RIGHT_PARENTHESIS;
                        case '+':
                            return PLUS;
                        case '-':
                            return MINUS;
                        case '*':
                            return MULTIPLY;
                        case '/':
                            return DIVIDE;
                        case '%':
                            return MOD;
                        case '=':
                            return EQUAL;
                        case ' ':
                            return START;
                        case '\t':
                            return START;
                        default:
                            if (isDigit(c)) {
                                return INTEGER;
                            }
                            if (isLetter(c)) {
                                return IDENTIFIER;
                            }
                            return ERROR;
                    }

                case INTEGER:
                    if (isDigit(c)) {
                        return INTEGER;
                    }
                    if (c == '_') {
                        return INTEGER_UNDERSCORE;
                    }
                    return ERROR;

                case INTEGER_UNDERSCORE:
                    if (isDigit(c)) {
                        return INTEGER;
                    }
                    return ERROR;

                case IDENTIFIER:
                    if (isDigit(c)) {
                        return IDENTIFIER;
                    }
                    if (isLetter(c)) {
                        return IDENTIFIER;
                    }
                    if (c == '_') {
                        return IDENTIFIER_UNDERSCORE;
                    }
                    return ERROR;

                case PLUS:
                    if (c == '+') {
                        return PLUS_PLUS;
                    }
                    if (c == '=') {
                        return PLUS_EQUAL;
                    }
                    return ERROR;

                case MINUS:
                    if (c == '-') {
                        return MINUS_MINUS;
                    }
                    if (c == '=') {
                        return MINUS_EQUAL;
                    }
                    return ERROR;

                case MULTIPLY:
                    if (c == '=') {
                        return MULT_EQUAL;
                    }
                    return ERROR;

                case DIVIDE:
                    if (c == '=') {
                        return DIV_EQUAL;
                    }
                    return ERROR;

                case MOD:
                    if (c == '=') {
                        return MOD_EQUAL;
                    }
                    return ERROR;

                default:
                    return ERROR;
            }
        }

        // The accepting states for the FSM.
        public boolean isAcceptingState() {
            switch (this) {
                case INTEGER:
                case IDENTIFIER:
                case LEFT_PARENTHESIS:
                case RIGHT_PARENTHESIS:
                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                case MOD:
                case PLUS_PLUS:
                case MINUS_MINUS:
                case EQUAL:
                case PLUS_EQUAL:
                case MINUS_EQUAL:
                case MULT_EQUAL:
                case DIV_EQUAL:
                case MOD_EQUAL:
                    return true;

                default:
                    return false;
            }
        }

        // Which token was recognized in each state:
        public Token.Kind getTokenKind() {
            switch (this) {
                case START:
                    return Token.Kind.END_OF_LINE;

                case INTEGER:
                    return Token.Kind.INTEGER;

                case IDENTIFIER:
                    return Token.Kind.IDENTIFIER;

                case LEFT_PARENTHESIS:
                    return Token.Kind.LEFT_PARENTHESIS;

                case RIGHT_PARENTHESIS:
                    return Token.Kind.RIGHT_PARENTHESIS;

                case PLUS:
                    return Token.Kind.PLUS;

                case MINUS:
                    return Token.Kind.MINUS;

                case MULTIPLY:
                    return Token.Kind.MULTIPLY;

                case DIVIDE:
                    return Token.Kind.DIVIDE;

                case MOD:
                    return Token.Kind.MOD;

                case PLUS_PLUS:
                    return Token.Kind.PLUS_PLUS;

                case MINUS_MINUS:
                    return Token.Kind.MINUS_MINUS;

                case EQUAL:
                    return Token.Kind.EQUAL;

                case PLUS_EQUAL:
                    return Token.Kind.PLUS_EQUAL;

                case MINUS_EQUAL:
                    return Token.Kind.MINUS_EQUAL;

                case MULT_EQUAL:
                    return Token.Kind.MULT_EQUAL;

                case DIV_EQUAL:
                    return Token.Kind.DIV_EQUAL;

                case MOD_EQUAL:
                    return Token.Kind.MOD_EQUAL;

                default:
                    return Token.Kind.ERROR;
            }
        }
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z');
    }

    // The actual scanner is implemented as a simple Finite State Machine.
    // It is structured as an iterator that returns all the tokens on the
    // input line.  It runs the FSM until it either hits the end of the line
    // or gets an error and then returns the token corresponding to the last
    // accepting state that was reached ... this allows us to find the longest
    // possible sequence of characters that forms a token (thus we recognize
    // ++ as a single token rather than two plus operators).
    private String expression;	// The string to be tokenized
    private Token token;		// The last token found
    private int start;			// position to restart scanning

    public Scanner(String expression) {
        this.start = 0;
        this.expression = expression;
        this.token = getNextToken();
    }

    @Override
    public boolean hasNext() {
        return this.token.kind != Token.Kind.END_OF_LINE;
    }

    @Override
    public Token next() {
        Token result = this.token;
        this.token = getNextToken();
        return result;
    }

    private Token getNextToken() {
        int length = this.expression.length();
        State state = State.START;
        State result = State.START;
        int start = this.start;
        int end = start - 1;

        // Start scanning at the current position
        // until the end of the line or an error.
        for (int i = this.start; i < length; i++) {
            char c = this.expression.charAt(i);

            // Update the starting position if necessary.
            if (state == State.START) {
                start = i;
                end = i - 1;
            }

            // Keep track of the last accepting state seen.
            state = state.nextState(c);
            if (state.isAcceptingState()) {
                result = state;
                end = i;
            }

            // We are done if we hit an error.
            // Return the last accepting state seen.
            if (state == State.ERROR) {
                if (result == State.START) {
                    result = state;
                    end = i;
                }
                break;
            }
        }

        // Build a token for the last accepting state.
        Token.Kind kind = result.getTokenKind();
        String text = this.expression.substring(start, end + 1);
        this.start = end + 1;
        switch (kind) {
            case INTEGER:
                int value = Integer.parseInt(this.expression.substring(start, end + 1));
                return new IntegerToken(start, end, value);

            case IDENTIFIER:
                String name = this.expression.substring(start, end + 1);
                return new IdentifierToken(start, end, name);

            default:
                return new Token(kind, start, end);
        }
    }

    @Override
    public Iterator<Scanner.Token> iterator() {
        return this;
    }

    public static void main(String[] args) {
        Console console = System.console();
        String prompt = "Expression: ";
        String line = console.readLine(prompt);
        while (line != null && line.length() > 0) {
            Scanner scanner = new Scanner(line);
            for (Token token : scanner) {
                System.out.print(token);
                System.out.println(" at " + token.start());
            }
            line = console.readLine(prompt);
        }
    }
}
