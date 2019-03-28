/**
 * Filename:   MyProfiler.java
 * Project:    p3b-201901     
 * Authors:    Roma Kashpar, lecture 001
 *
 * Semester:   Spring 2019
 * Course:     CS400
 * 
 * Due Date:   3-28-2019 10pm
 * Version:    1.0
 * 
 * Credits:    TODO: name individuals and sources outside of course staff
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

// Used as the data structure to test our hash table against
import java.util.ArrayList;
import java.util.TreeMap;

public class MyProfiler<K extends Comparable<K>, V> {

    HashTableADT<K, V> hashtable;
    TreeMap<K, V> treemap;
    
    public MyProfiler() {
        // Instantiate your HashTable and Java's TreeMap
    	
    	hashtable = new HashTable<K, V>();
    	treemap = new TreeMap<K, V>();
    }
    
    public void insert(K key, V value) {
        // Insert K, V into both data structures
    	try {
    		hashtable.insert(key, value);
    		treemap.put(key, value);
    	}
    	catch (IllegalNullKeyException e) {}
    	catch (DuplicateKeyException f) {}
    }
    
    public void retrieve(K key) {
        // get value V for key K from data structures
    	
    	try {
    		hashtable.get(key);
    		treemap.get(key);
    	}
    	catch (KeyNotFoundException g) {}
    	catch (IllegalNullKeyException h) {}
    }
    
    public static void main(String[] args) {
        try {
            int numElements = Integer.parseInt(args[0]);
            MyProfiler<Integer, Integer> profile = new MyProfiler<Integer, Integer>();

            ArrayList<Integer> elements = new ArrayList<Integer>();
            
            for (int i = 0; i < numElements; i++) {
                profile.insert(i, i);
                
            }
            
            for (int j = 0; j < numElements; j++) {
            	profile.retrieve(j);
            }
            
            // Create a profile object. 
            // For example, Profile<Integer, Integer> profile = new Profile<Integer, Integer>();
            // execute the insert method of profile as many times as numElements
            // execute the retrieve method of profile as many times as numElements
            // See, ProfileSample.java for example.           
        
            String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
            System.out.println(msg);
        }
        catch (Exception e) {
            System.out.println("Usage: java MyProfiler <number_of_elements>");
            System.exit(1);
        }
    }
}
