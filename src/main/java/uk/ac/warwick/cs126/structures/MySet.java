package uk.ac.warwick.cs126.structures;

/**
 * A generic implementation of the IList iterface.
 */
public class MySet<E> {

    private MyArrayList<E> array = new MyArrayList<>();

    // INCOMPLETE.
    public boolean add(E element) {
        // Adds element to the list when it does not already exist.
        // Returns true on success and false otherwise.
        for (int i = 0; i < array.size(); i++) {
//            System.out.println(element);
//            System.out.println(array.get(i));
//            System.out.println(element.equals(array.get(i)));
//            System.out.println();
            if (element.equals(array.get(i)))
                return false;
        }
        array.add(element);
        return true;
    }

    // INCOMPLETE.
    public String toString() {
        // Returns a string representation of this Set object.
        String temp = "(";
        for (int i = 0; i < array.size(); i++) {
            temp += array.get(i).toString();
            if(i < array.size() - 1)
                temp += ", ";
        }
        temp += ") ";
        return temp;
    }

    public void clear() {
        array.clear();
    }

    public boolean contains(E element) {
        return array.contains(element);
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public boolean remove(E element) {
        return array.remove(element);
    }

    public int size() {
        return array.size();
    }

    public E get(int i) {
        return array.get(i);
    }

    public E[] toArray(E[] array){
        for (int i = 0; i < this.size(); i++) {
            array[i] = this.get(i);
        }

        return array;
    }

}
