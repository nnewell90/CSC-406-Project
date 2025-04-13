import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class WithdrawScreen extends JFrame {
    private JTextField withdraw;
    private JTextField ssn;
    private JTextField account;

    public WithdrawScreen() {

        setTitle("Withdraw From Account");
        /*Label l1 = new Label("Please select an account type to withdraw from:");
        l1.setBounds(100, 50, 120, 80);
        add(l1);*/
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        // Labels and text fields
        add(new JLabel("Please enter the customer's Social Security Number: "));
        ssn = new JTextField();
        String SSN = ssn.getText().trim();
        add(ssn);

        add(new JLabel("Please enter the amount to withdraw: "));
        withdraw = new JTextField();
        Double amount = Double.parseDouble(withdraw.getText().trim());
        add(withdraw);


        add(new JLabel("Please enter the account number you wish to withdraw from: "));
        account = new JTextField();
        Long accountID = Long.parseLong(account.getText().trim());
        add(account);

        JButton submitButton = new JButton("Withdraw from Account");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeWithdrawal(SSN, amount, accountID);
            }
        });
        JButton returntoButton = new JButton("Return to Teller Screen");

        returntoButton.addActionListener(e -> {
            dispose();
            new TellerScreen();
        });

        add(submitButton);
        add(returntoButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void withdrawAccount() {
        String withdrawAccount = withdraw.getText().trim();
        String accountNumber = account.getText().trim();
        String socialsecurity = ssn.getText().trim();

        if (withdrawAccount.isEmpty() || accountNumber.isEmpty() || socialsecurity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Must complete required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {

            //Create the Customer Object and add it to the Array List
            //That way we can access the customer objects methods and what not later on
            // Account account = new Account(account1,account2);
            //Database.addItemToList(Database.accountList, account);

            // Store the data to database
            // might have to change txt name - not created yet!!!!!!!!!!!!
            try{ /*(FileWriter writer = new FileWriter("customers.txt", true)) {
                writer.write(withdraw + ";" + account + ";" + ssn + ";" + "\n");*/
                JOptionPane.showMessageDialog(this, "Withdrawal Completed Successfully!");
                //dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error Withdrawing from Account!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }//end of else
    }

    public void makeWithdrawal(String SSN, double amount, Long accountID){
        Customer customer = Database.getCustomerFromList(SSN);
        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

        if(customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found! Check Customer ID");
            return;
        }else if(account == null) {
            JOptionPane.showMessageDialog(this, "Account not found! Check Account ID");
            return;
        }

        AbstractAccount.AccountType type = account.accountType;

        if (type == AbstractAccount.AccountType.SavingsAccount){ //If withdrawing from a Savings Account
            SavingsAccount savings = (SavingsAccount) account;

            //Don't let them withdraw too much from the savings account
            if (savings.balance >= amount) {
                savings.withdraw(amount);
                JOptionPane.showMessageDialog(this, "Withdraw successful!");
                dispose();
                new TellerScreen();
            } else if (amount > savings.balance) {
                JOptionPane.showMessageDialog(this, "Insufficient funds! You only have $" +
                        savings.balance+" in Savings.");
            }

        }else if (type == AbstractAccount.AccountType.CheckingAccount){//If withdrawing from a savings account

            CheckingAccount checking = (CheckingAccount) account;
            Long overDraftAccountID = checking.overDraftAccountID;

            SavingsAccount linkedSavings = (SavingsAccount) Database.getAccountFromList(Database.abstractAccountList, overDraftAccountID);

            if(checking.balance >= amount) {//If they have enough in checking, perform a normal withdrawal
                checking.withdraw(amount);
                JOptionPane.showMessageDialog(this, "Withdraw of $" + amount + " successful! " +
                        "You only have $" + checking.balance+" left in this Checking Account");
                //close the screen and open a new teller screen
                dispose();
                new TellerScreen();

            }else if (checking.balance < amount) {//if the checking doesn't have enough funds

                //check if they have a linked Savings
                if(linkedSavings != null) {

                    //check there is enough in checking and savings
                    if(checking.balance+linkedSavings.balance > amount) {//if there isn't...
                        JOptionPane.showMessageDialog(this, "Insufficient funds in both checking and linked Savings!");
                    }else if (checking.balance+linkedSavings.balance >= amount){//if there is enough in the savings
                        //perform the withdrawal but pull from linked savings
                        linkedSavings.withdraw(amount);
                        JOptionPane.showMessageDialog(this, "Withdraw successful, however overdraft process was applied.");
                        dispose();
                        new TellerScreen();
                    }

                }else {//if no linked savings, deny the withdrawal
                    JOptionPane.showMessageDialog(this, "Insufficient funds! You only have $" +
                            checking.balance+" in Checking Account.");
                }

            }

        }else if (type == AbstractAccount.AccountType.CDSavingsAccount){//if withdrawing from a CD
            SavingsAccount.CDSavingsAccount cdAccount = (SavingsAccount.CDSavingsAccount) account;
            Date Date = new Date();

            //Don't let them withdraw too much from the cd account
            if (cdAccount.balance >= amount) {

                if (Date.after(cdAccount.dueDate) || Date.equals(cdAccount.dueDate)) {

                    cdAccount.withdraw(amount);
                    JOptionPane.showMessageDialog(this, "Withdraw successful!");
                    dispose();
                    new TellerScreen();
                }else if(Date.before(cdAccount.dueDate)) {//if they withdraw before the maturation date, proceed but penalize

                    //TODO apply penalty for withdrawing before the due date!!!**************************************************************************
                    cdAccount.withdraw(amount);
                    JOptionPane.showMessageDialog(this, "Withdraw successful! However, " +
                            "Penalty was applied for withdrawing before " + cdAccount.dueDate);
                    dispose();
                    new TellerScreen();
                }
            } else if (amount > cdAccount.balance) {//if they dont have enough, dont bother checking the date
                JOptionPane.showMessageDialog(this, "Insufficient funds! You only have $" +
                        cdAccount.balance+" in Certificate of Deposit.");
            }

        }else{//if withdrawing from an account that isn't checking or savings, then deny the withdrawal
            JOptionPane.showMessageDialog(this, "You cannot withdraw from a "+ type);
        }
    }//end of makeWithdrawal

}




