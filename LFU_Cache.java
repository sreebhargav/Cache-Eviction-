import java.util.*;

class LFUCache {
    private final int capacity;
    private int minFreq;
    private final Map<Integer, Node> map;  // Key → Node
    private final Map<Integer, LinkedHashSet<Node>> freqToList;  // Frequency → Set of Nodes

    // Node class representing each entry in the cache
    class Node {
        int key, val, freq;
        Node(int key, int val) {
            this.key = key;
            this.val = val;
            this.freq = 1; // New node starts with frequency 1
        }
    }

    // Constructor to initialize LFU Cache with a given capacity
    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.map = new HashMap<>();
        this.freqToList = new HashMap<>();
    }

    // Get operation: Fetches the value for a given key and updates frequency
    public int get(int key) {
        if (!map.containsKey(key)) return -1;  // Key not found
        Node data = map.get(key);
        updateFrequency(data);  // Increase frequency
        return data.val;
    }

    // Put operation: Inserts a new key-value pair or updates an existing key
    public void put(int key, int value) {
        if (capacity == 0) return;  // If capacity is 0, no insertions allowed

        if (map.containsKey(key)) {
            Node data = map.get(key);
            data.val = value; // Update value
            updateFrequency(data); // Increase frequency
            return;
        }

        if (map.size() == capacity) {
            removeLFUNode(); // Remove least frequently used node
        }

        Node scam = new Node(key, value);
        map.put(key, scam);
        minFreq = 1; // Reset min frequency

        freqToList.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(scam);
    }

    // Updates the frequency of a node
    private void updateFrequency(Node node) {
        int freq = node.freq;
        freqToList.get(freq).remove(node); // Remove node from current frequency list

        // If the list becomes empty, update minFreq
        if (freqToList.get(freq).isEmpty()) {
            freqToList.remove(freq);
            if (minFreq == freq) minFreq++;
        }

        // Increase frequency and add the node to the new frequency list
        node.freq++;
        freqToList.computeIfAbsent(node.freq, k -> new LinkedHashSet<>()).add(node);
    }

    // Removes the least frequently used node (if multiple, removes least recently used)
    private void removeLFUNode() {
        if (!freqToList.containsKey(minFreq)) return;

        Node node = freqToList.get(minFreq).iterator().next(); // Get LRU node in minFreq
        freqToList.get(minFreq).remove(node);

        if (freqToList.get(minFreq).isEmpty()) {
            freqToList.remove(minFreq);
           //minFreq++; // Update minFreq after removal
        }

        map.remove(node.key);
    }
}
