/**
 * Kenny Akers
 * Mr. Paige
 * Homework #15
 * 1/22/18
 */

import java.io.Console;

public class BinarySearchTree<T extends Comparable<T>> {

    public class Node {

        private T data;
        private Node left;
        private Node right;
        private Balance balanceFactor;

        public Node(T data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.balanceFactor = Balance.EVEN;
        }

        public Node(T data) {
            this(data, null, null); // A leaf node is balanced.
        }

        public T value() {
            return this.data;
        }

        public Node left() {
            return this.left;
        }

        public Node right() {
            return this.right;
        }

        public Balance balanceFactor() {
            return this.balanceFactor;
        }

        public void value(T data) {
            this.data = data;
        }

        public void left(Node child) {
            this.left = child;
        }

        public void right(Node child) {
            this.right = child;
        }

        public void setBalance(Balance bal) {
            this.balanceFactor = bal;
        }

    }

    private static enum Balance {
        LEFT('/'),
        EVEN('-'),
        RIGHT('\\');

        private char c;

        private Balance(char c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return Character.toString(c);
        }
    }

    private class RootAndTaller {

        public Node root;
        public boolean gotTaller;

        public RootAndTaller(Node root, boolean gotTaller) {
            this.root = root;
            this.gotTaller = gotTaller;
        }
    }

    private Node root;

    public BinarySearchTree() {
        this.root = null;
    }

    public BinarySearchTree(T[] values) {
        for (T value : values) {
            add(value);
        }
    }

    // Return the number of nodes in the tree (or in
    // the sub-tree rooted at a specified node).
    public int size() {
        return size(this.root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        } else {
            return size(node.left) + size(node.right) + 1;
        }
    }

    // Returnthe height of a tree (or the height of
    // a sub-tree rooted at a specified node).
    public int height() {
        return height(this.root);
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // Determine if a tree contains a specified key.
    public boolean contains(T item) {
        Node rover = this.root;
        while (rover != null) {
            int compare = item.compareTo(rover.data);
            if (compare < 0) {
                rover = rover.left;
            } else if (compare > 0) {
                rover = rover.right;
            } else {
                return true;
            }
        }
        return false;
    }

    // Find the node in a tree having a specified key value.
    public Node locate(Node node, T item) {
        Node rover = node;
        while (rover != null) {
            int compare = item.compareTo(rover.data);
            if (compare < 0) {
                rover = rover.left;
            } else if (compare > 0) {
                rover = rover.right;
            } else {
                return rover;
            }
        }
        return null;
    }

    public Node locate(T item) {
        return locate(this.root, item);
    }

    public T find(Node node, T item) {
        Node result = locate(node, item);
        return (result == null) ? null : result.data;
    }

    public T find(T item) {
        Node result = locate(item);
        return (result == null) ? null : result.data;
    }

    public Node min(Node node) {
        if (node == null) {
            return null;
        }
        Node rover = node;
        while (rover.left != null) {
            rover = rover.left;
        }
        return rover;
    }

    public T min() {
        Node result = min(this.root);
        return (result == null) ? null : result.data;
    }

    public Node max(Node node) {
        // A slightly different version than min:
        Node parent = null;
        Node rover = node;
        while (rover != null) {
            parent = rover;
            rover = rover.right;
        }
        return parent;
    }

    public T max() {
        Node result = max(this.root);
        return (result == null) ? null : result.data;
    }

    // Find the item in a tree having the largest value
    // that is no greater than a specified key.
    public T floor(T item) {
        Node floor = null;
        Node rover = this.root;
        while (rover != null) {
            int compare = item.compareTo(rover.data);
            if (compare < 0) {
                rover = rover.left;
            } else if (compare > 0) {
                floor = rover;
                rover = rover.right;
            } else {
                return rover.data;
            }
        }
        return (floor == null) ? null : floor.data;
    }

    // Find the item in a tree having the smallest value
    // that is not less than a specified key.
    public T ceiling(T item) {
        Node ceiling = null;
        Node rover = this.root;
        while (rover != null) {
            int compare = item.compareTo(rover.data);
            if (compare < 0) {
                ceiling = rover;
                rover = rover.left;
            } else if (compare > 0) {
                rover = rover.right;
            } else {
                return rover.data;
            }
        }
        return (ceiling == null) ? null : ceiling.data;
    }

    public int rank(Node node, T item) {
        // TODO
        return 0;
    }

    public int rank(T item) {
        return rank(this.root, item);
    }

    public T select(Node node, int rank) {
        // TODO
        return null;
    }

    public T select(int rank) {
        return select(this.root, rank);
    }

    private Node balance(Node node) {
        if (height(node.left) - height(node.right) > 1) { // Left subtree is 2+ taller than the right subtree.
            if (node.left.balanceFactor == Balance.LEFT) { // Need to do a single right rotation.
                System.out.println("Rotating right around " + node.data);
                node.balanceFactor = Balance.EVEN;
                node = rotateRight(node);
                node.balanceFactor = Balance.EVEN;
            } else { // Left child's right subtree is bigger than the left child's left subtree --> LR
                System.out.println("LR: Node is " + node.data);
                System.out.println("LR: Rotating left around " + node.left.data);

                node.left.balanceFactor = Balance.EVEN; // A
                node.left.right.balanceFactor = Balance.LEFT; // B
                node.left = rotateLeft(node.left);

                System.out.println("LR: Rotating right around " + node.data);

                node.balanceFactor = (node.balanceFactor == Balance.LEFT && node.right != null) ? Balance.RIGHT : Balance.EVEN;
                node = rotateRight(node);
                node.balanceFactor = Balance.EVEN;
            }
        } else { // Right subtree is 2+ taller than the left subtree.
            if (node.right.balanceFactor == Balance.RIGHT) { // Need to do a single left rotation.
                System.out.println("Rotating left around " + node.data);
                node.balanceFactor = Balance.EVEN;
                node = rotateLeft(node);
                node.balanceFactor = Balance.EVEN;
            } else { // Right child's left subtree is bigger than the right child's right subtree --> RL
                System.out.println("RL: Node is " + node.data); // A
                System.out.println("RL: Rotating right around " + node.right.data); // C

                node.right.balanceFactor = Balance.EVEN; // C
                node.right.left.balanceFactor = Balance.RIGHT;  // B        
                node.right = rotateRight(node.right);

                System.out.println("RL: Rotating left around " + node.data); // A

                node.balanceFactor = (node.balanceFactor == Balance.RIGHT && node.left != null) ? Balance.LEFT : Balance.EVEN;
                node = rotateLeft(node);
                node.balanceFactor = Balance.EVEN;
            }
        }
        return node;
    }


    public RootAndTaller add(Node node, T item) {
        boolean taller = false;

        if (node == null) {
            return new RootAndTaller(new Node(item), true);

        } else {
            int compare = item.compareTo(node.data);
            if (compare < 0) {
                RootAndTaller left = add(node.left, item);
                node.left = left.root;
                if (left.gotTaller) {
                    switch (node.balanceFactor) {
                        case LEFT:
                            System.out.println("left balance");
                            node = this.balance(node);
                            break;
                        case EVEN:
                            node.balanceFactor = Balance.LEFT;
                            taller = true;
                            break;
                        case RIGHT:
                            node.balanceFactor = Balance.EVEN;
                            taller = false;
                            break;
                    }
                } else {
                    taller = false;
                }

            } else if (compare > 0) {
                RootAndTaller right = add(node.right, item);
                node.right = right.root;
                if (right.gotTaller) {
                    switch (node.balanceFactor) {
                        case LEFT:
                            node.balanceFactor = Balance.EVEN;
                            taller = false;
                            break;
                        case EVEN:
                            node.balanceFactor = Balance.RIGHT;
                            taller = true;
                            break;
                        case RIGHT:
                            System.out.println("right balance");
                            node = this.balance(node);
                            break;
                    }
                } else {
                    taller = false;
                }

            } else {
                node.data = item;
                taller = false;
            }

            return new RootAndTaller(node, taller);
        }

        /*
        TEST DATA FOR DOUBLE ROTATION TREE
        
        cow
        bat
        ant
        bug
        dog
        
        then add cat will trigger the double 
         */
    }

    public Node rotateLeft(Node pivot) {
        Node right = pivot.right;
        pivot.right = pivot.right.left;
        right.left = pivot;
        return right;
    }

    public Node rotateRight(Node pivot) {
        Node left = pivot.left;
        pivot.left = pivot.left.right;
        left.right = pivot;
        return left;
    }

    public void add(T item) {
        this.root = add(this.root, item).root;
    }

    public Node remove(Node node, T item) {
        if (node == null) {
            return null;
        }
        int compare = item.compareTo(node.data);
        if (compare < 0) {
            node.left = remove(node.left, item);
            return node;
        } else if (compare > 0) {
            node.right = remove(node.right, item);
            return node;
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            Node min = min(node.right);
            node.data = min.data;
            node.right = deleteMin(node.right);
            return node;
        }
    }

    public Node deleteMin(Node node) {
        Node rover = node;
        Node parent = null;
        while (rover.left != null) {
            parent = rover;
            rover = rover.left;
        }
        if (parent == null) {
            return node.right;
        } else {
            parent.left = rover.right;
            return node;
        }
    }

    public void deleteMin() {
        this.root = deleteMin(this.root);
    }

    public Node deleteMax(Node node) {
        Node rover = node;
        Node parent = null;
        while (rover.right != null) {
            parent = rover;
            rover = rover.right;
        }
        if (parent == null) {
            return node.left;
        } else {
            parent.right = rover.left;
            return node;
        }
    }

    public void deleteMax() {
        this.root = deleteMax(this.root);
    }

    public void remove(T item) {
        this.root = remove(this.root, item);
    }

    public interface Action<T> {

        public void visit(T data, int depth, Balance factor);
    }

    public void traverse(Action<T> action) {
        traverse(root, action, 0);
    }

    public void traverse(Node node, Action<T> action, int depth) {
        if (node != null) {
            traverse(node.left, action, depth + 1);
            action.visit(node.data, depth, node.balanceFactor);
            traverse(node.right, action, depth + 1);
        }
    }

    private static String getArgument(String line, int index) {
        String[] words = line.split("\\s");
        return words.length > index ? words[index] : "";
    }

    private static String getCommand(String line) {
        return getArgument(line, 0);
    }

    public static void main(String[] args) {
        Console console = System.console();
        BinarySearchTree<String> tree;

        if (console == null) {
            System.err.println("No console");
            return;
        }

        if (args.length > 0) {
            tree = new BinarySearchTree<String>(args);
        } else {
            tree = new BinarySearchTree<String>();
        }

        String line = console.readLine("Command: ").trim();
        while (line != null) {
            String command = getCommand(line);
            String arg = getArgument(line, 1);

            switch (command) {
                case "size":
                    System.out.println(tree.size());
                    break;

                case "height":
                    System.out.println(tree.height());
                    break;

                case "add":
                    tree.add(arg);
                    break;

                case "contains":
                    System.out.println(tree.contains(arg));
                    break;

                case "find":
                    System.out.println(tree.find(arg));
                    break;

                case "floor":
                    System.out.println(tree.floor(arg));
                    break;

                case "ceiling":
                    System.out.println(tree.ceiling(arg));
                    break;

                case "min":
                    System.out.println(tree.min());
                    break;

                case "max":
                    System.out.println(tree.max());
                    break;

                case "rank":
                    System.out.println(tree.rank(arg));
                    break;

                case "select":
                    try {
                        int rank = Integer.parseInt(arg);
                        System.out.println(tree.select(rank));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid rank: " + arg);
                    }
                    break;

                case "remove":
                case "delete":
                    tree.remove(arg);
                    break;

                case "delmin":
                case "deletemin":
                    tree.deleteMin();
                    break;

                case "delmax":
                case "deletemax":
                    tree.deleteMax();
                    break;

                case "clear":
                    tree = new BinarySearchTree<String>();
                    break;

                case "tree":
                case "print":
                    Action<String> action = new Action<String>() {
                        @Override
                        public void visit(String data, int depth, Balance factor) {
                            int temp = depth;
                            while (depth-- > 0) {
                                System.out.print("  ");
                            }
                            System.out.println(data + " (" + factor + ")");
                        }
                    };
                    tree.traverse(action);
                    break;

                case "help":
                    System.out.println("size             Size of the tree");
                    System.out.println("height           Height of the tree");
                    System.out.println("min              Smallest item in the tree");
                    System.out.println("max              Largest item in the tree");
                    System.out.println("add <key>        Add an item");
                    System.out.println("contains <key>   Is an item in the tree");
                    System.out.println("find <key>       Finds an item");
                    System.out.println("floor <key>      Finds largest item <= key");
                    System.out.println("ceiling <key>    Finds smallest item >= key");
                    System.out.println("rank <key>       Determines the rank of the key");
                    System.out.println("select <#>       Finds the kth smallest key");
                    System.out.println("remove <key>     Remove an item");
                    System.out.println("delete <key>     Remove an item");
                    System.out.println("delmin           Removes the smallest item");
                    System.out.println("delmax           Removes the largest item");
                    System.out.println("clear            Deletes the entire tree");
                    System.out.println("print            Displays the tree");
                    System.out.println("exit             Exits the program");
                    break;

                case "exit":
                case "quit":
                    return;

                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
            line = console.readLine("Command: ").trim();
        }
    }
}
