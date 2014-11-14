import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MaxPQ<Key> implements Iterable<Key> {
	
	private Key[] pq;
	private int size;
	private int capacity;
	
	private Comparator<Key> comparator;
	
	public MaxPQ() {
		this(5, null);
	}
	
	@SuppressWarnings("unchecked")
	public MaxPQ(int capacity, Comparator<Key> comparator) {
		pq = (Key[]) new Object[capacity + 1];
		size = 0;
		this.capacity = capacity;
		this.comparator = comparator;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void add(Key key) {
		checkCapacity();
		pq[++size] = key;
		swim();
	}
	
	
	@SuppressWarnings("unchecked")
	private void checkCapacity() {
		if (size == capacity) {
			this.capacity <<= 1;
			Key[] temp = (Key[]) new Object[capacity + 1];
			for (int i = 1; i <= size; i++) {
				temp[i] = pq[i];
			}
			pq = temp;
		}
	}
	
	private void swim() {
		int curr = size;
		int parent = curr >> 1;
		while (curr > 1 && less(curr, parent)) {
			swap(curr, parent);
			curr = parent;
			parent >>= 1;
		}
	}
	
	public Key max() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return pq[1];
	}
	
	public Key delMax() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		Key result = pq[1];
		swap(size--, 1);
		pq[size + 1] = null;
		sink();
		return result;
	}
	
	public void swap(int i, int j) {
		Key temp = pq[i];
		pq[i] = pq[j];
		pq[j] = temp;
	}
	
	private void sink() {
		int curr = 1;
		while ((curr << 1) <= size) {
			int child = (curr << 1);
			if (child < size && less(child + 1, child)) {
				child++;
			}
			if (less(curr, child)) {
				break;
			}
			swap(child, curr);
			curr = child;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean less(int i, int j) {
		if (comparator == null) {
			return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
		}
		
		return comparator.compare(pq[i], pq[j]) > 0;
	}
	
	
	public Iterator<Key> iterator() {
		return new pqIterator();
	}
	
	private class pqIterator implements Iterator<Key> {
		
		//Three ways to implement an Iterator:
		//(1) hold nothing, directly use methods of outer class. (Our implementation)
		//(2) hold a reference of outer class. (Need to pass the reference by constructor of Iterator)
		//(3) hold a reference of a copy of outer class. (Need to initiate the copy in constructor)

		public boolean hasNext() {
			return !isEmpty();
		}

		public Key next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return delMax();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

}
