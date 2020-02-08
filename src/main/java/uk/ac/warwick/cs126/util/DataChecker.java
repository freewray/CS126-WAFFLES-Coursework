package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;
import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Review;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {
        // TODO
        return null;
    }

    public boolean isValid(Long inputID) {
        // TODO
        String stringID = String.valueOf(inputID);
        // 1. must be 16 digits
        if (stringID.length() < 16 || inputID == null)
            return false;
        // 2. must all be numbers
        for (int i = 0; i < stringID.length(); i++) {
            if (!Character.isDigit(stringID.charAt(i)))
                return false;
            int freq = 0;
            for (int j = 0; j < stringID.length(); j++) {
                if (stringID.charAt(j) == stringID.charAt(i))
                    freq++;
            }
            // 3. no number appears more than 3 times
            if (freq > 3)
                return false;
        }

        return true;
    }

    public boolean isValid(Customer customer) {
        // TODO
        return customer.getID() != null && isValid(customer.getID()) && customer.getFirstName() != null && customer.getLastName() != null && customer.getDateJoined() != null ;
    }

    public boolean isValid(Restaurant restaurant) {
        // TODO
        return false;
    }

    public boolean isValid(Favourite favourite) {
        // TODO
        return favourite != null && isValid(favourite.getID()) && isValid(favourite.getCustomerID()) && isValid(favourite.getRestaurantID()) && favourite.getDateFavourited() != null;
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}