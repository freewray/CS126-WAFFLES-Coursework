package uk.ac.warwick.cs126.structures;

public class ListElement<E> {
    private final E value;
    private ListElement<E> next;
    private ListElement<E> prev;

    public ListElement(E value) {
        this.value = value;
    }

    
    /** 
     * @return E
     */
    public E getValue() {
        return this.value;
    }

    
    /** 
     * @return ListElement<E>
     */
    public ListElement<E> getNext() {
        return this.next;
    }

    
    /** 
     * @return ListElement<E>
     */
    public ListElement<E> getPrev() {
        return this.prev;
    }

    
    /** 
     * @param e
     */
    public void setNext(ListElement<E> e) {
        this.next = e;
    }

    
    /** 
     * @param e
     */
    public void setPrev(ListElement<E> e) {
        this.prev = e;
    }

}
