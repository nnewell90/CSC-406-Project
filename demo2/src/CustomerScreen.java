import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerScreen extends JFrame {
    public CustomerScreen() {
        setTitle("Customer Screen");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JButton creditButton = new JButton("Credit Card");
        JButton reviewButton = new JButton("Review Account Status");
        JButton checkButton = new JButton("Input Checks");
        JButton returnButton = new JButton("Return to Main Menu");


        creditButton.addActionListener(e -> {
            dispose();
            new CreditCardScreen();
        });
        reviewButton.addActionListener(e -> {
            dispose();
            new CustomerSignIn();
        });
        checkButton.addActionListener(e -> {
            dispose();
            new InsertCheckScreen();
        });
        returnButton.addActionListener(e -> {
            saveUIState("MainMenu");
            dispose();
            new MainMenu();
        });


        add(creditButton);
        add(reviewButton);
        add(checkButton);
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
