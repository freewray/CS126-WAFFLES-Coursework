package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.Place;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// import java.util.Date;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class RestaurantStore implements IRestaurantStore {

    private MyArrayList<Restaurant> restaurantArray;
    private DataChecker dataChecker;
    private MyArrayList<Long> blackListedRestaurantID;

    public RestaurantStore() {
        // Initialise variables here
        restaurantArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blackListedRestaurantID = new MyArrayList<>();
    }

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

    public boolean addRestaurant(Restaurant restaurant) {
        Long id = dataChecker.extractTrueID(restaurant.getRepeatedID());
        restaurant.setID(id);
        // blackListedRestaurantID.contains(restaurant.getID()) ||
        for (int i = 0; i < blackListedRestaurantID.size(); i++) {
            if (blackListedRestaurantID.get(i).equals(restaurant.getID()))
                return false;
        }
        if (!dataChecker.isValid(restaurant)) {
            return false;
        }
        if (this.getRestaurant(restaurant.getID()) != null) {
            blackListedRestaurantID.add(restaurant.getID());
            restaurantArray.remove(this.getRestaurant(restaurant.getID()));
            return false;
        }
        restaurantArray.add(restaurant);
        return true;
    }

    public boolean addRestaurant(Restaurant[] restaurants) {
        boolean res = true;
        for (Restaurant newRestaurant : restaurants) {
            if (!this.addRestaurant(newRestaurant)) {
                res = this.addRestaurant(newRestaurant);
            }
        }
        return res;
    }

    public Restaurant getRestaurant(Long id) {
        for (int i = 0; i < restaurantArray.size(); i++) {
            if (restaurantArray.get(i).getID().equals(id))
                return restaurantArray.get(i);
        }
        return null;
    }

    public Restaurant[] getRestaurants() {
        Restaurant[] restaurants = new Restaurant[restaurantArray.size()];
        for (int i = 0; i < restaurantArray.size(); i++) {
            restaurants[i] = restaurantArray.get(i);
        }
        this.restaurantArrayQuickSortByID(restaurants);
        return restaurants;
    }

    private void restaurantArrayQuickSortByID(Restaurant[] restaurants) {
        this.restaurantArrayQuickSort(restaurants, "id", 0, restaurants.length - 1);
    }

    private void restaurantArrayQuickSortByName(Restaurant[] restaurants) {
        this.restaurantArrayQuickSort(restaurants, "name", 0, restaurants.length - 1);
    }

    private void restaurantArrayQuickSortByDateEstablished(Restaurant[] restaurants) {
        this.restaurantArrayQuickSort(restaurants, "dateEstablished", 0, restaurants.length - 1);
    }

    private void restaurantArrayQuickSortByWarwickStars(Restaurant[] restaurants) {
        this.restaurantArrayQuickSort(restaurants, "warwickStar", 0, restaurants.length - 1);
    }

    private void restaurantArrayQuickSortByCustomerRating(Restaurant[] restaurants) {
        this.restaurantArrayQuickSort(restaurants, "rating", 0, restaurants.length - 1);
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

    /**
     * sorted in ascending order of distance If they have the same Distance, then it
     * is sorted in ascending order of their ID.
     */
    private int distanceCompare(RestaurantDistance r1, RestaurantDistance r2) {
        if (r1.getDistance() == r2.getDistance())
            return r1.getRestaurant().getID().compareTo(r2.getRestaurant().getID());
        else
            return r1.getDistance() < r2.getDistance() ? -1 : 1;
    }

    private void restaurantArrayQuickSort(Restaurant[] restaurants, String sortBy, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            Restaurant pivot = restaurants[end];

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                int c = 0;
                if (sortBy == "id")
                    c = idCompare(restaurants[j], pivot);
                else if (sortBy == "name")
                    c = nameCompare(restaurants[j], pivot);
                else if (sortBy == "dateEstablished")
                    c = dateCompare(restaurants[j], pivot);
                else if (sortBy == "warwickStar")
                    c = warwickStarCompare(restaurants[j], pivot);
                else if (sortBy == "rating")
                    c = ratingCompare(restaurants[j], pivot);

                if (c < 0) {
                    i++;
                    Restaurant tmp = restaurants[i];
                    restaurants[i] = restaurants[j];
                    restaurants[j] = tmp;
                }
            }

            Restaurant tmp = restaurants[i + 1];
            restaurants[i + 1] = restaurants[end];
            restaurants[end] = tmp;

            partitionIndex = i + 1;

            restaurantArrayQuickSort(restaurants, sortBy, begin, partitionIndex - 1);
            restaurantArrayQuickSort(restaurants, sortBy, partitionIndex + 1, end);
        }
    }

    private void restaurantSortByDistance(RestaurantDistance[] restaurantsDistance, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            RestaurantDistance pivot = restaurantsDistance[end];

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (distanceCompare(restaurantsDistance[j], pivot) < 0) {

                    i++;

                    RestaurantDistance tmp = restaurantsDistance[i];
                    restaurantsDistance[i] = restaurantsDistance[j];
                    restaurantsDistance[j] = tmp;
                }
            }
            RestaurantDistance tmp = restaurantsDistance[i + 1];
            restaurantsDistance[i + 1] = restaurantsDistance[end];
            restaurantsDistance[end] = tmp;

            partitionIndex = i + 1;

            restaurantSortByDistance(restaurantsDistance, begin, partitionIndex - 1);
            restaurantSortByDistance(restaurantsDistance, partitionIndex + 1, end);
        }
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        this.restaurantArrayQuickSortByID(restaurants);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByName() {
        Restaurant[] restaurants = this.getRestaurants();
        this.restaurantArrayQuickSortByName(restaurants);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        Restaurant[] restaurants = this.getRestaurants();
        this.restaurantArrayQuickSortByDateEstablished(restaurants);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        this.restaurantArrayQuickSortByDateEstablished(restaurants);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByWarwickStars() {
        Restaurant[] restaurants = this.getRestaurants();
        this.restaurantArrayQuickSortByWarwickStars(restaurants);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        this.restaurantArrayQuickSortByCustomerRating(restaurants);
        return restaurants;
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        RestaurantDistance[] restaurantDistances = new RestaurantDistance[restaurantArray.size()];
        for (int i = 0; i < restaurantArray.size(); i++) {
            float distance = HaversineDistanceCalculator.inKilometres(latitude, latitude,
                    restaurantArray.get(i).getLatitude(), restaurantArray.get(i).getLongitude());
            restaurantDistances[i] = new RestaurantDistance(restaurantArray.get(i), distance);
        }
        restaurantSortByDistance(restaurantDistances, 0, restaurantDistances.length - 1);
        return restaurantDistances;
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude,
            float longitude) {
        RestaurantDistance[] restaurantDistances = new RestaurantDistance[restaurants.length];
        for (int i = 0; i < restaurants.length; i++) {
            float distance = HaversineDistanceCalculator.inKilometres(latitude, latitude, restaurants[i].getLatitude(),
                    restaurants[i].getLongitude());
            restaurantDistances[i] = new RestaurantDistance(restaurants[i], distance);
        }
        restaurantSortByDistance(restaurantDistances, 0, restaurantDistances.length - 1);
        return restaurantDistances;
    }

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
        MyArrayList<Restaurant> resList = new MyArrayList<>();
        for (int i = 0; i < restaurantArray.size(); i++) {
            ConvertToPlace convertToPlace = new ConvertToPlace();
            Place place = convertToPlace.convert(restaurantArray.get(i).getLatitude(),
                    restaurantArray.get(i).getLongitude());
            if (restaurantArray.get(i).getName().toLowerCase().contains(searchTermConvertedFaster.toLowerCase())
                    || restaurantArray.get(i).getCuisine().name().toLowerCase()
                            .contains(searchTermConvertedFaster.toLowerCase())
                    || place.getName().toLowerCase().contains(searchTermConvertedFaster.toLowerCase())) {

                resList.add(restaurantArray.get(i));
            }
        }
        Restaurant[] res = new Restaurant[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }
        this.restaurantArrayQuickSortByName(res);
        return res;
    }
}
