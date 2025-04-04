import javax.swing.*;
import java.awt.*;

public class NewAccountScreen extends JFrame {
    public NewAccountScreen() {
        setTitle("Create a New Account");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // First name label and text box
        Label l1 = new Label("Please enter the first name of the account holder:");
        JTextField accountFnameField = new JTextField(10);
        add(l1);
        add(accountFnameField);

        // Last name label and text box
        Label l2 = new Label("Please enter the last name of the account holder:");
        JTextField accountLnameField = new JTextField(10);
        add(l2);
        add(accountLnameField);

        // Account selection label
        Label l3 = new Label("Select the account you wish to create:");
        add(l3);

        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        l3.setBounds(100, 150, 120, 80);

        setLayout(new GridLayout(4, 1));


        // Radio buttons
        JRadioButton checkingButton = new JRadioButton("Checking");
        JRadioButton savingsButton = new JRadioButton("Savings");
        JRadioButton cdButton = new JRadioButton("Certificate of Deposit");
        JRadioButton creditcardButton = new JRadioButton("Credit Card");
        // Radio buttons panel
        JPanel radioPanel = new JPanel();
        radioPanel.add(checkingButton);
        radioPanel.add(savingsButton);
        radioPanel.add(cdButton);
        radioPanel.add(creditcardButton);
        add(radioPanel);

        // Bottom buttons
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");


        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(checkingButton);
        accountGroup.add(savingsButton);
        accountGroup.add(cdButton);
        accountGroup.add(creditcardButton);


        add(submitButton);
        add(returntoTellerScreen);


        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});

        setVisible(true);
    }
}
