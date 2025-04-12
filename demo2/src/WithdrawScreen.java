import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class WithdrawScreen extends JFrame {
    public WithdrawScreen() {
        setTitle("Withdraw From Account");
        Label l1 = new Label("Please enter the amount to withdraw:");
        Label l2 = new Label("Please select an account type to withdraw from:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        String a = l1.getText().trim();
        double amount = Double.parseDouble(a);

        JTextField accountholderField = new JTextField(10);
        JRadioButton checkingButton = new JRadioButton("Checking");
        JRadioButton savingsButton = new JRadioButton("Savings");
        JRadioButton cdButton = new JRadioButton("Certificate of Deposit");
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");


        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(checkingButton);
        accountGroup.add(savingsButton);
        accountGroup.add(cdButton);

        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        //submitButton.addActionListener(e -> makeWithdrawal(SSN, amount, accountID));
        add(accountholderField);


        JPanel radioPanel = new JPanel();
        radioPanel.add(checkingButton);
        radioPanel.add(savingsButton);
        radioPanel.add(cdButton);
        add(radioPanel);
        add(submitButton);
        add(returntoTellerScreen);


        setVisible(true);
    }

    public void makeWithdrawal(String SSN, double amount, Long accountID, String date){
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
            SavingsAccount linkedSavings = checking.overdraftAccount;

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            Date Date = java.sql.Date.valueOf(localDate);

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
