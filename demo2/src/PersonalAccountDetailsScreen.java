import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class PersonalAccountDetailsScreen extends JFrame {
    private final Customer customer;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    public PersonalAccountDetailsScreen(AbstractAccount account) {
        setSize(700, 500);
        setTitle("Account Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //grab the customer for displaying their name
        String ssn = account.getCustomerID();
        Customer customer = Database.getCustomerFromList(ssn);
        this.customer = customer;

        //Check the specific type of the account and call method to display information
        if(account instanceof SavingsAccount.SimpleSavingsAccount simpleSavings) {
            panel.add(simpleSavingsInfo(simpleSavings));
        }else if(account instanceof SavingsAccount.CDSavingsAccount cdSavings) {
            panel.add(cdSavingsInfo(cdSavings));
        }else if((account instanceof CheckingAccount tmbAccount) && (tmbAccount.getAccountSpecificType() == CheckingAccount.AccountType.TMB)){
            panel.add(tmbAccountInfo(tmbAccount));
        }else if((account instanceof CheckingAccount gdAccount) && (gdAccount.getAccountSpecificType() == CheckingAccount.AccountType.GoldDiamond)){
            panel.add(gdAccountInfo(gdAccount));
        }else if(account instanceof LoanAccount.ShortOrLong loanAccount){
            panel.add(loanAccountInfo(loanAccount));
        }else if(account instanceof LoanAccount.CC creditCardAccount){
            panel.add(creditCardInfo(creditCardAccount));
        }

        //Return Buttons
        JButton returnToAccounts = new JButton("Return to Accounts");
        JButton returnToCustomer = new JButton("Return to Customer Screen");

        returnToAccounts.addActionListener(e -> {
            dispose();
            new PersonalAccountsScreen(customer);
        });

        returnToCustomer.addActionListener(e -> {
            dispose();
            new CustomerScreen(customer);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // or BoxLayout if you want stacked
        buttonPanel.add(returnToAccounts);
        buttonPanel.add(returnToCustomer);


        add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel simpleSavingsInfo(SavingsAccount.SimpleSavingsAccount account) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(headerLabel("Account Holder: " + customer.getFirstName() + " " + customer.getLastName()));
        detailsPanel.add(headerLabel("Account Type: Simple Savings Account"));
        detailsPanel.add(plainLabel("Creation Date: " + account.getAccountCreationDate()));
        detailsPanel.add(plainLabel("Current Balance: $" + df.format((account.getBalance()))));
        detailsPanel.add(plainLabel("Interest Rate: " + SavingsAccount.SimpleSavingsAccount.getInterestRate()+"%"));

        if(account.overdraftForAccountID != -1){
            detailsPanel.add(plainLabel("Overdraft for Checking Account #" + account.overdraftForAccountID));
        }
        if(account.linkedToATMCard){
            detailsPanel.add(plainLabel("Linked To ATM Card"));
        }

        return detailsPanel;

    }

    private JPanel cdSavingsInfo(SavingsAccount.CDSavingsAccount account) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(headerLabel("Account Holder: " + customer.getFirstName() + " " + customer.getLastName()));
        detailsPanel.add(headerLabel("Account Type: Certificate of Deposit"));
        detailsPanel.add(plainLabel("Creation Date: " + account.getAccountCreationDate()));
        detailsPanel.add(plainLabel("Maturity Date: " + account.getDueDate()));
        detailsPanel.add(plainLabel("Principal Amount: $" + df.format(account.getBalance())));
        detailsPanel.add(plainLabel("Interest Rate: " + account.getInterestRate()+"%"));

        return  detailsPanel;
    }

    private JPanel tmbAccountInfo(CheckingAccount account) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(headerLabel("Account Holder: " + customer.getFirstName() + " " + customer.getLastName()));
        detailsPanel.add(headerLabel("Account Type: Checking Account_TMB"));
        detailsPanel.add(plainLabel("Creation Date: " + account.getAccountCreationDate()));
        detailsPanel.add(plainLabel("Current Balance: $" + df.format(account.getBalance())));
        detailsPanel.add(plainLabel("Interest Rate: " + CheckingAccount.getInterestRate()+"%"));

        if(account.overDraftAccountID != -1){
            detailsPanel.add(plainLabel("Overdraft Savings Account: #" + account.getOverdraftAccountID()));
        }
        if(account.linkedToATMCard){
            detailsPanel.add(plainLabel("Linked To ATM Card"));
        }


        return  detailsPanel;
    }

    private JPanel gdAccountInfo(CheckingAccount account) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(headerLabel("Account Holder: " + customer.getFirstName() + " " + customer.getLastName()));
        detailsPanel.add(headerLabel("Account Type: Checking Account_Gold Diamond"));
        detailsPanel.add(plainLabel("Creation Date: " + account.getAccountCreationDate()));
        detailsPanel.add(plainLabel("Current Balance: $" + df.format(account.getBalance())));
        detailsPanel.add(plainLabel("Interest Rate: " + CheckingAccount.getInterestRate()+"%"));

        if(account.overDraftAccountID != -1){
            detailsPanel.add(plainLabel("Overdraft Savings Account: #" + account.getOverdraftAccountID()));
        }
        if(account.linkedToATMCard){
            detailsPanel.add(plainLabel("Linked To ATM Card"));
        }

        return  detailsPanel;
    }

    private JPanel loanAccountInfo(LoanAccount.ShortOrLong account) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        String shortOrLong = "";
        if (account.numOfYearsTotal > 5){
            shortOrLong += "Long Term Loan";
        }else{
            shortOrLong += "Short Term Loan";
        }

        detailsPanel.add(headerLabel("Account Holder: " + customer.getFirstName() + " " + customer.getLastName()));
        detailsPanel.add(headerLabel("Account Type: " + shortOrLong));
        detailsPanel.add(plainLabel("Creation Date: " + account.getAccountCreationDate()));
        detailsPanel.add(plainLabel("Loan Amount: $" + df.format(account.getBalance())));
        detailsPanel.add(plainLabel("Interest Rate: " + account.getRate()+"%"));
        detailsPanel.add(plainLabel("Last Payment Date: " + account.getLastPaymentMadeDate()));
        detailsPanel.add(plainLabel("Next Payment: $" + df.format(account.getCurrentPaymentDue()) +
                " due on " + account.getThisPaymentDueDate()));

        return  detailsPanel;
    }

    private JPanel creditCardInfo(LoanAccount.CC account) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(headerLabel("Account Holder: " + customer.getFirstName() + " " + customer.getLastName()));
        detailsPanel.add(headerLabel("Account Type: Credit Card"));
        detailsPanel.add(plainLabel("Creation Date: " + account.getAccountCreationDate()));
        detailsPanel.add(plainLabel("Interest Rate: " + account.getRate()+"%"));
        detailsPanel.add(plainLabel("Limit: $" + df.format(account.getLimit())));
        detailsPanel.add(plainLabel("Current total of Charges: $" + df.format(account.getBalance())));
        double availableCredit = account.getLimit() - account.getBalance();
        detailsPanel.add(plainLabel("Available Credit $" + df.format(availableCredit)));

        for (String charge : account.chargeMessages){
            detailsPanel.add(new JLabel("Charge: " + charge));
        }

        return  detailsPanel;
    }

    private JLabel headerLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // or whatever font/size you like
        return label;
    }

    private JLabel plainLabel(String text){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }


}
