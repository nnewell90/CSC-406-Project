import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Date;

/**
 * The "generalized" account class which holds the most basic information and methods all account types need to have
 */
abstract public class AbstractAccount {
    // Data
    protected String customerID;
    protected LocalDate accountCreationDate;
    // protected String accountType;
    protected AccountType accountType;
    static long accountIDCounter = 0;
    long accountID; // Accounts need IDs,
    // so if a customer holds multiple of the same type, the correct one can be retrieved

    // This is used to actually delete accounts
    // Used for safe deletion, Java's garbage collector will eventually delete accounts;
    // this is meant to act as a placeholder until it actually does.
    boolean isDeleted = false;

    public void withdrawByCheck(int amt, String num) {

    }

    // The different types of accounts possible in the system
    public enum AccountType {
        // Checking
        CheckingAccount,
        // Savings
        SavingsAccount,
        SimpleSavingsAccount,
        CDSavingsAccount,
        // Loans
        LoanAccount,
        ShortOrLongLoanAccount,
        CCLoanAccount
    }

    // Methods
    public AbstractAccount(String customerID, LocalDate accountCreationDate, AccountType accountType) {
        this.customerID = customerID;
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
        incrementAccountIDCounter();
        this.accountID = accountIDCounter;
        this.isDeleted = false;
    }

    // Constructor used when accounts are being restored, meaning they have already been created
    public AbstractAccount(String customerID, LocalDate accountCreationDate, AccountType accountType, long accountID) {
        this.customerID = customerID;
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
        this.accountID = accountID;
        this.isDeleted = false;
    }

    public String getCustomerID() {
        if (isDeleted()) {
            return null;
        }
        return customerID;
    }

    public LocalDate getAccountCreationDate() {
        if (isDeleted()) {
            return null;
        }
        return accountCreationDate;
    }

    public abstract AccountType getAccountType();

    public long getAccountID() {
        if (isDeleted()) {
            return -1;
        }
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

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setAccountCreationDate(LocalDate accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public static long getAccountIDCounter() {
        return accountIDCounter;
    }

    public static void setAccountIDCounter(long accountIDCounter) {
        AbstractAccount.accountIDCounter = accountIDCounter;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }
}
