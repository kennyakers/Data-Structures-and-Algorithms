/**
 * Kenny Akers
 * Mr. Paige
 * Homework #14
 * 12/15/17
 */

import java.io.Console;

public class BinarySearchTree<T extends Comparable<T>> {

    public class Node {

        private T data;
        private Node left;
        private Node right;

        public Node(T data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        public Node(T data) {
            this(data, null, null);
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

        public void value(T data) {
            this.data = data;
        }

        public void left(Node child) {
            this.left = child;
        }

        public void right(Node child) {
            this.right = child;
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
        return 0;
    }

    public int rank(T item) {
        return rank(this.root, item);
    }

    public T select(Node node, int rank) {
        return null;
    }

    public T select(int rank) {
        return select(this.root, rank);
    }

    public Node add(Node node, T item) {
        if (node == null) {
            return new Node(item);
        } else {
            int compare = item.compareTo(node.data);
            if (compare < 0) {
                node.left = add(node.left, item);
            } else if (compare > 0) {
                node.right = add(node.right, item);
            } else {
                node.data = item;
            }
            return node;
        }
    }

    public void add(T item) {
        this.addIteratively(item);
    }

    public void addIteratively(T item) {
        // TODO: iteratively (Homework)
        if (this.root == null) { // If this is the first item we're adding...
            this.root = new Node(item); // Just set the root to item.
            return; // Done.
        }
        Node rover = this.root;
        Node parent = null;
        while (rover != null) {
            parent = rover; // Save the parent for use when adding (because rover will be null).
            rover = item.compareTo(rover.data) > 0 ? rover.right : rover.left; // Traverse down the correct sub-tree.
        }
        // Parent shouldn't be null because the while loop ran at least once because we account for a null root at the top.
        if (item.compareTo(parent.data) > 0) { // Otherwise, set the left or right child to be item depending on if it's ≤ or ≥ the parent.
            parent.right = new Node(item);
        } else {
            parent.left = new Node(item);
        }
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

        public void visit(T data, int depth);
    }

    public void traverse(Action<T> action) {
        traverse(root, action, 0);
    }

    public void traverse(Node node, Action<T> action, int depth) {
        if (node != null) {
            traverse(node.left, action, depth + 1);
            action.visit(node.data, depth);
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
                        public void visit(String data, int depth) {
                            while (depth-- > 0) {
                                System.out.print("  ");
                            }
                            System.out.println(data);
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
