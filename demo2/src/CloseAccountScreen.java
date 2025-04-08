import javax.swing.*;
        import java.awt.*;

public class CloseAccountScreen extends JFrame {
    public CloseAccountScreen() {
        setTitle("Close an Account");
        Label l1 = new Label("Please enter the account holder's name: ");
        Label l2 = new Label("Please enter the account holder's SSN:");
        l1.setBounds(100, 50, 120, 80);
        l2.setBounds(100, 100, 120, 80);
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        JTextField accountholderNameField = new JTextField(10);
        JTextField accountholderSSNField = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");


        //ButtonGroup accountGroup = new ButtonGroup();


        returntoTellerScreen.addActionListener(e -> {dispose(); new TellerScreen();});
        add(accountholderNameField);
        add(accountholderSSNField);



        add(submitButton);
        add(returntoTellerScreen);


        setVisible(true);
    }
}

