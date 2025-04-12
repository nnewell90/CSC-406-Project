import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DepositScreen extends JFrame {

    JTextField SSN, amount, accountID;

    public DepositScreen() {
        setTitle("Deposit To Account");
        setSize(500, 700);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SSN = new JTextField(15);
        add(new JLabel("Enter Customer SSN:"));
        add(SSN);

        accountID = new JTextField( 15);
        add(new JLabel("Enter Account ID:"));
        add(accountID);

        amount = new JTextField(15);
        add(new JLabel("Enter Deposit Amount:"));
        add(amount);

        JButton submitButton = new JButton("Submit Deposit");
        submitButton.addActionListener(e -> {
            String ssnText = SSN.getText().trim();
            long id = Long.parseLong(accountID.getText().trim());
            double depositAmount = Double.parseDouble(amount.getText().trim());

            makeDeposit(ssnText, depositAmount, id);
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new TellerScreen();
        });
        JButton returnToTellerScreen = new JButton("Return to Teller Screen");
        returnToTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});

        add(submitButton);
        add(returnToTellerScreen);

        setVisible(true);
    }


    //Method for makeDepot button...
    public void makeDeposit(String SSN, double amount, Long accountID) {
        Customer customer = Database.getCustomerFromList(SSN);

        if(customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

        if(account == null){
            JOptionPane.showMessageDialog(this, "Account not found, check credentials.");
            return;
        }

        AbstractAccount.AccountType type = account.getAccountType();
        if (type == AbstractAccount.AccountType.CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
            new TellerScreen();
        }else if (type == AbstractAccount.AccountType.SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposit Successful!");
            dispose();
        }else{
            JOptionPane.showMessageDialog(this, "Cannot make a deposit to a " + type);
        }

    }//end of make deposit

}
