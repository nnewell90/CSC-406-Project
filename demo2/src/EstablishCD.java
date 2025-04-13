import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EstablishCD extends JFrame {
    JTextField customerSSN, deposit, rate, dueDate;

    public EstablishCD() {
        setTitle("Establish Certificates of Deposit");
        Label l1 = new Label("Enter Customer SSN: ");
        Label l2 = new Label("Enter Deposit: ");
        Label l3 = new Label("Enter Interest Rate: ");
        Label l4 = new Label("Enter Due Date: ");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 50, 120, 80);
        l3.setBounds(100, 50, 120, 80);
        l4.setBounds(100, 50, 120, 80);
        add(l1);
        add(l2);
        add(l3);
        add(l4);
        setSize(500, 500);
        setLayout(new GridLayout(3,2));

        JTextField termField = new JTextField(10);
        JTextField minField = new JTextField(10);
        JButton returntoManager = new JButton("Return to Manager Screen");

        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> CreateCD());

        returntoManager.addActionListener(e -> new ManagerScreen());
        add(termField);
        add(minField);
        add(returntoManager);

        setVisible(true);
    }

    private void CreateCD() {
        String ssn = customerSSN.getText().trim();
        double depo = Double.parseDouble(deposit.getText());
        double interestRate = Double.parseDouble(rate.getText());
        String dateString = dueDate.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        Date DueDate = java.sql.Date.valueOf(localDate);
        Date date = new Date();

        if(customerSSN.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Customer SSN is required");
        }else if(deposit.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Deposit is required");
        }else if(rate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rate is required");
        }else if(dueDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Due Date is required");
        }else{

            SavingsAccount.CDSavingsAccount account = new SavingsAccount.CDSavingsAccount(ssn, date, depo, interestRate, DueDate);
            Database.addItemToList(Database.savingsAccountList, account);
            JOptionPane.showMessageDialog(null, "Account created, due on " + account.dueDate);
            dispose();
            new ManagerScreen();
        }


    }

}
