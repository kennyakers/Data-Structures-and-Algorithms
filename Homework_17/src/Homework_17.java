
 /**
 * Kenny Akers
 * Mr. Paige
 * Homework #17
 * 2/9/18
 */

import java.io.Console;

public class Homework_17 {

    public static void main(String[] args) {
        Console console = System.console();
        SymbolTable.initialize(); // Initialize the table.

        if (console == null) {
            System.err.println("No console");
            return;
        }

        /**
         * RegEx's:
         * IDENTIFIERS: [a-zA-Z]+(_?[a-zA-Z0-9])*
         */
        String line = console.readLine("Command: ").trim();
        Node expression;
        while (line != null) {
            String command = getCommand(line);
            switch (line) {
                case "2+3*4":
                    expression = new Addition(new Number(2), new Multiplication(new Number(3), new Number(4)));
                    break;
                case "4/(5-3)":
                    expression = new Division(new Number(4), new Subtraction(new Number(5), new Number(3)));
                    break;
                case "-(4+3)*2":
                    expression = new Multiplication(new Negate(new Addition(new Number(4), new Number(3))), new Number(2));
                    break;
                case "x=10%4":
                    expression = new Assignment(new Identifier("x"), new Modulus(new Number(10), new Number(4)));
                    break;
                case "x+=(-x)":
                    Identifier x = new Identifier("x");
                    expression = new AddAssignment(x, new Negate(x));
                    break;
                case "end":
                case "exit":
                case "quit":
                    return;
                default:
                    System.out.println("Invalid command: " + command);
                    expression = null;
                    break;
            }
            if (expression != null) {
                System.out.println("Expression: " + expression.format());
                System.out.println("Value: " + expression.evaluate());
            }
            line = console.readLine("Command: ").trim();
        }

    }

    private static String getArgument(String line, int index) {
        String[] words = line.split("\\s");
        return words.length > index ? words[index] : "";
    }

    private static String getCommand(String line) {
        return getArgument(line, 0);
    }

}
