import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CheckingAccount extends AbstractAccount{
    // Data
    double balance;
    static final int minimumBalanceGoldDiamond = 5000;
    static double interestRate; // For GoldDiamond accounts
    int overdraftsThisMonth;

    AccountType accountSpecificType;
    ArrayList<String> stopPaymentArray;
    
    boolean linkedToATMCard;

    // SavingsAccount.SimpleSavingsAccount overdraftAccount; // The overdraft account for this Checking account
    long overDraftAccountID; // ID of the overdraft account for this Checking account

    public enum AccountType {
        TMB,
        GoldDiamond
    }

    // Constructor used when creating a new account for the first time
    public CheckingAccount(String customerID, Date accountCreationDate, double initialBalance) {
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
        overDraftAccountID = -1;
        linkedToATMCard = false;
    }

    // Constructor used when restoring accounts
    public CheckingAccount(String customerID, Date accountCreationDate, double balance, int overdraftsThisMonth, long accountID, long overdraftAccountID, boolean linkedToATMCard, ArrayList<String> stopPaymentArrayPassed) {
        super(customerID, accountCreationDate, AbstractAccount.AccountType.CheckingAccount, accountID);
        setAccountType(AbstractAccount.AccountType.CheckingAccount);
        setBalance(balance);
        if (balance < minimumBalanceGoldDiamond) {
            setAccountSpecificType(AccountType.TMB);
        } else {
            setAccountSpecificType(AccountType.GoldDiamond);
        }
        stopPaymentArray = new ArrayList<>(stopPaymentArrayPassed);
        if (overdraftAccountID > -1) {
            this.overDraftAccountID = overdraftAccountID;
        } else {
            overDraftAccountID = -1;
        }
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
        Date accountCreationDate = new Date(Date.parse(split[1]));
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

        // Now make an aList of the held stopped checks
        ArrayList<String> stopPaymentArrayPassed = new ArrayList<>();
        if (split.length > 10) {
            stopPaymentArrayPassed.addAll(Arrays.asList(split).subList(8, split.length));
        }

        // Return an account made from this information
        CheckingAccount temp = new CheckingAccount(customerID, accountCreationDate, balance, overdraftsThisMonth, accountID, overdraftAccountID, linkedToATMCard, stopPaymentArrayPassed);
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
        balance += amount;
        balance -= getTransactionFee(false);
        if (accountSpecificType == AccountType.TMB) {
            if (balance >= minimumBalanceGoldDiamond) {
                setAccountSpecificType(AccountType.GoldDiamond);
            }
        } else { // AccountType is GoldDiamond
            if (balance < minimumBalanceGoldDiamond) {
                setAccountSpecificType(AccountType.TMB);
            }
        }
    }

    public void withdraw(double amount) {
        if (isDeleted()) {
            return;
        }
        if (balance >= amount) {
            balance -= amount;
            balance -= getTransactionFee(false);
            if (accountSpecificType == AccountType.TMB) {
                if (balance >= minimumBalanceGoldDiamond) {
                    setAccountSpecificType(AccountType.GoldDiamond);
                }
            }
        } else {
            boolean overdraftFail = false;

            // Check for a SavingsAccount (Overdraft account)
            if (overDraftAccountID != -1) { // One exists
                SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);

                if (overdraftAccount.getBalance() >= amount) { // Enough money in the overdraft account
                    // Withdraw from overdraft, deposit in checking, then withdraw from checking
                    overdraftAccount.withdraw(amount);
                    deposit(amount);
                    balance -= amount;
                } else if (balance + overdraftAccount.getBalance() >= amount) { // Not enough in the individual accounts; check them together
                    // Remove funds from the CheckingAccount, update "amount" with the amount gotten from the account
                    amount -= balance;
                    balance = 0.0;
                    setAccountSpecificType(AccountType.TMB);

                    // Now do the regular withdrawal procedure
                    double withFee = amount + 0.75; // Include a transfer fee for correct values
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
    }

    // Not 100% sure how this needs to work, for now just a simple function
    public void transfer(double amount /*, AbstractAccount transferToAccount*/) {
        if (isDeleted()) {
            return;
        }
        if (balance >= amount) {
            balance -= amount;
            balance -= getTransactionFee(true);
            if (accountSpecificType == AccountType.TMB) {
                if (balance >= minimumBalanceGoldDiamond) {
                    setAccountSpecificType(AccountType.GoldDiamond);
                }
            }
        } else {
            System.out.println("Insufficient Balance"); // !!! Swing will need to change this
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

        } else { // GoldDiamond account
            if (balance < minimumBalanceGoldDiamond) {
                if (isTransfer) {
                    fee = 1.25; // Transfer
                } else {
                    fee = 0.75; // Deposit/withdrawal
                }
            }
        }

        return fee;
    }

    // Calculates and adds the interest for a GoldDiamond account
    public void calcAndAddInterest() {
        if (isDeleted()) {
            return;
        }
        balance += balance * interestRate;
    }

    // This expects to be given SavingsAccounts interest rate, it does the x0.5 itself
    public void setInterestRate(double interestRatePassed) {
        interestRate = interestRatePassed * 0.5;
    }

    public double getInterestRate() {
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

    // Withdraw via a check rather than by card
    public void withdrawByCheck(int amount, String checkNumber) {
        if (isDeleted()) {
            return;
        }
        boolean stopPayment = false;

        // Check if the check is valid and if it is contained in the "stop payment" list
        if (!validateCheckNumber(checkNumber)) {
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
            if (amount > balance) {

                boolean overdraftFail = false;

                // Check for a SavingsAccount (Overdraft account)
                if (overDraftAccountID != -1) { // One exists
                    SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);

                    if (overdraftAccount.getBalance() >= amount) { // Enough money in the overdraft account
                        // Withdraw from overdraft, deposit in checking, then withdraw from checking
                        overdraftAccount.withdraw(amount);
                        deposit(amount);
                        balance -= amount;
                    } else if (balance + overdraftAccount.getBalance() >= amount) { // Not enough in the individual accounts; check them together
                        // Remove funds from the CheckingAccount, update "amount" with the amount gotten from the account
                        amount -= balance;
                        balance = 0.0;

                        // Now do the regular withdrawal procedure
                        overdraftAccount.withdraw(amount);
                        deposit(amount);
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

            } else { // Enough funds in Checking to cover amount
                balance -= amount;
            }

        }
    }
}
