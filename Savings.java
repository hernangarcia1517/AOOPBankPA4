/**
 * @author Hernan Garcia
 * @version 1.0.0
 * @since September 28, 2020
 */
public class Savings extends Account{
    private double interestRate;

    Savings(){
        //Default
    }
    /**
     * This is the constructor of the savings account class
     * @param accountNumber the account number of the savings account
     * @param currentBalance the current balance of the savings account
     * @param interestRate the interest rate of the savings account
     */
    Savings(int accountNumber, double currentBalance, double interestRate){
        super(accountNumber, currentBalance);
        this.interestRate = interestRate;
    }
    /**
     * This method is to get the interest rate of the savings account
     * @return double, interest rate
     */
    public double getInterestRate(){
        return interestRate;
    }
}
