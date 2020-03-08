package uk.ac.warwick.cs126.structures;

public class AVLTreeIDCounter extends AVLTree<IDCounter, Long> {

    public AVLTreeIDCounter() {
        super("default");
    }

    public AVLTreeIDCounter(String sortBy) {
        super(sortBy);
    }

    public int customCompare(IDCounter o1, IDCounter o2) {
        if (this.sortBy.equalsIgnoreCase("descendingDate")) {
            if (o2.getLatestReviewDate().compareTo(o1.getLatestReviewDate()) == 0)
                return idOnlyCompare(o1, o2);
            else
                return o2.getLatestReviewDate().compareTo(o1.getLatestReviewDate());
        } else
            return o1.compareTo(o2);
    }

    public int idOnlyCompare(IDCounter o1, IDCounter o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

    public int idOnlyCompare(Long id, IDCounter o) {
        return id.compareTo(o.getIdentifier());
    }
}