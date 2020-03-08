package uk.ac.warwick.cs126.structures;

public class AVLTreeCounter extends AVLTree<Counter<String>, String> {

    /**
     * @param o1 object 1 to be compared with
     * @param o2 object 2 to be compared with
     * @return nagative number if o1 is smaller
     * positive number if o1 is bigger
     */
    public int customCompare(Counter<String> o1, Counter<String> o2) {
        return o1.compareTo(o2);
    }

    /**
     * Only compares the string stored in word counter object
     *
     * @param o1 object 1 to be compared with
     * @param o2 object 2 to be compared with
     * @return nagative number if o1 is smaller
     * positive number if o1 is bigger
     */
    public int idOnlyCompare(Counter<String> o1, Counter<String> o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

    /**
     * @param id actual keyword to compare
     * @param o  the other object to compare with
     * @return nagative number if id is smaller
     * positive number if object stored number is bigger
     */
    public int idOnlyCompare(String id, Counter<String> o) {
        return id.compareTo(o.getIdentifier());
    }

    /**
     * @param keyword tobe searched in the tree
     * @return Counter<String> that included with the input string
     */
    public Counter<String> searchKeyword(String keyword) {
        return searchByID(keyword.toLowerCase());
    }

    /**
     * @param key insert new key into the tree
     */
    public void insertByWord(Counter<String> key) {
        insertByID(key);
    }
}