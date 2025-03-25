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

    SavingsAccount.SimpleSavingsAccount overdraftAccount; // The overdraft account for this Checking account


    public enum AccountType {
        TMB,
        GoldDiamond // Minimum balance needs to be enforced somehow, not sure where
    }

    // Constructor used when creating a new account for the first time
    public CheckingAccount(String customerID, Date accountCreationDate, String abstractAccountType, double initialBalance, AccountType type) {
        // Some logic for checking if an account with type GoldDiamond has the minimum funds
        // This could also be done at the end, just depends on how we want to implement it

        super(customerID, accountCreationDate, abstractAccountType);
        setBalance(initialBalance);
        setAccountSpecificType(type);
        overdraftsThisMonth = 0;
        stopPaymentArray = new ArrayList<>();
        overdraftAccount = null;
    }

    // Constructor used when restoring accounts
    public CheckingAccount(String customerID, Date accountCreationDate, String abstractAccountType, double balance, int overdraftsThisMonth, AccountType type, long accountID, long overdraftAccountID, ArrayList<String> stopPaymentArrayPassed) {
        super(customerID, accountCreationDate, abstractAccountType, accountID);
        setBalance(balance);
        setAccountSpecificType(type);
        stopPaymentArray = new ArrayList<>(stopPaymentArrayPassed);
        if (overdraftAccountID > -1) {
            setOverdraftForAccount((SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.savingsAccountList, overdraftAccountID));
        } else {
            overdraftAccount = null;
        }
    }

    public static void deleteAccount(CheckingAccount account) {
        // Functionality for deleting an account
    }

    @Override
    public String getAccountType() {
        return ("Savings Account " + accountSpecificType.toString());
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
        toReturn += ";" + getOverdraftsThisMonth();
        toReturn += ";" + getAccountSpecificType();
        toReturn += ";" + overdraftAccount.getAccountID(); // Get the account for the overdraft account
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
        Date accountCreationDate = new Date(Long.parseLong(split[1]));
        String abstractAccountType = split[2];
        long accountID = Long.parseLong(split[3]);

        // Specific stuff
        double balance = Double.parseDouble(split[4]);
        int overdraftsThisMonth = Integer.parseInt(split[5]);
        AccountType accountSpecificType = AccountType.valueOf(split[6]);
        long overdraftAccountID = -1; // -1 used as N/A placeholder, used in constructor
        overdraftAccountID = Long.parseLong(split[7]);

        // Now make an aList of the held stopped checks
        ArrayList<String> stopPaymentArrayPassed = new ArrayList<>();
        if (split.length > 8) {
            stopPaymentArrayPassed.addAll(Arrays.asList(split).subList(8, split.length));
        }

        // Return an account made from this information
        return new CheckingAccount(customerID, accountCreationDate, abstractAccountType, balance, overdraftsThisMonth, accountSpecificType, accountID, overdraftAccountID, stopPaymentArrayPassed);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
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
            if (overdraftAccount != null) {
                if (overdraftAccount.getBalance() >= amount) { // Enough money in the overdraft account
                    // Withdraw from overdraft, deposit in checking, then withdraw from checking
                    overdraftAccount.withdraw(amount);
                    deposit(amount);
                    balance -= amount;
                } else { // Not enough money in the overdraft account
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

    public String getAccountSpecificType() {
        return accountType;
    }

    // Overdraft setting
    public void setOverdraftForAccount(SavingsAccount.SimpleSavingsAccount overdraftAccount) {
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
    public void withdrawByCheck(int withdrawAmount, String checkNumber) {
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
            if (withdrawAmount > balance) {

                boolean overdraftFail = false;

                // Check for a SavingsAccount (Overdraft account)
                if (overdraftAccount != null) {
                    if (overdraftAccount.getBalance() >= withdrawAmount) { // Enough money in the overdraft account
                        // Withdraw from overdraft, deposit in checking, then withdraw from checking
                        overdraftAccount.withdraw(withdrawAmount);
                        deposit(withdrawAmount);
                        balance -= withdrawAmount;
                    } else { // Not enough money in the overdraft account
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
