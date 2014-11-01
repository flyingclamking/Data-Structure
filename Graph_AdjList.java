import java.util.ArrayList;
import java.util.List;

public class Graph_AdjList {
	
	private static final int DEFAULT_VERTEX = 10;
	
	private final int V;
	private int E;
	private List<Integer>[] adj;
	
	public Graph_AdjList() {
		this(DEFAULT_VERTEX);
	}
	
	@SuppressWarnings("unchecked")
	public Graph_AdjList(int V) {
		this.V = V;
		this.E = 0;
		this.adj = (ArrayList<Integer>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<Integer>();
		}
	}
	
	public int V() {
		return V;
	}
	
	public int E() {
		return E;
	}
	
	public void addEdge(int w, int u) {
		if (w < 0 || w >= V || u < 0 || u >= V) {
			throw new IndexOutOfBoundsException();
		}
		adj[w].add(u);
		adj[u].add(w);
		E++;
	}
	
	public Iterable<Integer> adj(int v) {
		if (v < 0 || v >= V) {
			throw new IndexOutOfBoundsException();
		}
		return adj[v];
	}
	
	public static int degree(Graph_AdjList G, int v) {
		int degree = 0;
		for (@SuppressWarnings("unused") int w : G.adj(v)) {
			degree++;
		}
		return degree;
	}
	
	public static int maxDegree(Graph_AdjList G) {
		int max = 0;
		for (int v = 0; v < G.V(); v++) {
			if (degree(G, v) > max) {
				max = degree(G, v);
			}
		}
		return max;
	}
	
	public static int numberOfSelfLoop(Graph_AdjList G) {
		int count = 0;
		for (int v = 0; v < G.V(); v++) {
			for (int w : G.adj(v)) {
				if (w == v) {
					count++;
				}
			}
		}
		return count;
	}
	
	public String toString() {
		String s = "This graph has " + V + "vertices and " + E + "edges.\n";
		for (int v = 0; v < V; v++) {
			s += v + " : ";
			for (int w : this.adj(v)) {
				s += w + " ";
			}
			s += "\n";
		}
		return s;
	}
}
