import java.util.ArrayList;

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

    // The arrayList which holds all customers
    static ArrayList<Customer> customers = new ArrayList<Customer>();

    public Customer(String SSN, String address, String city, String state, String zip, String firstName, String lastName) {
        setSSN(SSN);
        setAddress(address);
        setCity(city);
        setState(state);
        setZip(zip);
        setFirstName(firstName);
        setLastName(lastName);
        customers.add(this);
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
}
