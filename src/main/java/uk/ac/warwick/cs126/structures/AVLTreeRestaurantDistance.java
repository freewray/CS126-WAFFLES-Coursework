package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.RestaurantDistance;

public class AVLTreeRestaurantDistance extends AVLTree<RestaurantDistance, Long> {

    public int idOnlyCompare(RestaurantDistance o1, RestaurantDistance o2) {
        return 0;
    }

    /**
     * id only comparison
     *
     * @param r1 restaurant 1s'id to be compared with
     * @param r2 restaurant 2s'id to be compared with
     * @return nagative number if r1s'id is smaller
     * positive number if r2s'id is bigger
     */
    public int idOnlyCompare(Long id, RestaurantDistance o) {
        return id.compareTo(o.getRestaurant().getID());
    }

    /**
     * default comparison
     * 
     * @param r1 restaurant 1 to be compared with
     * @param r2 restaurant 2 to be compared with
     * @return negative number if r1 is smaller
     * sorted in ascending order of distance If they have the same Distance, then it
     * is sorted in ascending order of their ID.
     */
    public int customCompare(RestaurantDistance r1, RestaurantDistance r2) {
        if (r1.getDistance() == r2.getDistance())
            return r1.getRestaurant().getID().compareTo(r2.getRestaurant().getID());
        else
            return r1.getDistance() < r2.getDistance() ? -1 : 1;
    }
}
