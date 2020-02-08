package uk.ac.warwick.cs126.stores;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
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
        // TODO
        if (dataChecker.isValid(customer) && this.getCustomer(customer.getID()) == null){
            customerArray.add(customer);
            return true;
        }
        return false;
    }

    public boolean addCustomer(Customer[] customers) {
        // TODO
        for (int i = 0; i < customers.length; i++) {
            if (dataChecker.isValid(customers[i]))
                customerArray.add(customers[i]);
            else // if anyone of the cus in arrayay is not valid
                return false;
        }
        return true;
    }

    public Customer getCustomer(Long id) {
        // TODO
        for (int i = 0; i < customerArray.size(); i++) {
            if (customerArray.get(i).getID().equals(id))
                return customerArray.get(i);
        }
        return null;
    }

    public Customer[] getCustomers() {
        // TODO
        Customer[] res = new Customer[customerArray.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = customerArray.get(i);
        }
        quickSort(res, 0, res.length - 1, "id");
        return res;
    }

    public void quickSort(Customer array[], int begin, int end, String by) {
        if (begin < end) {
            int partitionIndex = 0;
            if (by == "id")
                partitionIndex = partition(array, begin, end);
            else if (by == "firstName")
                partitionIndex = partition(array, begin, end, 1);
            else if (by == "lastName")
                partitionIndex = partition(array, begin, end, 2);
            else return;

            quickSort(array, begin, partitionIndex - 1, by);
            quickSort(array, partitionIndex + 1, end, by);
        }
    }

    private int partition(Customer array[], int begin, int end) {
        long pivot = array[end].getID();

        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (array[j].getID().compareTo(pivot) <= 0) {
                i++;

                Customer tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }

        Customer tmp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = tmp;

        return i + 1;
    }

    private int partition(Customer array[], int begin, int end, int type) {
        String pivot = "";
        if (type == 1)
            pivot = array[end].getFirstName();
        else if (type == 2)
            pivot = array[end].getLastName();
        else
            return -1;

        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            String name = "";
            if (type == 1)
                name = array[j].getFirstName();
            else if (type == 2)
                name = array[j].getLastName();

            if (name.compareTo(pivot) <= 0) {
                i++;

                Customer tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }

        Customer tmp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = tmp;

        return i + 1;
    }

    public Customer[] getCustomers(Customer[] customers) {
        // TODO
        Customer[] res = customers.clone();
        quickSort(res, 0, res.length - 1, "id");
        return res;
    }

    public Customer[] getCustomersByName() {
        // TODO
        Customer[] res = new Customer[customerArray.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = customerArray.get(i);
        }
        quickSort(res, 0, res.length - 1, "id");
        quickSort(res, 0, res.length - 1, "firstName");
        quickSort(res, 0, res.length - 1, "lastName");
        return res;
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        // TODO
        Customer[] res = customers.clone();
        quickSort(res, 0, res.length - 1, "id");
        quickSort(res, 0, res.length - 1, "firstName");
        quickSort(res, 0, res.length - 1, "lastName");
        return res;
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        // ignore multiple spaces, only use the one space.
        if (searchTerm.length() == 0)
            return new Customer[0];
//        String searchTermConverted = StringFormatter.convertAccents(searchTerm.replaceAll("\\s+", ""));
        String searchTermConverted = StringFormatter.convertAccentsFaster(searchTerm.replaceAll("\\s+", ""));
        MyArrayList<Customer> resList = new MyArrayList<>();
        if (searchTermConverted.contains("/s")) {
            String[] term = searchTermConverted.split("/s");
            String firstName = term[0];
            String lastName = term[1];

            for (int i = 0; i < customerArray.size(); i++) {
                if (customerArray.get(i).getFirstName().toLowerCase().contains(firstName.toLowerCase()) && customerArray.get(i).getLastName().toLowerCase().contains(lastName.toLowerCase()))
                    resList.add(customerArray.get(i));
            }
        } else {
            for (int i = 0; i < customerArray.size(); i++) {
                if (customerArray.get(i).getFirstName().toLowerCase().contains(searchTermConverted.trim().toLowerCase()) || customerArray.get(i).getLastName().toLowerCase().contains(searchTermConverted.trim().toLowerCase()))
                    resList.add(customerArray.get(i));
            }
        }

        Customer[] res = new Customer[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }
        quickSort(res, 0, res.length - 1, "id");
        quickSort(res, 0, res.length - 1, "firstName");
        quickSort(res, 0, res.length - 1, "lastName");
        return res;
    }
}
