/**
 * @author Hernan Garcia
 * @version 1.0.0  
 * @since September 28, 2020
 */
public class Checking extends Account{
    private double interestRate;

    Checking(){
        //Default Constructor
    }
    /**
     * This is the constructor for checking account
     * @param accountNumber the account number
     * @param currentBalance the current balance
     * @param interestRate the interest rate
     */
    Checking(int accountNumber, double currentBalance, double interestRate){
        super(accountNumber, currentBalance);
        this.interestRate = interestRate;
    }
    /**
     * This method is to get the interest rate of the checking account
     * @return double, interest rate
     */
    public double getInterestRate(){
        return interestRate;
    }
}