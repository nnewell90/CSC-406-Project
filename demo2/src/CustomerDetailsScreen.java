import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

//This screen displays a list of accounts associated with a customer.
public class CustomerDetailsScreen extends JFrame {


    public CustomerDetailsScreen(Customer selectedCustomer) {

        String ssn = selectedCustomer.getSSN();

        setTitle("Customer Details");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Name
        assert selectedCustomer != null;
        JLabel header = new JLabel(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
        header.setFont(new Font("Arial", Font.BOLD, 15));

        //ID
        JLabel subHeader = new JLabel("ID: " + selectedCustomer.getCustomerID());
        subHeader.setFont(new Font("Arial", Font.BOLD, 10));

        //Combine Name and ID into its own panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(header);
        headerPanel.add(subHeader);

        //List of Current Accounts the customer holds
        ArrayList<AbstractAccount> accounts = selectedCustomer.getCustomerAccountsAsAccounts();
        ArrayList<String> accountStrings = new ArrayList<>();
        for(AbstractAccount account : accounts) {
            String s = (account.getAccountType() + "____account #"+ account.getAccountID());
            accountStrings.add(s);
        }

        JList<String> accountsList = new JList<>(accountStrings.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(accountsList);

        accountsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                dispose();
                String selected = accountsList.getSelectedValue();

                //grabs the account number from the string selected
                String accountID = selected.substring(selected.lastIndexOf("#")+1).trim();
                Long idLong = Long.parseLong(accountID);
                AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, idLong);

                new AccountDetailsScreen(account);
            }
        });

        //Button to go create a new account for the customer
        JButton createAccount = new JButton("Create New Account");
        createAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccount.addActionListener(e -> { dispose(); new CreateAccountScreen();});

        //Return Button
        JButton returnButton = new JButton("Return to Customers");
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.addActionListener(e -> {dispose();
            try {
                new ReviewCustomersScreen();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Panel for both buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(createAccount);
        bottomPanel.add(returnButton);

        //add statements...
        add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        setVisible(true);
    }

}
