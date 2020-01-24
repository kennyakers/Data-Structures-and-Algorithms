
public interface Queue<T> {

    public static class QueueException extends Exception {

        public QueueException(String message) {
            super(message);
        }
    }

    public static class OverflowException extends QueueException {

        public OverflowException() {
            super("Queue overflow");
        }
    }

    public static class UnderflowException extends QueueException {

        public UnderflowException() {
            super("Queue underflow");
        }
    }

    public static class EmptyException extends QueueException {

        public EmptyException() {
            super("Queue is empty");
        }
    }

    public boolean isEmpty();

    public T head() throws Queue.EmptyException;

    public void enqueue(T item) throws Queue.OverflowException;

    public T dequeue() throws Queue.UnderflowException;

}
