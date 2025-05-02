# Customer
## General
This class represents customers within the system.
Connection to accounts is done through a list of accountIDs rather than storing objects.

## Data
### Class Data
None
### Instance Data
- SSN - The Social Security Number of the customer
  - This acts as customerID
- address - Street address
- city - City
- state - State
- zip - Zip code
- firstName - First name of the customer
- lastName - Last name (surname) of the customer
- customerID - The ID for the customer in the system
  - This is set as the SSN of the customer
  - This is the means by which the system identifies and uses Customers
- isDeleted - Whether this Customer has been deleted
  - Because Customer doesn't extend AbstractAccount, it needs its own isDeleted
- customerAccountIDs - An arrayList of the accountIDs for Accounts the Customer owns

### Functions
#### Constructors
    public Customer(String SSN, String address, String city, String state, String zip, String firstName, String lastName) {
    public Customer(String SSN, String address, String city, String state, String zip, String firstName, String lastName, ArrayList<Long> customerAccountIDsPassed)

Like AbstractAccount, Customers need two constructors
- The first is for the creation of a Customer
- The second is for creating a Customer when restoring from the Database
  - This should only be called by this Class's fromFileString()

#### Delete
    public static double deleteCustomer(Customer customer)

Deletion of a Customer is the most complex delete method in the system
- Unlike other "deletes" in the system, this delete returns a value
  - It returns the amount of money the Customer owes the bank OR the amount of money the bank owes the Customer
  - If the returned value is positive, the Customer owes the bank that much money
  - If the returned value is negative, the bank owes the Customer that much money (times -1)


- Initialize oweOrOwed, the variable which holds the amount of money the customer owes to the bank or vice versa
- Remove all the Accounts of the Customer from the system
  - For each Account the Customer owns
    - Get the balance of the account and adjust oweOrOwed
      - For Checking, CDSavings, and SimpleSavings Accounts, subtract the balance from oweOrOwed
      - For CC and ShortOrLong Loan Accounts, add the balance to oweOrOwed
    - Delete the Account by calling the Class delete method for that Account
      - Ex: For a CheckingAccount, call CheckingAccount's delete method with this CheckingAccount
- Don't delete the Customer from the Database
  - A real bank would keep Customer information in case they came back
  - We don't have functionality for restoring Customers, but we want to keep Customer information still
- set isDeleted() to true for the Customer
  - This removes some functionality for the Customer
  - Getters for basic Customer information such as address and name can still work
  - It is not truly deleted from the system
- Return oweOrOwed

#### toFileString() and fromFileString()
Like AbstractAccount, this is used by the system to interact with the Database

##### toFileString()
    public String toFileString()
Like all others, writes a colon delimited String and returns it
- address
- city
- state
- zip
- firstName
- lastName
- customerID
- isDeleted
- The list of customer accountIDs
- Return the String

##### fromFileString()
    public static Customer fromFileString(String s)

Same as toFileString(), except it returns a Customer object
- This uses the second constructor

#### Customer Account List Functions
    public void addAccountToCustomerAccounts(long accountID)
    public void removeAccountFromCustomerAccounts(long accountID)
    public ArrayList<Long> getCustomerAccountIDs()
    public ArrayList<AbstractAccount> getCustomerAccountsAsAccounts()

There is basic functionality for manipulating and viewing the list of Accounts of a Customer
- add and remove
  - Basic, take the accountID for an account
  - These do not verify that they are valid accountIDs, that is up to the function caller
- Get accountIDs or Accounts
  - getCustomerAccountIDs - Returns the arrayList of accountIDs
  - getCustomerAccountsAsAccounts - Returns actual Account objects
    - This list is type AbstractAccount because all Accounts extend it