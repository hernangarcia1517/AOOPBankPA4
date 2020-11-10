/**
 * @author Hernan Garcia and Alyssandra Cordero
 * @version 1.2.0
 * @since November 9, 2020
 */

public class Customer extends Person implements Printable{
    private String phoneNumber;
    private String password;
    private String email;
    private String[] dateOfBirth;
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
    Customer(String firstName, String lastName, String customerID, String password, String email, boolean hasChecking, boolean hasSavings, String[] dateOfBirth, String phoneNumber, String[] address, Checking checkingAccount, Savings savingsAccount, Credit creditAccount){
        super(firstName, lastName, customerID, hasChecking, hasSavings);
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.address = address;
        this.checkingAccount = checkingAccount;
        this.savingAccount = savingsAccount;
        this.creditAccount = creditAccount;
    }
    /**
     * Thismethod is to get the customer's phone number
     * @return String, the phone number
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }
    /**
     * This methos is to get the date of birth
     * @return string array consisting of month day, year
     */
    public String[] getDateOfBirth(){
        return dateOfBirth;
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

    /**
     * This method is to print savings acccount balance
     */
    @Override
    public void print(){
        System.out.println(super.getName() + "'s savings account:");
        System.out.println("Savings account balance: " + savingAccount.getCurrentBalance());
    }

    /**
     * This method is to print checking acccount balance
     */
    @Override
    public void printChecking(){
        if(checkingAccount != null){
            System.out.println(super.getName() + "'s checking account:");
            System.out.println("Checking account balance: " + checkingAccount.getCurrentBalance());
        }else{
            System.out.println(super.getName() + " is not associated with a checking account.");
        }
    }

    /**
     * This method is to print credit acccount balance
     */
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
    /**
     * this method is to get the reference of the password
     * @return password
     */
	public String getPassword() {
		return password;
    }
    /**
     * this method is to get the reference of the email account
     * @return email account
     */
	public String getEmail() {
		return email;
	}
}
