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

        //social security label and textbox
        Label l3 = new Label("Please enter the SSN of the account holder:");
        JTextField accountSsnField = new JTextField(10);
        add(l3);
        add(accountSsnField);

        // Account selection label
        Label l4 = new Label("Select the account you wish to create:");
        add(l4);

        l1.setBounds(100, 50, 60, 40);
        l2.setBounds(100, 100, 60, 40);
        l3.setBounds(100,150,60,40);
        l4.setBounds(100, 200, 60, 40);

        setLayout(new GridLayout(5, 1));


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
