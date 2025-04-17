public class ATMCard {
    // Data
    int numOfWithdrawalsToday = 0;
    String customerID;
    long accountID;
    boolean forChecking = false; // Is this an ATM card for a CheckingAccount?
    // true = checking
    // false = savings

    // For safe deletion
    boolean isDeleted = false;

    public ATMCard(String customerID, long accountID) {
        this.customerID = customerID;
        this.accountID = accountID;

        // Find the actual account this is linked to and set the boolean based on that
        for (CheckingAccount c : Database.checkingAccountList) {
            if (c.getAccountID() == accountID) {
                forChecking = true;
                c.setLinkedToATMCard(true);
                break;
            }
        }
        if (!forChecking) { // The account wasn't a checkingAccount, so do logic for a SimpleSavingsAccount
            for (SavingsAccount.SimpleSavingsAccount c : Database.simpleSavingsAccountList) {
                if (c.getAccountID() == accountID) {
                    forChecking = false; // Should be redundant
                    c.setLinkedToATMCard(true);
                    break;
                }
            }
        }

    }

    public ATMCard(String customerID, long accountID, boolean forChecking, int numOfWithdrawalsToday) {
        this.customerID = customerID;
        this.accountID = accountID;
        this.forChecking = forChecking;
        this.numOfWithdrawalsToday = numOfWithdrawalsToday;
    }

    // Delete an ATM card from the database
    public static void deleteATMCard(ATMCard card) {
        if (card.isDeleted()) {
            return;
        }
        if (card.forChecking) {
            CheckingAccount checkingAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, card.getAccountID());
            checkingAccount.setLinkedToATMCard(false);
        } else {
            SavingsAccount.SimpleSavingsAccount savingsAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, card.getAccountID());
            savingsAccount.setLinkedToATMCard(false);
        }
        Database.removeItemFromList(Database.atmCardList, card);
        card.setDeleted(true);
    }

    public void withdraw(int amount) {
        if (isDeleted()) {
            return;
        }
        if (numOfWithdrawalsToday < 2) {
            numOfWithdrawalsToday++;

            if (forChecking) {
                CheckingAccount checkingAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, accountID);
                if (checkingAccount != null) {
                    checkingAccount.withdraw(amount);
                }
            } else {
                SavingsAccount.SimpleSavingsAccount savingsAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, accountID);
                if (savingsAccount != null) {
                    savingsAccount.withdraw(amount);
                }
            }
        } else {
            // !!! Change for Swing
            System.out.println("You have already withdrawn the maximum amount of times today.");
        }
    }

    public void setNumOfWithdrawalsToday(int numOfWithdrawalsToday) {
        this.numOfWithdrawalsToday = numOfWithdrawalsToday;
    }

    public int getNumOfWithdrawalsToday() {
        return numOfWithdrawalsToday;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public boolean isForChecking() {
        return forChecking;
    }

    public void setForChecking(boolean forChecking) {
        this.forChecking = forChecking;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String toFileString() {
        if (isDeleted()) {
            return null;
        }
        String toReturn = "";

        toReturn += getCustomerID();
        if (forChecking) {
            toReturn += ";" + "Checking";
        } else {
            toReturn += ";" + "Savings";
        }
        toReturn += ";" + getAccountID();

        toReturn += ";" + getNumOfWithdrawalsToday();

        toReturn += ";" + isDeleted();

        return toReturn;
    }

    static public ATMCard fromFileString(String s) {
        String[] split = s.split(";");

        // Get the customer object
        String customerID = split[0];

        // See what kind of account there is
        boolean forChecking;
        if (split[1].equals("Savings")) {
            forChecking = false;
        } else { // Must be a checking account
            forChecking = true;
        }

        // Get the savings/checking accountID
        long accountID = Long.parseLong(split[2]);

        // Get the number of withdrawals
        int numOfWithdrawalsToday = Integer.parseInt(split[3]);

        boolean isDeleted = Boolean.parseBoolean(split[4]);

        ATMCard temp = new ATMCard(customerID, accountID, forChecking, numOfWithdrawalsToday);
        temp.setDeleted(isDeleted);
        return temp;
    }
}
