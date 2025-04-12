import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LinkAccountsScreen extends JFrame {
    public LinkAccountsScreen() {
        setTitle("Link Accounts");
        Label l1 = new Label("Please enter the Checking Account number:");
        Label l2 = new Label("Please enter the Savings Account number you want to Link:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField checkingAccountField = new JTextField(10);
        JTextField savingsAccountField = new JTextField(10);

        String CA = checkingAccountField.getText().trim();
        String SA = savingsAccountField.getText().trim();
        JButton returnToTellerScreen = new JButton("Return to Teller Screen");
        JButton submitButton = new JButton("Link Accounts");
        submitButton.addActionListener(e -> LinkAccounts(CA, SA));




        returnToTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        add(checkingAccountField);
        add(savingsAccountField);
        add(returnToTellerScreen);
        add(submitButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        setVisible(true);
    }

    //Method for linking a checking account to a savings account
    public void LinkAccounts(String CheckAccount, String SaveAccount) {

        if (CheckAccount.isEmpty() || SaveAccount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the required fields.");
        } else {
            Long account1 = Long.parseLong(CheckAccount);
            Long account2 = Long.parseLong(SaveAccount);


            CheckingAccount checkingAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, account1);
            SavingsAccount savingsAccount = (SavingsAccount) Database.getAccountFromList(Database.savingsAccountList, account2);

            if(checkingAccount == null) {
                JOptionPane.showMessageDialog(this, "Checking Account not found, double-check account ID");
            } else if(savingsAccount == null) {
                JOptionPane.showMessageDialog(this, "Saving Account not found, double-check account ID");
            }else {
                //Link the Savings account to the Checking account
                checkingAccount.overdraftAccount = (SavingsAccount.SimpleSavingsAccount) savingsAccount;
                JOptionPane.showMessageDialog(this, "Accounts have been Linked Successfully!");
                dispose();
                new TellerScreen();
            }
        }
    }//end of linkAccounts

}
