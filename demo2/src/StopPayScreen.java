import javax.swing.*;
import java.awt.*;

public class StopPayScreen extends JFrame {
    public StopPayScreen() {
        setTitle("Stop Payment");
        Label l1 = new Label("Please enter the check number you wish to stop:");
        l1.setBounds(100, 50, 120, 80);
        add(l1);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField accountholderField = new JTextField(10);
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        add(accountholderField);


        add(returntoTellerScreen);


        setVisible(true);
    }
}

