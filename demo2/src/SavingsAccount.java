import java.util.Date;

public class SavingsAccount extends AbstractAccount {
    // Data
    double balance;
    CheckingAccount overdraftForAccount; // The checking account this account is an overdraft for

    public ATMCard atmCard;

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

        public SimpleSavingsAccount(int customerID, Date accountCreationDate, int initialBalance, Customer customer, int atmCardChoice) {
            super(customerID, accountCreationDate, "Simple Savings Account", initialBalance);

            if(atmCardChoice == 1){ //if card choice equals 1 the customer will receive their ATM card
                this.atmCard = new ATMCard(customer,this);
            }
        }

        @Override
        public String getAccountType() {
            return "Simple Savings Account";
        }

        @Override
        public String toFileString() {
            return "";
        }
        /*
        ATM card test print statement to ensure the selection of the atm card works properly
         */
        @Override
        public String toString(){
            return super.toString() + "Customer: " + customerID + " Has opened an account on: " + accountCreationDate + " Account Type: " + accountType + " Initial Deposit: " + balance + " ATM Card: " + (hasATMCard() ? " Yes " : " No ");
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

        public void deposit(int amount) {
            super.deposit(amount);
        }

        public void withdraw(int amount) {
            super.withdraw(amount);
        }

        //New ATM cards for simple savings accounts
        public boolean hasATMCard(){
            return atmCard != null;
        }
        public ATMCard getATMCard(){
            return atmCard;
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

        public void withdraw(int amount) {
            super.withdraw(amount);
        }



    }
}
