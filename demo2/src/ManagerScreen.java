import javax.swing.*;

public class ManagerScreen extends JFrame {
    public ManagerScreen() {
        setTitle("Manager Screen");
        setSize(300, 200);
        JLabel label = new JLabel("Welcome to Manager Screen", SwingConstants.CENTER);
        add(label);
        setVisible(true);
    }
}
