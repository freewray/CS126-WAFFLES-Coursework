package uk.ac.warwick.cs126.stores;

import java.util.Date;

public class Ratings{
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