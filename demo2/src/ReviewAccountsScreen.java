import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReviewAccountsScreen extends JFrame{
    public ReviewAccountsScreen() throws IOException {
        setTitle("Customer Accounts");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Customer Accounts");
        header.setFont(new Font("Arial", Font.BOLD, 15));

        List<String> customerList = Files.readAllLines(Paths.get("customers.txt"));
        JList<String> list = new JList<>(( customerList).toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(list);

        JButton returnButton = new JButton("Return to Teller Screen");

        returnButton.addActionListener(e -> {dispose();new TellerScreen();});


        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(returnButton, BorderLayout.SOUTH);

        setVisible(true);
    }


}
