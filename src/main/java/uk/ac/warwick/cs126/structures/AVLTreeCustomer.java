package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Customer;

public class AVLTreeCustomer extends AVLTree<Customer, Long> {

    public AVLTreeCustomer() {
        super();
    }

    public AVLTreeCustomer(String sortBy) {
        super(sortBy);
    }

    
    /** ID Comparison between 2 customers
     * @param c1 customer 1 id to compare to
     * @param c2 
     * @return int
     */
    private int idCompare(Customer c1, Customer c2) {
        return c1.getID().compareTo(c2.getID());
    }

    
    /** 
     * Name Comparision between 2 customers
     * @param c1 customer 1 name to compare to
     * @param c2 customer 2 name to compare to
     * @return nagative number if o1 is customer 1 name bigger
     *         positive number if o1 is customer 1 name smaller
     */
    private int nameCompare(Customer c1, Customer c2) {
        int firstNameCompare = c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
        int lastNameCompare = c1.getLastName().compareToIgnoreCase((c2.getLastName()));
        if (firstNameCompare == 0 && lastNameCompare == 0)
            return idCompare(c1, c2);
        else if (lastNameCompare == 0)
            return firstNameCompare;
        else
            return lastNameCompare;
    }

    
    /**
     * default comparison decleared by the tree
     *
     * @param o1 object 1 to be compared with
     * @param o2 object 2 to be compared with
     * @return nagative number if o1 is smaller
     *         positive number if o1 is bigger
     */
    public int customCompare(Customer c1, Customer c2) {
        if (this.sortBy.equalsIgnoreCase("id"))
            return this.idCompare(c1, c2);
        else if (this.sortBy.equalsIgnoreCase("name"))
            return this.nameCompare(c1, c2);
        else
            return 0;
    }

    
    /** 
     * @param c1 customer 1 to compare to
     * @param c2 customer 2 to compare to
     * @return nagative number if c1 id is smaller
     *         positive number if c1 id is smaller
     */
    public int idOnlyCompare(Customer c1, Customer c2) {
        return idCompare(c1, c2);
    }

    
    /** 
     * @param id to compare to
     * @param c customer to compare to
     * @return nagative number if id is smaller
     *         positive number if c's id is smaller
     */
    public int idOnlyCompare(Long id, Customer c) {
        return id.compareTo(c.getID());
    }
}
