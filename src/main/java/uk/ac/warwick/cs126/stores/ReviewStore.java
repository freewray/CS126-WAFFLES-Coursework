package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Review;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class ReviewStore implements IReviewStore {

    private MyArrayList<Review> reviewArray;
    private DataChecker dataChecker;
    private MyArrayList<Long> blackListedReviewID;

    public ReviewStore() {
        // Initialise variables here
        reviewArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blackListedReviewID = new MyArrayList<>();
    }

    public Review[] loadReviewDataToArray(InputStream resource) {
        Review[] reviewArray = new Review[0];

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

            Review[] loadedReviews = new Review[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int reviewCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Review review = new Review(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]),
                            data[4],
                            Integer.parseInt(data[5]));
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

    public boolean addReview(Review review) {
        if (blackListedReviewID.contains(review.getID())){
            return false;
        }
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getID().equals(review.getID())){
                blackListedReviewID.add(review.getID());
                reviewArray.remove(reviewArray.get(i));
                return false;
            }
            else if (reviewArray.get(i).getCustomerID().equals(review.getCustomerID())
                    && reviewArray.get(i).getRestaurantID().equals(review.getRestaurantID())
                    && reviewArray.get(i).getDateReviewed().compareTo(review.getDateReviewed()) < 0){

                blackListedReviewID.add(reviewArray.get(i).getID());
                reviewArray.remove(reviewArray.get(i));
                reviewArray.add(review);
                return true;
            }
        }
        reviewArray.add(review);
        return true;
    }

    public boolean addReview(Review[] reviews) {
        boolean res = true;
        for (Review review : reviews){
            if (! this.addReview(review))
                res = false;
        }
        return res;
    }

    public Review getReview(Long id) {
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getID().equals(id)){
                return reviewArray.get(i);
            }
        }
        return null;
    }

    public Review[] getReviews() {
        Review[] res = new Review[reviewArray.size()];
        for (int i = 0; i < reviewArray.size(); i++) {
            res[i] = reviewArray.get(i);
        }
        this.reviewArrayQuickSortByID(res);
        return res;
    }

    public void reviewArrayQuickSortByID(Review[] reviews){
        reviewArrayQuickSort(reviews, "id", 0, reviews.length - 1);
    }

    public void reviewArrayQuickSortByDateReviewed(Review[] reviews){
        reviewArrayQuickSort(reviews, "dateReviewed", 0, reviews.length - 1);
    }

    public void reviewArrayQuickSortByRating(Review[] reviews){
        reviewArrayQuickSort(reviews, "rating", 0, reviews.length - 1);
    }

    public void reviewArrayQuickSort(Review[] reviews, String sortBy, int begin, int end){
        if (begin < end) {
            int partitionIndex = 0;
            Review pivot = reviews[end];

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (sortBy == "id"){
                    if (reviews[j].getID().compareTo(pivot.getID()) < 0) {

                        i++;

                        Review tmp = reviews[i];
                        reviews[i] = reviews[j];
                        reviews[j] = tmp;
                    }
                }
                else if (sortBy == "dateReviewed"){
                    if (reviews[j].getDateReviewed().after(pivot.getDateReviewed())
                    || (reviews[j].getDateReviewed().equals(pivot.getDateReviewed())
                        && reviews[j].getID().compareTo(pivot.getID()) < 0)) {

                        i++;

                        Review tmp = reviews[i];
                        reviews[i] = reviews[j];
                        reviews[j] = tmp;
                    }
                }
                else if (sortBy == "rating"){
                    if (reviews[j].getRating() > pivot.getRating()
                            || (reviews[j].getRating() == pivot.getRating()
                                && reviews[j].getDateReviewed().after(pivot.getDateReviewed()))
                            || (reviews[j].getRating() == pivot.getRating()
                                && reviews[j].getDateReviewed().equals(pivot.getDateReviewed())
                                && reviews[j].getID().compareTo(pivot.getID()) < 0)) {

                        i++;

                        Review tmp = reviews[i];
                        reviews[i] = reviews[j];
                        reviews[j] = tmp;
                    }
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

    public Review[] getReviewsByDate() {
        Review[] res = this.getReviews();
        this.reviewArrayQuickSortByDateReviewed(res);
        return res;
    }

    public Review[] getReviewsByRating() {
        Review[] res = this.getReviews();
        this.reviewArrayQuickSortByRating(res);
        return res;
    }

    public Review[] getReviewsByCustomerID(Long id) {
        MyArrayList<Review> resList = new MyArrayList<>();
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)){
                resList.add(reviewArray.get(i));
            }
        }
        Review[] res = new Review[resList.size()];
        res = resList.toArray(res);
        this.reviewArrayQuickSortByDateReviewed(res);
        return res;
    }

    public Review[] getReviewsByRestaurantID(Long id) {
        MyArrayList<Review> resList = new MyArrayList<>();
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)){
                resList.add(reviewArray.get(i));
            }
        }
        Review[] res = new Review[resList.size()];
        res = resList.toArray(res);
        this.reviewArrayQuickSortByDateReviewed(res);
        return res;
    }

    public float getAverageCustomerReviewRating(Long id) {
        int sum = 0;
        int cnt = 0;
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)){
                sum += reviewArray.get(i).getRating();
                cnt++;
            }
        }
        if (cnt == 0)
            return 0.0f;
        float avg = ((float) sum)/cnt;
        return avg;
    }

    public float getAverageRestaurantReviewRating(Long id) {
        int sum = 0;
        int cnt = 0;
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)){
                sum += reviewArray.get(i).getRating();
                cnt++;
            }
        }
        if (cnt == 0)
            return 0.0f;
        float avg = ((float) sum)/cnt;
        return avg;
    }

    public int[] getCustomerReviewHistogramCount(Long id) {
        int[] res = new int[5];
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)){
                res[reviewArray.get(i).getRating() - 1]++;
            }
        }
        return res;
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        int[] res = new int[5];
        for (int i = 0; i < reviewArray.size(); i++ ) {
            if (reviewArray.get(i).getRestaurantID().equals(id)){
                res[reviewArray.get(i).getRating() - 1]++;
            }
        }
        return res;
    }

    private class ReviewCnt {

        Long id;
        Date latestReviewDate;
        int times;

        public ReviewCnt(Long id, Date latestReviewDate) {
            this.id = id;
            this.latestReviewDate = latestReviewDate;
            this.times = 1;
        }
    }

    public void reviewCntQuicksort(MyArrayList<ReviewCnt> array, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            int pivotTimes = array.get(end).times;
            Date pivotDate = array.get(end).latestReviewDate;

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {

                // sort times from highest to lowest
                // or if both has same times then
                // only exchange value when latest fav is older
                if (array.get(j).times > pivotTimes || (array.get(j).latestReviewDate.before(pivotDate) && array.get(j).times == pivotTimes)) {
                    i++;

                    ReviewCnt tmp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, tmp);
                }
            }

            ReviewCnt tmp = array.get(i + 1);
            array.set(i + 1, array.get(end));
            array.set(end, tmp);

            partitionIndex = i + 1;

            reviewCntQuicksort(array, begin, partitionIndex - 1);
            reviewCntQuicksort(array, partitionIndex + 1, end);
        }
    }

    public Long[] getTopCustomersByReviewCount() {
        Long[] topCustomers = new Long[20];

        MyArrayList<ReviewCnt> topCustomerReviewCnt = new MyArrayList<>();
        for (int i = 0, j = 0; i < reviewArray.size(); i++) {
            for (j = 0; j < topCustomerReviewCnt.size(); j++) {
                if (topCustomerReviewCnt.get(j).id.compareTo(reviewArray.get(i).getCustomerID()) == 0){
                    topCustomerReviewCnt.get(j).times++;
                    if (topCustomerReviewCnt.get(j).latestReviewDate.compareTo(reviewArray.get(i).getDateReviewed()) < 0)
                        topCustomerReviewCnt.get(j).latestReviewDate = reviewArray.get(i).getDateReviewed();

                    break;
                }
            }

            if (j == topCustomerReviewCnt.size()){
                ReviewCnt customerFavourite = new ReviewCnt(reviewArray.get(i).getCustomerID(), reviewArray.get(i).getDateReviewed());
                topCustomerReviewCnt.add(customerFavourite);
            }
        }
        // sort by favourite times
        reviewCntQuicksort(topCustomerReviewCnt, 0, topCustomerReviewCnt.size()-1);
        // sort by latest date (oldest to newest)
        for (int i = 0; i < topCustomers.length && i < topCustomerReviewCnt.size(); i++) {
            topCustomers[i] = topCustomerReviewCnt.get(i).id;
        }

        return topCustomers;
    }

    public Long[] getTopRestaurantsByReviewCount() {
        Long[] topRestaurants = new Long[20];

        MyArrayList<ReviewCnt> topRestaurantReviewCnt = new MyArrayList<>();
        for (int i = 0, j = 0; i < reviewArray.size(); i++) {
            for (j = 0; j < topRestaurantReviewCnt.size(); j++) {
                if (topRestaurantReviewCnt.get(j).id.compareTo(reviewArray.get(i).getRestaurantID()) == 0){
                    topRestaurantReviewCnt.get(j).times++;
                    if (topRestaurantReviewCnt.get(j).latestReviewDate.compareTo(reviewArray.get(i).getDateReviewed()) < 0)
                        topRestaurantReviewCnt.get(j).latestReviewDate = reviewArray.get(i).getDateReviewed();

                    break;
                }
            }

            if (j == topRestaurantReviewCnt.size()){
                ReviewCnt customerFavourite = new ReviewCnt(reviewArray.get(i).getRestaurantID(), reviewArray.get(i).getDateReviewed());
                topRestaurantReviewCnt.add(customerFavourite);
            }
        }
        // sort by favourite times
        reviewCntQuicksort(topRestaurantReviewCnt, 0, topRestaurantReviewCnt.size()-1);
        // sort by latest date (oldest to newest)
        for (int i = 0; i < topRestaurants.length && i < topRestaurantReviewCnt.size(); i++) {
            topRestaurants[i] = topRestaurantReviewCnt.get(i).id;
        }

        return topRestaurants;
    }
    private class Ratings{
        Long id;
        int cnt;
        int sumRating;
        Date latestReviewDate;

        public Ratings(Long id, Date latestReviewDate, int rating){
            this.id = id;
            this.latestReviewDate = latestReviewDate;
            this.sumRating = rating;
            this.cnt = 1;
        }

        public float getAverageRating(){
            return ((float)sumRating)/cnt;
        }
    }

    public void ratingsQuicksort(MyArrayList<Ratings> array, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            Ratings pivot = array.get(end);

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {

                // 1. avg rating from highest to lowest
                // 2. date of latest review - oldest to newest  
                // 3. ascending order of their id
                if (array.get(j).getAverageRating() > pivot.getAverageRating() 
                || (array.get(j).getAverageRating() == pivot.getAverageRating() &&
                    array.get(j).latestReviewDate.before(pivot.latestReviewDate))
                || (array.get(j).getAverageRating() == pivot.getAverageRating() &&
                    array.get(j).latestReviewDate.equals(pivot.latestReviewDate) &&
                    array.get(j).id.compareTo(pivot.id) < 0)) {
                    i++;

                    Ratings tmp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, tmp);
                }
            }

            Ratings tmp = array.get(i + 1);
            array.set(i + 1, array.get(end));
            array.set(end, tmp);

            partitionIndex = i + 1;

            ratingsQuicksort(array, begin, partitionIndex - 1);
            ratingsQuicksort(array, partitionIndex + 1, end);
        }
    }

    public Long[] getTopRatedRestaurants() {
        MyArrayList<Ratings> ratings = new MyArrayList<>();
        Long[] res = new Long[20];
        for (int i = 0, j = 0; i < reviewArray.size(); i++) {
            for (j = 0; j < ratings.size(); j++) {
                if (ratings.get(j).id.equals(reviewArray.get(i).getRestaurantID())){
                    ratings.get(j).cnt++;
                    ratings.get(j).sumRating += reviewArray.get(i).getRating();
                    // System.out.println(ratings.get(j).id + " - " + ratings.get(j).getAverageRating());
                    if (ratings.get(j).latestReviewDate.compareTo(reviewArray.get(i).getDateReviewed()) < 0)
                        ratings.get(j).latestReviewDate = reviewArray.get(i).getDateReviewed();

                    break;
                }
            }

            if (j == ratings.size()){
                Ratings rating = new Ratings(reviewArray.get(i).getRestaurantID(), reviewArray.get(i).getDateReviewed(), reviewArray.get(i).getRating());
                ratings.add(rating);
            }
        }
        ratingsQuicksort(ratings, 0, ratings.size() - 1);
        for (int i = 0; i < res.length && i < ratings.size(); i++) {
            res[i] = ratings.get(i).id;
        }
        
        return res;
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        String[] res = new String[5];
        Review[] restaurantReviews = this.getReviewsByRestaurantID(id);
        String reviewString = "";
        for(Review r : restaurantReviews){
            if (r.getRestaurantID().equals(id))
                reviewString += r.getReview();
        }
        String[] reviewWords = reviewString.split("\\W+");

        MyArrayList<KeywordCnt> keywordList = new MyArrayList<>();
        KeywordChecker keywordChecker = new KeywordChecker();
        for(int j = 0, i = 0; j < reviewWords.length; j++){
            if (keywordChecker.isAKeyword(reviewWords[j])) {
                for (i = 0; i < keywordList.size(); i++) {
                    System.out.println(keywordList.get(i).keyword + " - " + keywordList.get(i).frequency);
                    if (keywordList.get(i).keyword.equalsIgnoreCase(reviewWords[j])){
                        keywordList.get(i).frequency++;
                        break;
                    }
                }
                if (i == keywordList.size()){
                    KeywordCnt w = new KeywordCnt(reviewWords[j]);
                    keywordList.add(w);
                }
            }
        }

        quicksortKeywordCnt(keywordList, 0, keywordList.size() - 1);
        for (int i = 0; i < res.length && i < keywordList.size(); i++) {
            res[i] = keywordList.get(i).keyword;
        }

        return res;
    }
    
    private class KeywordCnt {
        String keyword;
        int frequency;

        public KeywordCnt(String keyword){
            this.keyword = keyword;
            this.frequency = 1;
        }
    }

    // public void quicksortKeywordCnt()
    public void quicksortKeywordCnt(MyArrayList<KeywordCnt> array, int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            KeywordCnt pivot = array.get(end);

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {

                // 1. avg rating from highest to lowest
                // 2. date of latest review - oldest to newest  
                // 3. ascending order of their id
                if (array.get(j).frequency > pivot.frequency 
                || (array.get(j).frequency == pivot.frequency &&
                    array.get(j).keyword.compareToIgnoreCase(pivot.keyword) < 0)) {
                    i++;

                    KeywordCnt tmp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, tmp);
                }
            }

            KeywordCnt tmp = array.get(i + 1);
            array.set(i + 1, array.get(end));
            array.set(end, tmp);

            partitionIndex = i + 1;

            quicksortKeywordCnt(array, begin, partitionIndex - 1);
            quicksortKeywordCnt(array, partitionIndex + 1, end);
        }
    }

    public Review[] getReviewsContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        String searchTermConvertedFaster = StringFormatter.convertAccentsFaster(searchTerm);
        return new Review[0];
    }
}
