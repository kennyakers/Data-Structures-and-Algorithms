/**
 * Kenny Akers
 * Mr. Paige
 * Homework #3
 * 9/8/17
 */

import java.util.Iterator;

public class DoublyLinkedList<T> implements Iterable<T> {

    public static class ListIndexOutOfBoundsException extends IndexOutOfBoundsException { // Exception handler.

        ListIndexOutOfBoundsException(int index) {
            super("List index out of bounds: " + index); // Give the message to the IndexOutOfBoundsException constructor.
        }
    }

    private class Node {

        T data; // Holds whatever the user puts in this node.
        Node next; // Pointer to the next node (if there is one).
        Node previous; // Pointer to the previous node (if there is one).

        public Node(T data, Node next, Node previous) { // Constructor to initalize a node with all data members filled out.
            this.data = data;
            this.next = next;
            this.previous = previous;
        }

        public Node(T data) { // Constructor to initialize a lone node.
            this(data, null, null);
        }
    }

    private Node head; // The first node in the list.
    private Node tail; // The last node in the list.
    private int size; // Number of nodes in the list.

    // Constructors:
    //    Empty list.
    //    Singleton list.
    //    Array of values.
    public DoublyLinkedList() { // Constructor to initialize an empty list.
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public DoublyLinkedList(T value) { // Constructor to initalize a singleton list (1 node).
        Node node = this.new Node(value); // Create a blank node.
        this.head = node;
        this.tail = node;
        this.size = 1;
    }

    public DoublyLinkedList(T[] values) { // Constructor to initalize a list of values.
        for (T val : values) {
            append(val); // Append each value to the end of the list.
        }
    }

    // Size        The number of elements in the list.
    // Contains    Determines if an item is in the list.
    public int size() {
        return this.size;
    }

    public boolean contains(T item) {
        for (T value : this) { // Can I do ": this"?
            if (value == item) { // If found...
                return true;
            }
        }
        return false; // Not found.
    }

    // Get         Returns the value of the ith element of the list.
    // Set         Updates the value of the ith element of the list.
    public T get(int index) throws ListIndexOutOfBoundsException {
        if (index > this.size - 1 || index < 0) {
            throw new ListIndexOutOfBoundsException(index);
        }

        Node node = null;

        // If statement that traverses the list forwards or backwards depending on if
        // @param index is in the first or second half of the list (Supposed to make
        // the method more efficient for larger lists).
        if (index >= this.size / 2) {
            node = this.tail;
            for (int i = this.size - 1; i > index; i--) {
                node = node.previous;
            }
        } else {
            node = this.head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        }
        return node.data;
    }

    public void set(int index, T value) throws ListIndexOutOfBoundsException {
        if (index > this.size - 1 || index < 0) { // If an illegal index is given, throw an exception.
            throw new ListIndexOutOfBoundsException(index);
        }
        Node node = this.head;

        /* 
        If statement that traverses the list forwards or backwards depending on if
        @param index is in the first or second half of the list (Supposed to make
        the method more efficient for larger lists).
        
        These for loops iterate through the list until it reaches the index of the node of interest.
        Since nodes do not have indeces assigned to them, you get to a specific node by iterating
        the same number of times as the index. 
         */
        if (index >= this.size / 2) {
            for (int i = this.size - 1; i > index; i--) {
                node = node.previous;
            }
        } else {
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        }

        node.data = value; // Set that node's data parameter to @param value.
    }

    // Append      Places a new element at the end of the list.
    // Prepend     Places a new element at the head of the list.
    // Remove      Removes ALL copies of a given element from the list.
    // Reverse     Reverses the order of the elements in the list.
    //             (MUST be accomplished by changing the links)!
    public void append(T item) {
        Node node = new Node(item); // Create new node - data filled out, previous and next remain null.

        if (this.head == null) { // If the list is empty...
            // Node's previous and next remain null.
            this.head = node; // Point the head pointer to the appended node.
            this.tail = node; // Point the tail pointer to the appended node.
        } else {
            node.previous = this.tail; // Point the appended node's previous to the previous tail (before updating this.tail).
            this.tail.next = node; // Point the previous tail node to the appended node.
            this.tail = node; // Point the tail pointer to the new end node.
        }

        this.size++; // Increment the size.
    }

    public void prepend(T item) {
        Node node = new Node(item, this.head, null); // New node - containing item, the previous head as its next, and null as its previous because it's the beginning of the list.

        this.head = node; // Update the head of the list to the prepended node.
        if (this.tail == null) { // If the list is empty...
            this.tail = node; // Point the tail to the node.
        }
        this.size++; // Increment the size.
    }

    public void remove(T item) {
        Node currentNode = this.head;

        while (currentNode != null) {
            if (currentNode.data.equals(item)) {
                //System.out.println("removing: " + currentNode.data);
                if (this.size == 1) { // If currentNode is the only node...
                    this.head = null; // Remove all references to it so the garbage collector can remove the node.
                    this.tail = null;
                    currentNode = null;
                    this.size--; // Decrement the size
                    break; // Just removed the final node in the list, so no need to continue.
                }
                if (currentNode.previous == null) { // If removing the first node...
                    this.head = currentNode.next; // Point the list's head to the next node.
                    currentNode.next.previous = null; // Remove the next node's previous pointer.
                    currentNode = this.head; // Update the currentNode variable.
                } else if (this.tail == currentNode) { // If removing the last node...
                    this.tail = currentNode.previous; // Set the tail to the node before currentNode.
                    currentNode.previous.next = null; // Set the previous node's next pointer to null so it doesn't point to this node.
                    this.size--; // Decrement the size
                    break; // Just removed the end node in the list, so break because there are no subsequent nodes.
                } else {
                    currentNode.next.previous = currentNode.previous; // Point the next node's previous pointer to the node before this node.
                    currentNode.previous.next = currentNode.next; // Point the previous node's next pointer to the node after this node.
                    currentNode = currentNode.next; // Advance currentNode to the next node.
                }

                this.size--; // Decrement the size
            } else {
                currentNode = currentNode.next; // Didn't find @param item, so advance currentNode to the next node.
            }
        }
    }

    public void reverse() {

        Node currentNode = this.head;

        /*
        System.out.print("Original:  ");
        for (int i = 0; i < this.size; i++) {
            System.out.print(this.get(i) + " ");
        }
         */
        while (currentNode != null) {
            Node oldPrev = currentNode.previous; // Store the previous node.
            currentNode.previous = currentNode.next; // Set the current node's previous reference to that of the node after it.
            currentNode.next = oldPrev; // Set the current node's next reference to that of the one before it.
            currentNode = currentNode.previous; // Advance currentNode to the next node (which is referenced by the new previous).
        }
        // Swap the head and tail because the list was reversed.
        Node oldHead = this.head;
        this.head = this.tail;
        this.tail = oldHead;

        //System.out.println("\nNew head: " + this.head.data);
        //System.out.println("New tail: " + this.tail.data);

        /*
        System.out.print("\nReversed:  ");
        for (T val : this) { // Use the forward iterator to print out the list.
            System.out.print(val + " ");
        }
         */
    }

    // And the obvious iterators:
    @Override
    public Iterator<T> iterator() {
        return this.new DoublyLinkedForwardIterator();
    }

    public Iterator<T> forwardIterator() {
        return this.new DoublyLinkedForwardIterator();
    }

    public Iterator<T> reverseIterator() {
        return this.new DoublyLinkedReverseIterator();
    }

    private class DoublyLinkedForwardIterator implements Iterator<T> {

        private Node currentNode;

        public DoublyLinkedForwardIterator() {
            this.currentNode = DoublyLinkedList.this.head; // Start at the beginning (head).
        }

        @Override
        public boolean hasNext() {
            return this.currentNode != null; // Return true only if the node is not null.
        }

        @Override
        public T next() {
            T result = this.currentNode.data; // Get the data stored in this Node.
            this.currentNode = this.currentNode.next; // Point currentNode to the next Node.
            return result; // Return the current value.
        }
    }

    private class DoublyLinkedReverseIterator implements Iterator<T> {

        private Node currentNode;

        public DoublyLinkedReverseIterator() {
            this.currentNode = DoublyLinkedList.this.tail; // Start at the end (tail).
        }

        @Override
        public boolean hasNext() {
            return this.currentNode != null; // Return true only if the previous node is not null.
        }

        @Override
        public T next() {
            /*
            Next has two jobs to do:
            return the value associated with the current spot in the iteration.
            advance to the next spot in the iteration.
             */
            T result = this.currentNode.data; // Get the data stored in the previous Node.
            this.currentNode = this.currentNode.previous; // Point currentNode to the previous Node.
            return result; // Return the current value.
        }
    }

}
