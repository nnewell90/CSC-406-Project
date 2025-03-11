import java.util.Date;

public class CheckingAccount extends AbstractAccount{
    // Data
    double balance;
    final int minimumBalanceGoldDiamond = 5000;
    AccountType accountSpecificType;

    //ATM card field to be able to choose if the customer wants an ATM card with their checking out
    private ATMCard atmCard;

    public enum AccountType {
        TMB,
        GoldDiamond
    }


    public CheckingAccount(int customerID, Date accountCreationDate, String accountType, double initialBalance, AccountType type, Customer customer, int atmCardChoice) {
        // Some logic for checking if an account with type GoldDiamond has the minimum funds
        // This could also be done at the end, just depends on how we want to implement it

        super(customerID, accountCreationDate, accountType);
        setBalance(initialBalance);
        setAccountSpecificType(type);

        if(atmCardChoice == 1){ //if ATM card choice is 1, then the customer gets an ATMCard
            this.atmCard = new ATMCard(customer, this);
        }
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

    //Checks account to see if they have an atm card
    public boolean hasATMCard(){
        return atmCard != null;
    }

    public ATMCard getAtmCard(){
        return atmCard;
    }

    // toString Method to show who all has what types of accounts, date opened, and how much their initial deposit was
    public String toString(){
        return "Customer: " + customerID + " Has opened an account on: " + accountCreationDate + " Account Type: " + accountType + " Initial Deposit: " + balance +
                " Account Level: " + accountSpecificType + " ATM Card: " + (hasATMCard() ? " Yes " : " No ");
    }
}
