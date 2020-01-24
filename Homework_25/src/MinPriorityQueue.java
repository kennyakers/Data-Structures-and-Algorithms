
import java.io.Console;
import java.util.Iterator;

public class MinPriorityQueue<T extends Comparable<T>> implements Iterable<T> {

    private T[] queue;
    private int size;

    private int swaps;
    private int compares;

    private boolean useSentinal = false;
    private boolean sinkToBottom = false;

    public MinPriorityQueue(int capacity) {
        this.queue = (T[]) new Comparable[capacity + 1];
        this.size = 0;

    }

    public MinPriorityQueue(T[] values) {
        this(values.length);
        int n = values.length;
        this.size = n;

        for (int i = 0; i < n; i++) {
            this.queue[i + 1] = values[i];
        }

        for (int k = n / 2; k > 0; k--) {
            sink(k);
        }
    }

    private T key(int index) {
        return this.queue[index];
    }

    private int parent(int index) {
        return index / 2;
    }

    private int leftChild(int index) {
        return 2 * index;
    }

    private int rightChild(int index) {
        return 2 * index + 1;
    }

    private boolean less(int i, int j) {
        this.compares++;
        return queue[i].compareTo(queue[j]) < 0;
    }

    private void swap(int i, int j) {
        this.swaps++;
        T temp = this.queue[i];
        this.queue[i] = this.queue[j];
        this.queue[j] = temp;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void add(T item) {
        this.queue[++this.size] = item;
        swim(this.size);
    }

    public T min() {
        if (this.size > 0) {
            return this.queue[1];
        } else {
            return null;
        }
    }

    public T removeMin() {
        T result = this.min();
        if (this.size > 0) {
            this.queue[1] = this.queue[this.size--];
            sink(1);
        }
        return result;
    }

    private void swimNotUsingSentinal(int index) {
        while (index > 1 && less(index, index / 2)) {
            swap(index / 2, index);
            index /= 2;
        }
    }

    private void swimUsingSentinal(int index) {
        int parent;
        queue[0] = queue[index];  // Sentinal (item being updated)
        while (less(index, parent = parent(index))) {
            swap(parent, index);
            index = parent;
        }
    }

    private void swim(int index) {
        if (useSentinal) {
            swimUsingSentinal(index);
        } else {
            swimNotUsingSentinal(index);
        }
    }

    private void sinkToBottom(int index) {
        int kid;
        int left;
        int right;

        while ((left = leftChild(index)) <= size) {
            right = rightChild(index);
            if (right <= size && less(right, left)) {
                kid = right;
            } else {
                kid = left;
            }
            swap(kid, index);
            index = kid;
        }
    }

    private void sinkToProperLevel(int index) {
        int kid;
        int left;
        int right;

        while ((left = leftChild(index)) <= size) {
            right = rightChild(index);
            if (right <= size && less(right, left)) {
                kid = right;
            } else {
                kid = left;
            }
            if (!less(kid, index)) {
                break;
            }
            swap(kid, index);
            index = kid;
        }
    }

    private void sink(int index) {
        if (sinkToBottom) {
            sinkToBottom(index);
            swim(index);
        } else {
            sinkToProperLevel(index);
        }
    }

    private class PriorityQueueIterator implements Iterator<T> {

        private int index;

        public PriorityQueueIterator() {
            this.index = 1;
        }

        public boolean hasNext() {
            return this.index <= MinPriorityQueue.this.size;
        }

        public T next() {
            return MinPriorityQueue.this.queue[this.index++];
        }
    }

    public Iterator<T> iterator() {
        return new PriorityQueueIterator();
    }

}
