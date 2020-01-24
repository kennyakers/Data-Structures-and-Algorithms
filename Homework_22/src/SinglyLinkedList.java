import java.util.Iterator;
import java.lang.Iterable;

public class SinglyLinkedList<T> implements List<T>, Iterable<T> {

    private class Node {

        T data;
        Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Node(T data) {
            this(data, null);
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        size = 0;
    }

    public SinglyLinkedList(T item) {
        Node node = this.new Node(item);
        this.head = node;
        this.tail = node;
        this.size = 1;
    }

	public SinglyLinkedList(T[] items) {
		this();
		for (T item : items) {
			this.append(item);
		}
	}

	public SinglyLinkedList(List<T> items) {
		this();
		for (T item : items) {
			this.append(item);
		}
	}


    public int size() {
        return this.size;
    }

	public boolean isEmpty() {
		return this.size == 0;
	}


    public boolean contains(T item) {
        Node rover = this.head;

        while (rover != null) {
            if (rover.data.equals(item)) {
                return true;
            }
            rover = rover.next;
        }
        return false;
    }


	public boolean equals(Iterable other) {
		Iterator<T> thisIterator = this.iterator();
		Iterator<T> otherIterator = other.iterator();
		while (thisIterator.hasNext() && otherIterator.hasNext()) {
			if (! thisIterator.next().equals(otherIterator.next())) return false;
		}
		return thisIterator.hasNext() == otherIterator.hasNext();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Iterable) {
			return equals((Iterable) other);
		} else {
			return false;
		}
	}


	public T head() {
		return (this.head != null) ? this.head.data : null;
	}

	public T tail() {
		return (this.tail != null) ? this.tail.data : null;
	}


    public void append(T item) {
        Node node = new Node(item);

        if (this.head == null) {
            this.head = node;
            this.tail = node;
        } else {
            this.tail.next = node;
            this.tail = node;
        }
        this.size++;
    }

	public void append(T[] items) {
		for (T item : items) {
			this.append(item);
		}
	}

	public void append(List<T> items) {
		for (T item : items) {
			this.append(item);
		}
	}


    public void prepend(T item) {
        Node node = new Node(item, this.head);

        this.head = node;
        if (this.tail == null) {
            this.tail = node;
        }
        this.size++;
    }

	public void prepend(T[] items) {
		for (int i = items.length - 1; i >= 0; i--) {
			this.prepend(items[i]);
		}
	}

	public void prepend(List<T> items) {
		T[] array = (T[]) new Object[items.size()];
		int index = 0;
		for (T item : items) {
			array[index++] = item;
		}
		this.prepend(array);
	}


	public T removeHead() {
		Node result = this.head;

		if (this.head != this.tail) {

			// List contains more than one element.
			// Make the second element the new head.

			this.head = this.head.next;
			this.size--;

		} else if (this.head != null) {

			// List contains exactly one element.
			// Make the list empty.

			this.head = null;
			this.tail = null;
			this.size = 0;
		}
	
		return (result != null) ? result.data : null;
	}

	public T removeTail() {
		Node current = this.head;
		Node previous = null;

		// Find the penultimate element on the list.

		while (current != tail) {
			previous = current;
			current = current.next;
		}

		// Make it be the new tail of the list.

		if (previous != null) {
			previous.next = null;
		} else {
			this.head = null;
		}

		// And return the old tail.

		this.tail = previous;
		this.size--;

		return (current != null) ? current.data : null;
	}

			
    public void remove(T item) {
        Node previous = null;
        Node current = this.head;

		// Traverse the list looking for the specified item(s).

        while (current != null) {
            if (current.data.equals(item)) {

				// Found one.  Remove it by pointing the predecessor's
				// next field to this node's successor.  Note that the
				// first element on the list does not have a predecessor
				// so the head of the list needs to be updated instead.

                if (previous == null) {
                    this.head = current.next;
                } else {
                    previous.next = current.next;
                }

				// Careful about removing the last element of the list.

                if (this.tail == current) {
                    this.tail = previous;
                }
	
				// And dont forget:

                this.size--;

			} else {
				previous = current;
			}
			current = current.next;
        }
    }


	public void reverse() {
		Node previous = null;
		Node current = this.head;

		while (current != null) {
			Node next = current.next;
			current.next = previous;
			previous = current;
			current = next;
		}

		Node temp = this.head;
		this.head = this.tail;
		this.tail = temp;
	}


	public String toString() {
		String result = "{";
		boolean first = true;
		for (T item : this) {
			if (!first) result += ", ";
			result += item;
			first = false;
		}
		result += "}";
		return result;
	}


    public Iterator<T> iterator() {
        return new ListIterator();
    }
    
    
    private class ListIterator implements Iterator<T> {
        
        private Node current;
        
        public ListIterator() {
            this.current = SinglyLinkedList.this.head;
        }
        
        public boolean hasNext() {
            return this.current != null;
        }
        
        public T next() {
            T result = this.current.data;
            this.current = this.current.next;
            return result;
        }
    }

}
