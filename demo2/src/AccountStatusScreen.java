import javax.swing.*;
import java.awt.*;

public class AccountStatusScreen extends JFrame {
    public AccountStatusScreen() {
        setTitle("Account Details");
        Label l1 = new Label("Your Checking Account Available Balance is: $146.83");
        Label l2 = new Label("Your Savings Account Available Balance is: $1344.92");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 50, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JButton returntoCustomerScreen = new JButton("Return to Customer Screen");

        returntoCustomerScreen.addActionListener(e -> {
            dispose();
            new CustomerScreen();
        });
        add(returntoCustomerScreen);

        setVisible(true);
    }
}

