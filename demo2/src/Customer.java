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
    String customerID;

    // Holds the accounts that each customer owns
    ArrayList<Long> customerAccountIDs = new ArrayList<>();

    // For safe deletion
    boolean isDeleted = false;

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
        setCustomerID(SSN);
        customerAccountIDs = new ArrayList<>();
    }

    // Deletes a customer from the entire system, including the database
    public static void deleteCustomer(Customer customer) {
        if (customer.isDeleted()) {
            return;
        }

        // Remove each account the customer has from the database
        ArrayList<AbstractAccount> accounts = customer.getCustomerAccountsAsAccounts();

        for (AbstractAccount account : accounts) {
            switch (account.getAccountType()) {
                // Checking
                case CheckingAccount -> CheckingAccount.deleteAccount((CheckingAccount) account);
                // Savings
                case SimpleSavingsAccount -> SavingsAccount.SimpleSavingsAccount.deleteAccount((SavingsAccount.SimpleSavingsAccount) account);
                case CDSavingsAccount -> SavingsAccount.CDSavingsAccount.deleteAccount((SavingsAccount.CDSavingsAccount) account);
                // Loan
                case CCLoanAccount -> LoanAccount.CC.deleteAccount((LoanAccount.CC)account);
                case ShortOrLongLoanAccount -> LoanAccount.ShortOrLong.deleteAccount((LoanAccount.ShortOrLong)account);
            }
        }

        // Remove the account from the lists in the database
        Database.removeItemFromList(Database.customerList, customer);

        // Finally, fully delete the account
        customer.setDeleted(true);
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

    public String getCustomerID() {
        if (isDeleted()) { // This is the only getter that will check this since it is used often
            return null;
        }
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // These two are for database purposes
    public String toFileString() {
        if (isDeleted) {
            return null;
        }
        String toReturn = "";

        // Basic data
        toReturn += getSSN();
        toReturn += ";" + getAddress();
        toReturn += ";" + getCity();
        toReturn += ";" + getState();
        toReturn += ";" + getZip();
        toReturn += ";" + getFirstName();
        toReturn += ";" + getLastName();
        toReturn += ";" + getCustomerID();

        // Customer's accounts (Uses IDs)
        for (Long accountID : customerAccountIDs) {
            toReturn += ";" + accountID;
        }

        return toReturn;
    }

    public static Customer fromFileString(String s) {
        String[] split = s.split(";");

        // Basic data
        String SSN = split[0];
        String address = split[1];
        String city = split[2];
        String state = split[3];
        String zip = split[4];
        String firstName = split[5];
        String lastName = split[6];
        // String customerID = split[7]; // This is set in the constructor

        // Customer accounts are restored from the database, don't do it here

        return new Customer(SSN, address, city, state, zip, firstName, lastName);
    }

    // Adds an account to the accounts customers have
    public void addAccountToCustomerAccounts(long accountID) {
        if (isDeleted()) {
            return;
        }
        customerAccountIDs.add(accountID);
    }

    public void removeAccountFromCustomerAccounts(long accountID) {
        if (isDeleted()) {
            return;
        }
        customerAccountIDs.remove(accountID);
    }

    // Gets the entire list of customer accounts as IDs
    public ArrayList<Long> getCustomerAccountIDs() {
        if (isDeleted()) {
            return null;
        }
        return customerAccountIDs;
    }

    // Gets the entire list of customer accounts as accounts
    public ArrayList<AbstractAccount> getCustomerAccountsAsAccounts() {
        if (isDeleted()) {
            return null;
        }
        return Database.getAllAccountsOfCustomer(customerAccountIDs);
    }
}
