import javax.swing.*;

public class AccountDetailsScreen extends JFrame {
    public AccountDetailsScreen(String id) {
        long accountID = Long.parseLong(id);
        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

        /*
        TODO wanna display account info on this screen.
        Things like recent purchases and transactions. and balance.
        Not sure where to get that content,
         */


    }
}
