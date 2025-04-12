import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopPayScreen extends JFrame {
    private JTextField account;
    private JTextField check;


    public StopPayScreen() {

        setTitle("Stop Payment of Check");
        /*Label l1 = new Label("Please select an account type to withdraw from:");
        l1.setBounds(100, 50, 120, 80);
        add(l1);*/
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        // Labels and text fields
        add(new JLabel("Please enter the customer's account number: "));
        account = new JTextField();
        add(account);

        add(new JLabel("Please enter the check number to stop: "));
        check = new JTextField();
        add(check);


        JButton submitButton = new JButton("Stop Payment of Check");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopPayment();
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

    private void stopPayment() {
        String accountNumbe = account.getText().trim();
        String checkNumber = check.getText().trim();

        if (accountNumbe.isEmpty() || checkNumber.isEmpty()) {
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
                JOptionPane.showMessageDialog(this, "Stop Pay of Check Completed!");
                //dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error Stopping Check!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }//end of else
    }
}