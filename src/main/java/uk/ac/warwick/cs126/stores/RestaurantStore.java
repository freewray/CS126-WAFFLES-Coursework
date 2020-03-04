package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.*;
import uk.ac.warwick.cs126.structures.AVLTreeCom;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.RestaurantAVLTree;
import uk.ac.warwick.cs126.structures.RestaurantDistanceAVLTree;
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
    private AVLTreeCom<Long> blackListedRestaurantID;
    private RestaurantAVLTree restaurantTree;

    public RestaurantStore() {
        // Initialise variables here
        dataChecker = new DataChecker();
        blackListedRestaurantID = new AVLTreeCom<>();
        restaurantTree = new RestaurantAVLTree();
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
        return restaurantTree.searchByID(id);
    }

    public Restaurant[] getRestaurants() {
        MyArrayList<Restaurant> tmp = restaurantTree.allNodes();
        Restaurant[] restaurants = new Restaurant[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            restaurants[i] = tmp.get(i);
        }
        return restaurants;
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        RestaurantAVLTree tree = new RestaurantAVLTree();
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
        RestaurantAVLTree tree = new RestaurantAVLTree(sortBy);
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

    public Restaurant[] getRestaurantsByName() {
        return this.getSortedRestaurant("name");
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        return this.getSortedRestaurant("date");
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        // create new tree with the sorting method
        RestaurantAVLTree tree = new RestaurantAVLTree("date");
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

    public Restaurant[] getRestaurantsByWarwickStars() {
        return this.getSortedRestaurant("warwickStar");
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        return this.getSortedRestaurant("rating");
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        // create a new tree that can sort Restaurant Distance
        RestaurantDistanceAVLTree tree = new RestaurantDistanceAVLTree();
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

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        // create a new tree that can sort Restaurant Distance
        RestaurantDistanceAVLTree tree = new RestaurantDistanceAVLTree();
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
        RestaurantAVLTree tree = new RestaurantAVLTree("name");
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
