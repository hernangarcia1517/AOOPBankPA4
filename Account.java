import java.util.ArrayList;

/**
 * @author Hernan Garcia
 * @version 1.1.0
 * @since October 19, 2020
 */
public abstract class Account {
    private int accountNumber;
    private double currentBalance;
    private ArrayList<String> transactions;

    Account(){
        //Default
    }
    /**
     * This is the constructor
     * @param accountNumber the number of the account
     * @param currentBalance the current balance of th account
     */
    Account(int accountNumber, double currentBalance){
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        transactions = new ArrayList<String>();
    }
    /**
     * This is to return the account number
     * @return the account number
     */
    public int getAccountNumber(){
        return accountNumber;
    }
    /**
     * This is to return the current balance
     * @return the current balance
     */
    public double getCurrentBalance(){
        return currentBalance;
    }
    /**
     * This method prints the balance of the account
     */
    public void inquireBalance(){
        transactions.add("Made a Balance Inquiry");
        System.out.println("Current Balance: $" + currentBalance);
        System.out.println();
    }
    /**
     * This method is used in order for the account to recieve funds
     * @param incomingMoney this is the amount of fund coming in
     * @param caseAction this the action that it is being used in
     */
    public void receiveMoney(double incomingMoney, String caseAction){
        if(incomingMoney <= 0.0){
            System.out.println("ERROR: Invalid Amount to Receive");
            return;
        }
        currentBalance += incomingMoney;
        if(caseAction.equals("Deposit")){
            transactions.add("Deposited $" + incomingMoney);
            System.out.println("Deposit successful.");
        } else if(caseAction.equals("Sent")){
            transactions.add("Received $" + incomingMoney);
            System.out.println("Money received");
        } else if(caseAction.equals("Handler")){
            transactions.add("Transaction Reader transaction $" + incomingMoney);
            //NOT GOOD PRACTCE TO LEAVE THIS EMPTY:/
        }else{
            transactions.add("Transfered $" + incomingMoney);
            System.out.println("Money Transfered");
        }
    }
    /**
     * This method is used we money is coming out of the account.
     * @param sendingAmount this is the amount of funds going out
     * @param caseAction this is the action action that it is being used in
     */
    public void sendMoney(double sendingAmount, String caseAction){
        if(sendingAmount <= 0.0){
            System.out.println("ERROR: Invalid Amount to Send");
            return;
        } else if(sendingAmount > currentBalance){
            System.out.println("Insufficient Funds");
            return;
        }
        currentBalance -= sendingAmount;
        if(caseAction.equals("Withdrawal")){
            transactions.add("Withdrew $" + sendingAmount);
            System.out.println("Withdrawal successful.");
        } else if(caseAction.equals("Sent")) {
            transactions.add("Sent $" + sendingAmount);
            System.out.println("Money sent.");
        }
    }
    /**
     * This method returns array list of transactions
     * @return array list
     */
    public ArrayList<String> getTransactions(){
        return transactions;
    }
}
