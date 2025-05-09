import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CheckingAccount extends AbstractAccount{
    // Class Data
    static final double minimumBalanceGoldDiamond = 5000;
    static double interestRate; // For GoldDiamond accounts


    // Instance data
    double balance;
    int overdraftsThisMonth;
    AccountType accountSpecificType;
    ArrayList<String> stopPaymentArray; // Checks that are stopped
    HashMap<String, Double> checkMap; // All checks
        // A positive value in Double means deposit
        // A negative value in Double means withdrawal
    
    boolean linkedToATMCard;

    // SavingsAccount.SimpleSavingsAccount overdraftAccount; // The overdraft account for this Checking account
    long overDraftAccountID; // ID of the overdraft account for this Checking account

    public enum AccountType {
        TMB,
        GoldDiamond
    }

    // Constructor used when creating a new account for the first time
    public CheckingAccount(String customerID, LocalDate accountCreationDate, double initialBalance) {
        super(customerID, accountCreationDate, AbstractAccount.AccountType.CheckingAccount);
        setAccountType(AbstractAccount.AccountType.CheckingAccount); // This is probably unnecessary
        setBalance(initialBalance);
        if (initialBalance < minimumBalanceGoldDiamond) {
            setAccountSpecificType(AccountType.TMB);
        } else {
            setAccountSpecificType(AccountType.GoldDiamond);
        }
        overdraftsThisMonth = 0;
        stopPaymentArray = new ArrayList<>();
        checkMap = new HashMap<>();
        overDraftAccountID = -1;
        linkedToATMCard = false;
    }

    // Constructor used when restoring accounts
    public CheckingAccount(String customerID, LocalDate accountCreationDate, double balance, int overdraftsThisMonth, long accountID, long overdraftAccountID, boolean linkedToATMCard, HashMap<String, Double> checkMapPassed, ArrayList<String> stopPaymentArrayPassed) {
        super(customerID, accountCreationDate, AbstractAccount.AccountType.CheckingAccount, accountID);
        setAccountType(AbstractAccount.AccountType.CheckingAccount);
        setBalance(balance);
        if (balance < minimumBalanceGoldDiamond) {
            setAccountSpecificType(AccountType.TMB);
        } else {
            setAccountSpecificType(AccountType.GoldDiamond);
        }
        checkMap = new HashMap<>(checkMapPassed);
        stopPaymentArray = new ArrayList<>(stopPaymentArrayPassed);
        this.overDraftAccountID = overdraftAccountID;
        this.linkedToATMCard = linkedToATMCard;
        setOverdraftsThisMonth(overdraftsThisMonth);
    }

    // Deletes an account from the entire system, including the database
    public static void deleteAccount(CheckingAccount account) {
        if (account.isDeleted()) {
            return;
        }

        // Unlink the account from an overdraft account if one exists
        if (account.getOverdraftAccountID() != -1) {
            account.removeOverdraftAccount();
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
        Database.removeItemFromList(Database.checkingAccountList, account);
        Database.removeItemFromList(Database.abstractAccountList, account);

        // Finally, fully delete the account
        account.setDeleted(true);
    }

    @Override
    public AbstractAccount.AccountType getAccountType() {
        return AbstractAccount.AccountType.CheckingAccount;
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
        toReturn += ";" + getOverdraftsThisMonth();
        toReturn += ";" + getAccountSpecificType();
        if (overDraftAccountID != -1) { // There may not be an overdraft account
            toReturn += ";" + overDraftAccountID; // Get the account for the overdraft account
        } else {
            toReturn += ";" + "-1"; // -1 is the "does not exist" metric for
        }

        toReturn += ";" + isLinkedToATMCard();

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

    // No override because fromFileString is static
    public static CheckingAccount fromFileString(String s) {
        String[] split = s.split(";");

        // Abstract stuff
        String customerID = split[0];

        String[] splitParseDate = split[1].split("-");
        int year = Integer.parseInt(splitParseDate[0]);
        int month = Integer.parseInt(splitParseDate[1]);
        int day = Integer.parseInt(splitParseDate[2]);
        LocalDate accountCreationDate = LocalDate.of(year, month, day);

        AbstractAccount.AccountType abstractAccountType = AbstractAccount.AccountType.valueOf(split[2]);
        long accountID = Long.parseLong(split[3]);

        // Specific stuff
        double balance = Double.parseDouble(split[4]);
        int overdraftsThisMonth = Integer.parseInt(split[5]);
        AccountType accountSpecificType = AccountType.valueOf(split[6]);
        long overdraftAccountID = -1; // -1 used as N/A placeholder, used in constructor
        overdraftAccountID = Long.parseLong(split[7]);

        boolean linkedToATMCard = Boolean.parseBoolean(split[8]);

        boolean isDeleted = Boolean.parseBoolean(split[9]);

        // Now make a map of the check numbers to be processed
        HashMap<String, Double> checkMap = new HashMap<>();
        int mapLength = Integer.parseInt(split[10]); // Get the number of entries for the map
        int baseIndex = 11; // This is an index representation for split[]
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

        // Return an account made from this information
        CheckingAccount temp = new CheckingAccount(customerID, accountCreationDate, balance, overdraftsThisMonth, accountID, overdraftAccountID, linkedToATMCard, checkMap, stopPaymentArray);
        temp.setDeleted(isDeleted);
        return temp;
    }

    public double getBalance() {
        if (isDeleted()) {
            return Double.NaN;
        }
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (isDeleted()) {
            return;
        }
        if (amount <= 0.0) {
            return;
        }
        balance += amount;
        balance -= getTransactionFee(false);
        if (balance >= minimumBalanceGoldDiamond) {
            setAccountSpecificType(AccountType.GoldDiamond);
        } else {
            setAccountSpecificType(AccountType.TMB);
        }
    }

    public void withdraw(double amount) {
        if (isDeleted()) {
            return;
        }
        if (amount <= 0.0) {
            return;
        }
        if (balance >= amount) {
            balance -= (amount + getTransactionFee(false));
        } else {
            boolean overdraftFail = false;

            // Check for a SavingsAccount (Overdraft account)
            if (overDraftAccountID != -1) { // One exists
                SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);

                // See if there is enough funds between the two accounts
                if (balance + overdraftAccount.getBalance() >= amount) {
                    // Remove funds from the CheckingAccount, update "amount" with the amount removed from the account
                    amount -= balance;
                    balance = 0.0;
                    setAccountSpecificType(AccountType.TMB);

                    // Now do the regular withdrawal procedure
                    double withFee = amount + getTransactionFee(false); // Include a transfer fee so the checking account isn't left negative
                    overdraftAccount.withdraw(withFee);
                    deposit(withFee);
                    balance -= amount;
                } else { // Not enough money between both accounts
                    overdraftFail = true;
                }
            } else { // No overdraft account
                overdraftFail = true;
            }

            // If an overdraft couldn't be resolved
            if (overdraftFail) {
                balance -= 25; // $25 charge
                System.out.println("Insufficient funds: Withdrawal denied, $25 Overdraft Service charged to account");
            }
        }
        // Update the account type at the end of the withdrawal process
        if (balance >= minimumBalanceGoldDiamond) {
            setAccountSpecificType(AccountType.GoldDiamond);
        } else {
            setAccountSpecificType(AccountType.TMB);
        }
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

        // See if there are enough funds to cover the transfer
        if (balance < amount) {
            System.out.println("Insufficient funds to transfer: Stopping transfer");
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

        // An account has been linked to a customer and is a valid account: Actually transfer funds
        balance -= amount;
        balance -= getTransactionFee(true);
        if (balance <= minimumBalanceGoldDiamond) {
            setAccountSpecificType(AccountType.TMB);
        } else {
            setAccountSpecificType(AccountType.GoldDiamond);
        }

        // Add the amount to the other account
        if (isCheckingAccount) {
            ca.deposit(amount);
        } else if (isSavingsAccount) {
            ss.deposit(amount);
        }

    }

    // Returns an amount to be subtracted from an account from a transaction fee
    public double getTransactionFee(boolean isTransfer) {
        double fee = 0;

        if (accountSpecificType == AccountType.TMB) {
            if (isTransfer) {
                fee = 1.25; // Transfer
            } else {
                fee = 0.75; // Deposit/withdrawal
            }
        }
        return fee;
    }

    // Calculates and adds the interest for a GoldDiamond account
    public void calcAndAddInterest() {
        if (isDeleted()) {
            return;
        }
        if (accountSpecificType == AccountType.GoldDiamond) { // Only GD accounts have interest
            balance += balance * interestRate;
        }

    }

    // Sets the interest rate for checking accounts, updates the rate in SavingsAccount.SimpleSavingsAccount
    public static void setInterestRate(double interestRatePassed) {
        interestRate = interestRatePassed;
        SavingsAccount.SimpleSavingsAccount.setInterestRateByCheckingAccount(interestRatePassed);
    }

    // Called in SavingsAccount so there isn't a loop in setting interest rates
    public static void setInterestRateBySavingsAccount (double interestRatePassed) {
        interestRate = interestRatePassed * 0.5; // Checking accounts have half the interest of SavingsAccounts
    }

    public static double getInterestRate() {
        return interestRate;
    }

    public int getOverdraftsThisMonth() {
        return overdraftsThisMonth;
    }

    public void setOverdraftsThisMonth(int overdraftsThisMonth) {
        this.overdraftsThisMonth = overdraftsThisMonth;
    }

    public void setAccountSpecificType(AccountType accountType) {
        this.accountSpecificType = accountType;
    }

    public CheckingAccount.AccountType getAccountSpecificType() {
        if (isDeleted()) {
            return null;
        }
        return accountSpecificType;
    }

    // Overdraft setting
    public void setOverdraftForAccount(long overDraftAccountID) {
        if (isDeleted()) {
            return;
        }
        SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);
        if (overdraftAccount != null) {
            this.overDraftAccountID = overDraftAccountID;
            overdraftAccount.setOverdraftForAccount(this.getAccountID());
        } else {
            // Some fail message
        }
    }

    // Returns the accountID for the overdraft account
    public long getOverdraftAccountID() {
        return overDraftAccountID;
    }

    public boolean isLinkedToATMCard() {
        return linkedToATMCard;
    }

    public void setLinkedToATMCard(boolean linkedToATMCard) {
        this.linkedToATMCard = linkedToATMCard;
    }

    public ATMCard getLinkedATMCard() {
        if (isDeleted()) {
            return null;
        }
        return Database.getATMCardFromList(getAccountID());
    }

    // Returns the actual overdraft account
    public SavingsAccount.SimpleSavingsAccount getOverDraftAccount() {
        if (isDeleted()) {
            return null;
        }
        if (overDraftAccountID != -1) {
            return (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);
        } else {
            // Some fail message
            return null;
        }
    }

    public void removeOverdraftAccount() {
        SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);
        if (overdraftAccount != null) {
            overdraftAccount.removeOverdraftForAccount();
            this.overDraftAccountID = -1;
        } else {
            // Some fail message
        }

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
            // See if this check is supposed to be stopped
            if (stopPaymentArray.contains(checkNumber)) {
                continue;
            }
            if (checkMap.get(checkNumber) > 0.0) {
                depositChecks.add(checkNumber);
            } else {
                withdrawChecks.add(checkNumber);
            }
            // Add this number to the stopPaymentArray
            // This is so a check cannot be made with the same check number as this check for this account
            stopPaymentArray.add(checkNumber);
        }

        // Process deposits first
        for (String checkNumber : depositChecks) {
            double amount = checkMap.get(checkNumber);
            deposit(amount);
        }

        // Process withdrawals after deposits
        for (String checkNumber : withdrawChecks) {
            double amount = checkMap.get(checkNumber);
            withdraw(amount * -1); // amount is negative, but withdraw() expects a positive number, so * -1
        }

        // All checks have either been stopped or processed, meaning they shouldn't be processed again
        // So clear the checkMap
        checkMap.clear();
    }

    // Add this check to the array of checks to stop payments for
    public void addStopPaymentNumber(String checkNumber) {
        if (isDeleted()) {
            return;
        }
        if (validateCheckNumber(checkNumber)) {
            stopPaymentArray.add(checkNumber);
            balance -= 35; // $35 charge
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
        } else if (!checkNumber.matches("\\d+")) { // If it is not only numbers
            validNumber = false;
        }

        // Return true or false
        return validNumber;
    }
}
