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

    public void createAccount() {

        //Pass Data values...
        String id = ID.getText().trim();
        String accountType = accountTypeComboBox.getSelectedItem().toString();
        double deposit = Double.parseDouble(Deposit.getText().trim());

        String dateString = creationDate.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        Date date = java.sql.Date.valueOf(localDate);

        Customer customer = Database.getCustomerFromList(id);
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found");
            return;
        }

        //Check what specific type of account it is
        CheckingAccount.AccountType specificType = null;
        if(deposit >= 5000.00){
            specificType = CheckingAccount.AccountType.GoldDiamond;
        }else if (deposit <5000.00){
            specificType = CheckingAccount.AccountType.TMB;
        }

        AbstractAccount account = null;

        if(accountType == "Checking") {
            account = new CheckingAccount(id, date, AbstractAccount.AccountType.CheckingAccount, deposit, specificType);
        }else if (accountType == "Savings") {
            account = new SavingsAccount(id, date, AbstractAccount.AccountType.SavingsAccount, deposit);
        }else if(accountType == "Loan") {
            dispose();
            new CreateLoanAccountScreen(customer, date);
        }

        //add account to list of customer accounts
        if(account == null){
            JOptionPane.showMessageDialog(this, "Cant find Account " + account.accountID);
            return;
        }
        customer.addAccountToCustomerAccounts(account.getAccountID());

        //close screen and go back to Teller screen
        dispose();
        new TellerScreen();

    }//end of Create Account

}
