import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

public class LinkAccountsScreen extends JFrame {
    private JTextField account1, account2;

    public LinkAccountsScreen() {
        setTitle("New Customer");
        setSize(500, 500);
        setLayout(new GridLayout(4, 2)); // change this for fields?

        // Labels and text fields
        add(new JLabel("Please enter your checking account number: "));
        account1 = new JTextField();
        add(account1);

        add(new JLabel("Please enter your savings account number: "));
        account2 = new JTextField();
        add(account2);


        JButton submitButton = new JButton("Link Accounts");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkAccounts();
            }
        });
        JButton returntoButton = new JButton("Return to Teller Screen");

        returntoButton.addActionListener(e -> {dispose(); new TellerScreen();});

        add(submitButton);
        add(returntoButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void linkAccounts() {
        String checkingAccount = account1.getText().trim();
        String savingsAccount = account2.getText().trim();

        if (checkingAccount.isEmpty() || savingsAccount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Must complete required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {

            //Create the Customer Object and add it to the Array List
            //That way we can access the customer objects methods and what not later on
           // Account account = new Account(account1,account2);
            //Database.addItemToList(Database.accountList, account);

            // Store the data to database
            // might have to change txt name - not created yet!!!!!!!!!!!!
            try (FileWriter writer = new FileWriter("customers.txt", true)) {
                writer.write(account1 + ";" + account2 + ";" + "\n");
                JOptionPane.showMessageDialog(this, "Accounts Linked Successfully!");
                //dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error linking accounts!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }//end of else
    }

}