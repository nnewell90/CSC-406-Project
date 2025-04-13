import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoanInterestScreen extends JFrame {
    JTextField rate, accountID;
    public LoanInterestScreen() {
        setTitle("Loan Interest Rates");
        Label l1 = new Label("Enter Interest Rate: ");
        l1.setBounds(100, 50, 120, 80);
        Label l2 = new Label("Enter Loan Account ID: ");
        add(l1);
        add(l2);
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        rate = new JTextField(10);
        add(rate);

        accountID = new JTextField(10);
        add(accountID);

        JButton submit = new JButton("Change Rate");
        submit.addActionListener(e -> {changeRate();});
        JTextField loanRateField = new JTextField(10);
        JButton returntoManager = new JButton("Return to Manager Screen");

        add(submit);
        returntoManager.addActionListener(e -> {dispose(); new ManagerScreen(); });
        add(loanRateField);
        add(returntoManager);

        setVisible(true);
    }

    private void changeRate(){
        double rateValue = Double.parseDouble(rate.getText());
        Long id = Long.parseLong(accountID.getText());

        if(rate.getText().isEmpty() || accountID.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter Rate and Account ID");
        }else{

            AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, id);
            if(account == null){
                JOptionPane.showMessageDialog(null, "Account not found");
            }else{

                if(account.getAccountType() == AbstractAccount.AccountType.LoanAccount){
                    LoanAccount loanAccount = (LoanAccount) account;
                    loanAccount.setRate(rateValue);
                    JOptionPane.showMessageDialog(null, "Rate has been changed to " + rateValue);
                    dispose();
                    new ManagerScreen();
                }else {
                    JOptionPane.showMessageDialog(null, "Account type is not LoanAccount");
                }

            }

        }

    }

}
