import javax.swing.*;

public class SystemControllerScreen extends JFrame {
    public SystemControllerScreen() {
        setTitle("System Controller");
        setSize(300, 200);
        JLabel label = new JLabel("Welcome to System Controller Screen", SwingConstants.CENTER);
        add(label);
        setVisible(true);
    }
}
