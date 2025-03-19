import java.util.Date;

public class LoanAccount extends AbstractAccount {

    public LoanAccount(String customerID, Date accountCreationDate, String accountType) {
        super(customerID, accountCreationDate, accountType);
    }

    @Override
    public String getAccountType() {
        return "";
    }

    @Override
    public String toFileString() {
        return "";
    }

    @Override
    public LoanAccount fromFileString() {
        LoanAccount temp = null;

        return temp;
    }

    static class ShortOrLong extends LoanAccount {
        // Data
        Date finalPaymentDate;
        Date nextPaymentDate;
        double rate;
        double fixedPayment;
        boolean problemAccount = false;


        public ShortOrLong(String customerID, Date accountCreationDate, String accountType) {
            super(customerID, accountCreationDate, accountType);
        }

        @Override
        public String getAccountType() {
            return "Short or Long term Loan Account";
        }

        @Override
        public String toFileString() {
            return "";
        }

        @Override
        public ShortOrLong fromFileString() {
            ShortOrLong temp = null;

            return temp;
        }
    }

    static class CC extends LoanAccount {
        // Data
        double limit;

        public CC(String customerID, Date accountCreationDate, String accountType) {
            super(customerID, accountCreationDate, accountType);
        }

        @Override
        public String getAccountType() {
            return "Credit Card";
        }

        @Override
        public String toFileString() {
            return "";
        }

        @Override
        public CC fromFileString() {
            CC temp = null;

            return temp;
        }
    }
}
