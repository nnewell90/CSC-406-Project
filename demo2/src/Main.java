import javax.swing.*;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
        //Creating a new customer
        Customer customer = new Customer("458254696","5 Butterfield Rd","Agency","MO","64401", "Troy", "Rush");
        //Add customer to the database
        Database.addItemToList(Database.customerList, customer);

        //Creating a checking account for the customer
        CheckingAccount gdAccount = new CheckingAccount("458254696", new Date(), AbstractAccount.AccountType.CheckingAccount, 6000.00, CheckingAccount.AccountType.GoldDiamond);
        //Adding account to database
        Database.addItemToList(Database.checkingAccountList, gdAccount);
        //Add the checking account to the customer
        customer.addAccountToCustomerAccounts(gdAccount.getAccountID());

        //Create a savings account
        SavingsAccount.SimpleSavingsAccount savingsAccount = new SavingsAccount.SimpleSavingsAccount("458254696",new Date(),3000);
        //Add savings to database
        Database.addItemToList(Database.simpleSavingsAccountList, savingsAccount);
        //Add account to the customer
        customer.addAccountToCustomerAccounts(savingsAccount.getAccountID());

        //Link savings to GD
        gdAccount.setOverdraftForAccount(savingsAccount);

        //Withdrawal from checking to see if it changes from GD to TMB
        gdAccount.withdraw(3000.00);
        System.out.println("Checking Account Balance: " + gdAccount.getBalance());

        //Overdraw the checking to see if the linking for overdraft works correctly
        gdAccount.withdraw(5000.00);
        //Check the balances
        System.out.println("Checking Account Balance: " + gdAccount.getBalance());
        System.out.println("Savings Account Balance: " + savingsAccount.getBalance());

        //Now Delete the checking account
        CheckingAccount.deleteAccount(gdAccount);
        System.out.println("Accounts: " + customer.getCustomerAccounts());

        //Now write 3 checks off the customers checking now it shouldn't allow this since we have deleted the checking account I think
        gdAccount.withdrawByCheck(500, "001");
        gdAccount.withdrawByCheck(200, "002");
        gdAccount.withdrawByCheck(100,"003");
        //Check the balances
        System.out.println("Checking Account Balance: " + gdAccount.getBalance());
        System.out.println("Savings Account Balance: " + savingsAccount.getBalance());

        //Now put a stop pay on one of the checks
        gdAccount.addStopPaymentNumber("001");
        //Confirm the check is stopped by writing another check with the same check number
        gdAccount.withdrawByCheck(150,"001");

        //Create a credit card for the customer
        LoanAccount.CC ccAccount = new LoanAccount.CC("458254696",new Date(), AbstractAccount.AccountType.CCLoanAccount,0.0,0.15,0.0,5000.00);
        ccAccount.setRate(0.18);
        //Confirm the account was created and the rate changed with setRate() function
        System.out.println("Loan Account: " + ccAccount.getAccountType());
        System.out.println("Rate: " + ccAccount.getRate());

        //Purchase something using the Credit Card
        ccAccount.charge(300.00, "Graphics Card", new Date());
        System.out.println("Statement: "  + ccAccount.getChargeMessagesString());
    }
}