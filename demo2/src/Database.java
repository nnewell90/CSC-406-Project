import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    public static <T>void addAccountToList(ArrayList<T> list, T account) {
        if (!list.contains(account)) {
            list.add(account);
        } else {
            // This will need to be changed to work with Swing
            System.out.println("Account already exists in the database: Doing nothing");
        }
    }

    public static <T>void removeAccountFromList(ArrayList<T> list, T account) {
        if (list.contains(account)) {
            list.remove(account);
        } else {
            // This will need to be changed to work with Swing
            System.out.println("Account does not exist in the database: Cannot remove account");
        }
    }

    // Gets an account from a given list
    // !!! work on this
    public static <T> AbstractAccount getAccountFromList(ArrayList<T> accountList, int customerID) {
        /*
        I'm not certain how to implement this.
        This function aims to get one account from a given list of accounts
        However if a customer holds multiple accounts of the same type there will need to be a way to
        show which one
        However, this function could instead be rewritten to instead return a list of all accounts a customer
        has of the given type, and then Swing could provide a dropdown menu (or something similar) to look
        at specific accounts
        The Customer class has an arrayList of accounts that each customer owns that could be used,
        but that could be a pain to work with; we can decide whether we want to try and use that or just
        search through the database's arrayLists for accounts the user owns

        I also added in an accountID in the AbstractAccount class so each account gets a unique ID
        This might be able to be used for this (and other searching), but I'm not certain
         */

        return null;
    }

    // The function for restoring information from the database (.txts) back to the system
    private static void restoreFromDatabase() {
        lock.lock();
        loadFromFile(abstractAccounts, abstractAccountList, AbstractAccount.class);

        loadFromFile(savingAccounts, savingsAccountList, SavingsAccount.class);
        loadFromFile(simpleSavingsAccounts, simpleSavingsAccountList, SavingsAccount.SimpleSavingsAccount.class);
        loadFromFile(cdSavingsAccounts, cdSavingsAccountList, SavingsAccount.CDSavingsAccount.class);

        loadFromFile(checkingAccounts, checkingAccountList, CheckingAccount.class);

        loadFromFile(loanAccounts, loanAccountList, LoanAccount.class);
        loadFromFile(shortOrLongLoans, shortOrLongLoanList, LoanAccount.ShortOrLong.class);
        loadFromFile(CCAccounts, CCList, LoanAccount.CC.class);

        loadFromFile(customers, customerList, Customer.class);
        loadFromFile(atmCards, atmCardList, ATMCard.class);
        lock.unlock();
    }

    // The function for storing information to the database (.txts) from the system
    private static void storeToDatabase() {
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
    private static <T>void loadFromFile(String fileName, ArrayList<T> list, Class<T> clazz) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            Method fromFileString = clazz.getDeclaredMethod("fromFileString", String.class); // !!! Every account class must include the functon "public static ClassName fromFileString(String data){...}"
            while ((line = reader.readLine()) != null) {
                T temp = (T) fromFileString.invoke(line, (Object) null);
                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("Error loading from file: " + fileName + " :"+ e.getMessage());
        }
    }

    // Writes to a .txt file
    /*
    Similar logic to above but a print writer rather than a buffered reader
     */
    private static <T>void storeToFile(String fileName, ArrayList<T> list, Class<T> clazz) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            Method toFileString = clazz.getDeclaredMethod("toFileString", clazz); // !!! Same as above but for this function
            for (T item : list) {
                String temp = (String) toFileString.invoke(item, (Object) null);
                writer.println(temp);
            }
        } catch (Exception e) {
            System.out.println("Error storing to file: " + fileName + " :" + e.getMessage());
        }
    }
}
