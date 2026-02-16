import java.util.*;
//doubly linkedilist(head,tail), hashmap
class LRUCache { 
    Node head = new Node(0, 0);             
    Node tail = new Node(0, 0);
    HashMap<Integer, Node> map = new HashMap<>();
    int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;  // Corrected reference   we are using doubly LinkedList + HashMap
        tail.prev = head;
    }

    public int get(int key) {
        if (map.containsKey(key)) {  
            Node data = map.get(key);     // if map has key then make it as MRU in the cache 
            remove(data);
            insert(data);
            return data.val;
        }
        return -1;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {               // if map has key remove it and make it MRU 
            remove(map.get(key));
        }
        if (map.size() == capacity) {        // if size limit exceeds remove LRU one
            remove(tail.prev);
        }
        insert(new Node(key, value));      // insert new element at the head
    }

    void insert(Node node) {
        map.put(node.key, node);            // add the node at the head -> node -> rest elements
        Node headNext = head.next;
        head.next = node;
        node.prev = head;
        node.next = headNext;
        headNext.prev = node;
    }

    void remove(Node node) {              
        map.remove(node.key);
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    class Node {
        Node prev, next;
        int key, val;
        Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }
}
