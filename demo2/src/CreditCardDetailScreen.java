import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreditCardDetailScreen extends JFrame {
    public final Customer customer;
    public CreditCardDetailScreen(Customer customer) {
        setTitle("Make A Purchase");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.customer = customer;

        Label l1 = new Label("Please Enter Credit Card Account ID:");
        JTextField accountIDField = new JTextField(10);
        add(l1);
        add(accountIDField);

        // Item description
        Label l2 = new Label("Please enter item description:");
        JTextField accountItemField = new JTextField(10);
        add(l2);
        add(accountItemField);

        // Item cost
        Label l3 = new Label("Please enter the cost of the item:");
        JTextField accountPriceField = new JTextField(10);
        add(l3);
        add(accountPriceField);

        //Date of purchase
        Label l4 = new Label("Please enter the date of purchase:");
        JTextField accountDateField = new JTextField(10);
        add(l4);
        add(accountDateField);


        l1.setBounds(100, 50, 60, 40);
        l2.setBounds(100, 100, 60, 40);
        l3.setBounds(100,150,60,40);

        setLayout(new GridLayout(5, 1));


        // Bottom buttons
        JButton submitButton = new JButton("Submit");
        JButton returnToCustomerButton = new JButton("Return to Customer Screen");


        add(submitButton);
        add(returnToCustomerButton);

        returnToCustomerButton.addActionListener(e -> {dispose(); new CustomerScreen(customer);});
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountID = accountIDField.getText();
                String itemName = accountItemField.getText().trim();
                String itemCost = accountPriceField.getText().trim();
                String dateName = accountDateField.getText().trim();
                if(accountIDField.getText().isEmpty() || accountItemField.getText().isEmpty()
                        || accountPriceField.getText().isEmpty() || accountDateField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter required fields");
                    return;
                }

                saveAccountToFile(itemName, itemCost, dateName);
                try {
                    makePurchase(accountID, itemName, itemCost, dateName);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }

    private void makePurchase(String accountID, String itemName, String itemCost, String dateName) throws ParseException {
        Long id = Long.parseLong(accountID);
        LoanAccount.CC ccAccount = (LoanAccount.CC) Database.getAccountFromList(Database.CCList, id);

        if(ccAccount == null) {
            JOptionPane.showMessageDialog(null, "Account not found! Double Check ID");
            return;
        }

        //pass values and charge the card...
        double cost = Double.parseDouble(itemCost);

        double balance = ccAccount.getBalance();
        double limit = ccAccount.getLimit();

        if (balance + cost < limit) {
            JOptionPane.showMessageDialog(this, "Purchase was Successful!");
        } else { // Over the limit
            JOptionPane.showMessageDialog(this, "Purchase was Denied!");
        }

        String dateString = dateName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        ccAccount.charge(cost, itemName, localDate);
        dispose();
        new CustomerScreen(customer);

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
