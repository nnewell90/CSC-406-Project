import javax.swing.*;
import java.awt.*;

public class TellerScreen extends JFrame {
    public TellerScreen() {
        setTitle("Teller Screen");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton newAccountButton = new JButton("Create a New Account");
        JButton newCustomerButton = new JButton("Create a New Customer");
        JButton linkButton = new JButton("Link Accounts");
        JButton withdrawButton = new JButton("Withdraw from Account");
        JButton stopButton = new JButton("Stop Payment");
        JButton reviewButton = new JButton("Review Customer Accounts");
        JButton returnButton = new JButton("Return to Main Menu");

        newAccountButton.addActionListener(e -> new NewAccountScreen());
        newCustomerButton.addActionListener(e -> new NewCustomerScreen());
        linkButton.addActionListener(e -> new LinkAccountsScreen());
        withdrawButton.addActionListener(e -> new WithdrawScreen());
        stopButton.addActionListener(e -> new StopPayScreen());
        reviewButton.addActionListener(e -> new CustomerReviewScreen());
        returnButton.addActionListener(e -> new MainMenu());

        add(newAccountButton);
        add(newCustomerButton);
        add(linkButton);
        add(withdrawButton);
        add(stopButton);
        add(reviewButton);
        add(returnButton);

        setVisible(true);
    }
}
