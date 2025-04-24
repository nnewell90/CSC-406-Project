import javax.swing.*;
import java.util.Date;
import java.awt.*;

public class CreateCreditCardAccountScreen extends JFrame {
    private final JTextField cardLimit, interestRate;
    private final Date date;
    private final Customer customer;
    public CreateCreditCardAccountScreen(Customer customer, Date date) {
        setSize(700, 500);
        setTitle("Creating a Credit Card Account for " + customer.getFirstName() + " " + customer.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.customer = customer;
        this.date = date;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Credit Card Limit:"));
        cardLimit = new JTextField(10);
        panel.add(cardLimit);
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JLabel("Interest Rate (If none enter 0): "));
        interestRate = new JTextField(10);
        panel.add(interestRate);
        panel.add(Box.createVerticalStrut(5));

        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> {
            CreateCCAccount();
        });
        submitButton.setAlignmentX(CENTER_ALIGNMENT);

        JButton returnButton = new JButton("Return to Teller Screen");
        returnButton.addActionListener(e -> {dispose();new TellerScreen();});
        returnButton.setAlignmentX(CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(returnButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(submitButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void CreateCCAccount() {

        //make sure fields have data
        if (cardLimit.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Loan Amount");
            return;
        } else if (interestRate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Interest Rate");
            return;
        }

        //pass Data values
        String id = customer.getCustomerID();
        double limit = Double.parseDouble(cardLimit.getText());
        double rate = Double.parseDouble(interestRate.getText());

        //Create the Account and add it to lists
        LoanAccount.CC ccAccount = new LoanAccount.CC(id, date, 0, rate, 0, limit);
        customer.addAccountToCustomerAccounts(ccAccount.getAccountID());
        Database.addItemToList(Database.CCList, ccAccount);

        //close screen with success message
        JOptionPane.showMessageDialog(this,
                "Credit Card Account Created Successfully\n" +
                        "Limit: " + limit);
        dispose();
        new TellerScreen();

    }
}
