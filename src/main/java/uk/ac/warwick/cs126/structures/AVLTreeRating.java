package uk.ac.warwick.cs126.structures;

public class AVLTreeRating extends AVLTree<Rating, Long> {

    public int customCompare(Rating o1, Rating o2) {
        return o1.compareTo(o2);
    }

    public int idOnlyCompare(Rating o1, Rating o2) {
        return o1.getId().compareTo(o2.getId());
    }

    public int idOnlyCompare(Long id, Rating o) {
        return id.compareTo(o.getId());
    }
}