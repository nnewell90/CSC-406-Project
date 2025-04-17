import java.util.Date;

public class CreateLoanAccountScreen {

    public CreateLoanAccountScreen(Customer customer, Date date) {

        /*
        I figured there were so many details for LoanAccounts that it could use its own screen
         TODO values that need to be instantiated from the user are...
            Loan Amount(which is balance), InterestRate,
             and MAYBE loan Term: You could probably display the start date which was kept from the previous screen,
            Then we might want a final payment date.
         */

    }

    public void CreateLoanAccount(Customer customer, Date date, double balance,
                                  double rate, double currentPaymentDue) {

        String id = customer.getCustomerID();
        AbstractAccount.AccountType accountType = AbstractAccount.AccountType.LoanAccount;

        LoanAccount lnAccount = new LoanAccount(id, date, balance, rate, currentPaymentDue);
    }

}
