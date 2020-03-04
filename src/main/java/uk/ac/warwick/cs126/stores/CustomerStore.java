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

public class CustomerStore implements ICustomerStore {

    private CustomerAVLTree customerTree;
    private DataChecker dataChecker;
    private AVLTreeCom<Long> blackListedCustomerID;

    public CustomerStore() {
        dataChecker = new DataChecker();
        blackListedCustomerID = new AVLTreeCom<>();
        customerTree = new CustomerAVLTree();
    }

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

    public boolean addCustomer(Customer[] customers) {
        boolean res = true;
        for (Customer customer : customers) {
            if (this.addCustomer(customer) == false)
                res = false;
        }
        return res;
    }

    public Customer getCustomer(Long id) {
        return customerTree.searchByID(id);
    }

    public Customer[] getCustomers() {
        MyArrayList<Customer> resList = new MyArrayList<>();
        customerTree.inOrder(resList);
        Customer[] result = new Customer[resList.size()];
        result = resList.toArray(result);
        return result;
    }

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
