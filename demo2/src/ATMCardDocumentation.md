# ATMCard

## General
Customers are able to have CheckingAccounts and SimpleSavingsAccounts linked to ATMCards.
ATMCards are very simple; they end up calling the withdraw() method from the Account the card is linked to.
They also have a limit of two withdrawals a day.

## Data
### Class Data
None
### Instance Data
- numOfWithdrawalsToday - The number of withdrawals today, 2 is the max per day
- customerID - ID of the Customer who owns this card 
- accountID - The accountID of the Account this is linked to
    - CheckingAccount or SimpleSavingsAccount
- forChecking - Whether this account is for a CheckingAccount; if false, it is for a SimpleSavingsAccount
- isDeleted - Whether this ATMCard has been deleted
  - Because this class does not extend AbstractAccount, it needs its own isDeleted declaration

## Functions
### Constructors
    public ATMCard(String customerID, long accountID)
    public ATMCard(String customerID, long accountID, boolean forChecking, int numOfWithdrawalsToday)

Similar to Accounts, there are two constructors for ATMCards
- The first is when an ATMCard is initially created
  - forChecking is set in here, so it isn't an argument
- The second is when an ATMCard is being restored from the database
  - Like Accounts, this should only be called from this Class's fromFileString()

### Delete
    public static void deleteATMCard(ATMCard card)

Simpler than Account deletion
- Find the Account this ATMCard is linked to
  - Remove the connection to this ATMCard from that Account
- Remove the ATMCard from the Database
- Set isDeleted to true, garbage collection will clean it up later

### Withdraw
    public void withdraw(int amount)

Basic withdrawing
- First check if numOfWithdrawalsToday is at 2 or more
- If not, increment numOfWithdrawalsToday
  - Then, call the withdrawal method on the account this ATMCard is linked to

### toFileString() and fromFileString()
Same as Accounts, this is how the system uses the Database.
Note that this does not extend the AbstractAccount class; these are manually created functions.

#### toFileString()
    public String toFileString()

Writes to a colon delimited String and returns it.
- customerID
- type of account
  - Checking or Savings
- accountID
- numOfWithdrawalsToday
- isDeleted
- Return the String

#### fromFileString()
    static public ATMCard fromFileString(String s)

Reads from a String and creates an ATMCard object from it.

The same order as toFileString()
- customerID
- type of account
    - Checking or Savings
- accountID
- numOfWithdrawalsToday
- isDeleted
- Returns the ATMCard object