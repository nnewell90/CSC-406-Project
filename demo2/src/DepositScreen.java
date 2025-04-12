import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepositScreen extends JFrame {
    private JTextField deposit;
    private JTextField ssn;
    private JTextField account;

    public DepositScreen() {

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
        String withdrawAccount = deposit.getText().trim();
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
                JOptionPane.showMessageDialog(this, "Deposit Completed Successfully!");
                //dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error Despositing to Account!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }//end of else
    }
}