import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(MainMenu::new);
        ArrayList<Customer> customers = new ArrayList<>();



        /*
        Manually inputting customers into the database will eventually be changed to use the java swing screens.
        For now this is just for testing
         */
        Customer customer1 = new Customer("423453245114", "North 4th", "Clarksdale", "MO", "64493", "Ronald", "Jones");
        Customer customer2 = new Customer("3456534251805","Jules", "St.Joseph", "MO", "64503","Mark", "Ingram");
        Customer customer3 = new Customer("4231488942109","Sylviana","St.Joseph","MO","64501","Sherry","Harrison");
        Customer customer4 = new Customer("6778790134305","West 3rd","Wathena","KS","54910","John","Morgeson");
        Customer customer5 = new Customer("345599870221","South Highly","St.Joseph","MO","64503","Gary","Gilkerson");
        Customer customer6 = new Customer("423567890355","Jules","St.Joseph","MO","64503","Mary","Francis");
        Customer customer7 = new Customer("3458123321402","Mocking Bird St.","St.Joseph","MO","64502","Carolyn","Johnson");
        Customer customer8 = new Customer("3458799122105","Senior Dr.", "St.Joseph", "MO", "64503","Larry","Waite");
        Customer customer9 = new Customer("4231689451801","Lovers Ln","St.Joseph","MO","64502","Marilyn","Macklin");
        Customer customer10 = new Customer("2354457892508","Stonecrest","St.Joseph","MO","64501","Lana","McGlynn");
        Customer customer11 = new Customer("4367892134156","N. Mulbury","St.Joseph","MO","64502","John","Munzer");
        Customer customer12 = new Customer("2334356561525","Kioa","Elwood","KS","56708","Mary","White");
        Customer customer13 = new Customer("435678787902","Martha Ln","Gower","MO","63301","Jerry","Gronius");
        Customer customer14 = new Customer("5634329131532","Lovers Ln","St.Joseph","MO","64502","Martin","Williams");
        Customer customer15 = new Customer("345679898703","Park Ln","St.Joseph","MO","64501","Broderick","Jones");
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);
        customers.add(customer6);
        customers.add(customer7);
        customers.add(customer8);
        customers.add(customer9);
        customers.add(customer10);
        customers.add(customer11);
        customers.add(customer12);
        customers.add(customer13);
        customers.add(customer14);
        customers.add(customer15);


        //Assigning ATMCards for checking account customers
        int atmCardCustomer1 = 1;
        //Creation of customers checking account
        CheckingAccount checkingAccount1 = new CheckingAccount(customer1.getCustomerID(), new Date(), "Checking", 50.00, CheckingAccount.AccountType.TMB, customer1, atmCardCustomer1);
        //Add account to customers portfolio
        customer1.addAccountToCustomerAccounts(checkingAccount1);

        //This is for savings accounts
        int atmCardCustomer2 = 1;
        SavingsAccount savingsAccount1 = new SavingsAccount.SimpleSavingsAccount(customer2.getCustomerID(), new Date(), 3000, customer2,1);
        customer2.addAccountToCustomerAccounts(savingsAccount1);

        //Print customers in the database with all their information and then print out their accounts
        for(Customer customer : customers){
            System.out.println(customer);

            for (AbstractAccount account : customer.getCustomerAccounts()){
                System.out.println(account);
            }
        }


    }
}