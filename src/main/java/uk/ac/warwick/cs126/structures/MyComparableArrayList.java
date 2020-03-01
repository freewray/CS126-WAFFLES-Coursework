package uk.ac.warwick.cs126.structures;

public class MyComparableArrayList<E extends Comparable<E>> extends MyArrayList<E> {
    public MyComparableArrayList(){
        super();
    }

    public boolean add(E element){
        return super.add(element);
    }

    public E get(int index){
        return (E) super.get(index);
    }

    public void quicksort(int begin, int end) {
        if (begin < end) {
            int partitionIndex;
            E pivot = this.get(end);

            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (this.get(j).compareTo(pivot) <= 0) {
                    i++;

                    E tmp = this.get(i);
                    this.set(i, this.get(j));
                    this.set(j, tmp);
                }
            }

            E tmp = this.get(i + 1);
            this.set(i + 1, this.get(end));
            this.set(end, tmp);

            partitionIndex = i + 1;

            quicksort(begin, partitionIndex - 1);
            quicksort(partitionIndex + 1, end);
        }
    }
}
