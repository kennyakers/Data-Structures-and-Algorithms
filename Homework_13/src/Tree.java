
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #13
 * 12/8/17
 */
public class Tree<T extends Comparable<T>> {

    private class Node<T> {

        private T value;
        private Node left;
        private Node right;
        private Node parent;

        public Node(T value) {
            this.value = value;
        }

        public Node(T value, Node parent) {
            this(value);
            this.parent = parent;
        }
    }

    private Node root;

    public Tree(T[] values) {
        this.root = this.build(values, null, 0);
    }

    private Node build(T[] values, Node parent, int k) {
        Node node = new Node(values[k], parent);
        node.left = (2 * k + 1 < values.length) ? build(values, node, 2 * k + 1) : null; // If k is within bounds for the next recursion, recurse, otherwise, assign null.
        node.right = (2 * k + 2 < values.length) ? build(values, node, 2 * k + 2) : null; // Same for the right.
        return node;
    }

    public void getLCA(Node root, T node1, T node2) {
        Node ancestor = computeLCA(this.root, this.find(node1), find(node2));
        if (ancestor == null) { // Both entered values do not exist in the tree.
            System.out.println(node1 + " and " + node2 + " do not exist.");
        } else {
            System.out.println(ancestor.value);
        }
    }

    public Node computeLCA(Node parent, Node node1, Node node2) {
        if (parent == null) { // Base case.
            return null;
        }

        if (parent == node1 || parent == node2) { // If this parent is either of the values, it is the LCA. 
            return parent;
        }

        Node left = computeLCA(parent.left, node1, node2);
        Node right = computeLCA(parent.right, node1, node2);

        // I had some fun with some ternary operators...
        // 1st Condition: If one node is on each branch, this node (parent) is a common ancestor. Otherwise...
        // 2nd Condition: If at least one of the values was found on either branch, return that branch.
        return (left != null && right != null) ? parent : (left != null ? left : right);
    }

    public Node root() {
        return this.root;
    }

    public Node find(T item) {
        return this.find(this.root, item);
    }

    public Node find(Node node, T key) {
        if (node == null) { // Base case (after processing a leaf (whose children are null)).
            return null;
        } else if (node.value != null && node.value.equals(key)) { // Found the node with @param key.
            return node;
        } else {
            Node nodeToFind = find(node.left, key); // Search for it in the left sub-tree.
            if (nodeToFind == null) { // If nothing on the left...
                nodeToFind = find(node.right, key); // Try the right.
            }
            return nodeToFind; // Return the found node.
        }
    }

    public void printPreOrder(Node node) { // Debugger.
        if (node == null) { // Base case.
            return;
        }
        System.out.print(node.value + " "); // Print out the current node.
        printPreOrder(node.left); // Recurse down the left side.
        printPreOrder(node.right); // Recurse down the right side.
    }

    public int numberLeaves(Node node) { // Quiz solution.
        if (node == null) {
            return 0;
        }

        if (node.left == null && node.right == null) {
            return 1;
        } else {
            return numberLeaves(node.left) + numberLeaves(node.right);
        }
    }

}
