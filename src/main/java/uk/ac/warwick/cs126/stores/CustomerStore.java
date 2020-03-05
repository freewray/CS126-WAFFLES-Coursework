package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.structures.AVLTreeCom;
import uk.ac.warwick.cs126.structures.CustomerAVLTree;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class stores customers and perform actions on customers
 */
public class CustomerStore implements ICustomerStore {


    /**
     * customers are stored in an AVL tree therefore quicker to insert,
     * delete and search
     */
    private CustomerAVLTree customerTree;
    private DataChecker dataChecker;
    /**
     * Comparable AVL Tree stores blackListed customerID
     */
    private AVLTreeCom<Long> blackListedCustomerID;

    public CustomerStore() {
        dataChecker = new DataChecker();
        blackListedCustomerID = new AVLTreeCom<>();
        customerTree = new CustomerAVLTree();
    }

    /**
     * Loads data from a csv file containing the Customer data into a Customer array, parsing the attributes where required.
     * @param resource       The source csv file to be loaded.
     * @return A Customer array with all Customers contained within the data file, regardless of the validity of the ID.
     */
    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }


    /**
     * Add a new Customer to the store. The method should return true if the Customer is successfully added to the data store.
     * The Customer should not be added if a Customer with the same ID already exists in the store.
     * If a duplicate ID is encountered, the existing Customer should be removed and the ID blacklisted from further use.
     * An invalid ID is one that contains zeros or more than 3 of the same digit, these should not be added, although they do not need to be blacklisted.
     * @param customer       The Customer object to add to the data store.
     * @return True if the Customer was successfully added, false otherwise.
     */
    public boolean addCustomer(Customer customer) {
        if (!dataChecker.isValid(customer) || blackListedCustomerID.search(customer.getID()) != null){
            return false;
        }
        if (this.getCustomer(customer.getID()) != null){
            blackListedCustomerID.insert(customer.getID());
            customerTree.remove(this.getCustomer(customer.getID()));
            return false;
        }
        customerTree.insert(customer);
        return true;
    }

    /**
     * Add new Customers in the input array to the store. The method should return true if the Customers are all successfully added to the data store.
     * Reference the {@link #addCustomer(Customer) addCustomer} method for details on ID handling.
     * @param customers       An array of Customer objects to add to the data store.
     * @return True if all of the Customers were successfully added, false otherwise.
     */
    public boolean addCustomer(Customer[] customers) {
        boolean res = true;
        for (Customer customer : customers) {
            if (this.addCustomer(customer) == false)
                res = false;
        }
        return res;
    }

    /**
     * Returns a single Customer, the Customer with the given ID, or null if not found.
     * @param id       The ID of the Customer to be retrieved.
     * @return The Customer with the given ID, or null if not found.
     */
    public Customer getCustomer(Long id) {
        return customerTree.searchByID(id);
    }

    /**
     * Returns an array of all Customers, sorted in ascending order of ID.
     * The Customer with the lowest ID should be the first element in the array.
     * @return A sorted array of Customer objects, with lowest ID first.
     */
    public Customer[] getCustomers() {
        MyArrayList<Customer> resList = new MyArrayList<>();
        customerTree.inOrder(resList);
        Customer[] result = new Customer[resList.size()];
        result = resList.toArray(result);
        return result;
    }

    /**
     * Returns an array of Customers, sorted in ascending order of ID.
     * The Customer with the lowest ID should be the first element in the array.
     * Similar functionality to the {@link #getCustomers() getCustomers} method.
     * @param customers       An array of Customer objects to be sorted.
     * @return A sorted array of Customer objects, with lowest ID first.
     */
    public Customer[] getCustomers(Customer[] customers) {
        CustomerAVLTree tree = new CustomerAVLTree();

        for (Customer c : customers)
            tree.insert(c);

        MyArrayList<Customer> resList = new MyArrayList<>();
        tree.inOrder(resList);
        Customer[] result = new Customer[resList.size()];
        result = resList.toArray(result);
        return result;
    }


    /**
     * Returns an array of all Customers, sorted in alphabetical order of Last Name, then First Name.
     * If the First Name and Last Name are identical for multiple Customers, they should be further sorted in ascending order of ID.
     * The Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically should be the first element in the array.
     * @return A sorted array of Customer objects, where the first element is the Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically, followed by ID if the Last Name, First Name combination is equal.
     */
    public Customer[] getCustomersByName() {
        Customer[] tmp = this.getCustomers();
        MyArrayList<Customer> resList = new MyArrayList<>();
        CustomerAVLTree tree = new CustomerAVLTree("name");
        for (Customer c : tmp) {
            tree.insert(c);
        }
        tree.inOrder(resList);
        Customer[] result = new Customer[resList.size()];
        result = resList.toArray(result);
        return result;
    }


    /**
     * Returns an array of all Customers, sorted in alphabetical order of Last Name, then First Name.
     * If the First Name and Last Name are identical for multiple Customers, they should be further sorted in ascending order of ID.
     * The Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically should be the first element in the array.
     * Similar functionality to the {@link #getCustomersByName() getCustomersByName} method.
     * @param customers       An array of Customer objects to be sorted.
     * @return A sorted array of Customer objects, where the first element is the Customer with the Last Name, First Name combination that is nearest to 'A' alphabetically, followed by ID if the Last Name, First Name combination is equal.
     */
    public Customer[] getCustomersByName(Customer[] customers) {
        MyArrayList<Customer> resList = new MyArrayList<>();
        CustomerAVLTree tree = new CustomerAVLTree("name");
        for (Customer c : customers) {
            tree.insert(c);
        }
        tree.inOrder(resList);
        Customer[] result = new Customer[resList.size()];
        result = resList.toArray(result);
        return result;
    }

    /**
     * Return an array of all the Customers whose Last Name and First Name (sorted in that order) contain the given query.
     * Search queries are accent-insensitive, case-insensitive and space-insensitive.
     * The array should be sorted using the criteria defined for the {@link #getCustomersByName() getCustomersByName} method.
     * @param searchTerm       The search string to find.
     * @return A array of Customer objects, sorted using the criteria defined for the {@link #getCustomersByName() getCustomersByName} method.
     */
    public Customer[] getCustomersContaining(String searchTerm) {
        // ignore multiple spaces, only use the one space.
        if (searchTerm.length() == 0)
            return new Customer[0];
        String searchTermConverted = StringFormatter.convertAccentsFaster(searchTerm.replaceAll("\\s+", " "));
        MyArrayList<Customer> resList = new MyArrayList<>();
        MyArrayList<Customer> customerList = new MyArrayList<>();
        customerTree.inOrder(customerList);
        if (searchTermConverted.contains("/s")) {
            String[] term = searchTermConverted.split("/s");
            String firstName = term[0];
            String lastName = term[1];

            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getFirstName().toLowerCase().contains(firstName.toLowerCase()) && customerList.get(i).getLastName().toLowerCase().contains(lastName.toLowerCase()))
                    resList.add(customerList.get(i));
            }
        } else {
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getFirstName().toLowerCase().contains(searchTermConverted.trim().toLowerCase()) || customerList.get(i).getLastName().toLowerCase().contains(searchTermConverted.trim().toLowerCase()))
                    resList.add(customerList.get(i));
            }
        }
        CustomerAVLTree tree = new CustomerAVLTree("name");
        for (int i = 0; i < resList.size(); i++) {
            tree.insert(resList.get(i));
        }
        resList.clear();
        tree.inOrder(resList);
        Customer[] res = new Customer[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }
        return res;
    }
}
