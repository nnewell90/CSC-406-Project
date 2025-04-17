import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.restoreFromDatabase();
        SwingUtilities.invokeLater(MainMenu::new);
    }
}