import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SavingsAccount extends AbstractAccount {
    // Data
    double balance;

    // Normal constructor
    public SavingsAccount(String customerID, Date accountCreationDate, AccountType accountType, double initialBalance) {
        super(customerID, accountCreationDate, accountType);
        setBalance(initialBalance);
    }

    // Restore from database constructor
    public SavingsAccount(String customerID, Date accountCreationDate, AccountType accountType, double balance, long accountID) {
        super(customerID, accountCreationDate, accountType, accountID);
        setBalance(balance);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SavingsAccount;
    }

    @Override
    public String toFileString() {
        String toReturn = "";

        // Abstract account information
        toReturn += getCustomerID();
        toReturn += ";" + getAccountCreationDate();
        toReturn += ";" + getAccountType();
        toReturn += ";" + getAccountID();

        // Specific account information
        toReturn += ";" + getBalance();

        return toReturn;
    }


    public static SavingsAccount fromFileString(String s) {
        String[] split = s.split(";");

        // Abstract stuff
        String customerID = split[0];
        Date accountCreationDate = new Date(Long.parseLong(split[1]));
        AccountType abstractAccountType = AccountType.valueOf(split[2]);
        long accountID = Long.parseLong(split[3]);

        // Specific
        double balance = Double.parseDouble(split[4]);

        return new SavingsAccount(customerID, accountCreationDate, abstractAccountType, balance, accountID);
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

    // Simple Savings class
    public static class SimpleSavingsAccount extends SavingsAccount {

        // Data
        double interestRate;
        // CheckingAccount overdraftForAccount; // The checking account this account is an overdraft for
        long overdraftForAccountID; // ID for the checking account this account is an overdraft for
        ArrayList<String> stopPaymentArray; // Used to stop checks from going through

        public SimpleSavingsAccount(String customerID, Date accountCreationDate, int initialBalance) {
            super(customerID, accountCreationDate, AccountType.SimpleSavingsAccount, initialBalance);
            overdraftForAccountID = -1;
            stopPaymentArray = new ArrayList<>();
        }

        public SimpleSavingsAccount(String customerID, Date accountCreationDate, AccountType abstractAccountType, long accountID, double balance, double interestRate, long overdraftAccountID, ArrayList<String> stopPaymentArrayPassed) {
            super(customerID, accountCreationDate, abstractAccountType, balance, accountID);
            setBalance(balance);
            setInterestRate(interestRate);
            stopPaymentArray = new ArrayList<>(stopPaymentArrayPassed);
            if (overdraftAccountID > -1) {
                overdraftForAccountID = overdraftAccountID;
            } else {
                overdraftForAccountID = -1;
            }
        }

        // Deletes an account from the entire system, including the database
        public static void deleteAccount(SimpleSavingsAccount account) {
            // Unlink the account from an overdraft account if one exists
            if (account.overdraftForAccountID != -1) {
                CheckingAccount overdraftAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, account.overdraftForAccountID);
                overdraftAccount.removeOverdraftAccount();
            }

            // Remove the account from the list of customer accounts
            Customer customer = Database.getCustomerFromList(account.getCustomerID());
            if (customer != null) {
                customer.removeAccountFromCustomerAccounts(account.getAccountID());
            }

            // Remove the account from the lists in the database
            Database.removeItemFromList(Database.simpleSavingsAccountList, account);
            Database.removeItemFromList(Database.abstractAccountList, account);

            // Finally, fully delete the account
            account = null;
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.SimpleSavingsAccount;
        }

        @Override
        public String toFileString() {
            String toReturn = "";

            // Abstract account information
            toReturn += getCustomerID();
            toReturn += ";" + getAccountCreationDate();
            toReturn += ";" + getAccountType();
            toReturn += ";" + getAccountID();

            // Specific account information
            toReturn += ";" + getBalance();
            toReturn += ";" + getInterestRate();
            if (overdraftForAccountID != -1) {
                toReturn += ";" + overdraftForAccountID;
            } else {
                toReturn += ";" + "-1";
            }
            for (String s : stopPaymentArray) {
                toReturn += ";" + s;
            }

            return toReturn;
        }


        public static SimpleSavingsAccount fromFileString(String s) {
            String[] split = s.split(";");

            // Abstract stuff
            String customerID = split[0];
            Date accountCreationDate = new Date(Long.parseLong(split[1]));
            AccountType abstractAccountType = AccountType.valueOf(split[2]);
            long accountID = Long.parseLong(split[3]);

            // Specific
            double balance = Double.parseDouble(split[4]);
            double interestRate = Double.parseDouble(split[5]);
            long overdraftAccountID = -1; // Have to do it this way because of later comparison
            overdraftAccountID = Long.parseLong(split[6]);

            // Now make an aList of the held stopped checks
            ArrayList<String> stopPaymentArrayPassed = new ArrayList<>();
            if (split.length > 7) {
                stopPaymentArrayPassed.addAll(Arrays.asList(split).subList(7, split.length));
            }

            return new SimpleSavingsAccount(customerID, accountCreationDate, abstractAccountType, accountID, balance, interestRate, overdraftAccountID, stopPaymentArrayPassed);
        }

        public void setOverdraftForAccount(long overdraftForAccountID) {
            CheckingAccount checkingAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, overdraftForAccountID);
            if (checkingAccount != null) {
                this.overdraftForAccountID = overdraftForAccountID;
            } else {
                // Some fail message
            }
        }

        public long getOverdraftForAccountID() {
            return overdraftForAccountID;
        }

        public CheckingAccount getOverdraftForAccount() {
            if (overdraftForAccountID != -1) {
                return (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, overdraftForAccountID);
            } else {
                // Some fail message
                return null;
            }
        }

        public void removeOverdraftForAccount() {
            overdraftForAccountID = -1;
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

            // Now do the actual withdrawing
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

    public static class CDSavingsAccount extends SavingsAccount {

        // Data
        double interestRate;
        Date dueDate;

        public CDSavingsAccount(String customerID, Date accountCreationDate, double initialBalance, double interestRate, Date dueDate) {
            super(customerID, accountCreationDate, AccountType.CDSavingsAccount, initialBalance);
            this.interestRate = interestRate;
            this.dueDate = dueDate;
        }

        public CDSavingsAccount(String customerID, Date accountCreationDate, long accountID, double balance, double interestRate, Date dueDate) {
            super(customerID, accountCreationDate, AccountType.CDSavingsAccount, accountID);
            this.balance = balance;
            this.interestRate = interestRate;
            this.dueDate = dueDate;
        }

        // Deletes an account from the entire system, including the database
        public static void deleteAccount(CDSavingsAccount account) {

            // Remove the account from the list of customer accounts
            Customer customer = Database.getCustomerFromList(account.getCustomerID());
            if (customer != null) {
                customer.removeAccountFromCustomerAccounts(account.getAccountID());
            }

            // Remove the account from the lists in the database
            Database.removeItemFromList(Database.cdSavingsAccountList, account);
            Database.removeItemFromList(Database.abstractAccountList, account);

            // Finally, fully delete the account
            account = null;
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.CDSavingsAccount;
        }

        @Override
        public String toFileString() {
            String toReturn = "";

            // Abstract account information
            toReturn += getCustomerID();
            toReturn += ";" + getAccountCreationDate();
            toReturn += ";" + getAccountType();
            toReturn += ";" + getAccountID();

            // Specific account information
            toReturn += ";" + getBalance();
            toReturn += ";" + getInterestRate();
            toReturn += ";" + getDueDate().toString();

            return toReturn;
        }


        public static CDSavingsAccount fromFileString(String s) {
            String[] split = s.split(";");

            // Abstract stuff
            String customerID = split[0];
            Date accountCreationDate = new Date(Long.parseLong(split[1]));
            String abstractAccountType = split[2]; // This is ignored, it is handled in the constructor
            long accountID = Long.parseLong(split[3]);

            // Specific
            double balance = Double.parseDouble(split[4]);
            double interestRate = Double.parseDouble(split[5]);
            Date dueDate = new Date(Long.parseLong(split[6]));

            return new CDSavingsAccount(customerID, accountCreationDate, accountID, balance, interestRate, dueDate);
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

        public Date getDueDate() {
            return dueDate;
        }

        public void setDueDate(Date dueDate) {
            this.dueDate = dueDate;
        }

        public void withdrawBeforeDueDate(int amount) {
            super.withdraw(amount);
            deleteAccount(this);
        }

        // Withdraw your funds after the due date, but before the rollover period
        // !!! This deletes the account
        public void withdrawAfterDueDate(int amount) {
            calcAndAddInterest();
            super.withdraw(amount);
            deleteAccount(this);
        }

        public void rollOver(int numberOfMonthsForNewCycle, double newRate, Date newDueDate) {
            interestRate = newRate;
            dueDate = newDueDate;
        }
    }
}
