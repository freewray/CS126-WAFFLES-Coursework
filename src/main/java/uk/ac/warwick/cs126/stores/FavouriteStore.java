package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.structures.IDCounter;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyComparableArrayList;
import uk.ac.warwick.cs126.structures.MySet;
import uk.ac.warwick.cs126.util.DataChecker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private MyArrayList<Long> blackListedFavouriteID;
    private DataChecker dataChecker;

    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        blackListedFavouriteID = new MyArrayList<>();
        dataChecker = new DataChecker();
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
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

    public boolean addFavourite(Favourite favourite) {
        if (!dataChecker.isValid(favourite) || blackListedFavouriteID.contains(favourite.getID()))
            return false;
        else if (getFavourite(favourite.getID()) != null) {
            if (getFavourite(favourite.getID()) != null) {
                favouriteArray.remove(getFavourite(favourite.getID()));
                blackListedFavouriteID.add(favourite.getID());
            }
            return false;
        } else if (getFavouritesByCustomerID(favourite.getCustomerID()) != null) {
            Favourite[] tmp = getFavouritesByCustomerID(favourite.getCustomerID());
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].getRestaurantID() == favourite.getRestaurantID())
                    favouriteArray.remove(tmp[i]);
            }
            favouriteArray.add(favourite);
            return true;
        }
        return false;
    }

    public boolean addFavourite(Favourite[] favourites) {
        boolean res = true;
        for (Favourite newFave : favourites) {
            if (!this.addFavourite(newFave))
                res = this.addFavourite(newFave);
        }
        return res;
    }

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

    public void favouriteQuickSort(Favourite array[], String sortBy, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
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

    public Favourite[] getFavourites() {
        Favourite[] res = new Favourite[favouriteArray.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = favouriteArray.get(i);
        }
        favouriteQuickSort(res,"id", 0, res.length - 1);
        return res;
    }

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

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!dataChecker.isValid(customer1ID) || !dataChecker.isValid(customer2ID))
            return new Long[0];
        MyArrayList<Favourite> customer1Favs = new MyArrayList<>();
        MyArrayList<Favourite> customer2Favs = new MyArrayList<>();
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getCustomerID().equals(customer1ID))
                customer1Favs.add(favouriteArray.get(i));
            if (favouriteArray.get(i).getCustomerID().equals(customer2ID))
                customer2Favs.add(favouriteArray.get(i));
        }
        if (customer1Favs.size() == 0 || customer2Favs.size() == 0)
            return new Long[0];

        MyArrayList<Favourite> resList = new MyArrayList<>();
        for (int i = 0; i < customer1Favs.size(); i++) {
            for (int j = 0; j < customer2Favs.size(); j++) {
                if (customer2Favs.get(j).getRestaurantID().equals(customer1Favs.get(i).getRestaurantID()))
                    resList.add(customer1Favs.get(i).getDateFavourited().compareTo(customer2Favs.get(j).getDateFavourited()) >= 0 ? customer1Favs.get(i) : customer2Favs.get(i));
            }
        }
        Favourite[] favRes = new Favourite[resList.size()];
        for (int i = 0; i < favRes.length; i++) {
            favRes[i] = resList.get(i);
        }
        favouriteQuickSort(favRes, "date", 0, favRes.length - 1);
        Long[] res = new Long[favRes.length];
        for (int i = 0; i < favRes.length; i++) {
            res[i] = favRes[i].getRestaurantID();
        }
        return res;
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!dataChecker.isValid(customer1ID) || !dataChecker.isValid(customer2ID))
            return new Long[0];
        MyArrayList<Favourite> customer1Favs = new MyArrayList<>();
        MyArrayList<Favourite> customer2Favs = new MyArrayList<>();
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getCustomerID().equals(customer1ID))
                customer1Favs.add(favouriteArray.get(i));
            if (favouriteArray.get(i).getCustomerID().equals(customer2ID))
                customer2Favs.add(favouriteArray.get(i));
        }
        if (customer1Favs.size() == 0)
            return new Long[0];
        else if (customer2Favs.size() == 0) {
            Long[] res = new Long[customer1Favs.size()];
            for (int i = 0; i < customer1Favs.size(); i++) {
                res[i] = customer1Favs.get(i).getRestaurantID();
            }
            return res;
        }
        for (int i = 0; i < customer1Favs.size(); i++) {
            for (int j = 0; j < customer2Favs.size(); j++) {
                if (customer2Favs.get(j).getRestaurantID().equals(customer1Favs.get(i).getRestaurantID()))
                    customer1Favs.remove(customer1Favs.get(i));
            }
        }
        Long[] res = new Long[customer1Favs.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = customer1Favs.get(i).getRestaurantID();
        }
        return res;
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!dataChecker.isValid(customer1ID) || !dataChecker.isValid(customer2ID))
            return new Long[0];
        MySet<Favourite> notCommon = new MySet<>();
        // add all restaurants to the set
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getCustomerID().equals(customer1ID))
                notCommon.add(favouriteArray.get(i));
            if (favouriteArray.get(i).getCustomerID().equals(customer2ID))
                notCommon.add(favouriteArray.get(i));
        }

        // remove commons
        Long[] common = this.getCommonFavouriteRestaurants(customer1ID, customer2ID);
        for (int i = 0; i < notCommon.size(); i++) {
            for (int j = 0; j < common.length; j++) {
                if (notCommon.get(i).getRestaurantID().equals(common[j]))
                    notCommon.remove(notCommon.get(i));
            }
        }

        // sort
        Favourite[] notCommonArr = new Favourite[notCommon.size()];
        notCommonArr = notCommon.toArray(notCommonArr);
        for (int i = 0; i < notCommon.size(); i++) {
            notCommonArr[i] = notCommon.get(i);
        }
        favouriteQuickSort(notCommonArr, "date", 0, notCommonArr.length - 1);

        // add only ids to result
        Long[] res = new Long[notCommonArr.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = notCommonArr[i].getRestaurantID();
        }
        return res;
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // arraylist of fav in rank sorted by their length
        Long[] topCustomer = new Long[20];

        MyComparableArrayList<IDCounter> customerFavourites = new MyComparableArrayList<>();
        for (int i = 0, j = 0; i < favouriteArray.size(); i++) {
            for (j = 0; j < customerFavourites.size(); j++) {
                if (customerFavourites.get(j).getIdentifier().equals(favouriteArray.get(i).getCustomerID())){
                    customerFavourites.get(j).addCount();
                    if (customerFavourites.get(j).getLatestReviewDate().before(favouriteArray.get(i).getDateFavourited()))
                        customerFavourites.get(j).setLatestReviewDate(favouriteArray.get(i).getDateFavourited());

                    break;
                }
            }

            if (j == customerFavourites.size()){
                IDCounter customerFavourite = new IDCounter(favouriteArray.get(i).getCustomerID(), favouriteArray.get(i).getDateFavourited());
                customerFavourites.add(customerFavourite);
            }
        }
        // sort by favourite times
        customerFavourites.quicksort(0, customerFavourites.size() - 1);
        // sort by latest date (oldest to newest)
        for (int i = 0; i < topCustomer.length && i < customerFavourites.size(); i++) {
            topCustomer[i] = customerFavourites.get(i).getIdentifier();
            System.out.println("[cnt = " + customerFavourites.get(i).getCount() + "] fave: " + customerFavourites.get(i).getIdentifier());
        }

        return topCustomer;
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        Long[] topRestaurants = new Long[20];

        MyComparableArrayList<IDCounter> topRestaurantFavourites = new MyComparableArrayList<>();
        for (int i = 0, j = 0; i < favouriteArray.size(); i++) {
            for (j = 0; j < topRestaurantFavourites.size(); j++) {
                if (topRestaurantFavourites.get(j).getIdentifier().compareTo(favouriteArray.get(i).getRestaurantID()) == 0){
                    topRestaurantFavourites.get(j).addCount();
                    if (topRestaurantFavourites.get(j).getLatestReviewDate().compareTo(favouriteArray.get(i).getDateFavourited()) < 0)
                        topRestaurantFavourites.get(j).setLatestReviewDate(favouriteArray.get(i).getDateFavourited());

                    break;
                }
            }

            if (j == topRestaurantFavourites.size()){
                IDCounter customerFavourite = new IDCounter(favouriteArray.get(i).getRestaurantID(), favouriteArray.get(i).getDateFavourited());
                topRestaurantFavourites.add(customerFavourite);
            }
        }
        // sort by favourite times
        topRestaurantFavourites.quicksort(0, topRestaurantFavourites.size() - 1);
        // sort by latest date (oldest to newest)
        for (int i = 0; i < topRestaurants.length && i < topRestaurantFavourites.size(); i++) {
            topRestaurants[i] = topRestaurantFavourites.get(i).getIdentifier();
            System.out.println("[cnt = " + topRestaurantFavourites.get(i).getCount() + "] fave: " + topRestaurantFavourites.get(i).getIdentifier());
        }

        return topRestaurants;
    }
}
