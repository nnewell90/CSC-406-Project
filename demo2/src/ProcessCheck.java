import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProcessCheck extends JFrame {

    JTextField accountID, checkNum, amount;
    public ProcessCheck() {
        setTitle("Process Checks");
        setSize(700, 700);
        setLayout(new GridLayout(5, 1));
        add(new JLabel("Account ID:"));
        accountID = new JTextField(10);
        add(accountID);


        JButton returntoManager = new JButton("Return to Manager Screen");
        JButton submitButton = new JButton("Process Checks");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                process();
            }
        });

        returntoManager.addActionListener(e -> {
            dispose();
            new ManagerScreen();
        });

        add(submitButton);
        add(returntoManager);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void process() {

        if (accountID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter account ID!");
        } else {

            long ID = Long.parseLong(accountID.getText().trim());
            AbstractAccount found = Database.getAccountFromList(Database.abstractAccountList, ID);

            if (!(found instanceof CheckingAccount account)) {
                JOptionPane.showMessageDialog(this, "Account is NOT a checking account!");
                return;
            }

            if(account.checkMap.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No checks in the queue to process!");
                return;
            }

            ArrayList<String> skippedChecks = new ArrayList<>();
            for (String checkNumber : new ArrayList<>(account.checkMap.keySet())) {
                if (account.stopPaymentArray.contains(checkNumber)) {
                    skippedChecks.add(checkNumber);
                }
            }

            account.processChecks();

            if (skippedChecks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All checks processed successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "The following checks were skipped due to stop payment:\n" + skippedChecks);
            }

            dispose();
            new ManagerScreen();
        }

    }

}

