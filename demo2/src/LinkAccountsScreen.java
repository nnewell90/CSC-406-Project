import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

public class LinkAccountsScreen extends JFrame {
    private JTextField checkingAccount, savingsAccount;

    public LinkAccountsScreen() {
        setTitle("Link Accounts");
        setSize(500, 500);
        setLayout(new GridLayout(4, 2)); // change this for fields?

        // Labels and text fields
        add(new JLabel("Please enter your checking account number: "));
        checkingAccount = new JTextField();
        add(checkingAccount);

        add(new JLabel("Please enter your savings account number: "));
        savingsAccount = new JTextField();
        add(savingsAccount);



        JButton submitButton = new JButton("Link Accounts");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LinkAccounts();
            }
        });
        JButton returntoButton = new JButton("Return to Teller Screen");

        returntoButton.addActionListener(e -> {dispose(); new TellerScreen();});

        add(submitButton);
        add(returntoButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    //Method for linking a checking account to a savings account
    public void LinkAccounts() {
        String checkAccount = checkingAccount.getText();
        String saveAccount = savingsAccount.getText();

        if (checkAccount.isEmpty() || saveAccount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the required fields.");
        } else {
            Long account1 = Long.parseLong(checkAccount);
            Long account2 = Long.parseLong(saveAccount);


            CheckingAccount checkingAccount = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, account1);
            SavingsAccount savingsAccount = (SavingsAccount) Database.getAccountFromList(Database.savingsAccountList, account2);

            if(checkingAccount == null) {
                JOptionPane.showMessageDialog(this, "Checking Account not found, double-check account ID");
            } else if(savingsAccount == null) {
                JOptionPane.showMessageDialog(this, "Saving Account not found, double-check account ID");
            }else {
                //Link the Savings account to the Checking account
                checkingAccount.setOverdraftForAccount(account2);
                JOptionPane.showMessageDialog(this, "Accounts have been Linked Successfully!");
                dispose();
                new TellerScreen();
            }
        }
    }

}