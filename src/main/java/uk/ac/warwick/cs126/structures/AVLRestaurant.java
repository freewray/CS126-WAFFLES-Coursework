package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Restaurant;

public class AVLRestaurant extends AVLTreeStore<Restaurant> {

    public AVLRestaurant() {
        super();
    }

    public AVLRestaurant(String sortBy) {
        super(sortBy);
    }

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

    @Override
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

    public void remove(Restaurant key) {
        AVLTreeNode<Restaurant> tmp;

        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    private AVLTreeNode<Restaurant> search(AVLTreeNode<Restaurant> node, Restaurant key) {

        if (node == null) {
            return null; // missing from tree
        } else if (key.getID().compareTo(node.getKey().getID()) < 0) {
            return search(node.getLeft(), key);
        } else if (key.getID().compareTo(node.getKey().getID()) > 0) {
            return search(node.getRight(), key);
        } else {
            return node; // found it
        }
    }

    public AVLTreeNode<Restaurant> search(Restaurant key) {
        return search(root, key);
    }

    private Restaurant searchByID(AVLTreeNode<Restaurant> node, Long id) {

        if (node == null) {
            return null; // missing from tree
        } else if (id.compareTo(node.getKey().getID()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (id.compareTo(node.getKey().getID()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node.getKey(); // found it
        }
    }

    public Restaurant searchByID(Long id) {
        return searchByID(root, id);
    }
}
