import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SavingsAccount extends AbstractAccount {
    // Data
    double balance;

    // Normal constructor
    public SavingsAccount(String customerID, LocalDate accountCreationDate, double initialBalance) {
        super(customerID, accountCreationDate, AccountType.SavingsAccount);
        setBalance(initialBalance);
    }

    // Restore from database constructor
    public SavingsAccount(String customerID, LocalDate accountCreationDate, double balance, long accountID) {
        super(customerID, accountCreationDate, AccountType.SavingsAccount, accountID);
        setBalance(balance);
    }

    @Override
    public AccountType getAccountType() {
        if (isDeleted()) {
            return null;
        }
        return AccountType.SavingsAccount;
    }

    @Override
    public String toFileString() {
        if (isDeleted()) {
            return null;
        }
        String toReturn = "";

        // Abstract account information
        toReturn += getCustomerID();
        toReturn += ";" + getAccountCreationDate();
        toReturn += ";" + getAccountType();
        toReturn += ";" + getAccountID();

        // Specific account information
        toReturn += ";" + getBalance();

        toReturn += ";" + isDeleted();

        return toReturn;
    }


    public static SavingsAccount fromFileString(String s) {
        String[] split = s.split(";");

        // Abstract stuff
        String customerID = split[0];
        String[] splitParseDate = split[1].split("-");
        int year = Integer.parseInt(splitParseDate[0]);
        int month = Integer.parseInt(splitParseDate[1]);
        int day = Integer.parseInt(splitParseDate[2]);
        LocalDate accountCreationDate = LocalDate.of(year, month, day);
        AccountType abstractAccountType = AccountType.valueOf(split[2]);
        long accountID = Long.parseLong(split[3]);

        // Specific
        double balance = Double.parseDouble(split[4]);

        boolean isDeleted = Boolean.parseBoolean(split[5]);

        SavingsAccount temp = new SavingsAccount(customerID, accountCreationDate, balance, accountID);
        temp.setDeleted(isDeleted);
        return temp;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        if (isDeleted()) {
            return Double.NaN;
        }
        return balance;
    }

    public void deposit(double amount) {
        if (isDeleted()) {
            return;
        }
        if (amount <= 0.0) {
            return;
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (isDeleted()) {
            return;
        }
        if (amount <= 0.0) {
            return;
        }
        if (balance > amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient Balance");
        }
    }

    // Simple Savings class
    public static class SimpleSavingsAccount extends SavingsAccount {

        // Data
        static double interestRate;
        // CheckingAccount overdraftForAccount; // The checking account this account is an overdraft for
        long overdraftForAccountID; // ID for the checking account this account is an overdraft for
        ArrayList<String> stopPaymentArray; // Used to stop checks from going through
        HashMap<String, Double> checkMap;

        boolean linkedToATMCard;

        public SimpleSavingsAccount(String customerID, LocalDate accountCreationDate, int initialBalance) {
            super(customerID, accountCreationDate, initialBalance);
            setAccountType(AccountType.SimpleSavingsAccount);
            overdraftForAccountID = -1;
            stopPaymentArray = new ArrayList<>();
            checkMap = new HashMap<>();
            linkedToATMCard = false;
        }

        public SimpleSavingsAccount(String customerID, LocalDate accountCreationDate, long accountID, double balance, long overdraftAccountID, boolean linkedToATMCard, HashMap<String, Double> checkMapPassed, ArrayList<String> stopPaymentArrayPassed) {
            super(customerID, accountCreationDate, balance, accountID);
            setAccountType(AccountType.SimpleSavingsAccount);
            setBalance(balance);
            checkMap = new HashMap<>(checkMapPassed);
            stopPaymentArray = new ArrayList<>(stopPaymentArrayPassed);
            if (overdraftAccountID > -1) {
                overdraftForAccountID = overdraftAccountID;
            } else {
                overdraftForAccountID = -1;
            }
            this.linkedToATMCard = linkedToATMCard;
        }

        // Deletes an account from the entire system, including the database
        public static void deleteAccount(SimpleSavingsAccount account) {
            if (account.isDeleted()) {
                return;
            }

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

            // Remove the ATM card from the system
            if (account.isLinkedToATMCard()) {
                ATMCard card = Database.getATMCardFromList(account.getAccountID());
                ATMCard.deleteATMCard(card);
            }

            // Remove the account from the lists in the database
            Database.removeItemFromList(Database.simpleSavingsAccountList, account);
            Database.removeItemFromList(Database.abstractAccountList, account);

            // Finally, fully delete the account
            account.setDeleted(true);
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.SimpleSavingsAccount;
        }

        @Override
        public String toFileString() {
            if (isDeleted()) {
                return null;
            }
            String toReturn = "";

            // Abstract account information
            toReturn += getCustomerID();
            toReturn += ";" + getAccountCreationDate();
            toReturn += ";" + getAccountType();
            toReturn += ";" + getAccountID();

            // Specific account information
            toReturn += ";" + getBalance();
            if (overdraftForAccountID != -1) {
                toReturn += ";" + overdraftForAccountID;
            } else {
                toReturn += ";" + "-1";
            }

            toReturn += ";" + linkedToATMCard;

            toReturn += ";" + isDeleted();

            // Add the check map
            toReturn += ";" + checkMap.size();
            for (String key : checkMap.keySet()) {
                toReturn += ";" + key;
                toReturn += ";" + checkMap.get(key);
            }

            // Add the stop payment list
            toReturn += ";" + stopPaymentArray.size();
            for (String s : stopPaymentArray) {
                toReturn += ";" + s;
            }

            return toReturn;
        }

        public static SimpleSavingsAccount fromFileString(String s) {
            String[] split = s.split(";");

            // Abstract stuff
            String customerID = split[0];
            String[] splitParseDate = split[1].split("-");
            int year = Integer.parseInt(splitParseDate[0]);
            int month = Integer.parseInt(splitParseDate[1]);
            int day = Integer.parseInt(splitParseDate[2]);
            LocalDate accountCreationDate = LocalDate.of(year, month, day);
            AccountType abstractAccountType = AccountType.valueOf(split[2]);
            long accountID = Long.parseLong(split[3]);

            // Specific
            double balance = Double.parseDouble(split[4]);
            long overdraftAccountID = -1; // Have to do it this way because of later comparison
            overdraftAccountID = Long.parseLong(split[5]);

            boolean linkedToAtmCard = Boolean.parseBoolean(split[6]);

            boolean isDeleted = Boolean.parseBoolean(split[7]);

            // Now make a map of the check numbers to be processed
            HashMap<String, Double> checkMap = new HashMap<>();
            int mapLength = Integer.parseInt(split[8]); // Get the number of entries for the map
            int baseIndex = 9; // This is an index representation for split[]
            for (int i = 0; i < mapLength; i++) {
                // * 2 is to offset the fact that every 2 entries is a String:Double pair, meaning the next String is 2 indexes from the current one
                int stringPosition = baseIndex + i * 2;
                int doublePosition = baseIndex + i * 2 + 1;
                checkMap.put(split[stringPosition], Double.parseDouble(split[doublePosition]));
            }

            // Now make an aList of the held stopped check numbers
            ArrayList<String> stopPaymentArray = new ArrayList<>();
            baseIndex += mapLength * 2; // * 2 to account for the String AND Double entries in the list of values
            int listLength = Integer.parseInt(split[baseIndex]); // Get the number of entries for the list
            if (listLength > 0) { // There is at least one entry, add them to the list
                baseIndex += 1; // Update the baseIndex to the correct index value
                stopPaymentArray.addAll(Arrays.asList(split).subList(baseIndex, split.length));
            }

            SimpleSavingsAccount temp = new SimpleSavingsAccount(customerID, accountCreationDate, accountID, balance, overdraftAccountID, linkedToAtmCard, checkMap, stopPaymentArray);
            temp.setDeleted(isDeleted);
            return temp;
        }

        // Transfer accounts from a user's Checking account to another one of their accounts, either a Checking or a SimpleSavings account
        public void transfer(double amount, long accountToTransferToID) {
            if (isDeleted()) {
                return;
            }

            // Make sure they are transferring a positive amount
            if (amount <= 0.0) {
                System.out.println("Given amount is less than $0.0: Stopping transfer");
                return;
            }

            // See if the given account ID to be transferred to is one of the customer's accounts
            String cID = this.getCustomerID();
            Customer c = Database.getCustomerFromList(cID);
            if (!c.getCustomerAccountIDs().contains(accountToTransferToID)) { // If an account is NOT within this customer's list of accountIDs
                System.out.println("Account to transfer to is not owned by the customer of this account: Stopping transfer");
                return;
            }

            // See if the account being transferred to is a valid account to transfer to: SimpleSavings or Checking
            // Note that the Database will return null if accounts are not found in the list
            SavingsAccount.SimpleSavingsAccount ss = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, accountToTransferToID);
            CheckingAccount ca = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, accountToTransferToID);
            boolean isSavingsAccount = false;
            boolean isCheckingAccount = false;
            if (ss != null) {
                isSavingsAccount = true;
            } else if (ca != null) {
                isCheckingAccount = true;
            } else { // An account could not be found for the given account ID
                System.out.println("An account could not be found for the given account ID: Stopping transfer");
                return;
            }

            // An account has been linked to a customer and is a valid account: Actually transfer funds if they are available
            if (balance >= amount) {
                balance -= amount;
                // Add the amount to the other account
                if (isCheckingAccount) {
                    ca.deposit(amount);
                } else if (isSavingsAccount) {
                    ss.deposit(amount);
                }
            } else {
                System.out.println("Insufficient Balance"); // !!! Swing will need to change this
            }
        }

        public void setOverdraftForAccount(long overdraftForAccountID) {
            if (isDeleted()) {
                return;
            }
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

        public boolean isLinkedToATMCard() {
            return linkedToATMCard;
        }

        public void setLinkedToATMCard(boolean linkedToATMCard) {
            this.linkedToATMCard = linkedToATMCard;
        }

        public ATMCard getATMCard() {
            return Database.getATMCardFromList(getAccountID());
        }

        public CheckingAccount getOverdraftForAccount() {
            if (isDeleted()) {
                return null;
            }
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

        public double getBalance() {
            return super.getBalance();
        }

        public double getInterestRate() {
            return interestRate;
        }

        public static void setInterestRate(double interestRatePassed) {
            interestRate = interestRatePassed;
            CheckingAccount.setInterestRateBySavingsAccount(interestRatePassed);
        }

        protected static void setInterestRateByCheckingAccount(double interestRatePassed) {
            interestRate = interestRatePassed * 2; // SavingsAccounts have double the interest of Checking accounts
        }

        // Calculates and adds the interest for an account
        public void calcAndAddInterest() {
            balance += balance * interestRate;
        }

        public void deposit(int amount) {
            super.deposit(amount);
        }

        public void withdraw(int amount) {
            super.withdraw(amount);
        }

        // Check stuff

        // Add a check to the list of checks which will be processed later
        // A positive value represents a deposit, a negative value represents a withdrawal
        public void addCheckToProcessLater(double amount, String checkNumber) {
            // First see if this check should be added to the list
            boolean addCheckToList = true;
            if (!validateCheckNumber(checkNumber)) { // See if the check number is NOT valid
                addCheckToList = false;
            }
            if (checkMap.containsKey(checkNumber)) { // See if the check is already in the check map
                addCheckToList = false;
            }

            // Finally, actually add the check to the map if it is valid
            if (addCheckToList && amount != 0.0) { // Check to make sure a "no-value" check isn't being inserted
                checkMap.put(checkNumber, amount);
            }
        }

        // Process all checks in the list(map) of checks to be processed
        public void processChecks() {
            // Add checks into a deposit or withdrawal list; this is so deposits can be processed before withdrawals
            ArrayList<String> depositChecks = new ArrayList<>();
            ArrayList<String> withdrawChecks = new ArrayList<>();
            for (String checkNumber : checkMap.keySet()) {
                if (checkMap.get(checkNumber) > 0.0) {
                    depositChecks.add(checkNumber);
                } else {
                    withdrawChecks.add(checkNumber);
                }
            }

            // Process deposits first
            for (String checkNumber : depositChecks) {
                // See if this check is supposed to be stopped
                if (stopPaymentArray.contains(checkNumber)) {
                    continue;
                }
                double amount = checkMap.get(checkNumber);
                deposit(amount);

            }

            // Process withdrawals after deposits
            for (String checkNumber : withdrawChecks) {
                if (stopPaymentArray.contains(checkNumber)) {
                    continue;
                }
                double amount = checkMap.get(checkNumber);
                withdraw(amount * -1); // amount is negative but withdraw expects a positive number, so * -1
            }
        }

        // Add this check to the array of checks to stop payments for
        public void addStopPaymentNumber(String checkNumber) {
            if (isDeleted()) {
                return;
            }
            if (validateCheckNumber(checkNumber)) {
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
    }

    public static class CDSavingsAccount extends SavingsAccount {

        // Data
        double interestRate;
        LocalDate dueDate;

        public CDSavingsAccount(String customerID, LocalDate accountCreationDate, double initialBalance, double interestRate, LocalDate dueDate) {
            super(customerID, accountCreationDate, initialBalance);
            setAccountType(AccountType.CDSavingsAccount);
            this.interestRate = interestRate;
            this.dueDate = dueDate;
        }

        public CDSavingsAccount(String customerID, LocalDate accountCreationDate, long accountID, double balance, double interestRate, LocalDate dueDate) {
            super(customerID, accountCreationDate, accountID);
            setAccountType(AccountType.CDSavingsAccount);
            this.balance = balance;
            this.interestRate = interestRate;
            this.dueDate = dueDate;
        }

        // Deletes an account from the entire system, including the database
        public static void deleteAccount(CDSavingsAccount account) {
            if (account.isDeleted()) {
                return;
            }
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
            if (isDeleted()) {
                return null;
            }
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

            toReturn += ";" + isDeleted();

            return toReturn;
        }


        public static CDSavingsAccount fromFileString(String s) {
            String[] split = s.split(";");

            // Abstract stuff
            String customerID = split[0];
            String[] splitParseDate = split[1].split("-");
            int year = Integer.parseInt(splitParseDate[0]);
            int month = Integer.parseInt(splitParseDate[1]);
            int day = Integer.parseInt(splitParseDate[2]);
            LocalDate accountCreationDate = LocalDate.of(year, month, day);
            String abstractAccountType = split[2]; // This is ignored, it is handled in the constructor
            long accountID = Long.parseLong(split[3]);

            // Specific
            double balance = Double.parseDouble(split[4]);
            double interestRate = Double.parseDouble(split[5]);
            splitParseDate = split[6].split("-");
            year = Integer.parseInt(splitParseDate[0]);
            month = Integer.parseInt(splitParseDate[1]);
            day = Integer.parseInt(splitParseDate[2]);
            LocalDate dueDate = LocalDate.of(year, month, day);
            boolean isDeleted = Boolean.parseBoolean(split[7]);

            CDSavingsAccount temp= new CDSavingsAccount(customerID, accountCreationDate, accountID, balance, interestRate, dueDate);
            temp.setDeleted(isDeleted);
            return temp;
        }

        public double getBalance() {
            if (isDeleted()) {
                return Double.NaN;
            }
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

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
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

        public void rollOver(int numberOfMonthsForNewCycle, double newRate, LocalDate newDueDate) {
            interestRate = newRate;
            dueDate = newDueDate;
        }
    }
}
