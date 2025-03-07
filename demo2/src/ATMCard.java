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
}
