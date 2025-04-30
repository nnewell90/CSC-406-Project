import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class WithdrawScreen extends JFrame {
    private JTextField withdraw;
    private JTextField ssn;
    private JTextField account;

    public WithdrawScreen() {

        setTitle("Withdraw From Account");
        /*Label l1 = new Label("Please select an account type to withdraw from:");
        l1.setBounds(100, 50, 120, 80);
        add(l1);*/
        setSize(500, 500);
        setLayout(new GridLayout(4, 1));

        // Labels and text fields
        add(new JLabel("Please enter the customer's Social Security Number: "));
        ssn = new JTextField();
        add(ssn);

        add(new JLabel("Please enter the amount to withdraw: "));
        withdraw = new JTextField();
        add(withdraw);


        add(new JLabel("Please enter the account number you wish to withdraw from: "));
        account = new JTextField();
        add(account);

        JButton submitButton = new JButton("Withdraw from Account");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String SSN = ssn.getText().trim();
                double amount = Double.parseDouble(withdraw.getText().trim());
                long accountID = Long.parseLong(account.getText().trim());


                makeWithdrawal(SSN, amount, accountID);
            }
        });
        JButton returntoButton = new JButton("Return to Teller Screen");

        returntoButton.addActionListener(e -> {
            dispose();
            new TellerScreen();
        });

        add(submitButton);
        add(returntoButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    public void makeWithdrawal(String SSN, double amount, Long accountID){
        Customer customer = Database.getCustomerFromList(SSN);
        AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

        if(customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found! Check Customer ID");
            return;
        }else if(account == null) {
            JOptionPane.showMessageDialog(this, "Account not found! Check Account ID");
            return;
        }else if(amount <= 0){
            JOptionPane.showMessageDialog(this, "Please enter a valid withdrawal amount greater than $0.");
            return;
        }

        AbstractAccount.AccountType type = account.accountType;

        if (account instanceof SavingsAccount.SimpleSavingsAccount savings){ //If withdrawing from a Savings Account

            if (amount > savings.getBalance()) {
                JOptionPane.showMessageDialog(this, "Insufficient funds in savings.");
                return;
            }

            double preSave = savings.getBalance();
            savings.withdraw(amount);
            double postSave = savings.getBalance();

            if (preSave > postSave) {
                JOptionPane.showMessageDialog(this, "Withdraw successful!");

            }else{
                JOptionPane.showMessageDialog(this, "Withdrawal Denied. Insufficient Funds.");
            }

        }else if (account instanceof CheckingAccount checking){//If withdrawing from a checking account

            if(checking.getBalance() >= amount) {
                checking.withdraw(amount);
                JOptionPane.showMessageDialog(this, "Withdrawal successful!");
            }else{

                long overDraftAccountID = checking.overDraftAccountID;
                SavingsAccount.SimpleSavingsAccount overdraftAccount = (SavingsAccount.SimpleSavingsAccount) Database.getAccountFromList(Database.simpleSavingsAccountList, overDraftAccountID);

                if(overdraftAccount == null) {
                    JOptionPane.showMessageDialog(this, "Withdrawal Denied. Insufficient funds. ");
                }else{

                    double preCheck = checking.getBalance();
                    double preSavings = overdraftAccount.getBalance();

                    checking.withdraw(amount);

                    double postCheck = checking.getBalance();
                    double postSavings = overdraftAccount.getBalance();

                    if(postCheck<preCheck && postSavings<preSavings){
                        JOptionPane.showMessageDialog(this, "Withdrawal Successful however overdraft was applied.");
                    }else{
                        JOptionPane.showMessageDialog(this, "Withdrawal Denied. Insufficient funds.");
                    }

                }

            }

        }else if (account instanceof SavingsAccount.CDSavingsAccount cdAccount){//if withdrawing from a CD
            LocalDate today = LocalDate.now();

            //Don't let them withdraw too much from the cd account
            if (cdAccount.balance >= amount) {

                if (today.isAfter(cdAccount.dueDate) || today.equals(cdAccount.dueDate)) {

                    cdAccount.withdraw(amount);
                    JOptionPane.showMessageDialog(this, "Withdraw successful!");
                }else if(today.isBefore(cdAccount.dueDate)) {//if they withdraw before the maturation date, proceed but penalize
                    
                    //apply "penalty"
                    cdAccount.withdraw(amount);
                    JOptionPane.showMessageDialog(this, "Withdraw successful! However, " +
                            "Penalty was applied for withdrawing before " + cdAccount.dueDate);

                }
            } else if (amount > cdAccount.balance) {//if they don't have enough, don't bother checking the date
                JOptionPane.showMessageDialog(this, "Insufficient funds! You only have $" +
                        cdAccount.balance+" in Certificate of Deposit.");
            }

        }else if(account instanceof LoanAccount.CC creditCard){

            String description = "Bank Withdrawal";
            LocalDate today = LocalDate.now();
            double preBalance = creditCard.getBalance();
            creditCard.charge(amount, description, today);

            if (preBalance + amount <= creditCard.getLimit()) {
                JOptionPane.showMessageDialog(this, "Withdrawal was Successful!");
            } else { // Over the limit
                JOptionPane.showMessageDialog(this, "Withdrawal was Denied!");
            }


        }else{//if withdrawing from an account that isn't checking or savings, then deny the withdrawal
            JOptionPane.showMessageDialog(this, "You cannot withdraw from a "+ type);
        }

        dispose();
        new TellerScreen();
    }//end of makeWithdrawal


}




