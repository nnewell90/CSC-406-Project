import javax.swing.*;
import java.awt.*;

public class AccountDetailsScreen extends JFrame {
    public AccountDetailsScreen(String id) {
        setSize(700, 500);
        long accountID = Long.parseLong(id);
        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

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


        //Return Buttons
        JButton returnToCustomer = new JButton("Return to Customer Details");
        JButton returnToTeller = new JButton("Return to Teller");

        returnToCustomer.addActionListener(e -> {
            Customer customer = Database.getCustomerFromList(account.getCustomerID());
            dispose();
            new CustomerDetailsScreen(customer);
        });

        returnToTeller.addActionListener(e -> {
            dispose();
            new TellerScreen();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // or BoxLayout if you want stacked
        buttonPanel.add(returnToCustomer);
        buttonPanel.add(returnToTeller);


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
        if(account.getAccountSpecificType() == CheckingAccount.AccountType.GoldDiamond){//GD accounts have an interest rate, so display it
            detailsPanel.add(new JLabel("Interest Rate: " + account.getInterestRate()));
        }

        ATMCard atmCard = account.getLinkedATMCard();
        detailsPanel.add(new JLabel("Linked To ATM Card: " + atmCard));


        SavingsAccount savingsAccount = account.getOverDraftAccount();
        detailsPanel.add(new JLabel("Overdraft Account: " + savingsAccount));


        panel.add(detailsPanel);
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
    }



}
