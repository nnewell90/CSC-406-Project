import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class CreditCardDetailScreen extends JFrame {
    public CreditCardDetailScreen() {
        setTitle("Create a New Account");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Item description
        Label l1 = new Label("Please enter item description:");
        JTextField accountItemField = new JTextField(10);
        add(l1);
        add(accountItemField);

        // Item cost
        Label l2 = new Label("Please enter the cost of the item:");
        JTextField accountPriceField = new JTextField(10);
        add(l2);
        add(accountPriceField);

        //Date of purchase
        Label l3 = new Label("Please enter the date of purchase:");
        JTextField accountDateField = new JTextField(10);
        add(l3);
        add(accountDateField);


        l1.setBounds(100, 50, 60, 40);
        l2.setBounds(100, 100, 60, 40);
        l3.setBounds(100,150,60,40);

        setLayout(new GridLayout(5, 1));


       /* // Radio buttons
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
        add(radioPanel);*/

        // Bottom buttons
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Credit Card Screen");


        /*ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(checkingButton);
        accountGroup.add(savingsButton);
        accountGroup.add(cdButton);
        accountGroup.add(creditcardButton);*/


        add(submitButton);
        add(returntoTellerScreen);

        returntoTellerScreen.addActionListener(e -> {dispose(); new CreditCardScreen();});
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = accountItemField.getText().trim();
                String itemCost = accountPriceField.getText().trim();
                String dateName = accountDateField.getText().trim();


                // save to file -- check for method already written!!!!!!!!!
                // otherwise use the method below!!!!!!!
                saveAccountToFile(itemName, itemCost, dateName);
            }
        });

        setVisible(true);
    }

    private void saveAccountToFile(String itemName, String itemCost, String dateName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("CCacountdetails.txt", true))) {
            writer.write(itemName + "; " + itemCost + "; " + dateName);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Item logged successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving account information!");
        }
    }

}