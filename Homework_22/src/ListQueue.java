public class ListQueue<T> implements Queue<T> {
	
	private SinglyLinkedList<T> queue;

	public ListQueue() {
		this.queue = new SinglyLinkedList<T>();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public T head() throws Queue.EmptyException {
		if (queue.isEmpty()) throw new Queue.EmptyException ();
		return queue.head();
	}

	@Override
	public void enqueue(T item) {
		queue.append(item);
	}

	@Override
	public T dequeue() throws Queue.UnderflowException {
		if (queue.isEmpty()) throw new Queue.UnderflowException ();
		return queue.removeHead();
	}

}

