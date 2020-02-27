package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MySet;
import uk.ac.warwick.cs126.util.DataChecker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        // TODO
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
        // TODO
        boolean res = true;
        for (Favourite newFave : favourites) {
            if (!this.addFavourite(newFave))
                res = this.addFavourite(newFave);
        }
        return res;
    }

    public Favourite getFavourite(Long id) {
        // TODO
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getID().equals(id))
                return favouriteArray.get(i);
        }
        return null;
    }

    public void idQuickSort(Favourite array[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            long pivot = array[end].getID();

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (array[j].getID().compareTo(pivot) <= 0) {
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

            idQuickSort(array, begin, partitionIndex - 1);
            idQuickSort(array, partitionIndex + 1, end);
        }
    }

    public void dateQuickSort(Favourite array[], int begin, int end, String sortBy) {
        if (begin < end) {
            int partitionIndex = 0;
            Date pivot = array[end].getDateFavourited();

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                int compared = 0;
                if (sortBy == "dsc")
                    compared = array[j].getDateFavourited().compareTo(pivot);
                else
                    compared = pivot.compareTo(array[j].getDateFavourited());
                if (compared > 0) {
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

            dateQuickSort(array, begin, partitionIndex - 1, sortBy);
            dateQuickSort(array, partitionIndex + 1, end, sortBy);
        }
    }

    public Favourite[] getFavourites() {
        // TODO
        Favourite[] res = new Favourite[favouriteArray.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = favouriteArray.get(i);
        }
        idQuickSort(res, 0, res.length - 1);
        return res;
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        // TODO
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

        idQuickSort(res, 0, res.length - 1);
        dateQuickSort(res, 0, res.length - 1, "dsc");
        return res;
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // TODO
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

        idQuickSort(res, 0, res.length - 1);
        dateQuickSort(res, 0, res.length - 1, "dsc");
        return res;
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
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
        dateQuickSort(favRes, 0, favRes.length - 1, "dsc");
        Long[] res = new Long[favRes.length];
        for (int i = 0; i < favRes.length; i++) {
            res[i] = favRes[i].getRestaurantID();
        }
        return res;
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
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
        // TODO
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
        idQuickSort(notCommonArr, 0, notCommonArr.length - 1);
        dateQuickSort(notCommonArr, 0, notCommonArr.length - 1, "dsc");

        // add only ids to result
        Long[] res = new Long[notCommonArr.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = notCommonArr[i].getRestaurantID();
        }
        return res;
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        // arraylist of fav in rank sorted by their length
        Long[] topCustomer = new Long[20];

        MyArrayList<FavouriteCnt> customerFavourites = new MyArrayList<>();
        for (int i = 0, j = 0; i < favouriteArray.size(); i++) {
            for (j = 0; j < customerFavourites.size(); j++) {
                if (customerFavourites.get(j).id.compareTo(favouriteArray.get(i).getCustomerID()) == 0){
                    customerFavourites.get(j).times++;
                    if (customerFavourites.get(j).latestFavDate.compareTo(favouriteArray.get(i).getDateFavourited()) < 0)
                        customerFavourites.get(j).latestFavDate = favouriteArray.get(i).getDateFavourited();

                    break;
                }
            }

            if (j == customerFavourites.size()){
                FavouriteCnt customerFavourite = new FavouriteCnt(favouriteArray.get(i).getCustomerID(), favouriteArray.get(i).getDateFavourited());
                customerFavourites.add(customerFavourite);
            }
        }
        // sort by favourite times
        favouriteCntQuicksort(customerFavourites, 0, customerFavourites.size()-1);
        // sort by latest date (oldest to newest)
        for (int i = 0; i < topCustomer.length && i < customerFavourites.size(); i++) {
            topCustomer[i] = customerFavourites.get(i).id;
        }

        return topCustomer;
    }

    private class FavouriteCnt {

        Long id;
        Date latestFavDate;
        int times;

        public FavouriteCnt(Long id, Date latestFavDate) {
            this.id = id;
            this.latestFavDate = latestFavDate;
            this.times = 1;
        }
    }

    public void favouriteCntQuicksort(MyArrayList<FavouriteCnt> array, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            int pivotTimes = array.get(end).times;
            Date pivotDate = array.get(end).latestFavDate;

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {

                // sort times from highest to lowest
                // or if both has same times then
                // only exchange value when latest fav is older
                if (array.get(j).times > pivotTimes || (array.get(j).latestFavDate.compareTo(pivotDate) < 0 && array.get(j).times == pivotTimes)) {
                    i++;

                    FavouriteCnt tmp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, tmp);
                }
            }

            FavouriteCnt tmp = array.get(i + 1);
            array.set(i + 1, array.get(end));
            array.set(end, tmp);

            partitionIndex = i + 1;

            favouriteCntQuicksort(array, begin, partitionIndex - 1);
            favouriteCntQuicksort(array, partitionIndex + 1, end);
        }
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        Long[] topRestaurants = new Long[20];

        MyArrayList<FavouriteCnt> topRestaurantFavourites = new MyArrayList<>();
        for (int i = 0, j = 0; i < favouriteArray.size(); i++) {
            for (j = 0; j < topRestaurantFavourites.size(); j++) {
                if (topRestaurantFavourites.get(j).id.compareTo(favouriteArray.get(i).getRestaurantID()) == 0){
                    topRestaurantFavourites.get(j).times++;
                    if (topRestaurantFavourites.get(j).latestFavDate.compareTo(favouriteArray.get(i).getDateFavourited()) < 0)
                        topRestaurantFavourites.get(j).latestFavDate = favouriteArray.get(i).getDateFavourited();

                    break;
                }
            }

            if (j == topRestaurantFavourites.size()){
                FavouriteCnt customerFavourite = new FavouriteCnt(favouriteArray.get(i).getRestaurantID(), favouriteArray.get(i).getDateFavourited());
                topRestaurantFavourites.add(customerFavourite);
            }
        }
        // sort by favourite times
        favouriteCntQuicksort(topRestaurantFavourites, 0, topRestaurantFavourites.size()-1);
        // sort by latest date (oldest to newest)
        for (int i = 0; i < topRestaurants.length && i < topRestaurantFavourites.size(); i++) {
            topRestaurants[i] = topRestaurantFavourites.get(i).id;
        }

        return topRestaurants;
    }
}
