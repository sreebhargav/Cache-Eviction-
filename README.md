# Cache-Eviction
# Java Cache Implementations: LRU Cache + LFU Cache

This repo contains two classic cache eviction strategies implemented in **Java**:

- **LRU Cache (Least Recently Used)**  
  Evicts the entry that has not been used for the longest time.

- **LFU Cache (Least Frequently Used)**  
  Evicts the entry with the lowest access frequency. If multiple keys share the same frequency, evict the **Least Recently Used** among them.

Both implementations are designed for interview readiness and focus on clean `O(1)` operations.

---

## 1) LRU Cache (HashMap + Doubly Linked List)

### Idea
- **HashMap** gives `O(1)` access to nodes by key.
- **Doubly Linked List** maintains usage order:
  - MRU near `head`
  - LRU near `tail`

### Operations
- `get(key)`: return value and move node to MRU
- `put(key, value)`: insert/update and evict LRU when capacity is full

### Complexity
- Time: `O(1)` for `get` and `put`
- Space: `O(capacity)`

---

## 2) LFU Cache (HashMap + Frequency Buckets)

### Data Structures
- `map: key -> Node(key, val, freq)` for `O(1)` access
- `freqToList: freq -> LinkedHashSet<Node>` to store nodes by frequency  
  `LinkedHashSet` preserves insertion order, which helps implement LRU tie-break within the same frequency.

### How Eviction Works
- Track `minFreq`, the smallest frequency currently present.
- When capacity is full:
  - Evict from `freqToList.get(minFreq)`
  - If multiple nodes have `minFreq`, evict the **least recently used** node in that bucket (the first item in the set)

### Operations
- `get(key)`: increase node frequency and return value
- `put(key, value)`:
  - update if exists (and increase frequency)
  - else insert with freq = 1
  - if full, remove LFU (and LRU tie-break)

### Complexity
- Time: `O(1)` average for `get` and `put`
- Space: `O(capacity)`

---

## When to Use Which

### LRU is best when
- recent access is a strong predictor of future access
- you want a simpler cache with predictable behavior

### LFU is best when
- “hot” keys stay hot for longer periods
- you want frequently used items to survive even if they are not accessed very recently
