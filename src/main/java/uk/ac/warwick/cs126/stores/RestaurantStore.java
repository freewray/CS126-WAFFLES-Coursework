package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.*;
import uk.ac.warwick.cs126.structures.AVLRestaurant;
import uk.ac.warwick.cs126.structures.AVLRestaurantDistance;
import uk.ac.warwick.cs126.structures.AVLTreeCom;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.StringFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// import java.util.Date;

public class RestaurantStore implements IRestaurantStore {

    private DataChecker dataChecker;
    /**
     * blackListed ID are stored in an AVL tree therefore
     * quicker search
     */
    private AVLTreeCom<Long> blackListedRestaurantID;
    /**
     * restaurants are stored in AVL tree and sorted by their ID
     * by default
     */
    private AVLRestaurant restaurantTree;

    public RestaurantStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        blackListedRestaurantID = new AVLTreeCom<>();
        restaurantTree = new AVLRestaurant();
    }

    /**
     * Loads data from a CSV file containing the Restaurant data into a Restaurant array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Restaurant array with all Restaurants contained within the data file, regardless of the validity of the ID.
     */
    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(data[0], data[1], data[2], data[3], Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]), PriceRange.valueOf(data[6]), formatter.parse(data[7]),
                            Float.parseFloat(data[8]), Float.parseFloat(data[9]), Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]), Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]), Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]), formatter.parse(data[16]), Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

    /**
     * Add a new Restaurant to the store. The method should return true if the Restaurant is successfully added to the data store.
     * The Restaurant contains a repeated ID string (3 repeats of a 16 digit ID). These need to have the true ID extracted and verified before the duplicate and invalid checks.
     * The repeated ID string is corrupt if there is no consensus (i.e. there is no majority).
     * A Restaurant with a corrupt repeated ID string should not be added to the store.
     * The Restaurant should not be added if a Restaurant with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Restaurant should be removed and the ID blacklisted from further use.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param restaurant       The Restaurant object to add to the data store.
     * @return True if the Restaurant was successfully added, false otherwise.
     */
    public boolean addRestaurant(Restaurant restaurant) {
        Long id = dataChecker.extractTrueID(restaurant.getRepeatedID());
        restaurant.setID(id);
        if (!dataChecker.isValid(restaurant) || blackListedRestaurantID.search(restaurant.getID()) != null){
            return false;
        }
        if (this.getRestaurant(restaurant.getID()) != null) {
            blackListedRestaurantID.insert(restaurant.getID());
            restaurantTree.remove(this.getRestaurant(restaurant.getID()));
            return false;
        }
        restaurantTree.insert(restaurant);
        return true;
    }

    /**
     * Add new Restaurants in the input array to the store. The method should return true if the Restaurants are all successfully added to the data store.
     * Reference the {@link #addRestaurant(Restaurant) addRestaurant} method for details on ID handling.
     * @param restaurants       An array of Restaurant objects to add to the data store.
     * @return True if all of the Restaurants were successfully added, false otherwise.
     */
    public boolean addRestaurant(Restaurant[] restaurants) {
        boolean res = true;
        for (Restaurant newRestaurant : restaurants) {
            if (!this.addRestaurant(newRestaurant)) {
                res = this.addRestaurant(newRestaurant);
            }
        }
        return res;
    }

    /**
     * Returns a single Restaurant, the Restaurant with the given ID, or null if not found.
     * @param id       The ID of the Restaurant to be retrieved.
     * @return The Restaurant with the given ID, or null if not found.
     */
    public Restaurant getRestaurant(Long id) {
        return restaurantTree.searchByID(id);
    }

    /**
     * Returns an array of all Restaurants, sorted in ascending order of ID.
     * The Restaurant with the lowest ID should be the first element in the array.
     * @return A sorted array of Restaurant objects, with lowest ID first.
     */
    public Restaurant[] getRestaurants() {
        MyArrayList<Restaurant> tmp = restaurantTree.allNodes();
        Restaurant[] restaurants = new Restaurant[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            restaurants[i] = tmp.get(i);
        }
        return restaurants;
    }

    /**
     * Returns an array of Restaurants, sorted in ascending order of ID.
     * The Restaurant with the lowest ID should be the first element in the array.
     * Similar functionality to the {@link #getRestaurants() getRestaurants} method.
     * @param restaurants       An array of Restaurant objects to be sorted.
     * @return A sorted array of Restaurant objects, with lowest ID first.
     */
    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        AVLRestaurant tree = new AVLRestaurant();
        for (Restaurant r : restaurants) {
            tree.insert(r);
        }
        MyArrayList<Restaurant> tmp = tree.allNodes();
        for (int i = 0; i < restaurants.length; i++) {
            restaurants[i] = tmp.get(i);
        }
        return restaurants;
    }

    public Restaurant[] getSortedRestaurant(String sortBy){
        // create new tree with the sorting method
        AVLRestaurant tree = new AVLRestaurant(sortBy);
        // get all nodes from previous tree
        MyArrayList<Restaurant> tmp = restaurantTree.allNodes();
        // insert nodes into new tree
        for (int i = 0; i < tmp.size(); i++) {
            tree.insert(tmp.get(i));
        }

        // get sorted data from tree
        tmp.clear();
        tmp = tree.allNodes();
        // copy data in array list onto an array
        Restaurant[] restaurants = new Restaurant[tmp.size()];
        for (int i = 0; i < restaurants.length; i++) {
            restaurants[i] = tmp.get(i);
        }
        return restaurants;
    }

    /**
     * Returns an array of all Restaurants, sorted in alphabetical order of Restaurant name.
     * If the Restaurant names are identical for multiple Restaurants, they should be further sorted in ascending order of ID.
     * The Restaurant with the Restaurant name that is nearest to 'A' alphabetically should be the first element in the array.
     * @return A sorted array of Restaurant objects, where the first element is the Restaurant with the Restaurant name that is nearest to 'A' alphabetically, followed by ID if the Restaurant names are equal.
     */
    public Restaurant[] getRestaurantsByName() {
        return this.getSortedRestaurant("name");
    }

    /**
     * Returns an array of all Restaurants, sorted in ascending order of date established (oldest first).
     * If the date established is the same, then sort by restaurant name in alphabetical order and finally in ascending order of ID.
     * The oldest Restaurant should be the first element in the array, with the Restaurant name that is nearest to 'A' alphabetically, followed by lowest ID should the date established be equal.
     * @return A sorted array of Restaurant objects, with the oldest Restaurant first.
     */
    public Restaurant[] getRestaurantsByDateEstablished() {
        return this.getSortedRestaurant("date");
    }

    /**
     * Returns an array of Restaurants, sorted in ascending order of date established (oldest first).
     * The array should be sorted using the criteria defined for the {@link #getRestaurantsByDateEstablished() getRestaurantsByDateEstablished} method.
     * @param restaurants       An array of Restaurant objects to be sorted.
     * @return A sorted array of Restaurant objects, with the oldest Restaurant first.
     */
    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        // create new tree with the sorting method
        AVLRestaurant tree = new AVLRestaurant("date");
        // get all nodes from array
        for (Restaurant r : restaurants) {
            tree.insert(r);
        }
        MyArrayList<Restaurant> tmp = tree.allNodes();
        for (int i = 0; i < restaurants.length; i++) {
            restaurants[i] = tmp.get(i);
        }
        return restaurants;
    }

    /**
     * Returns an array of all Restaurants that have at least 1 Warwick Star, sorted in descending order Stars.
     * If the number of Stars is the same, then sort by restaurant name in alphabetical order and finally in ascending order of ID.
     * The first element in the array should be the Restaurant with the highest number of Stars, and the Restaurant name that is nearest to 'A' alphabetically, followed by lowest ID should the number of Stars be equal.
     * @return A sorted array of Restaurant objects, with the Restaurant with the highest number of Stars first.
     */
    public Restaurant[] getRestaurantsByWarwickStars() {
        return this.getSortedRestaurant("warwickStar");
    }

    /**
     * Returns an array of Restaurants, sorted in descending order of rating.
     * The rating is calculated by averaging all review ratings for that Restaurant.
     * If the Restaurant rating is the same, then sort by restaurant name in alphabetical order and finally in ascending order of ID.
     * The first element in the array should be the Restaurant with the highest highest rating, and the Restaurant name that is nearest to 'A' alphabetically, followed by lowest ID should the ratings be equal.
     * @param restaurants       An array of Restaurant objects to be sorted.
     * @return A sorted array of Restaurant objects, with the Restaurant with the highest rating first.
     */
    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        return this.getSortedRestaurant("rating");
    }

    /**
     * Returns an array of RestaurantDistance objects, sorted in ascending order of distance from the input coordinates, for all Restaurants.
     * If the distance is the same, then sort by ascending order of ID.
     * The first element in the array should be the RestaurantDistance object with the smallest distance from the input coordinate, followed by lowest Restaurant ID should the distances be equal.
     * @param latitude          The latitude of the comparison location.
     * @param longitude         The longitude of the comparison location.
     * @return A sorted array of RestaurantDistance objects, with the nearest Restaurant to the input coordinates first.
     */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        // create a new tree that can sort Restaurant Distance
        AVLRestaurantDistance tree = new AVLRestaurantDistance();
        // copy nodes from previous stored tree
        MyArrayList<Restaurant> tmp = restaurantTree.allNodes();
        // create an array to hold sorted RestaurantDistances
        RestaurantDistance[] res = new RestaurantDistance[tmp.size()];
        // insert each node into the new avl tree
        for (int i = 0; i < tmp.size(); i++) {
            float distance = HaversineDistanceCalculator.inKilometres(latitude, latitude,
                    tmp.get(i).getLatitude(), tmp.get(i).getLongitude());
            tree.insert(new RestaurantDistance(tmp.get(i), distance));
        }
        MyArrayList<RestaurantDistance> tmp2 = tree.allNodes();
        for (int i = 0; i < tmp2.size(); i++) {
            res[i] = tmp2.get(i);
        }

        return res;
    }

    /**
     * Returns an array of RestaurantDistance objects, sorted in ascending order of distance from the input coordinates, for the given input Restaurants.
     * The array should be sorted using the criteria defined for the {@link #getRestaurantsByDistanceFrom(float, float) getRestaurantsByDistanceFrom} method.
     * The first element in the array should be the RestaurantDistance object with the smallest distance from the input coordinate, followed by lowest Restaurant ID should the distances be equal.
     * @param restaurants       An array of Restaurant objects to have the distance calculated.
     * @param latitude          The latitude of the comparison location.
     * @param longitude         The longitude of the comparison location.
     * @return A sorted array of RestaurantDistance objects, with the nearest Restaurant to the input coordinates first.
     */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        // create a new tree that can sort Restaurant Distance
        AVLRestaurantDistance tree = new AVLRestaurantDistance();
        // create a new array to hold answers
        RestaurantDistance[] res = new RestaurantDistance[restaurants.length];
        // insert each node into the new avl tree
        for (Restaurant r : restaurants) {
            float distance = HaversineDistanceCalculator.inKilometres(latitude, latitude,
                    r.getLatitude(), r.getLongitude());
            tree.insert(new RestaurantDistance(r, distance));
        }
        MyArrayList<RestaurantDistance> tmp = tree.allNodes();
        for (int i = 0; i < tmp.size(); i++) {
            res[i] = tmp.get(i);
        }

        return res;
    }

    /**
     * Return an array of all the Restaurants whose name, cuisine or place name contain the given query.
     * Search queries are accent-insensitive, case-insensitive and space-insensitive.
     * The array should be sorted using the criteria defined for the {@link #getRestaurantsByName() getRestaurantsByName} method.
     * @param searchTerm       The search string to find.
     * @return A array of Restaurant objects, sorted using the criteria defined for the {@link #getRestaurantsByName() getRestaurantsByName} method.
     */
    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster =
        // StringFormatter.convertAccentsFaster(searchTerm.trim().replaceAll("\\s+", "
        // "));
        // 1. trim leading and tralling white spaces
        // 2. convert accents
        String searchTermConvertedFaster = StringFormatter.convertAccentsFaster(searchTerm.replaceAll("\\s+", " "));
        // 3. change multiple whitespaces with one
        searchTermConvertedFaster = searchTermConvertedFaster.trim();
        if (searchTermConvertedFaster.length() == 0) {
            return new Restaurant[0];
        }
        MyArrayList<Restaurant> restaurants = restaurantTree.allNodes();
        MyArrayList<Restaurant> resList = new MyArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            ConvertToPlace convertToPlace = new ConvertToPlace();
            Place place = convertToPlace.convert(restaurants.get(i).getLatitude(),
                    restaurants.get(i).getLongitude());
            if (restaurants.get(i).getName().toLowerCase().contains(searchTermConvertedFaster.toLowerCase())
                    || restaurants.get(i).getCuisine().toString()
                            .contains(searchTermConvertedFaster.toLowerCase())
                    || place.getName().toLowerCase().contains(searchTermConvertedFaster.toLowerCase())) {

                resList.add(restaurants.get(i));
            }
        }

        // sort results in avl tree
        AVLRestaurant tree = new AVLRestaurant("name");
        for (int i = 0; i < resList.size(); i++) {
            tree.insert(resList.get(i));
        }
        resList.clear();
        resList = tree.allNodes();
        Restaurant[] res = new Restaurant[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }
        return res;
    }
}
