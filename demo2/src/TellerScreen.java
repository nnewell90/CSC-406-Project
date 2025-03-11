import javax.swing.*;
import java.awt.*;

public class TellerScreen extends JFrame {
    public TellerScreen() {
        setTitle("Teller Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton systemButton = new JButton("Create a New Account");
        JButton tellerButton = new JButton("Create a New Customer");
        JButton managerButton = new JButton("Link Accounts");
        JButton customerButton = new JButton("Withdraw from Account");
        JButton stopButton = new JButton("Stop Payment");
        JButton reviewButton = new JButton("Review Customer Accounts");
        JButton returnButton = new JButton("Return to Controller");

        systemButton.addActionListener(e -> new SystemControllerScreen());
        tellerButton.addActionListener(e -> new TellerScreen());
        managerButton.addActionListener(e -> new LoanInterest());
        customerButton.addActionListener(e -> new CustomerScreen());
        stopButton.addActionListener(e -> new CustomerScreen());
        reviewButton.addActionListener(e -> new CustomerScreen());
        returnButton.addActionListener(e -> new SystemControllerScreen());

        add(systemButton);
        add(tellerButton);
        add(managerButton);
        add(customerButton);
        add(stopButton);
        add(reviewButton);
        add(returnButton);

        setVisible(true);
    }
}
