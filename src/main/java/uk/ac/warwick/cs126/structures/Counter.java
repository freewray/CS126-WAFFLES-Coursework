package uk.ac.warwick.cs126.structures;

public class Counter<E extends Comparable<E>> implements Comparable<Counter<E>> {
    private final E identifier;
    private int count;

    public Counter(E identifier) {
        this.identifier = identifier;
        this.count = 1;
    }

    public E getIdentifier() {
        return identifier;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count = count + 1;
    }

    @Override
    public int compareTo(Counter<E> c) {
        int countCompare = c.getCount() - this.getCount();
        int identifierCompare = this.getIdentifier().compareTo(c.getIdentifier());
        if (countCompare == 0) {
            return identifierCompare;
        } else {
            return (countCompare < 0 ? -1 : 1);
        }
    }
}
