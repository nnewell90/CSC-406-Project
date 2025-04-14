import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EstablishCD extends JFrame {
    JTextField customerSSN, deposit, rate, dueDate;

    public EstablishCD() {
        setTitle("Establish Certificates of Deposit");

        add(new JLabel("Customer SSN:"));
        customerSSN = new JTextField(10);
        add(customerSSN);

        add(new JLabel("Deposit amount:"));
        deposit = new JTextField(10);
        add(deposit);

        add(new JLabel("Interest Rate(ex: 2.5 for 2.5%):"));
        rate = new JTextField(10);
        add(rate);

        add(new JLabel("Due Date (MM/DD/YYYY):"));
        dueDate = new JTextField(10);
        add(dueDate);


        setSize(500, 500);
        setLayout(new GridLayout(4,2));


        JButton returntoManager = new JButton("Return to Manager Screen");

        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> CreateCD());

        returntoManager.addActionListener(e -> {
            dispose();
            new ManagerScreen();
        });

        add(returntoManager);
        add(submitButton);
        setVisible(true);
    }

    private void CreateCD() {

        if(customerSSN.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Customer SSN is required");
            return;
        }else if(deposit.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Deposit is required");
            return;
        }else if(rate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rate is required");
            return;
        }else if(dueDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Due Date is required");
            return;
        }

        String ssn = customerSSN.getText().trim();
        double depo = Double.parseDouble(deposit.getText());
        double interestRate = Double.parseDouble(rate.getText());
        String dateString = dueDate.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        Date DueDate = java.sql.Date.valueOf(localDate);
        Date date = new Date();

        SavingsAccount.CDSavingsAccount account = new SavingsAccount.CDSavingsAccount(ssn, date, depo, interestRate, DueDate);
        Database.addItemToList(Database.savingsAccountList, account);
        JOptionPane.showMessageDialog(null, "Account created! Matures due on: " + account.getDueDate());
        dispose();
        new ManagerScreen();



    }

}
