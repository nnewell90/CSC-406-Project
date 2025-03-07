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

    // Holds the accounts that each customer owns
    ArrayList<AbstractAccount> customerAccounts = new ArrayList<>();

    // Constructor
    public Customer(String SSN, String address, String city, String state, String zip, String firstName, String lastName) {
        // First search to make sure a duplicate user isn't being created!

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

    // Adds an account to the accounts customers have
    public void addAccountToCustomerAccounts(AbstractAccount account) {
        customerAccounts.add(account);
    }
}
