import javax.swing.*;

public class SystemControllerScreen extends JFrame {
    public SystemControllerScreen() {
        setTitle("System Controller");
        setSize(300, 200);
        JLabel label = new JLabel("Welcome to System Controller Screen", SwingConstants.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(label);
        setVisible(true);
    }
}
