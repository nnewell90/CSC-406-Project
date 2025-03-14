import java.util.ArrayList;
import java.util.Date;

public class CheckingAccount extends AbstractAccount{
    // Data
    double balance;
    final int minimumBalanceGoldDiamond = 5000;
    double interestRate; // For GoldDiamond accounts

    AccountType accountSpecificType;
    ArrayList<String> stopPaymentArray;

    SavingsAccount overdraftAccount; // The overdraft account for this Checking account


    public enum AccountType {
        TMB,
        GoldDiamond // Minimum balance needs to be enforced somehow, not sure where
    }

    public CheckingAccount(String customerID, Date accountCreationDate, String accountType, double initialBalance, AccountType type) {
        // Some logic for checking if an account with type GoldDiamond has the minimum funds
        // This could also be done at the end, just depends on how we want to implement it

        super(customerID, accountCreationDate, accountType);
        setBalance(initialBalance);
        setAccountSpecificType(type);
        stopPaymentArray = new ArrayList<>();
        overdraftAccount = null;
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
        if (accountSpecificType == AccountType.TMB) {
            balance -= 0.75; // Transaction fee
        }
    }

    public void withdraw(double amount) {

        if (balance >= amount) {
            balance -= amount;

        } else {
            System.out.println("Insufficient Balance"); // !!! Swing will need to change this
        }
    }

    // Not 100% sure how this needs to work, for now just a simple function
    public void transfer(double amount /*, AbstractAccount transferToAccount*/) {
        if (balance >= amount) {
            balance -= amount;
            balance -= 1.25; // Transfer fee
        } else {
            System.out.println("Insufficient Balance"); // !!! Swing will need to change this
        }
    }

    // Calculates and adds the interest for a GoldDiamond account
    public void calcAndAddInterest() {
        balance += balance * interestRate;
    }

    // This expects to be given SavingsAccounts interest rate, it does the x0.5 itself
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate * 0.5;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setAccountSpecificType(AccountType accountType) {
        this.accountSpecificType = accountType;
    }

    public String getAccountSpecificType() {
        return accountType;
    }

    // Overdraft setting
    public void setOverdraftForAccount(SavingsAccount overdraftAccount) {
        this.overdraftAccount = overdraftAccount;
        overdraftAccount.setOverdraftForAccount(this);
    }

    public SavingsAccount getOverdraftAccount() {
        return overdraftAccount;
    }

    public void removeOverdraftAccount() {
        overdraftAccount.setOverdraftForAccount(null);
        this.overdraftAccount = null;
    }

    // Check stuff
    // Add this check to the array of checks to stop payments for
    public void addStopPaymentNumber(String checkNumber) {

        boolean validNumber = validateCheckNumber(checkNumber);

        // Now actually add a number
        if (validNumber) {
            stopPaymentArray.add(checkNumber);
            balance -= 25; // $25 charge
        } else {
            System.out.println("Invalid Check Number or number already stored");
        }
    }

    // checks a checkNumber's validity
    public boolean validateCheckNumber(String checkNumber) {
        boolean validNumber = true;

        // Do some checks on the given checkNumber
        if (stopPaymentArray.contains(checkNumber)) { // If the array already has this number
            validNumber = false;
        } else if (checkNumber.length() == 1) { // !!! This will need to change for actual check numbers
            validNumber = false;
        } else if (checkNumber.matches("[a-zA-Z]+") || checkNumber.matches("[-_]+")) { // Contains a letter, - , or _
            validNumber = false;
        }

        // Return true or false
        return validNumber;
    }

    // Withdraw via a check rather than by card
    public void withdrawByCheck(int withdrawAmount, String checkNumber) {
        boolean validNumber = validateCheckNumber(checkNumber);
        boolean stopPayment = false;

        // Check if the check is valid and if it is contained in the "stop payment" list
        if (!validNumber) {
            stopPayment = true;
            System.out.println("Invalid Check Number");
        }
        if (stopPaymentArray.contains(checkNumber)) {
            stopPayment = true;
            System.out.println("Check number has previously been set to not be paid");
        }

        // Now do actual withdrawing
        if (!stopPayment) {

            // First check to see if this will cause an overdraft
            if (withdrawAmount > balance) {

                boolean overdraftFail = false;

                // Check for a SavingsAccount
                if (overdraftAccount != null) {
                    if (overdraftAccount.getBalance() >= withdrawAmount) { // Enough money in the overdraft account
                        // Withdraw from overdraft, deposit in checking, then withdraw from checking
                        overdraftAccount.withdraw(withdrawAmount);
                        deposit(withdrawAmount);
                        balance -= withdrawAmount;
                    } else { // Not enough money in overdraft account
                        overdraftFail = true;
                    }
                } else { // No overdraft account
                    overdraftFail = true;
                }

                // If an overdraft couldn't be resolved
                if (overdraftFail) {
                    balance -= 25; // $25 charge
                    System.out.println("Insufficient funds: Check returned unpaid, $25 Overdraft Service charged to account");
                }

            } else { // Enough funds in Checking to cover amount
                balance -= withdrawAmount;
            }

        }
    }
}
