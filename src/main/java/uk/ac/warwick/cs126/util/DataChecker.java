package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;
import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Review;
import uk.ac.warwick.cs126.structures.MyArrayList;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {
        MyArrayList<Long> ids = new MyArrayList<>();
        for (String id : repeatedID){
            Long longID = Long.parseLong(id);
            if (ids.contains(longID))
                return longID;
            else
                ids.add(longID);
        }

        return null;
    }

    public boolean isValid(Long inputID) {
        String stringID = String.valueOf(inputID);
        // 1. must be 16 digits
        int[] cnt = new int[10];
        if (stringID.length() < 16 || inputID == null)
            return false;
        // 2. must all be numbers
        for (int i = 0; i < stringID.length(); i++) {
            if (!Character.isDigit(stringID.charAt(i)))
                return false;
            cnt[stringID.charAt(i) - '0']++;
        }
        // 3. no number appears more than 3 times
        for (int i : cnt)
            if (i > 3) return false;

        return true;
    }

    public boolean isValid(Customer customer) {
        if (customer == null) return false;
        if (customer.getID() == null || !isValid(customer.getID())) return false;
        if (customer.getFirstName() == null) return false;
        if (customer.getLastName() == null) return false;
        return customer.getDateJoined() != null;
    }

    public boolean isValid(Restaurant restaurant) {
        if (restaurant == null) return false;
        if (restaurant.getRepeatedID() == null) return false;
        if (restaurant.getID() == null || !isValid(restaurant.getID())) return false;
        if (restaurant.getName() == null) return false;
        if (restaurant.getOwnerFirstName() == null) return false;
        if (restaurant.getOwnerLastName() == null) return false;
        if (restaurant.getCuisine() == null) return false;
        if (restaurant.getEstablishmentType() == null) return false;
        if (restaurant.getPriceRange() == null) return false;
        if (restaurant.getDateEstablished() == null) return false;
        if (restaurant.getLastInspectedDate() == null) return false;
        if (restaurant.getLastInspectedDate().compareTo(restaurant.getDateEstablished()) <= 0)
            return false;
        if (restaurant.getFoodInspectionRating() > 5 || restaurant.getFoodInspectionRating() < 0)
            return false;
        if (restaurant.getWarwickStars() > 3 || restaurant.getWarwickStars() < 0)
            return false;
        if (restaurant.getCustomerRating() != 0.0f)
            return !(restaurant.getCustomerRating() > 5.0f) && !(restaurant.getCustomerRating() < 1.0f);
        
        return true;
    }

    public boolean isValid(Favourite favourite) {
        if (favourite == null) return false;
        if (favourite.getID() == null || !isValid(favourite.getID())) return false;
        if (!isValid(favourite.getRestaurantID())) return false;
        if (!isValid(favourite.getCustomerID())) return false;
        return favourite.getDateFavourited() != null;
    }

    public boolean isValid(Review review) {
        if (review == null) return false;
        if (!isValid(review.getID()) || !isValid(review.getCustomerID()) || !isValid(review.getRestaurantID()))
            return false;
        if (review.getReview() == null) return false;
        return review.getDateReviewed() != null;
    }
}