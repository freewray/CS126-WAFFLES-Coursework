package uk.ac.warwick.cs126.structures;

public class AVLTreeIDCounter extends AVLTree<IDCounter, Long> {

    public int customCompare(IDCounter o1, IDCounter o2) {
        return o1.compareTo(o2);
    }

    public int idOnlyCompare(IDCounter o1, IDCounter o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

    public int idOnlyCompare(Long id, IDCounter o) {
        return id.compareTo(o.getIdentifier());
    }
}