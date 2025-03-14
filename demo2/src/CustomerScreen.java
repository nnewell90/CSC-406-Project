import javax.swing.*;
import java.awt.*;

public class CustomerScreen extends JFrame {
    public CustomerScreen() {
        setTitle("Customer Screen");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton creditButton = new JButton("Credit Card");
        JButton reviewButton = new JButton("Review Account Status");
        JButton checkButton = new JButton("Input Checks");
        JButton returnButton = new JButton("Return to Main Menu");

        creditButton.addActionListener(e -> new CreditCardScreen());
        reviewButton.addActionListener(e -> new AccountStatusScreen());
        checkButton.addActionListener(e -> new InsertCheckScreen());
        returnButton.addActionListener(e -> new MainMenu());

        add(creditButton);
        add(reviewButton);
        add(checkButton);
        add(returnButton);

        setVisible(true);
    }
}
