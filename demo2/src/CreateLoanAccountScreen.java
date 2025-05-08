import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CreateLoanAccountScreen extends JFrame {
    private final JTextField loanTotalValue;
    private final JTextField interestRate;
    private final JTextField payoffDate;
    private final Customer customer;
    private final LocalDate startDate;

    public CreateLoanAccountScreen(Customer customer, LocalDate date) {
        setSize(700, 500);
        setTitle("Creating a Loan Account for "+ customer.getFirstName() + " " + customer.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.customer = customer;
        this.startDate = date;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Loan Amount(ex. 10000.00): "));
        loanTotalValue = new JTextField(10);
        panel.add(loanTotalValue);
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JLabel("Interest Rate (ex. 2.5): "));
        interestRate = new JTextField(10);
        panel.add(interestRate);
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JLabel("Loan Payoff Date "));
        payoffDate = new JTextField(10);
        panel.add(payoffDate);
        panel.add(Box.createVerticalStrut(5));


        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> {
            CreateLoanAccount();
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

    public void CreateLoanAccount() {

        //Make sure fields have data in them
        if (loanTotalValue.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Loan Amount");
            return;
        } else if (interestRate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Interest Rate");
            return;
        } else if (payoffDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Monthly Payment");
            return;
        }

        //Pass data values
        String id = customer.getCustomerID();
        double value = Double.parseDouble(loanTotalValue.getText().trim());
        double rate = Double.parseDouble(interestRate.getText().trim());
        String dateString = payoffDate.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate endDate = LocalDate.parse(dateString, formatter);

        int numOfYears = endDate.getYear() - startDate.getYear();

        //create account and add it to lists
        LoanAccount.ShortOrLong lnAccount = new LoanAccount.ShortOrLong(id, startDate, value, rate, endDate, numOfYears);
        customer.addAccountToCustomerAccounts(lnAccount.getAccountID());
        Database.addItemToList(Database.shortOrLongLoanList, lnAccount);

        //close screen with success message
        JOptionPane.showMessageDialog(this,
                "Loan Account Created\nLoan Term: "+ startDate + "-" + endDate);
        dispose();
        new TellerScreen();

    }

    //Method to calculate the final payment
    private LocalDate calcFinalPaymentDate(LocalDate date, int monthsToPayOffLoan) {
        //calculate the last payment date...
        return date.plusMonths(monthsToPayOffLoan);
    }

    private int calcYears(int monthsToPayOffLoan) {
        int years = (int) Math.ceil(monthsToPayOffLoan/12);

        return years;
    }

    private int calcRemainingMonths(int monthsToPayOffLoan) {
        int remaining = monthsToPayOffLoan%12;
        return remaining;
    }

}
