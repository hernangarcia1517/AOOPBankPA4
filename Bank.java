/**
 * @author Hernan Garcia
 * @version 1.1.0
 * @since October 19, 2020
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Bank {
    static int customerIdTracker = 0;
    static int checkingAccountIdTracker = 0;
    static int savingsAccountIdTracker = 0;
    static int creditAccountIdTracker = 0;
    /**
     * Main method
     * @param args arguments during run
     */
    public static void main(String[] args) {
        BufferedReader br = null;
        String currentUser = "";
        HashMap<String, Customer> data = new HashMap<String, Customer>();//20, 0.75f);
        HashMap<String, String> customerIds = new HashMap<String, String>();
        BufferedReader inputReader = null;
        Writer transactionLog = null;
        readCVSFile(data, customerIds, br, currentUser); //Read the CVS File
        runBankingApp(data, customerIds, inputReader, transactionLog); //Run the Banking Process
    } //END OF main()
    /**
     * This method read froma CSV File
     * @param data the hashtable in which we are storing datat
     * @param br buffered reader
     * @param currentUser the current user string
     */
    public static void readCVSFile(HashMap<String, Customer> data, HashMap<String, String>customerIds, BufferedReader br, String currentUser){
        try{
            br = new BufferedReader(new FileReader("CS 3331 - Bank Users 3(3).csv")); //Hardcoded File Give prompt instead
            int count = 0;
            HashMap<String, Integer> inputCorrector = new HashMap<String, Integer>();
            while((currentUser = br.readLine()) != null){
                if(count == 0){
                    String[] headers = currentUser.split(",");
                    boolean seenAddress = false;
                    for(int i = 0; i < headers.length; i++){
                        if(seenAddress) inputCorrector.put(headers[i], i + 2);
                        else inputCorrector.put(headers[i], i);
                        if(headers[i].equals("Address")) seenAddress = true;
                        //System.out.println(headers[i] +" "+ inputCorrector.get(headers[i]));
                    }
                }
                if(count > 0){ //Omitting the first row, which should just be the headers
                    String[] VALS = currentUser.split(","); //This is going to create a String array, now we have to store it
                    //System.out.println(inputCorrector.get("Date of Birth"));
                    String dateOfBirth = VALS[inputCorrector.get("Date of Birth")];
                    //System.out.println(inputCorrector.get("Address"));
                    String[] addressArray = {VALS[inputCorrector.get("Address")], VALS[inputCorrector.get("Address") + 1], VALS[inputCorrector.get("Address") + 2]};
                    data.put(
                        VALS[inputCorrector.get("Identification Number")], 
                        new Customer(
                            VALS[inputCorrector.get("First Name")], //First name
                            VALS[inputCorrector.get("Last Name")], //Last name
                            VALS[inputCorrector.get("Identification Number")], //Customer ID
                            true, //Has Checking
                            true, //Has Savings
                            dateOfBirth, //Date of birth
                            VALS[inputCorrector.get("Phone Number")], //Phone number
                            addressArray, //Address
                            createCheckingAccount(Integer.parseInt(VALS[inputCorrector.get("Checking Account Number")]), Double.parseDouble(VALS[inputCorrector.get("Checking Starting Balance")]), 0.0), //Checking
                            createSavingsAccount(Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]), Double.parseDouble(VALS[inputCorrector.get("Savings Starting Balance")]), 0.0), //Savings
                            createCreditAccount(Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]), Double.parseDouble(VALS[inputCorrector.get("Credit Max")]), Double.parseDouble(VALS[inputCorrector.get("Credit Starting Balance")]), 0.0) //Credit
                        )
                    );
                    customerIds.put(data.get(VALS[0]).getName(), VALS[0]);
                    if(Integer.parseInt(VALS[0]) > customerIdTracker) customerIdTracker = Integer.parseInt(VALS[0]);
                    if(Integer.parseInt(VALS[4]) > checkingAccountIdTracker) checkingAccountIdTracker = Integer.parseInt(VALS[4]);
                    if(Integer.parseInt(VALS[1]) > savingsAccountIdTracker) savingsAccountIdTracker = Integer.parseInt(VALS[1]);
                    if(Integer.parseInt(VALS[5]) > creditAccountIdTracker) creditAccountIdTracker = Integer.parseInt(VALS[5]);
                }
                count++;
            }
        } catch(FileNotFoundException e){ //Handle File not found
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally{ //Closing BufferedReader if it is not null
            if(br != null){
                try{
                    br.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    } //END OF readCVSFile()
    /**
     * The run banking app handles all the banking functionality
     * @param data the data hashmap
     * @param inputReader buffered reader that handles user input
     * @param transactionLog logs user transaction
     */
    public static void runBankingApp(HashMap<String, Customer> data, HashMap<String, String> customerIds, BufferedReader inputReader, Writer transactionLog){
        try{
            inputReader = new BufferedReader(new InputStreamReader(System.in));
            transactionLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("transactionLog.txt", true), "utf-8")); //Appending the file since we might ant to keep track of all transactions
            boolean isDone = false;
            while(!isDone){
                System.out.println("Enter Create Customer, Returning Customer or Bank Manager:");
                System.out.println("1. Create Customer 2. Returning Customer 3. Bank Manager 4. Transaction Reader 5. Exit");
                String desiredFunctionality = inputReader.readLine();
                switch(Integer.parseInt(desiredFunctionality)){
                    case 1: //Create Customer
                        runCreateCustomer(data, inputReader, transactionLog);
                        break;
                    case 2: //Customer
                        runCustomer(data, inputReader, transactionLog);
                        break;
                    case 3: //Bank Manager
                        runBankManager(data, customerIds, inputReader, transactionLog);
                        break;
                    case 4:
                        runTransactionReader(data, customerIds, transactionLog);
                    case 5:
                        isDone = !isDone;
                        System.out.println("Ending Application");
                        break;
                    default:
                        System.out.println("Input not recognized. Please try again.");
                        break;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{ //Closing BufferedReader and Writer
            try{
                if(inputReader != null){
                    inputReader.close();
                }
                if(transactionLog != null){
                    transactionLog.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    } //END OF runBankingApp()
    /**
     * This method handles the creation of a checking account for a customer
     * @param accountNumber the account number
     * @param currentBalance the opening balance
     * @param interestRate the interest rate
     * @return returning the checking account
     */
    public static Checking createCheckingAccount(int accountNumber, double currentBalance, double interestRate){
        return new Checking(accountNumber, currentBalance, interestRate);
    }//END OF createCheckingAccount()
    /**
     * This method handles the creation of a savings account for a customer
     * @param accountNumber the account number
     * @param currentBalance the opening balance
     * @param interestRate the interest rate
     * @return returning the savings account
     */
    public static Savings createSavingsAccount(int accountNumber, double currentBalance, double interestRate){
        return new Savings(accountNumber, currentBalance, interestRate);
    }//END OF createSavingsAccount()
    /**
     * This method handles the creation of a credit account for a customer
     * @param accountNumber the account number
     * @param currentBalance the opening balance
     * @param interestRate the interest rate
     * @return returning the credit account
     */
    public static Credit createCreditAccount(int accountNumber, double creditLine ,double outstandingBalance, double interestRate){
        return new Credit(accountNumber, creditLine, outstandingBalance, interestRate);
    }//END OF createCreditAccount()
    /**
     * This method runs the create new customer logic
     * @param data the hashmap of all the users
     * @param inputReader buffered reader that handles user input
     * @param transactionLog logs user transaction
     */
    public static void runCreateCustomer(HashMap<String, Customer> data, BufferedReader inputReader, Writer transactionLog){
        try{
            System.out.print("Enter First Name: ");
            String customerFirstName = inputReader.readLine();
            System.out.println();
            System.out.print("Enter Last Name: ");
            String customerLastName = inputReader.readLine();
            System.out.println();
            System.out.print("Enter Date of Birth (##-##-####): ");
            String customerDOB = inputReader.readLine();
            System.out.println();
            System.out.print("Enter Adress: ");
            String customerAddress = inputReader.readLine();
            String[] customerAddressArray = customerAddress.split(",");
            System.out.println();
            System.out.print("Enter Phone Number: ");
            String customerPhoneNumber = inputReader.readLine();
            System.out.println();
            System.out.print("Enter Savings Starting Balance: $");
            String savingsStartingBalance = inputReader.readLine();
            //create savings account
            savingsAccountIdTracker++;
            Savings savingsAccount = createSavingsAccount(savingsAccountIdTracker, Double.parseDouble(savingsStartingBalance), 0.0);
            System.out.println();
            System.out.print("Would you like to create a checking account?[y/n]: ");
            String createChecking = inputReader.readLine();
            Checking checkingAccount = null;
            if(createChecking.equals("y")){
                checkingAccount = createCheckingProcedure(inputReader); //Create Checking Account
                System.out.println("Checking account created successfully");
            }
            //Do checking procedure
            System.out.println();
            System.out.print("Would you like to create a credit account?[y/n]: ");
            String createCredit = inputReader.readLine();
            Credit creditAccount = null;
            if(createCredit.equals("y")){
                creditAccount = createCreditProcedure(inputReader); //Create Credit Account
                System.out.println("Credit account created successfully");
            }
            System.out.println();
            customerIdTracker++;
            data.put(
                Integer.toString(customerIdTracker), 
                new Customer(
                customerFirstName, //First name
                customerLastName, //Last name
                "123", //Customer ID
                true, //Has Checking
                true, //Has Savings
                customerDOB, //Date of birth
                customerPhoneNumber, //Phone number
                customerAddressArray, //Address
                checkingAccount, //Checking
                savingsAccount, //createSavingsAccount(Integer.parseInt(VALS[10]), Double.parseDouble(VALS[13]), 0.0), //Savings
                creditAccount //Credit
                )
            );
        }catch(IOException e){
            System.out.println("Error:" + e);
        }
    }//END OF runCreateCustomer()
    /**
     * This method creates a checking account for a new user
     * @param inputReader Handles user input
     * @return returns a Checking account object
     */
    public static Checking createCheckingProcedure(BufferedReader inputReader){
        try{
            checkingAccountIdTracker++;
            System.out.print("Enter starting Checking Balance: $");
            String checkingStartingBalance = inputReader.readLine();
            return createCheckingAccount(checkingAccountIdTracker, Double.parseDouble(checkingStartingBalance), 0.0);
        }catch(IOException e){
            System.out.println("Error: " + e);
            return null;
        }
    } //END OF createCheckingProcedure()

    public static Credit createCreditProcedure(BufferedReader inputReader){
        creditAccountIdTracker++;
        System.out.println("Creating Credit Account... Please wait");
        return createCreditAccount(creditAccountIdTracker, 10_000.0, 0.0, 0.0);
    } //END OF createCreditProcedure()
    /**
     * This method is going to handle the logic of runing the application as a customer
     * @param data this is the data stored in a hashmap
     * @param inputReader input reader which handles user input
     * @param transactionLog logs user transactions
     */
    public static void runCustomer(HashMap<String, Customer> data, BufferedReader inputReader, Writer transactionLog){
        try{
            System.out.print("Enter customerID number: ");
            String accountNumber = inputReader.readLine();
            Customer currentCustomer = data.get(accountNumber); //Getting checking object (accountNumber is the key)
            System.out.println();
            System.out.println("Welcome, " + currentCustomer.getName() + ".");
            boolean transactionCompleted = false;
            while(!transactionCompleted){ //Keep looping until all transactions are completed
                System.out.println("Enter number of desired action");
                System.out.println("1. Inquire Balance 2. Deposit 3. Withdrawal 4. Transer Money 5. Send Money 6. Exit");
                String desiredAction = inputReader.readLine();
                String moneyAction = "";
                switch(desiredAction){
                    case "1": //Inquire Balance
                        int desiredAccount = getDesiredAccount(true, inputReader);
                        if(desiredAccount == 1) currentCustomer.getCheckingAccount().inquireBalance(); //Check checking balance
                        if(desiredAccount == 2) currentCustomer.getSavingsAccount().inquireBalance(); //Check savings balance
                        if(desiredAccount == 3) currentCustomer.getCreditAccount().inquireBalance(); //Check credit balance
                        if(desiredAccount != 1 && desiredAccount != 2 && desiredAccount != 3) System.out.println("Error: No Balance inquired");
                        break;
                    case "2": //Deposit
                        System.out.println("To which account would you like to perform this transaction?:");
                        desiredAccount = getDesiredAccount(true, inputReader);
                        System.out.print("Input amount you'd like to deposit: $");
                        moneyAction = inputReader.readLine();
                        System.out.println();
                        if(desiredAccount == 1){ //Checking
                            currentCustomer.getCheckingAccount().receiveMoney(Double.parseDouble(moneyAction), "Deposit");
                            transactionLog.write(currentCustomer.getName() + " deposited $" + moneyAction + " in their checking.");
                            transactionLog.write("\r\n");
                        }
                        if(desiredAccount == 2){ //Savings
                            currentCustomer.getSavingsAccount().receiveMoney(Double.parseDouble(moneyAction), "Deposit");
                            transactionLog.write(currentCustomer.getName() + " deposited $" + moneyAction + " in their savings.");
                            transactionLog.write("\r\n");
                        }
                        if(desiredAccount == 3 && currentCustomer.getCreditAccount().canReceive(Double.parseDouble(moneyAction))){ //Credit
                            currentCustomer.getCreditAccount().receiveMoney(Double.parseDouble(moneyAction), "Deposit");
                            transactionLog.write(currentCustomer.getName() + " deposited $" + moneyAction + " in their credit.");
                            transactionLog.write("\r\n");
                        } else{
                            System.out.println("ERROR: Cannot deposit more than amount due.");
                        }
                        if(desiredAccount != 1 && desiredAccount != 2 && desiredAccount != 3) System.out.println("Error: No Deposit made");
                        break;
                    case "3": //Withdrawal
                        System.out.println("To which account would you like to perform this transaction?:");
                        desiredAccount = getDesiredAccount(false, inputReader);
                        System.out.print("Input amount you'd like to withdraw: $");
                        moneyAction = inputReader.readLine();
                        System.out.println();
                        if(desiredAccount == 1){ //Checking
                            currentCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction), "Withdrawal");
                            transactionLog.write(currentCustomer.getName() + " withdrew $" + moneyAction + " in their checking.");
                            transactionLog.write("\r\n");
                        }
                        if(desiredAccount == 2){ //Savings
                            currentCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction), "Withdrawal");
                            transactionLog.write(currentCustomer.getName() + " withdrew $" + moneyAction + " in their savings.");
                            transactionLog.write("\r\n");
                        }
                        if(desiredAccount != 1 && desiredAccount != 2 && desiredAccount != 3) System.out.println("Error: No Withdrawal made");
                        break;
                    case "4": //Transfer Money
                        System.out.println("Which account do you want to transfer from?:");
                        int transferFromAccount = getDesiredAccount(false, inputReader);
                        System.out.print("How much would you like to transfer?: $");
                        moneyAction = inputReader.readLine();
                        if(transferFromAccount == 1) currentCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction), "Transfer");
                        if(transferFromAccount == 2) currentCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction), "Transfer");
                        System.out.println("Which account do you want to transfer $" + moneyAction + " to?:");
                        int transferToAccount = getDesiredAccount(true, inputReader);
                        if(transferToAccount == 1){
                            currentCustomer.getCheckingAccount().receiveMoney(Double.parseDouble(moneyAction), "Transfer");
                            transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction + " from savings to checking.");
                            transactionLog.write("\r\n");
                        }
                        if(transferToAccount == 2){
                            currentCustomer.getSavingsAccount().receiveMoney(Double.parseDouble(moneyAction), "Transfer");
                            transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction + " from checking to savings.");
                            transactionLog.write("\r\n");
                        }
                        if(transferToAccount == 3 && currentCustomer.getCreditAccount().canReceive(Double.parseDouble(moneyAction))){
                            currentCustomer.getCreditAccount().receiveMoney(Double.parseDouble(moneyAction), "Transfer");
                            if(transferFromAccount == 1){
                                transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction + " from checking to credit.");
                                transactionLog.write("\r\n");
                            } else{
                                transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction + " from savings to credit.");
                                transactionLog.write("\r\n");
                            }
                        } else{
                            System.out.println("ERROR: Cannot transfer more than amount due.");
                        }
                        break;
                    case "5": //Send Money
                        System.out.println("Which account would you like to send money from?:");
                        desiredAccount = getDesiredAccount(false, inputReader);
                        System.out.print("Enter customer ID of receiver: ");
                        Customer receivingCustomer = data.get(inputReader.readLine());
                        System.out.println("Which account would you like to send money to?:");
                        int desiredReceiverAccount = getDesiredAccount(false, inputReader);
                        System.out.println();
                        System.out.print("Input amount you'd like to send: $");
                        moneyAction = inputReader.readLine();
                        if(desiredAccount == 1) currentCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction), "Sent"); //Check checking balance
                        if(desiredAccount == 2) currentCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction), "Sent"); //Check savings balance
                        System.out.println();
                        if(desiredReceiverAccount == 1){
                            receivingCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction), "Sent"); //Check checking balance
                            transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction + " to " + receivingCustomer.getName());
                            transactionLog.write("\r\n");
                        }
                        if(desiredReceiverAccount == 2){
                            receivingCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction), "Sent"); //Check savings balance
                            transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction + " to " + receivingCustomer.getName());
                            transactionLog.write("\r\n");
                        }
                        break;
                    case "6":
                        System.out.println("Thank you, have a nice day!");
                        return; //return, because we are ending all operations
                    default: //Unrecognized character
                        System.out.println("Action not recognized.");
                        break;
                }
            }
        } catch(IOException e){
            System.out.println("Error: " + e);
        }
    }//END OF runCustomer()
    /**
     * This method runs the bank manager functionality
     * @param data customer data stored in a hashmap
     * @param inputReader input reader handles user input
     * @param transactionLog to read the user transactions
     */
    public static void runBankManager(HashMap<String, Customer> data, HashMap<String, String> customerIds,BufferedReader inputReader, Writer transactionLog){
        try{
            boolean transactionCompleted = false;
            while(!transactionCompleted){
                System.out.println("1. Inquire customer by Name 2. Create Bank Statement 3. Print all accounts 4. Exit");
                String desiredAction = inputReader.readLine();
                switch(desiredAction){
                    case "1":
                        System.out.print("Enter first name of desired customer: ");
                        String customerFirstName = inputReader.readLine();
                        System.out.println();
                        System.out.print("Enter last name of desired customer: ");
                        String customerLastName = inputReader.readLine();
                        String desiredCustomerId = customerIds.get(customerFirstName + " "+ customerLastName);
                        Customer desiredCustomer = data.get(desiredCustomerId);
                        System.out.println();
                        System.out.println("Customer: " + desiredCustomer.getName());
                        System.out.print("Checking ");
                        desiredCustomer.getCheckingAccount().inquireBalance();
                        System.out.print("Savings ");
                        desiredCustomer.getSavingsAccount().inquireBalance();
                        System.out.print("Credit ");
                        desiredCustomer.getCreditAccount().inquireBalance();
                        System.out.println();
                        break;
                    case "2":
                        System.out.print("Enter first name of customer: ");
                        String statementCustomerFirstName = inputReader.readLine();
                        System.out.println();
                        System.out.print("Enter last name of customer: ");
                        String statementCustomerLastName = inputReader.readLine();
                        System.out.println();
                        String statementCustomerId = customerIds.get(statementCustomerFirstName + " " + statementCustomerLastName);
                        Customer statementCustomer = data.get(statementCustomerId);
                        BankStatement customerBankStatement = new BankStatement(statementCustomer);
                        customerBankStatement.createBankStatement(statementCustomerFirstName+statementCustomerLastName+"statement"+(new SimpleDateFormat("yyyy_MM_dd")).format(new Date()));
                        break;
                    case "3":
                        for(String key : data.keySet()){
                            Customer currentCustomer = data.get(key);
                            System.out.println("Customer: " + currentCustomer.getName());
                            System.out.print("Checking ");
                            currentCustomer.getCheckingAccount().inquireBalance();
                            System.out.print("Savings ");
                            currentCustomer.getSavingsAccount().inquireBalance();
                            System.out.print("Credit ");
                            currentCustomer.getCreditAccount().inquireBalance();
                            System.out.println();
                        }
                        break;
                    case "4":
                        transactionCompleted = !transactionCompleted;
                        break;
                    default:
                        System.out.println("Action not recognized. Try again.");
                        break;
                }
            }
        } catch(IOException e){
            System.out.println("Error: " + e);
        }
    }//END OF runBankManager()
    /**
     * This method reads transaction from a csv file
     * @param data this is the user data
     * @param customerIds this is a hashmap of the customer names and their ids
     * @param transactionLog this is a log of the transactions
     */
    public static void runTransactionReader(HashMap<String, Customer> data, HashMap<String, String> customerIds, Writer transactionLog){
        BufferedReader brTransactions = null;
        String currentTransaction = "";
        try{
            brTransactions = new BufferedReader(new FileReader("Transaction Actions(3).csv"));
            int count = 0;
            while((currentTransaction = brTransactions.readLine()) != null){
                if(count > 0){
                    String sender = "";
                    String receiver = "";
                    String senderAccount = "";
                    String receiverAccount = "";
                    String transactionAction = "";
                    String amount = "";
                    String[] TRANSACTION = currentTransaction.split(",");
                    //0 first naem 1 last name 2 from where 3 action 4 reciever first 5 reciever last 6 to where 7 amount
                    if(TRANSACTION[3].equals("inquires")){
                        sender = TRANSACTION[0] + " " + TRANSACTION[1];
                        receiver = TRANSACTION[0] + " " + TRANSACTION[1];
                        transactionAction = TRANSACTION[3];
                        senderAccount = TRANSACTION[2];
                        receiverAccount = TRANSACTION[2];
                    }else{
                        sender = TRANSACTION[0] + " " + TRANSACTION[1];
                        receiver = TRANSACTION[4] + " " + TRANSACTION[5];
                        transactionAction = TRANSACTION[3];
                        senderAccount = TRANSACTION[2];
                        receiverAccount = TRANSACTION[6];
                        amount = TRANSACTION[7];
                    }
                    switch(transactionAction){
                        case "pays":
                            transactionReaderTwoCustomerHandler(data, customerIds, sender, receiver, transactionAction, senderAccount, receiverAccount, Double.parseDouble(amount));
                            break;
                        case "withdraws":
                            transactionReaderOneCustomerHandler(data, customerIds, sender, transactionAction, senderAccount, Double.parseDouble(amount));
                            break;
                        case "inquires":
                            transactionReaderOneCustomerHandler(data, customerIds, sender, transactionAction, senderAccount, 0.0);
                            break;
                        case "deposits":
                            transactionReaderOneCustomerHandler(data, customerIds, receiver, transactionAction, receiverAccount, Double.parseDouble(amount));
                            break;
                        case "transfers":
                            transactionReaderTwoCustomerHandler(data, customerIds, sender, receiver, transactionAction, senderAccount, receiverAccount, Double.parseDouble(amount));
                            break;
                        default:
                            System.out.println("Action at row " + (count + 1) + " not recognized.");
                            break;
                    }
                    if(sender.equals("")){
                        //run single person transaction
                    }else{
                        //run multiple person transaction
                    }
                }
                count++;
            }
            System.out.println("Transactions Completed.");
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    } //END OF runTransactionReader()
    /**
     * This method handls the logic of reading a single customer transction from a transaction reader
     * @param data the hashmap of the users
     * @param customerIds hashmap of the users and their ids
     * @param customer string of customer name
     * @param transactionAction string of the tansaction that is oing to be done
     * @param account the account that is receiving the action
     * @param amount the amount of money being moved
     */
    public static void transactionReaderOneCustomerHandler(HashMap<String, Customer> data, HashMap<String, String> customerIds, String customer, String transactionAction, String account, double amount){
        String customerId = customerIds.get(customer);
        Customer customerAccount = data.get(customerId);
        String caseAction = "Handler";
        switch(transactionAction){
            case "inquires":
                if(account.equals("Checking")) customerAccount.getCheckingAccount().inquireBalance();
                if(account.equals("Savings")) customerAccount.getSavingsAccount().inquireBalance();
                if(account.equals("Credit")) customerAccount.getCreditAccount().inquireBalance();
                break;
            case "withdraws":
                if(account.equals("Checking")) customerAccount.getCheckingAccount().sendMoney(amount, caseAction);
                if(account.equals("Savings")) customerAccount.getSavingsAccount().sendMoney(amount, caseAction);
                if(account.equals("Credit")) customerAccount.getCreditAccount().sendMoney(amount, caseAction);
                break;
            case "deposits":
                if(account.equals("Checking")) customerAccount.getCheckingAccount().receiveMoney(amount, caseAction);
                if(account.equals("Savings")) customerAccount.getSavingsAccount().receiveMoney(amount, caseAction);
                if(account.equals("Credit")) customerAccount.getCreditAccount().receiveMoney(amount, caseAction);
                break;
            default:
                System.out.println("Action not recognized");
                break;
        }
    }
    /**
     * This method handles the logic of a transaction between two people from the transaction reader
     * @param data hashmap of the users and accounts
     * @param customerIds hashmap of users and their ids
     * @param sender name of the sender
     * @param receiver name of the receiver
     * @param transactionAction name of the transction being done
     * @param senderAccount the account that is performing the action
     * @param receiverAccount the accoun that is receiving the action
     * @param amount the amount of money that is being moved
     */
    public static void transactionReaderTwoCustomerHandler(HashMap<String, Customer> data, HashMap<String, String> customerIds, String sender, String receiver, String transactionAction, String senderAccount, String receiverAccount, double amount){
        String senderId = customerIds.get(sender);
        String receiverId = customerIds.get(receiver);
        Customer senderCustomer = data.get(senderId);
        Customer receiverCustomer = data.get(receiverId);
        switch(transactionAction){
            case "pays":
                twoCustomerHandlerActions(senderCustomer, receiverCustomer, senderAccount, receiverAccount, amount);
                break;
            case "transfers":
                twoCustomerHandlerActions(senderCustomer, receiverCustomer, senderAccount, receiverAccount, amount);
                break;
            default:
                System.out.println("Action not recognized");
                break;
        }
    } //END OF transactionReaderTwoCustomerHandler()
    /**
     * This method is to facilitate the logic of two people transaction from the transaction reader
     * @param senderCustomer This is a customer object the is sending money
     * @param receiverCustomer this is a customer object that is receiving money
     * @param senderAccount this is the account of the sender that is being affected
     * @param receiverAccount this is the account of the receiver that is being affected
     * @param amount this is the amount of money tht is being moved
     */
    public static void twoCustomerHandlerActions(Customer senderCustomer, Customer receiverCustomer, String senderAccount, String receiverAccount, double amount){
        String caseAction = "Handler";
        switch(senderAccount){
            case "Checking":
                senderCustomer.getCheckingAccount().sendMoney(amount, caseAction);
                if(receiverAccount.equals("Checking")) receiverCustomer.getCheckingAccount().receiveMoney(amount, caseAction);
                if(receiverAccount.equals("Savings")) receiverCustomer.getSavingsAccount().receiveMoney(amount, caseAction);
                if(receiverAccount.equals("Credit")) receiverCustomer.getCreditAccount().receiveMoney(amount, caseAction);
                break;
            case "Savings":
                senderCustomer.getSavingsAccount().sendMoney(amount, caseAction);
                if(receiverAccount.equals("Checking")) receiverCustomer.getCheckingAccount().receiveMoney(amount, caseAction);
                if(receiverAccount.equals("Savings")) receiverCustomer.getSavingsAccount().receiveMoney(amount, caseAction);
                if(receiverAccount.equals("Credit")) receiverCustomer.getCreditAccount().receiveMoney(amount, caseAction);
                break;
            default:
                System.out.println("ERROR: Account not valid");
                break;
        }
    } //END OF twoCustomerHandlerActions()
    /**
     * This method handles the logic of retreiving a desired account(checking, savings, credit)
     * @param showCredit we are checking where we want to display credit account
     * @param inputReader handles user input
     * @return integer depending on the desired account
     */
    public static int getDesiredAccount(boolean showCredit, BufferedReader inputReader){ //1. Checking 2. Savings 3. Credit
        try{
            String accountAction = "";
            boolean isDone = false;
            while(!isDone){
                if(showCredit){
                    System.out.println("1. Checking 2. Savings 3. Credit");
                    accountAction = inputReader.readLine();
                    if(accountAction.equals("1")) return 1; //Checking
                    if(accountAction.equals("2")) return 2; //Savings
                    if(accountAction.equals("3")) return 3; //Credit
                    else System.out.println("Input not recognized. Try again."); //Try again
                } else{
                    System.out.println("1. Checking 2. Savings");
                    accountAction = inputReader.readLine();
                    if(accountAction.equals("1")) return 1; //Checking
                    if(accountAction.equals("2")) return 2; //Savings
                    else System.out.println("Input not recognized. Try again."); //Try again
                }
            }
            System.out.println("Error: No account defined");
            return -1;
        } catch(IOException e){
            System.out.println("Error: " + e);
            return -1;
        }
    } //END OF getDesiredAccount()
} //END OF class

//Mickey Mouse 000-00-0001
//Donald Duck 000-00-0002