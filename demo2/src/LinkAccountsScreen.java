import javax.swing.*;
import java.awt.*;

public class LinkAccountsScreen extends JFrame {
    public LinkAccountsScreen() {
        setTitle("Link Accounts");
        Label l1 = new Label("Please enter the first account number:");
        Label l2 = new Label("Please enter the second account number you want to link:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField accountNumber1Field = new JTextField(10);
        JTextField accountNumber2Field = new JTextField(10);
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");




        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        add(accountNumber1Field);
        add(accountNumber2Field);
        add(returntoTellerScreen);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        setVisible(true);
    }
}
