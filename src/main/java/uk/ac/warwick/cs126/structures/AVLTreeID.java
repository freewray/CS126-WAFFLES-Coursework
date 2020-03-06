package uk.ac.warwick.cs126.structures;

/**
 * AVL tree structure for comparable elements
 */
public class AVLTreeID extends AVLTree<Long, Long> {

    public int customCompare(Long o1, Long o2) {
        return o1.compareTo(o2);
    }

    public int idOnlyCompare(Long id, Long o) {
        return id.compareTo(o);
    }
}
