import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class holds all "database" information for the banking system
 */
public class Database implements Runnable {
    // Various databases entries we need
        // This is probably way more than we reasonably need, but I am "over creating" for now
        // and we can trim what we don't need later on
    // Accounts
    // Abstract accounts
    static ArrayList<AbstractAccount> abstractAccountList = new ArrayList<>();

    // Savings accounts
    static ArrayList<SavingsAccount> savingsAccountList = new ArrayList<>();
    static ArrayList<SavingsAccount.SimpleSavingsAccount> simpleSavingsAccountList = new ArrayList<>();
    static ArrayList<SavingsAccount.CDSavingsAccount> cdSavingsAccountList = new ArrayList<>();

    // Checking accounts
    // TMB and GoldDiamond accounts share enough functionality that we just need one list
    static ArrayList<CheckingAccount> checkingAccountList = new ArrayList<>();

    // Loan accounts
    static ArrayList<LoanAccount> loanAccountList =  new ArrayList<>();
    static ArrayList<LoanAccount.ShortOrLong> shortOrLongLoanList = new ArrayList<>();
    static ArrayList<LoanAccount.CC> CCList = new ArrayList<>();

    // Customer and ATM
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<ATMCard> atmCardList = new ArrayList<>();

    // File names for the databases
    static String abstractAccounts = "abstractAccounts.txt";

    static String savingAccounts = "savingAccounts.txt";
    static String simpleSavingsAccounts = "simpleSavingsAccounts.txt";
    static String cdSavingsAccounts = "cdSavingsAccounts.txt";

    static String checkingAccounts = "checkingAccounts.txt";

    static String loanAccounts = "loanAccounts.txt";
    static String shortOrLongLoans = "shortOrLongLoans.txt";
    static String CCAccounts = "CCAccounts.txt";

    static String customers = "customers.txt";
    static String atmCards = "atmCards.txt";

    // Methods
    public Database() {

    }

    // Lock to ensure that when data is read from and written to database files
    // race conditions can't occur
    static Lock lock = new ReentrantLock();

    // This needs to save the database every few minutes
    @Override
    public void run() {
        storeToDatabase();
    }

    // Functions for adding and removing accounts from given lists
    // When working with Swing we may want to change these to return a String or some other Swing type
    // For now just a println() placeholder
    public static <T>void addItemToList(ArrayList<T> list, T item) {
        if (!list.contains(item)) {
            list.add(item);

            // If this is an Account, add it to the abstractAccountList, which is used for searching elsewhere
            if (item instanceof AbstractAccount) {
                if (!abstractAccountList.contains(item)) { // Safety check to make sure there aren't duplicates
                    abstractAccountList.add((AbstractAccount) item);
                }
            }

        } else {
            // This will need to be changed to work with Swing
            System.out.println("Item already exists in the database: Doing nothing");
        }
    }

    public static <T>void removeItemFromList(ArrayList<T> list, T item) {
        if (list.contains(item)) {
            list.remove(item);
        } else {
            // This will need to be changed to work with Swing
            System.out.println("Item does not exist in the database: Cannot remove item");
        }
    }

    // Gets an account from a given list
    // The given list must be from a type that extends from AbstractAccount
    public static <T extends AbstractAccount> AbstractAccount getAccountFromList(ArrayList<T> accountList, long accountID) {
        // Search through the given list for the given ID, then return the matching account
        for (AbstractAccount account : accountList) {
            if (account.accountID == accountID) {
                return account;
            }
        }
        // We have been given some ID for an account that doesn't exist
        // Probably give some warning message here
        return null;
    }

    // Gets a customer from the customer list
    public static Customer getCustomerFromList(String customerID) {
        for (Customer customer : customerList) {
            if (customer.customerID.equals(customerID)) {
                return customer;
            }
        }

        // Some invalid ID was given for a customer
        // Probably give some warning message here
        return null;
    }

    // Gets a ATMCard from the ATMCard list
    public static ATMCard getATMCardFromList(long accountID) {
        for (ATMCard c : atmCardList) {
            if (c.getAccountID() == accountID) {
                return c;
            }
        }

        // Some invalid ID was given for a customer
        // Probably give some warning message here
        return null;
    }


    // Gets all the accounts for one customer
    public static ArrayList<AbstractAccount> getAllAccountsOfCustomer(ArrayList<Long> customerAccountIDs) {
        ArrayList<AbstractAccount> toReturn = new ArrayList<>();
        for (Long ID : customerAccountIDs) {
            for (AbstractAccount a : abstractAccountList) {
                if (a.getAccountID() == ID) {
                    toReturn.add(a);
                    break;
                }
            }
        }

        return toReturn;
    }

    // Restores the internal accounts of each customer to their associated objects
    // This function is called when information needs to be restored from the database
    /*
    This will also set the AbstractAccount field accountIDCounter to the next highest value
    Since this function runs on every account, it is convenient to do it here
     */
    private static void restoreCustomerAccounts() {
        // This is inefficient with the way I have written the database, but I'm leaving it for now just to finish it

        // Savings accounts
        // Simple savings
        for (SavingsAccount.SimpleSavingsAccount a : simpleSavingsAccountList) { // Check every entry in the list
            if (a.getAccountID() > AbstractAccount.accountIDCounter) {
                AbstractAccount.accountIDCounter = a.getAccountID();
            }
            for (Customer c : customerList) { // Check for every customer
                if (a.getCustomerID().equals(c.getCustomerID())) {
                    c.addAccountToCustomerAccounts(a.getAccountID());
                    break; // Once a match has been found, move to the next account in the accountList
                }
            }
        }
        // CD
        for (SavingsAccount.CDSavingsAccount a : cdSavingsAccountList) { // Check every entry in the list
            if (a.getAccountID() > AbstractAccount.accountIDCounter) {
                AbstractAccount.accountIDCounter = a.getAccountID();
            }
            for (Customer c : customerList) { // Check for every customer
                if (a.getCustomerID().equals(c.getCustomerID())) {
                    c.addAccountToCustomerAccounts(a.getAccountID());
                    break; // Once a match has been found, move to the next account in the accountList
                }
            }
        }

        // Checking accounts
        for (CheckingAccount a : checkingAccountList) { // Check every entry in the list
            if (a.getAccountID() > AbstractAccount.accountIDCounter) {
                AbstractAccount.accountIDCounter = a.getAccountID();
            }
            for (Customer c : customerList) { // Check for every customer
                if (a.getCustomerID().equals(c.getCustomerID())) {
                    c.addAccountToCustomerAccounts(a.getAccountID());
                    break; // Once a match has been found, move to the next account in the accountList
                }
            }
        }

        // Loan accounts
        // ShortOrLong
        for (LoanAccount.ShortOrLong a : shortOrLongLoanList) { // Check every entry in the list
            if (a.getAccountID() > AbstractAccount.accountIDCounter) {
                AbstractAccount.accountIDCounter = a.getAccountID();
            }
            for (Customer c : customerList) { // Check for every customer
                if (a.getCustomerID().equals(c.getCustomerID())) {
                    c.addAccountToCustomerAccounts(a.getAccountID());
                    break; // Once a match has been found, move to the next account in the accountList
                }
            }
        }
        // CC
        for (LoanAccount.CC a : CCList) { // Check every entry in the list
            if (a.getAccountID() > AbstractAccount.accountIDCounter) {
                AbstractAccount.accountIDCounter = a.getAccountID();
            }
            for (Customer c : customerList) { // Check for every customer
                if (a.getCustomerID().equals(c.getCustomerID())) {
                    c.addAccountToCustomerAccounts(a.getAccountID());
                    break; // Once a match has been found, move to the next account in the accountList
                }
            }
        }
        // Finally, increment the AccountIDCounter by one so the next account can be made correctly
        AbstractAccount.incrementAccountIDCounter();
    }

    // The function for restoring information from the database (.txts) back to the system
    public static void restoreFromDatabase() {
        lock.lock();
        // Accounts
        loadFromFile(abstractAccounts, abstractAccountList, AbstractAccount.class);

        loadFromFile(savingAccounts, savingsAccountList, SavingsAccount.class);
        loadFromFile(simpleSavingsAccounts, simpleSavingsAccountList, SavingsAccount.SimpleSavingsAccount.class);
        loadFromFile(cdSavingsAccounts, cdSavingsAccountList, SavingsAccount.CDSavingsAccount.class);

        loadFromFile(checkingAccounts, checkingAccountList, CheckingAccount.class);

        loadFromFile(loanAccounts, loanAccountList, LoanAccount.class);
        loadFromFile(shortOrLongLoans, shortOrLongLoanList, LoanAccount.ShortOrLong.class);
        loadFromFile(CCAccounts, CCList, LoanAccount.CC.class);

        // Non-accounts
        loadFromFile(customers, customerList, Customer.class);
        loadFromFile(atmCards, atmCardList, ATMCard.class);

        // Each customer has an arrayList of their own accounts, restore that list so they can view it
        restoreCustomerAccounts();
        lock.unlock();
    }

    // The function for storing information to the database (.txts) from the system
    public static void storeToDatabase() {
        lock.lock();
        storeToFile(abstractAccounts, abstractAccountList, AbstractAccount.class);

        storeToFile(savingAccounts, savingsAccountList, SavingsAccount.class);
        storeToFile(simpleSavingsAccounts, simpleSavingsAccountList, SavingsAccount.SimpleSavingsAccount.class);
        storeToFile(cdSavingsAccounts, cdSavingsAccountList, SavingsAccount.CDSavingsAccount.class);

        storeToFile(checkingAccounts, checkingAccountList, CheckingAccount.class);

        storeToFile(loanAccounts, loanAccountList, LoanAccount.class);
        storeToFile(shortOrLongLoans, shortOrLongLoanList, LoanAccount.ShortOrLong.class);
        storeToFile(CCAccounts, CCList, LoanAccount.CC.class);

        storeToFile(customers, customerList, Customer.class);
        storeToFile(atmCards, atmCardList, ATMCard.class);
        lock.unlock();
    }

    // Helper functions so the store and restore functions can be a little shorter
    /*
    How this works
    Create a buffered reader and a line for it
    Get the method for reading from a string that is in file format, which means delimited, from the class for the given list
    For each line in the given list, call that method on the line, turning the delimited line into an object
    Add that item to the list in the database

    getDeclaredMethod: Gets a declared method from a given class, the String.class is necessary as the function expects a String parameter
    .invoke(): Basically just calls the function from getDeclaredMethod
        fromFileString is called on a string to turn that string into an object, so line is the first parameter
        The second parameter is just a dummy parameter because I was getting a warning about needing a second parameter for invoke()
     */
    private static <T>void loadFromFile(String fileName, ArrayList<T> list, Class<?> clazz) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            Method fromFileString = clazz.getDeclaredMethod("fromFileString", String.class); // !!! Every account class must include the functon "public ClassName fromFileString(){...}"
            while ((line = reader.readLine()) != null) {
                T temp = (T) fromFileString.invoke(null, line);
                list.add(temp);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading from file: " + fileName + " :"+ e.getMessage());
        }
    }

    // Writes to a .txt file
    /*
    Similar logic to above but a print writer rather than a buffered reader
     */
    private static <T>void storeToFile(String fileName, ArrayList<T> list, Class<?> clazz) {
        // Class clazz = list.getFirst().getClass();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            Method toFileString = clazz.getDeclaredMethod("toFileString"); // !!! Same as above but for this function
            for (T item : list) {
                String temp = (String) toFileString.invoke(item);
                writer.println(temp);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error storing to file: " + fileName + " :" + e.getMessage());
        }
    }

    //Added this method so I could grab a specific customer from the
    //customerList arrayList.
    public static Customer getCustomer(String ssn) {
        for (Customer c : customerList) {
            if (Objects.equals(c.getSSN(), ssn)) {
                return c;
            }
        }
        return null;
    }
}
