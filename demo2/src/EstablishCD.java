import javax.swing.*;
import java.awt.*;

public class EstablishCD extends JFrame {
    public EstablishCD() {
        setTitle("Establish Certificates of Deposit");
        Label l1 = new Label("Enter Term Length ");
        Label l2 = new Label("Enter Minimum Deposit ");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 50, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(3,2));

        JTextField termField = new JTextField(10);
        JTextField minField = new JTextField(10);
        JButton returntoManager = new JButton("Return to Manager Screen");


        returntoManager.addActionListener(e -> new ManagerScreen());
        add(termField);
        add(minField);
        add(returntoManager);

        setVisible(true);
    }
}
