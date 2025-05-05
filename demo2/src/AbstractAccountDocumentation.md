# Abstract Account

## General
AbstractAccount is the top level class for all Account classes.
If you see a mention to an 'Account' in the documentation, it refers to this class OR one of its child classes.

The Class child hierarchy is listed below
- AbstractAccount
  - CheckingAccount
  - SavingsAccount
    - SimpleSavingsAccount
    - CDSavingsAccount
  - LoanAccount
    - ShortOrLong
    - CC (Credit Card)

## Data
Instance Data
- customerID - The ID of the Customer this account is for
- accountCreationDate - The date this account was created
- accountType - The type of account this is, this is an enum described later
- accountID - This account's unique identifier in the system
  - This is the way accounts are identified and used throughout the system
- isDeleted - A boolean for whether this account has been deleted
  - This is necessary because Java does not allow for direct object deletion
  - If set, this account is effectively gone from the system; however, we must wait for the garbage collector to actually get rid of it
  - This means it may sometimes look like it is there, but it isn't actually usable
  - **NOTE**
    - isDeleted is not mentioned in child class documentation, but it should still be assumed to exist

Class Data
- accountIDCounter - This is incremented to assign new accounts unique IDs
- AccountType
  - An enum that holds the types of accounts in the system
  - CheckingAccount
  - SavingsAccount
    - SimpleSavingsAccount
    - CDSavingsAccount
  - LoanAccount
    - ShortOrLongLoanAccount
    - CCLoanAccount
  - Each of the Accounts above are in the Enum, indentation is meant to show children classes

## Functions
### Constructors
There are two constructors for AbstractAccount
- One is for when an account is being newly created
- One is for when an account is being restored from the database
- **This pattern is continued in all child classes**


    public AbstractAccount(String customerID, LocalDate accountCreationDate, AccountType accountType)
    public AbstractAccount(String customerID, LocalDate accountCreationDate, AccountType accountType, long accountID)

Note AccountID is not listed when being newly created but is when being restored.
Since Accounts are linked by their IDs, the same IDs must persist when being restored.

### toFileString() and FromFileString()
The system uses these functions to read and write to and from the database.

#### toFileString
This is an Abstract method that all child classes overwrite with their own method.
Generally, it creates a String delimited by a colon.
Look at a child class for an example of what this looks like.

#### fromFileString
fromFileString is a static method that must be written in all child classes.
However, because it is meant to be a static method, it cannot be easily inherited down to child classes.
What this means is that the system will not show it missing in classes, **it must be manually written in all children classes**.
Generally, it reads from a colon delimited list. Once again, look at a child class to see what this looks like.