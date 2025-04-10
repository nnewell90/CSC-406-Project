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


    //Method for makeDepot button.
    //Method for
    public void makeDeposit(String SSN, double amount, Long accountID) {
        Customer customer = Database.getCustomer(SSN);

        if(customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        //Not sure how to get this method to work.
//        AbstractAccount account = Database.getAccountFromList(customer.getCustomerAccounts(), accountID);
        AbstractAccount account = Database.findAccountByID(accountID);
        AbstractAccount.AccountType type = account.getAccountType();

        if(account == null){
            JOptionPane.showMessageDialog(this, "Account not found!");
            return;
        }

        if (type == AbstractAccount.AccountType.CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.deposit(amount);
        }else if (type == AbstractAccount.AccountType.SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.deposit(amount);
        }else{
            JOptionPane.showMessageDialog(this, "Account type not supported! Account type is " + type);
        }

    }//end of make deposit

}
