package uk.ac.warwick.cs126.structures;

import java.util.Date;

public class IDCounter implements Comparable<IDCounter> {

    private Date latestReviewDate;
    private final Long identifier;
    private int count;

    public IDCounter(Long identifier, Date latestReviewDate) {
        this.identifier = identifier;
        this.count = 1;
        this.latestReviewDate = latestReviewDate;
    }

    
    /** 
     * @return Long identifier
     */
    public Long getIdentifier() {
        return identifier;
    }

    
    /** 
     * @return int count
     */
    public int getCount() {
        return count;
    }

    /**
     * add count once
     */
    public void addCount() {
        this.count = count + 1;
    }

    
    /** 
     * @return the latest review date
     */
    public Date getLatestReviewDate() {
        return latestReviewDate;
    }

    
    /** 
     * @param latestReviewDate to set to (later one)
     */
    public void setLatestReviewDate(Date latestReviewDate) {
        this.latestReviewDate = latestReviewDate;
    }

    
    /** 
     * default method to compare to
     * @param c IDCounter to compare to
     * @return nagative number if this'id is smaller
     *          positive number if this'id is bigger
     */
    @Override
    public int compareTo(IDCounter c) {
        int countCompare = c.getCount() - this.getCount();
        int identifierCompare = this.getIdentifier().compareTo(c.getIdentifier());
        int dateCompare = this.getLatestReviewDate().compareTo(c.getLatestReviewDate());
        if (countCompare == 0 && dateCompare == 0) {
            return identifierCompare;
        } else if (countCompare == 0) {
            return dateCompare;
        } else {
            return countCompare;
        }
    }
}
