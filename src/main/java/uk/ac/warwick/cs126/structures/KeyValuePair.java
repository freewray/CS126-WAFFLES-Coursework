package uk.ac.warwick.cs126.structures;

public class KeyValuePair<K extends Comparable<K>, V> implements Comparable<KeyValuePair<K, V>> {

    protected final K key;
    protected final V value;

    public KeyValuePair(K k, V v) {
        key = k;
        value = v;
    }

    
    /** 
     * @return K the key of the pair
     */
    public K getKey() {
        return key;
    }

    
    /** 
     * @return V the value of the pair
     */
    public V getValue() {
        return value;
    }

    
    /** 
     * @param o the other key value pair object to compare to
     * @return nagative number if this'id is smaller
     *         positive number if this'id is bigger
     */
    public int compareTo(KeyValuePair<K, V> o) {
        return o.getKey().compareTo(this.getKey());
    }
}
