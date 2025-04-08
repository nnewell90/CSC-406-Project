import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class LoanAccount extends AbstractAccount {

    // Data
    double balance; // How much is left to pay off
    double rate;
    double currentPaymentDue;
    Date paymentDueDate;
    Date notifiedOfPaymentDate;
    Date lastPaymentMadeDate;
    boolean missedPayment; // This acts as the "problem account" flag
    
    
    // Regular constructor
    public LoanAccount(String customerID, Date accountCreationDate, AccountType accountType, double balance, double rate, double currentPaymentDue) {
        super(customerID, accountCreationDate, accountType);
        this.balance = balance;
        this.rate = rate;
        this.currentPaymentDue = currentPaymentDue;
        paymentDueDate = accountCreationDate; // These are temporary placeholders
        notifiedOfPaymentDate = accountCreationDate;
        lastPaymentMadeDate = accountCreationDate;
        missedPayment = false;
    }

    // Reading from database constructor
    public LoanAccount(String customerID, Date accountCreationDate, AccountType accountType, long accountID, double balance, double rate, double currentPaymentDue, Date paymentDueDate, Date notifiedOfPaymentDate, Date lastPaymentMadeDate, boolean missedPayment) {
        super(customerID, accountCreationDate, accountType, accountID);
        this.balance = balance;
        this.rate = rate;
        this.currentPaymentDue = currentPaymentDue;
        this.paymentDueDate = paymentDueDate;
        this.notifiedOfPaymentDate = notifiedOfPaymentDate;
        this.lastPaymentMadeDate = lastPaymentMadeDate;
        this.missedPayment = missedPayment;
    }

    public double getMonthlyPaymentOnLoanYearly(int numOfYearsTotal, double balance, double rate) {
        int numOfMonthsTotal = numOfYearsTotal * 12;
        double monthlyPrinciple = balance/numOfMonthsTotal;
        double monthlyInterest = (balance/2) * numOfYearsTotal * rate;
        return monthlyPrinciple + monthlyInterest;
    }

    public double getMonthlyPaymentOnLoanMonthly(int numOfMonthsTotal, double balance, double rate) {
        double monthlyPrinciple = balance/numOfMonthsTotal;
        double monthlyInterest = (balance/2) * numOfMonthsTotal * rate;
        return monthlyPrinciple + monthlyInterest;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.LoanAccount;
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
        toReturn += ";" + getRate();
        toReturn += ";" + getPaymentDueDate();
        toReturn += ";" + getNotifiedOfPaymentDate();
        toReturn += ";" + getCurrentPaymentDue();
        toReturn += ";" + getLastPaymentMadeDate();
        toReturn += ";" + isMissedPayment();
        
        return toReturn;
    }
    
    public static LoanAccount fromFileString(String s) {
        String[] split = s.split(";");

        // Abstract stuff
        String customerID = split[0];
        Date accountCreationDate = new Date(Long.parseLong(split[1]));
        AccountType abstractAccountType = AccountType.valueOf(split[2]);
        long accountID = Long.parseLong(split[3]);

        // Specific stuff
        double balance = Double.parseDouble(split[4]);
        double rate = Double.parseDouble(split[5]);
        Date paymentDueDate = new Date(Long.parseLong(split[6]));
        Date notifiedOfPaymentDate = new Date(Long.parseLong(split[7]));
        double currentPaymentDue = Double.parseDouble(split[8]);
        Date lastPaymentMadeDate = new Date(Long.parseLong(split[9]));
        boolean missedPayment = Boolean.parseBoolean(split[10]);
        
        return new LoanAccount(customerID, accountCreationDate, abstractAccountType, accountID, balance, rate, currentPaymentDue, paymentDueDate, notifiedOfPaymentDate, lastPaymentMadeDate, missedPayment);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Date getNotifiedOfPaymentDate() {
        return notifiedOfPaymentDate;
    }

    public void setNotifiedOfPaymentDate(Date notifiedOfPaymentDate) {
        this.notifiedOfPaymentDate = notifiedOfPaymentDate;
    }

    public double getCurrentPaymentDue() {
        return currentPaymentDue;
    }

    public void setCurrentPaymentDue(double currentPaymentDue) {
        this.currentPaymentDue = currentPaymentDue;
    }

    public Date getLastPaymentMadeDate() {
        return lastPaymentMadeDate;
    }

    public void setLastPaymentMadeDate(Date lastPaymentMadeDate) {
        this.lastPaymentMadeDate = lastPaymentMadeDate;
    }

    public boolean isMissedPayment() {
        return missedPayment;
    }

    public void setMissedPayment(boolean missedPayment) {
        this.missedPayment = missedPayment;
    }

    static class ShortOrLong extends LoanAccount {
        
        /*
        double balance; How much is left to pay off for the total loan, not by month, internally not including late fees, however getBalance() returns with lateFees added to balance
        double rate; The interest rate on the loan
        double currentPaymentDue; How much is left to pay for this pay period
        Date paymentDueDate; This is going to be set as the final payment, not the next payment due
        Date notifiedOfPaymentDate; The date a payment is notified to the system?
        Date lastPaymentMadeDate;
        boolean missedPayment; This acts as the "problem account" flag
         */
        
        // Data
        // Date finalPaymentDate;       paymentDueDate
        Date thisPaymentDueDate;
        
        // double fixedRate; rate
        double fixedPayment; // Amount that needs to be paid every month
        double amountPaidThisMonth; // Payment from customer for this month, used to mark missedPayment and pay off the loan
        double loanTotal; // The total the loan becomes, doesn't reduce as user pays
        // double loanLeft; balance,     this decreases as the user pays into the loan
        double lateFees; // A total for late fees that need to be paid
            // !!! Functionality may need to be changed to make an array of month payments so fees can be added there,
            // but this was a simple way to do it, so for now I'm just doing this

        // boolean problemAccount;      missedPayment
        int numOfYearsTotal; // 15/30 for Long, 5 for short
        int numOfMonthsTotal; // Some multiple of the above


        // Regular constructor
        public ShortOrLong(String customerID, Date accountCreationDate, AccountType accountType, double loanTotalValue, double rate, double fixedPaymentValue, Date finalPaymentDate, int numOfYears) {
            super(customerID, accountCreationDate, accountType, loanTotalValue, rate, fixedPaymentValue + fixedPaymentValue * rate);
            this.loanTotal = loanTotalValue; // This does NOT change as the user pays
            balance = loanTotalValue; // Initially set the total amount to pay to the given total;
            // this changes as the user pays, this is the amount of the loan left to pay
            this.rate = rate;
            amountPaidThisMonth = 0; // No payment yet when an account is created
            lateFees = 0;
            missedPayment = false;
            numOfYearsTotal = numOfYears; // !!! Maybe some logic for checking for 5/15/30
            numOfMonthsTotal = numOfYearsTotal * 12;
            //  If a check needs to be done, it needs to happen before this constructor call

            // Do calculation for monthly payment
            this.fixedPayment = getMonthlyPaymentOnLoanYearly(numOfYearsTotal, balance, rate);

            // this.fixedPayment = fixedPaymentValue; For now, keep the fixedPaymentValue input, but it will be corrected here
            currentPaymentDue = fixedPaymentValue + fixedPaymentValue * rate;
            
            paymentDueDate = finalPaymentDate;
            
            // Set thisPaymentDueDate
            int nextMonth = accountCreationDate.getMonth() + 1;
            thisPaymentDueDate = accountCreationDate;
            if (nextMonth == 12) { // If the next month is 12, roll over to the next year
                thisPaymentDueDate.setMonth(0);
                thisPaymentDueDate.setYear(thisPaymentDueDate.getYear() + 1);
            } else {
                thisPaymentDueDate.setMonth(nextMonth);
            }
            
            notifiedOfPaymentDate = accountCreationDate; // Since there has been no payment yet, just set it to the creation date
            lastPaymentMadeDate = accountCreationDate; // Same as above
        }

        // Reading from database constructor
        public ShortOrLong(String customerID, Date accountCreationDate, AccountType accountType, long accountID, double loanTotalValue, double rate, Date paymentDueDate, Date notifiedOfPaymentDate, Date thisPaymentDueDate, Date lastPaymentMadeDate, double fixedPaymentValue, double currentPaymentDue, double balance, double lateFees, double amountPaidThisMonth, boolean missedPayment, int numOfYearsTotal, int numOfMonthsTotal) {
            // Reminder that balance stands for how much is left to pay off, NOT the total value of the loan
            // This will return the total balance to pay off, not the value assigned to balance
            super(customerID, accountCreationDate, accountType, accountID, balance + lateFees, rate, currentPaymentDue, paymentDueDate, notifiedOfPaymentDate, lastPaymentMadeDate, missedPayment);
            this.thisPaymentDueDate = thisPaymentDueDate;
            this.fixedPayment = fixedPaymentValue;
            this.amountPaidThisMonth = amountPaidThisMonth;
            this.loanTotal = loanTotalValue;
            this.lateFees = lateFees;
            this.numOfYearsTotal = numOfYearsTotal;
            this.numOfMonthsTotal = numOfMonthsTotal;
        }

        // Deletes an account from the entire system, including the database
        public static void deleteAccount(ShortOrLong account) {

            // Remove the account from the list of customer accounts
            Customer customer = Database.getCustomerFromList(account.getCustomerID());
            if (customer != null) {
                customer.removeAccountFromCustomerAccounts(account.getAccountID());
            }

            // Remove the account from the lists in the database
            Database.removeItemFromList(Database.shortOrLongLoanList, account);
            Database.removeItemFromList(Database.abstractAccountList, account);

            // Finally, fully delete the account
            account = null;
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.ShortOrLongLoanAccount;
        }

        @Override
        public String toFileString() {
            String toReturn = "";

            // Abstract account information
            toReturn += getCustomerID();
            toReturn += ";" + getAccountCreationDate();
            toReturn += ";" + getAccountType();
            toReturn += ";" + getAccountID();

            // Generic loan info
            toReturn += ";" + getBalance();
            toReturn += ";" + getRate();
            toReturn += ";" + getCurrentPaymentDue();
            toReturn += ";" + getPaymentDueDate();
            toReturn += ";" + getNotifiedOfPaymentDate();
            toReturn += ";" + getLastPaymentMadeDate();
            toReturn += ";" + isMissedPayment();

            // Long/Short information
            toReturn += ";" + getThisPaymentDueDate();
            toReturn += ";" + getFixedPayment();
            toReturn += ";" + getAmountPaidThisMonth();
            toReturn += ";" + getLoanTotal();
            toReturn += ";" + getLateFees();
            toReturn += ";" + getNumOfYearsTotal();
            toReturn += ";" + getNumOfMonthsTotal();

            return toReturn;
        }

        public static ShortOrLong fromFileString(String s) {
            String[] split = s.split(";");

            // Abstract stuff
            String customerID = split[0];
            Date accountCreationDate = new Date(Long.parseLong(split[1]));
            AccountType abstractAccountType = AccountType.valueOf(split[2]);
            long accountID = Long.parseLong(split[3]);

            // Generic loan info
            double balance = Double.parseDouble(split[4]);
            double rate = Double.parseDouble(split[5]);
            double currentPaymentDue = Double.parseDouble(split[6]);
            Date paymentDueDate = new Date(Long.parseLong(split[7]));
            Date notifiedOfPaymentDate = new Date(Long.parseLong(split[8]));
            Date lastPaymentMadeDate = new Date(Long.parseLong(split[9]));
            boolean missedPayment = Boolean.parseBoolean(split[10]);

            // Specific
            Date thisPaymentDueDate = new Date(Long.parseLong(split[11]));
            double fixedPayment = Double.parseDouble(split[12]);
            double amountPaidThisMonth = Double.parseDouble(split[13]);
            double loanTotal = Double.parseDouble(split[14]);
            double lateFees = Double.parseDouble(split[15]);
            int numOfYearsTotal = Integer.parseInt(split[16]);
            int numOfMonthsTotal = Integer.parseInt(split[17]);

            return new ShortOrLong(customerID, accountCreationDate, abstractAccountType, accountID, loanTotal, rate, paymentDueDate, notifiedOfPaymentDate, thisPaymentDueDate, lastPaymentMadeDate, fixedPayment, currentPaymentDue, balance, lateFees, amountPaidThisMonth, missedPayment, numOfYearsTotal, numOfMonthsTotal);
        }

        public void makePayment(double amount, Date dayOfPay) {
            amountPaidThisMonth += amount;
            notifiedOfPaymentDate = new Date(dayOfPay.getTime());
            lastPaymentMadeDate = new Date(dayOfPay.getTime());
        }

        // Checks to see if this month's payment has been met, sets missed if not, and resets amountPaidThisMonth
        public void monthPaymentCheck() {
            // Below minimum payment
            if (amountPaidThisMonth < fixedPayment) {
                missedPayment = true;
                lateFees += 75; // Add a $75 late fee
            }
            balance -= amountPaidThisMonth;
            amountPaidThisMonth = 0;
        }

        // Goes to the next pay period
        // also sets if this is a problem customer, and resets payment information
        public void updatePayPeriod() {
            monthPaymentCheck();
            int tempMonth = thisPaymentDueDate.getMonth(); // (0-11 is January-December)
            tempMonth++;
            // Update the next payment date
            if (tempMonth == 12) { // If the next month is 12, roll over to the next year
                thisPaymentDueDate.setMonth(0);
                thisPaymentDueDate.setYear(thisPaymentDueDate.getYear() + 1);
            } else {
                thisPaymentDueDate.setMonth(tempMonth);
            }
        }

        @Override
        public double getBalance() {
            return balance + lateFees; // Balance acts as how much to pay off total, so include all late fees as well
        }
        
        public double getFixedPayment() {
            return fixedPayment;
        }

        public void setFixedPayment(double fixedPayment) {
            this.fixedPayment = fixedPayment;
        }

        public Date getThisPaymentDueDate() {
            return thisPaymentDueDate;
        }

        public void setThisPaymentDueDate(Date thisPaymentDueDate) {
            this.thisPaymentDueDate = thisPaymentDueDate;
        }

        public int getNumOfYearsTotal() {
            return numOfYearsTotal;
        }

        public void setNumOfYearsTotal(int numOfYearsTotal) {
            this.numOfYearsTotal = numOfYearsTotal;
        }

        public double getAmountPaidThisMonth() {
            return amountPaidThisMonth;
        }

        public void setAmountPaidThisMonth(double amountPaidThisMonth) {
            this.amountPaidThisMonth = amountPaidThisMonth;
        }

        public double getLoanTotal() {
            return loanTotal;
        }

        public void setLoanTotal(double loanTotal) {
            this.loanTotal = loanTotal;
        }

        public double getLateFees() {
            return lateFees;
        }

        public void setLateFees(double lateFees) {
            this.lateFees = lateFees;
        }

        public int getNumOfMonthsTotal() {
            return numOfMonthsTotal;
        }

        public void setNumOfMonthsTotal(int numOfMonthsTotal) {
            this.numOfMonthsTotal = numOfMonthsTotal;
        }
    }

    static class CC extends LoanAccount {
        // Data (Simple)
        double limit; // The maximum amount of money a customer is allowed to charge to the account
        double financeCharge; // If a month's charges aren't completely paid in time, this is added to the bill
        // double totalCharge; // balance    this cannot go beyond the limit

        // Data (Complex)
        double sumOfChargesThisMonth; // Used to calculate finance charge
        ArrayList<String> chargeMessages = new ArrayList<>(); // Messages for each charge and their date need to go in here

        // Regular constructor
        // Balance should start at 0,
        // rate may also be 0 because I don't see any specifications for a rate on CCs
        public CC(String customerID, Date accountCreationDate, AccountType accountType, double balance, double rate, double currentPaymentDue, double limit) {
            super(customerID, accountCreationDate, accountType, balance, rate, currentPaymentDue);
            this.limit = limit;
            financeCharge = 0;
            // totalCharge = balance
            sumOfChargesThisMonth = 0;
            chargeMessages = new ArrayList<>(); // Start with an empty list
        }

        // Reading from database constructor
        // Reminder that balance is the total charge for this account
        public CC(String customerID, Date accountCreationDate, AccountType accountType, long accountID, double balance, double rate, double currentPaymentDue, Date paymentDueDate, Date notifiedOfPaymentDate, Date lastPaymentMadeDate, boolean missedPayment, double limit, double financeCharge, double sumOfChargesThisMonth, ArrayList<String> chargeMessages) {
            super(customerID, accountCreationDate, accountType, accountID, balance, rate, currentPaymentDue, paymentDueDate, notifiedOfPaymentDate, lastPaymentMadeDate, missedPayment);
            this.limit = limit;
            this.financeCharge = financeCharge;
            this.sumOfChargesThisMonth = sumOfChargesThisMonth;
            this.chargeMessages = chargeMessages;
        }

        // Deletes an account from the entire system, including the database
        public static void deleteAccount(CC account) {

            // Remove the account from the list of customer accounts
            Customer customer = Database.getCustomerFromList(account.getCustomerID());
            if (customer != null) {
                customer.removeAccountFromCustomerAccounts(account.getAccountID());
            }

            // Remove the account from the lists in the database
            Database.removeItemFromList(Database.CCList, account);
            Database.removeItemFromList(Database.abstractAccountList, account);

            // Finally, fully delete the account
            account = null;
        }

        public void charge(double amount, String description, Date dayOfCharge) {
            // See if this charge would go over the set limit
            if (balance + amount < limit) {
                balance += amount; // This can be reduced by the customer
                sumOfChargesThisMonth += amount; // This cannot be reduced by the user, used for finance charge
                chargeMessages.add(dayOfCharge + ":" + description + "\nAmount:" + amount);
            } else { // Over the limit
                System.out.println("Cannot make charge because it would exceed the limit of the account");
                // Change this for Swing !!!
            }
        }

        public void payment (double amount) {
            balance -= amount;
            // !!! Maybe some logic to see if the customer pays too much
        }

        // Returns a bill for this month's charged
        public String getBillThisMonth() {
            return String.format("""
                    Total charge this month: %.2f
                    Finance charge this month: %.2f
                    """, balance, financeCharge);
        }

        // Returns whether the finance charge needs to be added to the total
        // If so, it adds the charge
        public boolean isFinanceChargeAddedToTotal() {
            // !!! Assumes that this is being called on the 11th so the user has time to pay
            boolean wasAdded = false;

            if (balance > 0) {
                addFinanceChargeToTotal();
                wasAdded = true;
            }

            return wasAdded;
        }

        public void addFinanceChargeToTotal() {
            balance += financeCharge;
        }

        public double calculateFinanceCharge(int daysInMonth) { // Potentially change this if time is kept for the system !!!
            return sumOfChargesThisMonth / daysInMonth;
        }

        public void monthPaymentCheck() {
            boolean fullyPaid = isFinanceChargeAddedToTotal();
            if (fullyPaid) { // !!! Change these for swing
                System.out.println("Credit card fully paid off this month, no finance charge added");
            } else {
                System.out.println("Credit card not paid off this month, adding finance charge to total");
            }
        }

        // Returns charge messages as a string
        public String getChargeMessagesString() {
            String initialString = """
                    Charge History for Credit Card
                    
                    """;
            StringBuilder allMessages = new StringBuilder(initialString);

            for (String message : chargeMessages) {
                allMessages.append(message).append("\n");
            }

            return allMessages.toString();
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.CCLoanAccount;
        }

        @Override
        public String toFileString() {
            String toReturn = "";

            // Abstract account information
            toReturn += getCustomerID();
            toReturn += ";" + getAccountCreationDate();
            toReturn += ";" + getAccountType();
            toReturn += ";" + getAccountID();

            // Generic loan info
            toReturn += ";" + getBalance();
            toReturn += ";" + getRate();
            toReturn += ";" + getCurrentPaymentDue();
            toReturn += ";" + getPaymentDueDate();
            toReturn += ";" + getNotifiedOfPaymentDate();
            toReturn += ";" + getLastPaymentMadeDate();
            toReturn += ";" + isMissedPayment();

            // CC information
            toReturn += ";" + getLimit();
            toReturn += ";" + getFinanceCharge();
            toReturn += ";" + getSumOfChargesThisMonth();
            // Add each message independently
            for (String chargeMessage : chargeMessages) {
                toReturn += ";" + chargeMessage;
            }

            return toReturn;
        }

        public static CC fromFileString(String s) {
            String[] split = s.split(";");

            // Abstract stuff
            String customerID = split[0];
            Date accountCreationDate = new Date(Long.parseLong(split[1]));
            AccountType abstractAccountType = AccountType.valueOf(split[2]);
            long accountID = Long.parseLong(split[3]);

            // Generic loan info
            double balance = Double.parseDouble(split[4]);
            double rate = Double.parseDouble(split[5]);
            double currentPaymentDue = Double.parseDouble(split[6]);
            Date paymentDueDate = new Date(Long.parseLong(split[7]));
            Date notifiedOfPaymentDate = new Date(Long.parseLong(split[8]));
            Date lastPaymentMadeDate = new Date(Long.parseLong(split[9]));
            boolean missedPayment = Boolean.parseBoolean(split[10]);

            // Specific
            double limit = Double.parseDouble(split[11]);
            double financeCharge = Double.parseDouble(split[12]);
            double sumOfChargesThisMonth = Double.parseDouble(split[13]);
            // Get each charge message
            ArrayList<String> chargeMessages = new ArrayList<>();
            if (split.length > 14) {
                chargeMessages.addAll(Arrays.asList(split).subList(14, split.length));
            }

            return new CC(customerID, accountCreationDate, abstractAccountType, accountID, balance, rate, currentPaymentDue, paymentDueDate, notifiedOfPaymentDate, lastPaymentMadeDate, missedPayment, limit, financeCharge, sumOfChargesThisMonth, chargeMessages);
        }

        public double getLimit() {
            return limit;
        }

        public void setLimit(double limit) {
            this.limit = limit;
        }

        public double getFinanceCharge() {
            return financeCharge;
        }

        public void setFinanceCharge(double financeCharge) {
            this.financeCharge = financeCharge;
        }

        public double getSumOfChargesThisMonth() {
            return sumOfChargesThisMonth;
        }

        public void setSumOfChargesThisMonth(double sumOfChargesThisMonth) {
            this.sumOfChargesThisMonth = sumOfChargesThisMonth;
        }

        // Returns the array of charge messages
        public ArrayList<String> getChargeMessagesArray() {
            return chargeMessages;
        }

        public void setChargeMessages(ArrayList<String> chargeMessages) {
            this.chargeMessages = chargeMessages;
        }
    }
}
