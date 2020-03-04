package uk.ac.warwick.cs126.structures;

import java.util.Date;
import java.util.Objects;

public class IDCounter implements Comparable<IDCounter>{

    private Date latestReviewDate;
    private Long identifier;
    private int count;

    public IDCounter(Long identifier, Date latestReviewDate) {
        this.identifier = identifier;
        this.count = 1;
        this.latestReviewDate = latestReviewDate;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count = count + 1;
    }

    public Date getLatestReviewDate() {
        return latestReviewDate;
    }

    public void setLatestReviewDate(Date latestReviewDate) {
        this.latestReviewDate = latestReviewDate;
    }

    @Override
    public int compareTo(IDCounter c) {
        int countCompare = c.getCount() - this.getCount();
        int identifierCompare = this.getIdentifier().compareTo(c.getIdentifier());
        int dateCompare = this.getLatestReviewDate().compareTo(c.getLatestReviewDate());
        if (countCompare == 0 && identifierCompare == 0){
            return identifierCompare;
        } else if (countCompare == 0) {
            return dateCompare;
        } else {
            return countCompare;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IDCounter idCounter = (IDCounter) o;
        return identifier.equals(idCounter.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
