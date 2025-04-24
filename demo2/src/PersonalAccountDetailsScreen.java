import javax.swing.*;
import java.awt.*;


public class PersonalAccountDetailsScreen extends JFrame {
    public PersonalAccountDetailsScreen(String accountID) {
        setSize(700, 500);
        long idLong = Long.parseLong(accountID);
        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, idLong);

        setTitle("Account Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        assert account != null;
        JLabel accountIDLabel = new JLabel("Account ID: " + accountID);
        JLabel balanceLabel = new JLabel("Balance: " + accountBalance(account));
        JLabel typeLabel = new JLabel("Account Type: " + account.getAccountType());
        JLabel dateLabel = new JLabel("Creation Date: " + account.getAccountCreationDate());


        accountIDLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Customer customer = Database.getCustomerFromList(account.getCustomerID());


        //Return Buttons
        JButton returnToAccounts = new JButton("Return to Accounts");
        JButton returnToCustomer = new JButton("Return to Customer Screen");

        returnToAccounts.addActionListener(e -> {
            dispose();
            new PersonalAccountsScreen(customer);
        });

        returnToCustomer.addActionListener(e -> {
            dispose();
            new CustomerScreen(customer);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // or BoxLayout if you want stacked
        buttonPanel.add(returnToAccounts);
        buttonPanel.add(returnToCustomer);


        panel.add(accountIDLabel);
        panel.add(balanceLabel);
        panel.add(typeLabel);
        panel.add(dateLabel);
        add(buttonPanel, BorderLayout.SOUTH);

        if(account instanceof CheckingAccount checking){
            checkingAccountInfo(panel, checking);
        }else if (account instanceof SavingsAccount savings){
            savingsAccountInfo(panel, savings);
        } else if(account instanceof LoanAccount loan){
            loanAccountInfo(panel, loan);
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private double accountBalance(AbstractAccount account){

        if(account.getAccountType() == AbstractAccount.AccountType.CheckingAccount){
            CheckingAccount checkingAccount = (CheckingAccount) account;
            return checkingAccount.getBalance();
        }else if(account.getAccountType() == AbstractAccount.AccountType.SavingsAccount){
            SavingsAccount savingsAccount = (SavingsAccount) account;
            return savingsAccount.getBalance();
        }else if(account.getAccountType() == AbstractAccount.AccountType.LoanAccount){
            LoanAccount loanAccount = (LoanAccount) account;
            return loanAccount.getBalance();
        }

        return 0;
    }

    private void checkingAccountInfo(JPanel panel, CheckingAccount account){

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        String type = String.valueOf(account.getAccountSpecificType());
        detailsPanel.add(new JLabel(type));
        if(type.equals(CheckingAccount.AccountType.GoldDiamond)){//GD accounts have an interest rate, so display it
            detailsPanel.add(new JLabel("Interest Rate: " + account.getInterestRate()));
        }

        if(account.linkedToATMCard){
            detailsPanel.add(new JLabel("Linked To ATM Card: "));
            ATMCard atmCard = account.getLinkedATMCard();
            detailsPanel.add(new JLabel(atmCard.toString()));
        }

        if(account.overDraftAccountID != -1){
            detailsPanel.add(new JLabel("Overdraft Account: "));
            SavingsAccount savingsAccount = account.getOverDraftAccount();
            detailsPanel.add(new JLabel(savingsAccount.toString()));
        }

        panel.add(detailsPanel);
        setVisible(true);
    }

    private void savingsAccountInfo(JPanel panel, SavingsAccount account){
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        if(account instanceof SavingsAccount.SimpleSavingsAccount){
            SavingsAccount.SimpleSavingsAccount simpleSavingsAccount = (SavingsAccount.SimpleSavingsAccount) account;
            detailsPanel.add(new JLabel("Simple Savings Account"));
            detailsPanel.add(new JLabel("Rate: " + simpleSavingsAccount.getInterestRate()));
            if(simpleSavingsAccount.overdraftForAccountID != -1) {
                detailsPanel.add(new JLabel("Overdraft for account: " + simpleSavingsAccount.overdraftForAccountID));
            }

        }else if(account instanceof SavingsAccount.CDSavingsAccount){
            SavingsAccount.CDSavingsAccount cdsSavingsAccount = (SavingsAccount.CDSavingsAccount) account;
            detailsPanel.add(new JLabel("CDS Savings Account"));
            detailsPanel.add(new JLabel("Rate: " + cdsSavingsAccount.getInterestRate()));
            detailsPanel.add(new JLabel("Due Date: " + cdsSavingsAccount.getDueDate()));

        }

        panel.add(detailsPanel);
        setVisible(true);

    }

    private void loanAccountInfo(JPanel panel, LoanAccount account){
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(new JLabel("Rate: " + account.getRate()));
        detailsPanel.add(new JLabel("Current Payment Due: " + account.getCurrentPaymentDue()));
        detailsPanel.add(new JLabel("Last Payment Made: " + account.lastPaymentMadeDate));

        if(account instanceof LoanAccount.CC){
            LoanAccount.CC ccAccount = (LoanAccount.CC) account;
            detailsPanel.add(new JLabel("limit: " + ccAccount.getLimit()));
            detailsPanel.add(new JLabel("Finance Charge: " + ccAccount.getFinanceCharge()));
            detailsPanel.add(new JLabel("Sum of Charges this month: " + ccAccount.sumOfChargesThisMonth));
            detailsPanel.add(new JLabel(ccAccount.getChargeMessagesString()));

        }

        panel.add(detailsPanel);
        setVisible(true);

    }
}
