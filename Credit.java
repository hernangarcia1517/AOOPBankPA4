/**
 * @author Hernan Garcia
 * @version 1.1.0
 * @since October 19, 2020
 */
public class Credit extends Account {
    private double interestRate;
    private double creditLine;

    Credit() {
        // Default
    }

    /**
     * This is the constructor of the credit account class
     * 
     * @param accountNumber  the account number
     * @param currentBalance the current balance of the credit account
     * @param interestRate   the interest rate of the credit account
     */
    Credit(int accountNumber, double creditLine, double outstandingBalance, double interestRate) {
        super(accountNumber, outstandingBalance);
        this.creditLine = creditLine;
        this.interestRate = interestRate;
    }

    /**
     * This method is to get the interest rate of the credit account
     * 
     * @return double, interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    public double getOutstandingBalance() {
        return super.getCurrentBalance();
    }

    /**
     * This method is to return the credit line
     * 
     * @return creditLine attribute
     */
    public double getCreditLimit() {
        return creditLine;
    }

    /**
     * This method is to check is the incoming funds do not exceed the amount owed
     * 
     * @param receivingBalance these are the incoming funds
     * @return if incoming funds do not exced mount owed we are returning a boolean
     */
    public boolean canReceive(double receivingBalance) {
        if (receivingBalance > Math.abs(super.getCurrentBalance()))
            return false;
        else
            return true;
    }

    /**
     * This method is to make sure that the user can withdraw a certain amount
     * 
     * @param withdrawalAmount the amount to withdraw
     * @return true if withdrawal is possible
     */
    public boolean canWithdraw(double withdrawalAmount) {
        double potentialAmountOwed = withdrawalAmount + Math.abs(super.getCurrentBalance());
        if (potentialAmountOwed < creditLine)
            return true;

        return false;
    }
}
