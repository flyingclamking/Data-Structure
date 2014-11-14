/***********************************************************************
*  REFERENCE : http://algs4.cs.princeton.edu/32bst/BST.java.html
***********************************************************************/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BST<Key extends Comparable<Key>, Value> implements Iterable<Key> {
	
	private Node root;
	
	
	/***********************************************************************
	 *  Basic functions.
	***********************************************************************/
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	
	/***********************************************************************
	 *  FOR ANY NODE:
	 *  node.size = 1 + size(node.left) + size(node.right);
	***********************************************************************/ 
	public int size() {
		return size(root);
	}
	
	private int size(Node x) {
		if (x == null) {
			return 0;
		}
		return x.size;
	}
	
	
	/***********************************************************************
	 *  Insert key-value pair into BST
	 *  If key already exists, update with new value
	***********************************************************************/
	public void put(Key key, Value value) {
		root = put(root, key, value);
	}
	
	private Node put(Node x, Key key, Value value) {
		if (x == null) {
			return new Node(key, value, 1);
		}
		int cmp = key.compareTo(x.key);
		if (cmp > 0) {
			x.right = put(x.right, key, value);
		} else if (cmp < 0) {
			x.left  = put(x.left,  key, value);
		} else {
			x.value = value;
		}
		
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}
	
	/***********************************************************************
	 *  Search BST for given key, and return associated value if found,
	 *  return null if not found
	***********************************************************************/
	public boolean contains(Key key) {
		return get(key) != null;
	}
	
	public Value get(Key key) {
		return get(root, key);
	}
	
	private Value get(Node x, Key key) {
		if (x == null) {
			return null;
		}
		
		int cmp = key.compareTo(x.key);
		if (cmp > 0) {
			return get(x.right, key);
		} else if (cmp < 0) {
			return get(x.left, key);
		} else {
			return x.value;
		}
	}
	
	
	/***********************************************************************
	 *  Delete
	***********************************************************************/
	public void deleteMin() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		root = deleteMin(root);
	}
	
	private Node deleteMin(Node x) {
		if (x.left == null) {
			return x.right;
		}
		
		x.left = deleteMin(x.left);
		x.size = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public void deleteMax() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		root = deleteMax(root);
	}
	
	private Node deleteMax(Node x) {
		if (x.right == null) {
			return x.left;
		}
		x.right = deleteMax(x.right);
		x.size = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public void delete(Key key) {
		root = delete(root, key);
	}
	
	private Node delete(Node x, Key key) {
		if (x == null) {
			return null;
		}
		
		int cmp = key.compareTo(x.key);
		if (cmp > 0) {
			x.right = delete(x.right, key);
		} else if (cmp < 0) {
			x.left = delete(x.left, key);
		} else {
			if (x.left == null) {
				return x.right;
			}
			if (x.right == null) {
				return x.left;
			}
			Node t = x;
			x = min(t.right);
			x.right = deleteMin(t.right);
			x.left = t.left;
		}
		x.size = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	
	/***********************************************************************
	 *  Min value, Max value, floor, and ceiling
	***********************************************************************/
	public Key min() {
		if (isEmpty()) {
			return null;
		}
		return min(root).key;
	}
	
	private Node min(Node x) {
		if (x.left == null) {
			return x;
		}
		return min(x.left);
	}
	
	public Key max() {
		if (isEmpty()) {
			return null;
		}
		return max(root).key;
	}
	
	private Node max(Node x) {
		if (x.right == null) {
			return x;
		}
		return max(x.right);
	}
	
	
	/***********************************************************************
	    *  Rank and selection Based on 0.
	 ***********************************************************************/
	public Key select(int k) {
		if (k < 0 || k >= size()) {
			return null;
		}
		
		Node x = select(root, k);
		return x.key;
	}
	
	// Return key of rank k.
	private Node select(Node node, int k) {
		if (node == null) {
			return null;
		}
		int leftSize = size(node.left);
		if (leftSize > k) {
			return select(node.left, k);
		} else if (leftSize < k) {
			return select(node.right, k - leftSize - 1);
		}
		
		return node;
	}
	
	public int rank(Key key) {
		return rank(root, key);
	}
	
	// Number of keys in the subtree less than key.
	private int rank(Node node, Key key) {
		int cmp = key.compareTo(node.key);
		if (cmp > 0) {
			return size(node.left) + 1 + rank(node.right, key);
		} else if (cmp < 0) {
			return rank(node.left, key);
		}
		return size(node.left);
	}
	
	
	/***********************************************************************
	    *  Range count and range search.
	***********************************************************************/
	public Iterable<Key> keys() {
		return keys(min(), max());
	}
	
	public Iterable<Key> keys(Key low, Key high) {
		List<Key> result = new ArrayList<Key>();
		keys(root, result, low, high);
		return result;
	}
	
	private void keys(Node x, List<Key> list, Key low, Key high) {
		if (x == null) {
			return;
		}
		int cmpLow = low.compareTo(x.key);
		int cmpHigh = high.compareTo(x.key);
		if (cmpLow < 0) {
			keys(x.left, list, low, high);
		}
		
		if (cmpLow <= 0 && cmpHigh >= 0) {
			list.add(x.key);
		}
		
		if (cmpHigh > 0) {
			keys(x.right, list, low, high);
		}
	}
	
	public int height() {
		return height(root);
	}
	
	private int height(Node x) {
		if (x == null) {
			return 0;
		}
		return 1 + Math.max(height(x.left), height(x.right));
	}
	
	
	// level order traversal
	public Iterable<Key> levelOrder() {
		List<Key> result = new ArrayList<Key>();
		ArrayDeque<Node> queue = new ArrayDeque<Node>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				Node node = queue.poll();
				result.add(node.key);
				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
		}
		
		return result;
	}
	
	
	/***********************************************************************
	    *  Inorder Iterator.
	***********************************************************************/
	public Iterator<Key> iterator() {
		return new InorderIterator();
	}
	
	private class InorderIterator implements Iterator<Key> {
		
		private Deque<Node> stack = new ArrayDeque<Node>();
		
		private void pushLeft(Node x) {
			while (x != null) {
				stack.push(x);
				x = x.left;
			}
		}
		
		public InorderIterator() {
			pushLeft(root);
		}

		public boolean hasNext() {
			return !stack.isEmpty();
		}

		public Key next() {
			Node x = stack.pop();
			pushLeft(x.right);
			return x.key;
		}
	}
	
	
	/***********************************************************************
	    *  Preorder Iterator.
	***********************************************************************/
	public Iterator<Key> preorderIterator() {
		return new PreorderIterator();
	}
	
	private class PreorderIterator implements Iterator<Key> {
		
		private Deque<Node> stack = new ArrayDeque<Node>();
		
		public PreorderIterator() {
			if (root != null) {
				stack.push(root);
			}
		}

		public boolean hasNext() {
			return !stack.isEmpty();
		}

		public Key next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Node result = stack.pop();
			if (result.right != null) {
				stack.push(result.right);
			}
			if (result.left != null) {
				stack.push(result.left);
			}
			return result.key;
		}
		
	}
	
	
	/***********************************************************************
	    *  Postorder Iterator.
	***********************************************************************/
	public Iterator<Key> postorderIterator() {
		return new PostorderIterator();
	}
	
	private class PostorderIterator implements Iterator<Key> {
		
		private Deque<Node> stack = new ArrayDeque<Node>();
		private Node prev;
		private Node curr;
		
		public PostorderIterator() {
			prev = null;
			curr = null;
			if (root != null) {
				stack.push(root);
			}
		}

		public boolean hasNext() {
			return !stack.isEmpty();
		}

		public Key next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Key result = null;
			while (result == null) {
				curr = stack.peek();
				if (prev == null || prev.left == curr || prev.right == curr) {
					if (curr.left != null) {
						stack.push(curr.left);
					} else if (curr.right != null) {
						stack.push(curr.right);
					} else {
						result = stack.pop().key;
					}
				} else if (curr.left == prev) {
					if (curr.right != null) {
						stack.push(curr.right);
					} else {
						result = stack.pop().key;
					}
				} else {
					result = stack.pop().key;
				}
				prev = curr;
			}
			
			return result;
		}
		
	}
	
	
	/***********************************************************************
	    *  Node class.
	***********************************************************************/
	private class Node {
		private Key key;
		private Value value;
		private Node left;
		private Node right;
		private int size;
		
		public Node(Key key, Value value, int size) {
			this.key = key;
			this.value = value;
			this.size = size;
			left = null;
			right = null;
		}
	}
	
}