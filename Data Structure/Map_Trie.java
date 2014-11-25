import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This is a Trie class which implemented based on HashMap
 * I use HashMap to implement TrieNode instead of fixed size array
 * This class is NOT THREAD-SAFE
 */

public class Map_Trie<Value> {
	
	private TrieNode root;
	private int size;
	
	public Map_Trie() {
		size = 0;
	}
	
	
	/**
     	* Returns the value associated with the given key.
     	* @throws NullPointerException if key is null
     	*/
	@SuppressWarnings("unchecked")
	public Value get(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		
		TrieNode node = get(root, key, 0);
		if (node == null) {
			return null;
		}
		
		return (Value) node.value;
	}
	
	private TrieNode get(TrieNode node, String key, int index) {
		if (node == null) {
			return null;
		}
		
		if (index == key.length()) {
			return node;
		}
		
		return get(node.getChildNodeForChar(key.charAt(index)), key, index + 1);
	}
	
	
	
	/**
	* Inserts the key-value pair into the symbol table, overwriting the old value
	* with the new value if the key is already in the symbol table.
     	* If the value is null, this effectively deletes the key from the symbol table.
     	* @throws NullPointerException if key is null
	*/
	public void put(String key, Value v) {
		if (key == null) {
			throw new NullPointerException();
		}
		root = put(root, key, v, 0);
	}
	
	private TrieNode put(TrieNode node, String key, Value v, int index) {
		if (node == null) {
			node = new TrieNode();
		}
		
		if (index == key.length()) {
			if (node.value == null) {
				size++;
			}
			node.value = v;
			return node;
		}
		
		char c = key.charAt(index);
		node.addNodeForChar(c, put(node.getChildNodeForChar(c), key, v, index + 1));
		return node;
	}
	
	
	/**
     	* Does this symbol table contain the given key?
     	* @throws NullPointerException if key is null
     	*/
	public boolean contains(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return get(key) != null;
	}
	
	
	
    	/**
     	* Returns the number of key-value pairs in this symbol table.
     	* @return the number of key-value pairs in this symbol table
     	*/
	public int size() {
		return size;
	}

	
	
    	/**
     	* Is this symbol table empty?
     	* @return true if this symbol table is empty and false otherwise
     	*/
	public boolean isEmpty() {
		return size == 0;
	}
	
	
	
	/**
     	* Returns all keys in the symbol table as an Iterable.
     	* To iterate over all of the keys in the symbol table named st,
     	* use the foreach notation: for (Key key : st.keys()).
     	* @return all keys in the sybol table as an Iterable
     	*/
	public Iterable<String> keys() {
		return keysWithPrefix("");
	}
	
	
	/**
	* Backtracking Algorithm
     	* Returns all of the keys in the set that start with prefix.
     	* @return all of the keys in the set that start with prefix, as an iterable
     	*/
	public Iterable<String> keysWithPrefix(String prefix) {
		List<String> result = new ArrayList<String>();
		TrieNode node = get(root, prefix, 0);
		helper(node, new StringBuilder(prefix), result);
		return result;
	}
	
	private void helper(TrieNode node, StringBuilder sb, List<String> result) {
		if (node == null) {
			return;
		}
		
		if (node.value != null) {
			result.add(sb.toString());
		}
		
		for (char c : node.keySet()) {
			sb.append(c);
			helper(node.getChildNodeForChar(c), sb, result);
			sb.deleteCharAt(sb.length() - 1);
		}
	}
	
	
	
	/**
     	* Returns the string in the symbol table that is the longest prefix of query,
     	* or null, if no such string.
     	* @throws NullPointerException if query is null
     	* @return the string in the symbol table that is the longest prefix of query,
     	*     or null if no such string
     	*/
	public String longestPrefixOf(String s) {
		if (s == null) {
			throw new NullPointerException();
		}
		
		return s.substring(0, helper(root, s, 0, 0));
	}
	
	private int helper(TrieNode node, String s, int index, int length) {
		if (node == null) {
			return length;
		}
		
		if (node.value != null) {
			length = index;
		}
		
		if (index == s.length()) {
			return length;
		}
		
		char c = s.charAt(index);
		return helper(node.getChildNodeForChar(c), s, index + 1, length);
	}
	
	
	
	/**
     	* Removes the key from the set if the key is present.
     	* @throws NullPointerException if key is null
     	*/
	public void delete(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		root = delete(root, key, 0);
	}
	
	private TrieNode delete(TrieNode node, String key, int index) {
		if (node == null) {
			return null;
		}
		
		if (index == key.length()) {
			if (node.value != null) {
				size--;
			}
			node.value = null;
		} else {
			char c = key.charAt(index);
			node.addNodeForChar(c, delete(node.getChildNodeForChar(c), key, index + 1));
		}
		
		// remove subtrie rooted at x if it is completely empty
		if (node.value != null) {
			return node;
		}
		
		for (char c : node.keySet()) {
			if (node.getChildNodeForChar(c) != null) {
				return node;
			}
		}
		
		return null;
	}
	
	
	
	private static class TrieNode {
		private Object value;
		private Map<Character, TrieNode> child;
		
		public TrieNode() {
			child = new HashMap<Character, TrieNode>();
		}
		
		private TrieNode getChildNodeForChar(char c) {
			if (child.containsKey(c)) {
				return child.get(c);
			}
			return null;
		}
		
		private void addNodeForChar(char c, TrieNode node) {
			child.put(c, node);
		}
		
		private Set<Character> keySet() {
			return child.keySet();
		}
	}
	
}
