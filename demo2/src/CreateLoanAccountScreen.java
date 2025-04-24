import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class CreateLoanAccountScreen extends JFrame {
    private final JTextField loanTotalValue;
    private final JTextField interestRate;
    private final JTextField monthlyPayment;
    private final Customer customer;
    private final Date date;

    public CreateLoanAccountScreen(Customer customer, Date date) {
        setSize(700, 500);
        setTitle("Creating a Loan Account for "+ customer.getFirstName() + " " + customer.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.customer = customer;
        this.date = date;

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

        panel.add(new JLabel("Monthly Payment(ex. 300): $"));
        monthlyPayment = new JTextField(10);
        panel.add(monthlyPayment);
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
        } else if (monthlyPayment.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Monthly Payment");
            return;
        }

        //Pass data values
        String id = customer.getCustomerID();
        double value = Double.parseDouble(loanTotalValue.getText().trim());
        double rate = Double.parseDouble(interestRate.getText().trim());
        double monthly = Double.parseDouble(monthlyPayment.getText().trim());

        //calculate months and years
        int monthsToPayOffLoan = (int) Math.ceil(value/monthly);
        Date finalPaymentDate = calcFinalPaymentDate(date, monthsToPayOffLoan);
        int totalYears = calcYears(monthsToPayOffLoan);
        int remainingMonths = calcRemainingMonths(monthsToPayOffLoan);

        //create account and add it to lists
        LoanAccount.ShortOrLong lnAccount = new LoanAccount.ShortOrLong(id, date, value, rate, monthly, finalPaymentDate, totalYears);
        customer.addAccountToCustomerAccounts(lnAccount.getAccountID());
        Database.addItemToList(Database.shortOrLongLoanList, lnAccount);

        //close screen with success message
        JOptionPane.showMessageDialog(this,
                "Loan Account Created\nLoan Term: "+ totalYears + " years and " + remainingMonths+" months.");
        dispose();
        new TellerScreen();

    }

    //Method to calculate the final payment
    private Date calcFinalPaymentDate(Date date, int monthsToPayOffLoan) {
        //change it to a java.LocalDate...
        LocalDate localStartDate = ((java.sql.Date) date).toLocalDate();

        //calculate the last payment date...
        LocalDate localEndDate = localStartDate.plusMonths(monthsToPayOffLoan);

        //change back into a java.Date
        Date endDate = Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return endDate;
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
