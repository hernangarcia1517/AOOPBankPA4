
/**
 * @author Hernan Garcia
 * @version 1.1.0
 * @since October 19, 2020
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class Bank {
	static int customerIdTracker = 0;
	static int checkingAccountIdTracker = 0;
	static int savingsAccountIdTracker = 0;
	static int creditAccountIdTracker = 0;

	/**
	 * Main method
	 * 
	 * @param args arguments during run
	 */
	public static void main(String[] args) {
		BufferedReader br = null;
		String currentUser = "";
		HashMap<String, Customer> data = new HashMap<String, Customer>();// 20, 0.75f);
		HashMap<String, String> customerIds = new HashMap<String, String>();
		BufferedReader inputReader = null;
		Writer transactionLog = null;
		readCVSFile(data, customerIds, br, currentUser); // Read the CVS File
		runBankingApp(data, customerIds, inputReader, transactionLog); // Run the Banking Process
	} // END OF main()

	/**
	 * This method read from a CSV File
	 * 
	 * @param data        the hashtable in which we are storing datat
	 * @param br          buffered reader
	 * @param currentUser the current user string
	 */
	public static void readCVSFile(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			BufferedReader br, String currentUser) {
		try {
			br = new BufferedReader(new FileReader("CS 3331 - Bank Users 4.csv")); // Hardcoded File Give prompt
																						// instead
			int count = 0;
			HashMap<String, Integer> inputCorrector = new HashMap<String, Integer>();
			while ((currentUser = br.readLine()) != null) {
				if (count == 0) {
					String[] headers = currentUser.split(",");
                    boolean seenAddress = false;
                    boolean seenDateOfBirth = false;
					for (int i = 0; i < headers.length; i++) {
                        if(seenAddress && seenDateOfBirth){
                            inputCorrector.put(headers[i], i + 3);
                        }else if (seenAddress){
							inputCorrector.put(headers[i], i + 2);
                        }else if(seenDateOfBirth){
                            inputCorrector.put(headers[i], i + 1);
                        }else{
							inputCorrector.put(headers[i], i);
                        }
						if (headers[i].equals("Address")){
							seenAddress = true;
                        }else if(headers[i].equals("Date of Birth")){
                            seenDateOfBirth = true;
                        }
						System.out.println(headers[i] +" "+ inputCorrector.get(headers[i]));
					}
				}
				if (count > 0) { // Omitting the first row, which should just be the headers
					String[] VALS = currentUser.split(","); // This is going to create a String array, now we have to
															// store it
					// System.out.println(inputCorrector.get("Date of Birth"));
					String[] dateOfBirth = { VALS[inputCorrector.get("Date of Birth")], VALS[inputCorrector.get("Date of Birth") + 1]};
					// System.out.println(inputCorrector.get("Address"));
					String[] addressArray = { VALS[inputCorrector.get("Address")],
							VALS[inputCorrector.get("Address") + 1], VALS[inputCorrector.get("Address") + 2] };
					data.put(VALS[inputCorrector.get("Identification Number")], new Customer(
							VALS[inputCorrector.get("First Name")], // First name
							VALS[inputCorrector.get("Last Name")], // Last name
							VALS[inputCorrector.get("Identification Number")], // Customer ID
							VALS[inputCorrector.get("Password")],
							VALS[inputCorrector.get("Email")],
							true, // Has Checking
							true, // Has Savings
							dateOfBirth, // Date of birth
							VALS[inputCorrector.get("Phone Number")], // Phone number
							addressArray, // Address
							createCheckingAccount(Integer.parseInt(VALS[inputCorrector.get("Checking Account Number")]),
									Double.parseDouble(VALS[inputCorrector.get("Checking Starting Balance")]), 0.0), // Checking
							createSavingsAccount(Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]),
									Double.parseDouble(VALS[inputCorrector.get("Savings Starting Balance")]), 0.0), // Savings
							createCreditAccount(Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]),
									Double.parseDouble(VALS[inputCorrector.get("Credit Max")]),
									Double.parseDouble(VALS[inputCorrector.get("Credit Starting Balance")]), 0.0) // Credit
                    ));
                    
					customerIds.put(data.get(VALS[inputCorrector.get("Identification Number")]).getName(), VALS[inputCorrector.get("Identification Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Identification Number")]) > customerIdTracker)
						customerIdTracker = Integer.parseInt(VALS[inputCorrector.get("Identification Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Checking Account Number")]) > checkingAccountIdTracker)
						checkingAccountIdTracker = Integer.parseInt(VALS[inputCorrector.get("Checking Account Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]) > savingsAccountIdTracker)
						savingsAccountIdTracker = Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]) > creditAccountIdTracker)
						creditAccountIdTracker = Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]);
				}
				count++;
			}
		} catch (FileNotFoundException e) { // Handle File not found
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // Closing BufferedReader if it is not null
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} // END OF readCVSFile()

	/**
	 * The run banking app handles all the banking functionality
	 * 
	 * @param data           the data hashmap
	 * @param inputReader    buffered reader that handles user input
	 * @param transactionLog logs user transaction
	 */
	public static void runBankingApp(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			BufferedReader inputReader, Writer transactionLog) {
		try {
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			transactionLog = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("transactionLog.txt", true), "utf-8")); // Appending the
																										// file since we
																										// might ant to
																										// keep track of
																										// all
																										// transactions
			boolean isDone = false;
			while (!isDone) {
				System.out.println(
						"1. Create Customer 2. Returning Customer 3. Bank Manager 4. Transaction Reader 5. Exit");
				String desiredFunctionality = inputReader.readLine();
				switch (Integer.parseInt(desiredFunctionality)) {
				case 1: // Create Customer
					runCreateCustomer(data, inputReader, transactionLog);
					break;
				case 2: // Customer
					runCustomer(data, inputReader, transactionLog);
					break;
				case 3: // Bank Manager
					runBankManager(data, customerIds, inputReader, transactionLog);
					break;
				case 4:
					runTransactionReader(data, customerIds, transactionLog);
				case 5:
					isDone = !isDone;
					createCopy(data);
					System.out.println("Ending Application");
					break;
				default:
					System.out.println("Input not recognized. Please try again.");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // Closing BufferedReader and Writer
			try {
				if (inputReader != null) {
					inputReader.close();
				}
				if (transactionLog != null) {
					transactionLog.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} // END OF runBankingApp()

	/**
	 * This method handles the creation of a checking account for a customer
	 * 
	 * @param accountNumber  the account number
	 * @param currentBalance the opening balance
	 * @param interestRate   the interest rate
	 * @return returning the checking account
	 */
	public static Checking createCheckingAccount(int accountNumber, double currentBalance, double interestRate) {
		return new Checking(accountNumber, currentBalance, interestRate);
	}// END OF createCheckingAccount()

	/**
	 * This method handles the creation of a savings account for a customer
	 * 
	 * @param accountNumber  the account number
	 * @param currentBalance the opening balance
	 * @param interestRate   the interest rate
	 * @return returning the savings account
	 */
	public static Savings createSavingsAccount(int accountNumber, double currentBalance, double interestRate) {
		return new Savings(accountNumber, currentBalance, interestRate);
	}// END OF createSavingsAccount()

	/**
	 * This method handles the creation of a credit account for a customer
	 * 
	 * @param accountNumber  the account number
	 * @param currentBalance the opening balance
	 * @param interestRate   the interest rate
	 * @return returning the credit account
	 */
	public static Credit createCreditAccount(int accountNumber, double creditLine, double outstandingBalance,
			double interestRate) {
		return new Credit(accountNumber, creditLine, outstandingBalance, interestRate);
	}// END OF createCreditAccount()

	/**
	 * This method runs the create new customer logic
	 * 
	 * @param data           the hashmap of all the users
	 * @param inputReader    buffered reader that handles user input
	 * @param transactionLog logs user transaction
	 */
	public static void runCreateCustomer(HashMap<String, Customer> data, BufferedReader inputReader,
	Writer transactionLog) {
customerIdTracker++;// increase the number of IDs we have
try {
	System.out.print("Enter First Name: ");
	String customerFirstName = inputReader.readLine();
	System.out.println();
	System.out.print("Enter Last Name: ");
	String customerLastName = inputReader.readLine();
	System.out.println();
	System.out.print("Enter Month of Birth");
	String customerBirthMonth = inputReader.readLine();
	System.out.println("Enter Day of Birth: ");
	String customerBirthDay = inputReader.readLine();
	System.out.println("Enter Year of Birth: ");
	String customerBirthYear = inputReader.readLine();
	String[] customerDOB = {customerBirthMonth + " " + customerBirthDay, customerBirthYear};
	System.out.println();
	System.out.print("Enter Adress (Street, City, ZIP Code):");
	String customerAddress = inputReader.readLine(); // NEED TO VERIFY THE INPUT
	String[] customerAddressArray = customerAddress.split(",");
	System.out.println();
	System.out.print("Enter Email Address: ");
	String email = inputReader.readLine();
	System.out.println();
	System.out.print("Enter Phone Number: ");
	String customerPhoneNumber = inputReader.readLine();
	System.out.println();
	String password = generatePassword(customerFirstName, customerLastName); // generate a password for the new
																				// user
	System.out.print("Enter Savings Starting Balance: $");
	String savingsStartingBalance = inputReader.readLine();
	System.out.print("Would you like to create an additional account?[y/n]: ");
	String createAdditional = inputReader.readLine();
			if (createAdditional.equals("y")) {
				System.out.println("To open a new checking account enter [1]\nTo open a new credit account enter [2]\n"
						+ "To open a new checking and credit account enter[3]:");
				String createAnother = inputReader.readLine();
				int createAnotherToInt = (Integer.valueOf(createAnother));
				boolean flagCustomer = true;
				do {
					switch (createAnotherToInt) {
					case 1:
						System.out.print("Enter Checking Starting Balance: $");
						String checkingStartingBalance = inputReader.readLine();

						// create the customer
						// create savings account
						Savings savingsAccount = createSavingsProcedure(savingsStartingBalance);
						// Create Checking Account
						Checking checkingAccount = createCheckingProcedure(checkingStartingBalance);
						Credit creditAccount = ghostCredit();
						data.put(Integer.toString(customerIdTracker), new Customer(
								customerFirstName, // First name
								customerLastName, // Last name
								Integer.toString(customerIdTracker), // Customer ID
								password,
								email,
								true, // Has Checking
								true, // Has Savings
								customerDOB, // Date of birth
								customerPhoneNumber, // Phone number
								customerAddressArray, // Address
								checkingAccount, // Checking
								savingsAccount, // createSavingsAccount(Integer.parseInt(VALS[10]),
								creditAccount // Credit
						));
						break;
					case 2:
						System.out.print("Enter Credit Starting Balance: $");
						String creditStartingBalance = inputReader.readLine();

//create the customer
// create savings account
						Savings savingsAccount1 = createSavingsProcedure(savingsStartingBalance);
						// Create Credit Account
						Credit creditAccount1 = createCreditProcedure(creditStartingBalance);

						Checking checkingAccount1 = ghostChecking();

						data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
								customerLastName, // Last name
								Integer.toString(customerIdTracker), // Customer ID
								password,
								email,
								true, // Has Checking
								true, // Has Savings
								customerDOB, // Date of birth
								customerPhoneNumber, // Phone number
								customerAddressArray, // Address
								checkingAccount1, // Checking
								savingsAccount1, // createSavingsAccount(Integer.parseInt(VALS[10]),
								creditAccount1 // Credit
						));
						break;
					case 3:
						System.out.print("Enter Checking Starting Balance: $");
						String checkingStartingBalance1 = inputReader.readLine();
						System.out.print("Enter Credit Starting Balance: $");
						String creditStartingBalance1 = inputReader.readLine();
						
						// create the customer
						
						// create savings account
						Savings savingsAccount3 = createSavingsProcedure(savingsStartingBalance);
						// Create Checking Account
						Checking checkingAccount3 = createCheckingProcedure(checkingStartingBalance1);
						// Create Credit Account
						Credit creditAccount3 = createCreditProcedure(creditStartingBalance1);
						
						data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
								customerLastName, // Last name
								Integer.toString(customerIdTracker), // Customer ID
								password,
								email,
								true, // Has Checking
								true, // Has Savings
								customerDOB, // Date of birth
								customerPhoneNumber, // Phone number
								customerAddressArray, // Address
								checkingAccount3, // Checking
								savingsAccount3, // createSavingsAccount(Integer.parseInt(VALS[10]),
								creditAccount3 // Credit
						));
						break;
					default:
						System.out.println("please enter 1, 2 or 3 to continue:");
//createAnother = inputReader.readLine();
//int createAnotherToInt1 = (Integer.valueOf(createAnother));
						flagCustomer = false;
					}
				} while (!flagCustomer);
			} else {
				// create the customer
				// create savings account
				Savings savingsAccount4 = createSavingsProcedure(savingsStartingBalance);
				// Create Checking Account
				Checking checkingAccount4 = ghostChecking();
				// Create Credit Account
				Credit creditAccount4 = ghostCredit();
				
				data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
						customerLastName, // Last name
						Integer.toString(customerIdTracker), // Customer ID
						password,
						email,
						true, // Has Checking
						true, // Has Savings
						customerDOB, // Date of birth
						customerPhoneNumber, // Phone number
						customerAddressArray, // Address
						checkingAccount4, // Checking
						savingsAccount4, // createSavingsAccount(Integer.parseInt(VALS[10]),
						creditAccount4 // Credit
				));
			}
		} catch (IOException e) {
			System.out.println("Error:" + e);
		}
	}// END OF runCreateCustomer()

	public static Checking createCheckingProcedure(String startingAmount) {
		checkingAccountIdTracker++;
		Checking checkingAccount = createCheckingAccount(checkingAccountIdTracker, Double.parseDouble(startingAmount),
				0.0);
		System.out.println("Checking account created successfully");
		System.out.println();
		return checkingAccount;
	} // END OF createCheckingProcedure()

	public static Credit createCreditProcedure(String startingAmount) {
		creditAccountIdTracker++;
		Credit creditAccount = createCreditAccount(creditAccountIdTracker, Double.parseDouble(startingAmount), 1000,
				0.0);
		System.out.println("Credit account created successfully");
		System.out.println();
		return creditAccount;
	} // END OF createCreditProcedure()

	public static Savings createSavingsProcedure(String startingAmount) {
		System.out.println();
		savingsAccountIdTracker++;
		Savings savingsAccount = createSavingsAccount(savingsAccountIdTracker, Double.parseDouble(startingAmount), 0.0);
		System.out.println("Savings account created successfully");
		System.out.println();
		return savingsAccount;
	}// END OF createSavingsProcedure()

	public static Checking ghostChecking() {
		Checking ghost = createCheckingAccount(0, 0.0, 0.0);
		return ghost;
	}

	public static Credit ghostCredit() {
		Credit ghost = createCreditAccount(0, 0.0, 0.0, 0.0);
		return ghost;
	}

	/**
	 * This method is going to handle the logic of runing the application as a
	 * customer
	 * 
	 * @param data           this is the data stored in a hashmap
	 * @param inputReader    input reader which handles user input
	 * @param transactionLog logs user transactions
	 */
	public static void runCustomer(HashMap<String, Customer> data, BufferedReader inputReader, Writer transactionLog) {
		try {
			System.out.print("Enter customerID number: ");
			String accountNumber = inputReader.readLine();
			Customer currentCustomer = data.get(accountNumber); // Getting checking object (accountNumber is the key)
			System.out.println();
			System.out.println("Welcome, " + currentCustomer.getName() + ".");
			boolean transactionCompleted = false;
			while (!transactionCompleted) { // Keep looping until all transactions are completed
				System.out.println("Enter number of desired action");
				System.out
						.println("1. Inquire Balance 2. Deposit 3. Withdrawal 4. Transer Money 5. Send Money 6. Exit");
				String desiredAction = inputReader.readLine();
				String moneyAction = "";
				switch (desiredAction) {
				case "1": // Inquire Balance
					int desiredAccount = getDesiredAccount(true, inputReader);
					if (desiredAccount == 1)
						currentCustomer.getCheckingAccount().inquireBalance(); // Check checking balance
					if (desiredAccount == 2)
						currentCustomer.getSavingsAccount().inquireBalance(); // Check savings balance
					if (desiredAccount == 3)
						currentCustomer.getCreditAccount().inquireBalance(); // Check credit balance
					if (desiredAccount != 1 && desiredAccount != 2 && desiredAccount != 3)
						System.out.println("Error: No Balance inquired");
					break;
				case "2": // Deposit
					boolean flag2 = true;
					System.out.println("To which account would you like to perform this transaction?:");
					desiredAccount = getDesiredAccount(true, inputReader);
					System.out.print("Input amount you'd like to deposit: $");
					moneyAction = inputReader.readLine();
					do {
						if (desiredAccount == 1) { // Checking
							currentCustomer.getCheckingAccount().receiveMoney(Double.parseDouble(moneyAction),
									"Deposit");
							transactionLog.write(
									currentCustomer.getName() + " deposited $" + moneyAction + " in their checking.");
							transactionLog.write("\r\n");
							return;
						}
						if (desiredAccount == 2) { // Savings
							currentCustomer.getSavingsAccount().receiveMoney(Double.parseDouble(moneyAction),
									"Deposit");
							transactionLog.write(
									currentCustomer.getName() + " deposited $" + moneyAction + " in their savings.");
							transactionLog.write("\r\n");
							return;
						}
						if (desiredAccount == 3
								&& currentCustomer.getCreditAccount().canReceive(Double.parseDouble(moneyAction))) { // Credit
							currentCustomer.getCreditAccount().receiveMoney(Double.parseDouble(moneyAction), "Deposit");
							transactionLog.write(
									currentCustomer.getName() + " deposited $" + moneyAction + " in their credit.");
							transactionLog.write("\r\n");
							return;
						} else {
							System.out.println(
									"ERROR: Cannot deposit more than amount due. Please enter a valid amount:");
							moneyAction = inputReader.readLine();
							flag2 = false;
						}
					} while (!flag2);
					break;
				case "3": // Withdrawal
					boolean flag3 = true;
					System.out.println("To which account would you like to perform this transaction?:");
					desiredAccount = getDesiredAccount(false, inputReader);
					System.out.print("Input amount you'd like to withdraw: $");
					moneyAction = inputReader.readLine();
					do {
						if (Double.parseDouble(moneyAction) < currentCustomer.getCheckingAccount().getCurrentBalance()
								&& Double.parseDouble(moneyAction) < currentCustomer.getSavingsAccount()
										.getCurrentBalance()) {
							if (desiredAccount == 1) { // Checking
								currentCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction),
										"Withdrawal");
								transactionLog.write(currentCustomer.getName() + " withdrew $" + moneyAction
										+ " in their checking.");
								transactionLog.write("\r\n");
								return;
							}
							if (desiredAccount == 2) { // Savings
								currentCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction),
										"Withdrawal");
								transactionLog.write(
										currentCustomer.getName() + " withdrew $" + moneyAction + " in their savings.");
								transactionLog.write("\r\n");
								return;
							} else {
								System.out.println("Error: No Withdrawal made");
							}
						} else {
							System.out.println("Invalid input, please enter a valid amount to continue:");
							moneyAction = inputReader.readLine();
							flag3 = false;
						}
					} while (!flag3);
					break;
				case "4": // Transfer Money

					boolean flag1 = true;

					System.out.println("Which account do you want to transfer from?:");
					int transferFromAccount = getDesiredAccount(false, inputReader);

					System.out.print("How much would you like to transfer?: $");
					moneyAction = inputReader.readLine();
					do {
						if (Double.parseDouble(moneyAction) < currentCustomer.getCheckingAccount().getCurrentBalance()
								&& Double.parseDouble(moneyAction) < currentCustomer.getSavingsAccount()
										.getCurrentBalance()) {
							if (transferFromAccount == 1)
								currentCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction),
										"Transfer");
							if (transferFromAccount == 2)
								currentCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction),
										"Transfer");

							System.out.println("Which account do you want to transfer $" + moneyAction + " to?:");
							int transferToAccount = getDesiredAccount(true, inputReader);

							if (transferToAccount == 1) {
								currentCustomer.getCheckingAccount().receiveMoney(Double.parseDouble(moneyAction),
										"Transfer");
								transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction
										+ " from savings to checking.");
								transactionLog.write("\r\n");
								return;
							}

							if (transferToAccount == 2) {
								currentCustomer.getSavingsAccount().receiveMoney(Double.parseDouble(moneyAction),
										"Transfer");
								transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction
										+ " from checking to savings.");
								transactionLog.write("\r\n");
								return;
							}

							if (transferToAccount == 3
									&& currentCustomer.getCreditAccount().canReceive(Double.parseDouble(moneyAction))) {
								currentCustomer.getCreditAccount().receiveMoney(Double.parseDouble(moneyAction),
										"Transfer");
								if (transferFromAccount == 1) {
									transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction
											+ " from checking to credit.");
									transactionLog.write("\r\n");
								} else {
									transactionLog.write(currentCustomer.getName() + " transfered $" + moneyAction
											+ " from savings to credit.");
									transactionLog.write("\r\n");
								}
								return;
							}
						} else {
							System.out.println("Invalid input, please enter a valid amount to continue:");
							moneyAction = inputReader.readLine();
							flag1 = false;
						}
					} while (!flag1);
					break;
				case "5": // Send Money
					boolean flag5 = true;
					System.out.println("Which account would you like to send money from?:");
					desiredAccount = getDesiredAccount(false, inputReader);
					System.out.print("Enter customer ID of receiver: ");
					String rC = inputReader.readLine();
					do {
						if (Double.parseDouble(rC) < data.size() + 1 && Double.parseDouble(rC) >= 0) {
							Customer receivingCustomer = data.get(rC);
							System.out.println("Which account would you like to send money to?:");
							int desiredReceiverAccount = getDesiredAccount(false, inputReader);
							System.out.print("Input amount you'd like to send: $");
							moneyAction = inputReader.readLine();
							// from checking
							if (desiredAccount == 1) {
								currentCustomer.getCheckingAccount().sendMoney(Double.parseDouble(moneyAction), "Sent"); // Check
								// to their checking
								if (desiredReceiverAccount == 1) {
									receivingCustomer.getCheckingAccount().receiveMoney(Double.parseDouble(moneyAction),
											"Deposit"); // Check
									transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction + " to "
											+ receivingCustomer.getName());
									transactionLog.write("\r\n");
									return;
								}
								// to their savings
								if (desiredReceiverAccount == 2) {
									receivingCustomer.getSavingsAccount().receiveMoney(Double.parseDouble(moneyAction),
											"Deposit"); // Check
									transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction + " to "
											+ receivingCustomer.getName());
									transactionLog.write("\r\n");
									return;
								}
							}
							// from savings
							if (desiredAccount == 2) {
								currentCustomer.getSavingsAccount().sendMoney(Double.parseDouble(moneyAction), "Sent"); // Check
								// to their checking
								// to their checking
								if (desiredReceiverAccount == 1) {
									receivingCustomer.getCheckingAccount().receiveMoney(Double.parseDouble(moneyAction),
											"Deposit"); // Check
									transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction + " to "
											+ receivingCustomer.getName());
									transactionLog.write("\r\n");
									return;
								}
								// to their savings
								if (desiredReceiverAccount == 2) {
									receivingCustomer.getSavingsAccount().receiveMoney(Double.parseDouble(moneyAction),
											"Deposit"); // Check
									transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction + " to "
											+ receivingCustomer.getName());
									transactionLog.write("\r\n");
									return;
								}
							}
						} else {
							System.out.println("Invalid input, please enter a valid ID:");
							rC = inputReader.readLine();
							flag5 = false;
						}
					} while (!flag5);
					break;
				case "6":
					System.out.println("Thank you, have a nice day!");
					return; // return, because we are ending all operations
				default: // Unrecognized character
					System.out.println("Action not recognized.");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}// END OF runCustomer()

	/**
	 * This method runs the bank manager functionality
	 * 
	 * @param data           customer data stored in a hashmap
	 * @param inputReader    input reader handles user input
	 * @param transactionLog to read the user transactions
	 */
	public static void runBankManager(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			BufferedReader inputReader, Writer transactionLog) {
		try {
			boolean transactionCompleted = false;
			while (!transactionCompleted) {
				System.out
						.println("1. Inquire customer by Name 2. Create Bank Statement 3. Print all accounts 4. Exit");
				String desiredAction = inputReader.readLine();
				switch (desiredAction) {
				case "1":
					System.out.print("Enter first name of desired customer: ");
					String customerFirstName = inputReader.readLine();
					System.out.println();
					System.out.print("Enter last name of desired customer: ");
					String customerLastName = inputReader.readLine();
					String desiredCustomerId = customerIds.get(customerFirstName + " " + customerLastName);
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
					String statementCustomerId = customerIds
							.get(statementCustomerFirstName + " " + statementCustomerLastName);
					Customer statementCustomer = data.get(statementCustomerId);
					BankStatement customerBankStatement = new BankStatement(statementCustomer);
					customerBankStatement.createBankStatement(statementCustomerFirstName + statementCustomerLastName
							+ "statement" + (new SimpleDateFormat("yyyy_MM_dd")).format(new Date()));
					break;
				case "3":
					for (String key : data.keySet()) {
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
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}// END OF runBankManager()

	/**
	 * This method reads transaction from a csv file
	 * 
	 * @param data           this is the user data
	 * @param customerIds    this is a hashmap of the customer names and their ids
	 * @param transactionLog this is a log of the transactions
	 */
	public static void runTransactionReader(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			Writer transactionLog) {
		BufferedReader brTransactions = null;
		String currentTransaction = "";
		try {
			brTransactions = new BufferedReader(new FileReader("Transaction Actions(3).csv"));
			int count = 0;
			while ((currentTransaction = brTransactions.readLine()) != null) {
				if (count > 0) {
					String sender = "";
					String receiver = "";
					String senderAccount = "";
					String receiverAccount = "";
					String transactionAction = "";
					String amount = "";
					String[] TRANSACTION = currentTransaction.split(",");
					// 0 first naem 1 last name 2 from where 3 action 4 reciever first 5 reciever
					// last 6 to where 7 amount
					if (TRANSACTION[3].equals("inquires")) {
						sender = TRANSACTION[0] + " " + TRANSACTION[1];
						receiver = TRANSACTION[0] + " " + TRANSACTION[1];
						transactionAction = TRANSACTION[3];
						senderAccount = TRANSACTION[2];
						receiverAccount = TRANSACTION[2];
					} else {
						sender = TRANSACTION[0] + " " + TRANSACTION[1];
						receiver = TRANSACTION[4] + " " + TRANSACTION[5];
						transactionAction = TRANSACTION[3];
						senderAccount = TRANSACTION[2];
						receiverAccount = TRANSACTION[6];
						amount = TRANSACTION[7];
					}
					switch (transactionAction) {
					case "pays":
						transactionReaderTwoCustomerHandler(data, customerIds, sender, receiver, transactionAction,
								senderAccount, receiverAccount, Double.parseDouble(amount));
						break;
					case "withdraws":
						transactionReaderOneCustomerHandler(data, customerIds, sender, transactionAction, senderAccount,
								Double.parseDouble(amount));
						break;
					case "inquires":
						transactionReaderOneCustomerHandler(data, customerIds, sender, transactionAction, senderAccount,
								0.0);
						break;
					case "deposits":
						transactionReaderOneCustomerHandler(data, customerIds, receiver, transactionAction,
								receiverAccount, Double.parseDouble(amount));
						break;
					case "transfers":
						transactionReaderTwoCustomerHandler(data, customerIds, sender, receiver, transactionAction,
								senderAccount, receiverAccount, Double.parseDouble(amount));
						break;
					default:
						System.out.println("Action at row " + (count + 1) + " not recognized.");
						break;
					}
					if (sender.equals("")) {
						// run single person transaction
					} else {
						// run multiple person transaction
					}
				}
				count++;
			}
			System.out.println("Transactions Completed.");
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	} // END OF runTransactionReader()

	/**
	 * This method handls the logic of reading a single customer transction from a
	 * transaction reader
	 * 
	 * @param data              the hashmap of the users
	 * @param customerIds       hashmap of the users and their ids
	 * @param customer          string of customer name
	 * @param transactionAction string of the tansaction that is oing to be done
	 * @param account           the account that is receiving the action
	 * @param amount            the amount of money being moved
	 */
	public static void transactionReaderOneCustomerHandler(HashMap<String, Customer> data,
			HashMap<String, String> customerIds, String customer, String transactionAction, String account,
			double amount) {
		String customerId = customerIds.get(customer);
		Customer customerAccount = data.get(customerId);
		String caseAction = "Handler";
		switch (transactionAction) {
		case "inquires":
			if (account.equals("Checking"))
				customerAccount.getCheckingAccount().inquireBalance();
			if (account.equals("Savings"))
				customerAccount.getSavingsAccount().inquireBalance();
			if (account.equals("Credit"))
				customerAccount.getCreditAccount().inquireBalance();
			break;
		case "withdraws":
			if (account.equals("Checking"))
				customerAccount.getCheckingAccount().sendMoney(amount, caseAction);
			if (account.equals("Savings"))
				customerAccount.getSavingsAccount().sendMoney(amount, caseAction);
			if (account.equals("Credit"))
				customerAccount.getCreditAccount().sendMoney(amount, caseAction);
			break;
		case "deposits":
			if (account.equals("Checking"))
				customerAccount.getCheckingAccount().receiveMoney(amount, caseAction);
			if (account.equals("Savings"))
				customerAccount.getSavingsAccount().receiveMoney(amount, caseAction);
			if (account.equals("Credit"))
				customerAccount.getCreditAccount().receiveMoney(amount, caseAction);
			break;
		default:
			System.out.println("Action not recognized");
			break;
		}
	}

	/**
	 * This method handles the logic of a transaction between two people from the
	 * transaction reader
	 * 
	 * @param data              hashmap of the users and accounts
	 * @param customerIds       hashmap of users and their ids
	 * @param sender            name of the sender
	 * @param receiver          name of the receiver
	 * @param transactionAction name of the transction being done
	 * @param senderAccount     the account that is performing the action
	 * @param receiverAccount   the accoun that is receiving the action
	 * @param amount            the amount of money that is being moved
	 */
	public static void transactionReaderTwoCustomerHandler(HashMap<String, Customer> data,
			HashMap<String, String> customerIds, String sender, String receiver, String transactionAction,
			String senderAccount, String receiverAccount, double amount) {
		String senderId = customerIds.get(sender);
		String receiverId = customerIds.get(receiver);
		Customer senderCustomer = data.get(senderId);
		Customer receiverCustomer = data.get(receiverId);
		switch (transactionAction) {
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
	} // END OF transactionReaderTwoCustomerHandler()

	/**
	 * This method is to facilitate the logic of two people transaction from the
	 * transaction reader
	 * 
	 * @param senderCustomer   This is a customer object the is sending money
	 * @param receiverCustomer this is a customer object that is receiving money
	 * @param senderAccount    this is the account of the sender that is being
	 *                         affected
	 * @param receiverAccount  this is the account of the receiver that is being
	 *                         affected
	 * @param amount           this is the amount of money tht is being moved
	 */
	public static void twoCustomerHandlerActions(Customer senderCustomer, Customer receiverCustomer,
			String senderAccount, String receiverAccount, double amount) {
		String caseAction = "Handler";
		switch (senderAccount) {
		case "Checking":
			senderCustomer.getCheckingAccount().sendMoney(amount, caseAction);
			if (receiverAccount.equals("Checking"))
				receiverCustomer.getCheckingAccount().receiveMoney(amount, caseAction);
			if (receiverAccount.equals("Savings"))
				receiverCustomer.getSavingsAccount().receiveMoney(amount, caseAction);
			if (receiverAccount.equals("Credit"))
				receiverCustomer.getCreditAccount().receiveMoney(amount, caseAction);
			break;
		case "Savings":
			senderCustomer.getSavingsAccount().sendMoney(amount, caseAction);
			if (receiverAccount.equals("Checking"))
				receiverCustomer.getCheckingAccount().receiveMoney(amount, caseAction);
			if (receiverAccount.equals("Savings"))
				receiverCustomer.getSavingsAccount().receiveMoney(amount, caseAction);
			if (receiverAccount.equals("Credit"))
				receiverCustomer.getCreditAccount().receiveMoney(amount, caseAction);
			break;
		default:
			System.out.println("ERROR: Account not valid");
			break;
		}
	} // END OF twoCustomerHandlerActions()

	/**
	 * This method handles the logic of retreiving a desired account(checking,
	 * savings, credit)
	 * 
	 * @param showCredit  we are checking where we want to display credit account
	 * @param inputReader handles user input
	 * @return integer depending on the desired account
	 */
	public static int getDesiredAccount(boolean showCredit, BufferedReader inputReader) { // 1. Checking 2. Savings 3.
																							// Credit
		try {
			String accountAction = "";
			boolean isDone = false;
			while (!isDone) {
				if (showCredit) {
					System.out.println("1. Checking 2. Savings 3. Credit");
					accountAction = inputReader.readLine();
					if (accountAction.equals("1"))
						return 1; // Checking
					if (accountAction.equals("2"))
						return 2; // Savings
					if (accountAction.equals("3"))
						return 3; // Credit
					else
						System.out.println("Input not recognized. Try again.");// Try again
				} else {
					System.out.println("1. Checking 2. Savings");
					accountAction = inputReader.readLine();
					if (accountAction.equals("1"))
						return 1; // Checking
					if (accountAction.equals("2"))
						return 2; // Savings
					else
						System.out.println("Input not recognized. Try again."); // Try again
				}
			}
			System.out.println("Error: No account defined");
			return -1;
		} catch (IOException e) {
			System.out.println("Error: " + e);
			return -1;
		}
	} // END OF getDesiredAccount()

	public static void createCopy(HashMap<String, Customer> data) throws IOException {// MAYBE
		// WE'LL USE
		// IT
// create the ArrayList<String> of the new lines for the file
// convert to objects again,
		BufferedReader br = new BufferedReader(new FileReader("CS 3331 - Bank Users 4.csv"));
		ArrayList<String> lines = new ArrayList<String>();

		data.entrySet().forEach(entry -> {
			lines.add(entry.getValue().getSavingsAccount().getAccountNumber() + "," + entry.getValue().getLastName()
					+ "," + entry.getValue().getCustomerID() + "," + (entry.getValue().getDateOfBirth()[0] + entry.getValue().getDateOfBirth()[1])+ "," +
					+ entry.getValue().getCheckingAccount().getAccountNumber() + ","
					+ entry.getValue().getCreditAccount().getAccountNumber() + "," + entry.getValue().getPhoneNumber()
					+ "," + entry.getValue().getCheckingAccount().getCurrentBalance() + ","
					+ entry.getValue().getSavingsAccount().getCurrentBalance() + "," + entry.getValue().getPassword()
					+ "," + entry.getValue().getCreditAccount().getCurrentBalance() + ","
					+ (entry.getValue().getAddress()[0] + entry.getValue().getAddress()[1]
							+ entry.getValue().getAddress()[2])
					+ "," + entry.getValue().getFirstName() + "," + entry.getValue().getEmail() + ","
					+ entry.getValue().getCreditAccount().getCreditLine());
		});

		try {
// creating sorted copy / printing to file:

			Collections.sort(lines, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					int s1i = Integer.parseInt(s1.substring(0, s1.indexOf(",")));
					int s2i = Integer.parseInt(s2.substring(0, s2.indexOf(",")));
					return s1i - s2i;
				}
			});

// create a BufferedWriter
			BufferedWriter outputFile = new BufferedWriter(
					new FileWriter("CS 3331 - Bank Users 4 - Copy.csv", false));// fileWriter to fals to
// overwrite the existing
// file
// read and print the first line of the last file
			outputFile.write(br.readLine());

			outputFile.newLine();// first jump to the next line;

// print the rest of the file
			for (String line : lines) {
				outputFile.write(line);
				outputFile.newLine();
			}
			outputFile.close();

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException es) {
			es.printStackTrace();
		}
	}
	public static String generatePassword(String firstName, String lastName) {
		String password = lastName + "*" + firstName + "!987";
		return password;
	}
} // END OF class
//Mickey Mouse 000-00-0001
//Donald Duck 000-00-0002