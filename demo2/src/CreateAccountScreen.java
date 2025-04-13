import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class CreateAccountScreen extends JFrame {

    private JTextField ID, creationDate, Deposit;
    private JComboBox<String> accountTypeComboBox;

    public CreateAccountScreen() {
        setTitle("Create New Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(new JLabel("Customer ID"));
        ID = new JTextField(10);
        formPanel.add(ID);
        formPanel.add(Box.createVerticalStrut(4));

        formPanel.add(new JLabel("Account Creation Date: (MM/DD/YYYY)"));
        creationDate = new JTextField(10);
        formPanel.add(creationDate);
        formPanel.add(Box.createVerticalStrut(4));

        formPanel.add(new JLabel("Account Type:"));

        String[] accountTypes = {"Savings", "Checking", "Loan"};
        accountTypeComboBox = new JComboBox<>(accountTypes);

        formPanel.add(accountTypeComboBox);


        formPanel.add(new JLabel("Starting Deposit: (If Loan Account, Leave Blank.)"));
        Deposit = new JTextField(10);
        formPanel.add(Deposit);

        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> createAccount());

        JButton returnButton = new JButton("Return to Teller Screen");
        returnButton.addActionListener(e -> {dispose();new TellerScreen();});

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(returnButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /*
     * Method that creates the Customer Account and adds it to the ArrayList's
     * If the account is a Loan it opens up a new screen for creating Loan Accounts specifically
     */
    public void createAccount() {

        //Pass Data values...
        String id = ID.getText().trim();
        String accountType = accountTypeComboBox.getSelectedItem().toString();
        String deposit = Deposit.getText().trim();
        String dateField = creationDate.getText().trim();

        if(id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID");
        }else if(dateField.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date");
        }else if(deposit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid deposit");
        }else {

            double depositNum = Double.parseDouble(Deposit.getText().trim());
            String dateString = creationDate.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            Date date = java.sql.Date.valueOf(localDate);

            Customer customer = Database.getCustomerFromList(id);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found, validate your customer ID");
                return;
            }else {

                //create the account and add it to the list...
                if (accountType.equals("Checking")) {
                    //Check what specific type of Checking account it is...
                    CheckingAccount.AccountType specificType = null;
                    if (depositNum >= 5000.00) {
                        specificType = CheckingAccount.AccountType.GoldDiamond;
                    } else if (depositNum < 5000.00) {
                        specificType = CheckingAccount.AccountType.TMB;
                    }
                    CheckingAccount account = new CheckingAccount(id, date, AbstractAccount.AccountType.CheckingAccount, depositNum, specificType);
                    customer.addAccountToCustomerAccounts(account.getAccountID());
                    Database.addItemToList(Database.checkingAccountList, account);

                } else if (accountType.equals("Savings")) {
                    SavingsAccount account = new SavingsAccount(id, date, AbstractAccount.AccountType.SavingsAccount, depositNum);
                    customer.addAccountToCustomerAccounts(account.getAccountID());
                    Database.addItemToList(Database.savingsAccountList, account);

                } else if (accountType.equals("Loan")) {
                    dispose();
                    new CreateLoanAccountScreen(customer, date);
                }

                //close screen and go back to Teller screen
                JOptionPane.showMessageDialog(this, "Account created successfully");
                dispose();
                new TellerScreen();
            }//end of customer check if/else
        }//end of datafield check if/else
    }//end of CreateAccount

}//end of CreateAccountScreen
