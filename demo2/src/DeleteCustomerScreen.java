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
        Database.removeItemFromList(Database.customerList, customer);
        JOptionPane.showMessageDialog(null, "Customer Deleted Successfully!" + deleted);

        dispose();
        new TellerScreen();
    }

}
