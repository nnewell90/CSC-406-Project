import javax.swing.*;
import java.awt.*;

public class CustomerScreen extends JFrame {
    public CustomerScreen() {
        setTitle("Customer Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton creditButton = new JButton("Credit Card");
        JButton reviewButton = new JButton("Review Account Status");
        JButton checkButton = new JButton("Input Checks");
        JButton returnButton = new JButton("Return to Main Menu");


        creditButton.addActionListener(e -> {
            dispose();
            new CreditCardScreen();
        });
        reviewButton.addActionListener(e -> {
            dispose();
            new AccountStatusScreen();
        });
        checkButton.addActionListener(e -> {
            dispose();
            new InsertCheckScreen();
        });
        returnButton.addActionListener(e -> {
            dispose();
            new SystemControllerScreen();
        });


        add(creditButton);
        add(reviewButton);
        add(checkButton);
        add(returnButton);

        setVisible(true);
    }
}
