import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class AccountDetailsScreen extends JFrame {
    public AccountDetailsScreen(String id) {
        setSize(700, 500);
        long accountID = Long.parseLong(id);
        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

        setTitle("Account Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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
            String customerID = account.getCustomerID();
            Customer customer = Database.getCustomerFromList(customerID);
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

        //TODO -display account transactions if its a checking account... or not.
        if(account instanceof CheckingAccount checking){
            checkingAccountInfo(panel, checking);
        }else if(account instanceof LoanAccount loan){
            loanAccountInfo(panel, loan);
        }

        add(panel, BorderLayout.CENTER);
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
//        JList<String> list = new JList("CCaccountdetails.txt").toArray(new String[0]);
//
//        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
//        panel.add(Box.createVerticalStrut(10)); // spacing
//        panel.add(scrollPane);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        String type = String.valueOf(account.getAccountSpecificType());
        detailsPanel.add(new JLabel(type));
        detailsPanel.add(new JLabel("DISPLAY ACCOUNT TRANSACTIONS??? "));


        panel.add(detailsPanel);
        setVisible(true);
    }

    private void loanAccountInfo(JPanel panel, LoanAccount account){

    }



}
