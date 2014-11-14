import java.util.LinkedList;
import java.util.List;

public class HashMap<K extends Comparable<K>, V> {
	
	private static final int DEFAULT_CAPACITY = 16;
	private static final float LOAD_FACTOR = 0.75f;
	
	private int capacity;
	private int size;
	private List<Cell<K, V>>[] items;
	
	public HashMap() {
		this(DEFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap(int capacity) {
		this.capacity = capacity;
		size = 0;
		items = (LinkedList<Cell<K, V>>[]) new LinkedList[capacity];
	}
	
	public int size() {
		return size;
	}
	
	public void put(K key, V value) {
		rangeCheck();
		int index = indexOfKey(key);
		if (items[index] == null) {
			items[index] = new LinkedList<Cell<K,V>>();
		}
		for (Cell<K, V> cell : items[index]) {
			if (cell.key.compareTo(key) == 0) {
				items[index].remove(cell);
				size--;
				break;
			}
		}
		items[index].add(new Cell<K, V>(key, value));
		size++;
	}
	
	private void rangeCheck() {
		if ((float) size / capacity >= LOAD_FACTOR) {
			doubleMapSize();
		}
	}
	
	private void doubleMapSize() {
		this.capacity = capacity << 1;
		@SuppressWarnings("unchecked")
		List<Cell<K, V>>[] newList = (LinkedList<Cell<K, V>>[]) new LinkedList[capacity << 1];
		for (List<Cell<K, V>> list : items) {
			if (list == null) {
				continue;
			}
			for (Cell<K, V> cell : list) {
				if (cell == null) {
					continue;
				}
				int index = indexOfKey(cell.key);
				if (newList[index] == null) {
					newList[index] = new LinkedList<Cell<K, V>>();
				}
				newList[index].add(cell);
			}
		}
		this.items = newList;
	}
	
	
	public V get(K key) {
		int index = indexOfKey(key);
		if (items[index] == null) {
			return null;
		}
		for (Cell<K, V> cell : items[index]) {
			if (key.compareTo(cell.key) == 0) {
				return cell.value;
			}
		}
		return null;
	}
	
	private final int hashcodeOfKey(K key) {
		int hash = 31;
		int length = key.toString().length();
		for (int i = 0; i < length; i++) {
			hash += (hash << 5) + key.toString().charAt(i);
		}
		return hash & 0x7FFFFFFF;
	}

	private final int indexOfKey(K key) {
		return hashcodeOfKey(key) & (capacity - 1);
	}
	
	private static class Cell<K extends Comparable<K>, V> {
		private K key;
		private V value;
		
		public Cell(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
	
}
