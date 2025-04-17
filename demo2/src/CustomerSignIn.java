import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerSignIn extends JFrame {

    JTextField customerID;
    public CustomerSignIn() {
        setTitle("Sign In");
        setSize(700, 700);
        setLayout(new GridLayout(4, 1));

        add(new JLabel("Customer ID (SSN):"));
        customerID = new JTextField(10);
        add(customerID);

        JButton returntoCustomerScreen = new JButton("Return to Customer Screen");
        JButton login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginCustomer();
            }
        });

        returntoCustomerScreen.addActionListener(e -> {
            dispose();
            new CustomerScreen();
        });

        add(login);
        add(returntoCustomerScreen);

        setVisible(true);
    }

    private void loginCustomer() {
        if(customerID.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Customer ID");
            return;
        }

        Customer customer = Database.getCustomerFromList(customerID.getText());

        if(customer == null) {
            JOptionPane.showMessageDialog(this, "Invalid Customer ID");
            return;
        }

        new CustomerDetailsScreen(customer);
        dispose();
    }
}

