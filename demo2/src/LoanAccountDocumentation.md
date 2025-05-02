# Loan Account
## General
Customers can have loans, the system tracks these through LoanAccount.
Like SavingsAccount, the outer LoanAccount class is a shell for its child classes;
it is not meant to be directly used.

The child classes are ShortOrLong and CC (Credit Card).

Unlike SavingsAccount, LoanAccount does have a lot of functionality for its child classes.

## Data
### Class Data
None
### Instance Data
- balance - How much of the loan is left to pay off
- rate - The rate of the loan
- currentPaymentDue - The current payment due for this pay period
- paymentDueDate - The due date for this pay period
- notifiedOfPaymentDate - The date the bank knew the last payment was made
  - This is functionally equivalent to lastPaymentMadeDate
- lastPaymentMadeDate - The date the last payment was made on the loan
- missedPayment - A boolean for if this account has had any missed payments during its lifetime

## Functions
    public LoanAccount(String customerID, LocalDate accountCreationDate, double balance, double rate, double currentPaymentDue)
    public LoanAccount(String customerID, LocalDate accountCreationDate, long accountID, double balance, double rate, double currentPaymentDue, LocalDate paymentDueDate, LocalDate notifiedOfPaymentDate, LocalDate lastPaymentMadeDate, boolean missedPayment)

Like AbstractAccount, there are 2 constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
    - The second shouldn't be called anywhere outside fromFileString() in this Class

### Payment functions
    public double getMonthlyPaymentOnLoanWithYearlyRate(int numOfYearsTotal, double balance, double rate
    public double getMonthlyPaymentOnLoanWithMonthlyRate(double balance, double rate)

There are two functions for getting the monthly payment due for Loans
- The first is meant for ShortOrLong loans
  - It calculates the total amount owed per month based on the total years of the loan, the base balance of the loan, and the rate of the loan
- The second isn't used
  - It was meant for CC loans, but other functionality ended up replacing it

### Delete Account
There is no delete account since LoanAccount is not meant to be used.
The child classes implement their own methods for account deletion.

### toFileString() and fromFileString()
The system uses these functions to store to the Database and to restore from it

#### toFileString()
    public String toFileString()

Writes instance data to a String (delimited with colons), and then returns that string.
To see how the entire write looks, view the code, but the general structure is
- Write information for AbstractAccount
    - customerID
    - accountCreationDate
    - AccountType (AbstractAccount)
    - accountID
- Write information for CheckingAccount
    - balance
    - rate
    - paymentDueDate
    - notifiedOfPaymentDate
    - currentPaymentDate
    - missedPayment
- Return the string

Strings are then written to txts in the Database.
Go to DatabaseBasics to see details.

#### fromFileString()
    public static LoanAccount fromFileString(String s)

This is basically the opposite of toFileString();
It reads a String and turns it into a CheckingAccount.
The second constructor is used in this function.

It follows the same structure as toFileString()
- Read information for AbstractAccount
- Read information for LoanAccount
- Create a CheckingAccount object
- Return that object

These objects are added to arrayLists in the Database.
Go to DatabaseBasics to see details.

#### Non-setter/getter functions
Note

## ShortOrLong
Short or Long loans are what you think of when you imagine a loan from the bank.
They are meant to be paid off over multiple years.

### Data
Some of LoanAccount's instance data is used for a different purpose than described.
- balance - The balance left to pay off on the loan, not including fees
- paymentDueDate - The final date of payments, NOT the next one

#### Class data
None

#### Instance data
- thisPaymentDueDate - The next date a payment is due
- fixedPayment - The amount of money that needs to be paid every month
- amountPaidThisMonth - The total amount paid this month
- loanTotal - The absolute total of the loan; this does not decrease as the user pays
- lateFees - An accumulation of late fees that needs to be paid off in addition to the loanTotal
- numOfYearsTotal - The number of years the loan was set up for
- numOfMonthsTotal - numOfYearsTotal, but multiplied by 12

### Functions
#### Constructors
    public ShortOrLong(String customerID, LocalDate accountCreationDate, double loanTotalValue, double rate, double fixedPaymentValue, LocalDate finalPaymentDate, int numOfYears)
    public ShortOrLong(String customerID, LocalDate accountCreationDate, long accountID, double loanTotalValue, double rate, LocalDate paymentDueDate, LocalDate notifiedOfPaymentDate, LocalDate thisPaymentDueDate, LocalDate lastPaymentMadeDate, double fixedPaymentValue, double currentPaymentDue, double balance, double lateFees, double amountPaidThisMonth, boolean missedPayment, int numOfYearsTotal, int numOfMonthsTotal)

Like AbstractAccount, there are two constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
    - The second shouldn't be called anywhere outside fromFileString() in this Class

#### Delete Account
    public static void deleteAccount(ShortOrLong account)

Account deletion goes through the process of
- Removing the Account from a Customer's list of accounts
- Removing the Account from the database
- And finally setting the isDeleted flag for garbage collection

#### toFileString() and fromFileString()
The system uses these functions to store to the Database and to restore from it

##### toFileString()
    public String toFileString()

Writes instance data to a String (delimited with colons), and then returns that string.
To see how the entire write looks, view the code, but the general structure is
- Write information for AbstractAccount
    - customerID
    - accountCreationDate
    - AccountType (AbstractAccount)
    - accountID
- Write information for LoanAccount
    - balance
    - rate
    - currentPaymentDue
    - paymentDueDate
    - notifiedOfPaymentDate
    - lastPaymentDueDate
    - missedPayment
- Write information for ShortOrLong
  - thisPaymentDueDate
  - fixedPayment
  - amountPaidThisMonth
  - loanTotal
  - lateFees
  - numOfYearsTotal
  - numOfMonthsTotal
- Return the string

Strings are then written to txts in the Database.
Go to DatabaseBasics to see details.

##### fromFileString()
    public static ShortOrLong fromFileString(String s)

This is basically the opposite of toFileString();
It reads a String and turns it into a ShortOrLong.
The second constructor is used in this function.

It follows the same structure as toFileString()
- Read information for AbstractAccount
- Read information for LoanAccount
- Read information for ShortOrLong
- Create a ShortOrLong object
- Return that object

These objects are added to arrayLists in the Database.
Go to DatabaseBasics to see details.

#### makePayment
    public void makePayment(double amount, LocalDate dayOfPay)

- Adds amount to amountPaidThisMonth
- Updates notifiedOfPaymentDate and lastPaymentMadeDate

#### monthPaymentCheck()
    public void monthPaymentCheck()

Checks to see if this month's payment has been made
- If amountPaidThisMonth is less than fixedPayment
  - Add a $75 fee
  - Set missedPayment to true
- Reduce balance by the amount paid this month
- Set amountPaidThisMonth to 0.0

#### updatePayPeriod()
    public void updatePayPeriod()

- Moves thisPaymentDueDate one month forward

## CC
CC is the credit card class.

### Data
#### Class Data
None
#### Instance Data
- limit - The total amount charges against this account are allowed to get to
- financeCharge - Basically the interest on a CC
  - This is the functionality that replaced the second payment function in LoanAccount
  - It is only applied when a card is not paid off at the pay period
- sumOfChargesThisMonth - The sum of charges against the account this month
- chargeMessages - An arrayList of strings with information about charges
  - This includes if they were accepted or denied

### Functions
#### Constructors
    public CC(String customerID, LocalDate accountCreationDate, double balance, double rate, double currentPaymentDue, double limit)
    public CC(String customerID, LocalDate accountCreationDate, long accountID, double balance, double rate, double currentPaymentDue, LocalDate paymentDueDate, LocalDate notifiedOfPaymentDate, LocalDate lastPaymentMadeDate, boolean missedPayment, double limit, double financeCharge, double sumOfChargesThisMonth, ArrayList<String> chargeMessages)

Like AbstractAccount, there are 2 constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
    - The second shouldn't be called anywhere outside fromFileString() in this Class

#### Delete Account
    public static void deleteAccount(CC account)

Account deletion goes through the process of
- Removing the Account from a Customer's list of accounts
- Removing the Account from the database
- And finally setting the isDeleted flag for garbage collection

#### toFileString() and fromFileString()
The system uses these functions to store to the Database and to restore from it

##### toFileString()
    public String toFileString()

Writes instance data to a String (delimited with colons), and then returns that string.
To see how the entire write looks, view the code, but the general structure is
- Write information for AbstractAccount
    - customerID
    - accountCreationDate
    - AccountType (AbstractAccount)
    - accountID
- Write information for LoanAccount
    - balance
    - rate
    - currentPaymentDue
    - paymentDueDate
    - notifiedOfPaymentDate
    - lastPaymentDueDate
    - missedPayment
- Write information for CC
  - limit
  - financeCharge
  - sumOfChargesThisMonth
  - chargeMessages
- Return the string

Strings are then written to txts in the Database.
Go to DatabaseBasics to see details.

##### fromFileString()
    public static CC fromFileString(String s)

This is basically the opposite of toFileString();
It reads a String and turns it into a ShortOrLong.
The second constructor is used in this function.

It follows the same structure as toFileString()
- Read information for AbstractAccount
- Read information for LoanAccount
- Read information for CC
- Create a ShortOrLong object
- Return that object

These objects are added to arrayLists in the Database.
Go to DatabaseBasics to see details.

#### Charge
    public void charge(double amount, String description, LocalDate dayOfCharge)

Charges against a CC are made using this
- It checks if this charge would go beyond the limit
- If not, amount is added to sumOfChargesThisMonth
- Either way, a string is added to chargeMessages
  - If the charge went through, it is appended with --ACCEPTED
  - If not, it is appended with --DENIED

#### Payment
    public void payment (double amount)

Reduces balance by amount

#### getBillThisMonth
    public String getBillThisMonth()

Gets the bill for this month as a string
- Line 1: Sum of charges for this month
  - This is a total, it does not show all charges
  - It does not include a finance charge if one was added
- Line 2: The finance charge for this month

#### getChargeMessageString
    public String getChargeMessagesString()

Returns the history of charges for this card as a string
- Starts with "Charge History for Credit Card"
- Every charge is put on a new line

#### Finance charge
    public boolean isFinanceChargeAddedToTotal()
    public void addFinanceChargeToTotal()
    public double calculateFinanceCharge(int daysInMonth)

A finance charge is added to the balance of a CC if it isn't paid off by the end of the month.
A finance charge is the sum of the charges for this month divided by the number of days in the month.


isFinanceChargeAddedToTotal()
- Returns a boolean
  - If it returns true, a finance charge is added to balance

addFinanceChargeToTotal()
- Adds financeCharge to balance

calculateFinanceCharge(int daysInMonth)
- Calculates the finance charge
  - sumOfChargesThisMonth / the number of days in this month
