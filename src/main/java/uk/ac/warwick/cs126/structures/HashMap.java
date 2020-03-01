package uk.ac.warwick.cs126.structures;

// This line allows us to cast our object to type (E) without any warnings.
// For further details, please see: http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/SuppressWarnings.html
@SuppressWarnings("unchecked")
public class HashMap<K extends Comparable<K>, V> {

    protected KeyValuePairLinkedList<K, V>[] table;

    public HashMap() {
        /* for very simple hashing, primes reduce collisions */
        this(1003);
    }

    public HashMap(int size) {
        table = new KeyValuePairLinkedList[size];
        initTable();
    }

    // INCOMPLETE.
    public V find(K key) {
        // returns the number of comparisons required to find element using Linear
        // Search.
        int hash_code = hash(key);
        int location = hash_code % table.length;
        int cnt = 0; // count the number of comparisons
        ListElement<KeyValuePair<K, V>> ptr = table[location].getHead();
        while (ptr != null) {
            cnt++;
            if (key.equals(ptr.getValue().getKey())) {
//                System.out.println("found: [" + ptr.getValue().getKey() + "," + ptr.getValue().getValue() + "] tried: " + cnt + " times");
                return ptr.getValue().getValue();
            }
            ptr = ptr.getNext();
        }
        return null;
    }

    protected void initTable() {
        for (int i = 0; i < table.length; i++) {
            table[i] = new KeyValuePairLinkedList<>();
        }
    }

    protected int hash(K key) {
        int code = key.hashCode();
        return code;
    }

    public void add(K key, V value) {
        int hash_code = hash(key);
        int location = hash_code % table.length;
//        System.out.println("Adding " + value + " under key " + key + " at location " + location);
        table[location].add(key, value);
    }

    public V get(K key) {
        int hash_code = hash(key);
        int location = hash_code % table.length;

        // ListElement<KeyValuePair> ptr = table[location].head;

        return table[location].get(key).getValue();
    }

    public String toString() {
        String res = "{";
        for (int i = 0; i < table.length; i++) {
            ListElement<KeyValuePair<K, V>> ptr = table[i].getHead();
            while (ptr != null) {
                res += "[" + ptr.getValue().getKey() + ",\"" + ptr.getValue().getValue() + "\"]";
                ptr = ptr.getNext();
            }
            if (i < table.length - 1)
                res += "\n";
        }
        res += "}";
        return res;
    }
}
