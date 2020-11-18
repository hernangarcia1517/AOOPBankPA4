import java.util.*;
import java.io.*;
public class LogFile {
    Customer customer;
    Checking checking;
    Savings savings;
    Credit credit;

    LogFile(){
        //Default
    }
    /**
     * Constructor for the LogFile class
     * @param customer customer object
     */
    LogFile(Customer customer){
        this.customer = customer;
        this.checking = customer.getCheckingAccount();
        this.savings = customer.getSavingsAccount();
        this.credit = customer.getCreditAccount();
    }

    public void createLogFile(String LogFileName){
        FileWriter writer = null;
        boolean hasChecking = false;
        boolean hasCredit = false;
        if(checking != null) hasChecking = true;
        if(credit != null) hasCredit = true;
        try{
            System.out.println("Writing log file, please wait...");
            writer= new FileWriter(LogFileName+"Log.txt");
            writer.write("LOG FILE FOR:");
            writer.write("\n");
            writer.write(customer.getName());
            writer.write("\n");

            writer.write("Transaction Records:");
            writer.write("\n");
            if(hasChecking && hasCredit){
                if(savings.getTransactions().size() == 0 && checking.getTransactions().size() == 0 && credit.getTransactions().size() == 0){
                    writer.write("No transactions this session.");
                    writer.write("\n");
                }
            }
            if(hasChecking){
                ArrayList<String> checkingTransactions = customer.getCheckingAccount().getTransactions();
                for(int i = 0; i < checkingTransactions.size(); i++){
                    writer.write(customer.getName() + " " + checkingTransactions.get(i));
                    writer.write("\n");
                }
            }
            ArrayList<String> savingsTransactions = customer.getSavingsAccount().getTransactions();
            for(int i = 0; i < savingsTransactions.size(); i++){
                writer.write(customer.getName() + " " +savingsTransactions.get(i));
                writer.write("\n");
            }

            if(hasCredit){
                ArrayList<String> creditTransactions = customer.getCreditAccount().getTransactions();
                for(int i = 0; i < creditTransactions.size(); i++){
                    writer.write(customer.getName() + " " + creditTransactions.get(i));
                    writer.write("\n");
                }
            }
        }catch(IOException e){
            System.out.println("Error" + e);
            System.out.println("Log File Not Created.");
        }finally{
            if(writer != null)
            {
                try{
                    writer.close();
                    System.out.println("Log File Created Successfully.");
                }catch(IOException e){
                    System.out.println("Error" + e);
                }
            }
        }
    }
}