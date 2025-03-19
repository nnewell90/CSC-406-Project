import java.util.Date;

/**
 * The "generalized" account class which holds the most basic information and methods all account types need to have
 */
abstract public class AbstractAccount {
    // Data
    protected String customerID = ""; // -1 should be seen as an error for IDs
    protected Date accountCreationDate;
    protected String accountType;
    static long accountIDCounter = 0;
    long accountID; // Accounts need IDs so if a customer holds multiple of the same type the correct one can be retrieved

    // Methods
    public AbstractAccount(String customerID, Date accountCreationDate, String accountType) {
        this.customerID = customerID;
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
        accountIDCounter++;
        this.accountID = accountIDCounter;
    }

    public String getCustomerID() {
        return customerID;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public abstract String getAccountType();

    // These are used for database manipulation
    /*
    Look in the Database class for specific details
    These don't require any arguments because I think it makes working with the various accounts easier
     */
    public abstract String toFileString();
    public abstract AbstractAccount fromFileString();
}
