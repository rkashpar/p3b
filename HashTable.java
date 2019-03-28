// Roma Kashpar
// kashpar@wisc.edu
// CS 400 Spring 2019
// p3a
// HashTable.java
//
//
// Collision resolution handled by a linkedList bucket
//
// Object converted to hash index using javas built in hashCode(), then placed into an array.
// linkedList used to handle collisions
//
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	// linkedList structure to be used to handle collisions and store nodes with the same hash index
	private class linkedList<k extends Comparable<K>, V> {

		private node<K, V> head; // head node in the list
		private int listSize; // size of the list

		// no-arg constructor for a linkedList
		private linkedList() {
			head = null;
			listSize = 0;
		}

		// insert operation in the linkedList
		private void insert(K key, V value) throws DuplicateKeyException {
			
			// Case 1: empty list, adds to head
			if (head == null) {
				head = new node<K, V>(key, value);
				listSize++;
				
			//Case 2: key already exists in the list, throws exception
			} else if (contains(key) == true) {
				throw new DuplicateKeyException();
				
			//Case 3: node can be appended to list
			} else {
				node<K, V> temp = head;
				
				// locates the last node in the list
				while (temp.next != null) {
					temp = temp.next;
				}
				
				// adds the new node after said last node
				temp.next = new node<K, V>(key, value);
				listSize++;
			}
		}

		// removes a node from the linkedList
		private boolean remove(K key) {
			
			// Case 1: list does not contain node to be removed, return false
			if (contains(key) != true) {
				return false;
				
			// Case 2: head node is the one to be removed, set head to the following node in the list
			} else if (head.key.compareTo(key) == 0) {
				head = head.next;
				listSize--;
				return true;
				
			// Case 3: non head node is removed
			} else {
				node<K, V> temp = head;
				
				// locates the node to be removed
				while (temp.key.compareTo(key) != 0) {
					temp = temp.next;
				}
				
				// removes node by overwriting pointer to the node following 
				temp.next = temp.next.next;
				listSize--;
				return true;

			}
		}

		// returns the value of a given key if the key exists
		private V get(K key) throws KeyNotFoundException {
			node<K, V> temp = head;
			if (contains(key) != true) {
				throw new KeyNotFoundException();
			} else {
				while (temp.key.compareTo(key) != 0) {
					temp = temp.next;
				}
				return temp.value;
			}
		}

		// attempts to locate a key in the linkedList
		private boolean contains(K key) {
			node<K, V> temp = head;
			while (temp != null) {
				if (temp.key.compareTo(key) == 0) {
					return true;
				}
				temp = temp.next;
			}
			return false;
		}

		private node getHead() {
			return head;
		}
	}

	// node class to be used in the hashArray and potentially 
	// in linkedLists in the array in case of collisions
	private class node<K extends Comparable<K>, V> {
		private K key;
		private V value;
		private node next; // the next node in the linked list

		// node constructor for given key and value pair
		private node(K key, V value) {
			this.key = key;
			this.value = value;
			next = null;
		}

		// returns the nodes key
		private K getKey() {
			return key;
		}

		// returns the nodes value
		private V getValue() {
			return value;
		}
	}

	int hashEntries; // number of key, value nodes in the hashArray
	int hashArraySize; // maximum capacity of the hashArray
	double loadFactorThreshold; // number of nodes divided by max capacity of the hashArray
	private linkedList<K, V>[] hashArray; // the hashArray itself that will store nodes in hash indexes

	// No arg constructor for the hashArray
	public HashTable() {
		hashArray = new linkedList[11];

		hashEntries = 0;
		hashArraySize = 11;
		loadFactorThreshold = 0.75;

	}

	// detailed constructor for the hashArray
	// initial capacity and load factor threshold
	// threshold is the load factor that causes a resize and rehash
	public HashTable(int initialCapacity, double loadFactorThreshold) {

		hashArray = new linkedList[initialCapacity];

		hashEntries = 0;
		hashArraySize = initialCapacity;
		this.loadFactorThreshold = loadFactorThreshold;

	}

	// Returns the load factor threshold that was passed into the 
	// constructor when creating the instance of the HashTable.
	public double getLoadFactorThreshold() {
		return loadFactorThreshold;
	}

	// Returns the current load factor for this hash table
	// load factor = number of items / current table size
	public double getLoadFactor() {
		return ((double)hashEntries / (double)hashArraySize);
	}

	// Return the current Capacity (table size) of the hash table array.
	public int getCapacity() {
		return hashArraySize;
	}

	// Returns the collision resolution scheme used for this hash table.
	// Implement with one of the following collision resolution strategies.
	// Define this method to return an integer to indicate which strategy.
	//
	// 5 CHAINED BUCKET: array of linked nodes
	public int getCollisionResolution() {
		return 5;
	}

	// Add the key,value pair to the data structure and increase the number of keys.
	// If key is null, throw IllegalNullKeyException;
	// If key is already in data structure, throw DuplicateKeyException();
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		
		// Case 1: given key is null, throw exception
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		// Case 2: given key has a new, never used before hash index location
		// creates a new linkedList bucket there
		if (hashArray[Math.abs(key.hashCode() % hashArraySize)] == null) {
			hashArray[Math.abs(key.hashCode() % hashArraySize)] = new linkedList<K, V>();
		}
		
		// Case 3: adds the given key to the linkedList at the hash location
		hashArray[Math.abs(key.hashCode()) % hashArraySize].insert(key, value);
		hashEntries++;

		// resizes array and rehashes nodes if loadFactor above threshold
		if (getLoadFactor() >= loadFactorThreshold) {
			
			// creates a temporary new hashArray that is double + 1 sized
			// creates temporary size value for the temporary array
			linkedList<K, V>[] tempHashArray = new linkedList[(2 * hashArraySize) + 1];
			int tempHashArraySize = (2 * hashArraySize) + 1;

			// rehashes and moves nodes to the temp array
			for (int i = 0; i < hashArray.length; i++) {
				if (hashArray[i] != null) {
					tempHashArray[Math.abs(hashArray[i].getHead().getKey().hashCode()) % tempHashArraySize] = hashArray[i];
				}
			}

			// reverts temporary variables to the actual ones
			hashArray = tempHashArray;
			hashArraySize = tempHashArraySize;

		}

	}

	// If key is found,
	// remove the key,value pair from the data structure
	// decrease number of keys.
	// return true
	// If key is null, throw IllegalNullKeyException
	// If key is not found, return false
	public boolean remove(K key) throws IllegalNullKeyException {
		
		// Case 1: give key is null, throws exception
		if (key == null) {
			throw new IllegalNullKeyException();
			
		// Case 2: key exists at hash location in the bucket, can be removed from the linkedList there
		}
		if (hashArray[Math.abs(key.hashCode()) % hashArraySize] != null
				&& hashArray[Math.abs(key.hashCode()) % hashArraySize].contains(key)) {
			hashEntries--;
			return hashArray[Math.abs(key.hashCode()) % hashArraySize].remove(key);

		}
		return false; // returns false if key doesn't exist

	}

	// Returns the value associated with the specified key
	// Does not remove key or decrease number of keys
	//
	// If key is null, throw IllegalNullKeyException
	// If key is not found, throw KeyNotFoundException().
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		
		// Case 1: null key given, throws exception
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		// Case 2: key not found at hash index bucket
		if (hashArray[Math.abs(key.hashCode()) % hashArraySize] == null) {
			throw new KeyNotFoundException();
		}

		// Case 3: key found, calls the linkedList method to return its value
		return hashArray[Math.abs(key.hashCode()) % hashArraySize].get(key);

	}

	// Returns the number of key,value pairs in the data structure
	public int numKeys() {
		return hashEntries;
	}
}