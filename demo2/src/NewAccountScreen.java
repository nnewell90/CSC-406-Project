import javax.swing.*;
import java.awt.*;

public class NewAccountScreen extends JFrame {
    public NewAccountScreen() {
        setTitle("Create a New Account");
        Label l1 = new Label("Please enter the first name of the account holder:");
        Label l2 = new Label("Please enter the last name of the account holder:");
        Label l3 = new Label("Select the account you wish to create:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        l3.setBounds(100, 150, 120, 80);
        add(l1);
        add(l2);
        add(l3);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField accountFnameField = new JTextField(10);
        JTextField accountLnameField = new JTextField(10);
        JRadioButton checkingButton = new JRadioButton("Checking");
        JRadioButton savingsButton = new JRadioButton("Savings");
        JRadioButton cdButton = new JRadioButton("Certificate of Deposit");
        JRadioButton creditcardButton = new JRadioButton("Credit Card");
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");


        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(checkingButton);
        accountGroup.add(savingsButton);
        accountGroup.add(cdButton);
        accountGroup.add(creditcardButton);


        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        add(accountFnameField);
        add(accountLnameField);


        JPanel radioPanel = new JPanel();
        radioPanel.add(checkingButton);
        radioPanel.add(savingsButton);
        radioPanel.add(cdButton);
        radioPanel.add(creditcardButton);
        add(radioPanel);
        add(submitButton);
        add(returntoTellerScreen);


        setVisible(true);
    }
}
