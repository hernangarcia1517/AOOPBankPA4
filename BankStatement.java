
/**
 * @author Hernan Garcia and Alyssandra Cordero
 * @version 1.1.0
 * @since November 9, 2020
 */
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BankStatement implements Printable {
    Customer customer;
    Checking checking;
    Savings savings;
    Credit credit;

    BankStatement() {
        // Default
    }

    /**
     * Constructor for the BankStatement class
     * 
     * @param customer customer object
     */
    BankStatement(Customer customer) {
        this.customer = customer;
        this.checking = customer.getCheckingAccount();
        this.savings = customer.getSavingsAccount();
        this.credit = customer.getCreditAccount();
    }

    /**
     * This method creates a bank statement for a customer
     * 
     * @param statementName takes in the name of a customer
     */
    public void createBankStatement(String statementName) {
        FileWriter writer = null;
        boolean hasChecking = false;
        boolean hasCredit = false;
        if (checking != null)
            hasChecking = true;
        if (credit != null)
            hasCredit = true;
        try {
            print();
            writer = new FileWriter(statementName + ".txt");
            writer.write("BANK STATEMENT FOR:");
            writer.write("\n");
            writer.write(customer.getName());
            writer.write("\n");
            writer.write("Address: " + customer.getAddress()[0] + "," + customer.getAddress()[1] + ","
                    + customer.getAddress()[2]);
            writer.write("\n");
            writer.write("Phone Number: " + customer.getPhoneNumber());
            writer.write("\n");
            writer.write("Current Account(s) Balance: ");
            writer.write("\n");
            if (checking != null) {
                printChecking();
                writer.write("Checking: " + checking.getCurrentBalance());
                writer.write("\n");
            }
            writer.write("Savings: " + savings.getCurrentBalance());
            writer.write("\n");
            if (credit != null) {
                printCredit();
                writer.write("Credit: " + credit.getCurrentBalance());
                writer.write("\n");
            }
            writer.write("Transaction Records:");
            writer.write("\n");
            if (hasChecking && hasCredit) {
                if (savings.getTransactions().size() == 0 && checking.getTransactions().size() == 0
                        && credit.getTransactions().size() == 0) {
                    writer.write("No transactions this session.");
                    writer.write("\n");
                }
            }
            if (hasChecking) {
                ArrayList<String> checkingTransactions = checking.getTransactions();
                for (int i = 0; i < checkingTransactions.size(); i++) {
                    writer.write(customer.getName() + " " + checkingTransactions.get(i));
                    writer.write("\n");
                }
            }
            ArrayList<String> savingsTransactions = savings.getTransactions();
            for (int i = 0; i < savingsTransactions.size(); i++) {
                writer.write(customer.getName() + " " + savingsTransactions.get(i));
                writer.write("\n");
            }

            if (hasCredit) {
                ArrayList<String> creditTransactions = credit.getTransactions();
                for (int i = 0; i < creditTransactions.size(); i++) {
                    writer.write(customer.getName() + " " + creditTransactions.get(i));
                    writer.write("\n");
                }
            }
            writer.write("Date of records: " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date())); // Could
                                                                                                                  // cause
                                                                                                                  // Issue
        } catch (IOException e) {
            System.out.println("Error" + e);
            System.out.println("Bank Statement Not Created.");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                    System.out.println("Bank Statement Created Successfully.");
                } catch (IOException e) {
                    System.out.println("Error" + e);
                }
            }
        }
    }

    /**
     * This method is to print prompts for the bank statement
     */
    @Override
    public void print() {
        System.out.println("Writing Bank Statement, please wait...");
    }

    /**
     * This method is to print prompts for a checking account in a bank statement
     */
    @Override
    public void printChecking() {
        System.out.println("Writing customer's checking account statement...");
    }

    /**
     * This method is to print prompts for a credit account in a bank statement
     */
    @Override
    public void printCredit() {
        System.out.println("Writing customer's credit account statement...");
    }
}