import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CustomerDetailsScreen extends JFrame {


    public CustomerDetailsScreen(String selectedCustomer) {

        String ssn = selectedCustomer.substring(0,9);
        Customer customer = Database.getCustomerFromList(ssn);

        setTitle("Customer Details");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Name
        JLabel header = new JLabel(customer.getFirstName() + " " + customer.getLastName());
        header.setFont(new Font("Arial", Font.BOLD, 15));

        //ID
        JLabel subHeader = new JLabel("ID: " + customer.getCustomerID());
        subHeader.setFont(new Font("Arial", Font.BOLD, 10));

        //Combine Name and ID into its own panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(header);
        headerPanel.add(subHeader);

        //List of Current Accounts the customer holds
        JList<String> list = new JList<>((customer.getCustomerAccountIDs()).toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(list);

        //Button to go create a new account for the customer
        JButton createAccount = new JButton("Create New Account");
        createAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccount.addActionListener(e -> {dispose(); new CreateAccountScreen();});

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
