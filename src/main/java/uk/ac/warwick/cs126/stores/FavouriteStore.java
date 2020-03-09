package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.structures.AVLTreeID;
import uk.ac.warwick.cs126.structures.AVLTreeIDCounter;
import uk.ac.warwick.cs126.structures.HashMap;
import uk.ac.warwick.cs126.structures.IDCounter;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.util.DataChecker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FavouriteStore implements IFavouriteStore {

    private final MyArrayList<Favourite> favouriteArray;
    private final AVLTreeID blackListedFavouriteID;
    private final DataChecker dataChecker;

    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        blackListedFavouriteID = new AVLTreeID();
        dataChecker = new DataChecker();
    }

    /**
     * Loads data from a csv file containing the Favourite data into a Favourite array, parsing the attributes where required.
     *
     * @param resource The source csv file to be loaded.
     * @return A Favourite array with all Favourites contained within the data file, regardless of the validity of the ID.
     */
    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(Long.parseLong(data[0]), Long.parseLong(data[1]),
                            Long.parseLong(data[2]), formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    /**
     * Add a new Favourite to the store. The method should return true if the Favourite is successfully added to the data store.
     * The Favourite should not be added if a Favourite with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Favourite should be removed and the ID blacklisted from further use.
     * If a Favourite has a unique ID but there already exists a Favourite with the same Customer ID and Restaurant ID, you replace it with the oldest of the pair.
     * The ID of the Favourite that was subsequently replaced is now blacklisted, and should not exist in the store.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     *
     * @param favourite The Favourite object to add to the data store.
     * @return True if the Favourite was successfully added, false otherwise.
     */
    public boolean addFavourite(Favourite favourite) {
        if (!dataChecker.isValid(favourite) || blackListedFavouriteID.search(favourite.getID()) != null)
            return false;
        if (getFavourite(favourite.getID()) != null) {
            favouriteArray.remove(getFavourite(favourite.getID()));
            blackListedFavouriteID.insert(favourite.getID());
            return false;
        } else if (getFavouritesByCustomerID(favourite.getCustomerID()) != null
                || getFavouritesByRestaurantID(favourite.getRestaurantID()) != null) {
            // replace only if everythings valid but the stored one is newer
            for (int i = 0; i < favouriteArray.size(); i++) {
                if (favouriteArray.get(i).getRestaurantID().equals(favourite.getRestaurantID())
                        && favouriteArray.get(i).getCustomerID().equals(favourite.getCustomerID())
                        && favouriteArray.get(i).getDateFavourited().after(favourite.getDateFavourited())) {
                    blackListedFavouriteID.insert(favouriteArray.get(i).getID());
                    favouriteArray.remove(favouriteArray.get(i));
                    favouriteArray.add(favourite);
                    return true;
                }
            }
        }
        favouriteArray.add(favourite);
        return true;
    }

    /**
     * Add new Favourites in the input array to the store. The method should return true if the Favourites are all successfully added to the data store.
     * Reference the {@link #addFavourite(Favourite) addFavourite} method for details on ID handling and existing Customer/Restaurant Favourites.
     *
     * @param favourites An array of Favourite objects to add to the data store.
     * @return True if all of the Favourites were successfully added, false otherwise.
     */
    public boolean addFavourite(Favourite[] favourites) {
        boolean res = true;
        for (Favourite newFave : favourites) {
            if (!this.addFavourite(newFave))
                res = this.addFavourite(newFave);
        }
        return res;
    }

    /**
     * Returns a single Favourite, the Favourite with the given ID, or null if not found.
     *
     * @param id The ID of the Favourite to be retrieved.
     * @return The Favourite with the given ID, or null if not found.
     */
    public Favourite getFavourite(Long id) {
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getID().equals(id))
                return favouriteArray.get(i);
        }
        return null;
    }

    private int idCompare(Favourite f1, Favourite f2) {
        return f1.getID().compareTo(f2.getID());
    }

    private int dateCompare(Favourite f1, Favourite f2) {
        int dateCompare = f2.getDateFavourited().compareTo(f1.getDateFavourited());
        if (dateCompare == 0)
            return idCompare(f1, f2);
        else
            return dateCompare;
    }

    public void favouriteQuickSort(Favourite[] array, String sortBy, int begin, int end) {
        if (begin < end) {
            int partitionIndex;
            Favourite pivot = array[end];
            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                int c = 0;

                if (sortBy.equalsIgnoreCase("date"))
                    c = dateCompare(array[j], pivot);
                else if (sortBy.equalsIgnoreCase("id"))
                    c = idCompare(array[j], pivot);

                if (c < 0) {
                    i++;

                    Favourite tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                }
            }

            Favourite tmp = array[i + 1];
            array[i + 1] = array[end];
            array[end] = tmp;

            partitionIndex = i + 1;

            favouriteQuickSort(array, sortBy, begin, partitionIndex - 1);
            favouriteQuickSort(array, sortBy, partitionIndex + 1, end);
        }
    }

    /**
     * Returns an array of all Favourites, sorted in ascending order of ID.
     * The Favourite with the lowest ID should be the first element in the array.
     *
     * @return A sorted array of Favourite objects, with lowest ID first.
     */
    public Favourite[] getFavourites() {
        Favourite[] res = new Favourite[favouriteArray.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = favouriteArray.get(i);
        }
        favouriteQuickSort(res, "id", 0, res.length - 1);
        return res;
    }

    /**
     * Returns an array of all Favourites by the Customer with the given ID.
     * The array is sorted by date favourited from newest to oldest, with ascending order of ID for matching dates.
     * The newest Favourite should be the first element in the array, with the lowest ID should the date favourited be equal.
     *
     * @param id The ID of the Customer who's Favourites are to be retrieved.
     * @return A sorted array of Favourite objects, with the newest Favourite first.
     */
    public Favourite[] getFavouritesByCustomerID(Long id) {
        if (!dataChecker.isValid(id))
            return new Favourite[0];
        MyArrayList<Favourite> resList = new MyArrayList<>();
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getCustomerID().equals(id))
                resList.add(favouriteArray.get(i));
        }
        Favourite[] res = new Favourite[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }

        favouriteQuickSort(res, "date", 0, res.length - 1);
        return res;
    }

    /**
     * Returns an array of all Favourites for the Restaurant with the given ID.
     * The array should be sorted using the criteria defined for the {@link #getFavouritesByCustomerID(Long) getFavouritesByCustomerID} method.
     *
     * @param id The ID of the Restaurant who's Favourites are to be retrieved.
     * @return A sorted array of Favourite objects, with the newest Favourite first.
     */
    public Favourite[] getFavouritesByRestaurantID(Long id) {
        if (!dataChecker.isValid(id))
            return new Favourite[0];
        MyArrayList<Favourite> resList = new MyArrayList<>();
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getRestaurantID().equals(id))
                resList.add(favouriteArray.get(i));
        }
        Favourite[] res = new Favourite[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }

        favouriteQuickSort(res, "date", 0, res.length - 1);
        return res;
    }

    /**
     * Return an array of IDs of all the Restaurants that have been favourited by both Customer with ID customer1ID and Customer with ID customer2ID.
     * The date favourited is taken as the latest of the favourited date of either Customer.
     * The array is sorted by date favourited from newest to oldest, with ascending order of Restaurant ID for matching dates.
     * The newest Favourite should be the first element in the array, with the lowest ID should the date favourited be equal.
     *
     * @param customer1ID The ID of the first Customer.
     * @param customer2ID The ID of the second Customer.
     * @return A sorted array of Restaurant IDs, with the newest Favourite first.
     */
    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!dataChecker.isValid(customer1ID) || !dataChecker.isValid(customer2ID))
            return new Long[0];
        Favourite[] customer1Favs = this.getFavouritesByCustomerID(customer1ID);
        Favourite[] customer2Favs = this.getFavouritesByCustomerID(customer2ID);
        MyArrayList<Favourite> common = new MyArrayList<>();
        HashMap<Long, Favourite> tmp = new HashMap<>();
        for (Favourite f : customer2Favs) {
            tmp.add(f.getRestaurantID(), f);
        }
        for (Favourite f : customer1Favs) {
            if (tmp.find(f.getRestaurantID()) != null)
                common.add(f.getDateFavourited().after(tmp.find(f.getRestaurantID()).getDateFavourited()) ? 
                f : tmp.find(f.getRestaurantID()));
        }
        Long[] res = new Long[common.size()];
        for (int i = 0; i < common.size(); i++) {
            res[i] = common.get(i).getRestaurantID();
        }
        return res;
    }

    /**
     * Return an array of IDs of all the Restaurants that have been favourited by Customer with ID customer1ID but not Customer with ID customer2ID.
     * The array should be sorted using the criteria defined for the {@link #getCommonFavouriteRestaurants(Long, Long) getCommonFavouriteRestaurants} method.
     *
     * @param customer1ID The ID of the first Customer.
     * @param customer2ID The ID of the second Customer.
     * @return A sorted array of Restaurant IDs, with the newest Favourite first.
     */
    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!dataChecker.isValid(customer1ID) || !dataChecker.isValid(customer2ID))
            return new Long[0];
        Favourite[] customer1Favs = this.getFavouritesByCustomerID(customer1ID);
        Favourite[] customer2Favs = this.getFavouritesByCustomerID(customer2ID);
        MyArrayList<Favourite> missing = new MyArrayList<>();
        HashMap<Long, Favourite> tmp = new HashMap<>();
        for (Favourite f : customer2Favs) {
            tmp.add(f.getRestaurantID(), f);
        }
        for (Favourite f : customer1Favs) {
            if (tmp.find(f.getRestaurantID()) == null)
                missing.add(f);
        }
        Long[] res = new Long[missing.size()];
        for (int i = 0; i < missing.size(); i++) {
            res[i] = missing.get(i).getRestaurantID();
        }
        return res;
    }

    /**
     * Return an array of IDs of all the Restaurants that have either:
     * <ul>
     *     <li>been favourited by Customer with ID customer1ID but not Customer with ID customer2ID</li>
     *     <li>been favourited by Customer with ID customer2ID but not Customer with ID customer1ID</li>
     * </ul>
     * The array should be sorted using the criteria defined for the {@link #getCommonFavouriteRestaurants(Long, Long) getCommonFavouriteRestaurants} method.
     *
     * @param customer1ID The ID of the first Customer.
     * @param customer2ID The ID of the second Customer.
     * @return A sorted array of Restaurant IDs, with the newest Favourite first.
     */
    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!dataChecker.isValid(customer1ID) || !dataChecker.isValid(customer2ID))
            return new Long[0];
        Favourite[] customer1Favs = this.getFavouritesByCustomerID(customer1ID);
        Favourite[] customer2Favs = this.getFavouritesByCustomerID(customer2ID);
        MyArrayList<Favourite> missing = new MyArrayList<>();
        HashMap<Long, Favourite> tmp = new HashMap<>();
        HashMap<Long, Favourite> tmp2 = new HashMap<>();
        for (Favourite f : customer2Favs) {
            tmp.add(f.getRestaurantID(), f);
        }
        for (Favourite f : customer1Favs) {
            if (tmp.find(f.getRestaurantID()) == null)
                missing.add(f);
        }

        for (Favourite f : customer2Favs) {
            tmp2.add(f.getRestaurantID(), f);
        }
        for (Favourite f : customer1Favs) {
            if (tmp2.find(f.getRestaurantID()) == null)
                missing.add(f);
        }

        Long[] res = new Long[missing.size()];
        for (int i = 0; i < missing.size(); i++) {
            res[i] = missing.get(i).getRestaurantID();
        }
        return res;
    }

    /**
     * Returns an array of 20 Customer IDs that have favourited the most Restaurants.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Favourite count, then by date of the oldest Favourite, and finally by ascending order of Customer ID for matching counts.
     *
     * @return A sorted array of 20 Customer IDs, with the Customer with the highest Favourite count first.
     */
    public Long[] getTopCustomersByFavouriteCount() {
        // arraylist of fav in rank sorted by their length
        Long[] topCustomer = new Long[20];
        AVLTreeIDCounter tree = new AVLTreeIDCounter();
        for (int i = 0; i < favouriteArray.size(); i++) {
            Long id = favouriteArray.get(i).getCustomerID();
            if (tree.searchByID(id) != null) {
                tree.searchByID(id).addCount();
                if (tree.searchByID(id).getLatestReviewDate()
                        .before(favouriteArray.get(i).getDateFavourited())) {
                    tree.searchByID(id).setLatestReviewDate(favouriteArray.get(i).getDateFavourited());
                }
            } else {
                tree.insertByID(new IDCounter(id, favouriteArray.get(i).getDateFavourited()));
            }
        }
        MyArrayList<IDCounter> tmp = tree.toArrayList();
        AVLTreeIDCounter tree2 = new AVLTreeIDCounter();
        for (int i = 0; i < tmp.size(); i++) {
            tree2.insert(tmp.get(i));
        }
        tmp.clear();
        tmp = tree2.toArrayList();
        for (int i = 0; i < topCustomer.length && i < tmp.size(); i++) {
            topCustomer[i] = tmp.get(i).getIdentifier();
            System.out.println("Top Customer: " + tmp.get(i).getIdentifier() + " - cnt: " + tmp.get(i).getCount());
        }

        return topCustomer;
    }

    /**
     * Returns an array of 20 Restaurant IDs that have been favourited the most.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Favourite count, then by date of the oldest Favourite, and finally by ascending order of Restaurant ID for matching counts.
     *
     * @return A sorted array of 20 Restaurant IDs, with the Restaurant with the highest Favourite count first.
     */
    public Long[] getTopRestaurantsByFavouriteCount() {
        System.out.println("getTopRestaurantsByFavouriteCount");
        Long[] topRestaurants = new Long[20];
        AVLTreeIDCounter tree = new AVLTreeIDCounter();
        for (int i = 0; i < favouriteArray.size(); i++) {
            Long id = favouriteArray.get(i).getRestaurantID();
            if (tree.searchByID(id) != null) {
                tree.searchByID(id).addCount();
                if (tree.searchByID(id).getLatestReviewDate()
                        .before(favouriteArray.get(i).getDateFavourited())) {
                    tree.searchByID(id).setLatestReviewDate(favouriteArray.get(i).getDateFavourited());
                }
            } else {
                tree.insertByID(new IDCounter(id, favouriteArray.get(i).getDateFavourited()));
            }
        }
        MyArrayList<IDCounter> tmp = tree.toArrayList();
        AVLTreeIDCounter tree2 = new AVLTreeIDCounter();
        for (int i = 0; i < tmp.size(); i++) {
            tree2.insert(tmp.get(i));
        }
        tmp.clear();
        tmp = tree2.toArrayList();
        for (int i = 0; i < topRestaurants.length && i < tmp.size(); i++) {
            topRestaurants[i] = tmp.get(i).getIdentifier();
            System.out.println("Top Restaurant: " + tmp.get(i).getIdentifier() + " - cnt: " + tmp.get(i).getCount());
        }
        return topRestaurants;
    }
}
