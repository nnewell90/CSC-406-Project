import javax.swing.*;
import java.awt.*;

public class ManagerScreen extends JFrame {
    public ManagerScreen() {
        setTitle("Manager Screen");
        setSize(300, 200);
        setLayout(new GridLayout(5, 1));

        JButton loanButton = new JButton("Loan Interest Rates");
        JButton cdButton = new JButton("CD Interest Rates");
        JButton cdmanageButton = new JButton("CD Manager");
        JButton checksButton = new JButton("Checks Processing");
        JButton returnButton = new JButton("Return to Controller");

        loanButton.addActionListener(e -> new LoanInterest());
        cdButton.addActionListener(e -> new CDInterest());
        cdmanageButton.addActionListener(e -> new EstablishCD());
        checksButton.addActionListener(e -> new ProcessCheck());
        returnButton.addActionListener(e -> new MainMenu());

        add(loanButton);
        add(cdButton);
        add(cdmanageButton);
        add(checksButton);
        add(returnButton);

        setVisible(true);
    }
}
