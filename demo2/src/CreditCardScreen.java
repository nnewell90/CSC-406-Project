import javax.swing.*;
import java.awt.*;

public class CreditCardScreen extends JFrame {
    public CreditCardScreen() {
        setTitle("Credit Card Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// allows closing this window separately

        Label l1 = new Label("Your Current Balance is $197.21.");
        Label l2 = new Label("Your Next Closing Date is April 9th, 2025.");
        Label l3 = new Label("Your Minimum Payment Due is $35.00.");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 50, 120, 80);
        l3.setBounds(100, 50, 120, 80);
        add(l1);
        add(l2);
        add(l3);
        setSize(300, 200);
        setLayout(new GridLayout(4, 1));

        JButton returntoCustomerr = new JButton("Return to Customer Screen");


        returntoCustomerr.addActionListener(e -> {
            dispose(); // closes the credit card screen
            new CustomerScreen(); // opens the customer screen again
        });

        add(returntoCustomerr);

        setVisible(true);
    }
}
