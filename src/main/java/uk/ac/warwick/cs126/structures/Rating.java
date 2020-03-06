package uk.ac.warwick.cs126.structures;

import java.util.Date;

public class Rating implements Comparable<Rating> {

    private final Long id;
    private int cnt;
    private int sumRating;
    private Date latestReviewDate;

    public Long getId() {
        return id;
    }

    public void addCnt() {
        this.cnt = cnt + 1;
    }

    public void addSumRating(int rating) {
        this.sumRating += rating;
    }

    public Date getLatestReviewDate() {
        return latestReviewDate;
    }

    public void setLatestReviewDate(Date latestReviewDate) {
        this.latestReviewDate = latestReviewDate;
    }

    public Rating(Long id, Date latestReviewDate, int rating){
        this.id = id;
        this.latestReviewDate = latestReviewDate;
        this.sumRating = rating;
        this.cnt = 1;
    }

    public float getAverageRating(){
        return ((float)sumRating)/cnt;
    }

    @Override
    public int compareTo(Rating r) {
        float ratingCompare = r.getAverageRating() - this.getAverageRating();
        int dateCompare = this.getLatestReviewDate().compareTo(r.getLatestReviewDate());
        int idCompare = this.getId().compareTo(r.getId());

        if (ratingCompare == 0 && dateCompare == 0)
            return idCompare;
        else if (ratingCompare == 0)
            return dateCompare;
        else
            return (ratingCompare < 0 ? -1 : 1);
    }
}