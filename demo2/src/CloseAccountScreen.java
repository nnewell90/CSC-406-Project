import javax.swing.*;
        import java.awt.*;
import java.time.LocalDate;

public class CloseAccountScreen extends JFrame {
    JTextField accountIDField;

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
        JButton returnToTellerScreen = new JButton("Return to Teller Screen");


        //ButtonGroup accountGroup = new ButtonGroup();

        submitButton.addActionListener(e -> closeAccount());
        returnToTellerScreen.addActionListener(e -> {
            dispose();
            new TellerScreen();
        });


        add(submitButton);
        add(returnToTellerScreen);


        setVisible(true);
    }

    private void closeAccount() {

        if (accountIDField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Account ID");
        } else {

            long accountID = Long.parseLong(accountIDField.getText());
            AbstractAccount account = Database.getAccountFromList(Database.abstractAccountList, accountID);

            if (account == null) {
                JOptionPane.showMessageDialog(null, "Account Not Found, check Credentials");
            } else {

                if (account.accountType == AbstractAccount.AccountType.CheckingAccount) {//checking accounts
                    CheckingAccount checkingAccount = (CheckingAccount) account;

                    calculateRemaining(checkingAccount);
                    CheckingAccount.deleteAccount(checkingAccount);
                    JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                    dispose();
                    new TellerScreen();
                } else if (account.accountType == AbstractAccount.AccountType.SavingsAccount) {//savings accounts

                    if (account instanceof SavingsAccount.SimpleSavingsAccount ssa) {//simple savings
                        calculateRemaining(ssa);
                        SavingsAccount.SimpleSavingsAccount.deleteAccount(ssa);
                        JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                        dispose();
                        new TellerScreen();

                    } else if (account instanceof SavingsAccount.CDSavingsAccount cdAccount) {//CD savings
                        LocalDate today = LocalDate.now();

                        int amount = (int) cdAccount.getBalance();
                        //Don't let them withdraw too much from the cd account

                        if (today.isAfter(cdAccount.dueDate) || today.equals(cdAccount.dueDate)) {

                            cdAccount.withdrawAfterDueDate(amount);
                            JOptionPane.showMessageDialog(null, "CD Account #" + account.getAccountID() + " is closed. Customer is owed $" + amount);
                            dispose();
                            new TellerScreen();

                        } else if (today.isBefore(cdAccount.dueDate)) {//if they withdraw before the maturation date, proceed but penalize

                            //apply "penalty"
                            JOptionPane.showMessageDialog(this, "CD account Closed! However, " +
                                    "a penalty was applied for closing before " + cdAccount.dueDate + ". Customer is owed $" + amount);

                            cdAccount.withdrawBeforeDueDate(amount);

                            JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                            dispose();
                            new TellerScreen();
                        }

                    }//end of CD savings if


                }else if (account.accountType == AbstractAccount.AccountType.ShortOrLongLoanAccount) {//Loan account

                    LoanAccount.ShortOrLong loanAccount = (LoanAccount.ShortOrLong) account;
                    calculateRemaining(loanAccount);
                    LoanAccount.ShortOrLong.deleteAccount(loanAccount);
                    JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                    dispose();
                    new TellerScreen();

                } else if(account.accountType == AbstractAccount.AccountType.CCLoanAccount) {//credit card
                    LoanAccount.CC creditCard = (LoanAccount.CC) account;
                    calculateRemaining(creditCard);
                    LoanAccount.CC.deleteAccount(creditCard);
                    JOptionPane.showMessageDialog(null, "Account #" + accountID + " Deleted");
                    dispose();
                    new TellerScreen();
                }else {
                    JOptionPane.showMessageDialog(null, "Cannot delete this type of account.");
                }


            }
        }
    }//End of Close Account

    private void calculateRemaining (AbstractAccount account){
        double balance = 0;
        if (account instanceof CheckingAccount checkingAccount) {
            balance = checkingAccount.getBalance();
        } else if (account instanceof SavingsAccount savingAccount) {
            balance = savingAccount.getBalance();
        }else if (account instanceof LoanAccount.ShortOrLong loanAccount) {
            balance = loanAccount.getBalance();
        }else if(account instanceof LoanAccount.CC creditCard) {
            balance = creditCard.getBalance();
        }

        if (balance > 0) {
            if (account instanceof LoanAccount || account instanceof LoanAccount.ShortOrLong || account instanceof LoanAccount.CC) {
                JOptionPane.showMessageDialog(this,
                        "Account closed. Customer still owes the bank $" + balance + ".");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Account closed. Customer is owed $" + balance + ".");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Account closed. No balance remaining.");
        }
    }


}

