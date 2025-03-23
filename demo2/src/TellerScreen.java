import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TellerScreen extends JFrame {
    public TellerScreen() {
        setTitle("Teller Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton newAccountButton = new JButton("Create a New Account");
        JButton newCustomerButton = new JButton("Create a New Customer");
        JButton linkButton = new JButton("Link Accounts");
        JButton withdrawButton = new JButton("Withdraw from Account");
        JButton stopButton = new JButton("Stop Payment");
        JButton reviewButton = new JButton("Review Customer Accounts");
        JButton returnButton = new JButton("Return to Main Menu");

        //systemButton.addActionListener(e -> new SystemControllerScreen());
        newCustomerButton.addActionListener(e ->{ dispose(); new NewCustomerScreen();});  // creates new customer
        //managerButton.addActionListener(e -> new LoanInterest());
        //customerButton.addActionListener(e -> new CustomerScreen());
        stopButton.addActionListener(e -> {dispose(); new CustomerScreen();});
        reviewButton.addActionListener(e ->{ dispose(); new CustomerScreen();});
        returnButton.addActionListener(e -> {saveUIState("MainMenu"); new MainMenu();dispose();});


        add(newAccountButton);
        add(newCustomerButton);
        add(linkButton);
        add(withdrawButton);
        add(stopButton);
        add(reviewButton);
        add(returnButton);

        setVisible(true);
    }
    private void saveUIState(String state) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ui_state.txt"))) {
            writer.write(state);
            System.out.println("UI state saved: " + state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
