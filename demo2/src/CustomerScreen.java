import javax.swing.*;
import java.awt.*;

public class CustomerScreen extends JFrame {
    public CustomerScreen() {
        setTitle("Customer Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Label for directions
        JLabel directions = new JLabel("Please Enter Username and Password");
        directions.setFont(new Font("Arial", Font.BOLD, 16));
        directions.setHorizontalAlignment(SwingConstants.CENTER);
        directions.setPreferredSize(new Dimension(250, 50));

        //This panel holds the username and password fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Username and password Text Fields
        JTextField username = new JTextField(15);
        JPasswordField password = new JPasswordField(15);

        JLabel userLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");

        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setMaximumSize(new Dimension(200, 25));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        password.setMaximumSize(new Dimension(200, 25));

        inputPanel.add(userLabel);
        inputPanel.add(username);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(passwordLabel);
        inputPanel.add(password);

        //add components to the frame
        add(directions, BorderLayout.NORTH);//places the title at the top
        add(inputPanel, BorderLayout.CENTER);//places the input fields in the center

        setVisible(true);
    }
}
