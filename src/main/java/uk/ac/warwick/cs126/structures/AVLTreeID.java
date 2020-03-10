package uk.ac.warwick.cs126.structures;

/**
 * AVL tree structure for comparable elements
 */
public class AVLTreeID extends AVLTree<Long, Long> {

    /** 
     * @param o1 ID 1 to compare to
     * @param o2 ID 2 to compare to
     * @return nagative number if o1 id is smaller
     *         positive number if o2 id is smaller
     */
    public int customCompare(Long o1, Long o2) {
        return o1.compareTo(o2);
    }

    /** 
     * @param id to compare to
     * @param object ID to compare to
     * @return nagative number if id id is smaller
     *         positive number if object's id is smaller
     */
    public int idOnlyCompare(Long id, Long o) {
        return id.compareTo(o);
    }
}
