import javax.swing.*;
import java.awt.*;

public class WithdrawScreen extends JFrame {
    public WithdrawScreen() {
        setTitle("Withdraw From Account");
        Label l1 = new Label("Please enter the amount to withdraw:");
        Label l2 = new Label("Please select an account type to withdraw from:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField accountholderField = new JTextField(10);
        JRadioButton checkingButton = new JRadioButton("Checking");
        JRadioButton savingsButton = new JRadioButton("Savings");
        JRadioButton cdButton = new JRadioButton("Certificate of Deposit");
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");


        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(checkingButton);
        accountGroup.add(savingsButton);
        accountGroup.add(cdButton);

        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        add(accountholderField);


        JPanel radioPanel = new JPanel();
        radioPanel.add(checkingButton);
        radioPanel.add(savingsButton);
        radioPanel.add(cdButton);
        add(radioPanel);
        add(submitButton);
        add(returntoTellerScreen);


        setVisible(true);
    }
}
