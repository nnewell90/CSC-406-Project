import java.util.Date;

/**
 * The "generalized" account class which holds the most basic information and methods all account types need to have
 */
abstract public class AbstractAccount {
    // Data
    protected int customerID = -1; // -1 should be seen as an error for IDs
    protected Date accountCreationDate;
    protected String accountType;

    // Methods
    public AbstractAccount(int customerID, Date accountCreationDate, String accountType) {
        this.customerID = customerID;
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
    }

    public int getCustomerID() {
        return customerID;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public abstract String getAccountType();

    // These are used for database manipulation
    public abstract String toFileString();
    public abstract AbstractAccount fromFileString();
}
