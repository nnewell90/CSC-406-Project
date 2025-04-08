import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReviewCustomersScreen extends JFrame{
    public ReviewCustomersScreen() throws IOException {
        setTitle("Customers");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Customers");
        header.setFont(new Font("Arial", Font.BOLD, 15));

        List<String> customerList = Files.readAllLines(Paths.get("customers.txt"));
        JList<String> list = new JList<>(( customerList).toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(list);

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCustomer = list.getSelectedValue();
                if (selectedCustomer != null) {
                    new CustomerDetailsScreen(selectedCustomer);
                }
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
