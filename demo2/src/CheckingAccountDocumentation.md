# Checking Account
## General
CheckingAccount is a child of AbstractAccount.
It has no child classes.

CheckingAccounts have their own AccountType for whether they are a GoldDiamond account or a TMB account.
The key differences between the types are
- TMB accounts have fees for depositing, withdrawing, and transferring
- GoldDiamond accounts have an interest rate, which is half of SavingsAccounts interest rate

CheckingAccounts can have an overdraft account by being linked to a SimpleSavingsAccount. 

## Data
### Class Data
- minimumBalanceGoldDiamond - The minimum balance an account must have to be a GoldDiamond account
- interestRate - The interest rate for GoldDiamond accounts

### Instance Data
- balance - The balance of the account
- overdraftsThisMonth - The number of overdrafts this account has had this month
- accountSpecificType - The AccountType for CheckingAccount; GoldDiamond or TMB
- stopPaymentArray - An arrayList of check numbers to stop payments on
- checkMap - A map of checks; the key is the check number and the value the amount
  - Amounts are positive for deposits and negative for withdrawals
- linkedToATMCard - Whether this account is linked to an ATMCard
  - The ATMCard is not tracked, simply whether it is linked
- overdraftAccountID - CheckingAccounts can be linked to a SimpleSavingsAccount for overdraft protection; this is the accountID of that account

## Functions
### Constructors

    public CheckingAccount(String customerID, LocalDate accountCreationDate, double initialBalance)
    public CheckingAccount(String customerID, LocalDate accountCreationDate, double balance, int overdraftsThisMonth, long accountID, long overdraftAccountID, boolean linkedToATMCard, HashMap<String, Double> checkMapPassed, ArrayList<String> stopPaymentArrayPassed)


Like AbstractAccount, there are 2 constructors
- The first is for when an account is being newly created
- The second is for when an account is being restored from the database
  - The second shouldn't be called anywhere outside fromFileString() in this Class

### Delete Account
    public static void deleteAccount(CheckingAccount account)
Account deletion goes through a process of
- Removing the linkage to an overdraft account if a link exists
- Removing the Account from a Customer's list of accounts
- Deleting an ATMCard linked to this account if one exists
- Removing the Account from the database
- And finally setting the isDeleted flag for garbage collection

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
  - overdraftsThisMonth
  - AccountType (CheckingAccount)
  - Overdraft accountID (If one is linked, otherwise -1)
  - isLinkedToATMCard (This is a boolean)
  - The check map
  - The stop payment arrayList
- Return the string

Strings are then written to txts in the Database.
Go to DatabaseBasics to see details.

#### fromFileString()
    public static CheckingAccount fromFileString(String s)

This is basically the opposite of toFileString();
It reads a String and turns it into a CheckingAccount.
The second constructor is used in this function.

It follows the same structure as toFileString()
- Read information for AbstractAccount
- Read information for CheckingAccount
- Create a CheckingAccount object
- Return that object

These objects are added to arrayLists in the Database.
Go to DatabaseBasics to see details.

### Depositing, withdrawing, and transferring

#### Deposit
    public void deposit(double amount) 

Deposit is pretty basic
- It checks if the account is deleted
- It checks if the amount is less than or equal to 0.0
- If both of these pass, the money is actually deposited
  - If the account is a TMB before the money goes in, a deposit fee is used
- Once the deposit is finished, the CheckingAccount.AccountType is updated
  - If it is at or above the minimum threshold for GoldDiamond accounts, it is made one
  - Otherwise, it is made a TMB account

#### Withdraw
    public void withdraw(double amount)

Withdraw is more complex because it also checks for an overdraft account
- It checks if the account is deleted
- It checks if the amount is less than or equal to 0.0
- If both of the checks pass, the withdrawal process starts
- First check to see if the account has enough funds to cover the amount
  - If it does, do a simple withdrawal
  - If it doesn't, start checking for an overdraft account
- If an overdraft account doesn't exist, skip the overdraft process
- If an overdraft account does exist (is linked to)
  - Get that overdraft account from the Database
  - See if the balance between both the Checking and overdraft account can cover the amount
    - If not, exit the overdraft process
    - Charge a $25 overdraft fee
  - If so
    - Drain the Checking account balance as much as possible
    - Take the rest from the overdraft account
      - This process is a little more complicated in the code
- Once the withdrawal is finished, the CheckingAccount.AccountType is updated
  - If it is at or above the minimum threshold for GoldDiamond accounts, it is made one
  - Otherwise, it is made a TMB account

#### Transfer
    public void transfer(double amount, long accountToTransferToID)
Transfer is the most complicated because it has to check for different Account types from AbstractAccount.
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

### Other non-basic-setter/getter functions
#### getTransactionFee()
    public double getTransactionFee(boolean isTransfer)
TMB accounts have deposit, withdrawal, and transfer fees
- This function returns that fee, depending on the transaction type
- Deposit and withdrawal have the same fee

#### calcAndAddInterest
    public void calcAndAddInterest()
GoldDiamond accounts have a small amount of interest; this function calculates and adds that interest to the balance of them

#### setInterestRate()
    public static void setInterestRate(double interestRatePassed)
    public static void setInterestRateBySavingsAccount (double interestRatePassed)
There are two functions for setting interest rates because Checking and SimpleSavings accounts have a linked interest rate.
Specifically, CheckingAccount has half the interest of SimpleSavingsAccount.
- What this means is that when one is updated, the other Class needs to also be updated

This leads to two functions
- The first is meant to be called by screens of the system
  - It updates the CheckingAccount Class's interest rate, and then updates the SimpleSavingsAccount interest rate
- The second is meant to only be called by SimpleSavingsAccount
  - It uses a passed interest rate from SimpleSavingsAccount's setInterestRate function to update this Class's interest rate
- SimpleSavingsAccount has these functions as well; Both classes can update the other

#### OverdraftAccount
    public void setOverdraftForAccount(long overDraftAccountID)
    public SavingsAccount.SimpleSavingsAccount getOverDraftAccount()
    public void removeOverdraftAccount()

CheckingAccounts may have linked SimpleSavingsAccounts act as an overdraft account.

Set and Get
- Search the database for a valid SimpleSavingsAccount
- If one is found, it is set or returned

Remove
- Search the database for the linked SimpleSavingsAccount
- Remove this account from the overdraft account's overdraftAccount field
  - Both CheckingAccount and SimpleSavingsAccount have a field for their respective part of the overdraft relationship
    - CheckingAccount has a linked SimpleSavingsAccount
    - SimpleSavingsAccount has a linked CheckingAccount
  - Because both classes link to another account, they have to update each other