# Database Basics
This aims to briefly describe how the database system works

## Storage
### In System
ArrayLists are used to store all account types, customers, and ATM cards.

    static ArrayList<CheckingAccount> checkingAccountList = new ArrayList<>();
### Outside System
.txt files are used to store data, delimited using ;

Each arrayList has an associated .txt

    static String checkingAccounts = "checkingAccounts.txt";
## Retrieving from the Database
The outside function for this is restoreFromDatabase()

There is a lock associated with it, so data cannot be altered while reading information from the database

restoreFromDatabase() calls a helper function, loadFromFile() which does the actual loading 

### loadFromFile()

This function uses a buffered reader to read from the given .txt file

This function requires that **every** class it reads from implements the ***fromFileString()*** function. This function accepts a String argument. It returns an object of the type of the passed in arrayList.

The String object to be passed is "line", which comes from the buffered reader and correlates to a line within the file. This is handled by the function however, so no user input is needed.

This function will run and add objects into the arrayList until the file runs out of lines.

Parameters for loadFromFile()

    fileName - The .txt associated with an arrayList

    list - The arrayList we wish to get data from

## Storing to the Database
*This process is very similar to reading from the database*

The outside function for this is storeToDatabase()

This also uses the lock mentioned before to prevent data corruption

storeToDatabase() also uses a helper function: storeToFile()

### storeToFile()

This function uses a print writer with a file writer to write to a file

This function requires that *every* class it reads from implements the ***toFileString()*** function. This function accepts no parameters, and is called on objects of the type of the passed in arrayList. It returns a delimited String, using ; as the delimiter.

The object called on comes from the arrayList, so once again you don't do anything but pass parameters.

This function will run for every item in the list and write to the file.

Parameters for storeToFile()

Exact same as loadFromFile()

## Using Invoke
The .invoke() method call is crucial to how these functions work.

While a deep understanding of it isn't necessary a brief one will help. You can also look in the official documentation for an in-depth description.

Both of the helper functions grab a method from the class of the objects within the arrayList; either being loadFromFile() or storeToFile()

This method is then "invoked" with the syntax:

    method.invoke(lineOrObject, null);

Invoke calls the given method, a Method, on the first parameter inside of it. The second parameter doesn't do anything, it simply serves to get rid of a warning.

If you want a better understanding of it, look at invoke()'s documentation

Example walk through: loadFromFile()

    Method fromFileString: Grabs the method with the samme name from the class of the arrayList
    T temp: A temporary object of the type from the arrayList, T
    invoke: Effectivley calls line.loadFromFile(), returning an object of type T
    The object is added to the arrayList
    The loop continues until completion


## Other Functions
### Adding and Removing accounts
There are functions for adding and removing items to/from a given list.
An item can be AbstractAccount, a child of Abstract Account, Customer, or ATMCard.
The list is the associated arrayList with the given item.

    public static <T>void addItemToList(ArrayList<T> list, T item)
    public static <T>void removeItemFromList(ArrayList<T> list, T item)

### Get specific account
Getting a specific account from the database is done through the function getAccountFromList.
It needs the accountID for that account, and the list to search through.

    public static <T extends AbstractAccount> AbstractAccount getAccountFromList(ArrayList<T> accountList, long accountID)


### Getting individual Customers and ATMCards

    public static Customer getCustomerFromList(String customerID)
    public static ATMCard getATMCardFromList(long accountID)

customerID is from the Customer class. ATMCards are linked to Accounts, accountID is the accountID for that account.

### Get all accounts of a customer

    public static ArrayList<AbstractAccount> getAllAccountsOfCustomer(ArrayList<Long> customerAccountIDs)

Returns all the Account objects a Customer holds. The ArrayList is meant to come from the Customer's customerAccountID arrayList from the Customer class. 