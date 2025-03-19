public class ATMCard {
    // Data
    int numOfWithdrawalsToday = 0;
    Customer customer;
    SavingsAccount savingsAccount;
    CheckingAccount checkingAccount;

    public ATMCard(Customer customer, SavingsAccount savingsAccount) {
        // Has to be a simpleSavings account, no CDs
        if (savingsAccount.getClass() == SavingsAccount.SimpleSavingsAccount.class) {
            this.customer = customer;
            this.savingsAccount = savingsAccount;
        }
    }

    public ATMCard(Customer customer, CheckingAccount checkingAccount) {
        this.customer = customer;
        this.checkingAccount = checkingAccount;
    }

    public void withdraw(int amount) {
        if (numOfWithdrawalsToday < 2) {
            numOfWithdrawalsToday++;
            if (savingsAccount != null) {
                savingsAccount.withdraw(amount);
            }
            if (checkingAccount != null) {
                checkingAccount.withdraw(amount);
            }
        } else {
            // !!! Change for Swing
            System.out.println("You have already withdrawn the maximum amount of times today.");
        }
    }

    public int getNumOfWithdrawalsToday() {
        return numOfWithdrawalsToday;
    }

    public Customer getCustomer() {
        return customer;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    public CheckingAccount getCheckingAccount() {
        return checkingAccount;
    }

    public String toFileString() {
        String toReturn = "";

        toReturn += customer.getCustomerID();
        if (savingsAccount != null) {
            toReturn += ";" + "Savings";
            toReturn += ";" + savingsAccount.getAccountID();
        }
        if (checkingAccount != null) {
            toReturn += ";" + "Checking";
            toReturn += ";" + checkingAccount.getAccountID();
        }

        return toReturn;
    }

    static public ATMCard fromFileString(String s) {
        String[] split = s.split(";");

        // Get the customer object
        String customerID = split[0];
        Customer customer = null;
        for (Customer c : Database.customerList) {
            if (c.getCustomerID().equals(customerID)) {
                customer = c;
                break;
            }
        }

        // Get the savings/checking account
        SavingsAccount savingsAccount = null;
        CheckingAccount checkingAccount = null;

        long accountID = Long.parseLong(split[2]);

        if (split[1].equals("Savings")) {
            savingsAccount = (SavingsAccount) Database.getAccountFromList(Database.savingsAccountList, accountID);
            return new ATMCard(customer, savingsAccount);
        } else { // Must be a checking account
            checkingAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, accountID);
            return new ATMCard(customer, checkingAccount);
        }

    }
}
