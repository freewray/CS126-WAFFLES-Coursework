package uk.ac.warwick.cs126.structures;

public class AVLTreeCounter extends AVLTree<Counter<String>, String> {

    public int customCompare(Counter<String> o1, Counter<String> o2) {
        return o1.compareTo(o2);
    }

    public int idOnlyCompare(Counter<String> o1, Counter<String> o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

    public int idOnlyCompare(String id, Counter<String> o) {
        return id.compareTo(o.getIdentifier());
    }

    public Counter<String> searchKeyword(String keyword) {
        return searchByID(keyword);
    }

    public void insertByWord(Counter<String> key) {
        insertByID(key);
    }
}