package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.util.DataChecker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private MyArrayList<Long> blackListedFavouriteId;
    private DataChecker dataChecker;

    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        blackListedFavouriteId = new MyArrayList<>();
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
        if (!dataChecker.isValid(favourite) || blackListedFavouriteId.contains(favourite.getID()))
            return false;
        else if (getFavourite(favourite.getID()) != null) {
            if (getFavourite(favourite.getID()) != null) {
                favouriteArray.remove(getFavourite(favourite.getID()));
                blackListedFavouriteId.add(favourite.getID());
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
        for (int i = 0; i < favourites.length; i++) {
            if (!addFavourite(favourites[i]))
                return false;
        }
        return true;
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

    public void dateQuickSort(Favourite array[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            Date pivot = array[end].getDateFavourited();

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (array[j].getDateFavourited().compareTo(pivot) > 0) {
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

            dateQuickSort(array, begin, partitionIndex - 1);
            dateQuickSort(array, partitionIndex + 1, end);
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
        dateQuickSort(res, 0, res.length - 1);
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
        dateQuickSort(res, 0, res.length - 1);
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
        dateQuickSort(favRes, 0, favRes.length - 1);
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
        return new Long[0];
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        return new Long[20];
    }
}
