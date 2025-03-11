import java.util.Date;

public class CheckingAccount extends AbstractAccount{
    // Data
    double balance;
    final int minimumBalanceGoldDiamond = 5000;
    AccountType accountSpecificType;

    public enum AccountType {
        TMB,
        GoldDiamond
    }

    public CheckingAccount(int customerID, Date accountCreationDate, String accountType, double initialBalance, AccountType type) {
        // Some logic for checking if an account with type GoldDiamond has the minimum funds
        // This could also be done at the end, just depends on how we want to implement it

        super(customerID, accountCreationDate, accountType);
        setBalance(initialBalance);
        setAccountSpecificType(type);
    }

    public static void deleteAccount(CheckingAccount account) {
        // Functionality for deleting an account
    }

    @Override
    public String getAccountType() {
        return "";
    }

    @Override
    public String toFileString() {
        return "";
    }

    @Override
    public CheckingAccount fromFileString() {
        CheckingAccount temp = null;

        // Logic once we have everything necessary for savings accounts

        return temp;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient Balance"); // !!! Swing will need to change this
        }
    }

    public void setAccountSpecificType(AccountType accountType) {
        this.accountSpecificType = accountType;
    }

    public String getAccountSpecificType() {
        return accountType;
    }
}
