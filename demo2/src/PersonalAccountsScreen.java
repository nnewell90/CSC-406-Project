import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class PersonalAccountsScreen extends JFrame {
    public PersonalAccountsScreen(Customer customer) {
        setTitle("My Accounts");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //So I can display accounts in a certain format...
        ArrayList<AbstractAccount> accounts = customer.getCustomerAccountsAsAccounts();
        ArrayList<String> accountStrings = new ArrayList<>();
        for(AbstractAccount account : accounts) {
            String s = (account.getAccountType() + "____account #"+ account.getAccountID());
            accountStrings.add(s);
        }

        JList<String> accountsList = new JList<>(accountStrings.toArray(new String[0]));
        JScrollPane accountsScrollPane = new JScrollPane(accountsList);

        accountsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                dispose();
                String selected = accountsList.getSelectedValue();

                //grabs the account number from the string selected
                String accountID = selected.substring(selected.lastIndexOf("#")+1).trim();
                Long IDlong = Long.parseLong(accountID);
                AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, IDlong);
                new PersonalAccountDetailsScreen(account);
            }
        });

        JButton returnToCustomersScreen = new JButton("Return to Customer Screen");
        returnToCustomersScreen.addActionListener(e -> {
            dispose();
            new CustomerScreen(customer);
        });


        add(accountsScrollPane, BorderLayout.CENTER);
        add(returnToCustomersScreen, BorderLayout.SOUTH);
        setVisible(true);
    }
}
