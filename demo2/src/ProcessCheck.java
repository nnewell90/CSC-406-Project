import javax.swing.*;
import java.awt.*;

public class ProcessCheck extends JFrame {
    public ProcessCheck() {
        setTitle("CD Interest Rates");
        Label l1 = new Label("Enter how many checks need to be processed: ");
        l1.setBounds(100, 50, 120, 80);
        add(l1);
        setSize(300, 200);
        setLayout(new GridLayout(4, 1));

        JTextField loanRateField = new JTextField(10);
        JButton returntoManager = new JButton("Return to Manager Screen");


        returntoManager.addActionListener(e -> new ManagerScreen());
        add(loanRateField);
        add(returntoManager);

        setVisible(true);
    }
}

