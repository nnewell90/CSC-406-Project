import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreateAccountScreen extends JFrame {

    public CreateAccountScreen(Customer customer) {
        setTitle("New Account for " + customer.getFirstName() + " " + customer.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));


        formPanel.add(new JLabel("Account Creation Date: (MM/DD/YYYY)"));
        formPanel.add(new JTextField(10));
        formPanel.add(Box.createVerticalStrut(4));

        formPanel.add(new JLabel("Account Type: "));
        formPanel.add(new JTextField(15));
        formPanel.add(Box.createVerticalStrut(4));

        formPanel.add(new JLabel("Starting Deposit: "));
        formPanel.add(new JTextField(4));

        JButton submitButton = new JButton("Create Account");

        JButton returnButton = new JButton("Return to Customer Details");
        returnButton.addActionListener(e -> {dispose();new CustomerDetailsScreen(customer.getSSN());});

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(returnButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
