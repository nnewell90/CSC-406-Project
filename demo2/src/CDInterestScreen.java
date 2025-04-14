import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CDInterestScreen extends JFrame {

    JTextField rate, accountID;
    public CDInterestScreen() {
        setTitle("CD Interest Rates");
        Label l1 = new Label("Enter Interest Rate: ");
        l1.setBounds(100, 50, 120, 80);
        Label l2 = new Label("Enter CD Account ID: ");
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        rate = new JTextField(10);
        add(rate);
        accountID = new JTextField(10);
        add(accountID);

        JButton returntoManager = new JButton("Return to Manager Screen");

        JButton submit = new JButton("Change Rate");
        submit.addActionListener(e -> {changeRate();});

        returntoManager.addActionListener(e -> {dispose(); new ManagerScreen();});
        add(submit);
        add(returntoManager);

        setVisible(true);
    }

    private void changeRate(){

        if(rate.getText().isEmpty() || accountID.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter Rate and Account ID");
            return;
        }

        double rateValue = Double.parseDouble(rate.getText());
        Long id = Long.parseLong(accountID.getText());

        if(rate.getText().isEmpty() || accountID.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter Rate and Account ID");
        }else{

            AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, id);
            if(account == null){
                JOptionPane.showMessageDialog(null, "Account not found");
            }else{

                if(account.getAccountType() == AbstractAccount.AccountType.CDSavingsAccount){
                    SavingsAccount.CDSavingsAccount cdAccount = (SavingsAccount.CDSavingsAccount) account;
                    cdAccount.setInterestRate(rateValue);
                    JOptionPane.showMessageDialog(null, "Rate has been changed to " + rateValue);
                    dispose();
                    new ManagerScreen();
                }else {
                    JOptionPane.showMessageDialog(null, "Account type is not CD Account");
                }

            }

        }

    }


}
