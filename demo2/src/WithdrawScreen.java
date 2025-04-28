import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

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
        add(ssn);

        add(new JLabel("Please enter the amount to withdraw: "));
        withdraw = new JTextField();
        add(withdraw);


        add(new JLabel("Please enter the account number you wish to withdraw from: "));
        account = new JTextField();
        add(account);

        JButton submitButton = new JButton("Withdraw from Account");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String SSN = ssn.getText().trim();
                double amount = Double.parseDouble(withdraw.getText().trim());
                long accountID = Long.parseLong(account.getText().trim());


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

        }else if (type == AbstractAccount.AccountType.CheckingAccount){//If withdrawing from a checking account

            CheckingAccount checking = (CheckingAccount) account;
            Long overDraftAccountID = checking.overDraftAccountID;

            SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);
            double balance = checking.getBalance();

            if (balance >= amount) {
                checking.withdraw(amount);
                JOptionPane.showMessageDialog(this, "Withdrawal successful!");
            }else if (overdraftAccount != null && (balance + overdraftAccount.getBalance() >= amount)) {
                checking.withdraw(amount);
                JOptionPane.showMessageDialog(this, "Withdrawal successful! Overdraft protection used.");
            }else {
                JOptionPane.showMessageDialog(this, "Withdrawal denied. Insufficient funds in checking and savings.");
                return;
            }

            dispose();
            new TellerScreen();

        }else if (type == AbstractAccount.AccountType.CDSavingsAccount){//if withdrawing from a CD
            SavingsAccount.CDSavingsAccount cdAccount = (SavingsAccount.CDSavingsAccount) account;
            LocalDate today = LocalDate.now();

            //Don't let them withdraw too much from the cd account
            if (cdAccount.balance >= amount) {

                if (today.isAfter(cdAccount.dueDate) || today.equals(cdAccount.dueDate)) {

                    cdAccount.withdraw(amount);
                    JOptionPane.showMessageDialog(this, "Withdraw successful!");
                    dispose();
                    new TellerScreen();
                }else if(today.isBefore(cdAccount.dueDate)) {//if they withdraw before the maturation date, proceed but penalize
                    
                    //apply "penalty"
                    cdAccount.withdraw(amount);
                    JOptionPane.showMessageDialog(this, "Withdraw successful! However, " +
                            "Penalty was applied for withdrawing before " + cdAccount.dueDate);
                    dispose();
                    new TellerScreen();
                }
            } else if (amount > cdAccount.balance) {//if they don't have enough, don't bother checking the date
                JOptionPane.showMessageDialog(this, "Insufficient funds! You only have $" +
                        cdAccount.balance+" in Certificate of Deposit.");
            }

        }else{//if withdrawing from an account that isn't checking or savings, then deny the withdrawal
            JOptionPane.showMessageDialog(this, "You cannot withdraw from a "+ type);
        }
    }//end of makeWithdrawal

    private void updateType(CheckingAccount checking) {
        if(checking.balance >= 5000){
            checking.setAccountSpecificType(CheckingAccount.AccountType.GoldDiamond);
        }else if(checking.balance < 5000){
            checking.setAccountSpecificType(CheckingAccount.AccountType.TMB);
        }
    }

}




