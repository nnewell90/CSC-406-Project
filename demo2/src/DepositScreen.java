import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class DepositScreen extends JFrame {
    private JTextField deposit;
    private JTextField ssn;
    private JTextField account;

    public DepositScreen() {

        setTitle("Deposit to Account");
        setSize(700, 700);
        setLayout(new GridLayout(4, 1));

        // Labels and text fields
        add(new JLabel("Please enter the customer's Social Security Number: "));
        ssn = new JTextField();
        add(ssn);

        add(new JLabel("Please enter the amount to deposit: "));
        deposit = new JTextField();
        add(deposit);


        add(new JLabel("Please enter the account number you wish to deposit to: "));
        account = new JTextField();
        add(account);

        JButton submitButton = new JButton("Deposit to Account");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositAccount();
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

    private void depositAccount() {

        String SSN = ssn.getText().trim();
        double amount = Double.parseDouble(deposit.getText().trim());
        long accountID = Long.parseLong(account.getText().trim());

        Customer customer = Database.getCustomerFromList(SSN);

       if (customer == null) {
           JOptionPane.showMessageDialog(this, "Customer not found");
           return;
       }

       AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

       if(account == null) {
           JOptionPane.showMessageDialog(this, "Account not found");
           return;
       }
       AbstractAccount.AccountType type = account.getAccountType();
        if (type == AbstractAccount.AccountType.CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.deposit(amount);
            updateType(checkingAccount);        //update type of account(TMB or Gold Diamond)
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new TellerScreen();
        }else if (type == AbstractAccount.AccountType.SimpleSavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new TellerScreen();
        }else if(type == AbstractAccount.AccountType.CDSavingsAccount) {
            SavingsAccount.CDSavingsAccount cdAccount = (SavingsAccount.CDSavingsAccount) account;
            cdAccount.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new TellerScreen();

        }else if(type == AbstractAccount.AccountType.CCLoanAccount) {
            LoanAccount.CC ccAccount = (LoanAccount.CC) account;
            ccAccount.payment(amount);
            JOptionPane.showMessageDialog(this, "Payment of $" + amount + " made to Credit Card!");
            dispose();
            new TellerScreen();
        }else if(type == AbstractAccount.AccountType.ShortOrLongLoanAccount) {
            LocalDate date = LocalDate.now();
            LoanAccount.ShortOrLong loanAccount = (LoanAccount.ShortOrLong) account;
            loanAccount.makePayment(amount, date);
            JOptionPane.showMessageDialog(this, "Payment of $" + amount + " made to Loan Account!");
            dispose();
            new TellerScreen();
        }else {
            JOptionPane.showMessageDialog(this, "Cannot make a deposit to a " + type);
        }

    }

    private void updateType(CheckingAccount checking) {
        if(checking.balance >= 5000){
            checking.setAccountSpecificType(CheckingAccount.AccountType.GoldDiamond);
        }else if(checking.balance < 5000){
            checking.setAccountSpecificType(CheckingAccount.AccountType.TMB);
        }
    }

}
