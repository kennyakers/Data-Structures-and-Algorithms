
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */
public class Identifier extends Leaf {

    private final String variable;

    public Identifier(String ID) {
        super(7); // Variables have a precedence of 7.
        this.variable = ID;
        SymbolTable.table.put(ID, 0); // Default value of 0.
    }

    public void setValue(int newValue) {
        SymbolTable.table.replace(variable, newValue);
    }

    @Override
    public String format() {
        return this.variable;
    }

    @Override
    public int evaluate() {
        return SymbolTable.table.get(variable);
    }

}
