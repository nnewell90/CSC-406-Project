import java.util.ArrayList;
import java.util.Date;

public class SavingsAccount extends AbstractAccount {
    // Data
    double balance;
    CheckingAccount overdraftForAccount; // The checking account this account is an overdraft for
    ArrayList<String> stopPaymentArray; // Used to stop checks from going through

    public SavingsAccount(String customerID, Date accountCreationDate, String accountType, double initialBalance) {
        super(customerID, accountCreationDate, accountType);
        setBalance(initialBalance);
        stopPaymentArray = new ArrayList<>();
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public String toFileString() {
        return "";
    }

    @Override
    public SavingsAccount fromFileString() {
        SavingsAccount temp = null;

        // Logic once we have everything necessary for savings accounts

        return temp;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance > amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient Balance");
        }
    }

    public void setOverdraftForAccount(CheckingAccount overdraftForAccount) {
        this.overdraftForAccount = overdraftForAccount;
    }

    public CheckingAccount getOverdraftForAccount() {
        return overdraftForAccount;
    }

    public void removeOverdraftForAccount() {
        this.overdraftForAccount = null;
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
                balance -= 25; // $25 charge
                System.out.println("Insufficient funds: Check returned unpaid, $25 Overdraft Service charged to account");
            } else {
                balance -= withdrawAmount;
            }

        }
    }

    // Simple Savings class
    static class SimpleSavingsAccount extends SavingsAccount {

        // Data
        double interestRate;

        public SimpleSavingsAccount(String customerID, Date accountCreationDate, int initialBalance) {
            super(customerID, accountCreationDate, "Simple Savings Account", initialBalance);
        }

        @Override
        public String getAccountType() {
            return "Simple Savings Account";
        }

        @Override
        public String toFileString() {
            return "";
        }

        @Override
        public SimpleSavingsAccount fromFileString() {
            SimpleSavingsAccount temp = null;

            // Logic once we have everything necessary for savings accounts

            return temp;
        }

        public double getBalance() {
            return super.getBalance();
        }

        public double getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(double interestRate) {
            this.interestRate = interestRate;
        }

        // Calculates and adds the interest for am account
        public void calcAndAddInterest() {
            balance += balance * interestRate;
        }

        public void deposit(int amount) {
            super.deposit(amount);
        }

        public void withdraw(int amount) {
            super.withdraw(amount);
        }
    }

    static class CDSavingsAccount extends SavingsAccount {

        // Data
        double interestRate;
        Date dueDate;

        public CDSavingsAccount(String customerID, Date accountCreationDate, int initialBalance, Date dueDate) {
            super(customerID, accountCreationDate, "CD", initialBalance);
            this.dueDate = dueDate;
        }

        @Override
        public String getAccountType() {
            return "CD Savings Account";
        }

        @Override
        public String toFileString() {
            return "";
        }

        @Override
        public CDSavingsAccount fromFileString() {
            CDSavingsAccount temp = null;

            // Logic once we have everything necessary for savings accounts

            return temp;
        }

        public double getBalance() {
            return super.getBalance();
        }

        public double getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(double interestRate) {
            this.interestRate = interestRate;
        }

        // Calculates and adds the interest for am account
        public void calcAndAddInterest() {
            balance += balance * interestRate;
        }

        public void withdraw(int amount) {
            // !!! The paper specifies some punishment for withdrawing early, but it doesn't state what that is
            super.withdraw(amount);
        }
    }
}
