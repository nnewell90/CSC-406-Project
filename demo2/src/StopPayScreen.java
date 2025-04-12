import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopPayScreen extends JFrame {
    private JTextField accountNum;
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
        accountNum = new JTextField();
        add(accountNum);

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
        String accountNumber = accountNum.getText().trim();
        String checkNumber = check.getText().trim();

        if (accountNumber.isEmpty() || checkNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Must complete required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {

            long number = Long.parseLong(accountNumber);     //Cast the account Number string to a long
            CheckingAccount account = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, number);

            //Prolly don't need to access th customer but in case...
            Customer customer = Database.getCustomerFromList(account.customerID);

            //perform stop payment only if checkNumber is valid
            if(account.validateCheckNumber(checkNumber)) {
                account.addStopPaymentNumber(checkNumber);
            }else{
                JOptionPane.showMessageDialog(this, "Check number is invalid. Check Credentials",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            try{
                JOptionPane.showMessageDialog(this, "Stop Pay of Check Completed!");
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error Stopping Check!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }//end of else
    }

}