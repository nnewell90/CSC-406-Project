import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

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
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = accountFnameField.getText().trim();
                String lastName = accountLnameField.getText().trim();
                String ssn = accountSsnField.getText().trim();
                String accountType = "";

                if (checkingButton.isSelected()) {
                    accountType = "checkingAccounts.txt";
                } else if (savingsButton.isSelected()) {
                    accountType = "savingAccounts.txt";
                } else if (cdButton.isSelected()) {
                    accountType = "cdSavingsAccounts.txt";
                } else if (creditcardButton.isSelected()) {
                    accountType = "CCAccounts.txt";
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an account type!");
                    return;
                }

                // save to file -- check for method already written!!!!!!!!!
                // otherwise use the method below!!!!!!!
                saveAccountToFile(accountType, firstName, lastName, ssn);
            }
        });

        setVisible(true);
    }

    private void saveAccountToFile(String fileName, String firstName, String lastName, String ssn) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(firstName + "; " + lastName + "; " + ssn);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Account Created Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving account information!");
        }
    }

}
