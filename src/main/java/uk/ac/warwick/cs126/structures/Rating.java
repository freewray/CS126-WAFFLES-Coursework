package uk.ac.warwick.cs126.structures;

import java.util.Date;

public class Rating implements Comparable<Rating> {

    private final Long id;
    private int cnt;
    private int sumRating;
    private Date latestReviewDate;

    public Rating(Long id, Date latestReviewDate, int rating){
        this.id = id;
        this.latestReviewDate = latestReviewDate;
        this.sumRating = rating;
        this.cnt = 1;
    }

    
    /** 
     * @return Long 
     */
    public Long getId() {
        return id;
    }

    /**
     * add count the the overall rating
     */
    public void addCnt() {
        this.cnt = cnt + 1;
    }

    
    /** 
     * @param rating to be added on to the sumRating
     */
    public void addSumRating(int rating) {
        this.sumRating += rating;
    }

    
    /** 
     * @return current sumRating
     */
    public int getSumRating(){
        return this.sumRating;
    }

    
    /** 
     * @return the LatestReviewDate
     */
    public Date getLatestReviewDate() {
        return latestReviewDate;
    }

    
    /** 
     * update the current latest review date with a later date
     * @param latestReviewDate
     */
    public void setLatestReviewDate(Date latestReviewDate) {
        this.latestReviewDate = latestReviewDate;
    }

    
    /** 
     * @return average rating  = sum / cnt
     */
    public float getAverageRating(){
        return ((float)sumRating)/cnt;
    }

    
    /** 
     * @param r restaurant to compare to
     * @return int negative if this restaurant is smaller
     *             positive if this restaurant is bigger
     */
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