import javax.swing.*;
import java.awt.*;

public class ManagerScreen extends JFrame {
    public ManagerScreen() {
        setTitle("Manager Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton loanButton = new JButton("Loan Interest Rates");
        JButton cdButton = new JButton("CD Interest Rates");
        JButton cdmanageButton = new JButton("CD Manager");
        JButton checksButton = new JButton("Checks Processing");
        JButton returnButton = new JButton("Return to Controller");

        loanButton.addActionListener(e -> new SystemControllerScreen());
        cdButton.addActionListener(e -> new TellerScreen());
        cdmanageButton.addActionListener(e -> new ManagerScreen());
        checksButton.addActionListener(e -> new CustomerScreen());
        returnButton.addActionListener(e -> new SystemControllerScreen());

        add(loanButton);
        add(cdButton);
        add(cdmanageButton);
        add(checksButton);
        add(returnButton);

        setVisible(true);
    }
}
