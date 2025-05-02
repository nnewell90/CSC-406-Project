import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomerSignIn extends JFrame {

    JTextField customerID;
    public CustomerSignIn() {
        setTitle("Sign In");
        setSize(700, 500);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        panel.add(new JLabel("Customer ID (SSN):"));
        customerID = new JTextField(15);
        panel.add(customerID);
        panel.add(Box.createVerticalStrut(10));

        ArrayList<Customer> customersList = Database.customerList;
        ArrayList<String> customerStringList = new ArrayList<>();
        for(Customer customer : customersList) {
            String s = customer.getFirstName() + " " + customer.getLastName() +
                    " SSN:"+ customer.getSSN();
            customerStringList.add(s);
        }

        JList<String> customerJList = new JList<>(customerStringList.toArray(new String [0]));
        JScrollPane customerScrollPane = new JScrollPane(customerJList);
        customerScrollPane.setPreferredSize(new Dimension(650, 400));


        customerJList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                String selected = customerJList.getSelectedValue();

                String ssn = selected.substring(selected.lastIndexOf(":") + 1).trim();
                Customer customer = Database.getCustomerFromList(ssn);
                JOptionPane.showMessageDialog(this, "Logged in Successfully!");
                dispose();
                new CustomerScreen(customer);

            }
        });

        panel.add(customerScrollPane);
        panel.add(Box.createVerticalStrut(10));

        JButton returnToMainMenuButton = new JButton("Return to Main Menu");
        JButton login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginCustomer();
            }
        });

        returnToMainMenuButton.addActionListener(e -> {
            saveUIState("MainMenu");
            dispose();
            new MainMenu();
        });

        panel.add(login);
        panel.add(Box.createVerticalStrut(5));
        panel.add(returnToMainMenuButton);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void saveUIState(String state) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ui_state.txt"))) {
            writer.write(state);
            System.out.println("UI state saved: " + state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginCustomer() {
        if(customerID.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Customer ID");
            return;
        }

        Customer customer = Database.getCustomerFromList(customerID.getText());

        if(customer == null) {
            JOptionPane.showMessageDialog(this, "Invalid Customer ID");
            return;
        }

        JOptionPane.showMessageDialog(this, "Logged in Successfully!");
        new CustomerScreen(customer);
        dispose();
    }
}

