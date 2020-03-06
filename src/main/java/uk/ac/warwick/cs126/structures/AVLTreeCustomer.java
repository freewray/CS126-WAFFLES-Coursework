package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Customer;

public class AVLTreeCustomer extends AVLTree<Customer, Long> {

    public AVLTreeCustomer() {
        super();
    }

    public AVLTreeCustomer(String sortBy) {
        super(sortBy);
    }

    private int idCompare(Customer c1, Customer c2) {
        return c1.getID().compareTo(c2.getID());
    }

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

    public int customCompare(Customer c1, Customer c2) {
        if (this.sortBy.equalsIgnoreCase("id"))
            return this.idCompare(c1, c2);
        else if (this.sortBy.equalsIgnoreCase("name"))
            return this.nameCompare(c1, c2);
        else
            return 0;
    }

    public int idOnlyCompare(Customer c1, Customer c2) {
        return idCompare(c1, c2);
    }

    public int idOnlyCompare(Long id, Customer c) {
        return id.compareTo(c.getID());
    }
}
