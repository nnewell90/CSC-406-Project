import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerScreen extends JFrame {
    public CustomerScreen(Customer customer) {
        setTitle("Customer Screen for " + customer.getFirstName() + " " + customer.getLastName());
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JButton creditButton = new JButton("Credit Card");
        JButton reviewButton = new JButton("Review Accounts");
        JButton issueCheckButton = new JButton("Issue a Check");
        JButton returnButton = new JButton("Return to Sign In");


        creditButton.addActionListener(e -> {
            dispose();
            new CreditCardDetailScreen(customer);
        });
        reviewButton.addActionListener(e -> {
            dispose();
            new PersonalAccountsScreen(customer);
        });
        issueCheckButton.addActionListener(e -> {
            dispose();
            new InsertCheckScreen(customer);
        });
        returnButton.addActionListener(e -> {
            dispose();
            new CustomerSignIn();
        });


        add(creditButton);
        add(reviewButton);
        add(issueCheckButton);
        add(returnButton);

        setVisible(true);
    }

}
