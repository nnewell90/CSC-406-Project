import javax.swing.*;

public class CustomerScreen extends JFrame {
    public CustomerScreen() {
        setTitle("Customer Screen");
        setSize(300, 200);
        JLabel label = new JLabel("Welcome to Customer Screen", SwingConstants.CENTER);
        add(label);
        setVisible(true);
    }
}
