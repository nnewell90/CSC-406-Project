import javax.swing.*;
import java.awt.*;

public class InsertCheckScreen extends JFrame {
    public InsertCheckScreen() {
        setTitle("Deposit Checks");
        Label l1 = new Label("Please Enter the Amount of the Check:");
        Label l2 = new Label("Select the account you want to deposit to:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 200); // changed width to fit text

        setLayout(new GridLayout(4, 1));

        JTextField amountField = new JTextField(10);
        JRadioButton checkingButton = new JRadioButton("Checking");
        JRadioButton savingsButton = new JRadioButton("Savings");
        JButton returntoCustomerScreen = new JButton("Return to Customer Screen");


        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(checkingButton);
        accountGroup.add(savingsButton);

        returntoCustomerScreen.addActionListener(e -> {
            dispose(); // close CustomerScreen
            new CustomerScreen(); // reopens CustomerScreen
        });
        add(amountField);


        JPanel radioPanel = new JPanel();
        radioPanel.add(checkingButton);
        radioPanel.add(savingsButton);
        add(radioPanel);
        add(returntoCustomerScreen);


        setVisible(true);
    }
}
