import javax.swing.*;
        import java.awt.*;

public class CloseAccountScreen extends JFrame {
    JTextField ssnField, accountIDField;

    public CloseAccountScreen() {
        setTitle("Close an Account");
        Label l1 = new Label("Please enter the Account ID you want closed: ");
        l1.setBounds(100, 50, 120, 80);
        add(l1);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        accountIDField = new JTextField(10);
        add(accountIDField);
        JButton submitButton = new JButton("Submit");
        JButton returntoTellerScreen = new JButton("Return to Teller Screen");


        //ButtonGroup accountGroup = new ButtonGroup();

        submitButton.addActionListener(e -> {
            closeAccount();
        });
        returntoTellerScreen.addActionListener(e -> {
            dispose();
            new TellerScreen();
        });


        add(submitButton);
        add(returntoTellerScreen);


        setVisible(true);
    }

    private void closeAccount() {

        if (accountIDField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Account ID");
        } else {

            Long accountID = Long.parseLong(accountIDField.getText());
            AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

            if (account == null) {
                JOptionPane.showMessageDialog(null, "Account Not Found, check Credentials");
            } else if (account != null) {

                if (account.accountType == AbstractAccount.AccountType.CheckingAccount) {
                    CheckingAccount checkingAccount = (CheckingAccount) account;
                    CheckingAccount.deleteAccount(checkingAccount);
                    JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                    dispose();
                    new TellerScreen();
                } else if (account.accountType == AbstractAccount.AccountType.SavingsAccount) {

                    if (account instanceof SavingsAccount.SimpleSavingsAccount) {
                        SavingsAccount.SimpleSavingsAccount ssa = (SavingsAccount.SimpleSavingsAccount) account;
                        SavingsAccount.SimpleSavingsAccount.deleteAccount(ssa);
                        JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                        dispose();
                        new TellerScreen();
                    } else if (account instanceof SavingsAccount.CDSavingsAccount) {
                        SavingsAccount.CDSavingsAccount cda = (SavingsAccount.CDSavingsAccount) account;
                        SavingsAccount.CDSavingsAccount.deleteAccount(cda);
                        JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                        dispose();
                        new TellerScreen();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot delete this type of account.");
                }

            }
        }
    }

}

