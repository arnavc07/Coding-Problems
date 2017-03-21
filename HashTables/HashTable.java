import java.util.ArrayList; 

class HashNode<K,V> {
	K key; // key
	V value; // value
	
	HashNode<K,V> next; // reference to next node
	
	public HashNode(K key, V value) {
		this.key = key;
		this.value = value;
		this.next = null; 
	}
}

public class HashTable<K,V> {
	 
	private ArrayList<HashNode<K,V>> map;
	private int numBuckets; 
	private int size; 
	
	// construct empty hash table
	public HashTable() {
		map = new ArrayList<>(); 
		numBuckets = 20; // initialize numBuckets
		size = 0; // initialize size of current hash map
		
		for (int i = 0; i < numBuckets; i++) {
			map.add(null);
		}
	}
	
	// return size of hashmap
	public int getSize() {
		return size;
	}
	
	// is the hashmap empty?
	public boolean isEmpty() {
		return (size == 0); 
	}
	
	// return the value associated with a key
	public V get(K key) {
		int hCode = getMapIndex(key); 
		HashNode<K,V> head = map.get(hCode);
		
		// search for key in chain
		while (head != null) {
			if (head.key.equals(key))
				return head.value;
			else
				head = head.next; 
		}
		
		// return null if key not found
		return null; 
	}
	
	// remove a (Key,Value) pair from the hash table, if it exists
	public void remove(K key) {
		int index = getMapIndex(key); 
		HashNode<K,V> start = map.get(index);
		HashNode<K,V> adj = start.next;
		
		if (start.key.equals(key)) {
			size--;
			map.set(index,adj);
		}
		else {
			while (adj != null) {
				if (adj.key.equals(key)) { // one of the elements in the list (not the head) is the key
					size--;
					start.next = adj.next; // remove the element, decrease size of map by one
					break;
				}
				// otherwise, keep going through the list
				start = adj;
				adj = adj.next;
			}
		}	
	}
	
	// add an element to the hash map
	public void add(K key, V value) {
		if (get(key) != null && get(key).equals(value))
			return;
		else {
			HashNode<K,V> node = new HashNode(key,value); 
			int index = getMapIndex(key);
			
			if (map.get(index) == null) // if there's nothing at the index, set index to node
				map.set(index,node);
			else { 
				// if there's a collision, set the head of the list at index to be the new node, with a ref to the old head
				HashNode<K,V> head = map.get(index);
				node.next = head;
				map.set(index,node);
			}
			
			size++; // increase size of hash map
			
			// load balancing
			if (((1.0*size)/numBuckets) >= 0.7) {
				doubleMapSize();
				
				ArrayList<HashNode<K,V>> temp = map; 
				map = new ArrayList<>(); 
				
				for (int i = 0; i < numBuckets; i++)
					map.add(null);
				
				for (HashNode<K,V> n : temp)
					while (node != null) {
						add(n.key, n.value);
						n = node.next; 
					}
			}
			
		}
	}
	
	// String representation of hash table
	public String toString() {
		String mapString = ""; 
		
		for (HashNode<K,V> node : map) {
			while (node != null) {
				mapString += "Key: " + node.key + " Value : " + node.value + "\n";
				node = node.next; 
			}
		}
			
		
		return mapString;
	}
	
	// helper function for getting the mapped index of a key
	private int getMapIndex (K key) {
		int hCode = key.hashCode();
		return (hCode % numBuckets); 
	}
	
	// helper function to double hashMap size if loadFactor > 0.7
	private void doubleMapSize() {
		int newNumBuckets = numBuckets*2; 
		for (int i = numBuckets; i < newNumBuckets; i++)
			map.add(null);
		numBuckets = newNumBuckets; 
	}
	
	// test the hash table
	public static void main(String[] args) {
		HashTable<String,String> students = new HashTable<>();
		
		students.add("17354","Arnav");
		students.add("23234","Michael");
		students.add("435t57","Larry");
		
		System.out.println(students);
		System.out.println("Size of students is: " + students.getSize());
		
		students.remove("23234");
		System.out.println(students);
		System.out.println("Size of students is: " + students.getSize());
		
	}
}