import java.lang.reflect.Method;
import java.util.Date;

/**
 * The "generalized" account class which holds the most basic information and methods all account types need to have
 */
abstract public class AbstractAccount {
    // Data
    protected String customerID;
    protected Date accountCreationDate;
    protected String accountType;
    static long accountIDCounter = 0;
    long accountID; // Accounts need IDs so if a customer holds multiple of the same type the correct one can be retrieved

    // Methods
    public AbstractAccount(String customerID, Date accountCreationDate, String accountType) {
        this.customerID = customerID;
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
        incrementAccountIDCounter();
        this.accountID = accountIDCounter;
    }

    // Constructor used when accounts are being restored, meaning they have already been created
    public AbstractAccount(String customerID, Date accountCreationDate, String accountType, long accountID) {
        this.customerID = customerID;
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
        this.accountID = accountID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public abstract String getAccountType();

    public long getAccountID() {
        return accountID;
    }

    // This is needed for when the database restores, ensuring the accountID counter is correctly setup
    // If this wasn't used, accounts would start at 0 again,
    // which would be an issue because of how overdraft accounts are implemented
    public static void incrementAccountIDCounter() {
        accountIDCounter++;
    }

    // These are used for database manipulation
    /*
    Look in the Database class for specific details
    toFileString is a non-static function which every class implements, this is easy to write
    fromFileString works better as a static function, which makes its implementation messier
     */
    public abstract String toFileString();
    public static AbstractAccount fromFileString (String line, Class<? extends AbstractAccount> clazz) {
        try {
            Method fromFileString = clazz.getDeclaredMethod("fromFileString", String.class);
            return (AbstractAccount) fromFileString.invoke(null, line);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // !!! Some error message
        return null;
    }
}
