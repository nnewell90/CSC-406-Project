import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteCustomerScreen extends JFrame {
    private JTextField accountNUM;


    public DeleteCustomerScreen() {

        setTitle("Delete a Customer");
        /*Label l1 = new Label("Please select an account type to withdraw from:");
        l1.setBounds(100, 50, 120, 80);
        add(l1);*/
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        // Labels and text fields
        add(new JLabel("Please enter the customer's SSN: "));
        accountNUM = new JTextField();
        add(accountNUM);


        JButton deleteButton = new JButton("Delete Customer");
        deleteButton.addActionListener(e -> {
            deleteCustomer();
        });
        JButton returntoButton = new JButton("Return to Teller Screen");

        returntoButton.addActionListener(e -> {
            dispose();
            new TellerScreen();
        });

        add(deleteButton);
        add(returntoButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void deleteCustomer() {

        if(accountNUM.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the SSN: ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = Database.getCustomerFromList(accountNUM.getText());
        if(customer == null) {
            JOptionPane.showMessageDialog(null, "Customer not Found! Double-check SSN.");
            return;
        }

        double deleted = Customer.deleteCustomer(customer);
        if (deleted > 0) {//customer owes bank
            JOptionPane.showMessageDialog(this, "Customer owes bank $" + deleted);
        }else if (deleted < 0 ){//bank owes customer
            JOptionPane.showMessageDialog(this, "Bank owes Customer $" + -deleted);
        }else{//No one owes
            JOptionPane.showMessageDialog(this, "No outstanding Balance.");
        }
        JOptionPane.showMessageDialog(this, "Customer Deleted Successfully!");

        dispose();
        new TellerScreen();
    }

}
