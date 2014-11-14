import java.util.Iterator;
import java.util.NoSuchElementException;

public class Graph_AdjMatrix {
	
	private final int V;
	private int E;
	private boolean[][] adj;
	
	private static final int DEFAULT_CAPACITY = 10;
	
	public Graph_AdjMatrix() {
		this(DEFAULT_CAPACITY);
	}
	
	public Graph_AdjMatrix(int V) {
		this.V = V;
		this.E = 0;
		this.adj = new boolean[V][V];
	}
	
	public int V() {
		return V;
	}
	
	public int E() {
		return E;
	}
	
	public void addEdge(int v, int w) {
		if (!adj[v][w]) {
			E++;
		}
		adj[v][w] = true;
		adj[w][v] = true;
	}
	
	public boolean contains(int v, int w) {
		return adj[v][w];
	}
	
	public Iterable<Integer> adj(int v) {
		return new adjIterator(v);
	}
	
	private class adjIterator implements Iterator<Integer>, Iterable<Integer> {
		
		int v, w;
		
		public adjIterator(int v) {
			this.w = 0;
			this.v = v;
		}
		
		public Iterator<Integer> iterator() {
			return this;
		}
		
		public boolean hasNext() {
			while (w < V) {
				if (adj[v][w]) {
					return true;
				}
				w++;
			}
			return false;
		}
		
		public Integer next() {
			if (hasNext()) {
				return w++;
			} else {
				throw new NoSuchElementException();
			}
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
}
