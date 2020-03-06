package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Review;
import uk.ac.warwick.cs126.structures.*;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReviewStore implements IReviewStore {

    private MyArrayList<Review> reviewArray;
    private DataChecker dataChecker;
    private AVLTreeCom<Long> blackListedReviewID;

    public ReviewStore() {
        // Initialise variables here
        reviewArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blackListedReviewID = new AVLTreeCom<>();
    }

    /**
     * Loads data from a TSV file containing the Review data into a Review array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Review array with all Reviews contained within the data file, regardless of the validity of the ID.
     */
    public Review[] loadReviewDataToArray(InputStream resource) {
        Review[] reviewArray = new Review[0];

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

            Review[] loadedReviews = new Review[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int reviewCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Review review = new Review(Long.parseLong(data[0]), Long.parseLong(data[1]),
                            Long.parseLong(data[2]), formatter.parse(data[3]), data[4], Integer.parseInt(data[5]));
                    loadedReviews[reviewCount++] = review;
                }
            }
            tsvReader.close();

            reviewArray = loadedReviews;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return reviewArray;
    }

    /**
     * Add a new Review to the store. The method should return true if the Review is successfully added to the data store.
     * The Review should not be added if a Review with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Review should be removed and the ID blacklisted from further use.
     * If a Review has a unique ID but there already exists a Review with the same Customer ID and Restaurant ID, you replace it with the newest of the pair.
     * The ID of the Review that was subsequently replaced is now blacklisted, and should not exist in the store.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param review       The Review object to add to the data store.
     * @return True if the Review was successfully added, false otherwise.
     */
    public boolean addReview(Review review) {
        if (blackListedReviewID.search(review.getID()) != null || !dataChecker.isValid(review)) {
            return false;
        }

        if (this.getReview(review.getID()) != null) {
            blackListedReviewID.insert(review.getID());
            reviewArray.remove(this.getReview(review.getID()));
            return false;
        }
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(review.getCustomerID())
                    && reviewArray.get(i).getRestaurantID().equals(review.getRestaurantID())
                    && reviewArray.get(i).getDateReviewed().before(review.getDateReviewed())) {

                blackListedReviewID.insert(reviewArray.get(i).getID());
                reviewArray.remove(reviewArray.get(i));
                reviewArray.add(review);
                return true;
            }
        }
        reviewArray.add(review);
        return true;
    }

    /**
     * Add new Reviews in the input array to the store. The method should return true if the Reviews are all successfully added to the data store.
     * Reference the {@link #addReview(Review) addReview} method for details on ID handling and existing Customer/Restaurant Reviews.
     * @param reviews       An array of Review objects to add to the data store.
     * @return True if all of the Reviews were successfully added, false otherwise.
     */
    public boolean addReview(Review[] reviews) {
        boolean res = true;
        for (Review review : reviews) {
            if (!this.addReview(review))
                res = false;
        }
        return res;
    }

    /**
     * Returns a single Review, the Review with the given ID, or null if not found.
     * @param id       The ID of the Review to be retrieved.
     * @return The Review with the given ID, or null if not found.
     */
    public Review getReview(Long id) {
        if (dataChecker.isValid(id)) {
            for (int i = 0; i < reviewArray.size(); i++) {
                if (reviewArray.get(i).getID().equals(id)) {
                    return reviewArray.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Returns an array of all Reviews, sorted in ascending order of ID.
     * The Review with the lowest ID should be the first element in the array.
     * @return A sorted array of Review objects, with lowest ID first.
     */
    public Review[] getReviews() {
        Review[] res = new Review[reviewArray.size()];
        for (int i = 0; i < reviewArray.size(); i++) {
            res[i] = reviewArray.get(i);
        }
        this.reviewArrayQuickSortByID(res);
        return res;
    }

    public void reviewArrayQuickSortByID(Review[] reviews) {
        reviewArrayQuickSort(reviews, "id", 0, reviews.length - 1);
    }

    public void reviewArrayQuickSortByDateReviewed(Review[] reviews) {
        reviewArrayQuickSort(reviews, "dateReviewed", 0, reviews.length - 1);
    }

    public void reviewArrayQuickSortByRating(Review[] reviews) {
        reviewArrayQuickSort(reviews, "rating", 0, reviews.length - 1);
    }

    public int idCompare(Review r1, Review r2) {
        return r1.getID().compareTo(r2.getID());
    }

    public int ratingCompare(Review r1, Review r2) {
        int ratingCompare = r2.getRating() - r1.getRating();
        if (ratingCompare == 0)
            return dateCompare(r1, r2);
        else
            return ratingCompare;
    }

    public int dateCompare(Review r1, Review r2) {
        int dateCompare = r2.getDateReviewed().compareTo(r1.getDateReviewed());
        if (dateCompare == 0)
            return idCompare(r1, r2);
        else
            return dateCompare;
    }

    public void reviewArrayQuickSort(Review[] reviews, String sortBy, int begin, int end) {
        if (begin < end) {
            int partitionIndex;
            Review pivot = reviews[end];

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {

                int c = 0;

                if (sortBy.equalsIgnoreCase("id"))
                    c = idCompare(reviews[j], pivot);
                else if (sortBy.equalsIgnoreCase("dateReviewed"))
                    c = dateCompare(reviews[j], pivot);
                else if (sortBy.equalsIgnoreCase("rating"))
                    c = ratingCompare(reviews[j], pivot);

                if (c < 0) {

                    i++;

                    Review tmp = reviews[i];
                    reviews[i] = reviews[j];
                    reviews[j] = tmp;
                }
            }

            Review tmp = reviews[i + 1];
            reviews[i + 1] = reviews[end];
            reviews[end] = tmp;

            partitionIndex = i + 1;

            reviewArrayQuickSort(reviews, sortBy, begin, partitionIndex - 1);
            reviewArrayQuickSort(reviews, sortBy, partitionIndex + 1, end);
        }
    }

    /**
     * Returns an array of all Reviews, sorted in descending order of date reviewed (newest first).
     * If the date reviewed is the same, then sort by ascending order of ID.
     * The newest Review should be the first element in the array, followed by lowest ID should the date reviewed be equal.
     * @return A sorted array of Review objects, with the newest Review first.
     */
    public Review[] getReviewsByDate() {
        Review[] res = this.getReviews();
        this.reviewArrayQuickSortByDateReviewed(res);
        return res;
    }

    /**
     * Returns an array of all Reviews, sorted in descending order of rating (highest first).
     * If the rating is the same, then sort by date reviewed (newest first), if still same then sort by ascending order of ID.
     * The highest rated Review should be the first element in the array, followed by lowest ID should the ratings be equal.
     * @return A sorted array of Review objects, with the highest rated Review first.
     */
    public Review[] getReviewsByRating() {
        Review[] res = this.getReviews();
        this.reviewArrayQuickSortByRating(res);
        return res;
    }

    /**
     * Returns an array of all Reviews by the Customer with the given ID.
     * The array is sorted by date reviewed from newest to oldest, with ascending order of ID for matching dates.
     * The newest Review should be the first element in the array, with the lowest ID should the date reviewed be equal.
     * @param id       The ID of the Customer who's Reviews are to be retrieved.
     * @return A sorted array of Review objects, with the newest Review first.
     */
    public Review[] getReviewsByCustomerID(Long id) {
        if (!dataChecker.isValid(id))
            return new Review[0];
        MyArrayList<Review> resList = new MyArrayList<>();
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)) {
                resList.add(reviewArray.get(i));
            }
        }
        Review[] res = new Review[resList.size()];
        res = resList.toArray(res);
        this.reviewArrayQuickSortByDateReviewed(res);
        return res;
    }

    /**
     * Returns an array of all Reviews for the Restaurant with the given ID.
     * The array should be sorted using the criteria defined for the {@link #getReviewsByCustomerID(Long) getReviewsByCustomerID} method.
     * @param id       The ID of the Restaurant who's Reviews are to be retrieved.
     * @return A sorted array of Review objects, with the newest Review first.
     */
    public Review[] getReviewsByRestaurantID(Long id) {
        if (!dataChecker.isValid(id))
            return new Review[0];
        MyArrayList<Review> resList = new MyArrayList<>();
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)) {
                resList.add(reviewArray.get(i));
            }
        }
        Review[] res = new Review[resList.size()];
        res = resList.toArray(res);
        this.reviewArrayQuickSortByDateReviewed(res);
        return res;
    }

    /**
     * Returns the average rating given by a Customer (to 1 dp), with the given ID.
     * If no ratings are found, return a rating of 0.
     * @param id       The ID of the Customer to retrieve the average rating for.
     * @return The average rating given by the Customer with the given ID, or 0 if no ratings found.
     */
    public float getAverageCustomerReviewRating(Long id) {
        if (!dataChecker.isValid(id))
            return -1;
        int sum = 0;
        int cnt = 0;
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)) {
                sum += reviewArray.get(i).getRating();
                cnt++;
            }
        }
        if (cnt == 0)
            return 0.0f;
        return ((float) sum) / cnt;
    }

    /**
     * Returns the rating for a Restaurant (to 1 dp), with the given ID, which is taken as the average of all ratings for that Restaurant.
     * If no ratings are found, return a rating of 0.
     * @param id       The ID of the Restaurant to retrieve the rating for.
     * @return The rating for the Restaurant with the given ID, or 0 if no ratings found.
     */
    public float getAverageRestaurantReviewRating(Long id) {
        if (!dataChecker.isValid(id))
            return -1;
        int sum = 0;
        int cnt = 0;
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)) {
                sum += reviewArray.get(i).getRating();
                cnt++;
            }
        }
        if (cnt == 0)
            return 0.0f;
        return ((float) sum) / cnt;
    }

    /**
     * Returns an array of 5 counts, corresponding to the Review ratings given by the Customer.
     * e.g. int[0] will contain the number of 1-star reviews given by the Customer, int[1] will contain the number of 2-star reviews etc.
     * @param id       The ID of the Customer who's Rating breakdown is to be retrieved.
     * @return An array of 5 counts detailing the breakdown of number of n-star reviews given by the Customer.
     */
    public int[] getCustomerReviewHistogramCount(Long id) {
        if (!dataChecker.isValid(id))
            return new int[0];
        int[] res = new int[5];
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)) {
                res[reviewArray.get(i).getRating() - 1]++;
            }
        }
        return res;
    }

    /**
     * Returns an array of 5 counts, corresponding to the Review ratings for the given Restaurant.
     * e.g. int[0] will contain the number of 1-star reviews for the Restaurant, int[1] will contain the number of 2-star reviews etc.
     * @param id       The ID of the Restaurant who's Rating breakdown is to be retrieved.
     * @return An array of 5 counts detailing the breakdown of number of n-star reviews for the given Restaurant.
     */
    public int[] getRestaurantReviewHistogramCount(Long id) {
        int[] res = new int[5];
        if (!dataChecker.isValid(id))
            return res;
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)) {
                res[reviewArray.get(i).getRating() - 1]++;
            }
        }
        return res;
    }

    /**
     * Returns an array of 20 Customer IDs that have reviewed the most Restaurants.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Review count, then by date of the oldest Review, and finally by ascending order of Customer ID for matching counts.
     * @return A sorted array of 20 Customer IDs, with the Customer with the highest Review count first.
     */
    public Long[] getTopCustomersByReviewCount() {
        Long[] topCustomers = new Long[20];
        AVLIDCounter tree = new AVLIDCounter();
        for (int i = 0; i < reviewArray.size(); i++) {
            Long id = reviewArray.get(i).getCustomerID();
            if (tree.searchByID(id) != null) {
                tree.searchByID(id).getKey().addCount();
                if (tree.searchByID(id).getKey().getLatestReviewDate().before(reviewArray.get(i).getDateReviewed())) {
                    tree.searchByID(id).getKey().setLatestReviewDate(reviewArray.get(i).getDateReviewed());
                }
            } else {
                tree.insertByID(new IDCounter(id, reviewArray.get(i).getDateReviewed()));
            }
        }
        MyArrayList<IDCounter> tmp = tree.toArrayList();
        AVLIDCounter tree2 = new AVLIDCounter();
        for (int i = 0; i < tmp.size(); i++) {
            tree2.insert(tmp.get(i));
        }
        tmp.clear();
        tmp = tree2.toArrayList();
        for (int i = 0; i < topCustomers.length && i < tmp.size(); i++) {
            topCustomers[i] = tmp.get(i).getIdentifier();
            System.out.println(tmp.get(i).getIdentifier() + " - " + tmp.get(i).getCount());
        }
        return topCustomers;
    }

    /**
     * Returns an array of 20 Restaurant IDs that have been reviewed the most.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending Review count, then by date of the oldest Review, and finally by ascending order of Restaurant ID for matching counts.
     * @return A sorted array of 20 Restaurant IDs, with the Restaurant with the highest Review count first.
     */
    public Long[] getTopRestaurantsByReviewCount() {
        Long[] topRestaurants = new Long[20];
        AVLIDCounter tree = new AVLIDCounter();
        for (int i = 0; i < reviewArray.size(); i++) {
            Long id = reviewArray.get(i).getRestaurantID();
            if (tree.searchByID(id) != null) {
                tree.searchByID(id).getKey().addCount();
                if (tree.searchByID(id).getKey().getLatestReviewDate().before(reviewArray.get(i).getDateReviewed())) {
                    tree.searchByID(id).getKey().setLatestReviewDate(reviewArray.get(i).getDateReviewed());
                }
            } else {
                tree.insertByID(new IDCounter(id, reviewArray.get(i).getDateReviewed()));
            }
        }
        MyArrayList<IDCounter> tmp = tree.toArrayList();
        AVLIDCounter tree2 = new AVLIDCounter();
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

    /**
     * Returns an array of 20 Restaurant IDs that have the highest rating.
     * If there are less than 20 IDs, the remaining indexes should be set to null.
     * The array should be sorted by descending average Review rating, then by date of the oldest Review, and finally by ascending order of Restaurant ID for matching counts.
     * @return A sorted array of 20 Restaurant IDs, with the Restaurant with the highest Review count first.
     */
    public Long[] getTopRatedRestaurants() {
        Long[] res = new Long[20];
        AVLRating tree = new AVLRating();
        for (int i = 0; i < reviewArray.size(); i++) {
            Long id = reviewArray.get(i).getRestaurantID();
            if (tree.searchByID(id) != null) {
                tree.searchByID(id).getKey().addCnt();
                tree.searchByID(id).getKey().addSumRating(reviewArray.get(i).getRating());
                if (tree.searchByID(id).getKey().getLatestReviewDate().before(reviewArray.get(i).getDateReviewed()))
                    tree.searchByID(id).getKey().setLatestReviewDate(reviewArray.get(i).getDateReviewed());
            } else {
                tree.insertByID(new Rating(id, reviewArray.get(i).getDateReviewed(), reviewArray.get(i).getRating()));
            }
        }
        MyArrayList<Rating> ratings = tree.toArrayList();
        AVLRating tree2 = new AVLRating();
        for (int i = 0; i < ratings.size(); i++) {
            tree2.insert(ratings.get(i));
        }
        ratings.clear();
        ratings = tree2.toArrayList();
        for (int i = 0; i < res.length && i < ratings.size(); i++) {
            res[i] = ratings.get(i).getId();
            System.out.println(
                    "Restaurant: " + ratings.get(i).getId() + " - rating " + ratings.get(i).getAverageRating());
        }
        return res;
    }

    /**
     * Returns an array of 5 keywords for the given Restaurant that have the highest frequency of use in the reviews.
     * If there are less than 5 keywords, the remaining indexes should be set to null.
     * The array should be sorted by descending count of frequency, then by alphabetical order (0-9 then A-Z) for matching counts.
     * @param id       The ID of the Restaurant who's top keywords are to be retrieved.
     * @return A sorted array of 5 keywords for the given Restaurant, with the highest frequency first.
     */
    public String[] getTopKeywordsForRestaurant(Long id) {
        String[] res = new String[5];
        Review[] restaurantReviews = this.getReviewsByRestaurantID(id);
        StringBuilder reviewString = new StringBuilder();
        for (Review r : restaurantReviews) {
            if (r.getRestaurantID().equals(id))
                reviewString.append(r.getReview());
        }
        String[] reviewWords = reviewString.toString().split("\\W+");
        AVLCounter tree = new AVLCounter(); // stores keyword in alphabetical order
        KeywordChecker keywordChecker = new KeywordChecker();
        for (String reviewWord : reviewWords) {
            if (keywordChecker.isAKeyword(reviewWord)) {
                if (tree.searchKeyword(reviewWord) != null) {
                    // word is already in tree
                    tree.searchKeyword(reviewWord).getKey().addCount();
                } else {
                    // insert keyword in alphabetical order
                    tree.insertByWord(new Counter<>(reviewWord.toLowerCase()));
                }
            }
        }
        MyArrayList<Counter<String>> tmp = tree.toArrayList();
        AVLCounter tree2 = new AVLCounter();
        for (int i = 0; i < tmp.size(); i++) {
            tree2.insert(tmp.get(i)); // insert keyword in frequency and then alphabetical order
        }
        tmp.clear();
        tmp = tree2.toArrayList();
        for (int i = 0; i < res.length && i < tmp.size(); i++) {
            res[i] = tmp.get(i).getIdentifier();
            System.out.println("[cnt = " + tmp.get(i).getCount() + "] Word: " + tmp.get(i).getIdentifier());
        }
        return res;
    }

    /**
     * Return an array of all the Reviews whose review message contain the given query.
     * Search queries are accent-insensitive, case-insensitive and space-insensitive.
     * The array should be sorted using the criteria defined for the {@link #getReviewsByDate() getReviewsByDate} method.
     * @param searchTerm       The search string to find.
     * @return A array of Customer objects, sorted using the criteria defined for the {@link #getReviewsByDate() getReviewsByDate} method.
     */
    public Review[] getReviewsContaining(String searchTerm) {
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        String searchTermConvertedFaster = StringFormatter.convertAccentsFaster(searchTerm.replaceAll("\\s+", " "));
        searchTermConvertedFaster = searchTermConvertedFaster.trim();
        if (searchTermConvertedFaster.length() == 0) {
            return new Review[0];
        }
        MyArrayList<Review> resList = new MyArrayList<>();
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getReview().toLowerCase().contains(searchTermConvertedFaster.toLowerCase())) {
                resList.add(reviewArray.get(i));
            }
        }
        Review[] res = new Review[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }
        this.reviewArrayQuickSortByDateReviewed(res);
        for (Review r : res) {
            System.out.println(
                    String.format("Date: %19s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getDateReviewed()))
                            + ", review: " + r.getReview());
        }
        return res;
    }
}
