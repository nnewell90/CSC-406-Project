import javax.swing.*;
import java.awt.*;

/**
 * This is the class that displays a main menu with clickable buttons that
 * direct to a new screen depending on the option you click.
 * */

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Main Menu");
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JButton tellerButton = new JButton("Teller Screen");
        JButton managerButton = new JButton("Manager Screen");
        JButton customerButton = new JButton("Customer Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tellerButton.addActionListener(e -> new TellerScreen());
        managerButton.addActionListener(e -> new ManagerScreen());
        customerButton.addActionListener(e -> new CustomerScreen());

        add(tellerButton);
        add(managerButton);
        add(customerButton);

        setVisible(true);
    }
}
