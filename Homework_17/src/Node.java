
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public abstract class Node {

    /**
     * Precedence System: 0: () 
     * 1: Post ++ -- 
     * 2: Pre ++ -- 
     * 3: Unary: + - 
     * 4: Binary: * / % 
     * 5: Binary: + - 
     * 6: Assignment: = += -= *= /= %=
     * 7: Variables and Numbers
     */
    protected int precedence;

    public Node(int precedence) {
        this.precedence = precedence;
    }

    public abstract int evaluate();

    public abstract String format();
}
