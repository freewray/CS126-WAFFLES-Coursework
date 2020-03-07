package uk.ac.warwick.cs126.structures;

public class AVLTreeRating extends AVLTree<Rating, Long> {

    /**
     * default comparison decleared by the tree
     *
     * @param o1 object 1 to be compared with
     * @param o2 object 2 to be compared with
     * @return nagative number if o1 is smaller
     * positive number if o1 is bigger
     */
    public int customCompare(Rating o1, Rating o2) {
        return o1.compareTo(o2);
    }

    /**
     * id comparison (restaurant id)
     *
     * @param o1 object 1s'id to be compared with
     * @param o2 object 2s'id to be compared with
     * @return nagative number if o1s'id is smaller
     * positive number if o1s'id is bigger
     */
    public int idOnlyCompare(Rating o1, Rating o2) {
        return o1.getId().compareTo(o2.getId());
    }

    /**
     * @param id identifier to be compared (id or keyword)
     * @param o  object to be compared with
     * @return nagative number if o1s'id is smaller
     * positive number if o1s'id is bigger
     */
    public int idOnlyCompare(Long id, Rating o) {
        return id.compareTo(o.getId());
    }
}