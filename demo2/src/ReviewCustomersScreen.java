import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
//This screen displays a list of customers in the system. when you click on one it takes you to a
//customer details screen
public class ReviewCustomersScreen extends JFrame{
    public ReviewCustomersScreen() throws IOException {
        setTitle("Customers");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Customers");
        header.setFont(new Font("Arial", Font.BOLD, 15));

        ArrayList<Customer> customersList = Database.customerList;
        ArrayList<String> customerStringList = new ArrayList<>();
        for(Customer customer : customersList) {
            String s = customer.getFirstName() + " " + customer.getLastName() +
                    " SSN:"+ customer.getSSN();
            customerStringList.add(s);
        }
        JList<String> customerJList =new JList<>(customerStringList.toArray(new String [0]));
        JScrollPane scrollPane = new JScrollPane(customerJList);

        customerJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = customerJList.getSelectedValue();

                String ssn = selected.substring(selected.lastIndexOf(":") + 1).trim();
                Customer customer = Database.getCustomerFromList(ssn);

                dispose();
                new CustomerDetailsScreen(customer);

            }
        });

        JButton returnButton = new JButton("Return to Teller Screen");

        returnButton.addActionListener(e -> { dispose(); new TellerScreen();});


        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(returnButton, BorderLayout.SOUTH);

        setVisible(true);
    }


}
