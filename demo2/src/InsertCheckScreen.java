import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertCheckScreen extends JFrame {

    private JTextField amount, checkNum, ID;
    private final Customer customer;

    public InsertCheckScreen(Customer customer) {

        setTitle("Issue a Check");
        setSize(700, 700);
        setLayout(new GridLayout(4, 1));
        this.customer = customer;

        add(new JLabel("Please enter Checking Account ID: "));
        ID = new JTextField(10);
        add(ID);

        add(new JLabel("Please enter check amount: "));
        amount = new JTextField();
        add(amount);


        add(new JLabel("Please Enter Check Number: (random 2-6 digit number)"));
        checkNum = new JTextField();
        add(checkNum);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processCheck();
            }
        });
        JButton returnToCustomerButton = new JButton("Return to Customer Screen");

        returnToCustomerButton.addActionListener(e -> {
            dispose();
            new CustomerScreen(customer);
        });

        add(submitButton);
        add(returnToCustomerButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void processCheck() {

        //make sure fields are full...
        if(amount.getText().equals("") || checkNum.getText().equals("") || ID.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill in required fields");
            return;
        }

        //find account and pass values...
        long accountID = Long.parseLong(ID.getText());
        CheckingAccount account = (CheckingAccount) Database.getAccountFromList(Database.checkingAccountList, accountID);
        double amount = Double.parseDouble(this.amount.getText().trim());
        String checkNum = this.checkNum.getText().trim();

        //make sure account exist...
        if(account == null) {
            JOptionPane.showMessageDialog(this, "Account not found! Double check account ID");
            return;
        }

        //Add check to process Later...
        account.addCheckToProcessLater(-amount, checkNum);//negative because it's a withdrawal...see checking class
        JOptionPane.showMessageDialog(this, "Check will be processed soon.");
        dispose();
        new CustomerScreen(customer);

    }


}
