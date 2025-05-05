# Savings Account
## General
The SavingsAccount class isn't meant to be directly used in the system.
Instead, it acts as a shell for SimpleSavingsAccount (SSA) and CDSavingsAccount (CD).

## Data
balance
- SSA - The balance of the account
- CD - How much the account is worth

## Functions

### Constructors
    public SavingsAccount(String customerID, LocalDate accountCreationDate, double initialBalance)
    public SavingsAccount(String customerID, LocalDate accountCreationDate, double balance, long accountID)

Like AbstractAccount, there are 2 constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
    - The second shouldn't be called anywhere outside fromFileString() in this Class
 
### toFileString() and fromFileString()
    public String toFileString()
    public static SavingsAccount fromFileString(String s)    

The system uses these functions to write data to the Database and get data from it
These are fleshed out in the child classes; the ones here are pretty bare, so they won't be gone over.

### deposit() and withdraw()
    public void deposit(double amount)
    public void withdraw(double amount)

Very basic functions
- They both check
  - If the Account has been deleted
  - If the given amount is less than 0.0
- If both checks pass
  - Deposit - Deposits the amount
  - Withdraw - Checks if there are enough funds, and if so withdraws

## SimpleSavingsAccount (SSA)
SimpleSavingsAccount acts as the actual saving account class for the system.
It is fairly simple and shares a lot of similarities/functionality with CheckingAccount.

### Data
#### Class Data
- interestRate - The interest rate all savings accounts share

#### Instance data
- balance - Not listed here, it comes from SavingsAccount
- stopPaymentArray - An arrayList of check numbers to stop payments on
- checkMap - A map of checks; the key is the check number and the value the amount
    - Amounts are positive for deposits and negative for withdrawals
- linkedToATMCard - Whether this account is linked to an ATMCard
    - The ATMCard is not tracked, simply whether it is linked
- overdraftAccountID - CheckingAccounts can be linked to a SimpleSavingsAccount for overdraft protection; this is the accountID of that account

### Functions
#### Constructors
    public SimpleSavingsAccount(String customerID, LocalDate accountCreationDate, int initialBalance)
    public SimpleSavingsAccount(String customerID, LocalDate accountCreationDate, long accountID, double balance, long overdraftAccountID, boolean linkedToATMCard, HashMap<String, Double> checkMapPassed, ArrayList<String> stopPaymentArrayPassed)

Like AbstractAccount, there are 2 constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
    - The second shouldn't be called anywhere outside fromFileString() in this Class

#### Delete account
    public static void deleteAccount(SimpleSavingsAccount account)
Account deletion goes through a process of
- Removing the linkage to an overdraft account if a link exists
- Removing the Account from a Customer's list of accounts
- Deleting an ATMCard linked to this account if one exists
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
- Write information for CheckingAccount
    - balance
    - Overdraft accountID (If one is linked, otherwise -1)
    - isLinkedToATMCard (This is a boolean)
    - isDeleted
    - The check map
      - First size, then entries
    - The stop payment arrayList
      - First size, then entries
- Return the string

Strings are then written to txts in the Database.
Go to DatabaseBasics to see details.

##### fromFileString()
    public static SimpleSavingsAccount fromFileString(String s)

This is basically the opposite of toFileString();
It reads a String and turns it into a CheckingAccount.
The second constructor is used in this function.

It follows the same structure as toFileString()
- Read information for AbstractAccount
- Read information for CheckingAccount
- Create a SimpleSavingsAccount object
- Return that object

These objects are added to arrayLists in the Database.
Go to DatabaseBasics to see details.

##### Depositing, Withdrawing, and Transferring
Depositing and Withdrawing use the functions from SavingsAccount

#### Transfer
    public void transfer(double amount, long accountToTransferToID)
Transfer is complicated because it has to check for different Account types from AbstractAccount.
Specifically, transfers can occur between CheckingAccounts and SimpleSavingsAccounts.
- It checks if the account is deleted
- It checks if the amount is less than or equal to 0.0
- It checks if there are enough funds to cover the transfer
    - Note this only looks at this account's balance; it does not consider overdraft accounts
- Next, it checks if the given account ID matches an accountID the Customer owns
- If one is found, check what type of account it is
    - The given accountID may not be a transferable account
    - Transferable AccountTypes are CheckingAccount and SimpleSavingsAccount
    - If the Account was neither of these types, the transfer ends without processing anything
- At this point a valid transferable Account was found
    - Actually do the transfer
- Update the AccountType based on the minimum threshold for GoldDiamond accounts

### Checks
There is a check system for checking accounts.
Checks can be added to a list (really a map) of checks, which are then processed later.
There is also a list of check numbers to stop the processing of.

#### addCheckToProcessLater()
    public void addCheckToProcessLater(double amount, String checkNumber)
Adds a check to the list of checks if it is both valid and doesn't already exist in the list.
- amount should be positive for deposits and negative for withdrawals.

#### processChecks()
    public void processChecks()
Process all the checks for an account.
- Deposits are processed before withdrawals
- Stop the processing of a check if that check's number is in the stopPayment arrayList

#### addStopPaymentNumber()
    public void addStopPaymentNumber(String checkNumber)
Adds a check number to the list of check numbers to not be procssed.
- This comes with a $25 charge

#### validateCheckNumber
    public boolean validateCheckNumber(String checkNumber)
This function is a little fake since we didn't know exactly how check numbers work.

Below are checks this does; if any fail, the check is rejected
- Is the number in the stopPaymentArray?
- Is the check number's length equal to 1 (this is the fake part)
- Does the check number contain any non-number characters?

#### Other non-basic-setter/getter functions
#### calcAndAddInterest
    public void calcAndAddInterest()
All SimpleSavingsAccounts share an interest rate, this function is used to update accounts with their interest.

#### setInterestRate()
    public static void setInterestRate(double interestRatePassed)
    public static void setInterestRateByCheckingAccount (double interestRatePassed)
There are two functions for setting interest rates because Checking and SimpleSavings accounts have a linked interest rate.
Specifically, CheckingAccount has half the interest of SimpleSavingsAccount.
- What this means is that when one is updated, the other Class needs to also be updated

This leads to two functions
- The first is meant to be called by screens of the system
    - It updates the SimpleSavingsAccount Class's interest rate, and then updates the CheckingAccount interest rate
- The second is meant to only be called by CheckingAccount
    - It uses a passed interest rate from CheckingAccounts's setInterestRate function to update this Class's interest rate
- CheckingAccount has these functions as well; Both classes can update the other

#### OverdraftAccount
    public void setOverdraftForAccount(long overDraftAccountID)
    public CheckingAccount getOverDraftAccount()
    public void removeOverdraftAccount()

CheckingAccounts may have linked SimpleSavingsAccounts act as an overdraft account.

Set and Get
- Search the database for a valid CheckingAccount
- If one is found, it is set or returned

Remove
- Search the database for the linked CheckingAccount
- Remove this account from the overdraft account's overdraftAccount field
    - Both CheckingAccount and SimpleSavingsAccount have a field for their respective part of the overdraft relationship
        - CheckingAccount has a linked SimpleSavingsAccount
        - SimpleSavingsAccount has a linked CheckingAccount
    - Because both classes link to another account, they have to update each other

## CDSavingsAccount
CDs are long-term savings accounts that are meant to not be interacted with for a set period of time in exchange for a higher interest rate.


### Data
#### Class Data
None
#### Instance Data
- interestRate - The interest rate for this CD account
- dueDate - The date this CD has interest applied

### Functions
#### Constructors
    public CDSavingsAccount(String customerID, LocalDate accountCreationDate, double initialBalance, double interestRate, LocalDate dueDate)
    public CDSavingsAccount(String customerID, LocalDate accountCreationDate, long accountID, double balance, double interestRate, LocalDate dueDate)

Like AbstractAccount, there are 2 constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
    - The second shouldn't be called anywhere outside fromFileString() in this Class

#### Delete account
    public static void deleteAccount(CDSavingsAccount account)

Account deletion goes through a process of
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
- Write information for CDSavingsAccount
    - balance
    - interestRate
    - dueDate
    - isDeleted
- Return the string

Strings are then written to txts in the Database.
Go to DatabaseBasics to see details.

##### fromFileString()
    public static CheckingAccount fromFileString(String s)

This is basically the opposite of toFileString();
It reads a String and turns it into a CheckingAccount.
The second constructor is used in this function.

It follows the same structure as toFileString()
- Read information for AbstractAccount
- Read information for CDSavingsAccount
- Create a CDSavingsAccount object
- Return that object

These objects are added to arrayLists in the Database.
Go to DatabaseBasics to see details.

#### Non-getter/setter functions
##### Withdraw
    public void withdrawBeforeDueDate()
    public void withdrawAfterDueDate()
There are two withdraw functions
- One for withdrawing before the due date
  - This returns the original balance
- One for withdrawing after the due date
  - This returns the balance + interest

#### Rollover
    public void rollOver(double newRate, LocalDate newDueDate)

Accounts can be rolled over by the bank; setting a new interest rate and new due date