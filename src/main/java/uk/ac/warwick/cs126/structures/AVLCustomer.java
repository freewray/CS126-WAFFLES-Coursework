package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Customer;

public class AVLCustomer extends AVLTreeStore<Customer> {

    public AVLCustomer() {
        super();
    }

    public AVLCustomer(String sortBy) {
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

    @Override
    public int customCompare(Customer r1, Customer r2) {
        if (this.sortBy.equalsIgnoreCase("id"))
            return this.idCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("name"))
            return this.nameCompare(r1, r2);
        else
            return 0;
    }

    public void remove(Customer key) {
        AVLTreeNode<Customer> tmp;

        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    public AVLTreeNode<Customer> search(AVLTreeNode<Customer> node, Customer key) {

        if (node == null) {
            return null; // missing from tree
        } else if (key.getID().compareTo(node.getKey().getID()) < 0) {
            return search(node.getLeft(), key);
        } else if (key.getID().compareTo(node.getKey().getID()) > 0) {
            return search(node.getRight(), key);
        } else {
            return node; // found it
        }
    }

    private Customer searchByID(AVLTreeNode<Customer> node, Long id) {

        if (node == null) {
            return null; // missing from tree
        } else if (id.compareTo(node.getKey().getID()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (id.compareTo(node.getKey().getID()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node.getKey(); // found it
        }
    }

    public Customer searchByID(Long id) {
        return searchByID(root, id);
    }
}
