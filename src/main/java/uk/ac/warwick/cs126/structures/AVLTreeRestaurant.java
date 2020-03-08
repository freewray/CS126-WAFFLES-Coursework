package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Restaurant;

public class AVLTreeRestaurant extends AVLTree<Restaurant, Long> {

    public AVLTreeRestaurant() {
        super();
    }

    public AVLTreeRestaurant(String sortBy) {
        super(sortBy);
    }

    /**
     * @param r1 's id to be compared with
     * @param r2 's id to be compared with
     * @return negative if r1's id is smaller
     * positive if r1's id is bigger
     */
    private int idCompare(Restaurant r1, Restaurant r2) {
        return r1.getID().compareTo(r2.getID());
    }

    private int nameCompare(Restaurant r1, Restaurant r2) {
        int nameCompare = r1.getName().compareToIgnoreCase(r2.getName());
        if (nameCompare == 0)
            return idCompare(r1, r2);
        else
            return nameCompare;
    }

    /**
     * sorted by Date Established, from oldest to most recent. If they have the same
     * Date Established, then it is sorted alphabetically by the restaurant Name. If
     * they have the same restaurant Name, then it is sorted in ascending order of
     * their ID.
     */
    private int dateCompare(Restaurant r1, Restaurant r2) {
        int dateCompare = r1.getDateEstablished().compareTo(r2.getDateEstablished());
        if (dateCompare == 0)
            return nameCompare(r1, r2);
        else
            return dateCompare;
    }

    /**
     * sorted in descending order of Warwick Stars. If they have the same Warwick
     * Stars, then it is sorted alphabetically by the restaurant Name. If they have
     * the same restaurant Name, then it is sorted in ascending order of their ID.
     */
    private int warwickStarCompare(Restaurant r1, Restaurant r2) {
        if (r1.getWarwickStars() == r2.getWarwickStars())
            return nameCompare(r1, r2);
        else
            return r2.getWarwickStars() - r1.getWarwickStars();
    }

    /**
     * sorted in descending order of Rating. If they have the same Rating, then it
     * is sorted alphabetically by the restaurant Name. If they have the same
     * restaurant Name, then it is sorted in ascending order of their ID.
     */
    private int ratingCompare(Restaurant r1, Restaurant r2) {
        if (r1.getCustomerRating() == r2.getCustomerRating())
            return nameCompare(r1, r2);
        else
            return r2.getCustomerRating() < r1.getCustomerRating() ? -1 : 1;
    }

    /**
     * default comparison declared by the tree
     *
     * @param r1 restaurant 1 to be compared with
     * @param r2 restaurant 2 to be compared with
     * @return negative number if r1 is smaller
     * positive number if r2 is bigger
     */
    public int customCompare(Restaurant r1, Restaurant r2) {
        if (this.sortBy.equalsIgnoreCase("id"))
            return this.idCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("name"))
            return this.nameCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("date"))
            return this.dateCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("warwickStar"))
            return this.warwickStarCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("rating"))
            return this.ratingCompare(r1, r2);
        else
            return 0;
    }

    /**
     * id comparison
     *
     * @param r1 restaurant 1s'id to be compared with
     * @param r2 restaurant 2s'id to be compared with
     * @return nagative number if r1s'id is smaller
     * positive number if r2s'id is bigger
     */
    public int idOnlyCompare(Restaurant r1, Restaurant r2) {
        return idCompare(r1, r2);
    }

    /**
     * @param id identifier to be compared (id or keyword)
     * @param r  restaurant to be compared with
     * @return nagative number if o1s'id is smaller
     * positive number if o1s'id is bigger
     */
    public int idOnlyCompare(Long id, Restaurant r) {
        return id.compareTo(r.getID());
    }
}
