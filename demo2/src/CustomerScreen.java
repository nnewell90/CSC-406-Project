import javax.swing.*;
import java.awt.*;

public class CustomerScreen extends JFrame {
    public CustomerScreen() {
        setTitle("Customer Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton creditButton = new JButton("Credit Card");
        JButton reviewButton = new JButton("Review Account Status");
        JButton checkButton = new JButton("Input Checks");
        JButton returnButton = new JButton("Return to Controller");

        creditButton.addActionListener(e -> new SystemControllerScreen());
        reviewButton.addActionListener(e -> new TellerScreen());
        checkButton.addActionListener(e -> new ManagerScreen());
        returnButton.addActionListener(e -> new SystemControllerScreen());

        add(creditButton);
        add(reviewButton);
        add(checkButton);
        add(returnButton);

        setVisible(true);
    }
}
