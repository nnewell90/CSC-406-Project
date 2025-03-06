import javax.swing.*;

public class TellerScreen extends JFrame {
    public TellerScreen() {
        setTitle("Teller Screen");
        setSize(300, 200);
        JLabel label = new JLabel("Welcome to Teller Screen", SwingConstants.CENTER);
        add(label);
        setVisible(true);
    }
}
