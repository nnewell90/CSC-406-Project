import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProcessCheck extends JFrame {

    JTextField accountID, checkNum, amount;

    public ProcessCheck() {
        setTitle("Process Checks");
        setSize(700, 700);
        setLayout(new GridLayout(5, 1));
        add(new JLabel("Account ID:"));
        accountID = new JTextField(10);
        add(accountID);

        add(new JLabel("Check Number (4 digit number):"));
        checkNum = new JTextField(10);
        add(checkNum);
        setLayout(new GridLayout(4, 1));

        add(new JLabel("Amount:"));
        amount = new JTextField(10);
        add(amount);

        JButton returntoManager = new JButton("Return to Manager Screen");
        JButton submitButton = new JButton("Process Check");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processCheck();
            }
        });

        returntoManager.addActionListener(e -> {dispose(); new ManagerScreen();});

        add(submitButton);
        add(returntoManager);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void processCheck() {

        if(accountID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter account ID!");
        }else if(checkNum.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter check number!");
        }else if(amount.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter amount!");
        }else{

            long ID = Long.parseLong(accountID.getText());
            String num = checkNum.getText().trim();
            int amt = Integer.parseInt(amount.getText());
            CheckingAccount account = (CheckingAccount) Database.getAccountFromList(Database.abstractAccountList, ID);

            if(account == null) {
                JOptionPane.showMessageDialog(null, "Account not found! Check credentials");
            }else if(account.accountType != AbstractAccount.AccountType.CheckingAccount){
                JOptionPane.showMessageDialog(null, "Account type is not checking account!");
            }else if(account != null && account.accountType == AbstractAccount.AccountType.CheckingAccount){

                if(!account.validateCheckNumber(num)) {
                    JOptionPane.showMessageDialog(null, "Check number is not valid!");
                    return;
                }

                account.addCheckToProcessLater(amt, num);
                JOptionPane.showMessageDialog(null, "Check amount successfully withdrawn!");
                dispose();
                new ManagerScreen();

            }

        }

    }

}

