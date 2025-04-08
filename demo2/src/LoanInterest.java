import javax.swing.*;
import java.awt.*;

public class LoanInterest extends JFrame {
    public LoanInterest() {
        setTitle("Loan Interest Rates");
        Label l1 = new Label("Enter Interest Rate for Loans: ");
        l1.setBounds(100, 50, 120, 80);
        add(l1);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField loanRateField = new JTextField(10);
        JButton returntoManager = new JButton("Return to Manager Screen");


        returntoManager.addActionListener(e -> new ManagerScreen());
        add(loanRateField);
        add(returntoManager);

        setVisible(true);
    }
}
