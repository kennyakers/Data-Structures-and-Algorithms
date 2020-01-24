import java.util.Iterator;

public interface List<T> extends Iterable<T> {

	public int size();
	public boolean isEmpty();
	public boolean contains(T item);

	public T head();
	public T tail();

	public void append(T item);
	public void append(T[] items);
	public void append(List<T> items);

	public void prepend(T item);
	public void prepend(T[] items);
	public void prepend(List<T> items);

	public void remove(T item);
	public T removeHead();
	public T removeTail();

	public void reverse();

	public Iterator<T> iterator();

}
