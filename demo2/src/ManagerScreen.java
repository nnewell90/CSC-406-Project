import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ManagerScreen extends JFrame {
    public ManagerScreen() {
        setTitle("Manager Screen");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        JButton loanButton = new JButton("Loan Interest Rates");
        JButton cdButton = new JButton("CD Interest Rates");
        JButton cdmanageButton = new JButton("CD Manager");
        JButton checksButton = new JButton("Checks Processing");
        JButton rollRatesButton = new JButton("Roll Rates");
        JButton returnButton = new JButton("Return to Main Menu");

        loanButton.addActionListener(e -> {dispose(); new LoanInterestScreen();});
        cdButton.addActionListener(e -> {dispose(); new CDInterestScreen();});
        cdmanageButton.addActionListener(e -> {dispose(); new EstablishCD();});
        checksButton.addActionListener(e -> {dispose(); new ProcessCheck();});
        rollRatesButton.addActionListener(e -> {dispose(); new RollRatesScreen();});
        returnButton.addActionListener(e -> {saveUIState("MainMenu"); new MainMenu();dispose();});


        add(loanButton);
        add(cdButton);
        add(cdmanageButton);
        add(checksButton);
        add(rollRatesButton);
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
