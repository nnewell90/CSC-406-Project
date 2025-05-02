import javax.swing.*;
import java.awt.*;

public class RollRatesScreen extends JFrame {
    private JTextField rate;
    private JComboBox<String> accountTypeComboBox;

    public RollRatesScreen() {
        setTitle("Roll Rates");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(new JLabel("New Interest Rate:"));
        rate = new JTextField();
        rate.setMaximumSize(new Dimension(600, 200));
        formPanel.add(rate);

        String[] accountTypes = {"Credit Cards", "Simple Savings", "CD's", "Loans"};
        accountTypeComboBox = new JComboBox<>(accountTypes);
        accountTypeComboBox.setMaximumSize(new Dimension(600, 200));
        formPanel.add(accountTypeComboBox);

        JButton submitButton = new JButton("Roll Rates");
        submitButton.addActionListener(e -> applyRates());

        JButton returnButton = new JButton("Return to Manager");
        returnButton.addActionListener(e -> {dispose();new ManagerScreen();});

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(returnButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);


    }

    private void applyRates() {
        if (rate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a roll rate");
            return;
        }

        if (accountTypeComboBox.getSelectedItem().equals("Credit Cards")) {

            for(LoanAccount.CC account : Database.CCList){
                account.setRate(Double.parseDouble(rate.getText()));
            }
            JOptionPane.showMessageDialog(this, "New Interest Rate has been updated to CC accounts.");

        }else if (accountTypeComboBox.getSelectedItem().equals("Simple Savings")) {

            for (SavingsAccount.SimpleSavingsAccount account : Database.simpleSavingsAccountList){
                account.setInterestRate(Double.parseDouble(rate.getText()));
            }
            JOptionPane.showMessageDialog(this, "New Interest Rate has been updated to Simple Savings accounts.");
        }else if (accountTypeComboBox.getSelectedItem().equals("CD's")) {

            for (SavingsAccount.CDSavingsAccount account : Database.cdSavingsAccountList){
                account.setInterestRate(Double.parseDouble(rate.getText()));
            }
            JOptionPane.showMessageDialog(this, "New Interest Rate has been updated to CDSavings accounts.");
        }else if(accountTypeComboBox.getSelectedItem().equals("Loans")) {

            for (LoanAccount.ShortOrLong account : Database.shortOrLongLoanList){
                account.setRate(Double.parseDouble(rate.getText()));
            }
            JOptionPane.showMessageDialog(this, "New Interest Rate has been updated to Loans accounts.");
        }

        dispose();
        new ManagerScreen();
    }
}
