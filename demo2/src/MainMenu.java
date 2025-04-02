import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MainMenu extends JFrame {
    private static final String UI_STATE_FILE = "ui_state.txt";
    private String lastScreen = "MainMenu"; // Default to Main Menu

    public MainMenu() {

        Database.restoreFromDatabase();

        setTitle("Main Menu");
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        //here is where our las screen is gonna be loaded from
        lastScreen = loadUIState();

        // If the last screen before the system went down was the NOT MainMenu, open that instead
        if (!lastScreen.equals("MainMenu")) {
            openLastScreen();
            return; // Also, this'll stop the main menu from appearing every time, it's kind of annoying
        }

        JButton tellerButton = new JButton("Teller Screen");
        JButton managerButton = new JButton("Manager Screen");
        JButton customerButton = new JButton("Customer Screen");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tellerButton.addActionListener(e -> {
            lastScreen = "TellerScreen";
            saveUIState();
            new TellerScreen();
            dispose(); // Close the Main Menu
        });

        managerButton.addActionListener(e -> {
            lastScreen = "ManagerScreen";
            saveUIState();
            new ManagerScreen();
            dispose();
        });

        customerButton.addActionListener(e -> {
            lastScreen = "CustomerScreen";
            saveUIState();
            new CustomerScreen();
            dispose();
        });

        add(tellerButton);
        add(managerButton);
        add(customerButton);

        // Please, only save UI state only when closing the program
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveUIState();
            }
        });

        setVisible(true);
    }

    // Save the last screen, ONLY if it isn't the MainMenu
    private void saveUIState() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UI_STATE_FILE))) {
            if (!lastScreen.equals("MainMenu")) {
                writer.write(lastScreen);
                System.out.println("UI state saved: " + lastScreen);
            } else {
                writer.write("MainMenu"); //Save it if it's the last screen we were on
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Now we're gonna load the last screen state the user was on
    private String loadUIState() {
        File file = new File(UI_STATE_FILE);
        if (!file.exists()) return "MainMenu";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "MainMenu";
        }
    }

    // Open the last screen instead of the Main Menu IF IT WAS THE LAST THING OPEN BEFORE IT WAS SHUT DOWN
    private void openLastScreen() {
        switch (lastScreen) {
            case "TellerScreen":
                new TellerScreen();
                break;
            case "ManagerScreen":
                new ManagerScreen();
                break;
            case "CustomerScreen":
                new CustomerScreen();
                break;
            default:
                new MainMenu(); // Fallback to main menu if something goes horribly, horribly wrong
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}
