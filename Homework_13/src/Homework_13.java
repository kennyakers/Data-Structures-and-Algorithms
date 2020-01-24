
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #13
 * 12/8/17
 */
public class Homework_13 {

    public static void main(String[] args) {
        if (args.length <= 2) { // If not enough arguments were entered (either nothing for the tree or nothing to search for)...
            System.out.println("Not enough arguments. Please specify values for the tree followed by two values to find LCA of.");
            return;
        }
        String[] array = new String[args.length - 2];
        for (int i = 0; i < array.length; i++) { // For the first n-2 arguments (the numbers in the tree)...
            array[i] = args[i].equals("-") ? null : args[i];
        }
        Tree<String> tree = new Tree<String>(array);
        tree.getLCA(tree.root(), args[array.length], args[array.length + 1]);
        System.out.println("Number of Leaves: " + tree.numberLeaves(tree.root()));
    }

}
