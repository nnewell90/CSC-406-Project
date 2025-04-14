import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertCheckScreen extends JFrame {

    private JTextField deposit, ssn, account;

    public InsertCheckScreen() {

        setTitle("Deposit a Check");
        setSize(700, 700);
        setLayout(new GridLayout(4, 1));

        // Labels and text fields
        add(new JLabel("Please enter your Customer ID (SSN): "));
        ssn = new JTextField();
        add(ssn);

        add(new JLabel("Please enter check amount: "));
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
            new CustomerScreen();
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
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new CustomerScreen();
        }else if (type == AbstractAccount.AccountType.SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new CustomerScreen();
        }else{
            JOptionPane.showMessageDialog(this, "Cannot make a deposit to a " + type);
        }

    }


}
