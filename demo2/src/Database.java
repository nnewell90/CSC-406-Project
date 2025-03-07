import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * This class holds all "database" information for the banking system
 */
public class Database implements Runnable {
    // Various databases we need
    // Accounts
    static ArrayList<AbstractAccount> abstractAccountList = new ArrayList<>();
    static ArrayList<SavingsAccount> savingsAccountList = new ArrayList<>();
    static ArrayList<CheckingAccount> checkingAccountList = new ArrayList<>();
    static ArrayList<LoanAccount> loanAccountList =  new ArrayList<>();

    // Customer and ATM
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<ATMCard> atmCardList = new ArrayList<>();

    // File names for the databases
    static String abstractAccounts = "abstractAccounts.txt";
    static String savingAccounts = "savingAccounts.txt";
    static String checkingAccounts = "checkingAccounts.txt";
    static String loanAccounts = "loanAccounts.txt";
    static String customers = "customers.txt";
    static String atmCards = "atmCards.txt";

    // Methods
    public Database() {

    }

    // This needs to save the database every few minutes
    @Override
    public void run() {
        storeToDatabase();
    }

    // The function for restoring information from the database (.txts) back to the system
    private static void restoreFromDatabase() {
        loadFromFile(abstractAccounts, abstractAccountList, AbstractAccount.class);
        loadFromFile(savingAccounts, savingsAccountList, SavingsAccount.class);
        loadFromFile(checkingAccounts, checkingAccountList, CheckingAccount.class);
        loadFromFile(loanAccounts, loanAccountList, LoanAccount.class);
        loadFromFile(customers, customerList, Customer.class);
        loadFromFile(atmCards, atmCardList, ATMCard.class);

    }

    // The function for storing information to the database (.txts) from the system
    private static void storeToDatabase() {
        storeToFile(abstractAccounts, abstractAccountList, AbstractAccount.class);
        storeToFile(savingAccounts, savingsAccountList, SavingsAccount.class);
        storeToFile(checkingAccounts, checkingAccountList, CheckingAccount.class);
        storeToFile(loanAccounts, loanAccountList, LoanAccount.class);
        storeToFile(customers, customerList, Customer.class);
        storeToFile(atmCards, atmCardList, ATMCard.class);
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
