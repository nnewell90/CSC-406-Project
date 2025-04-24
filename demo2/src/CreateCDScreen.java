import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateCDScreen extends JFrame{
    private final Customer customer;
    private final Date creationDate;
    private final double initialBalance;

    private JTextField interestRate, dueDate;
    public CreateCDScreen(Customer customer, Date creationDate, double initialBalance){
        setTitle("Create Certificate of Deposit for" + customer.getFirstName() + " " + customer.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);

        this.customer = customer;
        this.creationDate = creationDate;
        this.initialBalance = initialBalance;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(new JLabel("Interest Rate:"));
        interestRate = new JTextField(10);
        formPanel.add(interestRate);
        formPanel.add(Box.createVerticalStrut(4));

        formPanel.add(new JLabel("Due Date(MM/DD/YYYY):"));
        dueDate = new JTextField(10);
        formPanel.add(dueDate);
        formPanel.add(Box.createVerticalStrut(4));

        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> createCD());

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

    private void createCD(){

        if(interestRate.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Interest rate cannot be empty");
        }else if (dueDate.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Due date cannot be empty");
        }

        try {
            String dateString = dueDate.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            Date dueDate = java.sql.Date.valueOf(localDate);
            double rate = Double.parseDouble(interestRate.getText());
            String id = customer.getCustomerID();

            //create account and add it to lists...
            SavingsAccount.CDSavingsAccount account = new SavingsAccount.CDSavingsAccount(id,
                    creationDate, initialBalance, rate, dueDate);
            customer.addAccountToCustomerAccounts(account.getAccountID());
            Database.addItemToList(Database.cdSavingsAccountList, account);
            Database.addItemToList(Database.savingsAccountList, account);

            //close screen
            JOptionPane.showMessageDialog(this, "CD Account created successfully\nMatures on " + account.getDueDate());
            dispose();
            new TellerScreen();

        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Invalid input: Please check interest rate and due date format.");
        }

    }

}
