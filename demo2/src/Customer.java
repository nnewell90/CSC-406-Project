import java.util.ArrayList;
import java.util.HashMap;

/**
 * The customer class handles all customer data
 */
public class Customer {
    // Data
    String SSN;
    String address;
    String city;
    String state;
    String zip;
    String firstName;
    String lastName;
    int customerID;
    private static int customerIDCounter = 0;

    // The arrayList which holds all customers
    static ArrayList<Customer> customersList = new ArrayList<Customer>();
    static HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();

    public Customer(String SSN, String address, String city, String state, String zip, String firstName, String lastName) {
        // Data management
        setSSN(SSN);
        setAddress(address);
        setCity(city);
        setState(state);
        setZip(zip);
        setFirstName(firstName);
        setLastName(lastName);
        customerIDCounter++;
        setCustomerID(customerIDCounter);

        // List and Map management
        customersList.add(this);
        customerMap.put(customerID, this);
    }


    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    // Gets a customer from the arraylist
    public Customer getCustomerArrayList() {
        for (Customer customer : customersList) {
            if (customer.getCustomerID() == customerID) {
                return customer;
            }
        }
        return null; // If no customer could be found return null
    }

    // Gets a customer from the HashMap
    // Faster than an arrayList search, however this may also not be needed based on how we search
    public Customer getCustomerHashMap() {
        return customerMap.get(customerID);
    }

    // Removes a customer from the customer data
    // !!! This functionality will also need to include accounts at some point, but basic for now
    public void removeCustomer(Customer customer) {
        // Handle list
        for (Customer c : customersList) {
            if (c.getCustomerID() == customer.getCustomerID()) {
                customersList.remove(c);
            }
        }

        // Handle map
        customerMap.remove(customerID);
    }
}
