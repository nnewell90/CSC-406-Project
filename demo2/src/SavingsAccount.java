import java.util.Date;

public class SavingsAccount extends AbstractAccount {
    // Data
    double balance;
    CheckingAccount overdraftForAccount; // The checking account this account is an overdraft for

    public SavingsAccount(int customerID, Date accountCreationDate, String accountType, double initialBalance) {
        super(customerID, accountCreationDate, accountType);
        setBalance(initialBalance);
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
        SavingsAccount temp;

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

    // Simple Savings class
    static class SimpleSavingsAccount extends SavingsAccount {

        // Data
        double interestRate;

        public SimpleSavingsAccount(int customerID, Date accountCreationDate, int initialBalance) {
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
            SimpleSavingsAccount temp;

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

        public CDSavingsAccount(int customerID, Date accountCreationDate, int initialBalance, Date dueDate) {
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
            CDSavingsAccount temp;

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

        public void withdraw(int amount) {
            super.withdraw(amount);
        }
    }
}
