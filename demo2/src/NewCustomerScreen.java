import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

public class NewCustomerScreen extends JFrame {
    private JTextField ssn, address, city, state, zip, firstName, lastName;

    public NewCustomerScreen() {
        setTitle("New Customer");
        setSize(500, 500);
        setLayout(new GridLayout(4, 2)); // change this for fields?

        // Labels and text fields
        add(new JLabel("SSN: "));
        ssn = new JTextField();
        add(ssn);

        add(new JLabel("Street Address: "));
        address = new JTextField();
        add(address);

        add(new JLabel("City: "));
        city = new JTextField();
        add(city);

        add(new JLabel("State: "));
        state = new JTextField();
        add(state);

        add(new JLabel("Zip: "));
        zip = new JTextField();
        add(zip);

        add(new JLabel("First Name: "));
        firstName = new JTextField();
        add(firstName);

        add(new JLabel("Last Name: "));
        lastName = new JTextField();
        add(lastName);

        JButton submitButton = new JButton("Create New Customer");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCustomer();
            }
        });

        add(submitButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void createCustomer() {
        String socialSecurityNumber = ssn.getText().trim();
        String streetAddress = address.getText().trim();
        String resCity = city.getText().trim();
        String resState = state.getText().trim();
        String zipCode = zip.getText().trim();
        String fName = firstName.getText().trim();
        String lName = lastName.getText().trim();

        if (socialSecurityNumber.isEmpty() || fName.isEmpty() || lName.isEmpty() || streetAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Must complete required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Store the data to database
        // might have to change txt name - not created yet!!!!!!!!!!!!
        try (FileWriter writer = new FileWriter("customers.txt", true)) {
            writer.write(socialSecurityNumber + ";" + streetAddress + ";" + resCity + ";" + resState + ";" + zipCode + ";" + fName + ";" + lName + "\n");
            JOptionPane.showMessageDialog(this, "Customer Created Successfully!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating customer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}