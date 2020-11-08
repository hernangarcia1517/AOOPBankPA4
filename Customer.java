/**
 * @author Hernan Garcia
 * @version 1.1.0
 * @since October 19, 2020
 */
public class Customer extends Person implements Printable{
    private String dateOfBirth;
    private String phoneNumber;
    private String[] address;
    private Checking checkingAccount;
    private Savings savingAccount;
    private Credit creditAccount;

    Customer(){
        //Default
    }
    /**
     * This is te constructor of customer
     * @param firstName firstname of customer
     * @param lastName last name of customer
     * @param customerID the customer ID
     * @param hasChecking whether the customer has a checking account
     * @param hasSavings whether the customer has a savings account
     * @param dateOfBirth date of birth of the customer
     * @param phoneNumber customer's phone number
     * @param address customer's address
     * @param checkingAccount Customer reference to checking account
     * @param savingsAccount Customer reference to savings account
     * @param creditAccount customer reference to credit account
     */
    Customer(String firstName, String lastName, String customerID, boolean hasChecking, boolean hasSavings, String dateOfBirth, String phoneNumber, String[] address, Checking checkingAccount, Savings savingsAccount, Credit creditAccount){
        super(firstName, lastName, customerID, hasChecking, hasSavings);
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.checkingAccount = checkingAccount;
        this.savingAccount = savingsAccount;
        this.creditAccount = creditAccount;
    }
    /**
     * This methos is to get the date of birth
     * @return string array consisting of month day, year
     */
    public String getDateOfBirth(){
        return dateOfBirth;
    }
    /**
     * Thismethod is to get the customer's phone number
     * @return String, the phone number
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }
    /**
     * This method is to get the customer's address
     * @return String array consisting of home address, city, state
     */
    public String[] getAddress(){
        return address;
    }
    /**
     * this method is to get the reference of the checking account
     * @return checking account
     */
    public Checking getCheckingAccount(){
        return checkingAccount;
    }
    /**
     * this method is to get the reference of the savings account
     * @return savings account
     */
    public Savings getSavingsAccount(){
        return savingAccount;
    }
    /**
     * this method is to get the reference of the credit account
     * @return credit account
     */
    public Credit getCreditAccount(){
        return creditAccount;
    }

    @Override
    public void print(){
        System.out.println(super.getName() + "'s savings account:");
        System.out.println("Savings account balance: " + savingAccount.getCurrentBalance());
    }

    @Override
    public void printChecking(){
        if(checkingAccount != null){
            System.out.println(super.getName() + "'s checking account:");
            System.out.println("Checking account balance: " + checkingAccount.getCurrentBalance());
        }else{
            System.out.println(super.getName() + " is not associated with a checking account.");
        }
    }
    
    @Override
    public void printCredit(){
        if(creditAccount != null){
            System.out.println(super.getName() + "'s credit account:");
            System.out.println("Credit Limit:" + creditAccount.getCreditLimit());
            System.out.println("Outstanding Balance: " + creditAccount.getCurrentBalance());
        }else{
            System.out.println(super.getName() + " is not associated with a credit account.");
        }
    }
}
